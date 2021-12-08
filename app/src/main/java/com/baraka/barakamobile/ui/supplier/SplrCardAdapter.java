package com.baraka.barakamobile.ui.supplier;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.usermanaje.WorkerCardAdapter;
import com.baraka.barakamobile.ui.util.DbConfig;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SplrCardAdapter extends RecyclerView.Adapter<SplrCardAdapter.SplrViewHolder> {
    private String URL_SPLR_IMG = DbConfig.URL_SPLR + "imgSplr/";

    private List<SplrViewModel> splrViewModelList;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListenerSplrDetail;

    String messagesWA = " ";
    String phonesWA = "";

    public interface OnItemClickListener{
        void onItemClick(int posistion);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListenerSplrDetail = listener;
    }

    public SplrCardAdapter(List<SplrViewModel> splrViewModelList, Context context){
        this.splrViewModelList = splrViewModelList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public SplrCardAdapter.SplrViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.card_supplier, parent, false);
        SplrViewHolder splrViewHolder = new SplrViewHolder(view);

        return splrViewHolder;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull @NotNull SplrCardAdapter.SplrViewHolder holder, int position) {
        Log.e("ImgSplr", "Image: "+URL_SPLR_IMG+splrViewModelList.get(position).getImgSplr());

        String imgSplrPath = splrViewModelList.get(position).getImgSplr();
        byte[] decodedString = Base64.decode(imgSplrPath, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        holder.nameSplr.setText(splrViewModelList.get(position).getNameSplr());


        Picasso.get().load(URL_SPLR_IMG+imgSplrPath)
                .fit()
                .centerInside()
                .placeholder(R.drawable.default_image_comp_small)
                .error(R.drawable.default_image_comp_small)
                .into(holder.imgSplr);


        holder.imgTlpSplr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String numbSplrList = splrViewModelList.get(position).getPhoneSplr();
                Intent CallSplrList = new Intent(Intent. ACTION_DIAL);
                CallSplrList.setData(Uri. fromParts("tel", numbSplrList,null));
                context.startActivity(CallSplrList);
            }
        });

        holder.imgWaSplr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isWhatappInstalled()){
                    Intent intentWa = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://api.whatsapp.com/send?phone="+splrViewModelList.get(position).getPhoneSplr()+
                                    "&text="+messagesWA));
                    context.startActivity(intentWa);
                }else {
                    Toast.makeText(context,"Whatsapp Tidak Ditemukan! Harap Install Whatsapp Terlebih Dahulu.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.imgEmailSplr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(Intent.ACTION_SENDTO);
                in.setData(Uri.parse("mailto:"));
                in.putExtra(Intent.EXTRA_EMAIL,new String[] {splrViewModelList.get(position).getEmailSplr()});
                in.putExtra(Intent.EXTRA_SUBJECT,"");
                in.putExtra(Intent.EXTRA_TEXT,"");

                if(in.resolveActivity(context.getPackageManager())!=null){
                    context.startActivity(in);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return splrViewModelList.size();
    }

    public class SplrViewHolder extends RecyclerView.ViewHolder {
        TextView nameSplr;
        TextView addrSplr;
        TextView phoneSplr;
        TextView emailSplr;
        TextView descSplr;
        ImageView imgSplr;
        ImageView imgTlpSplr;
        ImageView imgWaSplr;
        ImageView imgEmailSplr;

        public SplrViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            nameSplr = itemView.findViewById(R.id.textViewNameSplr);
            imgSplr = itemView.findViewById(R.id.imageViewAllSplr);
            imgTlpSplr = itemView.findViewById(R.id.imgTlpSplr);
            imgWaSplr = itemView.findViewById(R.id.imgWASplr);
            imgEmailSplr = itemView.findViewById(R.id.imgEmailSplr);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListenerSplrDetail != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            onItemClickListenerSplrDetail.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    // Whatsapp Install Check
    private boolean isWhatappInstalled(){
        PackageManager packageManager = context.getPackageManager();
        boolean whatsappInstalled;

        try {
            packageManager.getPackageInfo("com.whatsapp",PackageManager.GET_ACTIVITIES);
            whatsappInstalled = true;
        }catch (PackageManager.NameNotFoundException e){
            whatsappInstalled = false;
        }
        return whatsappInstalled;
    }
}

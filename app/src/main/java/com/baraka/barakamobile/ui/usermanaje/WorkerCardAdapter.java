package com.baraka.barakamobile.ui.usermanaje;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.product.PrdctCardAdapter;
import com.baraka.barakamobile.ui.util.DbConfig;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorkerCardAdapter extends RecyclerView.Adapter<WorkerCardAdapter.WorkerViewHolder> {

    public final static String TAG_ID = "id";
    public final static String TAG_EMAIL = "email";
    public final static String TAG_NAME = "name";
    public final static String TAG_ADDR = "address";
    public final static String TAG_LEVEL = "level";
    public final static String TAG_PHONE = "phone";
    public final static String TAG_ACCESS = "access";
    public final static String TAG_IMAGE = "imgWorker";
    public final static String TAG_IDCOMP = "idCompany";
    public final static String TAG_COMP = "nameCompany";

    private List<WorkerViewModel> workerViewModelList;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListenerWorkerDetail;

    private String URL_WORKER_IMG = DbConfig.URL + "imgUser/";

    String messagesWA = " ";
    String phonesWA = "";

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListenerWorkerDetail = listener;
    }

    public WorkerCardAdapter(List<WorkerViewModel> workerViewModelList, Context context){
        this.workerViewModelList = workerViewModelList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public WorkerCardAdapter.WorkerViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.card_worker, parent, false);
        WorkerViewHolder workerViewHolder = new WorkerViewHolder(view);

        return  workerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull WorkerCardAdapter.WorkerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nameWorker.setText(workerViewModelList.get(position).getNameUser());
        holder.lvlWorker.setText(workerViewModelList.get(position).getLvlUser());
        holder.cardWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent workerDetailIntent = new Intent(context, WorkerDetailActivity.class);
                WorkerViewModel clickedWorker = workerViewModelList.get(position);

                workerDetailIntent.putExtra(TAG_ID, clickedWorker.getIdUser());
                workerDetailIntent.putExtra(TAG_NAME, clickedWorker.getNameUser());
                workerDetailIntent.putExtra(TAG_EMAIL, clickedWorker.getEmailUser());
                workerDetailIntent.putExtra(TAG_ADDR, clickedWorker.getAddrUser());
                workerDetailIntent.putExtra(TAG_LEVEL, clickedWorker.getLvlUser());
                workerDetailIntent.putExtra(TAG_IMAGE, clickedWorker.getImgUser());
                workerDetailIntent.putExtra(TAG_PHONE, clickedWorker.getPhoneUser());
                workerDetailIntent.putExtra(TAG_ACCESS, clickedWorker.getLoginUser());

                context.startActivity(workerDetailIntent);
            }
        });
        Log.e("ImgSplr", "Image: "+URL_WORKER_IMG+workerViewModelList.get(position).getImgUser());
        Picasso.get().load(URL_WORKER_IMG+workerViewModelList.get(position).getImgUser())
                .resize(450, 450)
                .centerCrop()
                .placeholder(R.drawable.default_image_person_small)
                .error(R.drawable.default_image_person_small)
                .into(holder.imgWorker);

        holder.imgTlpWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numb = workerViewModelList.get(position).getPhoneUser();
                Intent CallWorker = new Intent(Intent. ACTION_DIAL);
                CallWorker.setData(Uri. fromParts("tel", numb,null));
                context.startActivity(CallWorker);
            }
        });
        holder.imgWaWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isWhatappInstalled()){
                    Intent intentWa = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://api.whatsapp.com/send?phone="+workerViewModelList.get(position).getPhoneUser()+
                            "&text="+messagesWA));
                    context.startActivity(intentWa);
                }else {
                    Toast.makeText(context,"Whatsapp Tidak Ditemukan! Harap Install Whatsapp Terlebih Dahulu.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.imgEmailWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(Intent.ACTION_SENDTO);
                in.setData(Uri.parse("mailto:"));
                in.putExtra(Intent.EXTRA_EMAIL,new String[] {workerViewModelList.get(position).getEmailUser()});
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
        return workerViewModelList.size();
    }

    public class WorkerViewHolder extends RecyclerView.ViewHolder {
        ImageView imgWorker;
        TextView emailWorker;
        TextView nameWorker;
        TextView lvlWorker;
        TextView phoneUser;
        TextView loginUser;
        TextView compUser;
        TextView nameComp;
        ImageView imgTlpWorker;
        ImageView imgWaWorker;
        ImageView imgEmailWorker;
        CardView cardWorker;

        public WorkerViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            nameWorker = itemView.findViewById(R.id.textViewNameWorker);
            lvlWorker = itemView.findViewById(R.id.textViewJabatan);
            imgWorker = itemView.findViewById(R.id.imgWorker);
            imgTlpWorker = itemView.findViewById(R.id.imgTlpWorker);
            imgWaWorker = itemView.findViewById(R.id.imgWAWorker);
            imgEmailWorker = itemView.findViewById(R.id.imgEmailWorker);
            cardWorker = itemView.findViewById(R.id.cardWorker);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (onItemClickListenerWorkerDetail != null){
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION){
//                            onItemClickListenerWorkerDetail.onItemClick(position);
//                        }
//                    }
//                }
//            });
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

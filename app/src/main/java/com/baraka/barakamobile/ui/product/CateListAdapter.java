package com.baraka.barakamobile.ui.product;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.util.DbConfig;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CateListAdapter extends RecyclerView.Adapter<CateListAdapter.MyViewHolder> {

    private String URL_CATE_IMG = DbConfig.URL_CATE + "imgCat/";
    private List<CateViewModel> cateViewModelList;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListenerCatDetail;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListenerCatDetail = listener;
    }

    public CateListAdapter(Context context, List<CateViewModel>cateViewModelList){
        this.cateViewModelList = cateViewModelList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.card_list_cate, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        holder.nameCateList.setText(cateViewModelList.get(position).getNameCat());
        holder.descCateList.setText(cateViewModelList.get(position).getDescCat());

        Picasso.get().load(URL_CATE_IMG+cateViewModelList.get(position).getImgCat())
                .resize(450, 450)
                .centerCrop()
                .placeholder(R.drawable.default_image_small)
                .error(R.drawable.default_image_small)
                .into(holder.imgCateList);

        Log.e("ImgSplr", "Image: "+URL_CATE_IMG+cateViewModelList.get(position).getImgCat());
    }

    @Override
    public int getItemCount() {
        return cateViewModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameCateList;
        TextView descCateList;
        ImageView imgCateList;
        CardView cardCateList;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            nameCateList = itemView.findViewById(R.id.textViewNameCateList);
            descCateList = itemView.findViewById(R.id.textViewDescCateList);
            imgCateList = (ImageView) itemView.findViewById(R.id.imgCatList);
            cardCateList = (CardView) itemView.findViewById(R.id.cardCateList);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListenerCatDetail != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            onItemClickListenerCatDetail.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}

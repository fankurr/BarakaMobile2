package com.baraka.barakamobile.ui.product;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.supplier.SplrCardAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CateAdapterAddEditPrdct extends RecyclerView.Adapter<CateAdapterAddEditPrdct.MyViewHolder> {

    public static final String ID_CATE = "idCat";
    public static final String ID_CATE_INT = "idCate";
    public static final String ID_COMP_CATE = "idCompCat";
    public static final String NAME_CATE = "nameCat";
    public static final String DESC_CATE = "descCat";
    public static final String IMG_CATE = "imgCat";

    String idCateIntent, nameCateIntent;

    List<CateViewModel> cateViewModelList;
    Context context;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListenerCateEdit;

//    public interface OnItemClickListener{
//        void onItemClick(int posistion);
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener){
//        onItemClickListenerCateEdit = listener;
//    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listenerCateEdit){
        onItemClickListenerCateEdit = listenerCateEdit;
    }

    public CateAdapterAddEditPrdct(Context context, List<CateViewModel>cateViewModelList){
        this.cateViewModelList = cateViewModelList;
        this.context = context;
    }

    @Override
    public CateAdapterAddEditPrdct.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_cate, parent, false);
        CateAdapterAddEditPrdct.MyViewHolder holder = new CateAdapterAddEditPrdct.MyViewHolder(view);
        return new CateAdapterAddEditPrdct.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CateAdapterAddEditPrdct.MyViewHolder holder, int position) {
        holder.idCateList.setText(cateViewModelList.get(position).getIdCat());
        holder.nameCateList.setText(cateViewModelList.get(position).getNameCat());
        holder.descCateList.setText(cateViewModelList.get(position).getDescCat());
        holder.cardCateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intentCate = new Intent(context, AddEditPrdctActivity.class);
//                CateViewModel cateViewModel = cateViewModelList.get(position);
//
//                intentCate.putExtra(ID_CATE, String.valueOf(cateViewModel.getIdCat()));
//                intentCate.putExtra(NAME_CATE, cateViewModel.getNameCat());
//
//                context.startActivity(intentCate);
//                ((Activity)context).finish();

                Intent intentCate = new Intent();
                CateViewModel cateViewModel = cateViewModelList.get(position);

                idCateIntent = cateViewModel.getIdCat();
                nameCateIntent = cateViewModel.getNameCat();

                intentCate.putExtra(ID_CATE_INT, idCateIntent);
                intentCate.putExtra(NAME_CATE, nameCateIntent);
                ((Activity)context).setResult(Activity.RESULT_OK, intentCate);
                ((Activity)context).finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return cateViewModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView idCateList;
        public TextView nameCateList;
        public TextView descCateList;
        public CardView cardCateList;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            idCateList = (TextView) itemView.findViewById(R.id.textViewIdCateList);
            nameCateList = (TextView) itemView.findViewById(R.id.textViewNameCateList);
            descCateList = (TextView) itemView.findViewById(R.id.textViewDescCateList);
            cardCateList = (CardView) itemView.findViewById(R.id.cardCateList);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListenerCateEdit != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            onItemClickListenerCateEdit.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}

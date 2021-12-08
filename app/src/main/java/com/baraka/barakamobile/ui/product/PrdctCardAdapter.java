package com.baraka.barakamobile.ui.product;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.baraka.barakamobile.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class PrdctCardAdapter extends RecyclerView.Adapter<PrdctCardAdapter.PrdctViewHolder> {
    private List<PrdctViewModel> prdctViewModelList;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListenerPrdctDetail;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListenerPrdctDetail = listener;
    }


    public PrdctCardAdapter(List<PrdctViewModel> prdctViewModelList, Context context){
        this.prdctViewModelList = prdctViewModelList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public PrdctViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.card_prdct, parent, false);
        PrdctViewHolder prdctViewHolder = new PrdctViewHolder(view);

        return prdctViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PrdctViewHolder holder, int position) {
        holder.namePrdct.setText(prdctViewModelList.get(position).getNamePrdct());
        holder.unitPrice.setText(prdctViewModelList.get(position).getUnitPrice());
        holder.unitPrdct.setText(prdctViewModelList.get(position).getUnitPrdct());
        holder.stockPrct.setText(prdctViewModelList.get(position).getStockPrdct());
    }

    @Override
    public int getItemCount() {
        return prdctViewModelList.size();
    }

    public class PrdctViewHolder extends RecyclerView.ViewHolder {
        TextView namePrdct;
        TextView unitPrice;
        TextView unitPrdct;
        TextView stockPrct;
        CardView cardView;
        public PrdctViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            namePrdct = itemView.findViewById(R.id.textViewNamePrdct);
            unitPrice = itemView.findViewById(R.id.textViewUnitPrice);
            unitPrdct = itemView.findViewById(R.id.textViewUnitPrdct);
            stockPrct = itemView.findViewById(R.id.textViewStockPrdct);
            cardView = (CardView) itemView.findViewById(R.id.cardPrdct);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListenerPrdctDetail != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            onItemClickListenerPrdctDetail.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}

package com.baraka.barakamobile.ui.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baraka.barakamobile.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CateCardAdpater extends RecyclerView.Adapter<CateCardAdpater.MyViewHolder> {

    List<CateViewModel> cateViewModelList;
    Context context;

    public CateCardAdpater(Context context, List<CateViewModel>cateViewModelList){
        this.cateViewModelList = cateViewModelList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CateCardAdpater.MyViewHolder holder, int position) {
        holder.nameCate.setText(cateViewModelList.get(position).getNameCat());
    }

    @Override
    public int getItemCount() {
        return cateViewModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameCate;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            nameCate = (TextView) itemView.findViewById(R.id.textViewNameCat);
        }
    }
}

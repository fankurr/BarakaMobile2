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

import java.util.List;

public class CateListAdapter extends RecyclerView.Adapter<CateListAdapter.MyViewHolder> {

    List<CateViewModel> cateViewModelList;
    Context context;

    public CateListAdapter(Context context, List<CateViewModel>cateViewModelList){
        this.cateViewModelList = cateViewModelList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public CateListAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_cate, parent, false);
        CateListAdapter.MyViewHolder holder = new CateListAdapter.MyViewHolder(view);
        return new CateListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CateListAdapter.MyViewHolder holder, int position) {
        holder.nameCateList.setText(cateViewModelList.get(position).getNameCat());
        holder.descCateList.setText(cateViewModelList.get(position).getDescCat());

    }

    @Override
    public int getItemCount() {
        return cateViewModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameCateList;
        public TextView descCateList;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            nameCateList = (TextView) itemView.findViewById(R.id.textViewNameCateList);
            descCateList = (TextView) itemView.findViewById(R.id.textViewDescCateList);
        }
    }
}

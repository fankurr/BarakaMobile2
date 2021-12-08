package com.baraka.barakamobile.ui.sell;

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

public class TxCardAdapter extends RecyclerView.Adapter<TxCardAdapter.TxViewHolder> {

    List<TxViewModel> txViewModelList;
    Context context;

    public TxCardAdapter(Context context, List<TxViewModel> txViewModelList){
        this.txViewModelList = txViewModelList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public TxCardAdapter.TxViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tx, parent, false);
        TxViewHolder txViewHolder = new TxViewHolder(view);
        return new TxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TxCardAdapter.TxViewHolder holder, int position) {
        holder.namePrdctTx.setText(txViewModelList.get(position).getNamePrdct());
        holder.qtyTx.setText(txViewModelList.get(position).getQtyTx());
        holder.unitTx.setText(txViewModelList.get(position).getUnitPrdct());
        holder.datetimeTx.setText(txViewModelList.get(position).getDatetimeTx());
    }

    @Override
    public int getItemCount() {
        return txViewModelList.size();
    }

    public class TxViewHolder extends RecyclerView.ViewHolder {
        public TextView namePrdctTx;
        public TextView qtyTx;
        public TextView unitTx;
        public TextView datetimeTx;

        public TxViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            namePrdctTx = (TextView) itemView.findViewById(R.id.textViewNamePrdctTx);
            qtyTx = (TextView) itemView.findViewById(R.id.textViewQtyTx);
            unitTx = (TextView) itemView.findViewById(R.id.textViewUnitTx);
            datetimeTx = (TextView) itemView.findViewById(R.id.textViewDateTimeTx);
        }
    }
}

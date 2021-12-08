package com.baraka.barakamobile.ui.payout;

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

public class PayoutCardAdapter extends RecyclerView.Adapter<PayoutCardAdapter.PayoutViewHolder> {

    List<PayoutViewModelList> payoutViewModelLists;
    Context context;

    public PayoutCardAdapter(Context context, List<PayoutViewModelList> payoutViewModelLists){
        this.payoutViewModelLists = payoutViewModelLists;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public PayoutCardAdapter.PayoutViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_payout, parent, false);
        PayoutViewHolder payoutViewHolder = new PayoutViewHolder(view);
        return new PayoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PayoutCardAdapter.PayoutViewHolder holder, int position) {
        holder.valpayout.setText(payoutViewModelLists.get(position).getValPay());
        holder.descPayout.setText(payoutViewModelLists.get(position).getDescPay());
        holder.datetimePayout.setText(payoutViewModelLists.get(position).getDatetimePay());
        holder.signPayout.setText(payoutViewModelLists.get(position).getNameUser());
    }

    @Override
    public int getItemCount() {
        return payoutViewModelLists.size();
    }

    public class PayoutViewHolder extends RecyclerView.ViewHolder {
        public TextView valpayout;
        public TextView descPayout;
        public TextView datetimePayout;
        public TextView signPayout;

        public PayoutViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            valpayout = (TextView) itemView.findViewById(R.id.textViewValPayoutList);
            descPayout = (TextView) itemView.findViewById(R.id.textViewDescPayoutList);
            datetimePayout = (TextView) itemView.findViewById(R.id.textViewDAteTimePayoutList);
            signPayout = (TextView) itemView.findViewById(R.id.textViewSignByPayoutList);
        }
    }
}

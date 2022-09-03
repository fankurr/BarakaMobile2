package com.baraka.barakamobile.ui.sell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.util.DbConfig;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TxCardAdapter extends RecyclerView.Adapter<TxCardAdapter.TxViewHolder> {

    List<TxViewModel> txViewModelList;
    Context context;
    private String URL_PRDCT_IMG = DbConfig.URL_PRDCT + "imgPrdct/";

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

        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("In","ID"));
        double formatRpTx = Double.parseDouble(txViewModelList.get(position).getValueTx());
        holder.valTx.setText(formatRupiah.format(formatRpTx));

        holder.datetimeTx.setText(txViewModelList.get(position).getDatetimeTx());

        Picasso.get().load(URL_PRDCT_IMG+txViewModelList.get(position).getImgPrdct())
                .resize(450, 450)
                .centerCrop()
                .placeholder(R.drawable.default_image_comp_small)
                .error(R.drawable.default_image_comp_small)
                .into(holder.imgPrdctTx);
    }

    @Override
    public int getItemCount() {
        return txViewModelList.size();
    }

    public class TxViewHolder extends RecyclerView.ViewHolder {
        public TextView namePrdctTx;
        public TextView qtyTx;
        public TextView unitTx;
        public TextView valTx;
        public TextView datetimeTx;
        public ImageView imgPrdctTx;

        public TxViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            namePrdctTx = (TextView) itemView.findViewById(R.id.textViewNamePrdctTx);
            qtyTx = (TextView) itemView.findViewById(R.id.textViewQtyTx);
            unitTx = (TextView) itemView.findViewById(R.id.textViewUnitTx);
            valTx = (TextView) itemView.findViewById(R.id.textViewValTx);
            datetimeTx = (TextView) itemView.findViewById(R.id.textViewDateTimeTx);
            imgPrdctTx = (ImageView) itemView.findViewById(R.id.imgPrdctTx);
        }
    }
}

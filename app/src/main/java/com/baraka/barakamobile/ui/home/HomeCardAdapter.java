package com.baraka.barakamobile.ui.home;

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

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HomeCardAdapter extends RecyclerView.Adapter<HomeCardAdapter.HomeViewHolder> {

    List<HomeViewModelList> homeViewModelLists;
    Context contextHome;
    private String URL_PRDCT_IMG = DbConfig.URL_PRDCT + "imgPrdct/";

    final int EMPTY_VIEW = 77777;

    NumberFormat rupiah;

    public HomeCardAdapter(Context contextHome, List<HomeViewModelList> homeViewHolderList){
        this.homeViewModelLists = homeViewHolderList;
        this.contextHome = contextHome;
    }

    @NonNull
    @Override
    public HomeCardAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_home, parent, false);
        HomeViewHolder homeViewHolder = new HomeViewHolder(view);


        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCardAdapter.HomeViewHolder holder, int position) {
        holder.title.setText(homeViewModelLists.get(position).getNamePrdct());
//        holder.title.setText(homeViewModelLists.get(position).getDescPay());

        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("In","ID"));
        double formatRpHome = Double.parseDouble(homeViewModelLists.get(position).getValueTx());

        holder.value.setText(formatRupiah.format(formatRpHome));
//        holder.value.setText(homeViewModelLists.get(position).getValPay());


        holder.dateTime.setText(homeViewModelLists.get(position).getDatetimeTx());
//        holder.dateTime.setText(homeViewModelLists.get(position).getDatetimePay());

        Picasso.get().load(URL_PRDCT_IMG+homeViewModelLists.get(position).getImgPrdct())
                .resize(450, 450)
                .centerCrop()
                .placeholder(R.drawable.default_image_small)
                .error(R.drawable.default_image_small)
                .into(holder.imgTxHome);


    }

    @Override
    public int getItemCount() {
        return homeViewModelLists.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView value;
        public TextView dateTime;
        public ImageView imgTxHome;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.textViewTitleHome);
            value = (TextView) itemView.findViewById(R.id.textViewValHome);
            dateTime = (TextView) itemView.findViewById(R.id.textViewDateTimeHome);
            imgTxHome = (ImageView) itemView.findViewById(R.id.imgTxHome);
        }
    }
}

package com.baraka.barakamobile.ui.pos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.product.PrdctCardAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class POSCardAdapter extends RecyclerView.Adapter<POSCardAdapter.POSViewHolder> {

    public static final String ID_PRDCT = "idPrdct";
    public static final String NAME_PRDCT = "namePrdct";
    public static final String CODE_PRDCT = "codePrdct";
    public static final String CATE_PRDCT = "nameCat";
    public static final String SPLR_PRDCT = "nameSplr";
    public static final String DESC_PRDCT = "descPrdct";
    public static final String PRICE_PRDCT = "unitPrice";
    public static final String UNIT_PRDCT = "unitPrdct";
    public static final String STOCK_PRDCT = "stockPrdct";

    private ArrayList<POSViewModel> posViewModelList;
    private POSViewModel posViewModel;
    private Context context;
    private LayoutInflater layoutInflater;
    private PrdctCardAdapter.OnItemClickListener onItemClickListenerPosInput;

    public ArrayList<POSOutputViewModel> posOutputArrayList;
    POSOutputAdapter posOutputAdapter;


//    public interface OnItemClickListener {
//        void onItemClick(int position);
//    }
//
//    public void setOnItemClickListener(PrdctCardAdapter.OnItemClickListener listener){
//        onItemClickListenerPosInput = listener;
//    }

    public POSCardAdapter(ArrayList<POSViewModel> posViewModelList, Context context){
        this.posViewModelList = posViewModelList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public POSCardAdapter.POSViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.card_pos_prdct, parent, false);
        POSViewHolder posViewHolder = new POSViewHolder(view);

        return posViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull POSCardAdapter.POSViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtIdPrdct.setText(String.valueOf(posViewModelList.get(position).getIdPrdct()));
        holder.namePrdct.setText(posViewModelList.get(position).getNamePrdct());
        holder.unitPrice.setText(posViewModelList.get(position).getUnitPrice());
        holder.unitPrdct.setText(posViewModelList.get(position).getUnitPrdct());
        holder.stockPrct.setText(posViewModelList.get(position).getStockPrdct());
//        holder.cardPos.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                Intent inputPosIntent = new Intent(context, InputPOSActivity.class);
//                POSViewModel posViewModel = posViewModelList.get(position);
//
//                inputPosIntent.putExtra(ID_PRDCT, String.valueOf(posViewModel.getIdPrdct()));
//                inputPosIntent.putExtra(NAME_PRDCT, posViewModel.getNamePrdct());
//                inputPosIntent.putExtra(PRICE_PRDCT, posViewModel.getUnitPrice());
//                inputPosIntent.putExtra(UNIT_PRDCT, posViewModel.getUnitPrdct());
//                inputPosIntent.putExtra(STOCK_PRDCT, posViewModel.getStockPrdct());
//
//                context.startActivity(inputPosIntent);
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return posViewModelList.size();
    }

    public class POSViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPOSPrdct;
        TextView txtIdPrdct,namePrdct;
        TextView unitPrice;
        TextView unitPrdct;
        TextView stockPrct;
        CardView cardPos;

        public POSViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imgPOSPrdct = itemView.findViewById(R.id.imgPOSPrdct);
            txtIdPrdct = itemView.findViewById(R.id.txtIdPrdct);
            namePrdct = itemView.findViewById(R.id.textViewPOSPrdct);
            unitPrice = itemView.findViewById(R.id.textViewPOSUnitPrice);
            unitPrdct = itemView.findViewById(R.id.textViewUnitPrdctPosCard);
            stockPrct = itemView.findViewById(R.id.textViewPOSStock);
            cardPos = itemView.findViewById(R.id.cardPosPrdct);

        }
    }


}

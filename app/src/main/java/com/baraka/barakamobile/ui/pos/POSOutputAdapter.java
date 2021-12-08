package com.baraka.barakamobile.ui.pos;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baraka.barakamobile.R;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class POSOutputAdapter extends RecyclerView.Adapter<POSOutputAdapter.POSOutputViewHolder> {

    private final List<POSOutputViewModel> posOutputViewModelList;
    private Activity activity;
    private LayoutInflater inflater;
//    private List<POSOutputViewModel> posOutputViewModelList;
//    private ArrayList<String> posOutputViewModelList;
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<POSViewModel> posViewModelList;
    private static ArrayList<POSOutputViewModel> printView = new ArrayList<>();

    POSOutputAdapter posOutputAdapter;

    public POSOutputAdapter(Context context, List<POSOutputViewModel> posOutputViewModelList) {
        this.context = context;
        this.posOutputViewModelList = posOutputViewModelList;
    }

//    public POSOutputAdapter(ArrayList<POSOutputViewModel> posOutputViewModelList, Context context){
//        this.posOutputViewModelList = posOutputViewModelList;
//        this.context = context;
//    }

    @NonNull
    @NotNull
    @Override
    public POSOutputAdapter.POSOutputViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.card_pos_output, parent, false);
        POSOutputViewHolder posOutputViewHolder = new POSOutputViewHolder(view);

        return posOutputViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull POSOutputAdapter.POSOutputViewHolder holder, int position) {

        holder.IdPrdctOutput.setText(posOutputViewModelList.get(position).getIdPrdct());
        holder.namePrdctOutput.setText(posOutputViewModelList.get(position).getNamePrdct());
        holder.pricePrdctOutput.setText(posOutputViewModelList.get(position).getUnitPrice());
        holder.jmlhPrdctOutput.setText(posOutputViewModelList.get(position).getStockPrdct());
        holder.unitPrdctOutput.setText(posOutputViewModelList.get(position).getUnitPrdct());
        holder.pricePrdctOutputTotal.setText(posOutputViewModelList.get(position).getTotal());
        holder.delItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posOutputViewModelList.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
//        if (posOutputViewModelList == null){
//            return 0;
//        }
        return posOutputViewModelList.size();
    }

    public class POSOutputViewHolder extends RecyclerView.ViewHolder {
        TextView IdPrdctOutput,namePrdctOutput;
        TextView pricePrdctOutput;
        TextView unitPrdctOutput;
        TextView jmlhPrdctOutput;
        TextView pricePrdctOutputTotal;
        ImageView delItemBtn;

        public POSOutputViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            IdPrdctOutput = itemView.findViewById(R.id.txtIdPrdctPosOutput);
            namePrdctOutput = itemView.findViewById(R.id.txtViewNamePrdctPosOutput);
            pricePrdctOutput = itemView.findViewById(R.id.txtViewPricePrdctPosOutput);
            unitPrdctOutput = itemView.findViewById(R.id.txtViewUnitPrdctPosOutput);
            jmlhPrdctOutput = itemView.findViewById(R.id.txtViewJmlhPrdctPosOutput);
            pricePrdctOutputTotal = itemView.findViewById(R.id.txtViewPricePrdctPosOutputTotal);
            delItemBtn = itemView.findViewById(R.id.imgDelItemPosOutput);


        }
    }

    public static ArrayList<POSOutputViewModel> getPrintView() {
        return printView;
    }

}

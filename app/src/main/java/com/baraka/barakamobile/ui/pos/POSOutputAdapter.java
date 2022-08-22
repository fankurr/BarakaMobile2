package com.baraka.barakamobile.ui.pos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.util.DbConfig;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class POSOutputAdapter extends RecyclerView.Adapter<POSOutputAdapter.POSOutputViewHolder> {

    private String delTX = DbConfig.URL_TX + "delTx.php";

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
    public void onBindViewHolder(@NonNull @NotNull POSOutputAdapter.POSOutputViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.IdPrdctOutput.setText(posOutputViewModelList.get(position).getIdPrdct());
        holder.namePrdctOutput.setText(posOutputViewModelList.get(position).getNamePrdct());
        holder.pricePrdctOutput.setText(posOutputViewModelList.get(position).getUnitPrice());
        holder.jmlhPrdctOutput.setText(posOutputViewModelList.get(position).getQtyPrdct());
        holder.unitPrdctOutput.setText(posOutputViewModelList.get(position).getUnitPrdct());
        holder.pricePrdctOutputTotal.setText(posOutputViewModelList.get(position).getTotal());
        holder.delItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, posOutputViewModelList.getId_riwayat(), Toast.LENGTH_SHORT).show();
//                String idRiwayat = posOutputViewModelList.getId_riwayat();
//                showDialogHapusRiwayat(idRiwayat, position);
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

    public void showDialogHapusRiwayat(String idTx, int position){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Hapus Riwayat Diagnosa Ini");
//        alertDialog.setMessage("Message");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Hapus",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        delTx(idTx, position);
                        dialog.dismiss();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Tidak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        dialog.cancel();
                    }
                });

        alertDialog.show();

        Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        LinearLayout.LayoutParams PositiveButton = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        PositiveButton.weight = 100;
        PositiveButton.gravity = Gravity.CENTER;
        btnPositive.setLayoutParams(PositiveButton);

        LinearLayout.LayoutParams NegativeButton = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        NegativeButton.weight = 100;
        NegativeButton.gravity = Gravity.CENTER;
        btnNegative.setLayoutParams(NegativeButton);

    }

    public void delTx(String idTx, int position) {
        AndroidNetworking.post(delTX)
                .addBodyParameter("idTx", idTx)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            //mengambil pesan dari json
                            String pesan = response.getString("pesan");

                            if(pesan.equalsIgnoreCase("Sukses hapus pesanan")) {
                                Toast.makeText(context, "Pesanan Berhasil dihapus", Toast.LENGTH_SHORT).show();
                                posOutputViewModelList.remove(position);
                                notifyDataSetChanged();
//                                context.startActivity(new Intent(context, MainActivity.class));
                            } else {
                                Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //gagal terhubung ke database
//                            progressDialog.dismiss();
                            Toast.makeText(context, "Gagal menghapus", Toast.LENGTH_SHORT).show();
                        }
//                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
//                        progressDialog.dismiss();
                        Toast.makeText(context, "Koneksi Gagal : Gagal menghapus ", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static ArrayList<POSOutputViewModel> getPrintView() {
        return printView;
    }

}

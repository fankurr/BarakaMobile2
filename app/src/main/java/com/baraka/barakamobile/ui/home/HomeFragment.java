package com.baraka.barakamobile.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.databinding.FragmentHomeBinding;
import com.baraka.barakamobile.ui.profile.CompProActivity;
import com.baraka.barakamobile.ui.sell.TxCardAdapter;
import com.baraka.barakamobile.ui.sell.TxViewModel;
import com.baraka.barakamobile.ui.util.DbConfig;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {


    private String URL_DASHBOARD = DbConfig.URL_HOME + "allHome.php";
    private String URL_DASHBOARD_PAY = DbConfig.URL_HOME + "totalPay.php";
    private String URL_DASHBOARD_TX = DbConfig.URL_HOME + "totalSell.php";

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView imageView;
    private RecyclerView rViewHome;
    private RecyclerView.LayoutManager layoutManager;
    private HomeCardAdapter homeCardAdapter;

    ProgressBar progressBar;
    View view;

    private final static String TAG_ID = "id";
    private final static String TAG_EMAIL = "email";
    private final static String TAG_NAME = "name";
    private final static String TAG_LEVEL = "level";
    private final static String TAG_ACCESS = "access";
    private final static String TAG_IDCOMP = "idCompany";
    private final static String TAG_COMP = "nameCompany";

    public static final String ID_TX = "idTx";
    public static final String COMP_TX= "idCompTx";
    public static final String PRDCT_TX= "idPrdctTx";
    public static final String QTY_TX = "qtyTx ";
    public static final String DATETIME_TX = "datetimeTx";

    public static final String ID_COMP = "idComp";
    public static final String NAME_COMP = "nameComp";
    public static final String ADDR_COMP = "addrComp";
    public static final String PHONE_COMP = "phoneComp";
    public static final String EMAIL_COMP = "emailComp";
    public static final String LOGO_COMP = "logoComp";

    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";
    private String URL_PRDCT_IMG = DbConfig.URL_PRDCT + "imgPrdct/";

    List<HomeViewModelList> homeViewModelLists;

    TextView txtHomeTotalPayout, txtHomeTotalSell;

    NumberFormat rupiah;

    String id, email, name, level, access, idCompany, nameCompany;
    String idPay, idCompPay,valPay,descPay, datetimePay, signPay;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_home, container, false);

        setHasOptionsMenu(true);

        sharedPreferences = this.getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedPreferences.getString(TAG_ID, null);
        email = sharedPreferences.getString(TAG_EMAIL, null);
        name = sharedPreferences.getString(TAG_NAME, null);
        level = sharedPreferences.getString(TAG_LEVEL, null);
        access = sharedPreferences.getString(TAG_ACCESS, null);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);
        nameCompany = sharedPreferences.getString(TAG_COMP, null);


        txtHomeTotalPayout = (TextView) view.findViewById(R.id.txtHomeTotalPayout);
        txtHomeTotalSell = (TextView) view.findViewById(R.id.txtHomeTotalSell);

        rupiah = NumberFormat.getNumberInstance(new Locale("In", "ID"));

        rViewHome = (RecyclerView) view.findViewById(R.id.recyclerViewHome);
        rViewHome.setLayoutManager(new LinearLayoutManager(getContext()));
        rViewHome.setHasFixedSize(true);
        homeViewModelLists = new ArrayList<>();
        homeCardAdapter = new HomeCardAdapter(getContext(), homeViewModelLists);
        rViewHome.setAdapter(homeCardAdapter);

        getHome();
        txHome();
        payoutHome();

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.SwipeRefreshHome);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItem();
            }

            private void refreshItem() {
                txHome();
                payoutHome();

                //Fetch Data TX
                getHome();

                onItemLoad();

            }

            private void onItemLoad() {

                swipeRefreshLayout.setRefreshing(false);

            }
        });

        return view;
    }

    // Detail CompPro
    public void txHome(){

        AndroidNetworking.post(URL_DASHBOARD_TX)
                .addBodyParameter("idCompTx", idCompany.toString())
                .setTag("Load Data..")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            int success = response.getInt("success");
                            if (success==1){
                                JSONArray jsonArray = response.getJSONArray("data"); // mengambil [data] dari json
//                                Log.d("idUser", jsonArray.getJSONObject(0).getString("idUser")); //mengambil data username dari json yg sudah diinput

                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                if(jsonObject.getString("sumValTx") == "null"){
                                    txtHomeTotalSell.setText("000");
                                }else{
                                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("In","ID"));
                                    double formatRpSumValTx = Double.parseDouble(jsonObject.getString("sumValTx"));

                                    txtHomeTotalSell.setText(formatRupiah.format(formatRpSumValTx));
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Maaf, gagal Terhubung ke Database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());

                    }
                });
    }

    // Detail CompPro
    public void payoutHome(){

        AndroidNetworking.post(URL_DASHBOARD_PAY)
                .addBodyParameter("idCompPay", idCompany.toString())
                .setTag("Load Data..")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            int success = response.getInt("success");
                            if (success==1){
                                JSONArray jsonArray = response.getJSONArray("data"); // mengambil [data] dari json
//                                Log.d("idUser", jsonArray.getJSONObject(0).getString("idUser")); //mengambil data username dari json yg sudah diinput

                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                if(jsonObject.getString("sumValPay") == "null"){
                                    txtHomeTotalPayout.setText("000");
                                }else{
                                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("In","ID"));
                                    double formatRpSumValPay = Double.parseDouble(jsonObject.getString("sumValPay"));

                                    txtHomeTotalPayout.setText(formatRupiah.format(formatRpSumValPay));

                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Maaf, gagal Terhubung ke Database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());

                    }
                });
    }


    private void getHome() {
        ProgressDialog progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data..");
        progressDialog.show();

        sharedPreferences = this.getActivity().getSharedPreferences(my_shared_preferences,Context.MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        AndroidNetworking.post(URL_DASHBOARD)
                .addBodyParameter("idCompTx", idCompany)
                .addBodyParameter("idCompPay", idCompany)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        homeViewModelLists.clear();

                        Log.i("Info", "Data: " + response.toString());
                        try {
                            int status = response.getInt("code");
                            if (status == 1) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    HomeViewModelList homeViewModelList = new HomeViewModelList(
                                            jsonObject.getInt("idTx"),
                                            jsonObject.getInt("idCompTx"),
                                            jsonObject.getString("namePrdct"),
                                            jsonObject.getString("imageProduct"),
                                            jsonObject.getString("qtyTx"),
                                            jsonObject.getString("valueTx"),
                                            jsonObject.getString("datetimeTx"),
                                            jsonObject.getString("dt")

                                    );
                                    homeViewModelLists.add(homeViewModelList);
                                    HomeCardAdapter homeCardAdapter = new HomeCardAdapter(getContext(), homeViewModelLists);
                                    rViewHome.setAdapter(homeCardAdapter);
//                                txCardAdapter.setOnItemClickListener(SupplierFragment.this);
                                    progressDialog.dismiss();
                                }
                            }
                            if (status == 0) {
                                Toast.makeText(getContext(), "Anda Belum Memiliki Data Transaksi Terbaru!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        homeCardAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
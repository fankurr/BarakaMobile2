package com.baraka.barakamobile.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.baraka.barakamobile.ui.sell.TxCardAdapter;
import com.baraka.barakamobile.ui.sell.TxViewModel;
import com.baraka.barakamobile.ui.util.DbConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


    private String URL_DASHBOARD = DbConfig.URL_HOME + "allHome.php";

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

    List<HomeViewModelList> homeViewModelLists;

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


        rViewHome = (RecyclerView) view.findViewById(R.id.recyclerViewHome);
        rViewHome.setLayoutManager(new LinearLayoutManager(getContext()));
        rViewHome.setHasFixedSize(true);
        homeViewModelLists = new ArrayList<>();
        homeCardAdapter = new HomeCardAdapter(getContext(), homeViewModelLists);
        rViewHome.setAdapter(homeCardAdapter);

        getHome();

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.SwipeRefreshHome);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItem();
            }

            private void refreshItem() {
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
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                HomeViewModelList homeViewModelList = new HomeViewModelList(
                                        jsonObject.getInt("idTx"),
                                        jsonObject.getInt("idCompTx"),
                                        jsonObject.getString("namePrdct"),
                                        jsonObject.getString("qtyTx"),
                                        jsonObject.getString("valueTx"),
                                        jsonObject.getString("datetimeTx"),
                                        jsonObject.getString("idPay"),
                                        jsonObject.getString("valuePay"),
                                        jsonObject.getString("descPay"),
                                        jsonObject.getString("datetimePay"),
                                        jsonObject.getString("signPay")

                                );
                                homeViewModelLists.add(homeViewModelList);
                                HomeCardAdapter homeCardAdapter = new HomeCardAdapter(getContext(), homeViewModelLists);
                                rViewHome.setAdapter(homeCardAdapter);
//                                txCardAdapter.setOnItemClickListener(SupplierFragment.this);
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
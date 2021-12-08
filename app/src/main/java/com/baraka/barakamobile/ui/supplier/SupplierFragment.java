package com.baraka.barakamobile.ui.supplier;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.databinding.FragmentSupplierBinding;
import com.baraka.barakamobile.ui.product.PrdctCardAdapter;
import com.baraka.barakamobile.ui.product.PrdctViewModel;
import com.baraka.barakamobile.ui.product.ProductFragment;
import com.baraka.barakamobile.ui.usermanaje.WorkerDetailActivity;
import com.baraka.barakamobile.ui.usermanaje.WorkerViewModel;
import com.baraka.barakamobile.ui.util.DbConfig;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SupplierFragment extends Fragment implements SplrCardAdapter.OnItemClickListener {

    private SupplierViewModel supplierViewModel;
//    private FragmentSupplierBinding fragmentSupplierBinding;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SplrCardAdapter splrCardAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String URL_SPLR = DbConfig.URL_SPLR + "allSplr.php";

    private List<SplrViewModel> splrViewModelList;

    public static final String ID_SPLR = "idSplr";
    public static final String COMP_SPLR= "idCompSplr";
    public static final String NAME_SPLR= "nameSplr";
    public static final String ADDR_SPLR = "addrSplr";
    public static final String PHONE_SPLR = "phoneSplr";
    public static final String EMAIL_SPLR = "emailSplr";
    public static final String DESC_SPLR = "descSplr";
    public static final String IMG_SPLR = "imgSplr";

    public static final String ID_COMP = "idComp";
    public static final String NAME_COMP = "nameComp";
    public static final String ADDR_COMP = "addrComp";
    public static final String PHONE_COMP = "phoneComp";
    public static final String EMAIL_COMP = "emailComp";
    public static final String LOGO_COMP = "logoComp";


//    private final static String TAG_ID = "idUser";
//    private final static String TAG_EMAIL = "emailUser";
//    private final static String TAG_NAME = "nameUser";
//    private final static String TAG_LEVEL = "lvlUser";
//    private final static String TAG_ACCESS = "loginUser";
//    private final static String TAG_IDCOMP = "compUser";
//    private final static String TAG_COMP = "nameComp";

    private final static String TAG_ID = "id";
    private final static String TAG_EMAIL = "email";
    private final static String TAG_NAME = "name";
    private final static String TAG_LEVEL = "level";
    private final static String TAG_ACCESS = "access";
    private final static String TAG_IDCOMP = "idCompany";
    private final static String TAG_COMP = "nameCompany";

    String id, email, name, level, access, idCompany, nameCompany;


    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_supplier, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.RecycleViewSplr);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        splrViewModelList = new ArrayList<>();

        splrCardAdapter = new SplrCardAdapter(splrViewModelList, getContext());
        recyclerView.setAdapter(splrCardAdapter);

        sharedPreferences = this.getActivity().getSharedPreferences(my_shared_preferences,Context.MODE_PRIVATE);
        id = sharedPreferences.getString(TAG_ID, null);
        email = sharedPreferences.getString(TAG_EMAIL, null);
        name = sharedPreferences.getString(TAG_NAME, null);
        level = sharedPreferences.getString(TAG_LEVEL, null);
        access = sharedPreferences.getString(TAG_ACCESS, null);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);
        nameCompany = sharedPreferences.getString(TAG_COMP, null);

        ImageButton btnAddSplr = (ImageButton) view.findViewById(R.id.imgBtnAddSplr);
        btnAddSplr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAdd = new Intent(getContext(), AddEditSplrActivity.class);
                startActivity(intentAdd);
            }
        });

//        TextView txtComp = (TextView) view.findViewById(R.id.txtComp);
//        txtComp.setText(idCompany);

        //Fetch Data Kategori
        getSplr();

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swiperRefreshSplr);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItem();
            }

            private void refreshItem() {
                //Fetch Data Kategori
                getSplr();

                onItemLoad();
            }

            private void onItemLoad() {

                swipeRefreshLayout.setRefreshing(false);

            }
        });


        return view;
    }

    public void getSplr(){

        ProgressDialog progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data..");
        progressDialog.show();

        sharedPreferences = this.getActivity().getSharedPreferences(my_shared_preferences,Context.MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        AndroidNetworking.post(URL_SPLR)
                .addBodyParameter("idCompSplr", idCompany)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        splrViewModelList.clear();

                        Log.i("Info", "Data: " + response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                SplrViewModel splrViewModel = new SplrViewModel(
                                        jsonObject.getString("idSupplier"),
                                        jsonObject.getInt("idCompSupplier"),
                                        jsonObject.getString("nameSupplier"),
                                        jsonObject.getString("addrSupplier"),
                                        jsonObject.getString("phoneSupplier"),
                                        jsonObject.getString("emailSupplier"),
                                        jsonObject.getString("descSupplier"),
                                        jsonObject.getString("imgSupplier")

                                );
                                splrViewModelList.add(splrViewModel);
                                SplrCardAdapter splrCardAdapter = new SplrCardAdapter(splrViewModelList, getContext());
                                recyclerView.setAdapter(splrCardAdapter);
                                splrCardAdapter.setOnItemClickListener(SupplierFragment.this);
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        splrCardAdapter.notifyDataSetChanged();
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
    public void onItemClick(int position) {


        Intent splrDetailIntent = new Intent(getContext(), SupplierDetailActivity.class);
        SplrViewModel clickedSplr = splrViewModelList.get(position);

        splrDetailIntent.putExtra(ID_SPLR, clickedSplr.getIdSplr());
        splrDetailIntent.putExtra(NAME_SPLR, clickedSplr.getNameSplr());
        splrDetailIntent.putExtra(DESC_SPLR, clickedSplr.getDescSplr());
        splrDetailIntent.putExtra(ADDR_SPLR, clickedSplr.getAddrSplr());
        splrDetailIntent.putExtra(PHONE_SPLR, clickedSplr.getPhoneSplr());
        splrDetailIntent.putExtra(EMAIL_SPLR, clickedSplr.getEmailSplr());
        splrDetailIntent.putExtra(IMG_SPLR, clickedSplr.getImgSplr());

        startActivity(splrDetailIntent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
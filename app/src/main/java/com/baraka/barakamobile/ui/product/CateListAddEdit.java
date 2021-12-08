package com.baraka.barakamobile.ui.product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.supplier.SplrViewModel;
import com.baraka.barakamobile.ui.supplier.SupplierDetailActivity;
import com.baraka.barakamobile.ui.util.DbConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CateListAddEdit extends AppCompatActivity implements CateAdapterAddEditPrdct.OnItemClickListener {
    private CateViewModel cateViewModel;
    //    private FragmentProductBinding binding;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CateAdapterAddEditPrdct cateAdapterAddEditPrdct;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String URL_CATE = DbConfig.URL_CATE + "allCat.php";

    private final static String TAG_ID = "id";
    private final static String TAG_EMAIL = "email";
    private final static String TAG_NAME = "name";
    private final static String TAG_LEVEL = "level";
    private final static String TAG_ACCESS = "access";
    private final static String TAG_IDCOMP = "idCompany";
    private final static String TAG_COMP = "nameCompany";

    public static final String ID_CATE = "idCat";
    public static final String ID_CATE_INT = "idCate";
    public static final String ID_COMP_CATE = "idCompCat";
    public static final String NAME_CATE = "nameCat";
    public static final String DESC_CATE = "descCat";
    public static final String IMG_CATE = "imgCat";

    String id, email, name, level, access, idCompany, nameCompany;
    String idCateIntent, nameCateIntent;

    private List<CateViewModel> cateViewModelList;

    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cate_list);

        sharedPreferences = this.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedPreferences.getString(TAG_ID, null);
        email = sharedPreferences.getString(TAG_EMAIL, null);
        name = sharedPreferences.getString(TAG_NAME, null);
        level = sharedPreferences.getString(TAG_LEVEL, null);
        access = sharedPreferences.getString(TAG_ACCESS, null);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);
        nameCompany = sharedPreferences.getString(TAG_COMP, null);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Semua Kategori");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_twotone_chevron_left_24);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewCateList);
        recyclerView.setLayoutManager(new LinearLayoutManager(CateListAddEdit.this));
        recyclerView.setHasFixedSize(true);
        cateViewModelList = new ArrayList<>();

        cateAdapterAddEditPrdct = new CateAdapterAddEditPrdct(CateListAddEdit.this, cateViewModelList);
        recyclerView.setAdapter(cateAdapterAddEditPrdct);

        //Fetch Data Kategori
        getCateListAddEdit();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshCateList);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshItem();

            }

            private void refreshItem() {

                //Fetch Data Kategori
                getCateListAddEdit();

                onItemLoad();

            }

            private void onItemLoad() {

                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    private void getCateListAddEdit() {

        ProgressDialog progressDialog = new ProgressDialog(CateListAddEdit.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data..");
        progressDialog.show();

        sharedPreferences = this.getSharedPreferences(my_shared_preferences,Context.MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        AndroidNetworking.post(URL_CATE)
                .addBodyParameter("idCompCat", idCompany)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        cateViewModelList.clear();
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                CateViewModel cateViewModel = new CateViewModel(
                                        jsonObject.getString("idCategory"),
                                        jsonObject.getString("companyCate"),
                                        jsonObject.getString("category"),
                                        jsonObject.getString("descCate"),
                                        jsonObject.getString("imageCate")

                                );
                                cateViewModelList.add(cateViewModel);
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        cateAdapterAddEditPrdct.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(CateListAddEdit.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {
        Intent intentCate = new Intent();
        CateViewModel cateViewModel = cateViewModelList.get(position);
        idCateIntent = String.valueOf(cateViewModel.getIdCat());
        nameCateIntent = cateViewModel.getNameCat();

        Log.e("Select", "Item: "+idCateIntent+", "+nameCateIntent);

        intentCate.putExtra(ID_CATE_INT, idCateIntent);
        intentCate.putExtra(NAME_CATE, nameCateIntent);
        setResult(RESULT_OK, intentCate);
        finish();

//        Intent splrDetailIntent = new Intent(getContext(), SupplierDetailActivity.class);
//        SplrViewModel clickedSplr = splrViewModelList.get(position);
//
//        splrDetailIntent.putExtra(ID_SPLR, clickedSplr.getIdSplr());
//        splrDetailIntent.putExtra(NAME_SPLR, clickedSplr.getNameSplr());
//        splrDetailIntent.putExtra(DESC_SPLR, clickedSplr.getDescSplr());
//        splrDetailIntent.putExtra(ADDR_SPLR, clickedSplr.getAddrSplr());
//        splrDetailIntent.putExtra(PHONE_SPLR, clickedSplr.getPhoneSplr());
//        splrDetailIntent.putExtra(EMAIL_SPLR, clickedSplr.getEmailSplr());
//        splrDetailIntent.putExtra(IMG_SPLR, clickedSplr.getImgSplr());
//
//        startActivity(splrDetailIntent);
    }
}
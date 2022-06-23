package com.baraka.barakamobile.ui.product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.util.DbConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CateListActivity extends AppCompatActivity implements CateListAdapter.OnItemClickListener {
    private CateViewModel cateViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CateListAdapter cateListAdapter;
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
    public static final String ID_COMP_CATE = "idCompCat";
    public static final String NAME_CATE = "nameCat";
    public static final String DESC_CATE = "descCat";
    public static final String IMG_CATE = "imgCat";

    public static final String ID_COMP = "idComp";
    public static final String NAME_COMP = "nameComp";
    public static final String CODE_COMP = "codeComp";
    public static final String ADDR_COMP = "addrComp";
    public static final String PHONE_COMP = "phoneComp";
    public static final String EMAIL_COMP = "emailComp";
    public static final String LOGO_COMP = "logoComp";

    String id, email, name, level, access, idCompany, nameCompany;

    private List<CateViewModel> cateViewModelList;

    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cate_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Semua Kategori");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_twotone_chevron_left_24);

        ImageButton imgBtnAddCate = (ImageButton) findViewById(R.id.imgBtnAddCate);
        imgBtnAddCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sharedPreferences = this.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedPreferences.getString(TAG_ID, null);
        email = sharedPreferences.getString(TAG_EMAIL, null);
        name = sharedPreferences.getString(TAG_NAME, null);
        level = sharedPreferences.getString(TAG_LEVEL, null);
        access = sharedPreferences.getString(TAG_ACCESS, null);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);
        nameCompany = sharedPreferences.getString(TAG_COMP, null);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewCateList);
        recyclerView.setLayoutManager(new LinearLayoutManager(CateListActivity.this));
        recyclerView.setHasFixedSize(true);
        cateViewModelList = new ArrayList<>();

        cateListAdapter = new CateListAdapter(CateListActivity.this, cateViewModelList);
        recyclerView.setAdapter(cateListAdapter);

        //Fetch Data Kategori
        getCateList();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshCateList);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshItem();

            }

            private void refreshItem() {

                //Fetch Data Kategori
                getCateList();

                onItemLoad();

            }

            private void onItemLoad() {

                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    private void getCateList() {

        ProgressDialog progressDialog = new ProgressDialog(CateListActivity.this);
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
                                CateListAdapter cateListAdapter = new CateListAdapter(CateListActivity.this, cateViewModelList);
                                recyclerView.setAdapter(cateListAdapter);
                                cateListAdapter.setOnItemClickListener(CateListActivity.this);
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        cateListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(CateListActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onItemClick(int position){
        Intent cateDetailIntent = new Intent (CateListActivity.this, CateDetailActivity.class);
        CateViewModel clickedCate = cateViewModelList.get(position);

        cateDetailIntent.putExtra(ID_CATE, clickedCate.getIdCat());
        cateDetailIntent.putExtra(ID_COMP_CATE, clickedCate.getIdCompCat());
        cateDetailIntent.putExtra(NAME_CATE, clickedCate.getNameCat());
        cateDetailIntent.putExtra(DESC_CATE, clickedCate.getDescCat());
        cateDetailIntent.putExtra(IMG_CATE, clickedCate.getImgCat());

        cateDetailIntent.putExtra(TAG_IDCOMP, clickedCate.getIdComp());

        startActivity(cateDetailIntent);
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
}
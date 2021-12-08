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

public class PrdctListActivity extends AppCompatActivity implements PrdctCardAdapter.OnItemClickListener {
    private ProductViewModel productViewModel;
    //    private FragmentProductBinding binding;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PrdctCardAdapter prdctCardAdapter;
    private CateCardAdpater cateCardAdpater;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String URL_PRDCT = DbConfig.URL_PRDCT + "allPrdct.php";

    public static final String ID_PRDCT = "idPrdct";
    public static final String NAME_PRDCT = "namePrdct";
    public static final String CODE_PRDCT = "codePrdct";
    public static final String CATE_PRDCT = "nameCat";
    public static final String SPLR_PRDCT = "nameSplr";
    public static final String DESC_PRDCT = "descPrdct";
    public static final String PRICE_PRDCT = "unitPrice";
    public static final String UNIT_PRDCT = "unitPrdct";
    public static final String STOCK_PRDCT = "stockPrdct";

    private final static String TAG_ID = "id";
    private final static String TAG_EMAIL = "email";
    private final static String TAG_NAME = "name";
    private final static String TAG_LEVEL = "level";
    private final static String TAG_ACCESS = "access";
    private final static String TAG_IDCOMP = "idCompany";
    private final static String TAG_COMP = "nameCompany";

    String id, email, name, level, access, idCompany, nameCompany;

    private List<PrdctViewModel> prdctViewModelList;

    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prdct_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Semua Produk");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_twotone_chevron_left_24);

        ImageButton imgBtnAddPrdct = (ImageButton) findViewById(R.id.imgBtnAddPrdct);
        imgBtnAddPrdct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrdctListActivity.this, AddEditPrdctActivity.class);
                startActivity(intent);
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

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewPrdctList);
        recyclerView.setLayoutManager(new GridLayoutManager(PrdctListActivity.this, 2));
        recyclerView.setHasFixedSize(true);
        prdctViewModelList = new ArrayList<>();

        prdctCardAdapter = new PrdctCardAdapter(prdctViewModelList, PrdctListActivity.this);
        recyclerView.setAdapter(prdctCardAdapter);

        //Fetch Data Produk
        getProduct();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshPrdctList);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshItem();

            }

            private void refreshItem() {

                //Fetch Data Produk
                getProduct();

                onItemLoad();

            }

            private void onItemLoad() {

                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    private void getProduct() {
        ProgressDialog progressDialog = new ProgressDialog(PrdctListActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data Produk");
        progressDialog.show();

        sharedPreferences = this.getSharedPreferences(my_shared_preferences,Context.MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        AndroidNetworking.post(URL_PRDCT)
                .addBodyParameter("idCompPrdct", idCompany)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        prdctViewModelList.clear();
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                PrdctViewModel prdctViewModel = new PrdctViewModel(
//                                        jsonObject.getInt("idProduct"),
//                                        jsonObject.getString("nameProduct"),
//                                        jsonObject.getString("description"),
//                                        jsonObject.getString("category"),
//                                        jsonObject.getString("supplierProduct"),
//                                        jsonObject.getString("price"),
//                                        jsonObject.getString("unit"),
//                                        jsonObject.getString("stock"),
//                                        jsonObject.getString("imageProduct")

                                        jsonObject.getString("idProduct"),
                                        jsonObject.getString("nameProduct"),
                                        jsonObject.getString("codeProduct"),
                                        jsonObject.getString("supplierProduct"),
                                        jsonObject.getString("price"),
                                        jsonObject.getString("unit"),
                                        jsonObject.getString("stock"),
                                        jsonObject.getString("lastUpdate"),
                                        jsonObject.getString("qtyUpdate"),
                                        jsonObject.getString("updateBy"),
                                        jsonObject.getString("description"),
                                        jsonObject.getString("imageProduct"),
                                        jsonObject.getString("idCategory"),
                                        jsonObject.getString("idCompCategory"),
                                        jsonObject.getString("nameCategory"),
                                        jsonObject.getString("descCategory"),
                                        jsonObject.getString("imageCategory"),
                                        jsonObject.getString("idSupplier"),
                                        jsonObject.getString("idCompSupplier"),
                                        jsonObject.getString("nameSupplier"),
                                        jsonObject.getString("addrSupplier"),
                                        jsonObject.getString("phoneSupplier"),
                                        jsonObject.getString("emailSupplier"),
                                        jsonObject.getString("descSupplier"),
                                        jsonObject.getString("imgSupplier")

                                );
                                prdctViewModelList.add(prdctViewModel);
                                PrdctCardAdapter prdctCardAdapter = new PrdctCardAdapter(prdctViewModelList,PrdctListActivity.this);
                                recyclerView.setAdapter(prdctCardAdapter);
                                prdctCardAdapter.setOnItemClickListener(PrdctListActivity.this);
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        prdctCardAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(PrdctListActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        Intent prdctDetailIntent = new Intent(PrdctListActivity.this, PrdctDetail.class);
        PrdctViewModel clickedPrdct = prdctViewModelList.get(position);

        prdctDetailIntent.putExtra(NAME_PRDCT, clickedPrdct.getNamePrdct());
        prdctDetailIntent.putExtra(CODE_PRDCT, clickedPrdct.getCodePrdct());
        prdctDetailIntent.putExtra(DESC_PRDCT, clickedPrdct.getDescPrdct());
        prdctDetailIntent.putExtra(PRICE_PRDCT, clickedPrdct.getUnitPrice());
        prdctDetailIntent.putExtra(UNIT_PRDCT, clickedPrdct.getUnitPrdct());
        prdctDetailIntent.putExtra(STOCK_PRDCT, clickedPrdct.getStockPrdct());
        prdctDetailIntent.putExtra(CATE_PRDCT, clickedPrdct.getNameCat());
        prdctDetailIntent.putExtra(SPLR_PRDCT, clickedPrdct.getNameSplr());

        startActivity(prdctDetailIntent);
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
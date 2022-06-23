package com.baraka.barakamobile.ui.product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
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

public class CateDetailActivity extends AppCompatActivity {

    private final static String TAG_ID = "id";
    private final static String TAG_EMAIL = "email";
    private final static String TAG_NAME = "name";
    private final static String TAG_LEVEL = "level";
    private final static String TAG_ACCESS = "access";
    private final static String TAG_IDCOMP = "idCompany";
    private final static String TAG_COMP = "nameCompany";

    public static final String ID_COMP = "idComp";
    public static final String NAME_COMP = "nameComp";
    public static final String CODE_COMP = "codeComp";
    public static final String ADDR_COMP = "addrComp";
    public static final String PHONE_COMP = "phoneComp";
    public static final String EMAIL_COMP = "emailComp";
    public static final String LOGO_COMP = "logoComp";

    public static final String ID_CATE = "idCat";
    public static final String ID_COMP_CATE = "idCompCat";
    public static final String NAME_CATE = "nameCat";
    public static final String DESC_CATE = "descCat";
    public static final String IMG_CATE = "imgCat";

    public static final String my_shared_preferences = "my_shared_preferences";
    private String urlCatDetail = DbConfig.URL_CATE + "idCat.php";
    private SwipeRefreshLayout swipeRefreshLayout;

    String id, email, name, level, access, idCompany, nameCompany;
    String idCat, idCompCat, nameCat, descCat, imgCat;
    String idComp, nameComp, codeComp, addrComp, phoneComp, emailComp, logoComp;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    TextView txtViewNameCatDetail, txtViewDescCatDetail;
    Image imgCateDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cate_detail);

        ProgressDialog progressDialogLoad = new ProgressDialog(CateDetailActivity.this);
        progressDialogLoad.setCancelable(false);
        progressDialogLoad.setMessage("Memuat Data..");
        progressDialogLoad.show();

        sharedPreferences = this.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

        Intent intent = getIntent();
        idCat = intent.getStringExtra(ID_CATE);
        idCompCat = intent.getStringExtra(idCompCat);
        nameCat = intent.getStringExtra(NAME_CATE);
        descCat = intent.getStringExtra(DESC_CATE);
        imgCat = intent.getStringExtra(IMG_CATE);

        idCompany = intent.getStringExtra(TAG_IDCOMP);

        txtViewNameCatDetail = findViewById(R.id.textViewNameCatDetail);
        txtViewDescCatDetail = findViewById(R.id.textViewDescCatDetail);

        cateDetail();

        swipeRefreshLayout = findViewById(R.id.SwipeRefreshCatDetail);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItem();
            }

            private void refreshItem() {
                cateDetail();
                onItemLoad();
            }

            private void onItemLoad() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        progressDialogLoad.dismiss();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_twotone_chevron_left_24);

    }

    // Detail Category
    public void cateDetail(){
        progressDialog = new ProgressDialog(CateDetailActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Kategori..");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);

        AndroidNetworking.post(urlCatDetail)
                .addBodyParameter("idCat", idCat.toString())
                .setTag("Load Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            int success = response.getInt("success");
                            if (success==1){
                                JSONArray jsonArray = response.getJSONArray("data"); // mengambil [data] dari json

                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                txtViewNameCatDetail.setText(jsonObject.getString("nameCat"));
                                txtViewDescCatDetail.setText(jsonObject.getString("descCat"));

                                getSupportActionBar().setTitle(jsonObject.getString("nameCat"));

                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CateDetailActivity.this, "Maaf, gagal Terhubung ke Database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(CateDetailActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
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
}
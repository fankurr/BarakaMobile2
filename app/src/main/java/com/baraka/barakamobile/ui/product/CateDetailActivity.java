package com.baraka.barakamobile.ui.product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.supplier.SupplierDetailActivity;
import com.baraka.barakamobile.ui.util.DbConfig;
import com.squareup.picasso.Picasso;

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
    private String urlCatDel = DbConfig.URL_CATE + "delCat.php";
    private String URL_CATE_IMG = DbConfig.URL_CATE + "imgCat/";
    private SwipeRefreshLayout swipeRefreshLayout;

    String id, email, name, level, access, idCompany, nameCompany;
    String idCat, idCompCat, nameCat, descCat, imgCat;
    String idComp, nameComp, codeComp, addrComp, phoneComp, emailComp, logoComp;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    TextView txtViewNameCatDetail, txtViewDescCatDetail;
    ImageView imgCateDetail;
    Button btnEditCateEdit, btnDelCateDetail;

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
        idCompCat = intent.getStringExtra(ID_COMP_CATE);
        nameCat = intent.getStringExtra(NAME_CATE);
        descCat = intent.getStringExtra(DESC_CATE);
        imgCat = intent.getStringExtra(IMG_CATE);

        idCompany = intent.getStringExtra(TAG_IDCOMP);

        txtViewNameCatDetail = findViewById(R.id.textViewNameCatDetail);
        txtViewDescCatDetail = findViewById(R.id.textViewDescCatDetail);
        imgCateDetail = findViewById(R.id.imgCateDetail);

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

        btnDelCateDetail = findViewById(R.id.btnDelCateDetail);
        btnDelCateDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialogDel = new Dialog(CateDetailActivity.this);

                dialogDel.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogDel.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogDel.setContentView(R.layout.dialog_delete);

                TextView textWarnDel = dialogDel.findViewById(R.id.textDelWarning);
                textWarnDel.setText("Apa Anda yakin ingin menghapus [" +nameCat+ "] dari data Anda?");

                Button btnDelNo = dialogDel.findViewById(R.id.btnDelNo);
                btnDelNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogDel.dismiss();
                    }
                });
                Button btnDelYes = dialogDel.findViewById(R.id.btnDelYes);
                btnDelYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delCate();
                        dialogDel.dismiss();
                    }
                });
                dialogDel.show();
            }
        });

        btnEditCateEdit = findViewById(R.id.btnEditCateDetail);
        btnEditCateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentEditCate = new Intent(CateDetailActivity.this, AddEditCateActivity.class);
                intentEditCate.putExtra(ID_CATE, idCat);
                intentEditCate.putExtra(ID_COMP_CATE, idCompCat);
                intentEditCate.putExtra(NAME_CATE, nameCat);
                intentEditCate.putExtra(DESC_CATE, descCat);
                intentEditCate.putExtra(IMG_CATE, imgCat);
                intentEditCate.putExtra(TAG_IDCOMP, idCompany);

                finish();
                startActivity(intentEditCate);
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

                                Picasso.get().load(URL_CATE_IMG+jsonObject.getString("imgCat"))
                                        .resize(450, 450)
                                        .centerCrop()
                                        .placeholder(R.drawable.default_image_small)
                                        .error(R.drawable.default_image_small)
                                        .into(imgCateDetail);

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

    private void delCate() {
        ProgressDialog progressDialog = new ProgressDialog(CateDetailActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Produk");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);


        AndroidNetworking.post(urlCatDel)
                .addBodyParameter("idCat", idCat.toString())
                .setTag("Update Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject data) {
                        progressDialog.dismiss();
                        Log.d("Respon Edit",""+data);

                        try {
                            Boolean status = data.getBoolean("status");
                            if (status == true){
                                new AlertDialog.Builder(CateDetailActivity.this)
                                        .setMessage("Berhasil Menghapus Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = getIntent();
                                                setResult(RESULT_OK, intent);
                                                CateDetailActivity.this.finish();
                                                onBackPressed();
                                            }
                                        })
                                        .show();
                            }else{
                                new AlertDialog.Builder(CateDetailActivity.this)
                                        .setMessage("Gagal Mengupdate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = getIntent();
                                                setResult(RESULT_CANCELED,i);
                                                CateDetailActivity.this.finish();
                                            }
                                        })
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
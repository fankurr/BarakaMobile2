package com.baraka.barakamobile.ui.product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class AddEditCateActivity extends AppCompatActivity {

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
    private SwipeRefreshLayout swipeRefreshLayout;

    private String urlCatDetailEdit = DbConfig.URL_CATE + "idCat.php";
    private String urlCatEdit = DbConfig.URL_CATE + "editCat.php";

    String id, email, name, level, access, idCompany, nameCompany;
    String idCat, idCompCat, nameCat, descCat, imgCat;
    String idComp, nameComp, codeComp, addrComp, phoneComp, emailComp, logoComp;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    EditText inputNameCateEdit, inputDescCateEdit;
    Image imgCateEdit;
    Button btnBackCateEdit, btnSaveCateEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_cate);

        ProgressDialog progressDialogLoad = new ProgressDialog(AddEditCateActivity.this);
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

        inputNameCateEdit = findViewById(R.id.editTxtNameCateAddEdit);
        inputDescCateEdit = findViewById(R.id.editTxtDescCateAddEdit);

        btnBackCateEdit = findViewById(R.id.btnBackCateEdit);
        btnSaveCateEdit = findViewById(R.id.btnSaveCateEdit);

        inputNameCateEdit.setText(nameCat);
        inputDescCateEdit.setText(descCat);

//        cateDetailEdit();

        btnSaveCateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCate();
            }
        });

        btnBackCateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        progressDialogLoad.dismiss();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(nameCat);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_twotone_chevron_left_24);
    }


    // Detail Category Edit
//    public void cateDetailEdit(){
//        progressDialog = new ProgressDialog(AddEditCateActivity.this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Memuat Detail Kategori..");
//        progressDialog.show();
//
//        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);
//
//        AndroidNetworking.post(urlCatDetailEdit)
//                .addBodyParameter("idCat", idCat.toString())
//                .setTag("Load Data..")
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        try {
//                            int success = response.getInt("success");
//                            if (success==1){
//                                JSONArray jsonArray = response.getJSONArray("data"); // mengambil [data] dari json
//
//                                JSONObject jsonObject = jsonArray.getJSONObject(0);
//
//                                inputNameCateEdit.setText(jsonObject.getString("nameCat"));
//                                inputDescCateEdit.setText(jsonObject.getString("descCat"));
//
//                                getSupportActionBar().setTitle(jsonObject.getString("nameCat"));
//
//                                progressDialog.dismiss();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(AddEditCateActivity.this, "Maaf, gagal Terhubung ke Database", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        Toast.makeText(AddEditCateActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
//                        Log.d("ERROR","error => "+ anError.toString());
//                        progressDialog.dismiss();
//
//                    }
//                });
//    }


    //Edit Category
    public void editCate(){

        ProgressDialog progressDialog = new ProgressDialog(AddEditCateActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Produk");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);


        AndroidNetworking.post(urlCatEdit)
                .addBodyParameter("idCat", idCat.toString())
                .addBodyParameter("nameCat", inputNameCateEdit.getText().toString())
                .addBodyParameter("descCat", inputDescCateEdit.getText().toString())
                .addBodyParameter("imgCat", imgCat.toString())
                .setTag("Update Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject data) {
                        progressDialog.dismiss();
                        Log.d("Respon Edit",""+data);

                        try {
                            int success = data.getInt("success");
                            if (success == 1){
                                new AlertDialog.Builder(AddEditCateActivity.this)
                                        .setMessage("Berhasil Mengudate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(AddEditCateActivity.this, CateDetailActivity.class);
                                                intent.putExtra(ID_CATE, idCat);
                                                finish();
                                                startActivity(intent);
                                            }
                                        })
                                        .show();
                            }else{
                                new AlertDialog.Builder(AddEditCateActivity.this)
                                        .setMessage("Gagal Mengupdate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Log.i("Input", "Data: "+idCat+", "+idCompCat+", "+nameCat+", "+descCat.toString());
                                                Intent i = getIntent();
                                                setResult(RESULT_CANCELED,i);
                                                AddEditCateActivity.this.finish();
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
                        Toast.makeText(AddEditCateActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
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
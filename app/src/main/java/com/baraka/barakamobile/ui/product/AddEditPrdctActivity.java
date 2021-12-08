package com.baraka.barakamobile.ui.product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.List;

import static com.baraka.barakamobile.ui.product.ProductFragment.CATE_PRDCT;
import static com.baraka.barakamobile.ui.product.ProductFragment.CODE_PRDCT;
import static com.baraka.barakamobile.ui.product.ProductFragment.DESC_PRDCT;
import static com.baraka.barakamobile.ui.product.ProductFragment.ID_PRDCT;
import static com.baraka.barakamobile.ui.product.ProductFragment.NAME_PRDCT;
import static com.baraka.barakamobile.ui.product.ProductFragment.PRICE_PRDCT;
import static com.baraka.barakamobile.ui.product.ProductFragment.SPLR_PRDCT;
import static com.baraka.barakamobile.ui.product.ProductFragment.STOCK_PRDCT;
import static com.baraka.barakamobile.ui.product.ProductFragment.UNIT_PRDCT;

public class AddEditPrdctActivity extends AppCompatActivity {

    private final static String TAG_ID = "id";
    private final static String TAG_EMAIL = "email";
    private final static String TAG_NAME = "name";
    private final static String TAG_LEVEL = "level";
    private final static String TAG_ACCESS = "access";
    private final static String TAG_IDCOMP = "idCompany";
    private final static String TAG_COMP = "nameCompany";

    public static final String ID_PRDCT = "idPrdct";
    public static final String NAME_PRDCT = "namePrdct";
    public static final String CODE_PRDCT = "codePrdct";
    public static final String CATE_PRDCT = "nameCat";
    public static final String SPLR_PRDCT = "nameSplr";
    public static final String DESC_PRDCT = "descPrdct";
    public static final String PRICE_PRDCT = "unitPrice";
    public static final String UNIT_PRDCT = "unitPrdct";
    public static final String STOCK_PRDCT = "stockPrdct";

    public static final String ID_CATE = "idCate";
    public static final String ID_CATE_INT = "idCate";
    public static final String ID_COMP_CATE = "idCompCat";
    public static final String NAME_CATE = "nameCat";
    public static final String DESC_CATE = "descCat";
    public static final String IMG_CATE = "imgCat";

    private static final int CHANGE_CATE_REQUEST_CODE = 0;

    String id, email, name, level, access, idCompany, nameCompany;
    String idPrdct, namePrdct, codePrdct, descPrdct, pricePrdct, unitPricePrdct, stockPrdct, catPrdct, idCat, nameSplrPrdct;
    String idCateIntent, nameCateIntent;
    EditText inputNamePrdct, inputCodePrdct, inputDescPrdct, inputUnitPrdct, inputPricePrdct, inputStokPrdct, inputCatePrdct;
    Button btnUploadImgPrdctAddEdit;
    TextView textPath, textCate;
    CardView cardCatAddEditPrdct;

    private String URL_PRDCT_ADD_EDIT = DbConfig.URL_PRDCT + "editPrdct.php";
    private String URL_CATE = DbConfig.URL_CATE + "allCat.php";

    ProgressDialog progressDialog;
    AdapterSpinnerCatAddEdit adapterSpinnerCatAddEdit;
    Spinner spinnerCat;

    ArrayList<CateViewModel> cateViewModelList = new ArrayList<CateViewModel>();

    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_prdct);

        progressDialog = new ProgressDialog(this);

        Intent intent1 = getIntent();
        idCateIntent = intent1.getStringExtra(ID_CATE_INT);
        nameCateIntent = intent1.getStringExtra(NAME_CATE);

        Intent intent = getIntent();
        idPrdct = intent.getStringExtra(ID_PRDCT);
        namePrdct = intent.getStringExtra(NAME_PRDCT);
        codePrdct = intent.getStringExtra(CODE_PRDCT);
        descPrdct = intent.getStringExtra(DESC_PRDCT);
        pricePrdct = intent.getStringExtra(PRICE_PRDCT);
        unitPricePrdct = intent.getStringExtra(UNIT_PRDCT);
        stockPrdct = intent.getStringExtra(STOCK_PRDCT);
        idCat = intent.getStringExtra(ID_CATE);
        catPrdct = intent.getStringExtra(CATE_PRDCT);
        nameSplrPrdct = intent.getStringExtra(SPLR_PRDCT);

        inputNamePrdct = findViewById(R.id.editTxtNamePrdctAddEdit);
        inputCodePrdct = findViewById(R.id.editTxtKodePrdctAddEdit);
        inputDescPrdct = findViewById(R.id.editTxtDescPrdctAddEdit);
        inputUnitPrdct = findViewById(R.id.editTxtUnitPrdctAddEdit);
        inputPricePrdct = findViewById(R.id.editTxtPricePrdctAddEdit);
        inputStokPrdct = findViewById(R.id.editTxtStokPrdctAddEdit);
        textCate = findViewById(R.id.textViewNameCatAddEditPrdct);
        cardCatAddEditPrdct = findViewById(R.id.cardCatAddEditPrdct);

        btnUploadImgPrdctAddEdit = (Button) findViewById(R.id.btnUploadPrdctAddEdit);

        btnUploadImgPrdctAddEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 7);

            }
        });

        if (namePrdct == null) {
            setTitle("Tambah Produk");

            inputNamePrdct.setText(null);
            inputCodePrdct.setText(null);
            inputDescPrdct.setText(null);
            inputUnitPrdct.setText(null);
            inputPricePrdct.setText(null);
            inputStokPrdct.setText(null);
            textCate.setText("Pilih Kategori");

            cardCatAddEditPrdct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent1 = new Intent(AddEditPrdctActivity.this, CateListAddEdit.class);
                    startActivityForResult(intent1, CHANGE_CATE_REQUEST_CODE);

                }
            });

            Button btnSimpanAdd = findViewById(R.id.btnSavePrdctAddEdit);
            btnSimpanAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.setMessage("Menyimpan Data...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    addPrdct();
                    progressDialog.dismiss();

                }
            });

        } else {
            getSupportActionBar().setTitle(namePrdct);

            inputNamePrdct.setText(namePrdct);
            inputCodePrdct.setText(codePrdct);
            inputDescPrdct.setText(descPrdct);
            inputUnitPrdct.setText(unitPricePrdct);
            inputPricePrdct.setText(pricePrdct);
            inputStokPrdct.setText(stockPrdct);
            textCate.setText(catPrdct);

            cardCatAddEditPrdct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent1 = new Intent(AddEditPrdctActivity.this, CateListAddEdit.class);
                    startActivityForResult(intent1, CHANGE_CATE_REQUEST_CODE);

                }
            });

            Button btnSimpanEdit = findViewById(R.id.btnSavePrdctAddEdit);
            btnSimpanEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.setMessage("Merubah Data...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    editPrdct();
                    progressDialog.dismiss();

                }
            });
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_twotone_chevron_left_24);

        Button btnBack = findViewById(R.id.btnBackPrdctAddEdit);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    //Add Product
    public void addPrdct(){
        ProgressDialog progressDialog = new ProgressDialog(AddEditPrdctActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Produk");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        AndroidNetworking.post(URL_PRDCT_ADD_EDIT)
                .addBodyParameter("idPrdct", idPrdct)
                .addBodyParameter("catPrdct", idCateIntent)
                .addBodyParameter("namePrdct", inputNamePrdct.getText().toString())
                .addBodyParameter("codePrdct", inputCodePrdct.getText().toString())
                .addBodyParameter("unitPrice", inputPricePrdct.getText().toString())
                .addBodyParameter("unitPrdct", inputUnitPrdct.getText().toString())
                .addBodyParameter("stockPrdct", inputStokPrdct.getText().toString())
                .addBodyParameter("descPrdct", inputDescPrdct.getText().toString())
                .setTag("Update Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject data) {
                        progressDialog.dismiss();
                        Log.d("Respon Edit","" + data);

                        try {
                            Boolean status = data.getBoolean("status");
                            if (status == true){
                                new AlertDialog.Builder(AddEditPrdctActivity.this)
                                        .setMessage("Berhasil Mengudate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = getIntent();
                                                setResult(RESULT_OK, intent);
                                                AddEditPrdctActivity.this.finish();
                                            }
                                        })
                                        .show();
                            }else{
                                new AlertDialog.Builder(AddEditPrdctActivity.this)
                                        .setMessage("Gagal Mengupdate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Log.i("Input", "Data: "+idPrdct+", "+catPrdct+", "+namePrdct+", "+codePrdct+", "+pricePrdct+", "+unitPricePrdct+", "+stockPrdct+", "+descPrdct.toString());
                                                Intent i = getIntent();
                                                setResult(RESULT_CANCELED,i);
                                                AddEditPrdctActivity.this.finish();
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
                        Toast.makeText(AddEditPrdctActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();

                    }
                });

    }

    //Edit Product
    public void editPrdct(){

        ProgressDialog progressDialog = new ProgressDialog(AddEditPrdctActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Produk");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        AndroidNetworking.post(URL_PRDCT_ADD_EDIT)
                .addBodyParameter("idPrdct", idPrdct.toString())
                .addBodyParameter("catPrdct", idCateIntent)
                .addBodyParameter("namePrdct", inputNamePrdct.getText().toString())
                .addBodyParameter("codePrdct", inputCodePrdct.getText().toString())
                .addBodyParameter("unitPrice", inputPricePrdct.getText().toString())
                .addBodyParameter("unitPrdct", inputUnitPrdct.getText().toString())
                .addBodyParameter("stockPrdct", inputStokPrdct.getText().toString())
                .addBodyParameter("descPrdct", inputDescPrdct.getText().toString())
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
                                new AlertDialog.Builder(AddEditPrdctActivity.this)
                                .setMessage("Berhasil Mengudate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = getIntent();
                                                setResult(RESULT_OK, intent);
                                                AddEditPrdctActivity.this.finish();
                                            }
                                        })
                                        .show();
                            }else{
                            new AlertDialog.Builder(AddEditPrdctActivity.this)
                                    .setMessage("Gagal Mengupdate Data")
                                    .setCancelable(false)
                                    .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Log.i("Input", "Data: "+idPrdct+", "+catPrdct+", "+namePrdct+", "+codePrdct+", "+pricePrdct+", "+unitPricePrdct+", "+stockPrdct+", "+descPrdct.toString());
                                            Intent i = getIntent();
                                            setResult(RESULT_CANCELED,i);
                                            AddEditPrdctActivity.this.finish();
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
                        Toast.makeText(AddEditPrdctActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        textPath = (TextView)findViewById(R.id.textViewPathImgPrdctAddEdit);

        switch (requestCode) {
            case 7:
                if (resultCode==RESULT_OK) {
                    String PathHolder = data.getData().getPath();
                    textPath.setText(PathHolder);
                }
                break;
        }
        // Check that it is the SecondActivity with an OK result
        if (requestCode == CHANGE_CATE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                idCateIntent = data.getStringExtra(ID_CATE_INT);
                nameCateIntent = data.getStringExtra(NAME_CATE);

                Log.e("Select", "Item: "+idCateIntent+", "+nameCateIntent);

                // Get String data from Intent
                String returnString = data.getStringExtra(NAME_CATE);

                textCate = findViewById(R.id.textViewNameCatAddEditPrdct);
                textCate.setText(returnString);
            }
        }
    }

}
package com.baraka.barakamobile.ui.product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.supplier.SplrCardAdapter;
import com.baraka.barakamobile.ui.supplier.SplrViewModel;
import com.baraka.barakamobile.ui.supplier.SupplierFragment;
import com.baraka.barakamobile.ui.util.DbConfig;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    public static final String IMG_PRDCT = "imgPrdct";

    public static final String ID_CATE = "idCate";
    public static final String ID_CATE_INT = "idCate";
    public static final String ID_COMP_CATE = "idCompCat";
    public static final String NAME_CATE = "nameCat";
    public static final String DESC_CATE = "descCat";
    public static final String IMG_CATE = "imgCat";

    private static final int CHANGE_CATE_REQUEST_CODE = 0;

    String id, email, name, level, access, idCompany, nameCompany;
    String idPrdct, namePrdct, codePrdct, descPrdct, pricePrdct, unitPricePrdct, stockPrdct, catPrdct, idCat, imgPrdct, nameSplrPrdct;
    String idCateIntent, nameCateIntent;
    EditText inputNamePrdct, inputCodePrdct, inputDescPrdct, inputUnitPrdct, inputPricePrdct, inputStokPrdct, inputCatePrdct;
    Button btnUploadImgPrdctAddEdit;
    TextView textPath, textCate, txtIdCateAddPrdct, textIdSplrAddPrdct;
    CardView cardCatAddEditPrdct;
    ImageView imgPrdctEditDetail;
    Uri selectedImageUri;
    File fileImgPrdct;

    // the activity result code
    int SELECT_PICTURE = 200;

    private String URL_PRDCT_ADD = DbConfig.URL_PRDCT + "addPrdct.php";
    private String URL_PRDCT_ADD_EDIT = DbConfig.URL_PRDCT + "editPrdct.php";
    private String URL_CATE = DbConfig.URL_CATE + "allCat.php";
    private String URL_SPLR = DbConfig.URL_SPLR + "allSplr.php";
    private String urlEditPrdctDetail = DbConfig.URL_PRDCT + "idPrdct.php";
    private String URL_PRDCT_IMG = DbConfig.URL_PRDCT + "imgPrdct/";

    ProgressDialog progressDialog;
    AdapterSpinnerCatAddEdit adapterSpinnerCatAddEdit;
    AdapterSpinnerSplrAddEdit adapterSpinnerSplrAddEdit;
    Spinner spinnerCat, spinnerSplr;
    Calendar calender;
    SimpleDateFormat simpledateformat;
    String date;

    List<DataSpinnerCate> cateDataSpinner = new ArrayList<DataSpinnerCate>();
    List<DataSpinnerSplr> splrDataSpinner= new ArrayList<DataSpinnerSplr>();

    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_prdct);

        progressDialog = new ProgressDialog(this);

        sharedPreferences = AddEditPrdctActivity.this.getSharedPreferences(my_shared_preferences,Context.MODE_PRIVATE);
        id = sharedPreferences.getString(TAG_ID, null);
        email = sharedPreferences.getString(TAG_EMAIL, null);
        name = sharedPreferences.getString(TAG_NAME, null);
        level = sharedPreferences.getString(TAG_LEVEL, null);
        access = sharedPreferences.getString(TAG_ACCESS, null);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);
        nameCompany = sharedPreferences.getString(TAG_COMP, null);

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
        imgPrdct = intent.getStringExtra(IMG_PRDCT);
        idCat = intent.getStringExtra(ID_CATE);
        catPrdct = intent.getStringExtra(CATE_PRDCT);
        nameSplrPrdct = intent.getStringExtra(SPLR_PRDCT);

        inputNamePrdct = findViewById(R.id.editTxtNamePrdctAddEdit);
        inputCodePrdct = findViewById(R.id.editTxtKodePrdctAddEdit);
        inputDescPrdct = findViewById(R.id.editTxtDescPrdctAddEdit);
        inputUnitPrdct = findViewById(R.id.editTxtUnitPrdctAddEdit);
        inputPricePrdct = findViewById(R.id.editTxtPricePrdctAddEdit);
        inputStokPrdct = findViewById(R.id.editTxtStokPrdctAddEdit);
        txtIdCateAddPrdct = findViewById(R.id.txtIdCateAddPrdct);
        textIdSplrAddPrdct = findViewById(R.id.txtIdSplrAddPrdct);
        imgPrdctEditDetail = findViewById(R.id.imgPrdctEditDetail);
        spinnerCat = findViewById(R.id.spinnerCatePrdctAddEdit);
        spinnerSplr = findViewById(R.id.spinnerSplrPrdctAddEdit);

        calender = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("EEE, dd-MM-yyyy HH:mm:ss");
        date = simpledateformat.format(calender.getTime());

        btnUploadImgPrdctAddEdit = (Button) findViewById(R.id.btnUploadPrdctAddEdit);

        btnUploadImgPrdctAddEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("*/*");
//                startActivityForResult(intent, 7);
                imageChooser();

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

            Picasso.get().load(URL_PRDCT_IMG+imgPrdct)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.default_image_small)
                    .error(R.drawable.default_image_small)
                    .into(imgPrdctEditDetail);

            getCateList();

            spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    Log.e("Input", "Data: "+cateDataSpinner.get(position).getIdCat()+", "+cateDataSpinner.get(position).getIdCompCat()+", "+cateDataSpinner.get(position).getNameCat().toString());
                    txtIdCateAddPrdct.setText(cateDataSpinner.get(position).getIdCat());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            getSplr();

            spinnerSplr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    Log.e("Input", "Data: "+splrDataSpinner.get(position).getIdSplr()+", "+splrDataSpinner.get(position).getIdCompSplr()+", "+splrDataSpinner.get(position).getNameSplr().toString());
                    textIdSplrAddPrdct.setText(splrDataSpinner.get(position).getIdSplr());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

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

//            getCateList();
//
//            getSplr();

//            spinnerCat.setAdapter(adapterSpinnerCatAddEdit);
//
//            spinnerCat.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
//
//            spinnerCat.setSelected(Boolean.parseBoolean("Aksesoris"));

            editPrdctDetail();

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

    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        Intent intent = new Intent();
//        intent.setType("*/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), SELECT_PICTURE);
//        startActivityForResult(intent, SELECT_PICTURE);
    }

    public String getRealPathFromURI(Uri contentURI, Activity context) {

        String[] projection = { MediaStore.Images.Media.DATA };

        @SuppressWarnings("deprecation")
        Cursor cursor = context.managedQuery(contentURI, projection, null, null, null);
        if (cursor == null)
            return null;

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);
            // cursor.close();
            return s;
        }

        // cursor.close();
        return null;

    }


    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        textPath = (TextView)findViewById(R.id.textViewPathImgPrdctAddEdit);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                selectedImageUri = data.getData();
                String PathHolder = data.getData().getPath();
                textPath.setText(PathHolder);
                if (null != selectedImageUri) {
                    // update the preview image in the layout

                    Picasso.get().load(selectedImageUri)
                            .resize(450, 450)
                            .centerCrop()
                            .into(imgPrdctEditDetail);

                    String imagepath = getRealPathFromURI(selectedImageUri, this);

                    fileImgPrdct = new File(imagepath);

//                    imgUser.setImageURI(selectedImageUri);
                }
            }
        }
    }

    //Add Product
    public void addPrdct(){
        ProgressDialog progressDialog = new ProgressDialog(AddEditPrdctActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Produk");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        AndroidNetworking.upload(URL_PRDCT_ADD)
                .addMultipartParameter("idCompPrdct", idCompany.toString())
                .addMultipartParameter("catPrdct", txtIdCateAddPrdct.getText().toString())
                .addMultipartParameter("namePrdct", inputNamePrdct.getText().toString())
                .addMultipartParameter("codePrdct", inputCodePrdct.getText().toString())
                .addMultipartParameter("nameSplrPrdct", textIdSplrAddPrdct.getText().toString())
                .addMultipartParameter("unitPrice", inputPricePrdct.getText().toString())
                .addMultipartParameter("unitPrdct", inputUnitPrdct.getText().toString())
                .addMultipartParameter("stockPrdct", inputStokPrdct.getText().toString())
                .addMultipartParameter("lastUpdate", date)
                .addMultipartParameter("updateBy", id.toString())
                .addMultipartParameter("descPrdct", inputDescPrdct.getText().toString())
                .addMultipartFile("imgPrdct", fileImgPrdct)
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
                                                finish();
                                                startActivity(intent);
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
                        Log.e("ERROR","Data => "+idCompany+", "
                                +txtIdCateAddPrdct.getText().toString()+", "
                                +inputNamePrdct.getText().toString()+", "
                                +inputCodePrdct.getText().toString()+", "
                                +textIdSplrAddPrdct.getText().toString()+", "
                                +inputPricePrdct.getText().toString()+", "
                                +inputUnitPrdct.getText().toString()+", "
                                +inputStokPrdct.getText().toString()+", "
                                +date+", "
                                +id+", "
                                +inputDescPrdct.getText().toString()+", "
                                +fileImgPrdct);
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

        AndroidNetworking.upload(URL_PRDCT_ADD_EDIT)
                .addMultipartParameter("idCompPrdct", idCompany.toString())
                .addMultipartParameter("catPrdct", txtIdCateAddPrdct.getText().toString())
                .addMultipartParameter("namePrdct", inputNamePrdct.getText().toString())
                .addMultipartParameter("codePrdct", inputCodePrdct.getText().toString())
                .addMultipartParameter("nameSplrPrdct", textIdSplrAddPrdct.getText().toString())
                .addMultipartParameter("unitPrice", inputPricePrdct.getText().toString())
                .addMultipartParameter("unitPrdct", inputUnitPrdct.getText().toString())
                .addMultipartParameter("stockPrdct", inputStokPrdct.getText().toString())
                .addMultipartParameter("lastUpdate", date)
                .addMultipartParameter("updateBy", id.toString())
                .addMultipartParameter("descPrdct", inputDescPrdct.getText().toString())
                .addMultipartFile("imgPrdct", fileImgPrdct)
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
                                                Intent intent = new Intent(AddEditPrdctActivity.this, PrdctDetail.class);
                                                intent.putExtra(ID_PRDCT, idPrdct);
                                                startActivity(intent);
                                                finish();
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

//     Detail Profile
    public void editPrdctDetail(){
        progressDialog = new ProgressDialog(AddEditPrdctActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Profil");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);

        AndroidNetworking.post(urlEditPrdctDetail)
                .addBodyParameter("idPrdct", idPrdct.toString())
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

                                inputNamePrdct.setText(jsonObject.getString("namePrdct"));
                                inputCodePrdct.setText(jsonObject.getString("codePrdct"));
                                inputPricePrdct.setText(jsonObject.getString("unitPrice"));
                                inputUnitPrdct.setText(jsonObject.getString("unitPrdct"));
                                inputStokPrdct.setText(jsonObject.getString("stockPrdct"));
                                inputDescPrdct.setText(jsonObject.getString("descPrdct"));

                                Picasso.get().load(URL_PRDCT_IMG+jsonObject.getString("imgPrdct"))
                                        .resize(450, 450)
                                        .centerCrop()
                                        .placeholder(R.drawable.default_image_small)
                                        .error(R.drawable.default_image_small)
                                        .into(imgPrdctEditDetail);

//                                spinnerCat.setSelected(Boolean.parseBoolean("Aksesoris"));
                                getCateList();

                                getSplr();



                                spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                        Log.e("Input", "Data: "+cateDataSpinner.get(position).getIdCat()+", "+cateDataSpinner.get(position).getIdCompCat()+", "+cateDataSpinner.get(position).getNameCat().toString());
                                        txtIdCateAddPrdct.setText(cateDataSpinner.get(position).getIdCat());
                                        spinnerCat.setSelection(2);
                                        spinnerCat.setSelection(2);

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });


                                spinnerSplr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                        Log.e("Input", "Data: "+splrDataSpinner.get(position).getIdSplr()+", "+splrDataSpinner.get(position).getIdCompSplr()+", "+splrDataSpinner.get(position).getNameSplr().toString());
                                        textIdSplrAddPrdct.setText(splrDataSpinner.get(position).getIdSplr());

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                    }
                                });

                                getSupportActionBar().setTitle(jsonObject.getString("namePrdct"));

                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddEditPrdctActivity.this, "Maaf, gagal Terhubung ke Database", Toast.LENGTH_SHORT).show();
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

    private void getCateList() {

        ProgressDialog progressDialog = new ProgressDialog(AddEditPrdctActivity.this);
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
                        cateDataSpinner.clear();
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                DataSpinnerCate dataSpinnerCate = new DataSpinnerCate(
//                                        jsonObject.getString("idCategory"),
//                                        jsonObject.getString("companyCate"),
//                                        jsonObject.getString("category")
//
//                                );
                                DataSpinnerCate dataSpinnerCate = new DataSpinnerCate();

                                dataSpinnerCate.setIdCat(jsonObject.getString("idCategory"));
                                dataSpinnerCate.setIdCompCat(jsonObject.getString("companyCate"));
                                dataSpinnerCate.setNameCat(jsonObject.getString("category"));


                                cateDataSpinner.add(dataSpinnerCate);
                                adapterSpinnerCatAddEdit = new AdapterSpinnerCatAddEdit(AddEditPrdctActivity.this, cateDataSpinner);
                                spinnerCat.setAdapter(adapterSpinnerCatAddEdit);
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
//                        cateListAdapter.notifyDataSetChanged();
                        adapterSpinnerCatAddEdit.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(AddEditPrdctActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();
                    }
                });
    }

    public void getSplr(){

        ProgressDialog progressDialog = new ProgressDialog(AddEditPrdctActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data..");
        progressDialog.show();

        sharedPreferences = this.getSharedPreferences(my_shared_preferences,Context.MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        AndroidNetworking.post(URL_SPLR)
                .addBodyParameter("idCompSplr", idCompany)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        splrDataSpinner.clear();

                        Log.i("Info", "Data: " + response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                DataSpinnerSplr dataSpinnerSplr = new DataSpinnerSplr();

                                dataSpinnerSplr.setIdSplr(jsonObject.getString("idSupplier"));
                                dataSpinnerSplr.setIdCompSplr(jsonObject.getInt("idCompSupplier"));
                                dataSpinnerSplr.setNameSplr(jsonObject.getString("nameSupplier"));

                                splrDataSpinner.add(dataSpinnerSplr);
                                adapterSpinnerSplrAddEdit = new AdapterSpinnerSplrAddEdit(AddEditPrdctActivity.this, splrDataSpinner);
                                spinnerSplr.setAdapter(adapterSpinnerSplrAddEdit);
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        adapterSpinnerSplrAddEdit.notifyDataSetChanged();
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//        textPath = (TextView)findViewById(R.id.textViewPathImgPrdctAddEdit);
//
//        switch (requestCode) {
//            case 7:
//                if (resultCode==RESULT_OK) {
//                    String PathHolder = data.getData().getPath();
//                    textPath.setText(PathHolder);
//                }
//                break;
//        }
//        // Check that it is the SecondActivity with an OK result
//        if (requestCode == CHANGE_CATE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//
//                idCateIntent = data.getStringExtra(ID_CATE_INT);
//                nameCateIntent = data.getStringExtra(NAME_CATE);
//
//                Log.e("Select", "Item: "+idCateIntent+", "+nameCateIntent);
//
//                // Get String data from Intent
//                String returnString = data.getStringExtra(NAME_CATE);
//
//                textCate = findViewById(R.id.textViewNameCatAddEditPrdct);
//                textCate.setText(returnString);
//            }
//        }
//    }

}
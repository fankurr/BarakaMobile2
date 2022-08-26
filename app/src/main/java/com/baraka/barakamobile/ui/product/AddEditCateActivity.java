package com.baraka.barakamobile.ui.product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.util.DbConfig;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

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
    private String urlCatAdd = DbConfig.URL_CATE + "addCat.php";
    private String urlCatEdit = DbConfig.URL_CATE + "editCat.php";
    private String URL_CATE_IMG = DbConfig.URL_CATE + "imgCat/";

    String id, email, name, level, access, idCompany, nameCompany;
    String idCat, idCompCat, nameCat, descCat, imgCat;
    String idComp, nameComp, codeComp, addrComp, phoneComp, emailComp, logoComp;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    EditText inputNameCateEdit, inputDescCateEdit;
    ImageView imgCateEdit;
    Button btnBackCateEdit, btnSaveCateEdit, btnUploadImgCateAddEdit;
    TextView textPath;
    Uri selectedImageUri;
    File fileImgCate;

    // the activity result code
    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_cate);

        ProgressDialog progressDialogLoad = new ProgressDialog(AddEditCateActivity.this);
        progressDialogLoad.setCancelable(false);
        progressDialogLoad.setMessage("Memuat Data..");
        progressDialogLoad.show();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_twotone_chevron_left_24);

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
        imgCateEdit = findViewById(R.id.imgCateEdit);

        btnBackCateEdit = findViewById(R.id.btnBackCateEdit);
        btnSaveCateEdit = findViewById(R.id.btnSaveCateEdit);
        btnUploadImgCateAddEdit = findViewById(R.id.btnUploadCateAddEdit);
        btnUploadImgCateAddEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

//        inputNameCateEdit.setText(nameCat);
//        inputDescCateEdit.setText(descCat);

        if (nameCat == null) {
            setTitle("Tambah Supplier");

            inputNameCateEdit.setText(null);
            inputDescCateEdit.setText(null);

            Picasso.get().load(URL_CATE_IMG+imgCat)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.default_image_small)
                    .error(R.drawable.default_image_small)
                    .into(imgCateEdit);

            Button btnSimpanAdd = findViewById(R.id.btnSaveCateEdit);
            btnSimpanAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addCate();
//                    onBackPressed();
//                    AddEditSplrActivity.this.finish();

                }
            });

        }else{
            getSupportActionBar().setTitle(nameCat);

            cateDetailEdit();

            btnSaveCateEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editCate();
                }
            });
        }


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

        textPath = (TextView)findViewById(R.id.textPathImgCateAddEdit);

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
                            .into(imgCateEdit);

                    String imagepath = getRealPathFromURI(selectedImageUri, this);

                    fileImgCate = new File(imagepath);

//                    imgUser.setImageURI(selectedImageUri);
                }
            }
        }

//        textPath = (TextView)findViewById(R.id.textViewPathImgSplrAddEdit);
//
//        switch (requestCode) {
//            case 7:
//                if (resultCode==RESULT_OK) {
//                    String PathHolder = data.getData().getPath();
//                    textPath.setText(PathHolder);
//                }
//                break;
//        }
    }


    // Detail Category Edit
    public void cateDetailEdit(){
        progressDialog = new ProgressDialog(AddEditCateActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Kategori..");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);

        AndroidNetworking.post(urlCatDetailEdit)
                .addBodyParameter("idCat", idCat.toString())
                .setTag("Load Data..")
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

                                inputNameCateEdit.setText(jsonObject.getString("nameCat"));
                                inputDescCateEdit.setText(jsonObject.getString("descCat"));

                                Picasso.get().load(URL_CATE_IMG+jsonObject.getString("imgCat"))
                                        .resize(450, 450)
                                        .centerCrop()
                                        .placeholder(R.drawable.default_image_small)
                                        .error(R.drawable.default_image_small)
                                        .into(imgCateEdit);

                                getSupportActionBar().setTitle(jsonObject.getString("nameCat"));

                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddEditCateActivity.this, "Maaf, gagal Terhubung ke Database", Toast.LENGTH_SHORT).show();
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

    //Edit Category
    public void addCate(){

        ProgressDialog progressDialog = new ProgressDialog(AddEditCateActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Produk");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);


        AndroidNetworking.upload(urlCatAdd)
                .addMultipartParameter("idCompCat", idCompany.toString())
                .addMultipartParameter("nameCat", inputNameCateEdit.getText().toString())
                .addMultipartParameter("descCat", inputDescCateEdit.getText().toString())
                .addMultipartFile("imgCat", fileImgCate)
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
                                new AlertDialog.Builder(AddEditCateActivity.this)
                                        .setMessage("Berhasil Menambah Kategori")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(AddEditCateActivity.this, CateListActivity.class);
                                                intent.putExtra(ID_CATE, idCat);
                                                finish();
                                                startActivity(intent);
                                            }
                                        })
                                        .show();
                            }else{
                                new AlertDialog.Builder(AddEditCateActivity.this)
                                        .setMessage("Gagal Menambah Kategori")
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

    //Edit Category
    public void editCate(){

        ProgressDialog progressDialog = new ProgressDialog(AddEditCateActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Produk");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);


        AndroidNetworking.upload(urlCatEdit)
                .addMultipartParameter("idCat", idCat.toString())
                .addMultipartParameter("idCompCat", idCompany.toString())
                .addMultipartParameter("nameCat", inputNameCateEdit.getText().toString())
                .addMultipartParameter("descCat", inputDescCateEdit.getText().toString())
                .addMultipartFile("imgCat", fileImgCate)
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
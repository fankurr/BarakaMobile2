package com.baraka.barakamobile.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class EditCompProActivity extends AppCompatActivity {
    String id, email, name, address, level, postUser, phone, access, idCompany, nameCompany;
    String idComp, nameComp, codeComp, addrComp, phoneComp, emailComp, logoComp;
    EditText inputNameComp, inputAddrComp, inputTlpComp, inputEmailCOmp, inputCodeComp;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    Button btnBackEditCompPro, btnSaveEditCompPro;
    ImageView imgCompEdit;
    Uri selectedImageUri;
    File imgProfileComp;

    // the activity result code
    int SELECT_PICTURE = 200;

    public static final String my_shared_preferences = "my_shared_preferences";

    private final static String TAG_ID = "id";
    private final static String TAG_EMAIL = "email";
    private final static String TAG_NAME = "name";
    private final static String TAG_ADDRESS = "address";
    private final static String TAG_LEVEL = "level";
    private final static String TAG_POST = "postUser";
    private final static String TAG_TLP = "phone";
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

    private String urlCompProDetail = DbConfig.URL_COMP + "idComp.php";
    private String urlCompProEdit = DbConfig.URL_COMP + "editComp.php";
    private String URL_COMP_IMG_DETAIL = DbConfig.URL_COMP + "imgComp/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comp_pro);

        sharedPreferences = this.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

        id = sharedPreferences.getString(TAG_ID, id);
        level = sharedPreferences.getString(TAG_LEVEL, level);
        idComp = sharedPreferences.getString(ID_COMP, idComp);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, idCompany);
        codeComp = sharedPreferences.getString(CODE_COMP, codeComp);

        Intent intent = getIntent();
        id = intent.getStringExtra(TAG_ID);
        level = intent.getStringExtra(TAG_LEVEL);
        idCompany = intent.getStringExtra(TAG_IDCOMP);
        nameCompany = intent.getStringExtra(TAG_COMP);
        codeComp = intent.getStringExtra(CODE_COMP);
        addrComp = intent.getStringExtra(ADDR_COMP);
        phoneComp = intent.getStringExtra(PHONE_COMP);
        emailComp = intent.getStringExtra(EMAIL_COMP);

        inputNameComp = findViewById(R.id.editTxtNameCompProEdit);
        inputAddrComp = findViewById(R.id.editTxtAddrCompProEdit);
        inputTlpComp = findViewById(R.id.editTxtTlpCompProEdit);
        inputEmailCOmp = findViewById(R.id.editTxtEmailCompProEdit);
        inputCodeComp = findViewById(R.id.editTxtCodeCompProEdit);
        imgCompEdit = findViewById(R.id.imgCompEdit);

        detailComp();

        btnSaveEditCompPro = findViewById(R.id.btnSaveCompProEdit);
        btnSaveEditCompPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCompPro();
            }
        });

        btnBackEditCompPro = findViewById(R.id.btnBackCompProEdit);
        btnBackEditCompPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgCompEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_twotone_chevron_left_24);
    }

    // this function is triggered when
    // the Select Image Button is clicked
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

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout

                    Picasso.get().load(selectedImageUri)
                            .resize(450, 450)
                            .centerCrop()
                            .into(imgCompEdit);

                    String imageCompPath = getRealPathFromURI(selectedImageUri,EditCompProActivity.this);

                    imgProfileComp = new File(imageCompPath);

//                    imgUser.setImageURI(selectedImageUri);
                }
            }
        }
    }

    // Edit Profile
    public void editCompPro(){
        progressDialog = new ProgressDialog(EditCompProActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Profil..");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        AndroidNetworking.upload(urlCompProEdit)
                .addMultipartParameter("idComp", idCompany.toString())
                .addMultipartParameter("nameComp", inputNameComp.getText().toString())
                .addMultipartParameter("addrComp", inputAddrComp.getText().toString())
                .addMultipartParameter("phoneComp", inputTlpComp.getText().toString())
                .addMultipartParameter("emailComp", inputEmailCOmp.getText().toString())
                .addMultipartParameter("codeComp", inputCodeComp.getText().toString())
                .addMultipartFile("logoComp", imgProfileComp)
                .setTag("Update Data..")
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
                                new AlertDialog.Builder(EditCompProActivity.this)
                                        .setMessage("Berhasil Mengudate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(EditCompProActivity.this, CompProActivity.class);
                                                intent.putExtra(TAG_ID, id);
                                                intent.putExtra(TAG_LEVEL, level);
                                                intent.putExtra(TAG_IDCOMP, idCompany);
                                                intent.putExtra(CODE_COMP, codeComp);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .show();
                            }else{
                                new AlertDialog.Builder(EditCompProActivity.this)
                                        .setMessage("Gagal Mengupdate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Log.i("Input", "Data: "+idCompany+", "+nameCompany+", "+codeComp+", "+addrComp+", "+phoneComp+", "+emailComp.toString());
                                                Intent i = getIntent();
                                                setResult(RESULT_CANCELED,i);
                                                EditCompProActivity.this.finish();
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
                        Toast.makeText(EditCompProActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();

                    }
                });
    }

    // Detail Profile
    public void detailComp(){
        progressDialog = new ProgressDialog(EditCompProActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Toko..");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);
//        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        AndroidNetworking.post(urlCompProDetail)
                .addBodyParameter("idComp", idCompany.toString())
                .addBodyParameter("codeComp", codeComp.toString())
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
//                                Log.d("idUser", jsonArray.getJSONObject(0).getString("idUser")); //mengambil data username dari json yg sudah diinput

                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                inputNameComp.setText(jsonObject.getString("nameComp"));
                                inputAddrComp.setText(jsonObject.getString("addrComp"));
                                inputTlpComp.setText(jsonObject.getString("phoneComp"));
                                inputEmailCOmp.setText(jsonObject.getString("emailComp"));
                                inputCodeComp.setText(jsonObject.getString("codeComp"));

                                Picasso.get().load(URL_COMP_IMG_DETAIL+jsonObject.getString("logoComp"))
                                        .resize(450, 450)
                                        .centerCrop()
                                        .placeholder(R.drawable.default_image_comp_small)
                                        .error(R.drawable.default_image_comp_small)
                                        .into(imgCompEdit);

                                getSupportActionBar().setTitle(jsonObject.getString("nameComp"));

                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditCompProActivity.this, "Maaf, gagal Terhubung ke Database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(EditCompProActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
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
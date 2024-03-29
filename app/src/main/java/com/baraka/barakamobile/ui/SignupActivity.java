package com.baraka.barakamobile.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.profile.EditProfileActivity;
import com.baraka.barakamobile.ui.util.DbConfig;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity {
    Button btnUploadPhoto, btnBackSignup, btnDaftarSignup;
    ImageButton btnSearchComp;
    Intent intent;
    TextView textPath, txtSignupComp, txtIdCompSearch;
    ProgressDialog progressDialog;
    EditText inputNameRegist, inputEmailRegist, inputPwRegist, inputAddrRegist, inputPhoneRegist, inputCompRegist, inputLevelRegist;
    int lvlUser;
    String idComp, nameComp, codeComp, addrComp, phoneComp, emailComp, logoComp;

    int SELECT_PICTURE = 200;

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    int success;
    ConnectivityManager connectivityManager;

    private String url = DbConfig.URL + "regist.php";
    private String URL_COMP_CEK = DbConfig.URL + "searchComp.php";
    private String URL_USER_IMG_EDIT = DbConfig.URL + "imgUser/";

    private static final String TAG = SignupActivity.class.getSimpleName();
    private static final int SIGNUP_COMP_REQUEST_CODE = 0;

    public static final String ID_COMP = "idComp";
    public static final String NAME_COMP = "nameComp";
    public static final String CODE_COMP = "codeComp";
    public static final String ADDR_COMP = "addrComp";
    public static final String PHONE_COMP = "phoneComp";
    public static final String EMAIL_COMP = "emailComp";
    public static final String LOGO_COMP = "logoComp";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";
    String WorkerNo = "1";
    String WorkerYes = "2";

    Switch swSignupWorker;
    Locale locale;

    Uri selectedImageUri;
    File imgRegist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        locale = new Locale("id","ID");
        Locale.setDefault(locale);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (connectivityManager.getActiveNetworkInfo() != null
                    && connectivityManager.getActiveNetworkInfo().isAvailable()
                    && connectivityManager.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        inputNameRegist = (EditText) findViewById(R.id.inputNameRegist);
        inputEmailRegist = (EditText) findViewById(R.id.inputEmailRegist);
        inputPwRegist = (EditText) findViewById(R.id.inputPwRegist);
        inputAddrRegist = (EditText) findViewById(R.id.inputAddrRegist);
        inputPhoneRegist = (EditText) findViewById(R.id.inputPhoneRegist);
        inputCompRegist = (EditText) findViewById(R.id.inputCompRegist);
        inputLevelRegist = (EditText) findViewById(R.id.inputLevelRegist);
        swSignupWorker = findViewById(R.id.swSignupWorker);
        btnSearchComp = (ImageButton) findViewById(R.id.btnSearchComp);
        txtIdCompSearch = (TextView) findViewById(R.id.txtIdCompSearch);


        Intent intentComp = getIntent();
        idComp = intentComp.getStringExtra(ID_COMP);
        nameComp = intentComp.getStringExtra(NAME_COMP);
        codeComp = intentComp.getStringExtra(CODE_COMP);
        addrComp = intentComp.getStringExtra(ADDR_COMP);
        phoneComp = intentComp.getStringExtra(PHONE_COMP);
        emailComp = intentComp.getStringExtra(EMAIL_COMP);
        logoComp = intentComp.getStringExtra(LOGO_COMP);

        btnBackSignup = (Button) findViewById(R.id.btnBackSignup);
        btnDaftarSignup = (Button) findViewById(R.id.btnDaftarSignup);

        btnUploadPhoto = (Button)findViewById(R.id.btnUploadPhotoRegist);

        btnSearchComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkComp();
            }
        });

        swSignupWorker.setChecked(false);
        swSignupWorker.setText("Tidak");

        swSignupWorker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(swSignupWorker.isChecked()){
                    swSignupWorker.setText("Ya");
                }
                else {
                    swSignupWorker.setText("Tidak");
                }
            }
        });

//        if (swSignupWorker.isChecked()){
//            swSignupWorker.setText("Ya");
//        }else {
//            swSignupWorker.setText("Tidak");
//        }

        txtSignupComp = (TextView)findViewById(R.id.txtSignupComp);
        txtSignupComp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                txtSignupComp.setTextColor(R.color.colorPrimary);
                Intent intentRegComp = new Intent(SignupActivity.this, SignupCompActivity.class);
                startActivityForResult(intentRegComp, SIGNUP_COMP_REQUEST_CODE);
            }
        });

        btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestPermission();
                imageChooser();

//                intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("*/*");
//                startActivityForResult(intent, 7);
            }
        });

        btnBackSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
                Intent intentBackSingup = new Intent(SignupActivity.this, LoginActivity.class);
                finish();
                startActivity(intentBackSingup);
            }
        });

        btnDaftarSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (TextUtils.isEmpty(inputNameRegist.getText().toString())
                        && TextUtils.isEmpty(inputEmailRegist.getText().toString())
                        && TextUtils.isEmpty(inputAddrRegist.getText().toString())
                        && TextUtils.isEmpty(inputPhoneRegist.getText().toString())
                        && TextUtils.isEmpty(inputLevelRegist.getText().toString())){
                    inputNameRegist.setError("Harap Masukan Nama Anda!");
                    inputNameRegist.requestFocus();
                    inputEmailRegist.setError("Harap Masukan Email Anda!");
                    inputEmailRegist.requestFocus();
                    inputAddrRegist.setError("Harap Masukan Alamat Anda!");
                    inputAddrRegist.requestFocus();
                    inputPhoneRegist.setError("Harap Masukan No. Tlp Anda!");
                    inputPhoneRegist.requestFocus();
                    inputLevelRegist.setError("Harap Masukan Posisi Anda!");
                    inputLevelRegist.requestFocus();

                }else {
                    if (connectivityManager.getActiveNetworkInfo() != null
                            && connectivityManager.getActiveNetworkInfo().isAvailable()
                            && connectivityManager.getActiveNetworkInfo().isConnected()) {
                        if (swSignupWorker.isChecked()){
//                            swSignupWorker.setText("Ya");
                            addUserWorker();
                        }else{
                            addUserOwner();
                        }
//                        addUserOwner();
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), SELECT_PICTURE);
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

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(SignupActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        textPath = (TextView)findViewById(R.id.textViewPhotoRegist);

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

//                    Picasso.get().load(selectedImageUri)
//                            .resize(450, 450)
//                            .centerCrop()
//                            .into(imgUser);

                    String imagepath = getRealPathFromURI(selectedImageUri, SignupActivity.this);

                    imgRegist = new File(imagepath);

//                    imgUser.setImageURI(selectedImageUri);
                }
            }
        }
    }

    private void addUserOwner() {

//        if (TextUtils.isEmpty(inputNameComp.getText().toString())
//                && TextUtils.isEmpty(inputAddrComp.getText().toString())
//                && TextUtils.isEmpty(inputPhoneComp.getText().toString())) {
//
//        }else{
//            lvlUser = 2;
//        }

        ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Produk");

        AndroidNetworking.upload(url)
                .addMultipartParameter("nameUser", inputNameRegist.getText().toString())
                .addMultipartParameter("emailUser", inputEmailRegist.getText().toString())
                .addMultipartParameter("pwUser", inputPwRegist.getText().toString())
                .addMultipartParameter("addrUser", inputAddrRegist.getText().toString())
                .addMultipartParameter("compUser", txtIdCompSearch.getText().toString())
                .addMultipartParameter("postUser", inputLevelRegist.getText().toString())
                .addMultipartParameter("lvlUser", WorkerNo)
                .addMultipartParameter("phoneUser", inputPhoneRegist.getText().toString())
                .addMultipartFile("imgUser", imgRegist)
                .setTag("Tambah Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject data) {
                        progressDialog.dismiss();
                        Log.d("Respon Add",""+data);

                        try {
                            Boolean status = data.getBoolean("success");
                            if (status == true){
                                new AlertDialog.Builder(SignupActivity.this)
                                        .setMessage("Pendaftaran Akun Anda Berhasil!")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent intentBackSingup = new Intent(SignupActivity.this, LoginActivity.class);
                                                finish();
                                                startActivity(intentBackSingup);
                                            }
                                        })
                                        .show();
                            }else{
                                new AlertDialog.Builder(SignupActivity.this)
                                        .setMessage("Pendaftaran Akun Anda Gagal!")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Log.i("Input", "Data: "+inputNameRegist+", " +
                                                        ""+inputEmailRegist+", " +
                                                        ""+inputAddrRegist+", " +
                                                        ""+inputCompRegist+", " +
                                                        ""+inputLevelRegist+", " +
                                                        ""+inputPhoneRegist.toString());
                                                Intent intentBackSingup = new Intent(SignupActivity.this, LoginActivity.class);
                                                finish();
                                                startActivity(intentBackSingup);
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
                        Toast.makeText(SignupActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();

                    }
                });

    }

    private void addUserWorker() {

//        if (TextUtils.isEmpty(inputNameComp.getText().toString())
//                && TextUtils.isEmpty(inputAddrComp.getText().toString())
//                && TextUtils.isEmpty(inputPhoneComp.getText().toString())) {
//
//        }else{
//            lvlUser = 2;
//        }

        ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Produk");

        AndroidNetworking.upload(url)
                .addMultipartParameter("nameUser", inputNameRegist.getText().toString())
                .addMultipartParameter("emailUser", inputEmailRegist.getText().toString())
                .addMultipartParameter("pwUser", inputPwRegist.getText().toString())
                .addMultipartParameter("addrUser", inputAddrRegist.getText().toString())
                .addMultipartParameter("compUser", txtIdCompSearch.getText().toString())
                .addMultipartParameter("postUser", inputLevelRegist.getText().toString())
                .addMultipartParameter("lvlUser", WorkerYes)
                .addMultipartParameter("phoneUser", inputPhoneRegist.getText().toString())
                .addMultipartFile("imgUser", imgRegist)
                .setTag("Tambah Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject data) {
                        progressDialog.dismiss();
                        Log.d("Respon Add",""+data);

                        try {
                            Boolean status = data.getBoolean("success");
                            if (status == true){
                                new AlertDialog.Builder(SignupActivity.this)
                                        .setMessage("Pendaftaran Akun Anda Berhasil!")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intentBackSingup = new Intent(SignupActivity.this, LoginActivity.class);
                                                finish();
                                                startActivity(intentBackSingup);
                                            }
                                        })
                                        .show();
                            }else{
                                new AlertDialog.Builder(SignupActivity.this)
                                        .setMessage("Pendaftaran Akun Anda Gagal!")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Log.i("Input", "Data: "+inputNameRegist+", " +
                                                        ""+inputEmailRegist+", " +
                                                        ""+inputAddrRegist+", " +
                                                        ""+inputCompRegist+", " +
                                                        ""+inputLevelRegist+", " +
                                                        ""+inputPhoneRegist.toString());
                                                Intent intentBackSingup = new Intent(SignupActivity.this, LoginActivity.class);
                                                finish();
                                                startActivity(intentBackSingup);
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
                        Toast.makeText(SignupActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();

                    }
                });

    }

    private void checkComp() {


        ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Pencarian Toko..");

        AndroidNetworking.post(URL_COMP_CEK)
                .addBodyParameter("codeComp", inputCompRegist.getText().toString())
                .setTag("Check Data Toko..")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.i("Info", "Data: " + response.toString());

                        try {
                            int success = response.getInt("success");
                            if (success==1) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                jsonObject.getString("idComp");
                                jsonObject.getString("nameComp");
                                jsonObject.getString("codeComp");
                                jsonObject.getString("addrComp");
                                jsonObject.getString("phoneComp");
                                jsonObject.getString("emailComp");
                                jsonObject.getString("logoComp");

                                txtIdCompSearch.setText(jsonObject.getString("idComp"));

                                Toast.makeText(SignupActivity.this, "Toko Terdaftar!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(SignupActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();
                    }
                });

    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}
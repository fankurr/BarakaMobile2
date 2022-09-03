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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.supplier.SplrCardAdapter;
import com.baraka.barakamobile.ui.supplier.SplrViewModel;
import com.baraka.barakamobile.ui.supplier.SupplierFragment;
import com.baraka.barakamobile.ui.util.DbConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Locale;
import java.util.Random;

public class SignupCompActivity extends AppCompatActivity {
    private String URL_COMP_REG = DbConfig.URL_COMP + "addComp.php";
    private String URL_COMP_CEK = DbConfig.URL_COMP + "idComp.php";
    private String URL_COMP_IMG = DbConfig.URL_COMP + "imgUser/";

    EditText inputNameComp, inputCodeComp, inputCityComp, inputAddrComp, inputPhoneComp, inputEmailComp;
    Button btnUploadPhotoComp, btnBackSignupComp, btnDaftarSignupComp;
    TextView textPathComp, txtGenerateCodeComp;
    String idComp, nameComp, codeComp, cityComp, addrComp, phoneComp, emailComp, logoComp;

    public static final String ID_COMP = "idComp";
    public static final String NAME_COMP = "nameComp";
    public static final String CODE_COMP = "codeComp";
    public static final String CITY_COMP = "cityComp";
    public static final String ADDR_COMP = "addrComp";
    public static final String PHONE_COMP = "phoneComp";
    public static final String EMAIL_COMP = "emailComp";
    public static final String LOGO_COMP = "logoComp";

    Random random = new Random();
    private static final String _CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int RANDOM_STR_LENGTH = 9;

    int SELECT_PICTURE = 200;

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    ProgressDialog progressDialog;
    ConnectivityManager connectivityManager;

    Locale locale;

    Uri selectedImageUri;
    File imgRegistComp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_comp);

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

        btnBackSignupComp = findViewById(R.id.btnBackSignupComp);
        btnDaftarSignupComp = findViewById(R.id.btnDaftarSignupComp);
        btnUploadPhotoComp = findViewById(R.id.btnUploadPhotoRegistComp);

        inputNameComp = findViewById(R.id.inputNameCompRegist);
        inputCodeComp = findViewById(R.id.inputCodeCompRegist);
        inputCityComp = findViewById(R.id.inputCityCompRegist);
        inputAddrComp = findViewById(R.id.inputAddrCompRegist);
        inputPhoneComp = findViewById(R.id.inputPhoneCompRegist);
        inputEmailComp = findViewById(R.id.inputEmailCompRegist);

        txtGenerateCodeComp = findViewById(R.id.txtGenerateCodeComp);
        txtGenerateCodeComp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                txtGenerateCodeComp.setTextColor(R.color.colorPrimary);
                String nameStore = inputNameComp.getText().toString();
                String nameStoreCode = nameStore.replace(" ","");
                inputCodeComp.setText(nameStoreCode + getRandomString());
                inputCodeComp.setError(null);
            }
        });

        btnUploadPhotoComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestPermission();

//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("*/*");
//                startActivityForResult(intent, 7);
            }
        });

        btnBackSignupComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignupCompActivity.super.onBackPressed();
            }
        });

        btnDaftarSignupComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(inputNameComp.getText().toString())
                        && TextUtils.isEmpty(inputCodeComp.getText().toString())
                        && TextUtils.isEmpty(inputAddrComp.getText().toString())
                        && TextUtils.isEmpty(inputPhoneComp.getText().toString())){
                    inputNameComp.setError("Harap Masukan Nama Toko!");
                    inputNameComp.requestFocus();
                    inputCodeComp.setError("Harap Masukan Code Toko! Jika Anda bingung, Click 'Generate!'");
                    inputCityComp.requestFocus();
                    inputCityComp.setError("Harap Masukan Kota Domisili Toko!");
                    inputCodeComp.requestFocus();
                    inputAddrComp.setError("Harap Masukan Alamat Toko!");
                    inputAddrComp.requestFocus();
                    inputPhoneComp.setError("Harap Masukan No. Tlp Toko!");
                    inputPhoneComp.requestFocus();

                }else {
                    if (connectivityManager.getActiveNetworkInfo() != null
                            && connectivityManager.getActiveNetworkInfo().isAvailable()
                            && connectivityManager.getActiveNetworkInfo().isConnected()) {
                        addComp();
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(SignupCompActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

        imageChooser();
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


    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        textPathComp = (TextView)findViewById(R.id.textViewPhotoRegistComp);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                selectedImageUri = data.getData();
                String PathHolder = data.getData().getPath();
                textPathComp.setText(PathHolder);
                if (null != selectedImageUri) {
                    // update the preview image in the layout

//                    Picasso.get().load(selectedImageUri)
//                            .resize(450, 450)
//                            .centerCrop()
//                            .into(imgUser);

                    String imagepath = getRealPathFromURI(selectedImageUri, SignupCompActivity.this);

                    imgRegistComp = new File(imagepath);

//                    imgUser.setImageURI(selectedImageUri);
                }
            }
        }
    }

    public String getRandomString(){
        StringBuffer randStr = new StringBuffer();
        for (int i = 0; i < RANDOM_STR_LENGTH; i++) {
            int number = getRandomNumber();
            char ch = _CHAR.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
    }

    private int getRandomNumber() {
        int randomInt = 0;
        randomInt = random.nextInt(_CHAR.length());
        if (randomInt - 1 == -1) {
            return randomInt;
        } else {
            return randomInt - 1;
        }
    }

    private void addComp() {

        nameComp = inputNameComp.getText().toString();
        codeComp = inputCodeComp.getText().toString();
        cityComp = inputCityComp.getText().toString();
        addrComp = inputAddrComp.getText().toString();
        phoneComp = inputPhoneComp.getText().toString();
        emailComp = inputEmailComp.getText().toString();

        ProgressDialog progressDialog = new ProgressDialog(SignupCompActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Pendaftaran Toko Sedang Diproses..");
        progressDialog.show();

        AndroidNetworking.upload(URL_COMP_REG)
                .addMultipartParameter("nameComp", nameComp)
                .addMultipartParameter("codeComp", codeComp)
                .addMultipartParameter("cityComp", cityComp)
                .addMultipartParameter("addrComp", addrComp)
                .addMultipartParameter("phoneComp", phoneComp)
                .addMultipartParameter("emailComp", emailComp)
                .addMultipartFile("logoComp", imgRegistComp)
                .setTag("Tambah Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("Respon Add",""+response);

                        try {
                            Boolean status = response.getBoolean("success");
                            if (status == true){
                                new AlertDialog.Builder(SignupCompActivity.this)
                                        .setMessage("Pendaftaran Toko Anda Berhasil!")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intentComp = new Intent();
                                                intentComp.putExtra(ID_COMP, idComp);
                                                intentComp.putExtra(NAME_COMP, nameComp);
                                                intentComp.putExtra(CODE_COMP, codeComp);
                                                intentComp.putExtra(CITY_COMP, cityComp);
                                                intentComp.putExtra(ADDR_COMP, addrComp);
                                                intentComp.putExtra(PHONE_COMP, phoneComp);
                                                intentComp.putExtra(EMAIL_COMP, emailComp);
                                                intentComp.putExtra(LOGO_COMP, logoComp);

                                                Intent intentBackSingup = new Intent(SignupCompActivity.this, SignupActivity.class);
                                                finish();
                                                startActivity(intentBackSingup);
                                            }
                                        })
                                        .show();
                            }else{
                                new AlertDialog.Builder(SignupCompActivity.this)
                                        .setMessage("Pendaftaran Toko Anda Gagal!")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Log.i("Input", "Data: "+inputNameComp+", "+inputAddrComp+", "+inputPhoneComp+", "+inputEmailComp.toString());
                                                Intent intentBackSingup = new Intent(SignupCompActivity.this, SignupActivity.class);
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
                        Toast.makeText(SignupCompActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();

                    }
                });

    }



//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//        textPathComp = (TextView)findViewById(R.id.textViewPhotoRegistComp);
//
//        switch (requestCode) {
//            case 7:
//                if (resultCode==RESULT_OK) {
//                    String PathHolder = data.getData().getPath();
//                    textPathComp.setText(PathHolder);
//                }
//                break;
//        }
//    }
}
package com.baraka.barakamobile.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.util.DbConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    Button btnUploadPhoto, btnBackSignup, btnDaftarSignup;
    Intent intent;
    TextView textPath, txtSignupComp;
    ProgressDialog progressDialog;
    EditText inputNameRegist, inputEmailRegist, inputPwRegist, inputAddrRegist, inputPhoneRegist, inputCompRegist, inputLevelRegist;
    int lvlUser;
    String idComp, nameComp, codeComp, addrComp, phoneComp, emailComp, logoComp;

    int success;
    ConnectivityManager connectivityManager;

    private String url = DbConfig.URL + "regist.php";
    private String URL_COMP_CEK = DbConfig.URL_COMP + "idComp.php";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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

                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 7);
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

        AndroidNetworking.post(url)
                .addBodyParameter("nameUser", inputNameRegist.getText().toString())
                .addBodyParameter("emailUser", inputEmailRegist.getText().toString())
                .addBodyParameter("pwUser", inputPwRegist.getText().toString())
                .addBodyParameter("addrUser", inputAddrRegist.getText().toString())
                .addBodyParameter("compUser", inputCompRegist.getText().toString())
                .addBodyParameter("postUser", inputLevelRegist.getText().toString())
                .addBodyParameter("lvlUser", WorkerNo)
                .addBodyParameter("phoneUser", inputPhoneRegist.getText().toString())
                .setTag("Tambah Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject data) {
                        progressDialog.dismiss();
                        Log.d("Respon Add",""+data);

                        try {
                            Boolean status = data.getBoolean("status");
                            if (status == true){
                                new AlertDialog.Builder(SignupActivity.this)
                                        .setMessage("Pendaftaran Toko Anda Berhasil!")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = getIntent();
                                                setResult(RESULT_OK, intent);
                                                SignupActivity.this.finish();
                                            }
                                        })
                                        .show();
                            }else{
                                new AlertDialog.Builder(SignupActivity.this)
                                        .setMessage("Pendaftaran Toko Anda Gagal!")
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
                                                Intent i = getIntent();
                                                setResult(RESULT_CANCELED,i);
                                                SignupActivity.this.finish();
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

        AndroidNetworking.post(url)
                .addBodyParameter("nameUser", inputNameRegist.getText().toString())
                .addBodyParameter("emailUser", inputEmailRegist.getText().toString())
                .addBodyParameter("pwUser", inputPwRegist.getText().toString())
                .addBodyParameter("addrUser", inputAddrRegist.getText().toString())
                .addBodyParameter("compUser", inputCompRegist.getText().toString())
                .addBodyParameter("postUser", inputLevelRegist.getText().toString())
                .addBodyParameter("lvlUser", WorkerYes)
                .addBodyParameter("phoneUser", inputPhoneRegist.getText().toString())
                .setTag("Tambah Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject data) {
                        progressDialog.dismiss();
                        Log.d("Respon Add",""+data);

                        try {
                            Boolean status = data.getBoolean("status");
                            if (status == true){
                                new AlertDialog.Builder(SignupActivity.this)
                                        .setMessage("Pendaftaran Toko Anda Berhasil!")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = getIntent();
                                                setResult(RESULT_OK, intent);
                                                SignupActivity.this.finish();
                                            }
                                        })
                                        .show();
                            }else{
                                new AlertDialog.Builder(SignupActivity.this)
                                        .setMessage("Pendaftaran Toko Anda Gagal!")
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
                                                Intent i = getIntent();
                                                setResult(RESULT_CANCELED,i);
                                                SignupActivity.this.finish();
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
        progressDialog.setMessage("Pendaftaran Toko Sedang Diproses..");

        AndroidNetworking.post(URL_COMP_CEK)
                .addBodyParameter("idComp", idComp)
                .setTag("Check Data Toko..")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject data) {
                        progressDialog.dismiss();
                        Log.i("Info", "Data: " + data.toString());

                        try {
                            JSONArray jsonArray = data.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                SignupCompViewModel signupCompViewModel = new SignupCompViewModel(
                                        jsonObject.getString("idComp"),
                                        jsonObject.getString("nameComp"),
                                        jsonObject.getString("codeComp"),
                                        jsonObject.getString("addrComp"),
                                        jsonObject.getString("phoneComp"),
                                        jsonObject.getString("emailComp"),
                                        jsonObject.getString("logoComp")

                                );
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        textPath = (TextView)findViewById(R.id.textViewPhotoRegist);

        switch (requestCode) {
            case 7:
                if (resultCode==RESULT_OK) {
                    String PathHolder = data.getData().getPath();
                    textPath.setText(PathHolder);
                }
                break;
        }

        // Check that it is the SecondActivity with an OK result
        if (requestCode == SIGNUP_COMP_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

//                checkComp();

//                Intent intentComp = getIntent();
                idComp = data.getStringExtra(ID_COMP);
                nameComp = data.getStringExtra(NAME_COMP);
                codeComp = data.getStringExtra(CODE_COMP);
                addrComp = data.getStringExtra(ADDR_COMP);
                phoneComp = data.getStringExtra(PHONE_COMP);
                emailComp = data.getStringExtra(EMAIL_COMP);
                logoComp = data.getStringExtra(LOGO_COMP);

                Log.e("Comp: ", "Data: "+idComp+", "
                        +nameComp+", "
                        +codeComp+", "
                        +addrComp+", "
                        +phoneComp+", "
                        +emailComp+", "
                        +logoComp);

                // Get String data from Intent
                String returnString = data.getStringExtra(CODE_COMP);

                inputCompRegist = (EditText) findViewById(R.id.inputCompRegist);
                inputCompRegist.setText(returnString);
                inputCompRegist.setEnabled(false);
            }
        }
    }
}
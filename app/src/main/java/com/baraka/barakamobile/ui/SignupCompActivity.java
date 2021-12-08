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

import java.util.Random;

public class SignupCompActivity extends AppCompatActivity {
    private String URL_COMP_REG = DbConfig.URL_COMP + "addComp.php";
    private String URL_COMP_CEK = DbConfig.URL_COMP + "idComp.php";

    EditText inputNameComp, inputCodeComp, inputAddrComp, inputPhoneComp, inputEmailComp;
    Button btnUploadPhotoComp, btnBackSignupComp, btnDaftarSignupComp;
    TextView textPathComp, txtGenerateCodeComp;
    String idComp, nameComp, codeComp, addrComp, phoneComp, emailComp, logoComp;

    public static final String ID_COMP = "idComp";
    public static final String NAME_COMP = "nameComp";
    public static final String CODE_COMP = "codeComp";
    public static final String ADDR_COMP = "addrComp";
    public static final String PHONE_COMP = "phoneComp";
    public static final String EMAIL_COMP = "emailComp";
    public static final String LOGO_COMP = "logoComp";

    Random random = new Random();
    private static final String _CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int RANDOM_STR_LENGTH = 9;

    ProgressDialog progressDialog;
    ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_comp);

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

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 7);
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
        addrComp = inputAddrComp.getText().toString();
        phoneComp = inputPhoneComp.getText().toString();
        emailComp = inputEmailComp.getText().toString();

        ProgressDialog progressDialog = new ProgressDialog(SignupCompActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Pendaftaran Toko Sedang Diproses..");
        progressDialog.show();

        AndroidNetworking.post(URL_COMP_REG)
                .addBodyParameter("nameComp", nameComp)
                .addBodyParameter("codeComp", codeComp)
                .addBodyParameter("addrComp", addrComp)
                .addBodyParameter("phoneComp", phoneComp)
                .addBodyParameter("emailComp", emailComp)
                .addBodyParameter("logoComp", "")
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
                                                intentComp.putExtra(ADDR_COMP, addrComp);
                                                intentComp.putExtra(PHONE_COMP, phoneComp);
                                                intentComp.putExtra(EMAIL_COMP, emailComp);
                                                intentComp.putExtra(LOGO_COMP, logoComp);
                                                setResult(RESULT_OK, intentComp);
                                                finish();
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
                                                Intent i = getIntent();
                                                setResult(RESULT_CANCELED,i);
                                                SignupCompActivity.this.finish();
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        textPathComp = (TextView)findViewById(R.id.textViewPhotoRegistComp);

        switch (requestCode) {
            case 7:
                if (resultCode==RESULT_OK) {
                    String PathHolder = data.getData().getPath();
                    textPathComp.setText(PathHolder);
                }
                break;
        }
    }
}
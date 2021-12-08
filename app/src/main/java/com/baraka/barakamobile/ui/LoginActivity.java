package com.baraka.barakamobile.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.MainActivity;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.config.AppController;
import com.baraka.barakamobile.ui.util.DbConfig;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    private EditText InputEmailLogin, InputPassLogin;
    private Button btnLogin;
    private Button btnDaftar;
    Intent intent;

    int success;
    ConnectivityManager connectivityManager;

    private String url = DbConfig.URL + "login.php";

//    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

//    private final static String TAG_ID = "idUser";
//    private final static String TAG_EMAIL = "emailUser";
//    private final static String TAG_NAME = "nameUser";
//    private final static String TAG_LEVEL = "lvlUser";
//    private final static String TAG_ACCESS = "loginUser";
//    private final static String TAG_IDCOMP = "compUser";
//    private final static String TAG_COMP = "nameComp";

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

    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedPreferences;
    Boolean session = false;
    String id, email, name, address, level, postUser, phone, access, idCompany, nameCompany;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InputEmailLogin = (EditText) findViewById(R.id.inputEmailLogin);
        InputPassLogin = (EditText) findViewById(R.id.inputPassLogin);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnDaftar = (Button) findViewById(R.id.btnDaftar);

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

        // Cek session login jika TRUE maka langsung buka MainActivity
        sharedPreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedPreferences.getBoolean(session_status, false);
        id = sharedPreferences.getString(TAG_ID, null);
        email = sharedPreferences.getString(TAG_EMAIL, null);
        name = sharedPreferences.getString(TAG_NAME, null);
        address = sharedPreferences.getString(TAG_ADDRESS, null);
        level = sharedPreferences.getString(TAG_LEVEL, null);
        postUser = sharedPreferences.getString(TAG_POST, null);
        phone = sharedPreferences.getString(TAG_TLP, null);
        access = sharedPreferences.getString(TAG_ACCESS, null);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);
        nameCompany = sharedPreferences.getString(TAG_COMP, null);

//        if (email != null) {
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//        }

        if (session && email != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(TAG_ID, id);
            intent.putExtra(TAG_EMAIL, email);
            intent.putExtra(TAG_NAME, name);
            intent.putExtra(TAG_ADDRESS, address);
            intent.putExtra(TAG_LEVEL, level);
            intent.putExtra(TAG_POST, postUser);
            intent.putExtra(TAG_TLP, phone);
            intent.putExtra(TAG_ACCESS, access);
            intent.putExtra(TAG_IDCOMP, idCompany);
            intent.putExtra(TAG_COMP, nameCompany);
            finish();
            startActivity(intent);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Login..");
                progressDialog.show();

                AndroidNetworking.post(url)
                        .addBodyParameter("emailUser", InputEmailLogin.getText().toString())
                        .addBodyParameter("pwUser", InputPassLogin.getText().toString())
                        .setTag(this)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response
                                try {
                                    //mengambil pesan dari json
                                    int success = response.getInt("success");

                                    if(success==1) {
                                        JSONArray jsonArray = response.getJSONArray("data"); // mengambil [data] dari json
                                        Log.d("email", jsonArray.getJSONObject(0).getString("email")); //mengambil data username dari json yg sudah diinput


                                        Toast.makeText(LoginActivity.this, "Login Success ", Toast.LENGTH_SHORT).show();

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            id = jsonObject.getString("id");
                                            email =  jsonObject.getString("email");
                                            name =  jsonObject.getString("name");
                                            address =  jsonObject.getString("address");
                                            level = jsonObject.getString("level");
                                            postUser = jsonObject.getString("postUser");
                                            phone = jsonObject.getString("phone");
                                            access =  jsonObject.getString("access");
                                            idCompany =  jsonObject.getString("company");
                                            nameCompany = jsonObject.getString("nameCompany");

                                            sharedPreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putBoolean(session_status, true);
                                            editor.putString(TAG_ID, id);
                                            editor.putString(TAG_EMAIL,InputEmailLogin.getText().toString());
                                            editor.putString(TAG_NAME, name);
                                            editor.putString(TAG_ADDRESS, address);
                                            editor.putString(TAG_LEVEL, level);
                                            editor.putString(TAG_POST, postUser);
                                            editor.putString(TAG_TLP, phone);
                                            editor.putString(TAG_ACCESS, access);
                                            editor.putString(TAG_IDCOMP, idCompany);
                                            editor.putString(TAG_COMP, nameCompany);
                                            editor.commit();

                                            progressDialog.dismiss();

                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.putExtra(TAG_ID, id);
                                            intent.putExtra(TAG_EMAIL, email);
                                            intent.putExtra(TAG_NAME, name);
                                            intent.putExtra(TAG_ADDRESS, address);
                                            intent.putExtra(TAG_LEVEL, level);
                                            intent.putExtra(TAG_POST, postUser);
                                            intent.putExtra(TAG_TLP, phone);
                                            intent.putExtra(TAG_ACCESS, access);
                                            intent.putExtra(TAG_IDCOMP, idCompany);
                                            intent.putExtra(TAG_COMP, nameCompany);
                                            finish();
                                            startActivity(intent);

                                            Log.e("Successfully Login!", response.toString());

                                            Log.e("Profile: ", "ID User: " + id +
                                                    ", Name: " + name +
                                                    ", Address: " +address+
                                                    ", Email: " + email +
                                                    ", Level: "+level+
                                                    ", Position: " +postUser+
                                                    ", Phone: " +phone+
                                                    ", Access: " +access+
                                                    ", ID Comp: " +idCompany+
                                                    ", Company: "+nameCompany.toString());

                                            Toast.makeText(LoginActivity.this, response.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

//                                        Toast.makeText(LoginActivity.this, "Login Success ", Toast.LENGTH_SHORT).show();

                                        }

                                    } else if(success==1) {
                                        Toast.makeText(LoginActivity.this, "Data tidak tersedia, regis dulu yaa", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(LoginActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                            @Override
                            public void onError(ANError error) {
                                // handle error
                                Toast.makeText(LoginActivity.this, "Koneksi Gagal: " + error.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("ERROR","error => "+ error.toString());
                                progressDialog.dismiss();
                            }
                        });
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                finish();
                startActivity(intent);

            }
        });
    }
}
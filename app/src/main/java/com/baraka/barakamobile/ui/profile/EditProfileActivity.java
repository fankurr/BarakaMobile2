package com.baraka.barakamobile.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import com.baraka.barakamobile.ui.product.AddEditPrdctActivity;
import com.baraka.barakamobile.ui.util.DbConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditProfileActivity extends AppCompatActivity {
    String id, email, name, address, level, postUser, phone, access, idCompany, nameCompany;
    String idComp, nameComp, codeComp, addrComp, phoneComp, emailComp, logoComp;
    EditText inputNameProfileEdit, inputPostProfileEdit, inputAddrProfileEdit, inputTlpProfileEdit, inputEmailProfileEdit;
    TextView txtViewCompProfileEdit;

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    private String urlEdit = DbConfig.URL + "editUser.php";

    private String urlUserEdit = DbConfig.URL + "idUserComp.php";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        progressDialog = new ProgressDialog(EditProfileActivity.this);

        Intent intent = getIntent();
        id = intent.getStringExtra(TAG_ID);
        name = intent.getStringExtra(TAG_NAME);
        email = intent.getStringExtra(TAG_EMAIL);
        address = intent.getStringExtra(TAG_ADDRESS);
        postUser = intent.getStringExtra(TAG_POST);
        phone = intent.getStringExtra(TAG_TLP);
        access = intent.getStringExtra(TAG_ACCESS);
        level = intent.getStringExtra(TAG_LEVEL);

        idCompany = intent.getStringExtra(TAG_IDCOMP);
        nameCompany = intent.getStringExtra(TAG_COMP);
        codeComp = intent.getStringExtra(CODE_COMP);
        addrComp = intent.getStringExtra(ADDR_COMP);
        phoneComp = intent.getStringExtra(PHONE_COMP);
        emailComp = intent.getStringExtra(EMAIL_COMP);

        inputNameProfileEdit = findViewById(R.id.editTxtNameProfileEdit);
        txtViewCompProfileEdit = findViewById(R.id.txtViewCompProfileEdit);
        inputPostProfileEdit = findViewById(R.id.editTxtPostProfileEdit);
        inputAddrProfileEdit = findViewById(R.id.editTxtAddrProfileEdit);
        inputTlpProfileEdit = findViewById(R.id.editTxtTlpProfileEdit);
        inputEmailProfileEdit = findViewById(R.id.editTxtEmailProfileEdit);

//        inputNameProfileEdit.setText(name);
//        txtViewCompProfileEdit.setText(nameCompany);
//        inputPostProfileEdit.setText(postUser);
//        inputAddrProfileEdit.setText(address);
//        inputEmailProfileEdit.setText(email);
//        inputTlpProfileEdit.setText(phone);

        editDetailProfile();

        inputPostProfileEdit.setEnabled(false);
        inputPostProfileEdit.setFocusable(false);

        Button btnSaveProfileEdit = findViewById(R.id.btnSaveProfileEdit);
        btnSaveProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                progressDialog.setMessage("Merubah Data...");
//                progressDialog.setCancelable(false);
//                progressDialog.show();

                editProfile();
//                progressDialog.dismiss();
            }
        });

        Button btnBackProfileEdit = findViewById(R.id.btnBackProfileEdit);
        btnBackProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_twotone_chevron_left_24);
    }

    // Edit Profile
    public void editProfile(){
        progressDialog = new ProgressDialog(EditProfileActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Produk");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        AndroidNetworking.post(urlEdit)
                .addBodyParameter("idUser", id.toString())
                .addBodyParameter("nameUser", inputNameProfileEdit.getText().toString())
                .addBodyParameter("emailUser", inputEmailProfileEdit.getText().toString())
                .addBodyParameter("addrUser", inputAddrProfileEdit.getText().toString())
                .addBodyParameter("phoneUser", inputTlpProfileEdit.getText().toString())
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
                                new AlertDialog.Builder(EditProfileActivity.this)
                                        .setMessage("Berhasil Mengudate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .show();
                            }else{
                                new AlertDialog.Builder(EditProfileActivity.this)
                                        .setMessage("Gagal Mengupdate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Log.i("Input", "Data: "+id+", "+name+", "+postUser+", "+address+", "+email+", "+phone.toString());
                                                Intent i = getIntent();
                                                setResult(RESULT_CANCELED,i);
                                                EditProfileActivity.this.finish();
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
                        Toast.makeText(EditProfileActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();

                    }
                });
    }

    // Detail Profile
    public void editDetailProfile(){
        progressDialog = new ProgressDialog(EditProfileActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Profil");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        AndroidNetworking.post(urlUserEdit)
                .addBodyParameter("idUser", id.toString())
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
//                                Log.d("idUser", jsonArray.getJSONObject(0).getString("idUser")); //mengambil data username dari json yg sudah diinput

                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                inputNameProfileEdit.setText(jsonObject.getString("name"));
                                inputPostProfileEdit.setText(jsonObject.getString("postUser"));
                                inputAddrProfileEdit.setText(jsonObject.getString("address"));
                                inputEmailProfileEdit.setText(jsonObject.getString("email"));
                                inputTlpProfileEdit.setText(jsonObject.getString("phone"));

                                txtViewCompProfileEdit.setText(jsonObject.getString("nameCompany"));
                                getSupportActionBar().setTitle(jsonObject.getString("name"));

                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditProfileActivity.this, "Maaf, gagal Terhubung ke Database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(EditProfileActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
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
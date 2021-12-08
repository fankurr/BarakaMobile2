package com.baraka.barakamobile.ui.supplier;

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

import org.json.JSONException;
import org.json.JSONObject;

import static com.baraka.barakamobile.ui.supplier.SupplierFragment.ADDR_SPLR;
import static com.baraka.barakamobile.ui.supplier.SupplierFragment.DESC_SPLR;
import static com.baraka.barakamobile.ui.supplier.SupplierFragment.EMAIL_SPLR;
import static com.baraka.barakamobile.ui.supplier.SupplierFragment.ID_SPLR;
import static com.baraka.barakamobile.ui.supplier.SupplierFragment.NAME_SPLR;
import static com.baraka.barakamobile.ui.supplier.SupplierFragment.PHONE_SPLR;

public class AddEditSplrActivity extends AppCompatActivity {

    public static final String ID_SPLR = "idSplr";
    public static final String COMP_SPLR= "idCompSplr";
    public static final String NAME_SPLR= "nameSplr";
    public static final String ADDR_SPLR = "addrSplr";
    public static final String PHONE_SPLR = "phoneSplr";
    public static final String EMAIL_SPLR = "emailSplr";
    public static final String DESC_SPLR = "descSplr";
    public static final String IMG_SPLR = "imgSplr";


    private final static String TAG_ID = "id";
    private final static String TAG_EMAIL = "email";
    private final static String TAG_NAME = "name";
    private final static String TAG_LEVEL = "level";
    private final static String TAG_ACCESS = "access";
    private final static String TAG_IDCOMP = "idCompany";
    private final static String TAG_COMP = "nameCompany";

    private String URL_SPLR_EDIT = DbConfig.URL_SPLR + "editSplr.php";
    private String URL_SPLR_ADD = DbConfig.URL_SPLR + "addSplr.php";

    String id, email, name, level, access, idCompany, nameCompany;
    String idSplr, nameSplr,descSplr,addrSplr, phoneSplr, emailSplr;
    EditText inputNameSplr, inputAlamatSplr, inputTlpSplr, inputEmailSplr, inputDescSplr;
    Button btnUploadImgSplrAddEdit;
    TextView textPath;

    ProgressDialog progressDialog;


    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_splr);

        progressDialog = new ProgressDialog(this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_twotone_chevron_left_24);

        Button btnBack = findViewById(R.id.btnBackSplrAddEdit);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        idSplr = intent.getStringExtra(ID_SPLR);
        nameSplr = intent.getStringExtra(NAME_SPLR);
        descSplr = intent.getStringExtra(DESC_SPLR);
        addrSplr = intent.getStringExtra(ADDR_SPLR);
        phoneSplr = intent.getStringExtra(PHONE_SPLR);
        emailSplr = intent.getStringExtra(EMAIL_SPLR);

        inputNameSplr = findViewById(R.id.editTextNameSplrAddEdit);
        inputAlamatSplr = findViewById(R.id.editTextMultiAlamatSplrAddEdit);
        inputTlpSplr = findViewById(R.id.editTextPhoneSplrAddEdit);
        inputEmailSplr = findViewById(R.id.editTextEmailSplrAddEdit);
        inputDescSplr = findViewById(R.id.editTextDescSplrAddEdit);

        btnUploadImgSplrAddEdit = (Button) findViewById(R.id.btnUploadImgSplrAddEdit);

        btnUploadImgSplrAddEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 7);

            }
        });

        if (nameSplr == null) {
            setTitle("Tambah Produk");

            inputNameSplr.setText(null);
            inputAlamatSplr.setText(null);
            inputTlpSplr.setText(null);
            inputEmailSplr.setText(null);
            inputDescSplr.setText(null);

            Button btnSimpanAdd = findViewById(R.id.btnSaveSplrAddEdit);
            btnSimpanAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addSPlr();
//                    onBackPressed();
                    AddEditSplrActivity.this.finish();

                }
            });

        } else {
            getSupportActionBar().setTitle(nameSplr);

            inputNameSplr.setText(nameSplr);
            inputAlamatSplr.setText(addrSplr);
            inputTlpSplr.setText(phoneSplr);
            inputEmailSplr.setText(emailSplr);
            inputDescSplr.setText(descSplr);

            Button btnSimpanEdit = findViewById(R.id.btnSaveSplrAddEdit);
            btnSimpanEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.setMessage("Merubah Data...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    editSplr();
                    progressDialog.dismiss();

                }
            });
        }

    }

    private void editSplr() {

        ProgressDialog progressDialog = new ProgressDialog(AddEditSplrActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Produk");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);


        AndroidNetworking.post(URL_SPLR_EDIT)
                .addBodyParameter("idSplr", idSplr)
                .addBodyParameter("nameSplr", inputNameSplr.getText().toString())
                .addBodyParameter("addrSplr", inputAlamatSplr.getText().toString())
                .addBodyParameter("phoneSplr", inputTlpSplr.getText().toString())
                .addBodyParameter("emailSplr", inputEmailSplr.getText().toString())
                .addBodyParameter("descSplr", inputDescSplr.getText().toString())
//                .addBodyParameter("imgSplr", input.getText().toString())
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
                                new AlertDialog.Builder(AddEditSplrActivity.this)
                                        .setMessage("Berhasil Mengudate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = getIntent();
                                                setResult(RESULT_OK, intent);
                                                AddEditSplrActivity.this.finish();
                                            }
                                        })
                                        .show();
                            }else{
                                new AlertDialog.Builder(AddEditSplrActivity.this)
                                        .setMessage("Gagal Mengupdate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Log.i("Input", "Data: "+idSplr+", "+nameSplr+", "+addrSplr+", "+phoneSplr+", "+emailSplr+", "+descSplr.toString());
                                                Intent i = getIntent();
                                                setResult(RESULT_CANCELED,i);
                                                AddEditSplrActivity.this.finish();
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
                        Toast.makeText(AddEditSplrActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();

                    }
                });

    }

    private void addSPlr() {


        ProgressDialog progressDialog = new ProgressDialog(AddEditSplrActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Produk");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);


        AndroidNetworking.post(URL_SPLR_ADD)
                .addBodyParameter("idCompSplr", idCompany)
                .addBodyParameter("nameSplr", inputNameSplr.getText().toString())
                .addBodyParameter("addrSplr", inputAlamatSplr.getText().toString())
                .addBodyParameter("phoneSplr", inputTlpSplr.getText().toString())
                .addBodyParameter("emailSplr", inputEmailSplr.getText().toString())
                .addBodyParameter("descSplr", inputDescSplr.getText().toString())
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
                                new AlertDialog.Builder(AddEditSplrActivity.this)
                                        .setMessage("Berhasil Mengudate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = getIntent();
                                                setResult(RESULT_OK, intent);
                                                AddEditSplrActivity.this.finish();
                                            }
                                        })
                                        .show();
                            }else{
                                new AlertDialog.Builder(AddEditSplrActivity.this)
                                        .setMessage("Gagal Mengupdate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Log.i("Input", "Data: "+idSplr+", "+nameSplr+", "+addrSplr+", "+phoneSplr+", "+emailSplr+", "+descSplr.toString());
                                                Intent i = getIntent();
                                                setResult(RESULT_CANCELED,i);
                                                AddEditSplrActivity.this.finish();
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
                        Toast.makeText(AddEditSplrActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();

                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        textPath = (TextView)findViewById(R.id.textViewPathImgSplrAddEdit);

        switch (requestCode) {
            case 7:
                if (resultCode==RESULT_OK) {
                    String PathHolder = data.getData().getPath();
                    textPath.setText(PathHolder);
                }
                break;
        }
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
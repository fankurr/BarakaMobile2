package com.baraka.barakamobile.ui.usermanaje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.util.DbConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.baraka.barakamobile.ui.usermanaje.UserManajeFragment.TAG_ACCESS;
import static com.baraka.barakamobile.ui.usermanaje.UserManajeFragment.TAG_ADDR;
import static com.baraka.barakamobile.ui.usermanaje.UserManajeFragment.TAG_EMAIL;
import static com.baraka.barakamobile.ui.usermanaje.UserManajeFragment.TAG_LEVEL;
import static com.baraka.barakamobile.ui.usermanaje.UserManajeFragment.TAG_NAME;
import static com.baraka.barakamobile.ui.usermanaje.UserManajeFragment.TAG_PHONE;

public class WorkerDetailActivity extends AppCompatActivity {

    public final static String TAG_ID = "id";
    public final static String TAG_EMAIL = "email";
    public final static String TAG_NAME = "name";
    public final static String TAG_ADDR = "address";
    public final static String TAG_LEVEL = "level";
    public final static String TAG_PHONE = "phone";
    public final static String TAG_ACCESS = "access";
    public final static String TAG_IDCOMP = "idCompany";
    public final static String TAG_COMP = "nameCompany";

    private String URL_WORKER_LOGIN = DbConfig.URL_WORKER + "editUserLogin.php";

    String id, email, name, level, access, idCompany, nameCompany;
    String nameUser, emailUser, addrUser, lvlUser, phoneUser, loginUser;

    String loginOn = "1";
    String loginOff = "0";
    String messagesWA = " ";
    String phonesWA = "";

    Switch swLoginWorker;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_worker);

        Intent intent = getIntent();
        id = intent.getStringExtra(TAG_ID);
        nameUser = intent.getStringExtra(TAG_NAME);
        emailUser = intent.getStringExtra(TAG_EMAIL);
        addrUser = intent.getStringExtra(TAG_ADDR);
        lvlUser = intent.getStringExtra(TAG_LEVEL);
        phoneUser = intent.getStringExtra(TAG_PHONE);
        loginUser = intent.getStringExtra(TAG_ACCESS);

        TextView textViewNameWorker = findViewById(R.id.textViewNameWorkerDetail);
        TextView textViewEmailWorker = findViewById(R.id.textViewEmailWorkerDetail);
        TextView textViewAddrWorker = findViewById(R.id.textViewAddrWorkerDetail);
        TextView textViewLvlWorker = findViewById(R.id.textViewLvlWorkerDetail);
        TextView textViewPhoneWorker = findViewById(R.id.textViewTlpWorkerDetail);
        swLoginWorker = findViewById(R.id.swLoginWorkerDetail);

        textViewNameWorker.setText(nameUser);
        textViewEmailWorker.setText(emailUser);
        textViewAddrWorker.setText(addrUser);
        textViewLvlWorker.setText(lvlUser);
        textViewPhoneWorker.setText(phoneUser);

        if (loginUser.equals("1")){
            swLoginWorker.setChecked(true);
            swLoginWorker.setText("ON");

            swLoginWorker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    postWorkerLoginOff();

                    swLoginWorker.setText("OFF");
                }
            });
        }else {
            swLoginWorker.setChecked(false);
            swLoginWorker.setText("OFF");

            swLoginWorker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    postWorkerLoginOn();

//                    swLoginWorker.setChecked(true);
//                    swLoginWorker.setText("ON");
                }
            });

        }

        // Image Telephone
        ImageView imgCallWorker = (ImageView) findViewById(R.id.imgTlpWorkerDetail);
        imgCallWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callWorker();
            }
        });

        // Image Whatsapp
        ImageView imgWaWorker = (ImageView) findViewById(R.id.imgWAWorkerDetail);
        imgWaWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waWorker();
            }
        });

        // Image Email
        ImageView emailWorkerDetail = (ImageView) findViewById(R.id.imgEmailWorkerDetail);
        emailWorkerDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(Intent.ACTION_SENDTO);
                in.setData(Uri.parse("mailto:"));
                in.putExtra(Intent.EXTRA_EMAIL,new String[] {emailUser});
                in.putExtra(Intent.EXTRA_SUBJECT,"");
                in.putExtra(Intent.EXTRA_TEXT,"");

                if(in.resolveActivity(getPackageManager())!=null){
                    startActivity(in);
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(nameUser);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_twotone_chevron_left_24);

    }

    // Eksternal Intent Phone
    public void callWorker(){
        String numb = phoneUser;
        Intent CallWorker = new Intent(Intent. ACTION_DIAL);
        CallWorker.setData(Uri. fromParts("tel", numb,null));
        startActivity(CallWorker);
    }

    // Eksternal Whatsapp
    public void waWorker(){
        if (isWhatappInstalled()){

            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="+phoneUser+
                    "&text="+messagesWA));
            startActivity(i);
        }else {
            Toast.makeText(WorkerDetailActivity.this,"Whatsapp Tidak Ditemukan! Harap Install Whatsapp Terlebih Dahulu.",Toast.LENGTH_SHORT).show();
        }
    }

    // Whatsapp Install Check
    public boolean isWhatappInstalled(){

        PackageManager packageManager = getPackageManager();
        boolean whatsappInstalled;

        try {
            packageManager.getPackageInfo("com.whatsapp",PackageManager.GET_ACTIVITIES);
            whatsappInstalled = true;
        }catch (PackageManager.NameNotFoundException e){
            whatsappInstalled = false;
        }
        return whatsappInstalled;
    }

    public void postWorkerLoginOn(){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data Karyawan");
        progressDialog.show();


//        sharedPreferences = this.getActivity().getSharedPreferences(my_shared_preferences,MODE_PRIVATE);

        AndroidNetworking.post(URL_WORKER_LOGIN)
                .addBodyParameter("idUser", String.valueOf(id))
                .addBodyParameter("loginUser", loginOn)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();

                        swLoginWorker.setChecked(true);
                        swLoginWorker.setText("ON");

                        Toast.makeText(WorkerDetailActivity.this, "Akses Login Di Izinkan!", Toast.LENGTH_SHORT).show();

                        Log.i("Info", "Login: ID -> "+id+" -> Name -> "+ nameUser + " -> " + loginUser);

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(WorkerDetailActivity.this, "Koneksi Gagal: " + anError.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
//                        Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        progressDialog.dismiss();
                    }
                });
    }

    public void postWorkerLoginOff(){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data Karyawan");
        progressDialog.show();

//        sharedPreferences = this.getActivity().getSharedPreferences(my_shared_preferences,MODE_PRIVATE);

        AndroidNetworking.post(URL_WORKER_LOGIN)
                .addBodyParameter("idUser", String.valueOf(id))
                .addBodyParameter("loginUser", loginOff)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();

                        Toast.makeText(WorkerDetailActivity.this, "Akses Login Di Tolak!", Toast.LENGTH_SHORT).show();

                        Log.i("Info", "Login: ID -> "+id+" -> Name -> "+ nameUser + " -> " + loginUser);

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(WorkerDetailActivity.this, "Koneksi Gagal: " + anError.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
//                        Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
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
package com.baraka.barakamobile.ui.usermanaje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.baraka.barakamobile.ui.profile.ProfileActivity;
import com.baraka.barakamobile.ui.util.DbConfig;
import com.squareup.picasso.Picasso;

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
    public final static String TAG_IMAGE = "imgWorker";
    public final static String TAG_IDCOMP = "idCompany";
    public final static String TAG_COMP = "nameCompany";

    private String urlUserDetail = DbConfig.URL + "idUserComp.php";
    private String URL_WORKER_LOGIN = DbConfig.URL_WORKER + "editUserLogin.php";
    private String URL_WORKER_IMG = DbConfig.URL + "imgUser/";

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    String id, email, name, level, access, idCompany, nameCompany;
    String nameUser, emailUser, addrUser, lvlUser, phoneUser, loginUser, imgUser;

    String loginOn = "1";
    String loginOff = "0";
    String messagesWA = " ";
    String phonesWA = "";

    TextView textViewNameWorker, textViewEmailWorker, textViewAddrWorker, textViewLvlWorker, textViewPhoneWorker;
    Switch swLoginWorker;
    ImageView imgWorkerDetail;


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
        imgUser = intent.getStringExtra(TAG_IMAGE);
        phoneUser = intent.getStringExtra(TAG_PHONE);
        loginUser = intent.getStringExtra(TAG_ACCESS);

        textViewNameWorker = findViewById(R.id.textViewNameWorkerDetail);
        textViewEmailWorker = findViewById(R.id.textViewEmailWorkerDetail);
        textViewAddrWorker = findViewById(R.id.textViewAddrWorkerDetail);
        textViewLvlWorker = findViewById(R.id.textViewLvlWorkerDetail);
        textViewPhoneWorker = findViewById(R.id.textViewTlpWorkerDetail);
        swLoginWorker = findViewById(R.id.swLoginWorkerDetail);
        imgWorkerDetail = findViewById(R.id.imgWorkerDetail);

//        textViewNameWorker.setText(nameUser);
//        textViewEmailWorker.setText(emailUser);
//        textViewAddrWorker.setText(addrUser);
//        textViewLvlWorker.setText(lvlUser);
//        textViewPhoneWorker.setText(phoneUser);
//        Picasso.get().load(URL_WORKER_IMG+workerViewModelList.get(position).getImgUser())
//                .resize(450, 450)
//                .centerCrop()
//                .placeholder(R.drawable.default_image_comp_small)
//                .error(R.drawable.default_image_comp_small)
//                .into(holder.imgWorker);
        detailWorker();

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

    // Detail Profile
    public void detailWorker(){
        progressDialog = new ProgressDialog(WorkerDetailActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Profil..");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        AndroidNetworking.post(urlUserDetail)
                .addBodyParameter("idUser", id.toString())
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
                                textViewNameWorker.setText(jsonObject.getString("name"));
                                textViewEmailWorker.setText(jsonObject.getString("email"));
                                textViewAddrWorker.setText(jsonObject.getString("address"));
                                textViewLvlWorker.setText(jsonObject.getString("postUser"));
                                textViewPhoneWorker.setText(jsonObject.getString("phone"));

                                Picasso.get().load(URL_WORKER_IMG+jsonObject.getString("imgProfile"))
                                        .resize(450, 450)
                                        .centerCrop()
                                        .placeholder(R.drawable.default_image_person_small)
                                        .error(R.drawable.default_image_person_small)
                                        .into(imgWorkerDetail);


                                getSupportActionBar().setTitle(jsonObject.getString("name"));

                                Log.i("ImgUser", "Image: "+URL_WORKER_IMG+jsonObject.getString("imgProfile"));

                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(WorkerDetailActivity.this, "Maaf, gagal Terhubung ke Database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(WorkerDetailActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
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
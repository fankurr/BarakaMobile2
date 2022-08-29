package com.baraka.barakamobile;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.ui.LoginActivity;
import com.baraka.barakamobile.ui.pos.POSActivity;
import com.baraka.barakamobile.ui.profile.ProfileActivity;
import com.baraka.barakamobile.ui.util.DbConfig;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.baraka.barakamobile.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    String id, email, name, address, level, postUser, phone, access, imageUser, idCompany, nameCompany, idLog;
    String idComp, nameComp, codeComp, addrComp, phoneComp, emailComp, logoComp;

    ImageView imgUser;

    MediaPlayer mediaPlayerLogout;

    private final static String TAG_ID = "id";
    private final static String TAG_EMAIL = "email";
    private final static String TAG_NAME = "name";
    private final static String TAG_ADDRESS = "address";
    private final static String TAG_LEVEL = "level";
    private final static String TAG_POST = "postUser";
    private final static String TAG_TLP = "phone";
    private final static String TAG_ACCESS = "access";
    private final static String TAG_IMG = "image";
    private final static String TAG_IDCOMP = "idCompany";
    private final static String TAG_COMP = "nameCompany";

    public static final String ID_COMP = "idComp";
    public static final String NAME_COMP = "nameComp";
    public static final String CODE_COMP = "codeComp";
    public static final String ADDR_COMP = "addrComp";
    public static final String PHONE_COMP = "phoneComp";
    public static final String EMAIL_COMP = "emailComp";
    public static final String LOGO_COMP = "logoComp";

    public static final String ID_LOG = "idLogin";

    private String urlAddLogout = DbConfig.URL_LOG + "addLogout.php";
    private String urlUserDetail = DbConfig.URL + "idUserComp.php";
    private String URL_USER_IMG_DETAIL = DbConfig.URL + "imgUser/";

    Calendar calender;
    SimpleDateFormat simpledateformat;
    String date;

    Locale locale;

    String idLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        locale = new Locale("id","ID");
        Locale.setDefault(locale);

        sharedPreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
//        sharedPreferences = this.getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        id = sharedPreferences.getString(TAG_ID, id);
        email = sharedPreferences.getString(TAG_EMAIL, email);
        name = sharedPreferences.getString(TAG_NAME, name);
        address = sharedPreferences.getString(TAG_ADDRESS, address);
        level = sharedPreferences.getString(TAG_LEVEL, level);
        postUser = sharedPreferences.getString(TAG_POST, postUser);
        phone = sharedPreferences.getString(TAG_TLP, phone);
        access = sharedPreferences.getString(TAG_ACCESS, access);
        imageUser = sharedPreferences.getString(TAG_IMG, imageUser);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, idCompany);
        nameCompany = sharedPreferences.getString(TAG_COMP, nameCompany);
        idLog = sharedPreferences.getString(ID_LOG, idLog);

        codeComp = sharedPreferences.getString(CODE_COMP, codeComp);
        addrComp = sharedPreferences.getString(ADDR_COMP, addrComp);
        phoneComp = sharedPreferences.getString(PHONE_COMP, phoneComp);
        emailComp = sharedPreferences.getString(EMAIL_COMP, emailComp);
        logoComp = sharedPreferences.getString(LOGO_COMP, logoComp);

        id = getIntent().getStringExtra(TAG_ID);
        email = getIntent().getStringExtra(TAG_EMAIL);
        name = getIntent().getStringExtra(TAG_NAME);
        level = getIntent().getStringExtra(TAG_LEVEL);
        postUser = getIntent().getStringExtra(TAG_POST);
        phone = getIntent().getStringExtra(TAG_TLP);
        access = getIntent().getStringExtra(TAG_ACCESS);
        imageUser = getIntent().getStringExtra(TAG_IMG);
        idCompany = getIntent().getStringExtra(TAG_IDCOMP);
        nameCompany = getIntent().getStringExtra(TAG_COMP);

        mediaPlayerLogout = MediaPlayer.create(this, R.raw.sound_logout);

        calender = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("EEE, dd-MM-yyyy HH:mm:ss");
        date = simpledateformat.format(calender.getTime());

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


        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, POSActivity.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_product, R.id.nav_sell, R.id.nav_purchase, R.id.nav_supplier, R.id.nav_usermanaje)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



        if (level.equals("2")){
            MenuItem navUser = navigationView.getMenu().findItem(R.id.nav_usermanaje);
            navUser.setVisible(false);
        }else {
            MenuItem navUser = navigationView.getMenu().findItem(R.id.nav_usermanaje);
            navUser.setVisible(true);
        }

        View headerView = navigationView.getHeaderView(0);
        imgUser = (ImageView) headerView.findViewById(R.id.imgUser);
        detailProfile();
        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentProfile = new Intent(MainActivity.this, ProfileActivity.class);
                intentProfile.putExtra(TAG_ID, id);
                intentProfile.putExtra(TAG_EMAIL, email);
                intentProfile.putExtra(TAG_NAME, name);
                intentProfile.putExtra(TAG_ADDRESS, address);
                intentProfile.putExtra(TAG_LEVEL, level);
                intentProfile.putExtra(TAG_POST, postUser);
                intentProfile.putExtra(TAG_TLP, phone);
                intentProfile.putExtra(TAG_ACCESS, access);
                intentProfile.putExtra(TAG_IMG, imageUser);
                intentProfile.putExtra(TAG_IDCOMP, idCompany);
                intentProfile.putExtra(TAG_COMP, nameCompany);

                intentProfile.putExtra(CODE_COMP, codeComp);
                intentProfile.putExtra(ADDR_COMP, addrComp);
                intentProfile.putExtra(PHONE_COMP, phoneComp);
                intentProfile.putExtra(EMAIL_COMP, emailComp);
                startActivity(intentProfile);

            }
        });

        TextView txtNameUser = (TextView) headerView.findViewById(R.id.txtNameUser);
        name = getIntent().getStringExtra(TAG_NAME);
        txtNameUser.setText(name);
        txtNameUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentProfile = new Intent(MainActivity.this, ProfileActivity.class);
                intentProfile.putExtra(TAG_ID, id);
                intentProfile.putExtra(TAG_EMAIL, email);
                intentProfile.putExtra(TAG_NAME, name);
                intentProfile.putExtra(TAG_ADDRESS, address);
                intentProfile.putExtra(TAG_LEVEL, level);
                intentProfile.putExtra(TAG_POST, postUser);
                intentProfile.putExtra(TAG_TLP, phone);
                intentProfile.putExtra(TAG_IMG, imageUser);
                intentProfile.putExtra(TAG_ACCESS, access);
                intentProfile.putExtra(TAG_IDCOMP, idCompany);
                intentProfile.putExtra(TAG_COMP, nameCompany);

                intentProfile.putExtra(CODE_COMP, codeComp);
                intentProfile.putExtra(ADDR_COMP, addrComp);
                intentProfile.putExtra(PHONE_COMP, phoneComp);
                intentProfile.putExtra(EMAIL_COMP, emailComp);
                startActivity(intentProfile);
            }
        });

        TextView txtNameComp = (TextView) headerView.findViewById(R.id.txtNameComp);
        nameCompany = getIntent().getStringExtra(TAG_COMP);
        txtNameComp.setText(nameCompany);


        MenuItem navLogout = navigationView.getMenu().findItem(R.id.nav_logout);
        navLogout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_logout);

                Button DialogBtnLogoutBatal = dialog.findViewById(R.id.btnDialogLogoutBatal);
                DialogBtnLogoutBatal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                Button DialogBtnLogoutYa = dialog.findViewById(R.id.btnDialogLogoutYa);
                DialogBtnLogoutYa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        calender = Calendar.getInstance();
                        simpledateformat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
                        date = simpledateformat.format(calender.getTime());

                        idLogout = id+"_"+idCompany+"_"+date;

                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putBoolean(LoginActivity.session_status, false);
                        editor.putString(TAG_ID, null);
                        editor.putString(TAG_EMAIL, null);
                        editor.commit();

                        addLogout(idLog, idLogout);

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        dialog.dismiss();
                        finish();
                        startActivity(intent);

                        Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                        mediaPlayerLogout.start();
                    }
                });
                dialog.show();

                return false;
            }
        });
    }

    // Detail Profile
    public void detailProfile(){

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

                                Picasso.get().load(URL_USER_IMG_DETAIL+jsonObject.getString("imgProfile"))
                                        .resize(50, 50)
                                        .centerCrop()
                                        .placeholder(R.drawable.default_image_person_small)
                                        .error(R.drawable.default_image_person_small)
                                        .into(imgUser);


                                Log.i("ImgUser", "Image: "+URL_USER_IMG_DETAIL+jsonObject.getString("imgProfile"));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Maaf, gagal Terhubung ke Database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(MainActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //add login
    public void addLogout(String idLogin, String datetimeLogout){

        calender = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("EEEE, dd-MM-yyyy HH:mm:ss");
        date = simpledateformat.format(calender.getTime());

        AndroidNetworking.post(urlAddLogout)
                .addBodyParameter("idLogin", idLogin)
                .addBodyParameter("datetimeLogout", date)
                .setTag("Update Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject data) {
                        Log.d("Respon Edit",""+data);

                        try {
                            Boolean status = data.getBoolean("success");
                            if (status == true){
                                Toast.makeText(MainActivity.this, "Logout Berhasil Tercatat!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(MainActivity.this, "Logout Gagal Tercatat!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(MainActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                    }
                });

    }
}
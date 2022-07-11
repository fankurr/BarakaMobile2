package com.baraka.barakamobile;

import android.app.Dialog;
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

import com.baraka.barakamobile.ui.LoginActivity;
import com.baraka.barakamobile.ui.pos.POSActivity;
import com.baraka.barakamobile.ui.profile.ProfileActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.baraka.barakamobile.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    String id, email, name, address, level, postUser, phone, access, idCompany, nameCompany;
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
    private final static String TAG_IDCOMP = "idCompany";
    private final static String TAG_COMP = "nameCompany";

    public static final String ID_COMP = "idComp";
    public static final String NAME_COMP = "nameComp";
    public static final String CODE_COMP = "codeComp";
    public static final String ADDR_COMP = "addrComp";
    public static final String PHONE_COMP = "phoneComp";
    public static final String EMAIL_COMP = "emailComp";
    public static final String LOGO_COMP = "logoComp";

//    TextView txtNameComp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        idCompany = sharedPreferences.getString(TAG_IDCOMP, idCompany);
        nameCompany = sharedPreferences.getString(TAG_COMP, nameCompany);

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
        idCompany = getIntent().getStringExtra(TAG_IDCOMP);
        nameCompany = getIntent().getStringExtra(TAG_COMP);

        mediaPlayerLogout = MediaPlayer.create(this, R.raw.sound_logout);

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

                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putBoolean(LoginActivity.session_status, false);
                        editor.putString(TAG_ID, null);
                        editor.putString(TAG_EMAIL, null);
                        editor.commit();

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
}
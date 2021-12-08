package com.baraka.barakamobile.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.baraka.barakamobile.MainActivity;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.LoginActivity;

public class ProfileActivity extends AppCompatActivity {

    String id, email, name, address, level, postUser, phone, access, idCompany, nameCompany;

    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

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

    TextView txtnameUser, txtPostUser, txtAddrUser, txtPhoneUser, txtCompUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = this.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

//        sharedPreferences = getSharedPreferences(MainActivity.my_shared_preferences, Context.MODE_PRIVATE);
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

        txtnameUser = findViewById(R.id.textViewNameUserDetail);
        txtPostUser = findViewById(R.id.textViewLvlUserDetail);
        txtAddrUser = findViewById(R.id.textViewAddrUserDetail);
        txtPhoneUser = findViewById(R.id.textViewTlpUserDetail);
        txtCompUser = findViewById(R.id.textViewNameCompUserDetail);

        txtnameUser.setText(name);
        txtPostUser.setText(postUser);
        txtAddrUser.setText(address);
        txtPhoneUser.setText(phone);

        txtCompUser.setText(nameCompany);

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_twotone_chevron_left_24);
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
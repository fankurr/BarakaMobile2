package com.baraka.barakamobile.ui.profile;

import static com.baraka.barakamobile.ui.product.ProductFragment.ADDR_SPLR;
import static com.baraka.barakamobile.ui.product.ProductFragment.DESC_SPLR;
import static com.baraka.barakamobile.ui.product.ProductFragment.EMAIL_SPLR;
import static com.baraka.barakamobile.ui.product.ProductFragment.ID_PRDCT;
import static com.baraka.barakamobile.ui.product.ProductFragment.ID_SPLR;
import static com.baraka.barakamobile.ui.product.ProductFragment.NAME_SPLR;
import static com.baraka.barakamobile.ui.product.ProductFragment.PHONE_SPLR;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baraka.barakamobile.ui.profile.CompProActivity;
import com.baraka.barakamobile.MainActivity;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.LoginActivity;
import com.google.android.material.card.MaterialCardView;

public class ProfileActivity extends AppCompatActivity {

    String id, email, name, address, level, postUser, phone, access, idCompany, nameCompany;
    String idComp, nameComp, codeComp, addrComp, phoneComp, emailComp, logoComp;

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

    public static final String ID_COMP = "idComp";
    public static final String NAME_COMP = "nameComp";
    public static final String CODE_COMP = "codeComp";
    public static final String ADDR_COMP = "addrComp";
    public static final String PHONE_COMP = "phoneComp";
    public static final String EMAIL_COMP = "emailComp";
    public static final String LOGO_COMP = "logoComp";

    TextView txtnameUser, txtPostUser, txtAddrUser, txtPhoneUser, txtEmailUser, txtCompUser;

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

        codeComp = sharedPreferences.getString(CODE_COMP, codeComp);
        addrComp = sharedPreferences.getString(ADDR_COMP, addrComp);
        phoneComp = sharedPreferences.getString(PHONE_COMP, phoneComp);
        emailComp = sharedPreferences.getString(EMAIL_COMP, emailComp);
        logoComp = sharedPreferences.getString(LOGO_COMP, logoComp);

        txtnameUser = findViewById(R.id.textViewNameUserDetail);
        txtPostUser = findViewById(R.id.textViewLvlUserDetail);
        txtAddrUser = findViewById(R.id.textViewAddrUserDetail);
        txtEmailUser = findViewById(R.id.textViewEmailUserDetail);
        txtPhoneUser = findViewById(R.id.textViewTlpUserDetail);
        txtCompUser = findViewById(R.id.textViewNameCompUserDetail);

        txtnameUser.setText(name);
        txtPostUser.setText(postUser);
        txtAddrUser.setText(address);
        txtEmailUser.setText(email);
        txtPhoneUser.setText(phone);

        txtCompUser.setText(nameCompany);

        CardView cardCompPro = findViewById(R.id.cardCompUserDetail);
        cardCompPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCompPro = new Intent(ProfileActivity.this, CompProActivity.class);
                intentCompPro.putExtra(TAG_LEVEL, level);
                intentCompPro.putExtra(TAG_IDCOMP, idCompany);
                intentCompPro.putExtra(TAG_COMP, nameCompany);
                intentCompPro.putExtra(CODE_COMP, codeComp);
                intentCompPro.putExtra(ADDR_COMP, addrComp);
                intentCompPro.putExtra(PHONE_COMP, phoneComp);
                intentCompPro.putExtra(EMAIL_COMP, emailComp);
                startActivity(intentCompPro);
            }
        });

        Button btnEditProfile = findViewById(R.id.btnEditUserDetail);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentEditProfile = new Intent(ProfileActivity.this, EditProfileActivity.class);

                intentEditProfile.putExtra(TAG_ID, id);
                intentEditProfile.putExtra(TAG_NAME, name);
                intentEditProfile.putExtra(TAG_POST, postUser);
                intentEditProfile.putExtra(TAG_ADDRESS, address);
                intentEditProfile.putExtra(TAG_TLP, phone);
                intentEditProfile.putExtra(TAG_EMAIL, email);
                intentEditProfile.putExtra(TAG_COMP, nameCompany);
                startActivity(intentEditProfile);
            }
        });

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
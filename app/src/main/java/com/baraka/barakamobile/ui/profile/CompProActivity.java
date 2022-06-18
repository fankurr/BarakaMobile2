package com.baraka.barakamobile.ui.profile;

import static com.baraka.barakamobile.ui.supplier.SupplierFragment.ID_SPLR;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.baraka.barakamobile.R;

public class CompProActivity extends AppCompatActivity {
    String idComp, nameComp, codeComp, addrComp, phoneComp, emailComp, logoComp;
    String id, email, name, address, level, postUser, phone, access, idCompany, nameCompany;

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

    TextView txtNameComp, txtAddrComp, txtTlpComp, txtEmailCOmp, txtCodeComp;

    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_pro);

        sharedPreferences = this.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

//        idComp = sharedPreferences.getString(ID_COMP, idComp);
//        nameComp = sharedPreferences.getString(NAME_COMP, nameComp);
//        codeComp = sharedPreferences.getString(CODE_COMP, codeComp);
//        addrComp = sharedPreferences.getString(ADDR_COMP, addrComp);
//        phoneComp = sharedPreferences.getString(PHONE_COMP, phoneComp);
//        emailComp = sharedPreferences.getString(EMAIL_COMP, emailComp);
//        logoComp = sharedPreferences.getString(LOGO_COMP, logoComp);

        Intent intent = getIntent();
        level = intent.getStringExtra(TAG_LEVEL);
        idCompany = intent.getStringExtra(TAG_IDCOMP);
        nameCompany = intent.getStringExtra(TAG_COMP);
        codeComp = intent.getStringExtra(CODE_COMP);
        addrComp = intent.getStringExtra(ADDR_COMP);
        phoneComp = intent.getStringExtra(PHONE_COMP);
        emailComp = intent.getStringExtra(EMAIL_COMP);

        txtNameComp = findViewById(R.id.txtViewCompProDetail);
        txtAddrComp = findViewById(R.id.txtViewAddrCompProDetail);
        txtTlpComp = findViewById(R.id.txtViewTlpCompProDetail);
        txtEmailCOmp = findViewById(R.id.txtViewEmailCompProDetail);
        txtCodeComp = findViewById(R.id.txtViewCodeCompProDetail);

        txtNameComp.setText(nameCompany);
        txtAddrComp.setText(addrComp);
        txtTlpComp.setText(phoneComp);
        txtEmailCOmp.setText(emailComp);
        txtCodeComp.setText(codeComp);

        if (level.equals("2")){
            TextView labelCompPro = findViewById(R.id.txtViewLblCodeCompPro);
//            labelCompPro.setVisibility(View.INVISIBLE);
            TextView txtCodeCompPro = findViewById(R.id.txtViewCodeCompProDetail);
            txtCodeCompPro.setText("Untuk mengetahui kode toko, silahkan hubungi Owner/Pemilik toko.");
        }
//        else {
//            TextView labelCompPro = findViewById(R.id.txtViewLblCodeCompPro);
//            labelCompPro.setVisibility(View.VISIBLE);
//            TextView txtCodeCompPro = findViewById(R.id.txtViewCodeCompProDetail);
//            txtCodeCompPro.setVisibility(View.VISIBLE);
//        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(nameCompany);
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
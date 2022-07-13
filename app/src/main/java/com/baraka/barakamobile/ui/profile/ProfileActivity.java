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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.ui.profile.CompProActivity;
import com.baraka.barakamobile.MainActivity;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.LoginActivity;
import com.baraka.barakamobile.ui.util.DbConfig;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    String id, email, name, address, level, postUser, phone, access, imgProfile, idCompany, nameCompany;
    String idComp, nameComp, codeComp, addrComp, phoneComp, emailComp, logoComp;

    private SwipeRefreshLayout swipeRefreshLayout;

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    private String urlUserDetail = DbConfig.URL + "idUserComp.php";
    private String URL_USER_IMG_DETAIL = DbConfig.URL + "imgUser/";

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
    ImageView imgPhotoUserDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = this.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

        id = sharedPreferences.getString(TAG_ID, id);
        level = sharedPreferences.getString(TAG_LEVEL, level);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, idCompany);
        codeComp = sharedPreferences.getString(CODE_COMP, codeComp);
//        email = sharedPreferences.getString(TAG_EMAIL, email);
//        name = sharedPreferences.getString(TAG_NAME, name);
//        address = sharedPreferences.getString(TAG_ADDRESS, address);
//        postUser = sharedPreferences.getString(TAG_POST, postUser);
//        phone = sharedPreferences.getString(TAG_TLP, phone);
//        access = sharedPreferences.getString(TAG_ACCESS, access);
//        idCompany = sharedPreferences.getString(TAG_IDCOMP, idCompany);
//        nameCompany = sharedPreferences.getString(TAG_COMP, nameCompany);
//

//        addrComp = sharedPreferences.getString(ADDR_COMP, addrComp);
//        phoneComp = sharedPreferences.getString(PHONE_COMP, phoneComp);
//        emailComp = sharedPreferences.getString(EMAIL_COMP, emailComp);
//        logoComp = sharedPreferences.getString(LOGO_COMP, logoComp);

        txtnameUser = findViewById(R.id.textViewNameUserDetail);
        txtPostUser = findViewById(R.id.textViewLvlUserDetail);
        txtAddrUser = findViewById(R.id.textViewAddrUserDetail);
        txtEmailUser = findViewById(R.id.textViewEmailUserDetail);
        txtPhoneUser = findViewById(R.id.textViewTlpUserDetail);
        txtCompUser = findViewById(R.id.textViewNameCompUserDetail);
        imgPhotoUserDetail = findViewById(R.id.imgUserDetail);

        detailProfile();

        swipeRefreshLayout = findViewById(R.id.SwipeRefreshProfile);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItem();
            }

            private void refreshItem() {
                detailProfile();
                onItemLoad();
            }

            private void onItemLoad() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });


//        txtnameUser.setText(name);
//        txtPostUser.setText(postUser);
//        txtAddrUser.setText(address);
//        txtEmailUser.setText(email);
//        txtPhoneUser.setText(phone);
//
//        txtCompUser.setText(nameCompany);

        CardView cardCompPro = findViewById(R.id.cardCompUserDetail);
        cardCompPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCompPro = new Intent(ProfileActivity.this, CompProActivity.class);
                intentCompPro.putExtra(TAG_IDCOMP, idCompany);
                intentCompPro.putExtra(CODE_COMP, codeComp);
                intentCompPro.putExtra(TAG_LEVEL, level);
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

                finish();
                startActivity(intentEditProfile);
            }
        });

//        Log.e("Profile: ", "ID User: " + id +
//                ", Name: " + name +
//                ", Address: " +address+
//                ", Email: " + email +
//                ", Level: "+level+
//                ", Position: " +postUser+
//                ", Phone: " +phone+
//                ", Access: " +access+
//                ", ID Comp: " +idCompany+
//                ", Company: "+nameCompany.toString());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_twotone_chevron_left_24);
    }

    // Detail Profile
    public void detailProfile(){
        progressDialog = new ProgressDialog(ProfileActivity.this);
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
                                txtnameUser.setText(jsonObject.getString("name"));
                                txtPostUser.setText(jsonObject.getString("postUser"));
                                txtAddrUser.setText(jsonObject.getString("address"));
                                txtEmailUser.setText(jsonObject.getString("email"));
                                txtPhoneUser.setText(jsonObject.getString("phone"));

                                Picasso.get().load(URL_USER_IMG_DETAIL+jsonObject.getString("imgProfile"))
                                        .resize(450, 450)
                                        .centerCrop()
                                        .placeholder(R.drawable.default_image_person_small)
                                        .error(R.drawable.default_image_person_small)
                                        .into(imgPhotoUserDetail);

                                txtCompUser.setText(jsonObject.getString("nameCompany"));
                                getSupportActionBar().setTitle(jsonObject.getString("name"));

                                Log.i("ImgUser", "Image: "+URL_USER_IMG_DETAIL+jsonObject.getString("imgProfile"));

                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProfileActivity.this, "Maaf, gagal Terhubung ke Database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ProfileActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
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
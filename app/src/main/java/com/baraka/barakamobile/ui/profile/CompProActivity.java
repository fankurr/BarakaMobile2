package com.baraka.barakamobile.ui.profile;

import static com.baraka.barakamobile.ui.supplier.SupplierFragment.ID_SPLR;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.util.DbConfig;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CompProActivity extends AppCompatActivity {
    String idComp, nameComp, codeComp, cityComp, addrComp, phoneComp, emailComp, logoComp;
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
    public static final String CITY_COMP = "cityComp";
    public static final String ADDR_COMP = "addrComp";
    public static final String PHONE_COMP = "phoneComp";
    public static final String EMAIL_COMP = "emailComp";
    public static final String LOGO_COMP = "logoComp";

    TextView txtNameComp, txtCityComp, txtAddrComp, txtTlpComp, txtEmailCOmp, txtCodeComp;
    Button btnEditCompPro;
    ImageView imgComp;

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    private SwipeRefreshLayout swipeRefreshLayout;

    private String urlCompProDetail = DbConfig.URL_COMP + "idComp.php";
    private String URL_COMP_IMG_DETAIL = DbConfig.URL_COMP + "imgComp/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_pro);

        sharedPreferences = this.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

        id = sharedPreferences.getString(TAG_ID, id);
        level = sharedPreferences.getString(TAG_LEVEL, level);
        idComp = sharedPreferences.getString(ID_COMP, idComp);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, idCompany);
        codeComp = sharedPreferences.getString(CODE_COMP, codeComp);
        cityComp = sharedPreferences.getString(CITY_COMP, cityComp);
        addrComp = sharedPreferences.getString(ADDR_COMP, addrComp);

        Intent intent = getIntent();
        id = intent.getStringExtra(TAG_ID);
        level = intent.getStringExtra(TAG_LEVEL);
        idCompany = intent.getStringExtra(TAG_IDCOMP);
        nameCompany = intent.getStringExtra(TAG_COMP);
        codeComp = intent.getStringExtra(CODE_COMP);
        cityComp = intent.getStringExtra(CITY_COMP);
        addrComp = intent.getStringExtra(ADDR_COMP);
        phoneComp = intent.getStringExtra(PHONE_COMP);
        emailComp = intent.getStringExtra(EMAIL_COMP);

        txtNameComp = findViewById(R.id.txtViewCompProDetail);
        txtCityComp = findViewById(R.id.txtViewCityCompProDetail);
        txtAddrComp = findViewById(R.id.txtViewAddrCompProDetail);
        txtTlpComp = findViewById(R.id.txtViewTlpCompProDetail);
        txtEmailCOmp = findViewById(R.id.txtViewEmailCompProDetail);
        txtCodeComp = findViewById(R.id.txtViewCodeCompProDetail);
        imgComp = findViewById(R.id.imgCompPro);

//        txtNameComp.setText(nameCompany);
//        txtAddrComp.setText(addrComp);
//        txtTlpComp.setText(phoneComp);
//        txtEmailCOmp.setText(emailComp);
//        txtCodeComp.setText(codeComp);

        detailComp();

        swipeRefreshLayout = findViewById(R.id.SwipeRefreshCompPro);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItem();
            }

            private void refreshItem() {
                detailComp();
                onItemLoad();
            }

            private void onItemLoad() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        btnEditCompPro = findViewById(R.id.btnEditCompProDetail);
        btnEditCompPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intenEditCompPro = new Intent(CompProActivity.this, EditCompProActivity.class);

                intenEditCompPro.putExtra(TAG_ID, id);
                intenEditCompPro.putExtra(TAG_LEVEL, level);
                intenEditCompPro.putExtra(TAG_IDCOMP, idCompany);
                intenEditCompPro.putExtra(CODE_COMP, codeComp);

                finish();
                startActivity(intenEditCompPro);

            }
        });

//        if (level.equals("2")){
//            TextView labelCompPro = findViewById(R.id.txtViewLblCodeCompPro);
////            labelCompPro.setVisibility(View.INVISIBLE);
//            TextView txtCodeComp = findViewById(R.id.txtViewCodeCompProDetail);
//            txtCodeComp.setText("Untuk mengetahui kode toko, silahkan hubungi Owner/Pemilik toko.");
//        }
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

    // Detail CompPro
    public void detailComp(){
        progressDialog = new ProgressDialog(CompProActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Toko..");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);
//        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        AndroidNetworking.post(urlCompProDetail)
                .addBodyParameter("idComp", idCompany.toString())
                .addBodyParameter("codeComp", codeComp.toString())
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
                                txtNameComp.setText(jsonObject.getString("nameComp"));
                                txtCityComp.setText(jsonObject.getString("cityComp"));
                                txtAddrComp.setText(jsonObject.getString("addrComp"));
                                txtTlpComp.setText(jsonObject.getString("phoneComp"));
                                txtEmailCOmp.setText(jsonObject.getString("emailComp"));

                                Picasso.get().load(URL_COMP_IMG_DETAIL+jsonObject.getString("logoComp"))
                                        .resize(450, 450)
                                        .centerCrop()
                                        .placeholder(R.drawable.default_image_comp_small)
                                        .error(R.drawable.default_image_comp_small)
                                        .into(imgComp);


                                if (level.equals("2")){
                                    txtCodeComp = findViewById(R.id.txtViewCodeCompProDetail);
                                    txtCodeComp.setText("Untuk mengetahui kode toko, silahkan hubungi Owner/Pemilik toko.");
                                    btnEditCompPro = findViewById(R.id.btnEditCompProDetail);
                                    btnEditCompPro.setEnabled(false);
                                    btnEditCompPro.setBackgroundColor(Color.LTGRAY);
                                    btnEditCompPro.setTextColor(Color.GRAY);
                                }else{
                                    txtCodeComp.setText(jsonObject.getString("codeComp"));
                                }


                                getSupportActionBar().setTitle(jsonObject.getString("nameComp"));

                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CompProActivity.this, "Maaf, gagal Terhubung ke Database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(CompProActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
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
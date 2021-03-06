package com.baraka.barakamobile.ui.supplier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.MainActivity;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.product.PrdctDetail;
import com.baraka.barakamobile.ui.usermanaje.WorkerDetailActivity;
import com.baraka.barakamobile.ui.util.DbConfig;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.baraka.barakamobile.ui.supplier.SupplierFragment.ADDR_SPLR;
import static com.baraka.barakamobile.ui.supplier.SupplierFragment.DESC_SPLR;
import static com.baraka.barakamobile.ui.supplier.SupplierFragment.EMAIL_SPLR;
import static com.baraka.barakamobile.ui.supplier.SupplierFragment.ID_SPLR;
import static com.baraka.barakamobile.ui.supplier.SupplierFragment.NAME_SPLR;
import static com.baraka.barakamobile.ui.supplier.SupplierFragment.PHONE_SPLR;
import static com.baraka.barakamobile.ui.supplier.SupplierFragment.IMG_SPLR;


public class SupplierDetailActivity extends AppCompatActivity {

//    public static final String ID_SPLR = "idSplr";
//    public static final String COMP_SPLR= "idCompSplr";
//    public static final String NAME_SPLR= "nameSplr";
//    public static final String ADDR_SPLR = "addrSplr";
//    public static final String PHONE_SPLR = "phoneSplr";
//    public static final String EMAIL_SPLR = "emailSplr";
//    public static final String DESC_SPLR = "descSplr";
//    public static final String IMG_SPLR = "imgSplr";


    private final static String TAG_ID = "id";
    private final static String TAG_EMAIL = "email";
    private final static String TAG_NAME = "name";
    private final static String TAG_LEVEL = "level";
    private final static String TAG_ACCESS = "access";
    private final static String TAG_IDCOMP = "idCompany";
    private final static String TAG_COMP = "nameCompany";

    private String URL_SPLR_DETAIL = DbConfig.URL_SPLR + "idSplr.php";
    private String URL_SPLR_DEL = DbConfig.URL_SPLR + "delSplr.php";
    private String URL_SPLR_IMG_DETAIL = DbConfig.URL_SPLR + "imgSplr/";

    String id, email, name, level, access, idCompany, nameCompany;
    String idSplr, nameSplr,descSplr,addrSplr, phoneSplr, emailSplr, imgSplr;
    String messagesWaSplr = " ";
    String phonesWaSplr = "";

    TextView textViewNameSplr, textViewDescSplr, textViewAddrSplr, textViewTlpSplr, textViewEmailSplr;
    ImageView imgPhotoSplrDetail;

    ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_detail);

        Intent intentSplr = getIntent();
        idSplr = intentSplr.getStringExtra(ID_SPLR);
        nameSplr = intentSplr.getStringExtra(NAME_SPLR);
        descSplr = intentSplr.getStringExtra(DESC_SPLR);
        addrSplr = intentSplr.getStringExtra(ADDR_SPLR);
        phoneSplr = intentSplr.getStringExtra(PHONE_SPLR);
        emailSplr = intentSplr.getStringExtra(EMAIL_SPLR);
        imgSplr = intentSplr.getStringExtra(IMG_SPLR);

        textViewNameSplr = findViewById(R.id.textViewNameSplrDetail);
        textViewDescSplr = findViewById(R.id.textViewDescSplrDetail);
        textViewAddrSplr = findViewById(R.id.textViewAddrSplrDetail);
        textViewTlpSplr = findViewById(R.id.textViewTlpSplrDetail);
        textViewEmailSplr = findViewById(R.id.textViewEmailSplrDetail);
        imgPhotoSplrDetail = findViewById(R.id.imgPhotoSplrDetail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(nameSplr);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_twotone_chevron_left_24);

        textViewNameSplr.setText(nameSplr);
        textViewDescSplr.setText(descSplr);
        textViewAddrSplr.setText(addrSplr);
        textViewTlpSplr.setText(phoneSplr);
        textViewEmailSplr.setText(emailSplr);

        Log.e("ImgSplr", "Image: "+URL_SPLR_IMG_DETAIL+imgSplr);

        splrDetail();

        swipeRefreshLayout = findViewById(R.id.SwipeRefreshSplrDetail);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItem();
            }

            private void refreshItem() {
                onItemLoad();
                textViewNameSplr.setText(nameSplr);
                textViewDescSplr.setText(descSplr);
                textViewAddrSplr.setText(addrSplr);
                textViewTlpSplr.setText(phoneSplr);
                textViewEmailSplr.setText(emailSplr);
                Picasso.get().load(URL_SPLR_IMG_DETAIL+imgSplr)
                        .fit()
                        .centerInside()
                        .placeholder(R.drawable.default_image_comp_small)
                        .error(R.drawable.default_image_comp_small)
                        .into(imgPhotoSplrDetail);
                splrDetail();
            }

            private void onItemLoad() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        Button btnAddEdit = findViewById(R.id.btnEditSplrDetail);
        btnAddEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSplr = new Intent(SupplierDetailActivity.this, AddEditSplrActivity.class);

                intentSplr.putExtra(ID_SPLR, idSplr);
                intentSplr.putExtra(NAME_SPLR, nameSplr);
                intentSplr.putExtra(DESC_SPLR, descSplr);
                intentSplr.putExtra(ADDR_SPLR, addrSplr);
                intentSplr.putExtra(PHONE_SPLR, phoneSplr);
                intentSplr.putExtra(EMAIL_SPLR, emailSplr);
                intentSplr.putExtra(IMG_SPLR, imgSplr);

                finish();
                startActivity(intentSplr);
            }
        });

        Button btnDelSplr = findViewById(R.id.btnDelSplrDetail);
        btnDelSplr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogDel = new Dialog(SupplierDetailActivity.this);

                dialogDel.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogDel.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogDel.setContentView(R.layout.dialog_delete);

                TextView textWarnDel = dialogDel.findViewById(R.id.textDelWarning);
                textWarnDel.setText("Apa Anda yakin ingin menghapus [" +nameSplr+ "] dari data Anda?");

                Button btnDelNo = dialogDel.findViewById(R.id.btnDelNo);
                btnDelNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogDel.dismiss();
                    }
                });
                Button btnDelYes = dialogDel.findViewById(R.id.btnDelYes);
                btnDelYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delSplr();
                        dialogDel.dismiss();
                    }
                });
                dialogDel.show();
            }
        });

        // Image Telephone
        ImageView imgCallSplr = (ImageView) findViewById(R.id.imgTlpSplrDetail);
        imgCallSplr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSplr();
            }
        });

        // Image Whatsapp
        ImageView imgWaSplr = (ImageView) findViewById(R.id.imgWaSplrDetail);
        imgWaSplr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waSupplier();
            }
        });

        // Image Email
        ImageView emailSplrDetail = (ImageView) findViewById(R.id.imgEmailSplrDetail);
        emailSplrDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(Intent.ACTION_SENDTO);
                in.setData(Uri.parse("mailto:"));
                in.putExtra(Intent.EXTRA_EMAIL,new String[] {emailSplr});
                in.putExtra(Intent.EXTRA_SUBJECT,"");
                in.putExtra(Intent.EXTRA_TEXT,"");

                if(in.resolveActivity(getPackageManager())!=null){
                    startActivity(in);
                }
            }
        });


    }

    // Detail Profile
    public void splrDetail(){
        progressDialog = new ProgressDialog(SupplierDetailActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Profil");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);

        AndroidNetworking.post(URL_SPLR_DETAIL)
                .addBodyParameter("idSplr", idSplr.toString())
                .setTag("Load Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            int success = response.getInt("success");
                            if (success==1){
                                JSONArray jsonArray = response.getJSONArray("data"); // mengambil [data] dari json

                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                textViewNameSplr.setText(jsonObject.getString("nameSupplier"));
                                textViewDescSplr.setText(jsonObject.getString("descSupplier"));
                                textViewAddrSplr.setText(jsonObject.getString("addrSupplier"));
                                textViewTlpSplr.setText(jsonObject.getString("phoneSupplier"));
                                textViewEmailSplr.setText(jsonObject.getString("emailSupplier"));


                                Picasso.get().load(URL_SPLR_IMG_DETAIL+jsonObject.getString("imageSupplier"))
                                        .resize(450, 450)
                                        .centerCrop()
                                        .placeholder(R.drawable.default_image_comp_small)
                                        .error(R.drawable.default_image_comp_small)
                                        .into(imgPhotoSplrDetail);

                                getSupportActionBar().setTitle(jsonObject.getString("nameSupplier"));

                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SupplierDetailActivity.this, "Maaf, gagal Terhubung ke Database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(SupplierDetailActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();

                    }
                });
    }

    private void delSplr() {
        ProgressDialog progressDialog = new ProgressDialog(SupplierDetailActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Produk");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);


        AndroidNetworking.post(URL_SPLR_DEL)
                .addBodyParameter("idSplr", idSplr)
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
                                new AlertDialog.Builder(SupplierDetailActivity.this)
                                        .setMessage("Berhasil Menghapus Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = getIntent();
                                                setResult(RESULT_OK, intent);
                                                SupplierDetailActivity.this.finish();
                                                onBackPressed();
                                            }
                                        })
                                        .show();
                            }else{
                                new AlertDialog.Builder(SupplierDetailActivity.this)
                                        .setMessage("Gagal Mengupdate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = getIntent();
                                                setResult(RESULT_CANCELED,i);
                                                SupplierDetailActivity.this.finish();
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
                        Toast.makeText(SupplierDetailActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();

                    }
                });
    }

    // Eksternal Intent Phone
    public void callSplr(){
        String numbSplr = phoneSplr;
        Intent CallSplr = new Intent(Intent. ACTION_DIAL);
        CallSplr.setData(Uri. fromParts("tel", numbSplr,null));
        startActivity(CallSplr);
    }

    // Eksternal Whatsapp
    public void waSupplier(){
        if (isWhatappInstalled()){
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="+phoneSplr+
                    "&text="+messagesWaSplr));
            startActivity(i);
        }else {
            Toast.makeText(SupplierDetailActivity.this,"Whatsapp Tidak Ditemukan! Harap Install Whatsapp Terlebih Dahulu.",Toast.LENGTH_SHORT).show();
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
package com.baraka.barakamobile.ui.product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.profile.ProfileActivity;
import com.baraka.barakamobile.ui.supplier.SupplierDetailActivity;
import com.baraka.barakamobile.ui.util.DbConfig;
import com.squareup.picasso.Picasso;

import static com.baraka.barakamobile.ui.product.ProductFragment.ADDR_SPLR;
import static com.baraka.barakamobile.ui.product.ProductFragment.CATE_PRDCT;
import static com.baraka.barakamobile.ui.product.ProductFragment.CODE_PRDCT;
import static com.baraka.barakamobile.ui.product.ProductFragment.DESC_PRDCT;
import static com.baraka.barakamobile.ui.product.ProductFragment.DESC_SPLR;
import static com.baraka.barakamobile.ui.product.ProductFragment.EMAIL_SPLR;
import static com.baraka.barakamobile.ui.product.ProductFragment.ID_CATE;
import static com.baraka.barakamobile.ui.product.ProductFragment.ID_PRDCT;
import static com.baraka.barakamobile.ui.product.ProductFragment.ID_SPLR;
import static com.baraka.barakamobile.ui.product.ProductFragment.NAME_PRDCT;
import static com.baraka.barakamobile.ui.product.ProductFragment.NAME_SPLR;
import static com.baraka.barakamobile.ui.product.ProductFragment.PHONE_SPLR;
import static com.baraka.barakamobile.ui.product.ProductFragment.PRICE_PRDCT;
import static com.baraka.barakamobile.ui.product.ProductFragment.SPLR_PRDCT;
import static com.baraka.barakamobile.ui.product.ProductFragment.STOCK_PRDCT;
import static com.baraka.barakamobile.ui.product.ProductFragment.UNIT_PRDCT;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

public class PrdctDetail extends AppCompatActivity {

    private final static String TAG_ID = "id";
    private final static String TAG_EMAIL = "email";
    private final static String TAG_NAME = "name";
    private final static String TAG_LEVEL = "level";
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

    public static final String ID_PRDCT = "idPrdct";
    public static final String NAME_PRDCT = "namePrdct";
    public static final String CODE_PRDCT = "codePrdct";
    public static final String ID_CATE = "idCate";
    public static final String CATE_PRDCT = "nameCat";
    public static final String SPLR_PRDCT = "nameSplr";
    public static final String DESC_PRDCT = "descPrdct";
    public static final String PRICE_PRDCT = "unitPrice";
    public static final String UNIT_PRDCT = "unitPrdct";
    public static final String STOCK_PRDCT = "stockPrdct";
    public static final String IMG_PRDCT = "imgPrdct";

    public static final String ID_SPLR = "idSplr";
    public static final String COMP_SPLR= "idCompSplr";
    public static final String NAME_SPLR= "nameSplr";
    public static final String ADDR_SPLR = "addrSplr";
    public static final String PHONE_SPLR = "phoneSplr";
    public static final String EMAIL_SPLR = "emailSplr";
    public static final String DESC_SPLR = "descSplr";
    public static final String IMG_SPLR = "imgSplr";

    private String urlPrdctDetail = DbConfig.URL_PRDCT + "idPrdct.php";
    private String urlPrdctDel = DbConfig.URL_PRDCT + "delPrdct.php";
    private String URL_PRDCT_IMG = DbConfig.URL_PRDCT + "imgPrdct/";
    private String URL_SPLR_IMG = DbConfig.URL_SPLR + "imgSplr/";

    String messagesWaSplr = " ";
    String id, email, name, level, access, idCompany, nameCompany;
    String idComp, nameComp, codeComp, addrComp, phoneComp, emailComp, logoComp;
    String idPrdct, idCat, namePrdct, codePrdct, descPrdct, pricePrdct, unitPricePrdct, unitStockPrdct, stockPrdct, imgPrdct, catPrdct, nameSplrPrdct;
    String idSplr, nameSplr, descSplr, addrSplr, phoneSplr, emailSplr;

    TextView textViewIdPrdct, textViewNamePrdct, textViewCodePrdct, textViewDescPrdct, textViewPricePrdct, textViewUnitPricePrdct, textViewUnitStockPrdct, textViewStockPrdct, textViewCatPrdct, textViewNameSplr;
    ImageView imgPrdctDetail, imgSplrPrdctDetail;
    Button btnEdit, btnDell;

    NumberFormat rupiah;

    private SwipeRefreshLayout swipeRefreshLayout;

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prdct_detail);

        ProgressDialog progressDialog = new ProgressDialog(PrdctDetail.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data..");
        progressDialog.show();

        sharedPreferences = PrdctDetail.this.getSharedPreferences(my_shared_preferences,Context.MODE_PRIVATE);
        id = sharedPreferences.getString(TAG_ID, null);
        email = sharedPreferences.getString(TAG_EMAIL, null);
        name = sharedPreferences.getString(TAG_NAME, null);
        level = sharedPreferences.getString(TAG_LEVEL, null);
        access = sharedPreferences.getString(TAG_ACCESS, null);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);
        nameCompany = sharedPreferences.getString(TAG_COMP, null);

        Intent intent = getIntent();
        idPrdct = intent.getStringExtra(ID_PRDCT);
        namePrdct = intent.getStringExtra(NAME_PRDCT);
        codePrdct = intent.getStringExtra(CODE_PRDCT);
        descPrdct = intent.getStringExtra(DESC_PRDCT);
        pricePrdct = intent.getStringExtra(PRICE_PRDCT);
        unitPricePrdct = intent.getStringExtra(UNIT_PRDCT);
        unitStockPrdct = intent.getStringExtra(UNIT_PRDCT);
        stockPrdct = intent.getStringExtra(STOCK_PRDCT);
        imgPrdct = intent.getStringExtra(IMG_PRDCT);
        idCat = intent.getStringExtra(ID_CATE);
        catPrdct = intent.getStringExtra(CATE_PRDCT);
        nameSplrPrdct = intent.getStringExtra(SPLR_PRDCT);

        idSplr = intent.getStringExtra(ID_SPLR);
        nameSplr = intent.getStringExtra(NAME_SPLR);
        descSplr = intent.getStringExtra(DESC_SPLR);
        addrSplr = intent.getStringExtra(ADDR_SPLR);
        phoneSplr = intent.getStringExtra(PHONE_SPLR);
        emailSplr = intent.getStringExtra(EMAIL_SPLR);

//        textViewIdPrdct = findViewById(R.id.textViewIdPrdctDetail);
        textViewNamePrdct = findViewById(R.id.textViewNamePrdctDetail);
        textViewCodePrdct = findViewById(R.id.textViewKodePrdctDetail);
        textViewDescPrdct = findViewById(R.id.textViewDescPrdctDetail);
        textViewPricePrdct = findViewById(R.id.textViewPricePrdctDetail);
        textViewUnitPricePrdct = findViewById(R.id.textViewUnitPricePrdctDetail);
        textViewUnitStockPrdct = findViewById(R.id.textViewUnitPrdctDetail);
        textViewStockPrdct = findViewById(R.id.textViewStockPrdctDetail);
        textViewCatPrdct = findViewById(R.id.textViewNameCatePrdctDetail);
        textViewNameSplr = findViewById(R.id.textViewNameSplrPrdctDetail);

        rupiah = NumberFormat.getNumberInstance(new Locale("In", "ID"));

        imgPrdctDetail = findViewById(R.id.imgPrdctDetail);
        imgSplrPrdctDetail = findViewById(R.id.imgSplrPrdctDetail);

        prdctDetail();

        swipeRefreshLayout = findViewById(R.id.SwipeRefreshPrdctDetail);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItem();
            }

            private void refreshItem() {
                prdctDetail();
                onItemLoad();
            }

            private void onItemLoad() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });



//        textViewCatPrdct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentCateDetail = new Intent()
//            }
//        });

        progressDialog.dismiss();

        // Eksternal Intent Phone
        ImageView imgTlpSplrPrdctDetail = (ImageView) findViewById(R.id.imgTlpSplrDetailPrdct);
        imgTlpSplrPrdctDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String numbSplrPrdctDetail = phoneSplr;
                Intent CallSplrPrdct = new Intent(Intent. ACTION_DIAL);
                CallSplrPrdct.setData(Uri. fromParts("tel", numbSplrPrdctDetail,null));
                startActivity(CallSplrPrdct);
            }
        });

        // Eksternal Whatsapp
        ImageView imgWaSplrPrdctDetail = (ImageView) findViewById(R.id.imgWASplrDetailPrdct);
        imgWaSplrPrdctDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isWhatappInstalled()){
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="+phoneSplr+
                            "&text="+messagesWaSplr));
                    startActivity(i);
                }else {
                    Toast.makeText(PrdctDetail.this,"Whatsapp Tidak Ditemukan! Harap Install Whatsapp Terlebih Dahulu.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Eksternal Email
        ImageView imgEmailSplrPrdctDetail = (ImageView) findViewById(R.id.imgEmailSplrDetailPrdct);
        imgEmailSplrPrdctDetail.setOnClickListener(new View.OnClickListener() {
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

        // Intent Detail Supplier
        CardView cardSplrDetailPrdct = findViewById(R.id.cardSplrDetailPrdct);
        cardSplrDetailPrdct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSplrDetail = new Intent(PrdctDetail.this, SupplierDetailActivity.class);

                intentSplrDetail.putExtra(ID_SPLR, idSplr);
                intentSplrDetail.putExtra(NAME_SPLR, nameSplr);
                intentSplrDetail.putExtra(DESC_SPLR, descSplr);
                intentSplrDetail.putExtra(ADDR_SPLR, addrSplr);
                intentSplrDetail.putExtra(PHONE_SPLR, phoneSplr);
                intentSplrDetail.putExtra(EMAIL_SPLR, emailSplr);
//                intentSplrDetail.putExtra(IMG_SPLR, clickedSplr.getImgSplr());

                startActivity(intentSplrDetail);
            }
        });

        // Intent Edit
        btnEdit = findViewById(R.id.btnEditPrdctDetail);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentEdit = new Intent(PrdctDetail.this, AddEditPrdctActivity.class);

                intentEdit.putExtra(ID_PRDCT, idPrdct);
                intentEdit.putExtra(NAME_PRDCT, namePrdct);
                intentEdit.putExtra(CODE_PRDCT, codePrdct);
                intentEdit.putExtra(DESC_PRDCT, descPrdct);
                intentEdit.putExtra(PRICE_PRDCT, pricePrdct);
                intentEdit.putExtra(UNIT_PRDCT, unitPricePrdct);
                intentEdit.putExtra(STOCK_PRDCT, stockPrdct);
                intentEdit.putExtra(ID_CATE, idCat);
                intentEdit.putExtra(CATE_PRDCT, catPrdct);
                intentEdit.putExtra(SPLR_PRDCT, nameSplrPrdct);

                startActivity(intentEdit);
                finish();
            }
        });

        btnDell = findViewById(R.id.btnDellPrdctDetail);
        btnDell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialogDel = new Dialog(PrdctDetail.this);

                dialogDel.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogDel.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogDel.setContentView(R.layout.dialog_delete);

                TextView textWarnDel = dialogDel.findViewById(R.id.textDelWarning);
                textWarnDel.setText("Apa Anda yakin ingin menghapus [" +namePrdct+ "] dari data Anda?");

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

                        delPrdct();
                        dialogDel.dismiss();
                    }
                });
                dialogDel.show();
            }
        });



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(namePrdct);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_twotone_chevron_left_24);


    }

    // Detail Profile
    public void prdctDetail(){
        progressDialog = new ProgressDialog(PrdctDetail.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Profil");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);

        AndroidNetworking.post(urlPrdctDetail)
                .addBodyParameter("idPrdct", idPrdct.toString())
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

//                                textViewIdPrdct.setText(jsonObject.getString("idPrdct"));
                                textViewNamePrdct.setText(jsonObject.getString("namePrdct"));
                                textViewCodePrdct.setText(jsonObject.getString("codePrdct"));
                                textViewDescPrdct.setText(jsonObject.getString("descPrdct"));

                                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("In","ID"));
                                double formatRpPrdct = Double.parseDouble(jsonObject.getString("unitPrice"));


                                textViewPricePrdct.setText(formatRupiah.format(formatRpPrdct));
                                textViewUnitPricePrdct.setText(jsonObject.getString("unitPrdct"));
                                textViewUnitStockPrdct.setText(jsonObject.getString("unitPrdct"));
                                textViewStockPrdct.setText(jsonObject.getString("stockPrdct"));
                                textViewCatPrdct.setText(jsonObject.getString("nameCategory"));
                                textViewNameSplr.setText(jsonObject.getString("nameSupplier"));

                                Picasso.get().load(URL_SPLR_IMG+jsonObject.getString("imgSupplier"))
                                        .resize(450, 450)
                                        .centerCrop()
                                        .placeholder(R.drawable.default_image_comp_small)
                                        .error(R.drawable.default_image_comp_small)
                                        .into(imgSplrPrdctDetail);

                                Picasso.get().load(URL_PRDCT_IMG+jsonObject.getString("imgPrdct"))
                                        .resize(450, 450)
                                        .centerCrop()
                                        .placeholder(R.drawable.default_image_small)
                                        .error(R.drawable.default_image_small)
                                        .into(imgPrdctDetail);

                                Log.e("ImgSplr", "Image: "+URL_PRDCT_IMG+jsonObject.getString("imgPrdct"));
                                getSupportActionBar().setTitle(jsonObject.getString("namePrdct"));

                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PrdctDetail.this, "Maaf, gagal Terhubung ke Database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(PrdctDetail.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();

                    }
                });
    }

    private void delPrdct() {
        ProgressDialog progressDialog = new ProgressDialog(PrdctDetail.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Produk");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);


        AndroidNetworking.post(urlPrdctDel)
                .addBodyParameter("idPrdct", idPrdct.toString())
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
                                new AlertDialog.Builder(PrdctDetail.this)
                                        .setMessage("Berhasil Menghapus Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = getIntent();
                                                setResult(RESULT_OK, intent);
                                                PrdctDetail.this.finish();
                                                onBackPressed();
                                            }
                                        })
                                        .show();
                            }else{
                                new AlertDialog.Builder(PrdctDetail.this)
                                        .setMessage("Gagal Mengupdate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = getIntent();
                                                setResult(RESULT_CANCELED,i);
                                                PrdctDetail.this.finish();
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
                        Toast.makeText(PrdctDetail.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();

                    }
                });
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
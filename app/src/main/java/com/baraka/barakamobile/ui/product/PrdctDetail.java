package com.baraka.barakamobile.ui.product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.supplier.SupplierDetailActivity;

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

public class PrdctDetail extends AppCompatActivity {

    String messagesWaSplr = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prdct_detail);

        ProgressDialog progressDialog = new ProgressDialog(PrdctDetail.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data..");
        progressDialog.show();

        Intent intent = getIntent();
        String idPrdct = intent.getStringExtra(ID_PRDCT);
        String namePrdct = intent.getStringExtra(NAME_PRDCT);
        String codePrdct = intent.getStringExtra(CODE_PRDCT);
        String descPrdct = intent.getStringExtra(DESC_PRDCT);
        String pricePrdct = intent.getStringExtra(PRICE_PRDCT);
        String unitPricePrdct = intent.getStringExtra(UNIT_PRDCT);
        String unitStockPrdct = intent.getStringExtra(UNIT_PRDCT);
        String stockPrdct = intent.getStringExtra(STOCK_PRDCT);
        String idCat = intent.getStringExtra(ID_CATE);
        String catPrdct = intent.getStringExtra(CATE_PRDCT);
        String nameSplrPrdct = intent.getStringExtra(SPLR_PRDCT);

        String idSplr = intent.getStringExtra(ID_SPLR);
        String nameSplr = intent.getStringExtra(NAME_SPLR);
        String descSplr = intent.getStringExtra(DESC_SPLR);
        String addrSplr = intent.getStringExtra(ADDR_SPLR);
        String phoneSplr = intent.getStringExtra(PHONE_SPLR);
        String emailSplr = intent.getStringExtra(EMAIL_SPLR);

        TextView textViewNamePrdct = findViewById(R.id.textViewNamePrdctDetail);
        TextView textViewCodePrdct = findViewById(R.id.textViewKodePrdctDetail);
        TextView textViewDescPrdct = findViewById(R.id.textViewDescPrdctDetail);
        TextView textViewPricePrdct = findViewById(R.id.textViewPricePrdctDetail);
        TextView textViewUnitPricePrdct = findViewById(R.id.textViewUnitPricePrdctDetail);
        TextView textViewUnitStockPrdct = findViewById(R.id.textViewUnitPrdctDetail);
        TextView textViewStockPrdct = findViewById(R.id.textViewStockPrdctDetail);
        TextView textViewCatPrdct = findViewById(R.id.textViewNameCatePrdctDetail);
        TextView textViewNameSplr = findViewById(R.id.textViewNameSplrPrdctDetail);

        textViewNamePrdct.setText(namePrdct);
        textViewCodePrdct.setText(codePrdct);
        textViewDescPrdct.setText(descPrdct);
        textViewPricePrdct.setText(pricePrdct);
        textViewUnitPricePrdct.setText(unitPricePrdct);
        textViewUnitStockPrdct.setText(unitStockPrdct);
        textViewStockPrdct.setText(stockPrdct);
        textViewCatPrdct.setText(catPrdct);
        textViewNameSplr.setText(nameSplr);

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
        Button btnEdit = findViewById(R.id.btnEditPrdctDetail);
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
            }
        });



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(namePrdct);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_twotone_chevron_left_24);


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

    public void getPrdctDetail(){

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
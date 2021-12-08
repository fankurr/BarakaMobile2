package com.baraka.barakamobile.ui.pos;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baraka.barakamobile.R;
//import com.baraka.barakamobile.ui.pos.DB_POST.PosOut_DB;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.baraka.barakamobile.ui.pos.POSCardAdapter.ID_PRDCT;
import static com.baraka.barakamobile.ui.pos.POSCardAdapter.NAME_PRDCT;
import static com.baraka.barakamobile.ui.pos.POSCardAdapter.PRICE_PRDCT;
import static com.baraka.barakamobile.ui.pos.POSCardAdapter.STOCK_PRDCT;
import static com.baraka.barakamobile.ui.pos.POSCardAdapter.UNIT_PRDCT;


public class InputPOSActivity extends AppCompatActivity {

    private ArrayList<POSOutputViewModel> posOutputViewModelList;
    ArrayList<POSViewModel> posViewModelList;
    private POSViewModel posViewModel;
    private POSOutputViewModel posOutputViewModel;

    private RecyclerView recyclerView;

    POSCardAdapter posCardAdapter;
    POSOutputAdapter posOutputAdapter;

//    PosOut_DB posOut_db;


    TextView txtNamaPrdct,txtPricePrdct,txtUnitPrdct,txtIdPrdct;
    EditText inputJumlah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_pos);

        ProgressDialog progressDialog = new ProgressDialog(InputPOSActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data..");
        progressDialog.show();


        Intent intent = getIntent();
        final String idPrdctInput = intent.getStringExtra(ID_PRDCT);
        final String namePrdctInput = intent.getStringExtra(NAME_PRDCT);
        final String pricePrdctInput = intent.getStringExtra(PRICE_PRDCT);
        final String unitPrdctInput = intent.getStringExtra(UNIT_PRDCT);
        final String stockPrdctInput = intent.getStringExtra(STOCK_PRDCT);

        txtIdPrdct = findViewById(R.id.txtIdPrdctPosInput);
        txtNamaPrdct = findViewById(R.id.textViewNamePrdctPosInput);
        txtPricePrdct = findViewById(R.id.textViewPricePrdctPosInput);
        txtUnitPrdct = findViewById(R.id.textViewUnitPrdctPosInput);
        TextView txtStockPrdct = findViewById(R.id.textViewStokPrdctPosInput);
        inputJumlah = findViewById(R.id.inputJumlahPembelianPrdct);

        txtIdPrdct.setText(idPrdctInput);
        txtNamaPrdct.setText(namePrdctInput);
        txtPricePrdct.setText(pricePrdctInput);
        txtUnitPrdct.setText(unitPrdctInput);
        txtStockPrdct.setText(stockPrdctInput);
//        inputJumlah.getText().toString();

        progressDialog.dismiss();

//        posOut_db = posOut_db.createDatabase(this);

        Button btnAddInputPos = findViewById(R.id.btnTambahPosInput);

        btnAddInputPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                getData();

                finish();
//                TextView namePrdct = findViewById(R.id.txtViewNamePrdctPosOutput);
//                TextView pricePrdctOutput = findViewById(R.id.txtViewPricePrdctPosOutput);
//                TextView jmlhPrdctOutput = findViewById(R.id.txtViewJmlhPrdctPosOutput);
//                TextView unitPrdctOutput = findViewById(R.id.txtViewUnitPrdctPosOutput);

                Toast.makeText(InputPOSActivity.this,  inputJumlah.getText().toString()+ " " +unitPrdctInput+ " " +namePrdctInput+ " Ditambahkan", Toast.LENGTH_LONG).show();
                onBackPressed();

            }
        });

        Button btnBackInputPos = findViewById(R.id.btnBatalPosInput);
        btnBackInputPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(namePrdctInput);
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

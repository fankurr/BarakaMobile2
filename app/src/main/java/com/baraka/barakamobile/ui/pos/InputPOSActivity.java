package com.baraka.barakamobile.ui.pos;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.util.DbConfig;
//import com.baraka.barakamobile.ui.pos.DB_POST.PosOut_DB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static com.baraka.barakamobile.ui.pos.POSCardAdapter.ID_PRDCT;
import static com.baraka.barakamobile.ui.pos.POSCardAdapter.NAME_PRDCT;
import static com.baraka.barakamobile.ui.pos.POSCardAdapter.PRICE_PRDCT;
import static com.baraka.barakamobile.ui.pos.POSCardAdapter.STOCK_PRDCT;
import static com.baraka.barakamobile.ui.pos.POSCardAdapter.UNIT_PRDCT;

import org.json.JSONException;
import org.json.JSONObject;


public class InputPOSActivity extends AppCompatActivity {

    private ArrayList<POSOutputViewModel> posOutputViewModelList;
    ArrayList<POSViewModel> posViewModelList;
    private POSViewModel posViewModel;
    private POSOutputViewModel posOutputViewModel;


    private final static String TAG_ID = "id";
    private final static String TAG_EMAIL = "email";
    private final static String TAG_NAME = "name";
    private final static String TAG_LEVEL = "level";
    private final static String TAG_ACCESS = "access";
    private final static String TAG_IDCOMP = "idCompany";
    private final static String TAG_COMP = "nameCompany";

    private RecyclerView recyclerView;

    POSCardAdapter posCardAdapter;
    POSOutputAdapter posOutputAdapter;

//    PosOut_DB posOut_db;

    Calendar calender;
    SimpleDateFormat simpledateformat;
    String date;

    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    String idPrdctInput, namePrdctInput, pricePrdctInput, unitPrdctInput, stockPrdctInput;

    TextView txtNamaPrdct,txtPricePrdct,txtUnitPrdct,txtIdPrdct,txtStockPrdct;
    EditText inputJumlah;

    String id, email, name, level, access, idCompany, nameCompany;
    float valueTx, inputValTx, prdctPrice;


    private String TX = DbConfig.URL_TX + "addTx.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_pos);

        ProgressDialog progressDialog = new ProgressDialog(InputPOSActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data..");
        progressDialog.show();

        calender = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("EEE, dd-MM-yyyy HH:mm:ss");
        date = simpledateformat.format(calender.getTime());

        Intent intent = getIntent();
        idPrdctInput = intent.getStringExtra(ID_PRDCT);
        namePrdctInput = intent.getStringExtra(NAME_PRDCT);
        pricePrdctInput = intent.getStringExtra(PRICE_PRDCT);
        unitPrdctInput = intent.getStringExtra(UNIT_PRDCT);
        stockPrdctInput = intent.getStringExtra(STOCK_PRDCT);

        txtIdPrdct = findViewById(R.id.txtIdPrdctPosInput);
        txtNamaPrdct = findViewById(R.id.textViewNamePrdctPosInput);
        txtPricePrdct = findViewById(R.id.textViewPricePrdctPosInput);
        txtUnitPrdct = findViewById(R.id.textViewUnitPrdctPosInput);
        txtStockPrdct = findViewById(R.id.textViewStokPrdctPosInput);
        inputJumlah = findViewById(R.id.inputJumlahPembelianPrdct);

        txtIdPrdct.setText(idPrdctInput);
        txtNamaPrdct.setText(namePrdctInput);
        txtPricePrdct.setText(pricePrdctInput);
        txtUnitPrdct.setText(unitPrdctInput);
        txtStockPrdct.setText(stockPrdctInput);
        inputJumlah.getText().toString();


        progressDialog.dismiss();

//        posOut_db = posOut_db.createDatabase(this);

        Button btnAddInputPos = findViewById(R.id.btnTambahPosInput);

        btnAddInputPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                getData();
//                TextView namePrdct = findViewById(R.id.txtViewNamePrdctPosOutput);
//                TextView pricePrdctOutput = findViewById(R.id.txtViewPricePrdctPosOutput);
//                TextView jmlhPrdctOutput = findViewById(R.id.txtViewJmlhPrdctPosOutput);
//                TextView unitPrdctOutput = findViewById(R.id.txtViewUnitPrdctPosOutput);

                Toast.makeText(InputPOSActivity.this,  inputJumlah.getText().toString()+ " " +unitPrdctInput+ " " +namePrdctInput+ " Ditambahkan", Toast.LENGTH_LONG).show();
                onBackPressed();

                finish();

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

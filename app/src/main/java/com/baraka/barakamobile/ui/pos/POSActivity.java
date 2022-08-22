package com.baraka.barakamobile.ui.pos;


import static com.baraka.barakamobile.ui.pos.POSCardAdapter.NAME_PRDCT;
import static com.baraka.barakamobile.ui.pos.POSCardAdapter.PRICE_PRDCT;
import static com.baraka.barakamobile.ui.pos.POSCardAdapter.STOCK_PRDCT;
import static com.baraka.barakamobile.ui.pos.POSCardAdapter.UNIT_PRDCT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.BuildConfig;
import com.baraka.barakamobile.MainActivity;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.product.CateCardAdpater;
import com.baraka.barakamobile.ui.product.CateViewModel;
import com.baraka.barakamobile.ui.product.PrdctCardAdapter;
import com.baraka.barakamobile.ui.product.PrdctDetail;
import com.baraka.barakamobile.ui.product.PrdctViewModel;
import com.baraka.barakamobile.ui.product.ProductFragment;
import com.baraka.barakamobile.ui.product.ProductViewModel;
import com.baraka.barakamobile.ui.supplier.SupplierDetailActivity;
import com.baraka.barakamobile.ui.usermanaje.WorkerLogViewModel;
import com.baraka.barakamobile.ui.util.DbConfig;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// ============================================= //

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class POSActivity extends AppCompatActivity {

    private POSViewModel posViewModel;
    private RecyclerView recyclerView, recyclerView1;
    private RecyclerView.LayoutManager layoutManager;
    private POSCardAdapter posCardAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    ListView listViewPosCheckOut;
    POSOutputAdapter posOutputAdapter;

    SlidingUpPanelLayout slidingUpPanelLayoutPos;

    private String URL_PRDCT = DbConfig.URL_PRDCT + "allPrdct.php";
    private String URL_CATE = DbConfig.URL_CATE + "allCat.php";
    private String TX = DbConfig.URL_TX + "addTx.php";

    public static final String ID_PRDCT_POS = "idProduct";
    public static final String NAME_PRDCT_POS = "namePrdct";
    public static final String CATE_PRDCT_POS = "nameCategory";
    public static final String DESC_PRDCT_POS = "description";
    public static final String PRICE_PRDCT_POS = "unitPrice";
    public static final String UNIT_PRDCT_POS = "unit";
    public static final String STOCK_PRDCT_POS = "stockPrct";

//    private final static String TAG_ID = "idUser";
//    private final static String TAG_EMAIL = "emailUser";
//    private final static String TAG_NAME = "nameUser";
//    private final static String TAG_LEVEL = "lvlUser";
//    private final static String TAG_ACCESS = "loginUser";
//    private final static String TAG_IDCOMP = "compUser";
//    private final static String TAG_COMP = "nameComp";

    private final static String TAG_ID = "id";
    private final static String TAG_EMAIL = "email";
    private final static String TAG_NAME = "name";
    private final static String TAG_LEVEL = "level";
    private final static String TAG_ACCESS = "access";
    private final static String TAG_IDCOMP = "idCompany";
    private final static String TAG_COMP = "nameCompany";

    String id, email, name, level, access, idCompany, nameCompany;
    TextView txtIdTx, txtNamaPrdct,txtPricePrdct,txtUnitPrdct,txtIdPrdct,txtStockPrdct, totalPrdctIn;
    TextView txtIdPrdctOut, txtPricePrdctOut, txtQtyOut, txtTotalOut;
    String totalPrdctPrice;
    EditText inputJumlah;
    TextView totalTxt;

    POSOutputViewModel IdPrdct;
    POSOutputViewModel NamePrdct;
    POSOutputViewModel UnitPrice;
    POSOutputViewModel JmlhPrdct;
    POSOutputViewModel Total;

    private File pdfFile;
    String pdfname;
    Context context;

    Calendar calender;
    SimpleDateFormat simpledateformat;
    String date;

    float valueTx, inputValTx, prdctPrice;

    private ArrayList<POSViewModel> posViewModelList;
//    private ArrayList<POSOutputViewModel> posOutputViewModelList;

    private List<POSOutputViewModel> posOutputViewModelList;


    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);

        slidingUpPanelLayoutPos = (SlidingUpPanelLayout) findViewById(R.id.sliding_pos);
        slidingUpPanelLayoutPos.addPanelSlideListener(onSlideListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.point_of_sale);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_twotone_chevron_left_24);

        sharedPreferences = getSharedPreferences(my_shared_preferences,Context.MODE_PRIVATE);
        id = sharedPreferences.getString(TAG_ID, null);
        email = sharedPreferences.getString(TAG_EMAIL, null);
        name = sharedPreferences.getString(TAG_NAME, null);
        level = sharedPreferences.getString(TAG_LEVEL, null);
        access = sharedPreferences.getString(TAG_ACCESS, null);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);
        nameCompany = sharedPreferences.getString(TAG_COMP, null);

        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewPOS);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(posCardAdapter);
        recyclerView.setHasFixedSize(true);
        posViewModelList = new ArrayList<>();

        calender = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("EEE, dd-MM-yyyy HH:mm:ss");
        date = simpledateformat.format(calender.getTime());

        posCardAdapter = new POSCardAdapter(posViewModelList, this);

        totalTxt = (TextView) findViewById(R.id.textViewPosTotal);

        txtIdPrdctOut = (TextView) findViewById(R.id.txtIdPrdctPosOutput);
        txtPricePrdctOut = (TextView) findViewById(R.id.txtViewPricePrdctPosOutput);
        txtQtyOut = (TextView) findViewById(R.id.txtViewJmlhPrdctPosOutput);
        txtTotalOut = (TextView) findViewById(R.id.txtViewPricePrdctPosOutputTotal);

        //Fetch Data Produk
        getPrdct();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshPOS);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItem();
            }

            private void refreshItem() {
                //Fetch Data Produk
                getPrdct();
                onItemLoad();
            }

            private void onItemLoad() {

                swipeRefreshLayout.setRefreshing(false);

            }
        });


//onclick pada card recycleview
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                public boolean onSingleTapUp(MotionEvent e){
                    return true;
                }
            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)){
                    int position = rv.getChildAdapterPosition(child);
                            /*Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                            i.putExtra("id", movies.get(position).getId());
                            getApplicationContext().startActivity(i);*/
                    Toast.makeText(getApplicationContext(), "Id : " + posViewModelList.get(position).getNamePrdct() + " selected", Toast.LENGTH_SHORT).show();

                    final Dialog dialogPosInput = new Dialog(POSActivity.this);

                    dialogPosInput.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogPosInput.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogPosInput.setContentView(R.layout.activity_input_pos);
                    dialogPosInput.setCancelable(true);

                    txtIdPrdct = dialogPosInput.findViewById(R.id.txtIdPrdctPosInput);
                    txtNamaPrdct = dialogPosInput.findViewById(R.id.textViewNamePrdctPosInput);
                    txtPricePrdct = dialogPosInput.findViewById(R.id.textViewPricePrdctPosInput);
                    txtUnitPrdct = dialogPosInput.findViewById(R.id.textViewUnitPrdctPosInput);
                    txtStockPrdct = dialogPosInput.findViewById(R.id.textViewStokPrdctPosInput);
                    totalPrdctIn = dialogPosInput.findViewById(R.id.txtTotalPrdctPosInput);
                    inputJumlah = dialogPosInput.findViewById(R.id.inputJumlahPembelianPrdct);


                    txtIdPrdct.setText(String.valueOf(posViewModelList.get(position).getIdPrdct()));
                    txtNamaPrdct.setText(posViewModelList.get(position).getNamePrdct());
                    txtPricePrdct.setText(posViewModelList.get(position).getUnitPrice());
                    txtUnitPrdct.setText(posViewModelList.get(position).getUnitPrdct());
                    txtStockPrdct.setText(posViewModelList.get(position).getStockPrdct());

                    Button btnTambahPos = dialogPosInput.findViewById(R.id.btnTambahPosInput);
                    btnTambahPos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            posOutputViewModelList.clear();

                            if (TextUtils.isEmpty(inputJumlah.getText().toString())){
                                inputJumlah.setError("Harap Masukan Jumlah Barang!");
                                inputJumlah.requestFocus();
                            }else{

                                float price = Float.parseFloat(posViewModelList.get(position).getUnitPrice());
                                float input = Float.parseFloat(inputJumlah.getText().toString());

                                float currTotal = price*input;
                                totalPrdctPrice = String.valueOf(currTotal);

                                POSOutputViewModel posOutputViewModel = new POSOutputViewModel(

                                        txtIdPrdct.getText().toString(),
                                        txtNamaPrdct.getText().toString(),
                                        txtPricePrdct.getText().toString(),
                                        txtUnitPrdct.getText().toString(),
                                        inputJumlah.getText().toString(),
                                        totalPrdctPrice
                                );

                                ProgressDialog progressDialog = new ProgressDialog(POSActivity.this);
                                progressDialog.setCancelable(false);
                                progressDialog.setMessage("Proses Checkout..");
                                progressDialog.show();

                                sharedPreferences = POSActivity.this.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                                idCompany = sharedPreferences.getString(TAG_IDCOMP, null);
//                                inputValTx = Integer.parseInt(inputJumlah.getText().toString());
//                                prdctPrice = Integer.parseInt(String.valueOf(price));
//
//                                valueTx = inputValTx * prdctPrice;

                                AndroidNetworking.post(TX)
                                        .addBodyParameter("idComp", idCompany)
                                        .addBodyParameter("idPrdct", txtIdPrdct.getText().toString())
                                        .addBodyParameter("qtyTx", inputJumlah.getText().toString())
                                        .addBodyParameter("valueTx", String.valueOf(currTotal))
                                        .addBodyParameter("datetimeTx", date)
                                        .setTag(this)
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                progressDialog.dismiss();
                                                Log.d("Respon Edit", "" + response);

                                                try {
                                                    Boolean status = response.getBoolean("success");
                                                    if (status == true) {
                                                        new android.app.AlertDialog.Builder(POSActivity.this)
                                                                .setMessage("Penambahan Data Belanja Berhasil!")
                                                                .setCancelable(false)
                                                                .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {

//                                                                        Log.e("Input", "Data: "+idCompany+", "+idCompPay+", "+valPay+", "+descPay+", "+datetimePay+", "+signPay.toString());
                                                                        Context context = POSActivity.this;
                                                                        POSActivity.this.setResult(RESULT_OK);
                                                                        android.app.AlertDialog optionDialog = new android.app.AlertDialog.Builder(POSActivity.this).create();
                                                                        optionDialog.dismiss();
                                                                        Toast.makeText(context, "Berhasil Menambah Data Belanja", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {

                                                        new android.app.AlertDialog.Builder(POSActivity.this)
                                                                .setMessage("Penambahan Data Belanja Gagal!")
                                                                .setCancelable(false)
                                                                .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        Context c = POSActivity.this;
                                                                        android.app.AlertDialog optionDialog = new android.app.AlertDialog.Builder(POSActivity.this).create();
                                                                        optionDialog.dismiss();
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
                                                Toast.makeText(POSActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                                                Log.i("ERROR", "error => " + anError.toString());
                                                progressDialog.dismiss();
                                            }
                                        });

//                                Log.i("POS Out => ", "Items: "+IdPrdct.getIdPrdct()+
//                                        ", "+NamePrdct.getNamePrdct()+
//                                        ", "+UnitPrice.getUnitPrice()+
//                                        ", "+Total.getTotal()+
//                                        ", "+JmlhPrdct.getQtyPrdct().toString());



                                float totalPrice = 0;
                                totalPrice += currTotal;
                                totalTxt.setText(String.valueOf(totalPrice));
                                for (int i = 0; i<posOutputViewModelList.size(); i++)
                                {


                                        float post = Float.parseFloat(posOutputViewModelList.get(i).getTotal());
                                        totalPrice += post;

                                        Toast.makeText(POSActivity.this, "Total: " + totalPrice, Toast.LENGTH_SHORT).show();

                                        Log.i("i", "= " + i);
                                        Log.i("posOutputViewModelList ", "= " + post);
                                        Log.i("size", "= " + posOutputViewModelList.size());



                                        totalTxt.setText(String.valueOf(totalPrice));
                                        posOutputAdapter.notifyDataSetChanged();

                                }


                                posOutputViewModelList.add(posOutputViewModel);
                                POSOutputAdapter posOutputAdapter = new POSOutputAdapter(POSActivity.this, posOutputViewModelList);
                                recyclerView1.setAdapter(posOutputAdapter);
                                posCardAdapter.notifyDataSetChanged();
                                dialogPosInput.dismiss();
                            }

                        }
                    });

                    Button btnBatalPos = dialogPosInput.findViewById(R.id.btnBatalPosInput);
                    btnBatalPos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogPosInput.dismiss();
                        }
                    });

                    dialogPosInput.show();

                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        thread.start(); //memanggil thread (refresh textview)

//sengaja ditaro disini, kalu di taru di Slidingup datanya ke delete terus
        recyclerView1 = (RecyclerView) findViewById(R.id.rvPosOutput);

        recyclerView1.setLayoutManager(new LinearLayoutManager(POSActivity.this));
        recyclerView1.setAdapter(posOutputAdapter);
        recyclerView1.setHasFixedSize(true);
        posOutputViewModelList = new ArrayList<>();

        posOutputAdapter = new POSOutputAdapter(POSActivity.this, posOutputViewModelList);



        totalTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posOutputViewModelList.clear();
                totalTxt.setText("00.000");
                recyclerView1.setAdapter(null);
            }
        });

        ImageView imgCheckout = (ImageView) findViewById(R.id.imgCheckout);
        imgCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //untuk menampung data posOutput agar bisa diupload banyak data
//                ArrayList<String> idPrdct = new ArrayList<>();
                ArrayList<String> namaProduct = new ArrayList<>();

                String idPrdct = "";

                if(!posOutputViewModelList.isEmpty()) {
                    for (int i = 0; i < posOutputViewModelList.size(); i++) {
//                        idPrdct.add(posOutputViewModelList.get(i).getIdPrdct());
                        namaProduct.add(posOutputViewModelList.get(i).getNamePrdct());

                        idPrdct = posOutputViewModelList.get(i).getIdPrdct()+ " ";
                    }

//                    ProgressDialog progressDialog = new ProgressDialog(POSActivity.this);
//                    progressDialog.setCancelable(false);
//                    progressDialog.setMessage("Proses Checkout..");
//                    progressDialog.show();
//
//                    sharedPreferences = POSActivity.this.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
//                    idCompany = sharedPreferences.getString(TAG_IDCOMP, null);
//
//                    String finalIdPrdct = idPrdct;
//                    AndroidNetworking.post(TX)
//                            .addBodyParameter("idPrdctTx", "5")
//                            .setTag(this)
//                            .setPriority(Priority.MEDIUM)
//                            .build()
//                            .getAsJSONObject(new JSONObjectRequestListener() {
//                                @Override
//                                public void onResponse(JSONObject response) {
//                                    progressDialog.dismiss();
//                                    Log.d("Respon Edit",""+response);
//
//                                    try {
//                                        Boolean status = response.getBoolean("success");
//                                        if (status == true){
//                                            new android.app.AlertDialog.Builder(POSActivity.this)
//                                                    .setMessage("Checkout Berhasil!")
//                                                    .setCancelable(false)
//                                                    .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialog, int which) {
//                                                            Context context = POSActivity.this;
//                                                            POSActivity.this.setResult(RESULT_OK);
//                                                            android.app.AlertDialog optionDialog = new android.app.AlertDialog.Builder(POSActivity.this).create();
//                                                            optionDialog.dismiss();
//                                                            Toast.makeText(context, "Berhasil Menambah Data Pengeluaran", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    })
//                                                    .show();
//                                        }else{
//
//                                            new android.app.AlertDialog.Builder(POSActivity.this)
//                                                    .setMessage("Checkout Gagal!")
//                                                    .setCancelable(false)
//                                                    .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialog, int which) {
////                                                Log.i("Input", "Data: "+idCompTx+", "+idCompPay+", "+valPay+", "+descPay+", "+datetimePay+", "+signPay.toString());
//                                                            Context c = POSActivity.this;
//                                                            android.app.AlertDialog optionDialog = new android.app.AlertDialog.Builder(POSActivity.this).create();
//                                                            optionDialog.dismiss();
//                                                        }
//                                                    })
//                                                    .show();
//                                        }
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                                @Override
//                                public void onError(ANError anError) {
//                                    Toast.makeText(POSActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
//                                    Log.d("ERROR","error => "+ anError.toString());
//                                    Log.i("Input", "Data: "+idCompany+", "+ finalIdPrdct.toString());
//                                    progressDialog.dismiss();
//                                }
//                            });

                    Log.i("Id Product",idPrdct.toString());
                    Log.i("Nama Produk",namaProduct.toString());
                }

//                try {
//                    createPdf();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (DocumentException e) {
//                    e.printStackTrace();
//                }
                addTx(idPrdct);
            }
        });

    }

//    public void clearOutpur() {
//        int size = posViewModelList.size();
//        if (size > 0) {
//            for (int i = 0; i < size; i++) {
//                posOutputViewModelList.remove(0);
//            }
//
//            notifyItemRangeRemoved(0, size);
//        }
//    }

    private void getPrint(){
//        posOutputViewModelList = posOutputAdapter.getPrintView();
//        Document document = new Document(PageSize.A4);
//        final File file = new File(getStorageDir("PDF"), "print.pdf");
//        try {
//            PdfWriter.getInstance
//        }

    }

    // untuk merefresh textview total belanja yang diinput(pos), setelah diahpus salah satu
    Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                while (!thread.isInterrupted()) {
                    Thread.sleep(100);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // update TextView here!
                            float totalPrice = 0;
                            //                                totalTxt.setText(String.valueOf(totalPrice));
                            for (int i = 0; i<posOutputViewModelList.size(); i++)
                            {
                                float post = Float.parseFloat(posOutputViewModelList.get(i).getTotal());
                                totalPrice += post;
                                posOutputAdapter.notifyDataSetChanged();
                            }

                            totalTxt.setText(String.valueOf(totalPrice));

                        }
                    });
                }
            } catch (InterruptedException e) {
            }
        }
    };

    private SlidingUpPanelLayout.PanelSlideListener onSlideListener() {
        return new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        };
    }

//    public void sumTotal(int price, int input){
//        int currTotal;
//
//        price = Integer.parseInt(String.valueOf(txtPricePrdct));
//        input = Integer.parseInt(inputJumlah.getText().toString());
//
//        currTotal = price+input;
//
//        TextView totalTxt = (TextView) findViewById(R.id.textViewPosTotal);
//        totalTxt.setText(String.valueOf(currTotal));
//    }

    private void addTx(String idPrdct){

        ProgressDialog progressDialog = new ProgressDialog(POSActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Proses Checkout..");
        progressDialog.show();

        sharedPreferences = POSActivity.this.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        String finalIdPrdct = idPrdct;
        AndroidNetworking.post(TX)
                .addBodyParameter("idPrdctTx", idPrdct)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("Respon Edit",""+response);

                        try {
                            Boolean status = response.getBoolean("success");
                            if (status == true){
                                new android.app.AlertDialog.Builder(POSActivity.this)
                                        .setMessage("Checkout Berhasil!")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Context context = POSActivity.this;
                                                POSActivity.this.setResult(RESULT_OK);
                                                android.app.AlertDialog optionDialog = new android.app.AlertDialog.Builder(POSActivity.this).create();
                                                optionDialog.dismiss();
                                                Toast.makeText(context, "Berhasil Menambah Data Pengeluaran", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .show();
                            }else{

                                new android.app.AlertDialog.Builder(POSActivity.this)
                                        .setMessage("Checkout Gagal!")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
//                                                Log.i("Input", "Data: "+idCompTx+", "+idCompPay+", "+valPay+", "+descPay+", "+datetimePay+", "+signPay.toString());
                                                Context c = POSActivity.this;
                                                android.app.AlertDialog optionDialog = new android.app.AlertDialog.Builder(POSActivity.this).create();
                                                optionDialog.dismiss();
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
                        Toast.makeText(POSActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        Log.i("Input", "Data: "+idCompany+", "+ finalIdPrdct.toString());
                        progressDialog.dismiss();
                    }
                });

//        for (int i = 0; i < posOutputViewModelList.size(); i++) {
//            IdPrdct = posOutputViewModelList.get(i);
//            NamePrdct = posOutputViewModelList.get(i);
//            UnitPrice = posOutputViewModelList.get(i);
//            JmlhPrdct = posOutputViewModelList.get(i);
//            Total = posOutputViewModelList.get(i);
//            String IdPrdctL = IdPrdct.getIdPrdct();
//            String NamePrdctL = NamePrdct.getNamePrdct();
//            String UnitPriceL = UnitPrice.getUnitPrice();
//            String TotalL = Total.getTotal();
//            String JumlahL = JmlhPrdct.getQtyPrdct();
//        }

//        ProgressDialog progressDialog = new ProgressDialog(POSActivity.this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Proses Checkout..");
//        progressDialog.show();
//
//        sharedPreferences = POSActivity.this.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
//        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);
//
//        AndroidNetworking.post(TX)
//                .addBodyParameter("idCompTx", idCompany)
//                .addBodyParameter("idPrdctTx", IdPrdct.getIdPrdct())
//                .addBodyParameter("qtyTx", JmlhPrdct.getQtyPrdct())
//                .addBodyParameter("valueTx", Total.getTotal())
//                .addBodyParameter("datetimeTx", date)
//                .setTag(this)
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        progressDialog.dismiss();
//                        Log.d("Respon Edit",""+response);
//
//                        try {
//                            Boolean status = response.getBoolean("success");
//                            if (status == true){
//                                new android.app.AlertDialog.Builder(POSActivity.this)
//                                        .setMessage("Checkout Berhasil!")
//                                        .setCancelable(false)
//                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                Context context = POSActivity.this;
//                                                POSActivity.this.setResult(RESULT_OK);
//                                                android.app.AlertDialog optionDialog = new android.app.AlertDialog.Builder(POSActivity.this).create();
//                                                optionDialog.dismiss();
//                                                Toast.makeText(context, "Berhasil Menambah Data Pengeluaran", Toast.LENGTH_SHORT).show();
//                                            }
//                                        })
//                                        .show();
//                            }else{
//
//                                new android.app.AlertDialog.Builder(POSActivity.this)
//                                        .setMessage("Checkout Gagal!")
//                                        .setCancelable(false)
//                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
////                                                Log.i("Input", "Data: "+idCompTx+", "+idCompPay+", "+valPay+", "+descPay+", "+datetimePay+", "+signPay.toString());
//                                                Context c = POSActivity.this;
//                                                android.app.AlertDialog optionDialog = new android.app.AlertDialog.Builder(POSActivity.this).create();
//                                                optionDialog.dismiss();
//                                            }
//                                        })
//                                        .show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        Toast.makeText(POSActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
//                        Log.d("ERROR","error => "+ anError.toString());
//                        Log.i("Input", "Data: "+idCompany+", "+txtIdPrdctOut+", "+txtQtyOut+", "+txtTotalOut+", "+date.toString());
//                        progressDialog.dismiss();
//                    }
//                });
    }

    private void getPrdct() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data..");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,Context.MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        AndroidNetworking.post(URL_PRDCT)
                .addBodyParameter("idCompPrdct", idCompany)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        posViewModelList.clear();
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                POSViewModel posViewModel = new POSViewModel(

                                        jsonObject.getInt("idProduct"),
                                        jsonObject.getString("nameProduct"),
                                        jsonObject.getString("price"),
                                        jsonObject.getString("unit"),
                                        jsonObject.getString("stock")

//                                        jsonObject.getInt("idProduct"),
//                                        jsonObject.getString("nameProduct"),
//                                        jsonObject.getString("description"),
//                                        jsonObject.getString("category"),
//                                        jsonObject.getString("supplierProduct"),
//                                        jsonObject.getString("price"),
//                                        jsonObject.getString("unit"),
//                                        jsonObject.getString("stock"),
//                                        jsonObject.getString("imageProduct")
//
//                                        jsonObject.getInt("idProduct"),
//                                        jsonObject.getString("nameProduct"),
//                                        jsonObject.getString("codeProduct"),
//                                        jsonObject.getString("supplierProduct"),
//                                        jsonObject.getString("price"),
//                                        jsonObject.getString("unit"),
//                                        jsonObject.getString("stock"),
//                                        jsonObject.getString("lastUpdate"),
//                                        jsonObject.getString("qtyUpdate"),
//                                        jsonObject.getString("updateBy"),
//                                        jsonObject.getString("description"),
//                                        jsonObject.getString("imageProduct"),
//                                        jsonObject.getInt("idCategory"),
//                                        jsonObject.getString("idCompCategory"),
//                                        jsonObject.getString("nameCategory"),
//                                        jsonObject.getString("descCategory"),
//                                        jsonObject.getString("imageCategory"),
//                                        jsonObject.getInt("idSupplier"),
//                                        jsonObject.getString("idCompSupplier"),
//                                        jsonObject.getString("nameSupplier"),
//                                        jsonObject.getString("addrSupplier"),
//                                        jsonObject.getString("phoneSupplier"),
//                                        jsonObject.getString("emailSupplier"),
//                                        jsonObject.getString("descSupplier"),
//                                        jsonObject.getString("imgSupplier")

                                );
//                                creating adapter object and setting it to recyclerview

                                posViewModelList.add(posViewModel);
                                POSCardAdapter posCardAdapter = new POSCardAdapter(posViewModelList, POSActivity.this);
                                recyclerView.setAdapter(posCardAdapter);
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        posCardAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(POSActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    private void createPdf() throws FileNotFoundException, DocumentException {
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/POSBaraka");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i("Print: ", "Created a new directory for PDF");
        }

        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        pdfname = "Struk_" + nameCompany + "_" + timeStamp + ".pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);

        OutputStream output = new FileOutputStream(pdfFile);

//        BaseFont baseFont = BaseFont.createFont("res/font/ubuntu_mono.ttf", "UTF-8", BaseFont.EMBEDDED);
//        Font urFontName = new Font(baseFont, 12);
        Document document = new Document(PageSize.A4);

        PdfPTable table = new PdfPTable(new float[]{1, 3, 3, 3, 3});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(50);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("No.");
        table.addCell("Nama Produk");
        table.addCell("Harga");
        table.addCell("Jumlah");
        table.addCell("Total");
        table.setHeaderRows(1);

        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.WHITE);
        }

        for (int i = 0; i < posOutputViewModelList.size(); i++) {
            NamePrdct = posOutputViewModelList.get(i);
            UnitPrice = posOutputViewModelList.get(i);
            JmlhPrdct = posOutputViewModelList.get(i);
            Total = posOutputViewModelList.get(i);
            String NamePrdctL = NamePrdct.getNamePrdct();
            String UnitPriceL = UnitPrice.getUnitPrice();
            String TotalL = Total.getTotal();
            String JumlahL = JmlhPrdct.getQtyPrdct();
            table.addCell(String.valueOf(i+1));
            table.addCell(String.valueOf(NamePrdctL));
            table.addCell(String.valueOf(UnitPriceL));
            table.addCell(String.valueOf(JumlahL));
            table.addCell(String.valueOf(TotalL));
        }

        //Step 2
        PdfWriter.getInstance(document, output);

        //Step 3
        document.open();

        //Step 4 Add content
//        document.add((Element) new Paragraph("Test"));
//        document.add((Element) new Paragraph("Test Test Test Test Test Test Test Test "));

        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.UNDERLINE);
        Font g = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.NORMAL);
        Font h = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.NORMAL);
        document.add(new Paragraph("Struk ", f));
        document.add(new Paragraph(nameCompany, g));
        document.add(new Paragraph(" ", h));
        document.add(table);

        //Step 5: Close the document
        document.close();
        Log.e("List: ", posOutputViewModelList.toString());

        Toast.makeText(this, "Pdf Generate!", Toast.LENGTH_SHORT).show();

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

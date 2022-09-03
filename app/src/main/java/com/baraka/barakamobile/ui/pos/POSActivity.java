package com.baraka.barakamobile.ui.pos;


import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.usermanaje.WorkerLogListActivity;
import com.baraka.barakamobile.ui.util.DbConfig;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

// ============================================= //

import android.content.ActivityNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

import androidx.core.content.FileProvider;

import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    private String urlCompProDetail = DbConfig.URL_COMP + "idComp.php";
    private String URL_COMP_IMG_DETAIL = DbConfig.URL_COMP + "imgComp/";
    private String URL_PRDCT_IMG = DbConfig.URL_PRDCT + "imgPrdct/";

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

    public static final String ID_COMP = "idComp";
    public static final String NAME_COMP = "nameComp";
    public static final String CODE_COMP = "codeComp";
    public static final String CITY_COMP = "cityComp";
    public static final String ADDR_COMP = "addrComp";
    public static final String PHONE_COMP = "phoneComp";
    public static final String EMAIL_COMP = "emailComp";
    public static final String LOGO_COMP = "logoComp";

    String id, email, name, level, access, idCompany, nameCompany;
    String idComp, nameComp, codeComp, cityComp, addrComp, phoneComp, emailComp, logoComp;
    TextView txtIdTx, txtNamaPrdct,txtPricePrdct,txtUnitPrdct,txtIdPrdct,txtStockPrdct, totalPrdctIn;
    TextView txtIdPrdctOut, txtPricePrdctOut, txtQtyOut, txtTotalOut;
    String totalPrdctPrice;
    EditText inputJumlah;
    TextView totalTxt;
    ImageView imgPosInput;

    POSOutputViewModel IdPrdct;
    POSOutputViewModel NamePrdct;
    POSOutputViewModel UnitPrice;
    POSOutputViewModel JmlhPrdct;
    POSOutputViewModel Total;

    private File pdfFile;
    String pdfname;
    Context context;

    FloatingActionButton btnCheckout;

    Calendar calender, calenderTTD;
    SimpleDateFormat simpledateformat, simpledateformatTTD;
    String date, dateTTD;
    ImageView imgCompHeader;
    Locale locale;

    ImageView imgCompHeaderTx;

    float valueTx, inputValTx, prdctPrice;

    private ArrayList<POSViewModel> posViewModelList;
//    private ArrayList<POSOutputViewModel> posOutputViewModelList;

    private List<POSOutputViewModel> posOutputViewModelList;


    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);

        locale = new Locale("id","ID");
        Locale.setDefault(locale);

        verifyStoragePermission(this);

        slidingUpPanelLayoutPos = (SlidingUpPanelLayout) findViewById(R.id.sliding_pos);
        slidingUpPanelLayoutPos.addPanelSlideListener(onSlideListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.point_of_sale);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_twotone_chevron_left_24);

        sharedPreferences = this.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

        id = sharedPreferences.getString(TAG_ID, id);
        level = sharedPreferences.getString(TAG_LEVEL, level);
        idComp = sharedPreferences.getString(ID_COMP, idComp);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, idCompany);
        cityComp = sharedPreferences.getString(CITY_COMP, cityComp);
        addrComp = sharedPreferences.getString(ADDR_COMP, addrComp);
        codeComp = sharedPreferences.getString(CODE_COMP, codeComp);

        id = sharedPreferences.getString(TAG_ID, null);
        email = sharedPreferences.getString(TAG_EMAIL, null);
        name = sharedPreferences.getString(TAG_NAME, null);
        level = sharedPreferences.getString(TAG_LEVEL, null);
        access = sharedPreferences.getString(TAG_ACCESS, null);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);
        nameCompany = sharedPreferences.getString(TAG_COMP, null);

        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewPOS);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(posCardAdapter);
        recyclerView.setHasFixedSize(true);
        posViewModelList = new ArrayList<>();

        calender = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("EEE, dd-MM-yyyy HH:mm:ss");
        date = simpledateformat.format(calender.getTime());

        calenderTTD = Calendar.getInstance();
        simpledateformatTTD = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        dateTTD = simpledateformatTTD.format(calenderTTD.getTime());

        posCardAdapter = new POSCardAdapter(posViewModelList, this);

        totalTxt = (TextView) findViewById(R.id.textViewPosTotal);

        txtIdPrdctOut = (TextView) findViewById(R.id.txtIdPrdctPosOutput);
        txtPricePrdctOut = (TextView) findViewById(R.id.txtViewPricePrdctPosOutput);
        txtQtyOut = (TextView) findViewById(R.id.txtViewJmlhPrdctPosOutput);
        txtTotalOut = (TextView) findViewById(R.id.txtViewPricePrdctPosOutputTotal);

        imgCompHeaderTx = (ImageView) findViewById(R.id.imgCompHeaderTx);

        //Fetch Data Produk
        getPrdct();
        detailComp();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshPOS);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItem();
            }

            private void refreshItem() {
                //Fetch Data Produk
                getPrdct();
                detailComp();
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
                    imgPosInput = dialogPosInput.findViewById(R.id.imgPOSInput);


                    txtIdPrdct.setText(String.valueOf(posViewModelList.get(position).getIdPrdct()));

                    txtNamaPrdct.setText(posViewModelList.get(position).getNamePrdct());

                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("In","ID"));
                    double formatRpPOSInput = Double.parseDouble(posViewModelList.get(position).getUnitPrice());
                    txtPricePrdct.setText(formatRupiah.format(formatRpPOSInput));

                    txtUnitPrdct.setText(posViewModelList.get(position).getUnitPrdct());

                    txtStockPrdct.setText(posViewModelList.get(position).getStockPrdct());

                    Picasso.get().load(URL_PRDCT_IMG+posViewModelList.get(position).getImgPrdct())
                            .fit()
                            .centerCrop()
                            .placeholder(R.drawable.default_image_small)
                            .error(R.drawable.default_image_small)
                            .into(imgPosInput);


                    Button btnTambahPos = dialogPosInput.findViewById(R.id.btnTambahPosInput);
                    btnTambahPos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            posOutputViewModelList.clear();

                            if (TextUtils.isEmpty(inputJumlah.getText().toString())){
                                inputJumlah.setError("Harap Masukan Jumlah Barang!");
                                inputJumlah.requestFocus();
                            }else{

                                int price = Integer.parseInt(posViewModelList.get(position).getUnitPrice());
                                int input = Integer.parseInt(inputJumlah.getText().toString());

                                int currTotal = price*input;
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
                                                        Toast.makeText(POSActivity.this, "Berhasil Menambah Data Belanja", Toast.LENGTH_SHORT).show();
//                                                        new android.app.AlertDialog.Builder(POSActivity.this)
//                                                                .setMessage("Penambahan Data Belanja Berhasil!")
//                                                                .setCancelable(false)
//                                                                .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
//                                                                    @Override
//                                                                    public void onClick(DialogInterface dialog, int which) {
//

//                                                                        Context context = POSActivity.this;
//                                                                        POSActivity.this.setResult(RESULT_OK);
//                                                                        android.app.AlertDialog optionDialog = new android.app.AlertDialog.Builder(POSActivity.this).create();
//                                                                        optionDialog.dismiss();
//                                                                        Toast.makeText(context, "Berhasil Menambah Data Belanja", Toast.LENGTH_SHORT).show();
//                                                                    }
//                                                                })
//                                                                .show();
                                                    } else {
//
//                                                        new android.app.AlertDialog.Builder(POSActivity.this)
//                                                                .setMessage("Penambahan Data Belanja Gagal!")
//                                                                .setCancelable(false)
//                                                                .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
//                                                                    @Override
//                                                                    public void onClick(DialogInterface dialog, int which) {
//                                                                        Context c = POSActivity.this;
//                                                                        android.app.AlertDialog optionDialog = new android.app.AlertDialog.Builder(POSActivity.this).create();
//                                                                        optionDialog.dismiss();
//                                                                    }
//                                                                })
//                                                                .show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onError(ANError anError) {
//                                                Toast.makeText(POSActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                                                Log.i("ERROR", "error => " + anError.toString());
                                                progressDialog.dismiss();
                                            }
                                        });

//                                Log.i("POS Out => ", "Items: "+IdPrdct.getIdPrdct()+
//                                        ", "+NamePrdct.getNamePrdct()+
//                                        ", "+UnitPrice.getUnitPrice()+
//                                        ", "+Total.getTotal()+
//                                        ", "+JmlhPrdct.getQtyPrdct().toString());



                                int totalPrice = 0;
//                                totalPrice += currTotal;

//                                for (int i = 0; i<posOutputViewModelList.size(); i++)
//                                {
//
//
//                                        int post = Integer.parseInt(posOutputViewModelList.get(i).getTotal());
//                                        totalPrice += post;
//
//                                        Toast.makeText(POSActivity.this, "Total: " + totalPrice, Toast.LENGTH_SHORT).show();
//
//                                        Log.i("i", "= " + i);
//                                        Log.i("posOutputViewModelList ", "= " + post);
//                                        Log.i("size", "= " + posOutputViewModelList.size());
//
//
//
//
//                                        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("In","ID"));
//                                        double formatRpPOSTotal = Double.parseDouble(String.valueOf(totalPrice));
//                                        totalTxt.setText(formatRupiah.format(formatRpPOSTotal));
//
//                                        posOutputAdapter.notifyDataSetChanged();
//
//                                }


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

        btnCheckout = (FloatingActionButton) findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
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

                    Log.i("Id Product",idPrdct.toString());
                    Log.i("Nama Produk",namaProduct.toString());
                }

                addTx(idPrdct);
                try {
                    createPdf();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void verifyStoragePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE);
        if(SDK_INT >= Build.VERSION_CODES.R){
            if(!Environment.isExternalStorageManager() && permission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(
                        activity,
                        PERMISION_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );

                Intent intent = new Intent();
                intent.setAction(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }
    }

    // Access pdf from storage and using to Intent get options to view application in available applications.
    private void openPDF(String pdfname) {

        // Get the File location and file name.
        File file = new File(Environment.getExternalStorageDirectory(), "POSBaraka/"+pdfname);
        Log.d("pdfFIle", "" + file);

        // Get the URI Path of file.
        Uri uriPdfPath = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
        Log.d("pdfPath", "" + uriPdfPath);

        // Start Intent to View PDF from the Installed Applications.
        Intent pdfOpenIntent = new Intent(Intent.ACTION_VIEW);
        pdfOpenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenIntent.setClipData(ClipData.newRawUri("", uriPdfPath));
        pdfOpenIntent.setDataAndType(uriPdfPath, "application/pdf");
        pdfOpenIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |  Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        try {
            startActivity(pdfOpenIntent);
        } catch (ActivityNotFoundException activityNotFoundException) {
            Toast.makeText(this,"There is no app to load corresponding PDF",Toast.LENGTH_LONG).show();

        }
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
                            int totalPrice = 0;
                            //                                totalTxt.setText(String.valueOf(totalPrice));
                            for (int i = 0; i<posOutputViewModelList.size(); i++)
                            {
                                int post = Integer.parseInt(posOutputViewModelList.get(i).getTotal());
                                totalPrice += post;
                                posOutputAdapter.notifyDataSetChanged();
                            }
                            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("In","ID"));
                            double formatRpPOSTotal = Double.parseDouble(String.valueOf(totalPrice));
                            totalTxt.setText(formatRupiah.format(formatRpPOSTotal));

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
                                Toast.makeText(context, "Menambah Keranjang", Toast.LENGTH_SHORT).show();
                            }else{

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
//                        Toast.makeText(POSActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        Log.i("Input", "Data: "+idCompany+", "+ finalIdPrdct.toString());
                        progressDialog.dismiss();
                    }
                });

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
                            int status = response.getInt("code");
                            if (status == 1) {

                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    POSViewModel posViewModel = new POSViewModel(

                                            jsonObject.getInt("idProduct"),
                                            jsonObject.getString("nameProduct"),
                                            jsonObject.getString("price"),
                                            jsonObject.getString("unit"),
                                            jsonObject.getString("stock"),
                                            jsonObject.getString("imageProduct")

                                    );
//                                creating adapter object and setting it to recyclerview

                                    posViewModelList.add(posViewModel);
                                    POSCardAdapter posCardAdapter = new POSCardAdapter(posViewModelList, POSActivity.this);
                                    recyclerView.setAdapter(posCardAdapter);
                                    progressDialog.dismiss();
                                }
                            }
                            if (status == 0) {
                                Toast.makeText(POSActivity.this, "Data Produk Tidak Ada!", Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(POSActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    private void createPdf() throws IOException, DocumentException {
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/POSBaraka");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i("Print: ", "Created a new directory for PDF");
        }

        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        pdfname = "Laporan_TX_" + nameCompany + "_" + timeStamp + ".pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);

        OutputStream output = new FileOutputStream(pdfFile);

//        BaseFont baseFont = BaseFont.createFont("res/font/ubuntu_mono.ttf", "UTF-8", BaseFont.EMBEDDED);
//        Font urFontName = new Font(baseFont, 12);
        Document document = new Document(PageSize.A4);

        Bitmap bmp = ((BitmapDrawable)imgCompHeaderTx.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        Image image = Image.getInstance(stream.toByteArray());
        image.scaleToFit(60, 60);
        image.setWidthPercentage(100);
        image.setAlignment(Element.ALIGN_LEFT);


        Font o = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.BOLD);
        Font z = new Font(Font.FontFamily.TIMES_ROMAN, 14.0f, Font.NORMAL);

        Paragraph header = new Paragraph(nameCompany,o);
        header.add(new Paragraph("\n"+addrComp,z));

        PdfPTable tableHeader = new PdfPTable(new float[]{0.5f, 3});
        tableHeader.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableHeader.getDefaultCell().setFixedHeight(50);
        tableHeader.setTotalWidth(PageSize.A4.getWidth());
        tableHeader.setWidthPercentage(100);
        tableHeader.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableHeader.addCell(image);
        tableHeader.addCell(header);

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
        PdfPCell[] cellsHeader = tableHeader.getRow(0).getCells();

        Paragraph tglTTD = new Paragraph(cityComp+", "+dateTTD);
        tglTTD.setAlignment(Element.ALIGN_RIGHT);

        Paragraph userTTD = new Paragraph(name);
        userTTD.setAlignment(Element.ALIGN_RIGHT);
        userTTD.setIndentationRight(60);
        userTTD.setSpacingBefore(60);

        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.WHITE);
        }

        for (int j = 0; j < cellsHeader.length; j++) {
            cellsHeader[j].setBorder(Rectangle.NO_BORDER);
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
        document.add(tableHeader);

        LineSeparator ls = new LineSeparator();
        //Step 4 Add content
//        document.add((Element) new Paragraph("Test"));
//        document.add((Element) new Paragraph("Test Test Test Test Test Test Test Test "));

        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 14.0f, Font.UNDERLINE);
        Font g = new Font(Font.FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL);
        Font h = new Font(Font.FontFamily.TIMES_ROMAN, 5.0f, Font.NORMAL);
        document.add(new Paragraph(" ", h));
        document.add(new Chunk(ls));
        ls.setOffset(5);
        document.add(new Paragraph("Laporan Transaksi ", f));
        document.add(new Paragraph(" ", g));
        document.add(table);
        document.add(tglTTD);
        document.add(userTTD);

        //Step 5: Close the document
        document.close();

        Toast.makeText(this, "Pdf Generate!", Toast.LENGTH_SHORT).show();

        openPDF(pdfname);
    }

    // Detail CompPro
    public void detailComp(){

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
                                jsonObject.getString("logoComp");

                                Picasso.get().load(URL_COMP_IMG_DETAIL+jsonObject.getString("logoComp"))
                                        .resize(30, 30)
                                        .centerCrop()
                                        .into(imgCompHeaderTx);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("ERROR","error => "+ anError.toString());

                    }
                });
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return  permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(POSActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
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

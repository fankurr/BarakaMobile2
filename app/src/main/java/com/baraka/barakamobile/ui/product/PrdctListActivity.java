package com.baraka.barakamobile.ui.product;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.util.DbConfig;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PrdctListActivity extends AppCompatActivity implements PrdctCardAdapter.OnItemClickListener {
    private ProductViewModel productViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PrdctCardAdapter prdctCardAdapter;
    private CateCardAdpater cateCardAdpater;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String URL_PRDCT = DbConfig.URL_PRDCT + "allPrdct.php";
    private String urlCompProDetail = DbConfig.URL_COMP + "idComp.php";
    private String URL_COMP_IMG_DETAIL = DbConfig.URL_COMP + "imgComp/";

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

    public static final String ID_SPLR = "idSplr";
    public static final String COMP_SPLR= "idCompSplr";
    public static final String NAME_SPLR= "nameSplr";
    public static final String ADDR_SPLR = "addrSplr";
    public static final String PHONE_SPLR = "phoneSplr";
    public static final String EMAIL_SPLR = "emailSplr";
    public static final String DESC_SPLR = "descSplr";
    public static final String IMG_SPLR = "imgSplr";

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
    String idLog, nameUser, datetimeLogin, datetimeLogout;
    String pdfname;

    private File pdfFile;
    Context context;

    Calendar calender, calenderTTD;
    SimpleDateFormat simpledateformat, simpledateformatTTD;
    String date, dateTTD;
    ImageView imgCompHeader;
    Locale locale;

    private List<PrdctViewModel> prdctViewModelList;

    PrdctViewModel codePrdct;
    PrdctViewModel namePrdct;
    PrdctViewModel catPrdct;
    PrdctViewModel nameSplrPrdct;
    PrdctViewModel stockPrdct;
    PrdctViewModel unitPrdct;

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
        setContentView(R.layout.activity_prdct_list);

        locale = new Locale("id","ID");
        Locale.setDefault(locale);

        verifyStoragePermission(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Semua Produk");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_twotone_chevron_left_24);

        calenderTTD = Calendar.getInstance();
        simpledateformatTTD = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        dateTTD = simpledateformatTTD.format(calenderTTD.getTime());


        ImageButton imgBtnAddPrdct = (ImageButton) findViewById(R.id.imgBtnAddPrdct);
        imgBtnAddPrdct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrdctListActivity.this, AddEditPrdctActivity.class);
                startActivity(intent);
            }
        });

        sharedPreferences = this.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedPreferences.getString(TAG_ID, null);
        email = sharedPreferences.getString(TAG_EMAIL, null);
        name = sharedPreferences.getString(TAG_NAME, null);
        level = sharedPreferences.getString(TAG_LEVEL, null);
        access = sharedPreferences.getString(TAG_ACCESS, null);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);
        nameCompany = sharedPreferences.getString(TAG_COMP, null);

        id = sharedPreferences.getString(TAG_ID, id);
        level = sharedPreferences.getString(TAG_LEVEL, level);
        idComp = sharedPreferences.getString(ID_COMP, idComp);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, idCompany);
        cityComp = sharedPreferences.getString(CITY_COMP, cityComp);
        addrComp = sharedPreferences.getString(ADDR_COMP, addrComp);
        codeComp = sharedPreferences.getString(CODE_COMP, codeComp);

        imgCompHeader = findViewById(R.id.imgCompHeaderPrdct);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewPrdctList);
        recyclerView.setLayoutManager(new GridLayoutManager(PrdctListActivity.this, 2));
        recyclerView.setHasFixedSize(true);
        prdctViewModelList = new ArrayList<>();

        prdctCardAdapter = new PrdctCardAdapter(prdctViewModelList, PrdctListActivity.this);
        recyclerView.setAdapter(prdctCardAdapter);

        //Fetch Data Produk
        getProduct();
        detailComp();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshPrdctList);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshItem();

            }

            private void refreshItem() {

                //Fetch Data Produk
                getProduct();
                detailComp();

                onItemLoad();

            }

            private void onItemLoad() {

                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    private void getProduct() {
        ProgressDialog progressDialog = new ProgressDialog(PrdctListActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data Produk");
        progressDialog.show();

        sharedPreferences = this.getSharedPreferences(my_shared_preferences,Context.MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        AndroidNetworking.post(URL_PRDCT)
                .addBodyParameter("idCompPrdct", idCompany)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        prdctViewModelList.clear();
                        try {
                            int status = response.getInt("code");
                            if (status == 1) {

                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    PrdctViewModel prdctViewModel = new PrdctViewModel(

                                            jsonObject.getString("idProduct"),
                                            jsonObject.getString("nameProduct"),
                                            jsonObject.getString("codeProduct"),
                                            jsonObject.getString("supplierProduct"),
                                            jsonObject.getString("price"),
                                            jsonObject.getString("unit"),
                                            jsonObject.getString("stock"),
                                            jsonObject.getString("lastUpdate"),
                                            jsonObject.getString("updateBy"),
                                            jsonObject.getString("description"),
                                            jsonObject.getString("imageProduct"),
                                            jsonObject.getString("idCategory"),
                                            jsonObject.getString("idCompCategory"),
                                            jsonObject.getString("nameCategory"),
                                            jsonObject.getString("descCategory"),
                                            jsonObject.getString("imageCategory"),
                                            jsonObject.getString("idSupplier"),
                                            jsonObject.getString("idCompSupplier"),
                                            jsonObject.getString("nameSupplier"),
                                            jsonObject.getString("addrSupplier"),
                                            jsonObject.getString("phoneSupplier"),
                                            jsonObject.getString("emailSupplier"),
                                            jsonObject.getString("descSupplier"),
                                            jsonObject.getString("imgSupplier")

                                    );
                                    prdctViewModelList.add(prdctViewModel);
                                    PrdctCardAdapter prdctCardAdapter = new PrdctCardAdapter(prdctViewModelList,PrdctListActivity.this);
                                    recyclerView.setAdapter(prdctCardAdapter);
                                    prdctCardAdapter.setOnItemClickListener(PrdctListActivity.this);
                                    progressDialog.dismiss();
                                }
                            }
                            if (status == 0) {
                                Toast.makeText(PrdctListActivity.this, "Data Produk Tidak Ada!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        prdctCardAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(PrdctListActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();
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

    private void createPdf() throws IOException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/POSBaraka");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i("Print: ", "Created a new directory for PDF");
        }

        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        pdfname = "LaporanStokBarang_" + nameCompany + "_" + timeStamp + ".pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);

//        pdfFile = new File(pdfFolder + "_LaporanLoginKaryawan_" + nameCompany + "_" + timeStamp + ".pdf");

        OutputStream output = new FileOutputStream(pdfFile);

        Document document = new Document(PageSize.A4);

        Bitmap bmp = ((BitmapDrawable)imgCompHeader.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        Image image = Image.getInstance(stream.toByteArray());
        image.scaleToFit(60, 60);
        image.setWidthPercentage(100);
        image.setAlignment(Element.ALIGN_LEFT);

        PdfPTable table = new PdfPTable(new float[]{1, 3, 3, 3, 3});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(50);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("No.");
        table.addCell("Kode Barang");
        table.addCell("Nama Barang");
        table.addCell("Kategori");
        table.addCell("Stok");
        table.setHeaderRows(1);

        Font o = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.BOLD);
        Font z = new Font(Font.FontFamily.TIMES_ROMAN, 14.0f, Font.NORMAL);

        Paragraph header = new Paragraph(nameCompany,o);
        header.add(new Paragraph("\n"+addrComp,z));


        PdfPTable tableHeader = new PdfPTable(new float[]{0.5f, 3});
        tableHeader.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableHeader.getDefaultCell().setFixedHeight(60);
        tableHeader.setTotalWidth(PageSize.A4.getWidth());
        tableHeader.setWidthPercentage(100);
        tableHeader.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableHeader.addCell(image);
        tableHeader.addCell(header);


        PdfPCell[] cells = table.getRow(0).getCells();
        PdfPCell[] cellsHeader = tableHeader.getRow(0).getCells();

        Paragraph tglTTD = new Paragraph(cityComp+", "+dateTTD);
        tglTTD.setAlignment(Element.ALIGN_RIGHT);

        Paragraph userTTD = new Paragraph(name);
        userTTD.setAlignment(Element.ALIGN_RIGHT);
        userTTD.setIndentationRight(60);
        userTTD.setSpacingBefore(60);

        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(new BaseColor(100, 85, 161));
        }

        for (int j = 0; j < cellsHeader.length; j++) {
            cellsHeader[j].setBorder(Rectangle.NO_BORDER);
        }

        for (int i = 0; i < prdctViewModelList.size(); i++) {
            codePrdct = prdctViewModelList.get(i);
            namePrdct = prdctViewModelList.get(i);
            catPrdct = prdctViewModelList.get(i);
            stockPrdct = prdctViewModelList.get(i);
            unitPrdct = prdctViewModelList.get(i);
            String codePrdctL = codePrdct.getCodePrdct();
            String namePrdctL = namePrdct.getNamePrdct();
            String catPrdctL = catPrdct.getNameCat();
            String stokPrdctL = stockPrdct.getStockPrdct();
            String unitPrdctL = unitPrdct.getUnitPrdct();
            table.addCell(String.valueOf(i+1));
            table.addCell(String.valueOf(codePrdctL));
            table.addCell(String.valueOf(namePrdctL));
            table.addCell(String.valueOf(catPrdctL));
            table.addCell(String.valueOf(stokPrdctL+" "+unitPrdctL));
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
        document.add(new Paragraph("Laporan Stok Barang ", f));
        document.add(new Paragraph(" ", g));
        document.add(table);
        document.add(tglTTD);
        document.add(userTTD);

        //Step 5: Close the document
        document.close();
        Log.e("List: ", prdctViewModelList.toString());

        Toast.makeText(this, "Pdf Generate!", Toast.LENGTH_SHORT).show();

        checkPermission();
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
                                        .into(imgCompHeader);


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

    @Override
    public void onItemClick(int position) {
        Intent prdctDetailIntent = new Intent(PrdctListActivity.this, PrdctDetail.class);
        PrdctViewModel clickedPrdct = prdctViewModelList.get(position);

        prdctDetailIntent.putExtra(ID_PRDCT, clickedPrdct.getIdPrdct());
        prdctDetailIntent.putExtra(NAME_PRDCT, clickedPrdct.getNamePrdct());
        prdctDetailIntent.putExtra(CODE_PRDCT, clickedPrdct.getCodePrdct());
        prdctDetailIntent.putExtra(DESC_PRDCT, clickedPrdct.getDescPrdct());
        prdctDetailIntent.putExtra(PRICE_PRDCT, clickedPrdct.getUnitPrice());
        prdctDetailIntent.putExtra(UNIT_PRDCT, clickedPrdct.getUnitPrdct());
        prdctDetailIntent.putExtra(STOCK_PRDCT, clickedPrdct.getStockPrdct());
        prdctDetailIntent.putExtra(ID_CATE, clickedPrdct.getIdCat());
        prdctDetailIntent.putExtra(CATE_PRDCT, clickedPrdct.getNameCat());
        prdctDetailIntent.putExtra(SPLR_PRDCT, clickedPrdct.getNameSplr());

        prdctDetailIntent.putExtra(ID_SPLR, clickedPrdct.getIdSplr());
        prdctDetailIntent.putExtra(NAME_SPLR, clickedPrdct.getNameSplr());
        prdctDetailIntent.putExtra(DESC_SPLR, clickedPrdct.getDescSplr());
        prdctDetailIntent.putExtra(ADDR_SPLR, clickedPrdct.getAddrSplr());
        prdctDetailIntent.putExtra(PHONE_SPLR, clickedPrdct.getPhoneSplr());
        prdctDetailIntent.putExtra(EMAIL_SPLR, clickedPrdct.getEmailSplr());
        prdctDetailIntent.putExtra(IMG_SPLR, clickedPrdct.getImgSplr());

        startActivity(prdctDetailIntent);
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return  permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(PrdctListActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_worker_log, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.printLog:
                try {
                    createPdf();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
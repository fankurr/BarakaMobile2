package com.baraka.barakamobile.ui.usermanaje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION;

public class WorkerLogListActivity extends AppCompatActivity {

    private List<WorkerLogViewModel> workerLogViewModelList =  new ArrayList<>();
    WorkerLogListAdapter workerLogListAdapter;
    ListView listViewWorkerLog;
    private SwipeRefreshLayout swipeRefreshLayout;

    public final static String TAG_ID = "id";
    public final static String TAG_EMAIL = "email";
    public final static String TAG_NAME = "name";
    public final static String TAG_ADDR = "address";
    public final static String TAG_LEVEL = "level";
    public final static String TAG_PHONE = "phone";
    public final static String TAG_ACCESS = "access";
    public final static String TAG_IDCOMP = "idCompany";
    public final static String TAG_COMP = "nameCompany";

    public static final String ID_COMP = "idComp";
    public static final String NAME_COMP = "nameComp";
    public static final String CODE_COMP = "codeComp";
    public static final String CITY_COMP = "cityComp";
    public static final String ADDR_COMP = "addrComp";
    public static final String PHONE_COMP = "phoneComp";
    public static final String EMAIL_COMP = "emailComp";
    public static final String LOGO_COMP = "logoComp";

    String id, email, name, level, access, idCompany, nameCompany;
    String idLog, nameUser, datetimeLogin, datetimeLogout;
    String idComp, nameComp, codeComp, cityComp, addrComp, phoneComp, emailComp, logoComp;

    WorkerLogViewModel NameUser;
    WorkerLogViewModel EmailUser;
    WorkerLogViewModel DateTimeLogin;
    WorkerLogViewModel DateTimeLogout;

    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";


    private String URL_WORKER_LOG = DbConfig.URL_LOG + "allLog.php";
    private String urlCompProDetail = DbConfig.URL_COMP + "idComp.php";
    private String URL_COMP_IMG_DETAIL = DbConfig.URL_COMP + "imgComp/";

    private File pdfFile;
    String pdfname;
    Context context;

    Calendar calender, calenderTTD;
    SimpleDateFormat simpledateformat, simpledateformatTTD;
    String date, dateTTD;
    ImageView imgCompHeader;
    Locale locale;

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
        setContentView(R.layout.activity_worker_log_list);

        locale = new Locale("id","ID");
        Locale.setDefault(locale);

        verifyStoragePermission(this);


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



        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Log");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_twotone_chevron_left_24);

        imgCompHeader = findViewById(R.id.imgCompHeader);

        calenderTTD = Calendar.getInstance();
        simpledateformatTTD = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        dateTTD = simpledateformatTTD.format(calenderTTD.getTime());

        listViewWorkerLog = (ListView) findViewById(R.id.listViewWorkerLogList);
        workerLogListAdapter = new WorkerLogListAdapter(WorkerLogListActivity.this, workerLogViewModelList);
        listViewWorkerLog.setAdapter(workerLogListAdapter);

        // GET Worker Log JSON
        getWorkerLog();
        detailComp();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshWorkerLog);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshItem();

            }

            private void refreshItem() {

                // GET Worker Log JSON
                getWorkerLog();
                detailComp();
                onItemLoad();

            }

            private void onItemLoad() {

                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    public void getWorkerLog(){
        ProgressDialog progressDialog = new ProgressDialog(WorkerLogListActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data Log Karyawan");
        progressDialog.show();

//        sharedPreferences = this.getActivity().getSharedPreferences(my_shared_preferences,MODE_PRIVATE);

        AndroidNetworking.post(URL_WORKER_LOG)
                .addBodyParameter("idCompLogin", idCompany)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        workerLogViewModelList.clear();
                        Log.i("Info", "Data: " + response.toString());

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                WorkerLogViewModel workerLogViewModel = new WorkerLogViewModel(
                                        jsonObject.getString("idLogin"),
                                        jsonObject.getString("idCompany"),
                                        jsonObject.getString("idUser"),
                                        jsonObject.getString("nameUser"),
                                        jsonObject.getString("emailUser"),
                                        jsonObject.getString("login"),
                                        jsonObject.getString("logout")
                                );
                                workerLogViewModelList.add(workerLogViewModel);
                                WorkerLogListAdapter workerLogListAdapter = new WorkerLogListAdapter(WorkerLogListActivity.this, workerLogViewModelList);
                                listViewWorkerLog.setAdapter(workerLogListAdapter);
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        workerLogListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(WorkerLogListActivity.this, "Koneksi Gagal: " + anError.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
//                        Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
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

        pdfname = "LaporanLoginKaryawan_" + nameCompany + "_" + timeStamp + ".pdf";
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
        table.addCell("Nama");
        table.addCell("Email");
        table.addCell("Login");
        table.addCell("Logout");
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
            cells[j].setBackgroundColor(new BaseColor(17, 205, 239));
        }

        for (int j = 0; j < cellsHeader.length; j++) {
            cellsHeader[j].setBorder(Rectangle.NO_BORDER);
        }

        for (int i = 0; i < workerLogViewModelList.size(); i++) {
            NameUser = workerLogViewModelList.get(i);
            EmailUser = workerLogViewModelList.get(i);
            DateTimeLogin = workerLogViewModelList.get(i);
            DateTimeLogout = workerLogViewModelList.get(i);
            String nameUserL = NameUser.getNameUser();
            String emailUserL = NameUser.getEmailUser();
            String datetimeLoginL = DateTimeLogin.getDatetimeLogin();
            String datetimeLogoutL = DateTimeLogout.getDatetimeLogout();
            table.addCell(String.valueOf(i+1));
            table.addCell(String.valueOf(nameUserL));
            table.addCell(String.valueOf(emailUserL));
            table.addCell(String.valueOf(datetimeLoginL));
            table.addCell(String.valueOf(datetimeLogoutL));
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
        document.add(new Paragraph("Laporan Login Karyawan ", f));
        document.add(new Paragraph(" ", g));
        document.add(table);
        document.add(tglTTD);
        document.add(userTTD);

        //Step 5: Close the document
        document.close();
        Log.e("List: ", workerLogViewModelList.toString());

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

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return  permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(WorkerLogListActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
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
package com.baraka.barakamobile.ui.usermanaje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.util.DbConfig;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

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

    String id, email, name, level, access, idCompany, nameCompany;
    String idLog, nameUser, datetimeLogin, datetimeLogout;

    WorkerLogViewModel NameUser;
    WorkerLogViewModel EmailUser;
    WorkerLogViewModel DateTimeLogin;
    WorkerLogViewModel DateTimeLogout;

    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;

    private String URL_WORKER_LOG = DbConfig.URL_LOG + "allLog.php";

    private File pdfFile;
    String pdfname;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_log_list);
        requestPermission();

        sharedPreferences = this.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
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

        listViewWorkerLog = (ListView) findViewById(R.id.listViewWorkerLogList);
        workerLogListAdapter = new WorkerLogListAdapter(WorkerLogListActivity.this, workerLogViewModelList);
        listViewWorkerLog.setAdapter(workerLogListAdapter);

        // GET Worker Log JSON
        getWorkerLog();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshWorkerLog);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshItem();

            }

            private void refreshItem() {

                // GET Worker Log JSON
                getWorkerLog();

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

    private void createPdf() throws FileNotFoundException, DocumentException {

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

        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(new BaseColor(17, 205, 239));
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

        //Step 4 Add content
//        document.add((Element) new Paragraph("Test"));
//        document.add((Element) new Paragraph("Test Test Test Test Test Test Test Test "));

        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.UNDERLINE);
        Font g = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.NORMAL);
        Font h = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.NORMAL);
        document.add(new Paragraph("Laporan Login Karyawan ", f));
        document.add(new Paragraph(nameCompany, g));
        document.add(new Paragraph(" ", h));
        document.add(table);

        //Step 5: Close the document
        document.close();
        Log.e("List: ", workerLogViewModelList.toString());

        Toast.makeText(this, "Pdf Generate!", Toast.LENGTH_SHORT).show();

        checkPermission();
        previewPdf();
    }

    private void previewPdf() {
//        PackageManager packageManager = getPackageManager();
//        Intent testIntent = new Intent(Intent.ACTION_VIEW);
//        testIntent.setType("application/pdf");
//        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
//        if (list.size() > 0) {
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_VIEW);
//            Uri uri = Uri.fromFile(pdfFile);
//            intent.setDataAndType(uri, "application/pdf");
//            startActivity(intent);
//        } else {
//            Toast.makeText(this, "Download a PDF Viewer to see the generated PDF", Toast.LENGTH_SHORT).show();
//        }
//
//        try {
//            File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + dir + "/" + file);
//            Uri path = Uri.fromFile(pdfFile);
//
//            // Setting the intent for pdf reader
//            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
//            pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            pdfIntent.setDataAndType(path, "application/pdf");
//            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            startActivity(pdfIntent);
//        } catch (ActivityNotFoundException e) {
//            Toast.makeText(this, "Can't read pdf file", Toast.LENGTH_SHORT).show();
//        }

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
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
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
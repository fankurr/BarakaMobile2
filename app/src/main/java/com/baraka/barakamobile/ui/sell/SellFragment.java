package com.baraka.barakamobile.ui.sell;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.databinding.FragmentSellBinding;
import com.baraka.barakamobile.ui.supplier.SplrCardAdapter;
import com.baraka.barakamobile.ui.supplier.SplrViewModel;
import com.baraka.barakamobile.ui.usermanaje.WorkerLogListActivity;
import com.baraka.barakamobile.ui.usermanaje.WorkerLogViewModel;
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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION.SDK_INT;
import static android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION;

public class SellFragment extends Fragment {

    private String URL_SELL = DbConfig.URL_SELL + "allTx.php";
    private String URL_SELL_MONTH = DbConfig.URL_SELL + "monthTx.php";
    private String urlCompProDetail = DbConfig.URL_COMP + "idComp.php";
    private String URL_COMP_IMG_DETAIL = DbConfig.URL_COMP + "imgComp/";

    private SellViewModel sellViewModel;
    private FragmentSellBinding binding;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static final String ID_TX = "idTx";
    public static final String COMP_TX= "idCompTx";
    public static final String PRDCT_TX= "idPrdctTx";
    public static final String QTY_TX = "qtyTx ";
    public static final String DATETIME_TX = "datetimeTx";

    public static final String ID_COMP = "idComp";
    public static final String NAME_COMP = "nameComp";
    public static final String CODE_COMP = "codeComp";
    public static final String CITY_COMP = "cityComp";
    public static final String ADDR_COMP = "addrComp";
    public static final String PHONE_COMP = "phoneComp";
    public static final String EMAIL_COMP = "emailComp";
    public static final String LOGO_COMP = "logoComp";

    private final static String TAG_ID = "id";
    private final static String TAG_EMAIL = "email";
    private final static String TAG_NAME = "name";
    private final static String TAG_LEVEL = "level";
    private final static String TAG_ACCESS = "access";
    private final static String TAG_IDCOMP = "idCompany";
    private final static String TAG_COMP = "nameCompany";

    String id, email, name, level, access, idCompany, nameCompany;
    String idComp, nameComp, codeComp, cityComp, addrComp, phoneComp, emailComp, logoComp;
    private TxCardAdapter txCardAdapter;

    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    private RecyclerView recyclerViewTx;
    private RecyclerView.LayoutManager layoutManagerTx;

    private List<TxViewModel> txViewModelList;

    TxViewModel NamePrdct;
    TxViewModel QtyTX;
    TxViewModel DateTimeTx;
    TxViewModel ValueTx;

    private File pdfFile;
    String pdfname;
    Context context;

    Calendar calender, calenderTTD;
    SimpleDateFormat simpledateformat, simpledateformatTTD;
    String date, dateTTD;
    ImageView imgHeader;
    Locale locale;

    ImageView imgCompHeaderSell;

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_sell, container, false);

        setHasOptionsMenu(true);
        verifyStoragePermission(getActivity());

        recyclerViewTx = (RecyclerView) view.findViewById(R.id.RecycleViewTx);
        recyclerViewTx.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewTx.setHasFixedSize(true);
        txViewModelList = new ArrayList<>();

        txCardAdapter = new TxCardAdapter(getContext(), txViewModelList);
        recyclerViewTx.setAdapter(txCardAdapter);

        sharedPreferences = this.getActivity().getSharedPreferences(my_shared_preferences,Context.MODE_PRIVATE);
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

        imgCompHeaderSell = (ImageView) view.findViewById(R.id.imgCompHeaderSell);

        calender = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("EEE, dd-MM-yyyy HH:mm:ss");
        date = simpledateformat.format(calender.getTime());

        calenderTTD = Calendar.getInstance();
        simpledateformatTTD = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        dateTTD = simpledateformatTTD.format(calenderTTD.getTime());

        //Fetch Data TX
        getTx();
        detailComp();

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshSell);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshItem();

            }

            private void refreshItem() {
                //Fetch Data TX
                getTx();
                detailComp();

                onItemLoad();

            }

            private void onItemLoad() {

                swipeRefreshLayout.setRefreshing(false);

            }
        });


        return view;
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
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
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
        Uri uriPdfPath = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", file);
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
            Toast.makeText(getContext(),"There is no app to load corresponding PDF",Toast.LENGTH_LONG).show();

        }
    }

    public void getTx(){
        ProgressDialog progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data..");
        progressDialog.show();

        sharedPreferences = this.getActivity().getSharedPreferences(my_shared_preferences,Context.MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        AndroidNetworking.post(URL_SELL)
                .addBodyParameter("idCompTx", idCompany)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        txViewModelList.clear();

                        Log.i("Info", "Data: " + response.toString());
                        try {
                            int status = response.getInt("success");
                            if (status == 1) {

                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    TxViewModel txViewModel = new TxViewModel(
                                            jsonObject.getInt("idTransaction"),
                                            jsonObject.getInt("idCompTransaction"),
                                            jsonObject.getString("CompTransaction"),
                                            jsonObject.getInt("idProduct"),
                                            jsonObject.getString("Product"),
                                            jsonObject.getString("qtyTransaction"),
                                            jsonObject.getString("unit"),
                                            jsonObject.getString("valueTx"),
                                            jsonObject.getString("dateTime"),
                                            jsonObject.getString("imageProduct")

                                    );
                                    txViewModelList.add(txViewModel);
                                    TxCardAdapter txCardAdapter = new TxCardAdapter(getContext(), txViewModelList);
                                    recyclerViewTx.setAdapter(txCardAdapter);
//                                txCardAdapter.setOnItemClickListener(SupplierFragment.this);
                                    progressDialog.dismiss();
                                }
                            }
                            if (status == 0) {
                                Toast.makeText(getContext(), "Data Penjualan Tidak Ada!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        txCardAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();
                    }
                });
    }


    public void getTxMonth() {
        ProgressDialog progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data..");
        progressDialog.show();

        sharedPreferences = this.getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        AndroidNetworking.post(URL_SELL_MONTH)
                .addBodyParameter("idCompTx", idCompany)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        txViewModelList.clear();

                        Log.i("Info", "Data: " + response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                TxViewModel txViewModel = new TxViewModel(
                                        jsonObject.getInt("idTransaction"),
                                        jsonObject.getInt("idCompTransaction"),
                                        jsonObject.getString("CompTransaction"),
                                        jsonObject.getInt("idProduct"),
                                        jsonObject.getString("Product"),
                                        jsonObject.getString("qtyTransaction"),
                                        jsonObject.getString("unit"),
                                        jsonObject.getString("valueTx"),
                                        jsonObject.getString("dateTime"),
                                        jsonObject.getString("imageProduct")

                                );
                                txViewModelList.add(txViewModel);
                                TxCardAdapter txCardAdapter = new TxCardAdapter(getContext(), txViewModelList);
                                recyclerViewTx.setAdapter(txCardAdapter);
//                                txCardAdapter.setOnItemClickListener(SupplierFragment.this);
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        txCardAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", "error => " + anError.toString());
                        progressDialog.dismiss();
                    }
                });
    }

    private void createPdf() throws IOException, DocumentException {
        requestPermission();

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/POSBaraka");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i("Print: ", "Created a new directory for PDF");
        }

        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        pdfname = "LaporanPenjualan_" + nameCompany + "_" + timeStamp + ".pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);

//        pdfFile = new File(pdfFolder + "_LaporanLoginKaryawan_" + nameCompany + "_" + timeStamp + ".pdf");

        OutputStream output = new FileOutputStream(pdfFile);

        Document document = new Document(PageSize.A4);
        Bitmap bmp = ((BitmapDrawable)imgCompHeaderSell.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        Image image = Image.getInstance(stream.toByteArray());
        image.scaleToFit(60, 60);
        image.setWidthPercentage(100);
        image.setAlignment(Element.ALIGN_LEFT);
//        document.add(image);

//        Font regularReport = new Font(baseFont, 30,Font.BOLD, BaseColor.BLACK);

        PdfPTable table = new PdfPTable(new float[]{1, 3, 3, 3, 3});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(50);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("No.");
        table.addCell("Nama Produk");
        table.addCell("Jumlah");
        table.addCell("Tanggal & Waktu");
        table.addCell("Harga");
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
            cells[j].setBackgroundColor(new BaseColor(0, 206, 137));
        }


        for (int j = 0; j < cellsHeader.length; j++) {
            cellsHeader[j].setBorder(Rectangle.NO_BORDER);
        }

        for (int i = 0; i < txViewModelList.size(); i++) {
            NamePrdct = txViewModelList.get(i);
            QtyTX = txViewModelList.get(i);
            DateTimeTx = txViewModelList.get(i);
            ValueTx = txViewModelList.get(i);
            String namePrdctL = NamePrdct.getNamePrdct();
            String qtyTxL = QtyTX.getQtyTx();
            String datetimeTxL = DateTimeTx.getDatetimeTx();
            String valueTxL = ValueTx.getValueTx();
            table.addCell(String.valueOf(i+1));
            table.addCell(String.valueOf(namePrdctL));
            table.addCell(String.valueOf(qtyTxL));
            table.addCell(String.valueOf(datetimeTxL));
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("In","ID"));
            double formatRpPrintSell = Double.parseDouble(String.valueOf(valueTxL));
            table.addCell(formatRupiah.format(formatRpPrintSell));
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
        document.add(new Paragraph("Laporan Penjualan ", f));
        document.add(new Paragraph(" ", g));
        document.add(table);
        document.add(tglTTD);
        document.add(userTTD);

        //Step 5: Close the document
        document.close();
        Log.e("List: ", txViewModelList.toString());

        Toast.makeText(getContext(), "Pdf Generate!", Toast.LENGTH_SHORT).show();

        checkPermission();
        openPDF(pdfname);
    }

    private void createPdfMonth() throws IOException, DocumentException {

        requestPermission();

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/POSBaraka");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i("Print: ", "Created a new directory for PDF");
        }

        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        pdfname = "LaporanPenjualan_" + nameCompany + "_" + timeStamp + ".pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);

//        pdfFile = new File(pdfFolder + "_LaporanLoginKaryawan_" + nameCompany + "_" + timeStamp + ".pdf");

        OutputStream output = new FileOutputStream(pdfFile);

        Document document = new Document(PageSize.A4);
        Bitmap bmp = ((BitmapDrawable)imgCompHeaderSell.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        Image image = Image.getInstance(stream.toByteArray());
        image.scaleToFit(60, 60);
        image.setWidthPercentage(100);
        image.setAlignment(Element.ALIGN_LEFT);
//        document.add(image);

//        Font regularReport = new Font(baseFont, 30,Font.BOLD, BaseColor.BLACK);

        PdfPTable table = new PdfPTable(new float[]{1, 3, 3, 3, 3});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(50);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("No.");
        table.addCell("Nama Produk");
        table.addCell("Jumlah");
        table.addCell("Tanggal & Waktu");
        table.addCell("Harga");
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
            cells[j].setBackgroundColor(new BaseColor(0, 206, 137));
        }


        for (int j = 0; j < cellsHeader.length; j++) {
            cellsHeader[j].setBorder(Rectangle.NO_BORDER);
        }

        for (int i = 0; i < txViewModelList.size(); i++) {
            NamePrdct = txViewModelList.get(i);
            QtyTX = txViewModelList.get(i);
            DateTimeTx = txViewModelList.get(i);
            ValueTx = txViewModelList.get(i);
            String namePrdctL = NamePrdct.getNamePrdct();
            String qtyTxL = QtyTX.getQtyTx();
            String datetimeTxL = DateTimeTx.getDatetimeTx();
            String valueTxL = ValueTx.getValueTx();
            table.addCell(String.valueOf(i+1));
            table.addCell(String.valueOf(namePrdctL));
            table.addCell(String.valueOf(qtyTxL));
            table.addCell(String.valueOf(datetimeTxL));
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("In","ID"));
            double formatRpPrintSell = Double.parseDouble(String.valueOf(valueTxL));
            table.addCell(formatRupiah.format(formatRpPrintSell));
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
        document.add(new Paragraph("Laporan Penjualan Bulanan ", f));
        document.add(new Paragraph(" ", g));
        document.add(table);
        document.add(tglTTD);
        document.add(userTTD);

        //Step 5: Close the document
        document.close();
        Log.e("List: ", txViewModelList.toString());

        Toast.makeText(getContext(), "Pdf Generate!", Toast.LENGTH_SHORT).show();

        checkPermission();
        openPDF(pdfname);
    }


    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), READ_EXTERNAL_STORAGE);
        return  permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
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
                    Toast.makeText(getContext(), "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Permission Denined.", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
        }
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
                                        .placeholder(R.drawable.default_image_comp_small)
                                        .error(R.drawable.default_image_comp_small)
                                        .into(imgCompHeaderSell);


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

    public void TraditionallistDialogSort() {
        // setup alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Urutkan");
        // buat array list
        String[] options = {"Bulan", "Semua"};
        //Pass array list di Alert dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // pilih opsi
                        getTxMonth();
                        break;
                    case 1:
                        getTx();
                        break;
                    default:
                }
            }
        });
        // buat dan tampilkan alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void TraditionallistDialogPrint() {
        // setup alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Print");
        // buat array list
        String[] options = {"Bulan", "Semua"};
        //Pass array list di Alert dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // pilih opsi
                        try {
                            createPdfMonth();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        try {
                            createPdf();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                }
            }
        });
        // buat dan tampilkan alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_print, menu);

    }

//    try {
//        createPdf();
//    } catch (IOException e) {
//        e.printStackTrace();
//    } catch (DocumentException e) {
//        e.printStackTrace();
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.printItem:
                TraditionallistDialogPrint();
                return true;
            case R.id.printISort:
                TraditionallistDialogSort();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
//        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
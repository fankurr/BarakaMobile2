package com.baraka.barakamobile.ui.payout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.profile.CompProActivity;
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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION.SDK_INT;
import static android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION;

public class PayoutFragment extends Fragment {

    private String URL_PAYOUT = DbConfig.URL_PAY + "allPay.php";
    private String URL_PAYOUT_MONTH = DbConfig.URL_PAY + "monthPay.php";
    private String URL_PAYOUT_ADD = DbConfig.URL_PAY + "addPay.php";
    private String urlCompProDetail = DbConfig.URL_COMP + "idComp.php";
    private String URL_COMP_IMG_DETAIL = DbConfig.URL_COMP + "imgComp/";

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

    String idComp, nameComp, codeComp, cityComp, addrComp, phoneComp, emailComp, logoComp;
    String id, email, name, address, level, postUser, phone, access, idCompany, nameCompany;
    String printLogoComp = "";

    private PayoutViewModel purchaseViewModel;
    private FragmentPagerAdapter binding;

    private RecyclerView recyclerViewPayout;
    private RecyclerView.LayoutManager layoutManagerPayout;
    private PayoutCardAdapter payoutCardAdapter;
    List<PayoutViewModelList> payoutViewModelLists;
    private SwipeRefreshLayout swipeRefreshPayout;

    PayoutViewModelList ValuePayout;
    PayoutViewModelList DescPayout;
    PayoutViewModelList DateTimePayout;
    PayoutViewModelList SignPayout;

    EditText inputValuePay, inputDescPay;

    String idPay, idCompPay,valPay,descPay, datetimePay, signPay;

    private File pdfFile;
    String pdfname;
    Context context;

    Calendar calender, calenderTTD;
    SimpleDateFormat simpledateformat, simpledateformatTTD;
    String date, dateTTD;
    ImageView imgHeader;
    Locale locale;

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_payout, container, false);

        locale = new Locale("id","ID");
        Locale.setDefault(locale);

        setHasOptionsMenu(true);
        verifyStoragePermission(getActivity());
        sharedPreferences = this.getActivity().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);

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

        imgHeader = (ImageView) view.findViewById(R.id.imgHeaderLogo);

        recyclerViewPayout = (RecyclerView) view.findViewById(R.id.recyclerViewPayout);
        recyclerViewPayout.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPayout.setHasFixedSize(true);
        payoutViewModelLists = new ArrayList<>();

        payoutCardAdapter = new PayoutCardAdapter(getContext(), payoutViewModelLists);
        recyclerViewPayout.setAdapter(payoutCardAdapter);

        calender = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("EEE, dd-MM-yyyy HH:mm:ss");
        date = simpledateformat.format(calender.getTime());

        calenderTTD = Calendar.getInstance();
        simpledateformatTTD = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        dateTTD = simpledateformatTTD.format(calenderTTD.getTime());

        ImageButton imgBtnAddPay = (ImageButton) view.findViewById(R.id.imgButtonAddPayout);
        imgBtnAddPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogAddPay = new Dialog(getActivity());
                dialogAddPay.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogAddPay.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogAddPay.setContentView(R.layout.activity_payout_add);

                inputValuePay = dialogAddPay.findViewById(R.id.editTxtValPayout);
                inputDescPay = dialogAddPay.findViewById(R.id.editTxtDescPay);

                Button btnCancelPay = dialogAddPay.findViewById(R.id.btnCancelPay);
                btnCancelPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogAddPay.dismiss();
                    }
                });

                Button btnSavePay = dialogAddPay.findViewById(R.id.btnSavePay);
                btnSavePay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (TextUtils.isEmpty(inputDescPay.getText().toString()) && TextUtils.isEmpty(inputValuePay.getText().toString())){
                            inputValuePay.setError("Harap Masukan Jumlah Pengeluaran!");
                            inputValuePay.requestFocus();
                            inputDescPay.setError("Harap Masukan Deskripsi Pengeluaran!");
                            inputValuePay.requestFocus();
                        }else {
                            addPayout();
                            dialogAddPay.dismiss();
                        }
                    }
                });
                dialogAddPay.show();
            }
        });


        //Fetch Data Payout
        getPayout();
        detailComp();

        swipeRefreshPayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshPayout);
        swipeRefreshPayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshItem();

            }

            private void refreshItem() {
                //Fetch Data Payout
                getPayout();
                detailComp();

                onItemLoad();

            }

            private void onItemLoad() {

                swipeRefreshPayout.setRefreshing(false);

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

    private void getPayout() {
        ProgressDialog progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data..");
        progressDialog.show();

        sharedPreferences = this.getActivity().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        AndroidNetworking.post(URL_PAYOUT)
                .addBodyParameter("idCompPay", idCompany)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        payoutViewModelLists.clear();

                        Log.i("Info", "Data: " + response.toString());
                        try {
                            int status = response.getInt("code");

                            if (status == 1) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    PayoutViewModelList payoutViewModelList = new PayoutViewModelList(
                                            jsonObject.getString("idPayout"),
                                            jsonObject.getString("value"),
                                            jsonObject.getString("descPayout"),
                                            jsonObject.getString("datetimePayout"),
                                            jsonObject.getString("name")

                                    );
                                    payoutViewModelLists.add(payoutViewModelList);
                                    PayoutCardAdapter payoutCardAdapter = new PayoutCardAdapter(getContext(), payoutViewModelLists);
                                    recyclerViewPayout.setAdapter(payoutCardAdapter);
                                    progressDialog.dismiss();
                                }
                            }
                            if (status == 0) {
                                Toast.makeText(getContext(), "Data Pengeluaran Tidak Ada!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

//                            JSONArray jsonArray = response.getJSONArray("data");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                PayoutViewModelList payoutViewModelList = new PayoutViewModelList(
//                                        jsonObject.getString("idPayout"),
//                                        jsonObject.getString("value"),
//                                        jsonObject.getString("descPayout"),
//                                        jsonObject.getString("datetimePayout"),
//                                        jsonObject.getString("name")
//
//                                );
//                                payoutViewModelLists.add(payoutViewModelList);
//                                PayoutCardAdapter payoutCardAdapter = new PayoutCardAdapter(getContext(), payoutViewModelLists);
//                                recyclerViewPayout.setAdapter(payoutCardAdapter);
//                                progressDialog.dismiss();
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        payoutCardAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();
                    }
                });
    }

    private void getPayoutMonth() {
        ProgressDialog progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data..");
        progressDialog.show();

        sharedPreferences = this.getActivity().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        AndroidNetworking.post(URL_PAYOUT_MONTH)
                .addBodyParameter("idCompPay", idCompany)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        payoutViewModelLists.clear();

                        Log.i("Info", "Data: " + response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                PayoutViewModelList payoutViewModelList = new PayoutViewModelList(
                                        jsonObject.getString("idPayout"),
                                        jsonObject.getString("value"),
                                        jsonObject.getString("descPayout"),
                                        jsonObject.getString("datetimePayout"),
                                        jsonObject.getString("name")

                                );
                                payoutViewModelLists.add(payoutViewModelList);
                                PayoutCardAdapter payoutCardAdapter = new PayoutCardAdapter(getContext(), payoutViewModelLists);
                                recyclerViewPayout.setAdapter(payoutCardAdapter);
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        payoutCardAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();
                    }
                });
    }

    private void addPayout() {
        ProgressDialog progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data..");
        progressDialog.show();

        sharedPreferences = this.getActivity().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        AndroidNetworking.post(URL_PAYOUT_ADD)
                .addBodyParameter("idCompPay", idCompany)
                .addBodyParameter("valPay", inputValuePay.getText().toString())
                .addBodyParameter("descPay", inputDescPay.getText().toString())
                .addBodyParameter("datetimePay", date)
                .addBodyParameter("signPay", id)
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
                                new AlertDialog.Builder(getActivity())
                                        .setMessage("Berhasil Menambah Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Context context = getActivity();
                                                getActivity().setResult(RESULT_OK);
                                                AlertDialog optionDialog = new AlertDialog.Builder(getActivity()).create();
                                                optionDialog.dismiss();
                                                Toast.makeText(context, "Berhasil Menambah Data Pengeluaran", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .show();
                            }else{

                                new AlertDialog.Builder(getActivity())
                                        .setMessage("Gagal Menambah Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
//                                                Log.i("Input", "Data: "+idPay+", "+idCompPay+", "+valPay+", "+descPay+", "+datetimePay+", "+signPay.toString());
                                                Context c = getActivity();
                                                AlertDialog optionDialog = new AlertDialog.Builder(getActivity()).create();
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
                        Toast.makeText(getContext(), "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();
                    }
                });
    }

    private void createPdfMonth() throws IOException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/POSBaraka");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i("Print: ", "Created a new directory for PDF");
        }

        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        pdfname = "LaporanPengeluaranBulanan_" + nameCompany + "_" + timeStamp + ".pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);

        OutputStream output = new FileOutputStream(pdfFile);

        Document document = new Document(PageSize.A4);

        Bitmap bmp = ((BitmapDrawable)imgHeader.getDrawable()).getBitmap();
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
        table.addCell("Nilai Pengeluaran");
        table.addCell("Deskripsi");
        table.addCell("Tanggal & Waktu");
        table.addCell("Di Bayar Oleh");
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
            cells[j].setBackgroundColor(BaseColor.PINK);
        }

        for (int j = 0; j < cellsHeader.length; j++) {
            cellsHeader[j].setBorder(Rectangle.NO_BORDER);
        }



        for (int i = 0; i < payoutViewModelLists.size(); i++) {
            ValuePayout = payoutViewModelLists.get(i);
            DescPayout = payoutViewModelLists.get(i);
            DateTimePayout = payoutViewModelLists.get(i);
            SignPayout = payoutViewModelLists.get(i);
            String ValuePayoutL = ValuePayout.getValPay();
            String DescPayoutL = DescPayout.getDescPay();
            String DateTimePayoutL = DateTimePayout.getDatetimePay();
            String SignPayoutL = SignPayout.getNameUser();
            table.addCell(String.valueOf(i+1));

            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("In","ID"));
            double formatRpPrintPay = Double.parseDouble(String.valueOf(ValuePayoutL));
            table.addCell(formatRupiah.format(formatRpPrintPay));

            table.addCell(String.valueOf(DescPayoutL));
            table.addCell(String.valueOf(DateTimePayoutL));
            table.addCell(String.valueOf(SignPayoutL));
        }

        //Step 2
        PdfWriter.getInstance(document, output);

        //Step 3
        document.open();
        document.add(tableHeader);


        LineSeparator ls = new LineSeparator();


        //Step 4 Add content

        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 14.0f, Font.UNDERLINE);
        Font g = new Font(Font.FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL);
        Font h = new Font(Font.FontFamily.TIMES_ROMAN, 5.0f, Font.NORMAL);
        document.add(new Paragraph(" ", h));
        document.add(new Chunk(ls));
        ls.setOffset(5);
        document.add(new Paragraph("Laporan Pengeluaran Bulanan", f));
        document.add(new Paragraph(" ", g));
        document.add(table);
        document.add(tglTTD);
        document.add(userTTD);



        //Step 5: Close the document
        document.close();
        Log.e("List: ", payoutViewModelLists.toString());

        Toast.makeText(getContext(), "Pdf Generate!", Toast.LENGTH_SHORT).show();

        checkPermission();
        openPDF(pdfname);
    }

    private void createPdf() throws IOException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/POSBaraka");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i("Print: ", "Created a new directory for PDF");
        }

        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        pdfname = "LaporanPengeluaran_" + nameCompany + "_" + timeStamp + ".pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);

        OutputStream output = new FileOutputStream(pdfFile);

        Document document = new Document(PageSize.A4);

        Bitmap bmp = ((BitmapDrawable)imgHeader.getDrawable()).getBitmap();
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
        table.addCell("Nilai Pengeluaran");
        table.addCell("Deskripsi");
        table.addCell("Tanggal & Waktu");
        table.addCell("Di Bayar Oleh");
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
            cells[j].setBackgroundColor(BaseColor.PINK);
        }

        for (int j = 0; j < cellsHeader.length; j++) {
            cellsHeader[j].setBorder(Rectangle.NO_BORDER);
        }



        for (int i = 0; i < payoutViewModelLists.size(); i++) {
            ValuePayout = payoutViewModelLists.get(i);
            DescPayout = payoutViewModelLists.get(i);
            DateTimePayout = payoutViewModelLists.get(i);
            SignPayout = payoutViewModelLists.get(i);
            String ValuePayoutL = ValuePayout.getValPay();
            String DescPayoutL = DescPayout.getDescPay();
            String DateTimePayoutL = DateTimePayout.getDatetimePay();
            String SignPayoutL = SignPayout.getNameUser();
            table.addCell(String.valueOf(i+1));

            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("In","ID"));
            double formatRpPrintPay = Double.parseDouble(String.valueOf(ValuePayoutL));
            table.addCell(formatRupiah.format(formatRpPrintPay));

            table.addCell(String.valueOf(DescPayoutL));
            table.addCell(String.valueOf(DateTimePayoutL));
            table.addCell(String.valueOf(SignPayoutL));
        }

        //Step 2
        PdfWriter.getInstance(document, output);

        //Step 3
        document.open();
        document.add(tableHeader);


        LineSeparator ls = new LineSeparator();


        //Step 4 Add content

        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 14.0f, Font.UNDERLINE);
        Font g = new Font(Font.FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL);
        Font h = new Font(Font.FontFamily.TIMES_ROMAN, 5.0f, Font.NORMAL);
        document.add(new Paragraph(" ", h));
        document.add(new Chunk(ls));
        ls.setOffset(5);
        document.add(new Paragraph("Laporan Pengeluaran ", f));
        document.add(new Paragraph(" ", g));
        document.add(table);
        document.add(tglTTD);
        document.add(userTTD);



        //Step 5: Close the document
        document.close();
        Log.e("List: ", payoutViewModelLists.toString());

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
                                        .into(imgHeader);


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
                        getPayoutMonth();
                        break;
                    case 1:
                        getPayout();
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
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_print, menu);
    }

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
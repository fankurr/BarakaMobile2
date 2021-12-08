package com.baraka.barakamobile.ui.sell;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SellFragment extends Fragment {

    private String URL_SELL = DbConfig.URL_SELL + "allTx.php";

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
    public static final String ADDR_COMP = "addrComp";
    public static final String PHONE_COMP = "phoneComp";
    public static final String EMAIL_COMP = "emailComp";
    public static final String LOGO_COMP = "logoComp";

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
    private TxCardAdapter txCardAdapter;

    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    private RecyclerView recyclerViewTx;
    private RecyclerView.LayoutManager layoutManagerTx;

    private List<TxViewModel> txViewModelList;

    TxViewModel NamePrdct;
    TxViewModel QtyTX;
    TxViewModel DateTimeTx;

    private File pdfFile;
    String pdfname;
    Context context;

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_sell, container, false);

        setHasOptionsMenu(true);

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

        //Fetch Data TX
        getTx();

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshSell);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshItem();

            }

            private void refreshItem() {
                //Fetch Data TX
                getTx();

                onItemLoad();

            }

            private void onItemLoad() {

                swipeRefreshLayout.setRefreshing(false);

            }
        });


        return view;
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
                                        jsonObject.getString("dateTime")

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
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();
                    }
                });
    }

    private void createPdf() throws FileNotFoundException, DocumentException {
//        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DOCUMENTS), "POSBaraka/");
//        if (!pdfFolder.exists()) {
//            pdfFolder.mkdir();
//            Log.i("Print", "Pdf Directory created");
//        }

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

        PdfPTable table = new PdfPTable(new float[]{1, 3, 3, 3});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(50);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("No.");
        table.addCell("Nama Produk");
        table.addCell("Jumlah");
        table.addCell("Tanggal & Waktu");
        table.setHeaderRows(1);

        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(new BaseColor(0, 206, 137));
        }
        for (int i = 0; i < txViewModelList.size(); i++) {
            NamePrdct = txViewModelList.get(i);
            QtyTX = txViewModelList.get(i);
            DateTimeTx = txViewModelList.get(i);
            String namePrdctL = NamePrdct.getNamePrdct();
            String qtyTxL = QtyTX.getQtyTx();
            String datetimeTxL = DateTimeTx.getDatetimeTx();
            table.addCell(String.valueOf(i+1));
            table.addCell(String.valueOf(namePrdctL));
            table.addCell(String.valueOf(qtyTxL));
            table.addCell(String.valueOf(datetimeTxL));
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
        document.add(new Paragraph("Laporan Penjualan ", f));
        document.add(new Paragraph(nameCompany, g));
        document.add(new Paragraph(" ", h));
        document.add(table);

        //Step 5: Close the document
        document.close();
        Log.e("List: ", txViewModelList.toString());

        Toast.makeText(getContext(), "Pdf Generate!", Toast.LENGTH_SHORT).show();

        checkPermission();
        previewPdf();
    }

    private void previewPdf() {

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

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_print, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.printItem:
                try {
                    createPdf();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
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
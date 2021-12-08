package com.baraka.barakamobile.ui.payout;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.databinding.FragmentPayoutBinding;
import com.baraka.barakamobile.ui.pos.POSOutputViewModel;
import com.baraka.barakamobile.ui.sell.TxCardAdapter;
import com.baraka.barakamobile.ui.sell.TxViewModel;
import com.baraka.barakamobile.ui.supplier.AddEditSplrActivity;
import com.baraka.barakamobile.ui.supplier.SupplierDetailActivity;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class PayoutFragment extends Fragment {

    private String URL_PAYOUT = DbConfig.URL_PAY + "allPay.php";
    private String URL_PAYOUT_ADD = DbConfig.URL_PAY + "addPay.php";

    private final static String TAG_ID = "id";
    private final static String TAG_EMAIL = "email";
    private final static String TAG_NAME = "name";
    private final static String TAG_LEVEL = "level";
    private final static String TAG_ACCESS = "access";
    private final static String TAG_IDCOMP = "idCompany";
    private final static String TAG_COMP = "nameCompany";

    String id, email, name, level, access, idCompany, nameCompany;

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

    Calendar calender;
    SimpleDateFormat simpledateformat;
    String date;

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;

    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_payout, container, false);

        setHasOptionsMenu(true);

        sharedPreferences = this.getActivity().getSharedPreferences(my_shared_preferences,Context.MODE_PRIVATE);
        id = sharedPreferences.getString(TAG_ID, null);
        email = sharedPreferences.getString(TAG_EMAIL, null);
        name = sharedPreferences.getString(TAG_NAME, null);
        level = sharedPreferences.getString(TAG_LEVEL, null);
        access = sharedPreferences.getString(TAG_ACCESS, null);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);
        nameCompany = sharedPreferences.getString(TAG_COMP, null);

        recyclerViewPayout = (RecyclerView) view.findViewById(R.id.recyclerViewPayout);
        recyclerViewPayout.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPayout.setHasFixedSize(true);
        payoutViewModelLists = new ArrayList<>();

        payoutCardAdapter = new PayoutCardAdapter(getContext(), payoutViewModelLists);
        recyclerViewPayout.setAdapter(payoutCardAdapter);

        calender = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("EEE, dd-MM-yyyy HH:mm:ss");
        date = simpledateformat.format(calender.getTime());

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

        swipeRefreshPayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshPayout);
        swipeRefreshPayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshItem();

            }

            private void refreshItem() {
                //Fetch Data Payout
                getPayout();

                onItemLoad();

            }

            private void onItemLoad() {

                swipeRefreshPayout.setRefreshing(false);

            }
        });

        return view;
    }

    private void getPayout() {
        ProgressDialog progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data..");
        progressDialog.show();

        sharedPreferences = this.getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
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

        sharedPreferences = this.getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
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
                                                Log.i("Input", "Data: "+idPay+", "+idCompPay+", "+valPay+", "+descPay+", "+datetimePay+", "+signPay.toString());
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

        pdfname = "LaporanPengeluaran_" + nameCompany + "_" + timeStamp + ".pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);

        OutputStream output = new FileOutputStream(pdfFile);

        Document document = new Document(PageSize.A4);

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

        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.PINK);
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
            table.addCell(String.valueOf(ValuePayoutL));
            table.addCell(String.valueOf(DescPayoutL));
            table.addCell(String.valueOf(DateTimePayoutL));
            table.addCell(String.valueOf(SignPayoutL));
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
        document.add(new Paragraph("Laporan Pengeluaran ", f));
        document.add(new Paragraph(nameCompany, g));
        document.add(new Paragraph(" ", h));
        document.add(table);

        //Step 5: Close the document
        document.close();
        Log.e("List: ", payoutViewModelLists.toString());

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
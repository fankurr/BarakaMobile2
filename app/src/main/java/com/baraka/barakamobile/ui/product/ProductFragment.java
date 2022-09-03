package com.baraka.barakamobile.ui.product;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.util.DbConfig;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment implements PrdctCardAdapter.OnItemClickListener {

    private ProductViewModel productViewModel;
//    private FragmentProductBinding binding;
    private RecyclerView recyclerView, recyclerView1;
    private RecyclerView.LayoutManager layoutManager;
    private PrdctCardAdapter prdctCardAdapter;
    private CateCardAdpater cateCardAdpater;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView imageView;

    ProgressBar progressBar;


    private String URL_PRDCT = DbConfig.URL_PRDCT + "allPrdct.php";
    private String URL_CATE = DbConfig.URL_CATE + "allCat.php";
    private String URL_PRDCT_IMG = DbConfig.URL_PRDCT + "imgPrdct/";


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
    public static final String IMG_PRDCT = "imgPrdct";

    public static final String ID_SPLR = "idSplr";
    public static final String COMP_SPLR= "idCompSplr";
    public static final String NAME_SPLR= "nameSplr";
    public static final String ADDR_SPLR = "addrSplr";
    public static final String PHONE_SPLR = "phoneSplr";
    public static final String EMAIL_SPLR = "emailSplr";
    public static final String DESC_SPLR = "descSplr";
    public static final String IMG_SPLR = "imgSplr";

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

    private List<CateViewModel> cateViewModelList;
    private List<PrdctViewModel> prdctViewModelList;

    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_product, container, false);

//        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);

        sharedPreferences = this.getActivity().getSharedPreferences(my_shared_preferences,Context.MODE_PRIVATE);
        id = sharedPreferences.getString(TAG_ID, null);
        email = sharedPreferences.getString(TAG_EMAIL, null);
        name = sharedPreferences.getString(TAG_NAME, null);
        level = sharedPreferences.getString(TAG_LEVEL, null);
        access = sharedPreferences.getString(TAG_ACCESS, null);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);
        nameCompany = sharedPreferences.getString(TAG_COMP, null);


        recyclerView = (RecyclerView) view.findViewById(R.id.RecycleViewCate);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setHasFixedSize(true);
        cateViewModelList = new ArrayList<>();
        cateCardAdpater = new CateCardAdpater(getContext(), cateViewModelList);
        recyclerView.setAdapter(cateCardAdpater);


        recyclerView1 = (RecyclerView) view.findViewById(R.id.RecycleViewPrdct);
        recyclerView1.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView1.setHasFixedSize(true);
        prdctViewModelList = new ArrayList<>();

        prdctCardAdapter = new PrdctCardAdapter(prdctViewModelList, getContext());
        recyclerView1.setAdapter(prdctCardAdapter);


        //Fetch Data Kategori
        getCategory();

        //Fetch Data Produk
        getProduct();

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.SwipeRefreshPrdct);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshItem();

            }

            private void refreshItem() {

                //Fetch Data Kategori
                getCategory();

                //Fetch Data Produk
                getProduct();

                onItemLoad();

            }

            private void onItemLoad() {

                swipeRefreshLayout.setRefreshing(false);

            }
        });

        ImageView imageViewAllPrdct = (ImageView) view.findViewById(R.id.imageViewAllPrdct);
        imageViewAllPrdct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAllPrdct = new Intent(getContext(), PrdctListActivity.class);
                startActivity(intentAllPrdct);
            }
        });

        ImageView imageViewAllCate = (ImageView) view.findViewById(R.id.imageViewAllSplr);
        imageViewAllCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentAllCate = new Intent(getContext(), CateListActivity.class);
                startActivity(intentAllCate);
            }
        });

        return view;
    }

    public void getProduct() {
        ProgressDialog progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data Produk");
        progressDialog.show();

        sharedPreferences = this.getActivity().getSharedPreferences(my_shared_preferences,Context.MODE_PRIVATE);
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
                                    PrdctCardAdapter prdctCardAdapter = new PrdctCardAdapter(prdctViewModelList, getContext());
                                    recyclerView1.setAdapter(prdctCardAdapter);
                                    prdctCardAdapter.setOnItemClickListener(ProductFragment.this);
                                    progressDialog.dismiss();
                                }
                            }
                            if (status == 0) {
                                Toast.makeText(getContext(), "Data Produk Tidak Ada!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();
                    }
                });
    }

    public void getCategory(){
        ProgressDialog progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data..");
        progressDialog.show();

        sharedPreferences = this.getActivity().getSharedPreferences(my_shared_preferences,Context.MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        AndroidNetworking.post(URL_CATE)
                .addBodyParameter("idCompCat", idCompany)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        cateViewModelList.clear();
                        try {
                            int status = response.getInt("success");
                            if (status == 1) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    CateViewModel cateViewModel = new CateViewModel(
                                            jsonObject.getString("idCategory"),
                                            jsonObject.getString("companyCate"),
                                            jsonObject.getString("category"),
                                            jsonObject.getString("descCate"),
                                            jsonObject.getString("imageCate")

                                    );
                                    cateViewModelList.add(cateViewModel);
                                    progressDialog.dismiss();
                                }
                            }
                            if (status == 0) {
                                Toast.makeText(getContext(), "Data Kategori Tidak Ada!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        cateCardAdpater.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();
                    }
                });
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }

    @Override
    public void onItemClick(int position) {
        Intent prdctDetailIntent = new Intent(getContext(), PrdctDetail.class);
        PrdctViewModel clickedPrdct = prdctViewModelList.get(position);

        prdctDetailIntent.putExtra(ID_PRDCT, clickedPrdct.getIdPrdct());
        prdctDetailIntent.putExtra(NAME_PRDCT, clickedPrdct.getNamePrdct());
        prdctDetailIntent.putExtra(CODE_PRDCT, clickedPrdct.getCodePrdct());
        prdctDetailIntent.putExtra(DESC_PRDCT, clickedPrdct.getDescPrdct());
        prdctDetailIntent.putExtra(PRICE_PRDCT, clickedPrdct.getUnitPrice());
        prdctDetailIntent.putExtra(UNIT_PRDCT, clickedPrdct.getUnitPrdct());
        prdctDetailIntent.putExtra(STOCK_PRDCT, clickedPrdct.getStockPrdct());
        prdctDetailIntent.putExtra(IMG_PRDCT, clickedPrdct.getImgPrdct());
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
}
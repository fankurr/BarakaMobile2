package com.baraka.barakamobile.ui.usermanaje;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.databinding.FragmentUserManajeBinding;
import com.baraka.barakamobile.ui.LoginActivity;
import com.baraka.barakamobile.ui.product.CateCardAdpater;
import com.baraka.barakamobile.ui.product.CateViewModel;
import com.baraka.barakamobile.ui.product.PrdctCardAdapter;
import com.baraka.barakamobile.ui.product.PrdctDetail;
import com.baraka.barakamobile.ui.product.PrdctListActivity;
import com.baraka.barakamobile.ui.product.PrdctViewModel;
import com.baraka.barakamobile.ui.product.ProductFragment;
import com.baraka.barakamobile.ui.util.DbConfig;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class UserManajeFragment extends Fragment{

    private UserManajeViewModel userManajeViewModel;
    private FragmentUserManajeBinding binding;

    private RecyclerView recyclerView, recyclerViewLog;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String URL_WORKER = DbConfig.URL_WORKER + "workerComp.php";
    private String URL_WORKER_LOG = DbConfig.URL_LOG + "allLog.php";

    private List<WorkerViewModel> workerViewModelList;
    private List<WorkerLogViewModel> workerLogViewModelList;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

//    private final static String TAG_ID = "idUser";
//    private final static String TAG_EMAIL = "emailUser";
//    private final static String TAG_NAME = "nameUser";
//    private final static String TAG_LEVEL = "lvlUser";
//    private final static String TAG_ACCESS = "loginUser";
//    private final static String TAG_IDCOMP = "compUser";
//    private final static String TAG_COMP = "nameComp";

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

    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";
    WorkerCardAdapter workerCardAdapter;
    WorkerLogAdapter workerLogAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_user_manaje, container, false);

        // RecyclerView Worker
        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerViewWorker);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        workerViewModelList = new ArrayList<>();

        workerCardAdapter = new WorkerCardAdapter(workerViewModelList, getContext());
        recyclerView.setAdapter(workerCardAdapter);

        // RecyclerView Log
        recyclerViewLog = (RecyclerView) view.findViewById(R.id.RecyclerViewLogWorker);
        recyclerViewLog.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewLog.setHasFixedSize(true);
        workerLogViewModelList = new ArrayList<>();

        workerLogAdapter = new WorkerLogAdapter(workerLogViewModelList, getContext());
        recyclerViewLog.setAdapter(workerLogAdapter);



        // Cek session login jika TRUE maka langsung buka MainActivity
        sharedPreferences = this.getActivity().getSharedPreferences(my_shared_preferences,Context.MODE_PRIVATE);
        id = sharedPreferences.getString(TAG_ID, null);
        email = sharedPreferences.getString(TAG_EMAIL, null);
        name = sharedPreferences.getString(TAG_NAME, null);
        level = sharedPreferences.getString(TAG_LEVEL, null);
        access = sharedPreferences.getString(TAG_ACCESS, null);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);
        nameCompany = sharedPreferences.getString(TAG_COMP, null);

        // GET Worker JSON
        getWorker();

        // GET Worker Log JSON
        getWorkerLog();

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshUser);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItem();
            }

            private void refreshItem() {

                // GET Worker JSON
                getWorker();

                // GET Worker Log JSON
                getWorkerLog();

                onItemLoad();
            }

            private void onItemLoad() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        ImageView imgLogListAll = (ImageView) view.findViewById(R.id.imgLogListAll);
        imgLogListAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogList = new Intent(getContext(), WorkerLogListActivity.class);
                startActivity(intentLogList);
            }
        });


        return view;

    }

    public void getWorker(){
        ProgressDialog progressDialog = new ProgressDialog(this. getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data Karyawan");
        progressDialog.show();

//        sharedPreferences = this.getActivity().getSharedPreferences(my_shared_preferences,MODE_PRIVATE);

        AndroidNetworking.post(URL_WORKER)
                .addBodyParameter("compUser", idCompany)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        workerViewModelList.clear();
                        Log.i("Info", "Data: " + response.toString());

                        try {
                            int status = response.getInt("code");
                            if (status == 1) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    WorkerViewModel workerViewModel = new WorkerViewModel(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("email"),
                                            jsonObject.getString("name"),
                                            jsonObject.getString("address"),
                                            jsonObject.getString("level"),
                                            jsonObject.getString("phone"),
                                            jsonObject.getString("access"),
                                            jsonObject.getString("imgWorker"),
                                            jsonObject.getString("company"),
                                            jsonObject.getString("nameCompany")
                                    );


                                    workerViewModelList.add(workerViewModel);
                                    WorkerCardAdapter workerCardAdapter = new WorkerCardAdapter(workerViewModelList, getContext());
                                    recyclerView.setAdapter(workerCardAdapter);
//                                workerCardAdapter.setOnItemClickListener(UserManajeFragment.this);
                                    progressDialog.dismiss();
                                }
                            }
                            if (status == 0) {
                                Toast.makeText(getContext(), "Data Karyawan Tidak Ada!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        workerCardAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), "Koneksi Gagal: " + anError.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
//                        Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        progressDialog.dismiss();
                    }
                });
    }

    public void getWorkerLog(){
        ProgressDialog progressDialog = new ProgressDialog(this. getContext());
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
                                WorkerLogAdapter workerLogAdapter = new WorkerLogAdapter(workerLogViewModelList, getContext());
                                recyclerViewLog.setAdapter(workerLogAdapter);
//                                workerCardAdapter.setOnItemClickListener(UserManajeFragment.this);
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        workerCardAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), "Koneksi Gagal: " + anError.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
//                        Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        progressDialog.dismiss();
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


//    @Override
//    public void onItemClick(int position) {
//    }
}
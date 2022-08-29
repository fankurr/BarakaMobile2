package com.baraka.barakamobile.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.baraka.barakamobile.R;
import com.baraka.barakamobile.ui.product.AddEditPrdctActivity;
import com.baraka.barakamobile.ui.util.DbConfig;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class EditProfileActivity extends AppCompatActivity {
    String id, email, name, address, level, postUser, phone, access, imageUser, idCompany, nameCompany;
    String idComp, nameComp, codeComp, addrComp, phoneComp, emailComp, logoComp;
    // the activity result code
    int SELECT_PICTURE = 200;

    EditText inputNameProfileEdit, inputPostProfileEdit, inputAddrProfileEdit, inputTlpProfileEdit, inputEmailProfileEdit;
    TextView txtViewCompProfileEdit;
    ImageView imgUser;
    Uri selectedImageUri;
    File imgProfile;

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    private String urlEdit = DbConfig.URL + "editProfile.php";
    private String URL_USER_IMG_EDIT = DbConfig.URL + "imgUser/";

    private String urlUserEdit = DbConfig.URL + "idUserComp.php";

    private final static String TAG_ID = "id";
    private final static String TAG_EMAIL = "email";
    private final static String TAG_NAME = "name";
    private final static String TAG_ADDRESS = "address";
    private final static String TAG_LEVEL = "level";
    private final static String TAG_POST = "postUser";
    private final static String TAG_TLP = "phone";
    private final static String TAG_ACCESS = "access";
    private final static String TAG_IMG = "image";
    private final static String TAG_IDCOMP = "idCompany";
    private final static String TAG_COMP = "nameCompany";

    public static final String ID_COMP = "idComp";
    public static final String NAME_COMP = "nameComp";
    public static final String CODE_COMP = "codeComp";
    public static final String ADDR_COMP = "addrComp";
    public static final String PHONE_COMP = "phoneComp";
    public static final String EMAIL_COMP = "emailComp";
    public static final String LOGO_COMP = "logoComp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        progressDialog = new ProgressDialog(EditProfileActivity.this);

        Intent intent = getIntent();
        id = intent.getStringExtra(TAG_ID);
        name = intent.getStringExtra(TAG_NAME);
        email = intent.getStringExtra(TAG_EMAIL);
        address = intent.getStringExtra(TAG_ADDRESS);
        postUser = intent.getStringExtra(TAG_POST);
        phone = intent.getStringExtra(TAG_TLP);
        access = intent.getStringExtra(TAG_ACCESS);
        imageUser = intent.getStringExtra(TAG_IMG);
        level = intent.getStringExtra(TAG_LEVEL);

        idCompany = intent.getStringExtra(TAG_IDCOMP);
        nameCompany = intent.getStringExtra(TAG_COMP);
        codeComp = intent.getStringExtra(CODE_COMP);
        addrComp = intent.getStringExtra(ADDR_COMP);
        phoneComp = intent.getStringExtra(PHONE_COMP);
        emailComp = intent.getStringExtra(EMAIL_COMP);

        inputNameProfileEdit = findViewById(R.id.editTxtNameProfileEdit);
//        txtViewCompProfileEdit = findViewById(R.id.txtViewCompProfileEdit);
        inputPostProfileEdit = findViewById(R.id.editTxtPostProfileEdit);
        inputAddrProfileEdit = findViewById(R.id.editTxtAddrProfileEdit);
        inputTlpProfileEdit = findViewById(R.id.editTxtTlpProfileEdit);
        inputEmailProfileEdit = findViewById(R.id.editTxtEmailProfileEdit);
        imgUser = findViewById(R.id.imgUserEdit);

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageChooser();

//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("*/*");
//                startActivityForResult(intent, 7);

            }
        });

//        inputNameProfileEdit.setText(name);
//        txtViewCompProfileEdit.setText(nameCompany);
//        inputPostProfileEdit.setText(postUser);
//        inputAddrProfileEdit.setText(address);
//        inputEmailProfileEdit.setText(email);
//        inputTlpProfileEdit.setText(phone);

        editDetailProfile();

        inputPostProfileEdit.setEnabled(false);
        inputPostProfileEdit.setFocusable(false);

        Button btnSaveProfileEdit = findViewById(R.id.btnSaveProfileEdit);
        btnSaveProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                progressDialog.setMessage("Merubah Data...");
//                progressDialog.setCancelable(false);
//                progressDialog.show();

                editProfile();
//                progressDialog.dismiss();
            }
        });

        Button btnBackProfileEdit = findViewById(R.id.btnBackProfileEdit);
        btnBackProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_twotone_chevron_left_24);
    }

    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), SELECT_PICTURE);
    }

    public String getRealPathFromURI(Uri contentURI, Activity context) {

        String[] projection = { MediaStore.Images.Media.DATA };

        @SuppressWarnings("deprecation")
        Cursor cursor = context.managedQuery(contentURI, projection, null, null, null);
        if (cursor == null)
            return null;

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);
            // cursor.close();
            return s;
        }

        // cursor.close();
        return null;

    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout

                    Picasso.get().load(selectedImageUri)
                            .resize(450, 450)
                            .centerCrop()
                            .into(imgUser);

                    String imagepath = getRealPathFromURI(selectedImageUri,EditProfileActivity.this);

                    imgProfile = new File(imagepath);

//                    imgUser.setImageURI(selectedImageUri);
                }
            }
        }
    }

    // Edit Profile
    public void editProfile(){
        progressDialog = new ProgressDialog(EditProfileActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Profil..");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

//        AndroidNetworking.upload(urlEdit)
//                .addMultipartFile("imgUser", imgUser)
//                .addMultipartParameter("key","value")
//                .setTag("uploadTest")
//                .setPriority(Priority.HIGH)
//                .build()
//                .setUploadProgressListener(new UploadProgressListener() {
//                    @Override
//                    public void onProgress(long bytesUploaded, long totalBytes) {
//                        // do anything with progress
//                    }
//                })
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // do anything with response
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        // handle error
//                    }
//                });

        AndroidNetworking.upload(urlEdit)
                .addMultipartParameter("idUser", id.toString())
                .addMultipartParameter("nameUser", inputNameProfileEdit.getText().toString())
                .addMultipartParameter("emailUser", inputEmailProfileEdit.getText().toString())
                .addMultipartParameter("addrUser", inputAddrProfileEdit.getText().toString())
                .addMultipartParameter("phoneUser", inputTlpProfileEdit.getText().toString())
                .addMultipartFile("imgUser", imgProfile)
                .setTag("Update Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("Respon Edit","" + response);

                        try {
                            Boolean status = response.getBoolean("status");
                            if (status == true){
                                new AlertDialog.Builder(EditProfileActivity.this)
                                        .setMessage("Berhasil Mengudate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .show();
                            }else{
                                new AlertDialog.Builder(EditProfileActivity.this)
                                        .setMessage("Gagal Mengupdate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
//                                                Log.i("Input", "Data: "+id+", "+name+", "+postUser+", "+address+", "+email+", "+phone.toString());
                                                Intent i = getIntent();
                                                setResult(RESULT_CANCELED,i);
                                                EditProfileActivity.this.finish();
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
                        Toast.makeText(EditProfileActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();

                    }
                });
    }

    // Detail Profile
    public void editDetailProfile(){
        progressDialog = new ProgressDialog(EditProfileActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Detail Profil..");
        progressDialog.show();

        sharedPreferences = getSharedPreferences(my_shared_preferences,MODE_PRIVATE);
        idCompany = sharedPreferences.getString(TAG_IDCOMP, null);

        AndroidNetworking.post(urlUserEdit)
                .addBodyParameter("idUser", id.toString())
                .setTag("Load Data")
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
                                inputNameProfileEdit.setText(jsonObject.getString("name"));
                                inputPostProfileEdit.setText(jsonObject.getString("postUser"));
                                inputAddrProfileEdit.setText(jsonObject.getString("address"));
                                inputEmailProfileEdit.setText(jsonObject.getString("email"));
                                inputTlpProfileEdit.setText(jsonObject.getString("phone"));

                                Picasso.get().load(URL_USER_IMG_EDIT+jsonObject.getString("imgProfile"))
                                        .resize(450, 450)
                                        .centerCrop()
                                        .placeholder(R.drawable.default_image_person_small)
                                        .error(R.drawable.default_image_person_small)
                                        .into(imgUser);

//                                txtViewCompProfileEdit.setText(jsonObject.getString("nameCompany"));
                                getSupportActionBar().setTitle(jsonObject.getString("name"));

                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditProfileActivity.this, "Maaf, gagal Terhubung ke Database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(EditProfileActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+ anError.toString());
                        progressDialog.dismiss();

                    }
                });
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
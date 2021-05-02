package com.wfour.onlinestoreapp.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wfour.onlinestoreapp.AppController;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.network.modelmanager.singletonmanager.AddressManager;
import com.wfour.onlinestoreapp.network1.MyProgressDialog;
import com.wfour.onlinestoreapp.objects.DataPart;
import com.wfour.onlinestoreapp.objects.Person;
import com.wfour.onlinestoreapp.objects.UserObj;
import com.wfour.onlinestoreapp.retrofit.ApiUtils;
import com.wfour.onlinestoreapp.retrofit.respone.ResponeUser;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.FileUtil;
import com.wfour.onlinestoreapp.utils.ImageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {

    Context context;
    EditText name_et, email_et, mobile_et, address_et, member_id_et;
    ImageView img_avatar, btn_edit_avatar;
    TextView lbl_cancel_edit, lbl_save_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        init();
    }

    void init() {
        context = this;
        lbl_cancel_edit = findViewById(R.id.lbl_cancel_edit);
        lbl_save_edit = findViewById(R.id.lbl_save_edit);
        img_avatar = findViewById(R.id.img_avatar);
        btn_edit_avatar = findViewById(R.id.btn_edit_avatar);
        name_et = findViewById(R.id.name_et);
        email_et = findViewById(R.id.email_et);
        mobile_et = findViewById(R.id.mobile_et);
        address_et = findViewById(R.id.address_et);
        member_id_et = findViewById(R.id.member_id_et);

        Intent intent = getIntent();
        name_et.setText(intent.getStringExtra("name_text"));
        email_et.setText(intent.getStringExtra("email_text"));
        mobile_et.setText(intent.getStringExtra("phone_text"));
        address_et.setText(intent.getStringExtra("addres_text"));
        member_id_et.setText(intent.getStringExtra("member_id_text"));
        if (intent!=null && !intent.getStringExtra("avatar_url_text").isEmpty()) {
            Picasso.with(context).load(intent.getStringExtra("avatar_url_text")).into(img_avatar);
        }
        lbl_cancel_edit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        lbl_save_edit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (check()) {

                            save();
//                            updateProfile();
                        }
                    }
                }
        );
        btn_edit_avatar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppUtil.pickImage((Activity) context, AppUtil.PICK_IMAGE_REQUEST_CODE);
                    }
                }
        );
    }

    boolean check() {
        boolean b = true;
        if (name_et.getText().toString().isEmpty()) {
            Toast.makeText(context, "Enter Name", Toast.LENGTH_SHORT).show();
            name_et.requestFocus();
            b = false;
            return b;
        } else if (email_et.getText().toString().isEmpty()) {
            Toast.makeText(context, "Enter Email", Toast.LENGTH_SHORT).show();
            email_et.requestFocus();
            b = false;
            return b;
        } else if (mobile_et.getText().toString().isEmpty()) {
            Toast.makeText(context, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
            mobile_et.requestFocus();
            b = false;
            return b;
        } else if (address_et.getText().toString().isEmpty()) {
            Toast.makeText(context, "Enter Address", Toast.LENGTH_SHORT).show();
            address_et.requestFocus();
            b = false;
            return b;
        } else if (member_id_et.getText().toString().isEmpty()) {
            Toast.makeText(context, "Enter Member Id", Toast.LENGTH_SHORT).show();
            member_id_et.requestFocus();
            b = false;
            return b;
        }
        return b;
    }

    private void updateProfile() {
        String id = DataStoreManager.getUser().getId();
        final String passWord = DataStoreManager.getUser().getPassWord();
        DataPart avatar = null;
        if (img_avatar.getDrawable() != null) {
            avatar = new DataPart("avatar.png", AppUtil.getFileDataFromDrawable(context, img_avatar.getDrawable()), DataPart.TYPE_IMAGE);
        }

        ModelManager.updateProfile(context, avatar.toString(), id, name_et.getText().toString(), address_et.getText().toString(), mobile_et.getText().toString(), email_et.getText().toString(), new ModelManagerListener() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(object.toString());
                    ApiResponse response = new ApiResponse(jsonObject);
                    Log.e("hihi", " 2" + response.toString());

                    if (!response.isError()) {
                        UserObj userObj = response.getDataObject(UserObj.class);
                        //userObj.setToken(DataStoreManager.getUser().getToken());
                        //userObj.setRememberMe(DataStoreManager.getUser().isRememberMe());
                        userObj.setName(name_et.getText().toString());
                        userObj.setAddress(address_et.getText().toString());
                        userObj.setPhone(mobile_et.getText().toString());
                        userObj.setPassWord(passWord);
                        DataStoreManager.saveUser(userObj);
                        AppController.getInstance().setUserUpdated(true);
                        AppUtil.showToast(context, R.string.msg_update_success);
//                        setData(userObj);
//                        AddressManager.getInstance().getArray().clear();
//                        AddressManager.getInstance().addItem(new Person(bussinessName, phone, address));
                        Log.e("hihi", "3: " + userObj.toString());
                    } else {
                        Toast.makeText(context, response.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    private RequestBody name, email, avatar, address, phone, id;

    private void save() {
        MyProgressDialog myProgressDialog = new MyProgressDialog(context);
        myProgressDialog.setCancelable(false);
        myProgressDialog.show();
        final String passWord = DataStoreManager.getUser().getPassWord();

        String sid = DataStoreManager.getUser().getId();
        String savartar = DataStoreManager.getUser().getAvatar();
        String semail = email_et.getText().toString();
        String sname = name_et.getText().toString();
        String saddress = address_et.getText().toString();
        String sphone = mobile_et.getText().toString();

        id = RequestBody.create(MediaType.parse("text/plain"), sid);
        name = RequestBody.create(MediaType.parse("text/plain"), sname);
        address = RequestBody.create(MediaType.parse("text/plain"), saddress);
        avatar = RequestBody.create(MediaType.parse("text/plain"), savartar);
        phone = RequestBody.create(MediaType.parse("text/plain"), sphone);
        email = RequestBody.create(MediaType.parse("text/plain"), semail);


        if (file != null) {
            ApiUtils.getAPIService().updateProfile(id, name, address, partImage, phone, email).enqueue(new Callback<ResponeUser>() {
                @Override
                public void onResponse(Call<ResponeUser> call, Response<ResponeUser> response) {
                    myProgressDialog.cancel();
                    if (response.body().isSuccess(context)) {
                        if (response.body() != null) {
                            UserObj userObj = response.body().getData();
                            DataStoreManager.saveUser(userObj);
                            AppController.getInstance().setUserUpdated(true);
                            AppUtil.showToast(context, R.string.msg_update_success);

                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponeUser> call, Throwable t) {
                    myProgressDialog.cancel();
                    Log.e("UpdateProfileActivity", "onFailure: " + t.getMessage());
                }
            });
        } else {
            ApiUtils.getAPIService().updateProfile(id, name, address, avatar, phone, email).enqueue(new Callback<ResponeUser>() {
                @Override
                public void onResponse(Call<ResponeUser> call, Response<ResponeUser> response) {
                    myProgressDialog.cancel();
                    if (response.body().isSuccess(context)) {
                        if (response.body() != null) {
                            UserObj userObj = response.body().getData();
                            DataStoreManager.saveUser(userObj);
                            AppController.getInstance().setUserUpdated(true);
                            AppUtil.showToast(context, R.string.msg_update_success);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponeUser> call, Throwable t) {
                    myProgressDialog.cancel();
                    Log.e("UpdateProfile", "onFailure: " + t.getMessage());
                }
            });

        }
    }


    private String image = "";
    private File file;
    private static final String PARAM_AVATAR = "avatar";
    private MultipartBody.Part partImage = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("resultCode", "resultCode = " + resultCode);
        if (requestCode == AppUtil.PICK_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                AppUtil.setImageFromUri(img_avatar, data.getData());
                image = FileUtil.getPath(context, data.getData()).trim();
                Log.e("TAG", "onActivityResult: " + image);
                file = new File(image);
                parseMultipart();
            }
        }
    }

    private void parseMultipart() {
        if (!image.isEmpty()) {
            File fileImage1 = new File(image);
            RequestBody fileReqBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), fileImage1);
            partImage = MultipartBody.Part.createFormData(PARAM_AVATAR, fileImage1.getName(), fileReqBody1);
        }
    }
}
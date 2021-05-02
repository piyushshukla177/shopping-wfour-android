package com.wfour.onlinestoreapp.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wfour.onlinestoreapp.AppController;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.UserObj;

import org.json.JSONException;
import org.json.JSONObject;

public class ViewProfileActivity extends AppCompatActivity {

    ImageView btn_edit_avatar, img_avatar;
    Context context;
    TextView name_tv, email_tv, phone_number_tv, address_tv, member_number_tv,lbl_update_profile;
    ProgressBar progressbar;

    String name_text, email_text, avatar_url_text, member_id_text, addres_text, phone_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        init();
    }

    void init() {
        context = this;
        btn_edit_avatar = findViewById(R.id.btn_edit_avatar);
        img_avatar = findViewById(R.id.img_avatar);
        email_tv = findViewById(R.id.email_tv);
        name_tv = findViewById(R.id.name_tv);
        phone_number_tv = findViewById(R.id.phone_number_tv);
        address_tv = findViewById(R.id.address_tv);
        member_number_tv = findViewById(R.id.member_number_tv);
        progressbar = findViewById(R.id.progressbar);
        lbl_update_profile = findViewById(R.id.lbl_update_profile);
        btn_edit_avatar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, UpdateProfileActivity.class);
                        intent.putExtra("name_text",name_text);
                        intent.putExtra("email_text",email_text);
                        intent.putExtra("avatar_url_text",avatar_url_text);
                        intent.putExtra("member_id_text",member_id_text);
                        intent.putExtra("addres_text",addres_text);
                        intent.putExtra("phone_text",phone_text);
                        startActivity(intent);
                    }
                }
        );
        lbl_update_profile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, UpdateProfileActivity.class);
                        intent.putExtra("name_text",name_text);
                        intent.putExtra("email_text",email_text);
                        intent.putExtra("avatar_url_text",avatar_url_text);
                        intent.putExtra("member_id_text",member_id_text);
                        intent.putExtra("addres_text",addres_text);
                        intent.putExtra("phone_text",phone_text);
                        startActivity(intent);
                    }
                }
        );
        getProfile();
    }

    private void getProfile() {
        progressbar.setVisibility(View.VISIBLE);
        if (DataStoreManager.getUser() != null) {
            String id = DataStoreManager.getUser().getId();
            final String passWord = DataStoreManager.getUser().getPassWord();

            String address = DataStoreManager.getUser().getAddress();
            String phone = DataStoreManager.getUser().getPhone();
            String name = DataStoreManager.getUser().getName();

            ModelManager.getProfile(context, id, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    try {
                        JSONObject jsonObject = new JSONObject(object.toString());
                        ApiResponse response = new ApiResponse(jsonObject);
                        progressbar.setVisibility(View.GONE);

                        if (!response.isError()) {
                            try {
                                JSONObject obj = response.getDataObject();
                                name_tv.setText(obj.getString("name"));
                                email_tv.setText(obj.getString("email"));
                                phone_number_tv.setText(obj.getString("phone"));
                                address_tv.setText(obj.getString("address"));
                                member_number_tv.setText(obj.getString("id"));
                                if (!obj.getString("avatar").isEmpty()) {
                                    Picasso.with(context).load(obj.getString("avatar")).into(img_avatar);
                                }
                                name_text = obj.getString("name");
                                email_text = obj.getString("email");
                                addres_text = obj.getString("address");
                                member_id_text = obj.getString("id");
                                avatar_url_text = obj.getString("avatar");
                                phone_text = obj.getString("phone");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            Toast.makeText(context, response.getMessage(), Toast.LENGTH_SHORT).show();
                            progressbar.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        progressbar.setVisibility(View.GONE);

                        e.printStackTrace();
                    }
                }
                @Override
                public void onError() {

                    progressbar.setVisibility(View.GONE);
                }
            });
        }
    }
}
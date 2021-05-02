package com.wfour.onlinestoreapp.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.network.ApiResponse;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.OrderObj;
import com.wfour.onlinestoreapp.utils.NetworkUtility;

import org.json.JSONObject;

import java.util.ArrayList;

public class DeleteBillActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Spinner spinner;
    private TextView btnDelete;
    private OrderObj item;

    private TextView tvId, tvDate, tvStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_bill);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Delete Bill");

        tvDate = findViewById(R.id.tvDateTime);
        tvId = findViewById(R.id.tvId);
        tvStatus = findViewById(R.id.tvStatus);
        //spinner = findViewById(R.id.spinner);
        btnDelete = findViewById(R.id.btnNext);
        btnDelete.setOnClickListener(this);

        ArrayList<String> causeList = new ArrayList<String>();
        causeList.add("Ly do huy");
        causeList.add("Them/bot san pham");
        causeList.add("Dat trung");
        causeList.add("Thay doi dia chi giao hang");
        causeList.add("Doi hinh thuc thanh toan");
        causeList.add("Khac");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, causeList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(arrayAdapter);


//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView)parent.getChildAt(0)).setTextColor(Color.GRAY);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        getData();
    }
    public void getData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            item = bundle.getParcelable(Args.KEY_PRODUCT_OBJECT);

            tvId.setText(item.getId());
            tvDate.setText(item.getCreateDate());
            tvStatus.setText(getString(R.string.moving));
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    void inflateLayout() {

    }

    @Override
    void initUI() {

    }

    @Override
    void initControl() {

    }
    private void initOrderUpdate(){
        if(NetworkUtility.isNetworkAvailable()){
            String id = DataStoreManager.getUser().getId();
            Log.e("hihi", "item: "+ item.getId());
            ModelManager.orderUpdate(self, id, item.getId(), "cancel", new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject obj = (JSONObject) object;
                    ApiResponse response = new ApiResponse(obj);

                    Log.e("hihi", "Gson: "+ new Gson().toJson(response));
                    if(!response.isError()){
                        goToBillDetailActivity();
                    }
                }

                @Override
                public void onError() {

                }
            });

        }else {
            Toast.makeText(self, getString(R.string.msg_no_network), Toast.LENGTH_SHORT).show();

        }
    }
    public void goToBillDetailActivity(){
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v == btnDelete){
            initOrderUpdate();
//            Intent intent = new Intent();
//            setResult(Activity.RESULT_OK, intent);
//            finish();
        }
    }
}

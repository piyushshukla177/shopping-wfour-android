package com.wfour.onlinestoreapp.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wfour.onlinestoreapp.AppController;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.network.ApiResponse;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.CartObj;
import com.wfour.onlinestoreapp.objects.OrderDetailObj;
import com.wfour.onlinestoreapp.objects.OrderObj;
import com.wfour.onlinestoreapp.utils.NetworkUtility;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.view.adapters.Bill_Detail_InfoAdapter;
import com.wfour.onlinestoreapp.view.adapters.OrderDetailAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

public class BillDetailActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_CODE = 100;
    private TextView tvName, tvDevery, tvBill_id, tvStatus, tvDate, tvPhone, tvAllAddress, tvPayMethod, tvPay, tvTransportFee, tvShippingMethod, delivery,tv_delivery_charges;
    private Toolbar toolbar;
    private TextView btnDeleteBill;
    private Intent intent;
    private String order_id;
    private ArrayList<OrderDetailObj> orderDetailObjs;
    private OrderDetailObj orderDetailObj;
    private RecyclerView rclView,info_detailRcl;
    private OrderDetailAdapter mAdapter;
    private Bill_Detail_InfoAdapter bill_DetailAdapter;
    private OrderObj item;
    private int count;
 // private ArrayList<ProductObj> cartList;
    private ArrayList<CartObj> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);

        tvBill_id = findViewById(R.id.tvIdBill);
        tvName = findViewById(R.id.tvName);
        tvDate = findViewById(R.id.tvDateTime);
        tvAllAddress = findViewById(R.id.tvAllAddress);
        tvStatus = findViewById(R.id.tvStatus);
        tvPhone = findViewById(R.id.tvPhone);
        tvPayMethod = findViewById(R.id.tvPayMethod);
        tvPay = findViewById(R.id.tvPay);
        tvTransportFee = findViewById(R.id.tvTransportFee);
        tvShippingMethod = findViewById(R.id.tvShippingMethod);
        tvDevery = findViewById(R.id.tv_devery);
        delivery = findViewById(R.id.tv_delivery);
        tv_delivery_charges = findViewById(R.id.tv_delivery_charges);
        info_detailRcl = findViewById(R.id.info_detailRcl);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.bill_detail);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("bill", "bill");
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        btnDeleteBill = findViewById(R.id.btnDeleteBill);
        btnDeleteBill.setOnClickListener(this);


        getData();
        initOrderHistoryDetail();

        rclView = findViewById(R.id.contentRcl);
        mAdapter = new OrderDetailAdapter(self, orderDetailObjs,item.getStatus());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(self);
        rclView.setLayoutManager(layoutManager);
        rclView.setNestedScrollingEnabled(false);
        rclView.setAdapter(mAdapter);

        info_detailRcl = findViewById(R.id.info_detailRcl);
        bill_DetailAdapter = new Bill_Detail_InfoAdapter(self, orderDetailObjs);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(self);
        info_detailRcl.setLayoutManager(layoutManager1);
        info_detailRcl.setNestedScrollingEnabled(false);
        info_detailRcl.setAdapter(bill_DetailAdapter);

        Log.e("statuss",item.getStatus());
        String status = item.getStatus();
        if (item.getStatus().equals("2") || item.getStatus().equals("5") || item.getStatus().equals("1") || item.getStatus().equals("3") || "point".equals(item.getPaymentMethod())) {
            btnDeleteBill.setVisibility(View.GONE);
        } else {
            btnDeleteBill.setVisibility(View.VISIBLE);
        }
    }

    public void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            item = bundle.getParcelable(Args.KEY_PRODUCT_OBJECT);
            tvName.setText(item.getBillingName());
            tvPhone.setText(item.getBillingPhone());
            tvDate.setText(item.getCreateDate());
            tvAllAddress.setText(item.getBillingAddress());
            tvBill_id.setText(item.getBillCode());
            if (item.getTotal() >= 10.00 || "point".equals(item.getPaymentMethod())) {
                tvTransportFee.setText("0");
            } else {
                tvTransportFee.setText(StringUtil.convertNumberToString(item.getTransportFee(), 2));
            }
            if (!"Wfour Delivery(Free shipping for orders from $10)".equals(item.getTransportType())) {
                tvDevery.setText(R.string.msg_foti_iha);
                delivery.setText(R.string.msg_enderesu);
                tvName.setVisibility(View.GONE);
                tvPhone.setVisibility(View.GONE);
            } else {
                tvDevery.setText(R.string.metode_haruka);
                delivery.setText(R.string.enderesu_reseptor);
            }
            tvStatus.setText("Pjhdkjhsjdh");
            tvShippingMethod.setText(item.getTransportType());

            if (item.getStatus().equals("0")) {
                tvStatus.setText(getString(R.string.moving));
            } else if (item.getStatus().equals("1")) {
                tvStatus.setText(R.string.appoved);
            } else if (item.getStatus().equals("2")) {
                tvStatus.setText(AppController.getInstance().getString(R.string.canceled));
            } else if (item.getStatus().equals("3")) {
                tvStatus.setText(R.string.haruka_ona);
                if (item.getPaymentMethod().equals("point")) {
                    tvStatus.setText(R.string.troka_ona);
                }
            } else if (item.getStatus().equals("4")) {
                tvStatus.setText(AppController.getInstance().getString(R.string.not_paid));
            } else if (item.getStatus().equals("5")) {
                tvStatus.setText(R.string.delivery);
            } else {
                tvStatus.setText(AppController.getInstance().getString(R.string.rejected));
                if (item.getPaymentMethod().equals("point")) {
                    tvStatus.setText(R.string.troka_ona);
                }
            }

            if (item.getPaymentMethod().equals("cash")) {
                tvPayMethod.setText(getString(R.string.Cash_payment_on_delivery));
                return;
            } else {
                tvPayMethod.setText("Point");
            }
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cart, menu);
        MenuItem menuItem = menu.findItem(R.id.action_cart);
        menuItem.setIcon(GlobalFunctions.buildCounterDrawable(self, count, R.drawable.ic_cart));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_cart) {
            GlobalFunctions.startActivityWithoutAnimation(self, CartActivity.class);
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalFunctions.getCountCart(cartList, count);
        count = DataStoreManager.getCountCart();
        invalidateOptionsMenu();
    }


    public void initOrderHistoryDetail() {
        if (NetworkUtility.isNetworkAvailable()) {
            String id = DataStoreManager.getUser().getId();
            ModelManager.orderHistoryDetail(self, id, item.getId(), new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject obj = (JSONObject) object;
                    ApiResponse response = new ApiResponse(obj);
                    orderDetailObjs = new ArrayList<>();
                    if (!response.isError()) {
                        orderDetailObjs.addAll(response.getDataList(OrderDetailObj.class));

                        setPay();

                        mAdapter.addList(orderDetailObjs);
                        bill_DetailAdapter.addList(orderDetailObjs);
                    }
                }

                @Override
                public void onError() {

                }
            });
        } else {
            Toast.makeText(self, getString(R.string.msg_no_network), Toast.LENGTH_SHORT).show();

        }
    }

    private void setPay() {
        double temp = 0;
        for (int i = 0; i < orderDetailObjs.size(); i++) {
            orderDetailObj = orderDetailObjs.get(i);
            temp += orderDetailObj.getSubTotal();
            Log.e("BillDetailActivity", "setPay: " + temp);
        }
        double pay = temp + item.getTransportFee();
        tv_delivery_charges.setText(String.valueOf(item.getTransportFee()));
        //pay = Math.round(pay*100)/100.00d;
        if (temp >= 10) {
            if (item.getPaymentMethod().equals("point")) {
                tvPay.setText(" P." + StringUtil.convertNumberToString(temp, 2));
            } else {
                tvPay.setText(" $" + StringUtil.convertNumberToString(temp, 2));
            }

        } else {
            if (item.getPaymentMethod().equals("point")) {
                tvPay.setText(" P." + StringUtil.convertNumberToString(pay, 2));
            } else {
                tvPay.setText(" $" + StringUtil.convertNumberToString(pay, 2));
            }

        }
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


    @Override
    public void onClick(View v) {
        if (v == btnDeleteBill) {
            Intent intent = new Intent(self, DeleteBillActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(Args.KEY_PRODUCT_OBJECT, item);
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                btnDeleteBill.setVisibility(View.GONE);
                tvStatus.setText("Cancelled");
            }
        }
    }
}

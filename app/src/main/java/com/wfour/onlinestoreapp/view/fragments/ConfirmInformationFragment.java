package com.wfour.onlinestoreapp.view.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.base.BaseFragment;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.network.modelmanager.singletonmanager.AddressManager;
import com.wfour.onlinestoreapp.network.modelmanager.singletonmanager.CartManager;
import com.wfour.onlinestoreapp.network1.NetworkUtility;
import com.wfour.onlinestoreapp.objects.CartObj;
import com.wfour.onlinestoreapp.objects.DeliveryObj;
import com.wfour.onlinestoreapp.objects.Person;
import com.wfour.onlinestoreapp.objects.ProductObj;
import com.wfour.onlinestoreapp.quickblox.conversation.utils.DialogUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.view.activities.BillInformationActivity;
import com.wfour.onlinestoreapp.view.activities.BillTakingActivity;
import com.wfour.onlinestoreapp.view.activities.CartActivity;
import com.wfour.onlinestoreapp.view.activities.MainActivity;
import com.wfour.onlinestoreapp.view.adapters.OrderAdapter;

import org.json.JSONObject;

import java.util.ArrayList;


public class ConfirmInformationFragment extends BaseFragment implements View.OnClickListener {

    public static final String DELIVERY = "Delivery";

    private TextView tvPay, tvName, tvPhone, tvAllAddress, btnConfirm, tvMoney, btnNext, tvPrice, tvDelivery, tvAdres;
    private Bundle bundle;
    private CartActivity mCartActivity;
    private Toolbar toolbar;
    //    private ArrayList<ProductObj> cartObjList;
    private ArrayList<CartObj> cartObjList;
    private RecyclerView contentRcl;
    private OrderAdapter pAdapter;
    private ArrayList<Person> personList;
    private Person person;
    private double total, transportFee;
    private String items;
    private String allAddress;
    private String billName;
    private String billPhone, transportType;
    private ArrayList<DeliveryObj> deliveryObjList;
    private DeliveryObj deliveryObj;
    private String delivery;
    private int count;
    private ArrayList<ProductObj> cartList;
    private CartListFragment cartListFragment;
    private String color, size;
    private String a;
    private String billCode;
    private String address;
    private TextView tvInfo, tvNameDevery;

    public static ConfirmInformationFragment newInstance() {
        Bundle bundle = new Bundle();
        ConfirmInformationFragment fragment = new ConfirmInformationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_confirm_information;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        setHasOptionsMenu(true);
        tvName = view.findViewById(R.id.tvName);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvAllAddress = view.findViewById(R.id.tvAllAddress);
        tvPay = view.findViewById(R.id.tvPay);
        tvMoney = view.findViewById(R.id.tvMoney);
        btnNext = view.findViewById(R.id.btnConfirm);
        toolbar = view.findViewById(R.id.toolbar);
        tvPrice = view.findViewById(R.id.tvTransportFee);
        tvDelivery = view.findViewById(R.id.tvDelivery);
        tvAdres = view.findViewById(R.id.tvAddress);
        tvInfo = view.findViewById(R.id.tv_info);
        tvNameDevery = view.findViewById(R.id.tvNameDelivery);


        btnNext.setOnClickListener(this);

        mCartActivity = ((CartActivity) getActivity());

        mCartActivity.setSupportActionBar(toolbar);
        mCartActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCartActivity.setTitle(R.string.confirmInformation);

        initData();

        contentRcl = view.findViewById(R.id.contentRcl);
        pAdapter = new OrderAdapter(mCartActivity, cartObjList);
        contentRcl.setLayoutManager(new GridLayoutManager(self, 1));
        contentRcl.setNestedScrollingEnabled(false);
        contentRcl.setAdapter(pAdapter);

    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            deliveryObj = bundle.getParcelable(Args.KEY_DELIVERY_OBJECT);
            Log.e("initData", "initData: " + deliveryObj.getAddress());
            String text = bundle.getString(SoloveFragment.TEXT);
            if (CartManager.getInstance().getTotal() < 10.0 && "Wfour Delivery".equals(deliveryObj.getName()) && CartManager.getInstance().getTotal() > 0.0) {
                tvPrice.setText("1.0");
            } else if (CartManager.getInstance().getTotal() >= 10.0 && "Wfour Delivery".equals(deliveryObj.getName()) || CartManager.getInstance().getTotal() == 0.0) {
                tvPrice.setText("0");
            } else if (!"Wfour Delivery".equals(deliveryObj.getName())) {
                tvPrice.setText("0");
            }
            tvPay.setText(text);
            //tvPrice.setText(StringUtil.convertNumberToString(deliveryObj.getPrice(), 2));
            transportFee = deliveryObj.getPrice();
            transportType = deliveryObj.getName() + "(" + deliveryObj.getDescription() + ")";
            address = deliveryObj.getAddress();
            delivery = deliveryObj.getName() + " (" + deliveryObj.getDescription() + ")";
            if (CartManager.getInstance().getTotal() >= 10.00 && "Wfour Delivery".equals(deliveryObj.getName())) {
                tvDelivery.setText("Wfour Delivery (Above $10)");
            } else if (CartManager.getInstance().getTotal() < 10.00 && "Wfour Delivery".equals(deliveryObj.getName())) {
                tvDelivery.setText("Wfour Delivery (Bellow $10)");
            } else if (!"Wfour Delivery".equals(deliveryObj.getName())) {
                tvDelivery.setText(deliveryObj.getDescription());
            }
            a = tvPrice.getText().toString();

        } else {

        }

        cartObjList = CartManager.getInstance().getArray();
        if (cartObjList != null) {
            Double.parseDouble(a);
            double money = CartManager.getInstance().getTotal();
            total = money + Double.parseDouble(tvPrice.getText().toString());

            tvMoney.setText(StringUtil.convertNumberToString(total, 2));
            Gson gson = new Gson();
            items = gson.toJson(cartObjList);
            Log.e("hihi", "initData: " + new Gson().toJson(items));

        }

        personList = AddressManager.getInstance().getArray();
        if (personList != null && "Wfour Delivery".equals(deliveryObj.getName())) {
            person = personList.get(0);
            allAddress = person.getAddress();
            billName = person.getName();
            billPhone = person.getPhone();

            tvName.setText(billName);
            tvPhone.setText(billPhone);
            tvAllAddress.setText(allAddress);
            tvAdres.setVisibility(View.GONE);
            tvInfo.setText(R.string.enderesu_reseptor);
            tvNameDevery.setText(R.string.metode_haruka);
        } else if (!"Wfour Delivery".equals(deliveryObj.getName()) && personList != null) {
            person = personList.get(0);
            allAddress = deliveryObj.getAddress();
            billName = person.getName();
            billPhone = person.getPhone();
            tvName.setVisibility(View.GONE);
            tvPhone.setVisibility(View.GONE);
            tvAllAddress.setText(deliveryObj.getAddress());
            tvInfo.setText(R.string.msg_enderesu);
            tvNameDevery.setText(R.string.msg_foti_iha);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_go_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_go_home) {
            GlobalFunctions.startActivityWithoutAnimation(self, MainActivity.class);
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void orderProduct() {
        if (NetworkUtility.getInstance(self).isNetworkAvailable()) {
            final String user_id = DataStoreManager.getUser().getId();
            if (CartManager.getInstance().getTotalPoint() > 0) {
                ModelManager.orderProduct(self, user_id, billName, billPhone, allAddress, CartManager.getInstance().getTotalPoint(), "point", items, transportFee, transportType, new ModelManagerListener() {
                    @Override
                    public void onSuccess(Object object) {
                        try {
                            JSONObject jsonObject = new JSONObject(object.toString());
                            ApiResponse response = new ApiResponse(jsonObject);

                            if (!response.isError()) {
                                //OrderObj orderObj = response.getDataObject(OrderObj.class);
                                billCode = response.getDataObject().get("billingCode").toString();

                                if ("Wfour Delivery".equals(deliveryObj.getName())) {
                                    goBillInformationActivity();
                                } else if (!"Wfour Delivery".equals(deliveryObj.getName())) {
                                    goBillTakingActivity();
                                }

                            } else {
                                Toast.makeText(self, "Cannot creat order", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e(DELIVERY, "onSuccess: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError() {
                        Log.e(DELIVERY, "onError: ERROR");
                    }
                });
            } else {
                ModelManager.orderProduct(self, user_id, billName, billPhone, allAddress, total, "cash", items, transportFee, transportType, new ModelManagerListener() {
                    @Override
                    public void onSuccess(Object object) {
                        try {
                            JSONObject jsonObject = new JSONObject(object.toString());
                            ApiResponse response = new ApiResponse(jsonObject);

                            if (!response.isError()) {
                                //OrderObj orderObj = response.getDataObject(OrderObj.class);
                                billCode = response.getDataObject().get("billingCode").toString();

                                if ("Wfour Delivery".equals(deliveryObj.getName())) {
                                    goBillInformationActivity();
                                } else if (!"Wfour Delivery".equals(deliveryObj.getName())) {
                                    goBillTakingActivity();
                                }

                            } else {
                                Toast.makeText(self, "Cannot creat order", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e(DELIVERY, "onSuccess: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError() {
                        Log.e(DELIVERY, "onError: ERROR");
                    }
                });
            }

        } else {
            Toast.makeText(self, getString(R.string.msg_no_network), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void getData() {
    }

    private void goBillInformationActivity() {
        Log.e(DELIVERY, "onSuccess: " + billCode);
        Bundle bundle = new Bundle();
        bundle.putString(DELIVERY, delivery);
        bundle.putString("billCode", billCode);
        GlobalFunctions.startActivityWithoutAnimation(self, BillInformationActivity.class, bundle);
        cartObjList.clear();
        DataStoreManager.clearCountCart();
        pAdapter.notifyDataSetChanged();
        getActivity().finish();
    }

    private void goBillTakingActivity() {
        Bundle bundle = new Bundle();
        bundle.putString(DELIVERY, address);
        bundle.putString("billCode", billCode);
        GlobalFunctions.startActivityWithoutAnimation(self, BillTakingActivity.class, bundle);
        cartObjList.clear();
        DataStoreManager.clearCountCart();
        pAdapter.notifyDataSetChanged();
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {
        if (v == btnNext) {
            showDialogInformation();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(DELIVERY, "onDestroy: ");

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(DELIVERY, "onStart: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(DELIVERY, "onStop: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(DELIVERY, "onResume: ");
    }

    private void showDialogInformation() {
        final Dialog dialogOrder = DialogUtil.setDialogCustomView(self, R.layout.payment_confirmation_layout, false);
        //dialogOrder.setTitle("hehe");

        personList = AddressManager.getInstance().getArray();
        TextView btn_ok = dialogOrder.findViewById(R.id.btn_ok);


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderProduct();
                dialogOrder.dismiss();
            }
        });
        dialogOrder.show();
    }
}

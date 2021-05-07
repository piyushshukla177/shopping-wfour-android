package com.wfour.onlinestoreapp.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.base.BaseFragment;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.MyOnClickDelivery;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.network1.NetworkUtility;
import com.wfour.onlinestoreapp.objects.DeliveryObj;
import com.wfour.onlinestoreapp.objects.ProductObj;
import com.wfour.onlinestoreapp.view.activities.CartActivity;
import com.wfour.onlinestoreapp.view.activities.MainActivity;
import com.wfour.onlinestoreapp.view.adapters.DeliveryAdapter;

import org.json.JSONObject;

import java.util.ArrayList;


public class SoloveFragment extends BaseFragment {

    public static final String TEXT = "text";
    protected final static String TAG = SoloveFragment.class.getSimpleName();

    private RadioButton rdbMoney, rdbBankCard;
    private RadioGroup radioGroup;
    private TextView btnNext, tvDescription, tvPrice, tvName;
    private String text;
    private Toolbar toolbar;
    private CartActivity mCartActivity;

    private ArrayList<DeliveryObj> deliveryObjList;
    private DeliveryObj deliveryObj = new DeliveryObj();
    //    private RecyclerView rcv_data;
    private DeliveryAdapter mAdapter;
    private Bundle bundle = new Bundle();
    private int count;
    private ArrayList<ProductObj> cartList;
    private CartListFragment cartListFragment;
    private ArrayList<DeliveryObj> list;
    private View view;
    private String txt;

    private ConfirmInformationFragment confirmInformationFragment;

    public static SoloveFragment newInstance() {
        Bundle bundle = new Bundle();
        SoloveFragment fragment = new SoloveFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_solove;
    }

    @Override
    protected void init() {

    }

    String address;

    @Override
    protected void initView(View view) {
        setHasOptionsMenu(true);
        rdbMoney = view.findViewById(R.id.RdbMoney);
        rdbBankCard = view.findViewById(R.id.RdbBankCard);
        btnNext = view.findViewById(R.id.btnNext);
        toolbar = view.findViewById(R.id.toolbar);
        radioGroup = view.findViewById(R.id.radioGroup);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvName = view.findViewById(R.id.tvName);
        mCartActivity = ((CartActivity) getActivity());
//        rcv_data = view.findViewById(R.id.rcvData);

        mCartActivity.setSupportActionBar(toolbar);
        mCartActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCartActivity.setTitle("Metode Pagamentu");

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //doOnDifficultyLevelChanged(radioGroup,i);
            }
        });
        initDeliveryList();
        mAdapter = new DeliveryAdapter(self, deliveryObjList, new MyOnClickDelivery() {

            @Override
            public void onClick(int position) {
                deliveryObj = new DeliveryObj();
                deliveryObj = deliveryObjList.get(position);
            }
        });
        Bundle bundle1 = getArguments();
        deliveryObj = bundle1.getParcelable(Args.KEY_DELIVERY_OBJECT);

//
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(self);
//        rcv_data.setLayoutManager(layoutManager);
//        rcv_data.setAdapter(mAdapter);
//        rcv_data.setNestedScrollingEnabled(false);
//        mAdapter.notifyDataSetChanged();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!deliveryObjList.get(0).isSelected()
//                        && !deliveryObjList.get(1).isSelected()
//                        && !deliveryObjList.get(2).isSelected()
//                        && !deliveryObjList.get(3).isSelected()) {
//                    Toast.makeText(self, "Choose shipping method", Toast.LENGTH_LONG).show();
//                    return;
//                }
                if (!rdbBankCard.isChecked() && !rdbMoney.isChecked()) {

                } else {
                    if (deliveryObj != null) {
                        if (rdbMoney.isChecked()) {
                            text = "Selu Iha Fatin";
                        } else if (rdbBankCard.isChecked()) {

                        }
                        setData();
                    } else {
                        Toast.makeText(self, "Item cannot be empty", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }

    private void setData() {
        Bundle bundle = new Bundle();
//        deliveryObj = new DeliveryObj();
//        for(int i= 0; i< deliveryObjList.size(); i++){
//            if(deliveryObjList.get(i).isSelected()){
//                deliveryObj = deliveryObjList.get(i);
//            }
//        }
        bundle.putParcelable(Args.KEY_DELIVERY_OBJECT, deliveryObj);
        bundle.putString(TEXT, text);
        FragmentTransaction fragmentTransaction = ((CartActivity) getActivity()).getSupportFragmentManager().beginTransaction();
        if (confirmInformationFragment == null) {
            confirmInformationFragment = ConfirmInformationFragment.newInstance();
        }
        confirmInformationFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.frgCart, confirmInformationFragment).addToBackStack("ConfirmInformationFragment").commit();
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void getData() {
    }


    private void initDeliveryList() {
        if (NetworkUtility.getInstance(self).isNetworkAvailable()) {
            ModelManager.deliveryList(self, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject obj = (JSONObject) object;
                    ApiResponse response = new ApiResponse(obj);
                    deliveryObjList = new ArrayList<>();
                    if (!response.isError()) {
                        deliveryObjList.addAll(response.getDataList(DeliveryObj.class));


//                        deliveryObj = deliveryObjList.get(0);
//                        deliveryObj = deliveryObjList.get(1);
//                        deliveryObj = deliveryObjList.get(2);
//                        deliveryObj = deliveryObjList.get(3);

                        mAdapter.addList(deliveryObjList);
                    }
                    Log.e(TAG, "onError: " + "Success");
                }

                @Override
                public void onError() {
                    Log.e(TAG, "onError: " + "Error");
                }
            });
        } else {
            Toast.makeText(self, getString(R.string.msg_no_network), Toast.LENGTH_SHORT).show();

        }
    }

    private void doOnDifficultyLevelChanged(RadioGroup group, int checkedId) {
        int checkedRadioId = group.getCheckedRadioButtonId();

        if (checkedRadioId == R.id.RdbMoney) {
            //Toast.makeText(mCartActivity, "ban chon 1 ", Toast.LENGTH_LONG).show();
        } else if (checkedRadioId == R.id.RdbBankCard) {
            //Toast.makeText(mCartActivity, "ban chon 2 ", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
    }
}

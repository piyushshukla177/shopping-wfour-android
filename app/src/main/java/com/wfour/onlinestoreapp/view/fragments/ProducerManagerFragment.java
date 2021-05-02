package com.wfour.onlinestoreapp.view.fragments;

import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.*;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.DealObj;
import com.wfour.onlinestoreapp.objects.ReservationObj;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.NetworkUtility;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.view.activities.DealsActivity;
import com.wfour.onlinestoreapp.view.activities.MainActivity;
import com.wfour.onlinestoreapp.view.activities.NewDealActivity;
import com.wfour.onlinestoreapp.view.activities.ProductManagerActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suusoft on 11/17/2017.
 */

public class ProducerManagerFragment extends com.wfour.onlinestoreapp.base.BaseFragment implements View.OnClickListener {

    private String TAG = ProducerManagerFragment.class.getSimpleName();

    private TextView tvAvailableNow, tvEarn,tvDealSold;
    private RelativeLayout btnGotoProduct, btnGotoSold, btnNewDeal;
    private FragmentTransaction fragmentTransaction;
    private ArrayList<DealObj> dealObjs;
    private double earn =0;
    private List<ReservationObj> reservationList;

    public static ProducerManagerFragment newInstance() {

        Bundle args = new Bundle();

        ProducerManagerFragment fragment = new ProducerManagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_producer_manager;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        tvAvailableNow      = (TextView) view.findViewById(R.id.tv_credits);
        tvEarn          = (TextView) view.findViewById(R.id.tv_earn_credits);
        tvDealSold          = (TextView) view.findViewById(R.id.tv_count_deal_sold);
        btnGotoProduct      = (RelativeLayout) view.findViewById(R.id.btn_go_to_product);
        btnGotoSold         = (RelativeLayout) view.findViewById(R.id.btn_go_to_sold);
        btnNewDeal         = (RelativeLayout) view.findViewById(R.id.btn_new_deal);

        btnGotoSold.setOnClickListener(this);
        btnGotoProduct.setOnClickListener(this);
        btnNewDeal.setOnClickListener(this);

    }

    @Override
    protected void getData() {
        tvAvailableNow.setText(StringUtil.convertNumberToString(DataStoreManager.getUser().getBalance(), 1));

        getInforSold();

    }

    private void getInforSold() {
        reservationList = new ArrayList<>();
        if (NetworkUtility.isNetworkAvailable()){
            earn = 0;
            ModelManager.getReservationList(self, Constants.SOLD, "", 1, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    Log.e("ReservationListFragment", "onSuccess " + object.toString() );
                    JSONObject jsonObject = (JSONObject) object;
                    ApiResponse response = new ApiResponse(jsonObject);
                    if (!response.isError()) {
                        Log.e("ReservationListFragment", "onSuccess response success" );
                        reservationList = response.getDataList(ReservationObj.class);
                        for (ReservationObj item : reservationList) {
                            earn = earn + item.getDeal().getPrice();
                            Log.e("ReservationListFragment", "add" + item.getDeal().toString() );
                        }
                        tvEarn.setText(StringUtil.convertNumberToString(earn, 1));

                    } else {
                    }

                    tvDealSold.setText(reservationList.size() + "");
                    Log.e(TAG, "reservationList.size() = "  +  reservationList.size() );


                }

                @Override
                public void onError() {
                    AppUtil.showToast(self, R.string.msg_have_some_errors);
                }
            });

//            ModelManager.getDealList(self, null, null, "", Constants.SOLD, null, null, AppController.getInstance().getLatMyLocation(),
//                    AppController.getInstance().getLongMyLocation(), null, null, 1, new ModelManagerListener() {
//                        @Override
//                        public void onSuccess(Object object) {
//
//                            Log.e(TAG, "onSuccess " + object.toString() );
//                            JSONObject jsonObject = (JSONObject) object;
//                            ApiResponse response = new ApiResponse(jsonObject);
//                            DealObj dealObj;
//                            if (!response.isError()) {
//                                Log.e(TAG, "onSuccess response success" );
//                                dealObjs.addAll(response.getDataList(DealObj.class));
//                                for (int i = 0; i < dealObjs.size(); i++){
//                                    earn = earn + dealObjs.get(i).getPrice();
//                                }
//
//                                tvEarn.setText(StringUtil.convertNumberToString(earn, 1));
//
//                            } else {
//
//                            }
//
//                            tvDealSold.setText(dealObjs.size() + "");
//                            Log.e(TAG, "dealObjs.size() = "  +  dealObjs.size() );
//
//                        }
//
//                        @Override
//                        public void onError() {
//                            tvDealSold.setText(dealObjs.size() + "");
//                        }
//                    });
        }else {
            AppUtil.showToast(self, R.string.msg_network_not_available);
        }

    }

    @Override
    public void onClick(View v) {
        if (v==btnGotoProduct){

            AppUtil.startActivity(self, ProductManagerActivity.class);
//            fragmentTransaction = ((MainActivity) self).getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.frl_main, SellerFragment.newInstance()).addToBackStack("SellerFragment").commit();
//            ((MainActivity) self).setTitle(R.string.producer_manager);
        }else if (v==btnGotoSold){
            Bundle bundle = new Bundle();
            bundle.putString(Args.TYPE_OF_SEARCH_DEAL, Constants.SOLD);

            GlobalFunctions.startActivityWithoutAnimation(self, DealsActivity.class, bundle);

//            Bundle bundle0 = new Bundle();
//            bundle0.putString(Args.TYPE_OF_SEARCH_DEAL, Constants.SOLD);
//            fragmentTransaction = ((MainActivity) self).getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.frl_main, ReservationListFragment.newInstance(bundle0)).addToBackStack("ReservationListFragment").commit();
//            ((MainActivity) self).setTitle(R.string.sold);
        } else if (v==btnNewDeal){
            AppUtil.startActivity(self, NewDealActivity.class);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            ((MainActivity) self).getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

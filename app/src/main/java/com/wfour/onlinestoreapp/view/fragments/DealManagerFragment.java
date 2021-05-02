package com.wfour.onlinestoreapp.view.fragments;

import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.view.activities.MainActivity;
import com.wfour.onlinestoreapp.widgets.textview.TextViewBold;

/**
 * Created by Suusoft on 11/01/2017.
 */

public class DealManagerFragment extends com.wfour.onlinestoreapp.base.BaseFragment implements View.OnClickListener {

    private TextViewBold btnBuyer, btnSeller;
    private MyDealFragment mFrgMyDeal;
    private DealsFragment mFrgDeal;

    public static DealManagerFragment newInstance() {
        Bundle args = new Bundle();
        DealManagerFragment fragment = new DealManagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_deal_manager2;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        btnBuyer = (TextViewBold) view.findViewById(R.id.btn_buyer);
        btnSeller = (TextViewBold) view.findViewById(R.id.btn_producer);

        btnBuyer.setOnClickListener(this);
        btnSeller.setOnClickListener(this);
    }

    @Override
    protected void getData() {
        ((MainActivity)self).setTitle(R.string.deal_manager );
    }

    @Override
    public void onClick(View v) {
        final FragmentTransaction fragmentTransaction = ((MainActivity)self).getSupportFragmentManager().beginTransaction();
        if (v==btnBuyer){

            if (mFrgMyDeal == null) {
                mFrgMyDeal =MyDealFragment.newInstance();
            }
            fragmentTransaction.replace(R.id.frl_main, mFrgMyDeal).addToBackStack("mFrgMyDeal").commit();

            ((MainActivity)self).setTitle(R.string.buy_deals);

        } else if (v==btnSeller){
            if (mFrgDeal == null) {
                mFrgDeal = DealsFragment.newInstance();
            }
            fragmentTransaction.replace(R.id.frl_main, mFrgDeal).addToBackStack("mFrgDeal").commit();

            ((MainActivity)self).setTitle(R.string.product_management);

        }
    }
}

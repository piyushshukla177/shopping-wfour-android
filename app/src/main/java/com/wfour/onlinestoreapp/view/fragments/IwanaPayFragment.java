package com.wfour.onlinestoreapp.view.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.view.activities.BuyCreditsActivity;
import com.wfour.onlinestoreapp.view.activities.HistoryActivity;
import com.wfour.onlinestoreapp.view.activities.RedeemActivity;
import com.wfour.onlinestoreapp.view.activities.TransferActivity;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

/**
 * Created by Suusoft on 30/11/2016.
 */

public class IwanaPayFragment extends BaseFragment implements View.OnClickListener {
    private TextViewRegular btnBuyCredits, btnRedeem, btnTranfer, btnHistories;

    public static IwanaPayFragment newInstance() {

        Bundle args = new Bundle();

        IwanaPayFragment fragment = new IwanaPayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    View inflateLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_iwana_pay, container, false);
    }

    @Override
    void initUI(View view) {

        btnBuyCredits = (TextViewRegular) view.findViewById(R.id.btn_buy_credits);
        btnHistories = (TextViewRegular) view.findViewById(R.id.btn_history);
        btnRedeem = (TextViewRegular) view.findViewById(R.id.btn_functions);
        btnTranfer = (TextViewRegular) view.findViewById(R.id.btn_transfer);
    }

    @Override
    void initControl() {
        btnTranfer.setOnClickListener(this);
        btnRedeem.setOnClickListener(this);
        btnHistories.setOnClickListener(this);
        btnBuyCredits.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnBuyCredits) {
            AppUtil.startActivity(getActivity(), BuyCreditsActivity.class);
        } else if (view == btnHistories) {
            AppUtil.startActivity(getActivity(), HistoryActivity.class);
        } else if (view == btnRedeem) {
            AppUtil.startActivity(getActivity(), RedeemActivity.class);
        } else if (view == btnTranfer) {
            AppUtil.startActivity(getActivity(), TransferActivity.class);
        }
    }
}

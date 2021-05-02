package com.wfour.onlinestoreapp.view.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.objects.RecentChatObj;

/**
 * Created by Suusoft on 11/07/2017.
 */

public class FragmentChidChatActionDeal extends com.wfour.onlinestoreapp.base.BaseFragment implements View.OnClickListener {

    private TextView btnDeal, btnNoDeal;
    private RecentChatObj mRecentChatObj;
    private Dialog mPriceConfirmationDialog;
    private IActionDeal iActionDeal;


    public interface IActionDeal{
        void onDeal();
        void onNoDeal();
    }

    public static FragmentChidChatActionDeal newInstance(RecentChatObj mRecentChatObj, IActionDeal iActionDeal) {
        Bundle args = new Bundle();
        FragmentChidChatActionDeal fragment = new FragmentChidChatActionDeal();
        fragment.setArguments(args);
        fragment.mRecentChatObj = mRecentChatObj;
        fragment.iActionDeal = iActionDeal;
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_child_chat_action_deal;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        btnDeal  = (TextView) view.findViewById(R.id.btn_deal);
        btnNoDeal   = (TextView) view.findViewById(R.id.btn_no_deal);

        btnNoDeal.setOnClickListener(this);
        btnDeal.setOnClickListener(this);
    }

    @Override
    protected void getData() {

    }

    @Override
    public void onClick(View v) {
        if(v==btnDeal){
            iActionDeal.onDeal();
        }else if(v==btnNoDeal){
            iActionDeal.onNoDeal();
        }
    }



}

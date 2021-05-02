package com.wfour.onlinestoreapp.view.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.objects.RecentChatObj;

/**
 * Created by Suusoft on 11/09/2017.
 */

public class FragmentChidChatActionReviewDeal extends com.wfour.onlinestoreapp.base.BaseFragment implements View.OnClickListener {

    private TextView btnReview;
    private  IActionReview iActionReview;

    @Override
    public void onClick(View v) {
        if (iActionReview!=null)
            iActionReview.onReview();
    }

    public interface IActionReview {
        void onReview();
    }


    public static FragmentChidChatActionReviewDeal newInstance(RecentChatObj mRecentChatObj , IActionReview iActionReview ) {
        Bundle args = new Bundle();
        FragmentChidChatActionReviewDeal fragment = new FragmentChidChatActionReviewDeal();
        fragment.setArguments(args);
        fragment.iActionReview = iActionReview;
        return fragment;
    }
    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_child_chat_action_review_deal;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        btnReview = (TextView) view.findViewById(R.id.btn_view_reviews);
        btnReview.setOnClickListener(this);
    }

    @Override
    protected void getData() {

    }
}

package com.wfour.onlinestoreapp.view.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.widgets.textview.TextViewBold;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

/**
 * Created by Suusoft on 24/12/2016.
 */

public class GuideFragment extends com.wfour.onlinestoreapp.base.BaseFragment {
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private TextViewBold tvTitle;
    private TextViewRegular tvDescriptions;
    private RelativeLayout rltParent;


    public static GuideFragment newInstance(String title, String description) {

        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(DESCRIPTION, description);
        GuideFragment fragment = new GuideFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_guide;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        tvTitle = (TextViewBold) view.findViewById(R.id.tv_title);
        tvDescriptions = (TextViewRegular) view.findViewById(R.id.tv_description);
        rltParent = (RelativeLayout) view.findViewById(R.id.rlt_parent);
    }

    @Override
    protected void getData() {
        Bundle bundle = getArguments();
        String title = bundle.getString(TITLE);

        if (title.equals(getString(R.string.find_deals))) {
            rltParent.setBackgroundResource(R.drawable.background_find_deal);
        } else if (title.equals(getString(R.string.iwanachat))) {
            rltParent.setBackgroundResource(R.drawable.background_chat);
        } else if (title.equals(getString(R.string.vip))) {
            rltParent.setBackgroundResource(R.drawable.background_chat);
        }

        tvTitle.setText(title);
        tvDescriptions.setText(bundle.getString(DESCRIPTION));

    }


}

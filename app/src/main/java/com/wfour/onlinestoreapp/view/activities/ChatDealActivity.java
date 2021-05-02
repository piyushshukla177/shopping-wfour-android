package com.wfour.onlinestoreapp.view.activities;

import android.content.Intent;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;

import com.wfour.onlinestoreapp.R;

/**
 * Created by Suusoft on 10/13/2017.
 */

public class ChatDealActivity extends com.wfour.onlinestoreapp.base.BaseActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NONE;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_chat_deal;
    }

    @Override
    protected void getExtraData(Intent intent) {

    }

    @Override
    protected void initilize() {

    }

    @Override
    protected void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    @Override
    protected void onViewCreated() {

    }
}

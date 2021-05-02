package com.wfour.onlinestoreapp.view.activities;

import android.content.Intent;
import androidx.fragment.app.FragmentTransaction;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.view.fragments.AllDealsFragment;
import com.wfour.onlinestoreapp.globals.Args;

/**
 * Created by Suusoft on 12/20/2016.
 */

public class AllCategoryActivity extends com.wfour.onlinestoreapp.base.BaseActivity {


    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_category_list;
    }

    @Override
    protected void getExtraData(Intent intent) {

    }

    @Override
    protected void initilize() {

    }

    @Override
    protected void initView() {
        setToolbarTitle(R.string.all_deals);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, AllDealsFragment.newInstance(Args.TYPE_OF_CATEGORY_ALL));
        transaction.commit();
    }

    @Override
    protected void onViewCreated() {

    }
}

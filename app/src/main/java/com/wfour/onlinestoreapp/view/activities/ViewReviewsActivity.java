package com.wfour.onlinestoreapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.view.fragments.DealReviewFragment;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.objects.UserObj;

/**
 * Created by Suusoft on 02/12/2016.
 */

public class ViewReviewsActivity extends com.wfour.onlinestoreapp.base.BaseActivity {
    private static String TAG = ViewReviewsActivity.class.getSimpleName();
    public static final int RESULT_CODE_VIEW_REVIEWS = 1234;

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_view_reviews;
    }

    @Override
    protected void getExtraData(Intent intent) {

    }

    @Override
    protected void initilize() {
        setToolbarTitle(getString(R.string.view_reviews));
        Log.e(TAG, "initilize setToolbarTitle ");
    }

    @Override
    protected void initView() {


    }

    @Override
    protected void onViewCreated() {
        Bundle bundle = getIntent().getExtras();
        String id = null;
        String name = null;
        String i_am = "";
        if (bundle != null) {
            Log.e(TAG, "onViewCreated bundle != null ");
            if (bundle.containsKey(Args.USER_ID)) {
                id = bundle.getString(Args.USER_ID);
                name = bundle.getString(Args.NAME_DRIVER);
                i_am = bundle.getString(Args.I_AM);
                if (i_am.equals(Constants.BUYER) || i_am.equals(Constants.PASSENGER)) {
                    if (i_am.equals(Constants.BUYER)) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.ll_parent_content, DealReviewFragment.newInstance(id, Constants.PRO, "")).commit();
                    } else if (i_am.equals(Constants.PASSENGER)){
                        getSupportFragmentManager().beginTransaction().replace(R.id.ll_parent_content, DealReviewFragment.newInstance(id, Constants.PRO, "")).commit();
                    }

                } else if (i_am.equals(Constants.SELLER) || i_am.equals(Constants.DRIVER)) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.ll_parent_content, DealReviewFragment.newInstance(id, Constants.BASIC, "")).commit();
                }

            } else {
                Log.e(TAG, "onViewCreated bundle == null ");
                UserObj userObj = DataStoreManager.getUser();
                id = userObj.getId();
                name = userObj.getName();
                Log.e(TAG, "onViewCreated UserObj = " + new Gson().toJson(userObj));

                getSupportFragmentManager().beginTransaction().replace(R.id.ll_parent_content, DealReviewFragment.newInstance(id, Constants.ALL_REVIEWS, "")).commit();
                setToolbarTitle(name);
                Log.e(TAG, "onViewCreated setToolbarTitle = " + name);
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(RESULT_CODE_VIEW_REVIEWS);
    }
}

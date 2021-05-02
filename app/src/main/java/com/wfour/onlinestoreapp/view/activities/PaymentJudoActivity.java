package com.wfour.onlinestoreapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;


import com.wfour.onlinestoreapp.R;

/**
 * Created by Suusoft on 23/12/2016.
 */

public class PaymentJudoActivity extends com.wfour.onlinestoreapp.base.BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG = PaymentJudoActivity.class.getName();
    private static final String PUBLISH_KEY = "pk_live_bDT3Y92UjjLtUerECuty4giP";

    private com.google.android.gms.common.api.GoogleApiClient googleApiClient;

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;

    }

    @Override
    protected int getLayoutInflate() {

        return R.layout.activity_payment_stripe;
    }

    @Override
    protected void getExtraData(Intent intent) {


    }

    @Override
    protected void initilize() {

    }

    @Override
    protected void initView() {
        paymentStripe();
    }

    @Override
    protected void onViewCreated() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void onStart() {
        super.onStart();

    }

    public void onStop() {
        super.onStop();

    }

    private void paymentStripe() {
//        JudoPay judoPay = new JudoPay();
//
//        judoPay.payment(this, "500");
    }
}

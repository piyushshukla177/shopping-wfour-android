package com.wfour.onlinestoreapp.view.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.sendbird.android.User;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.BaseActivity;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.objects.UserObj;
import com.wfour.onlinestoreapp.retrofit.ApiUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class VerificationActivity extends BaseActivity implements View.OnClickListener {
    private AppCompatEditText otpCode;

    private String phoneNumberInPrefs = null, password = null, username = null;
    private ProgressDialog progressDialog;
    private TextView verificationMessage, retryTimer, tvBack, tvOptCode, tvVerificationHeading, tvWeSendCode, submit;
    private CountDownTimer countDownTimer;
    private String mVerificationId;
    private ImageView imgBack;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private boolean authInProgress;
    private Context mContext;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                //force authenticate
                String otp = otpCode.getText().toString();
                if (!TextUtils.isEmpty(otp) && !TextUtils.isEmpty(mVerificationId))
                    signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(mVerificationId, otp));
                //verifyOtp(otpCode[0].getText().toString() + otpCode[1].getText().toString() + otpCode[2].getText().toString() + otpCode[3].getText().toString());
                break;
            case R.id.back:
                back();
                break;
            case R.id.tv_back:
                back();
                break;
            default:
                break;
        }
    }

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NONE;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_verification;
    }

    @Override
    protected void getExtraData(Intent intent) {

    }

    @Override
    protected void initilize() {

    }

    @Override
    protected void initView() {
        mContext = this;
        phoneNumberInPrefs = getIntent().getStringExtra("phone");
        password = getIntent().getStringExtra("password");
        username = getIntent().getStringExtra("username");

        progressDialog = new ProgressDialog(this);
        //initiate authentication
        mAuth = FirebaseAuth.getInstance();
        retryTimer = findViewById(R.id.resend);
        verificationMessage = findViewById(R.id.verificationMessage);
//        tvVerificationHeading = findViewById(R.id.verificationHeading);
        otpCode = findViewById(R.id.otp);
//        tvOptCode   = findViewById(R.id.tv_opt_code);
        imgBack = findViewById(R.id.back);
        tvBack = findViewById(R.id.tv_back);
//        tvWeSendCode= findViewById(R.id.txt_send_code);
        initiateAuth(phoneNumberInPrefs);
        submit = findViewById(R.id.submit);
        imgBack.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    protected void onViewCreated() {

    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        mContext = null;
        super.onDestroy();
    }

    private void showProgress(int i) {
        String title = (i == 1) ? getString(R.string.otp_sending) : getString(R.string.otp_verifying);
        String message = (i == 1) ? (getString(R.string.otp_sending_msg) + " " + phoneNumberInPrefs) : getString(R.string.otp_verifying_msg);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void initiateAuth(String phone) {
        if (mContext != null) {
            showProgress(1);
            PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 60, TimeUnit.SECONDS, this,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onCodeAutoRetrievalTimeOut(String s) {
                            super.onCodeAutoRetrievalTimeOut(s);
                        }

                        @Override
                        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                            progressDialog.dismiss();
                            signInWithPhoneAuthCredential(phoneAuthCredential);
                        }

                        @Override
                        public void onVerificationFailed(FirebaseException e) {
                            authInProgress = false;
                            progressDialog.dismiss();
                            countDownTimer.cancel();
                            verificationMessage.setText(getString(R.string.error_detail) + ((e.getMessage() != null) ? ("\n" + e.getMessage()) : ""));
                            retryTimer.setVisibility(View.VISIBLE);
                            retryTimer.setText(getString(R.string.resend));
                            retryTimer.setOnClickListener(view -> initiateAuth(phoneNumberInPrefs));
                            Toast.makeText(VerificationActivity.this, getString(R.string.error_detail) + (e.getMessage() != null ? "\n" + e.getMessage() : ""), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCodeSent(@NonNull String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(verificationId, forceResendingToken);
                            authInProgress = true;
                            progressDialog.dismiss();
                            mVerificationId = verificationId;
                            mResendToken = forceResendingToken;

                            verificationMessage.setText(getString(R.string.otp_sent) + " " + phoneNumberInPrefs);
                            retryTimer.setVisibility(View.GONE);
                        }
                    });
            startCountdown();
        }
    }

    private void startCountdown() {
        retryTimer.setOnClickListener(null);
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                if (retryTimer != null) {
                    retryTimer.setText(String.valueOf(l / 1000));
                }
            }

            @Override
            public void onFinish() {
                if (retryTimer != null) {
                    retryTimer.setText(getText(R.string.resend));
                    retryTimer.setOnClickListener(view -> initiateAuth(phoneNumberInPrefs));
                }
            }
        }.start();
    }

    private void back() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.verification_cancel_title);
        builder.setMessage(R.string.verification_cancel_message);
        builder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
            dialogInterface.dismiss();
            onBackPressed();
        });
        builder.setPositiveButton(R.string.no, (dialogInterface, i) -> dialogInterface.dismiss());

        if (progressDialog.isShowing() || authInProgress) {
            builder.create().show();
        } else {
            onBackPressed();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        showProgress(2);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {
            progressDialog.setMessage(getString(R.string.logging_in));
            login();
        }).addOnFailureListener(e -> {
            Toast.makeText(VerificationActivity.this, getString(R.string.error_detail) + (e.getMessage() != null ? "\n" + e.getMessage() : ""), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            authInProgress = false;
        });
    }

    private void login() {
        Intent intent = new Intent();
//        Bundle bundle = new Bundle();
//        bundle.putString(Args.KEY_PHONE_CODE, "ok");
//        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}

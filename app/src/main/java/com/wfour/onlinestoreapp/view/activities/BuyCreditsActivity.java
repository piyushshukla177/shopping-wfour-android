package com.wfour.onlinestoreapp.view.activities;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.*;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.network1.NetworkUtility;
import com.wfour.onlinestoreapp.objects.SettingsObj;
import com.wfour.onlinestoreapp.objects.UserObj;
import com.wfour.onlinestoreapp.paypal.Paypal;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by Suusoft on 05/12/2016.
 */

public class BuyCreditsActivity extends com.wfour.onlinestoreapp.base.BaseActivity implements View.OnClickListener {
    private static final String TAG = BuyCreditsActivity.class.getName();
    private static final String ACTION_BUY_CREDIT = "exchange";
    private static final String PAYPAL = "Paypal";
    private static final String SKRILL = "Skrill";
    private TextViewRegular tvCreditsAvailable;
    private FrameLayout btnPayViaPaypal;
    private FrameLayout btnPayViaSkrill;
    private EditText edtCredits;
    private TextViewRegular tvTotal, tvSubTotal, tvFee;
    private RelativeLayout btnBuy;
    private ImageView imgPaypal, imgSkrill;
    private UserObj user;
    private String methodPayment;

    private boolean isNeedFinished;
    private TextView tvBtn;

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_buy_credits;
    }

    @Override
    protected void getExtraData(Intent intent) {
        if (intent.hasExtra(Args.IS_NEED_FINISH)) {
            isNeedFinished = true;
        }
    }

    @Override
    protected void initilize() {
        Paypal.startPaypalService(this);
//        AppUtil.setSoftInputMode(this);
        user = DataStoreManager.getUser();

    }

    @Override
    protected void initView() {
        setToolbarTitle(R.string.iwana_pay_buy_credit);
        tvCreditsAvailable = (TextViewRegular) findViewById(R.id.tv_credits);
        btnPayViaPaypal = (FrameLayout) findViewById(R.id.btn_paypal);
        btnPayViaSkrill = (FrameLayout) findViewById(R.id.btn_skrill);
        edtCredits = (EditText) findViewById(R.id.edt_credits);
        tvTotal = (TextViewRegular) findViewById(R.id.tv_total);
        tvSubTotal = (TextViewRegular) findViewById(R.id.tv_sub_total);
        tvFee = (TextViewRegular) findViewById(R.id.tv_fee);
        btnBuy = (RelativeLayout) findViewById(R.id.btn_functions);
        tvBtn = (TextView) findViewById(R.id.tv_btn);
        imgPaypal = (ImageView) findViewById(R.id.img_paypal);
        imgSkrill = (ImageView) findViewById(R.id.img_skrill);

        btnPayViaSkrill.setVisibility(View.GONE);
    }

    @Override
    protected void onViewCreated() {
        btnPayViaPaypal.setOnClickListener(this);
        btnPayViaSkrill.setOnClickListener(this);
        btnBuy.setOnClickListener(this);
        tvBtn.setText(getString(R.string.button_buy));
        edtCredits.requestFocus();

//        tvCreditsAvailable.setText(AppUtil.formatCurrency(user.getBalance()));
        tvCreditsAvailable.setText(StringUtil.convertNumberToString(user.getBalance(), 1));
        edtCredits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null && !charSequence.toString().isEmpty()) {
                    double price = Double.valueOf(charSequence.toString());
                    int exchange = Integer.parseInt(DataStoreManager.getSettingUtility().getExchange_rate());
                    int fee = Integer.parseInt(DataStoreManager.getSettingUtility().getExchange_fee());
                    price = price / exchange;
                    tvTotal.setText(AppUtil.formatCurrency(price + fee));
                } else {
                    tvTotal.setText("0");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getSettingUtility();
    }

    @Override
    public void onClick(View view) {
        if (view == btnPayViaPaypal) {
            if (NetworkUtility.getInstance(getApplicationContext()).isNetworkAvailable()) {
                processButtonPaypal();
            } else {
                AppUtil.showToast(getApplicationContext(), R.string.msg_network_not_available);
            }

        } else if (view == btnPayViaSkrill) {
            if (NetworkUtility.getInstance(getApplicationContext()).isNetworkAvailable()) {
//                AppUtil.startActivity(this, PaymentStripeActivity.class);
                processButtonSkrill();
                AppUtil.showToast(getApplicationContext(), R.string.msg_promotion_is_developping);
            } else {
                AppUtil.showToast(getApplicationContext(), R.string.msg_network_not_available);
            }

        } else if (view == btnBuy) {
            if (NetworkUtility.getInstance(getApplicationContext()).isNetworkAvailable()) {
                if (isValid()) {
                    if (methodPayment == null) {
                        AppUtil.showToast(getApplicationContext(), R.string.msg_chose_method_payment);
                    } else {
                        if (methodPayment == PAYPAL) {
                            java.text.DecimalFormat format = new java.text.DecimalFormat();
                            try {

                                Number number = format.parse(tvTotal.getText().toString());
                                double price = number.doubleValue();
                                Paypal.requestPaypalPayment(this, price);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        } else if (methodPayment == SKRILL) {

                        }
                    }

                }
            } else {
                AppUtil.showToast(getApplicationContext(), R.string.msg_network_not_available);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Paypal.PAYPAL_REQUEST_CODE) {
            if (Paypal.isConfirm(data)) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                org.json.JSONObject jsonObject = confirm.toJSONObject();
                try {
                    JSONObject response = jsonObject.getJSONObject("response");
                    if (NetworkUtility.getInstance(getApplicationContext()).isNetworkAvailable()) {
                        sendTransactionsToServer(response.getString("id"));
                    } else {
                        AppUtil.showToast(getApplicationContext(), R.string.msg_network_not_available);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private void sendTransactionsToServer(String id) {
        String amount = edtCredits.getText().toString();
        ModelManager.transaction(this, id, amount, ACTION_BUY_CREDIT, "", "", methodPayment, new ModelManagerListener() {
            @Override
            public void onSuccess(Object object) {
                org.json.JSONObject jsonObject = (JSONObject) object;
                ApiResponse apiResponse = new ApiResponse(jsonObject);
                if (!apiResponse.isError()) {
                    org.json.JSONObject obj = apiResponse.getDataObject();
                    try {
                        String balance = obj.getString("balance");
                        if (balance != null) {
//                            tvCreditsAvailable.setText(AppUtil.formatCurrency(Double.parseDouble(balance)));
                            tvCreditsAvailable.setText(StringUtil.convertNumberToString(Float.parseFloat(balance), 1));
                            UserObj user = DataStoreManager.getUser();
                            user.setBalance(Float.parseFloat(balance));
                            DataStoreManager.saveUser(user);
                            AppUtil.showToast(getApplicationContext(), R.string.msg_transaction_success);

                            if (isNeedFinished) {
                                setResult(Args.RQ_BUY_CREDIT, new Intent());
                                finish();
                            } else {
                                finish();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    AppUtil.showToast(getApplicationContext(), apiResponse.getMessage());
                }
            }

            @Override
            public void onError() {
                Log.e(TAG, "Send BuyCredit to Server Error!");
            }
        });
    }

    private boolean isValid() {
        String amount = edtCredits.getText().toString();
        if (StringUtil.isEmpty(amount)) {
            AppUtil.showToast(getApplicationContext(), R.string.msg_amout_is_require);
            return false;
        } else {
            int amountCredit = Integer.parseInt(amount);
            if (amountCredit <= 0) {
                AppUtil.showToast(getApplicationContext(), R.string.msg_value_credits);
                return false;
            }
        }
        return true;
    }

    private void processButtonSkrill() {
        methodPayment = SKRILL;
        imgSkrill.setImageResource(R.drawable.stripe_on);
        imgPaypal.setImageResource(R.drawable.paypal_off);
    }

    private void processButtonPaypal() {
        methodPayment = PAYPAL;
        imgSkrill.setImageResource(R.drawable.stripe_off);
        imgPaypal.setImageResource(R.drawable.paypal_on);
    }

    private void getSettingUtility() {
        ModelManager.getSettingUtility(this, new ModelManagerListener() {
            @Override
            public void onSuccess(Object object) {
                org.json.JSONObject jsonObject = (JSONObject) object;
                ApiResponse apiResponse = new ApiResponse(jsonObject);
                if (!apiResponse.isError()) {
                    DataStoreManager.saveSettingUtility(jsonObject.toString());
                    SettingsObj settingsObj = DataStoreManager.getSettingUtility();
                    tvFee.setText(String.format(getString(R.string.you_have_to_pay_01_dollar_transaction_fee),settingsObj.getExchange_fee()));
                } else {
                    Log.e(TAG, "GET SETTING UTILITY ERROR!");
                }
            }

            @Override
            public void onError() {
                Log.e(TAG, "GET SETTING UTILITY ERROR!");
            }
        });
    }
}

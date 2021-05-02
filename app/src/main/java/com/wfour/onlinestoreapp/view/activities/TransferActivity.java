package com.wfour.onlinestoreapp.view.activities;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.UserObj;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Suusoft on 05/12/2016.
 */

public class TransferActivity extends com.wfour.onlinestoreapp.base.BaseActivity implements View.OnClickListener {
    private static final String ACTION_TRANSFER = "transfer";
    private TextViewRegular tvCreditsAvailable;
    private EditText edtCredits;
    private EditText editFromMail;
    private EditText edtReceiveMail;
    private TextView tvBtn;
    private TextView tvInfo;
    private RelativeLayout btnTransfer;
    private EditText edtDescription;
    private UserObj user;

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_transfer;
    }

    @Override
    protected void getExtraData(Intent intent) {

    }

    @Override
    protected void initilize() {
        user = DataStoreManager.getUser();
    }

    @Override
    protected void initView() {
        setToolbarTitle(R.string.iwana_pay_tranfers);
        tvCreditsAvailable = (TextViewRegular) findViewById(R.id.tv_credits);
        edtCredits = (EditText) findViewById(R.id.edt_credits);
        tvInfo = (TextView) findViewById(R.id.tv_info);
        btnTransfer = (RelativeLayout) findViewById(R.id.btn_functions);
        tvBtn = (TextView) findViewById(R.id.tv_btn);
        editFromMail = (EditText) findViewById(R.id.edt_from_email);
        edtReceiveMail = (EditText) findViewById(R.id.edt_receive_mail);
        edtDescription = (EditText) findViewById(R.id.edt_description);
    }

    @Override
    protected void onViewCreated() {
        edtCredits.requestFocus();
        btnTransfer.setOnClickListener(this);
        tvBtn.setText(getString(R.string.button_transfer));
        editFromMail.setText(user.getEmail());
        tvInfo.setText(getString(R.string.note_transfer));
//        tvCreditsAvailable.setText(AppUtil.formatCurrency(user.getBalance()));
        tvCreditsAvailable.setText(StringUtil.convertNumberToString(user.getBalance(), 1));
        edtCredits.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    editFromMail.setFocusable(false);
                    edtReceiveMail.requestFocus();
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == btnTransfer) {
            if (com.wfour.onlinestoreapp.network1.NetworkUtility.getInstance(getApplicationContext()).isNetworkAvailable()) {
                if (isValid()) {
                    transfer();
                }
            } else {
                AppUtil.showToast(getApplicationContext(), R.string.msg_network_not_available);
            }
        }
    }

    private void transfer() {
        ModelManager.transaction(this, "", edtCredits.getText().toString(), ACTION_TRANSFER, edtDescription.getText().toString(), "", edtReceiveMail.getText().toString(), new ModelManagerListener() {
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
                            finish();
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

            }
        });
    }

    private boolean isValid() {
        String fromMail = editFromMail.getText().toString();
        String receiveMail = edtReceiveMail.getText().toString();
        String description = edtDescription.getText().toString();
        String amount = edtCredits.getText().toString();
        String numCredits = tvCreditsAvailable.getText().toString().replaceAll(",", "");
//        DecimalFormat format = new DecimalFormat();
//        Number number = null;
//        try {
//            number = format.parse(numCredits);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        if (StringUtil.isEmpty(amount)) {
            AppUtil.showToast(getApplicationContext(), R.string.msg_amout_is_require);
            return false;
        } else {
            Double amountCredit = Double.parseDouble(amount);
            if (amountCredit <= 0) {
                AppUtil.showToast(getApplicationContext(), R.string.msg_value_credits);
                return false;
            } else if (amountCredit > Double.parseDouble(numCredits)) {
                AppUtil.showToast(getApplicationContext(), R.string.msg_enought_credits);
                return false;
            }
        }
        if (fromMail.equals(receiveMail)) {
            AppUtil.showToast(this,R.string.msg_transfer_to_yourself);
            return false;
        }
//        if (!StringUtil.isValidEmail(fromMail)) {
//            AppUtil.showToast(getApplicationContext(), R.string.msg_email_is_required);
//            return false;
//        }
        if (!StringUtil.isValidEmail(receiveMail)) {
            AppUtil.showToast(getApplicationContext(), R.string.msg_email_is_required);
            return false;
        }
        if (StringUtil.isEmpty(description)) {
            AppUtil.showToast(getApplicationContext(), R.string.msg_description_is_require);
            edtDescription.requestFocus();
            return false;
        }
        return true;
    }
}

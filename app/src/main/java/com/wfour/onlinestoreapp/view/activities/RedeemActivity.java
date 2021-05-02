package com.wfour.onlinestoreapp.view.activities;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wfour.onlinestoreapp.AppController;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.interfaces.IRedeem;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.network1.NetworkUtility;
import com.wfour.onlinestoreapp.objects.UserObj;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Suusoft on 05/12/2016.
 */

public class RedeemActivity extends com.wfour.onlinestoreapp.base.BaseActivity implements View.OnClickListener, IRedeem {
    private static final String ACTION_REDEEM = "redeem";
    private static final String TAG = RedeemActivity.class.getName();
    private TextViewRegular tvCreditsAvailable;
    private EditText edtCredits;
    private TextView tvInfo;
    private RelativeLayout btnRedeem;
    private EditText edtDescription;
    private UserObj user;
    private TextView tvBtn;

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_redeem;
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
        setToolbarTitle(R.string.iwana_pay_redeem);
        AppController.getInstance().setiRedeem(this);
        tvCreditsAvailable = (TextViewRegular) findViewById(R.id.tv_credits);
        edtCredits = (EditText) findViewById(R.id.edt_credits);
        tvInfo = (TextView) findViewById(R.id.tv_info);
        btnRedeem = (RelativeLayout) findViewById(R.id.btn_functions);
        tvBtn = (TextView) findViewById(R.id.tv_btn);
        edtDescription = (EditText) findViewById(R.id.edt_description);

    }

    @Override
    protected void onViewCreated() {
        btnRedeem.setOnClickListener(this);
        tvBtn.setText(getString(R.string.button_redeem));
        edtCredits.requestFocus();
//        tvCreditsAvailable.setText(AppUtil.formatCurrency(user.getBalance()));
        if (NetworkUtility.getInstance(this).isNetworkAvailable()) {
            getProfile();
        } else {
            AppUtil.showToast(this, R.string.msg_network_not_available);
        }

    }

    @Override
    public void onClick(View view) {
        if (view == btnRedeem) {
            if (NetworkUtility.getInstance(getApplicationContext()).isNetworkAvailable()) {
                if (isValid()) {
                    redeem();
                }
            } else {
                AppUtil.showToast(getApplicationContext(), R.string.msg_network_not_available);
            }
        }
    }

    private void redeem() {
        ModelManager.transaction(this, "", edtCredits.getText().toString(), ACTION_REDEEM, edtCredits.getText().toString(), "", "", new ModelManagerListener() {
            @Override
            public void onSuccess(Object object) {
                org.json.JSONObject jsonObject = (JSONObject) object;
                ApiResponse apiResponse = new ApiResponse(jsonObject);
                if (!apiResponse.isError()) {
                    AppUtil.showToast(getApplicationContext(), R.string.msg_transaction_success);
                    finish();
                } else {
                    AppUtil.showToast(getApplicationContext(), apiResponse.getMessage());
                }
            }

            @Override
            public void onError() {
                Log.e(TAG, "REDEEM: Error!");
            }
        });
    }

    private boolean isValid() {
        String amount = edtCredits.getText().toString();
        String description = edtDescription.getText().toString();
        String numCredits = tvCreditsAvailable.getText().toString().replaceAll(",","");
//        DecimalFormat format = new DecimalFormat();
//        Number number = null;
//        try {
//            number = format.parse(numCredits);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        if (StringUtil.isEmpty(amount)) {
            AppUtil.showToast(getApplicationContext(), R.string.msg_amout_is_require);
            edtCredits.requestFocus();
            return false;
        } else {
            int amountCredit = Integer.parseInt(amount);
            if (amountCredit <= 0) {
                AppUtil.showToast(getApplicationContext(), R.string.msg_value_credits);
                edtCredits.requestFocus();
                return false;
            }else if (amountCredit > Double.parseDouble(numCredits)) {
                AppUtil.showToast(getApplicationContext(), R.string.msg_enought_credits);
                return false;
            }
        }
        if (StringUtil.isEmpty(description)) {
            AppUtil.showToast(getApplicationContext(), R.string.msg_description_is_require);
            edtDescription.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void updateBalace(final float balance) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvCreditsAvailable.setText(StringUtil.convertNumberToString(balance, 1));
//                tvCreditsAvailable.setText(AppUtil.formatCurrency(balance));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppController.getInstance().setiRedeem(null);
    }

    private void getProfile() {
        String id = DataStoreManager.getUser().getId();
        String passWord = DataStoreManager.getUser().getPassWord();

        Log.e("hihi", " pas"+ passWord);
        String address = DataStoreManager.getUser().getAddress();
        String phone = DataStoreManager.getUser().getPhone();
        String name = DataStoreManager.getUser().getName();
        ModelManager.getProfile(self, id,  new ModelManagerListener() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(object.toString());
                    ApiResponse response = new ApiResponse(jsonObject);
                    if (!response.isError()) {
                        UserObj userObj = response.getDataObject(UserObj.class);
                        UserObj user = DataStoreManager.getUser();
                        user.setBalance(userObj.getBalance());
                        DataStoreManager.saveUser(user);
//                        tvCreditsAvailable.setText(AppUtil.formatCurrency(user.getBalance()));
                        tvCreditsAvailable.setText(StringUtil.convertNumberToString(user.getBalance(),1));

                    } else {
                        Toast.makeText(self, response.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {

            }
        });
    }
}

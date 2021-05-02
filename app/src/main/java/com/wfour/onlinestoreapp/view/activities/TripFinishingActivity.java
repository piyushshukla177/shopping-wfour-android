package com.wfour.onlinestoreapp.view.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wfour.onlinestoreapp.quickblox.conversation.utils.DialogUtil;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBEvent;
import com.quickblox.messages.model.QBNotificationType;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.configs.ChatConfigs;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IConfirmation;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.network1.NetworkUtility;
import com.wfour.onlinestoreapp.objects.DealObj;
import com.wfour.onlinestoreapp.objects.PaymentMethodObj;
import com.wfour.onlinestoreapp.objects.RecentChatObj;
import com.wfour.onlinestoreapp.objects.ReservationObj;
import com.wfour.onlinestoreapp.objects.TransportDealObj;
import com.wfour.onlinestoreapp.objects.UserObj;
import com.wfour.onlinestoreapp.parsers.JSONParser;
import com.wfour.onlinestoreapp.quickblox.SharedPreferencesUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.widgets.textview.TextViewBold;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import org.json.JSONObject;

import java.util.ArrayList;

public class TripFinishingActivity extends BaseActivity {

    private static final String TAG = TripFinishingActivity.class.getSimpleName();

    private int mTransactionFee = 1; // Set 1 credit as default

    private TransportDealObj mTransportDealObj;
    private ReservationObj mReservation;

    private TextViewBold mLblDollar;
    private RelativeLayout mLblPay;
    private TextView tvBtn;
    private TextViewRegular mLblCredits, mLblMonetary, mLblTotal;
    private EditText mTxtAmount;
    private RecyclerView mRcl;
    private LinearLayout mLlNote;

    private String mSelectedPayment = PaymentMethodObj.CREDITS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    void inflateLayout() {
        setContentView(R.layout.activity_trip_finishing);
    }

    @Override
    protected void getExtraValues() {
        super.getExtraValues();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Args.TRANSPORT_DEAL_OBJ)) {
                mTransportDealObj = bundle.getParcelable(Args.TRANSPORT_DEAL_OBJ);

                try {
                    mTransactionFee = Integer.parseInt(DataStoreManager.getSettingUtility().getTrip_payment_fee());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (bundle.containsKey(Args.RESERVATION_DEAL_OBJ)) {
                mReservation = bundle.getParcelable(Args.RESERVATION_DEAL_OBJ);

                try {
                    mTransactionFee = Integer.parseInt(DataStoreManager.getSettingUtility().getDeal_payment_fee());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    void initUI() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Show as up button
        try {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        mLblDollar = (TextViewBold) findViewById(R.id.lbl_dollar);
        mLblPay = (RelativeLayout) findViewById(R.id.btn_functions);
        tvBtn = (TextView) findViewById(R.id.tv_btn);
        mLblCredits = (TextViewRegular) findViewById(R.id.lbl_credits);
        mLblMonetary = (TextViewRegular) findViewById(R.id.lbl_monetary_unit);
        mLblTotal = (TextViewRegular) findViewById(R.id.lbl_total);
        mTxtAmount = (EditText) findViewById(R.id.txt_amount);
        mLlNote = (LinearLayout) findViewById(R.id.ll_note);

        mRcl = (RecyclerView) findViewById(R.id.rcl_payment_methods);
        mRcl.setLayoutManager(new LinearLayoutManager(self, LinearLayoutManager.HORIZONTAL, false));
        mRcl.setHasFixedSize(true);

        // Should call this methods at the end of declaring UI
        initData();
        initPaymentMethod();
    }

    @Override
    void initControl() {
        tvBtn.setText(getString(R.string.pay));
        mLblPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRatingDialog();
            }
        });

        mTxtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateTotalFare();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void payDeal(float rate, String comment) {
        if (NetworkUtility.getInstance(self).isNetworkAvailable()) {
            String strPrice = mTxtAmount.getText().toString().trim();
            if (strPrice.equals("")) {
                strPrice = "0";
            } else if (strPrice.contains(",")) {
                strPrice = strPrice.replace(",", "");
            }
            if (mSelectedPayment.equals(PaymentMethodObj.CREDITS)) {
                strPrice = String.valueOf(Double.parseDouble(strPrice) + mTransactionFee);
            }

            final double price = Double.parseDouble(strPrice);
            boolean checked = true;
            if (mSelectedPayment.equals(PaymentMethodObj.CREDITS)) {
                checked = DataStoreManager.getUser().getBalance() >= price;
            }
            Log.e(TAG, checked + "");
            if (checked) {
                if (mTransportDealObj != null) {
                    Log.e(TAG, "manualTrip mTransportDealObj != null  " + new Gson().toJson(mTransportDealObj));
                    ModelManager.manualTrip(self, TransportDealObj.ACTION_FINISH, mTransportDealObj,
                            mSelectedPayment, strPrice, rate, comment,
                            new ModelManagerListener() {
                                @Override
                                public void onSuccess(Object object) {
                                    JSONObject jsonObject = (JSONObject) object;
                                    Log.e(TAG, "manualTrip onSuccess  JSONObject" + new Gson().toJson(object));
                                    AppUtil.showToast(self,JSONParser.getMessage(jsonObject) );
                                    if (JSONParser.responseIsSuccess(jsonObject)) {
                                        // Save deal is not negotiated
                                        RecentChatObj recentChatObj = new RecentChatObj(mTransportDealObj, null, SharedPreferencesUtil.getQbUser());
                                        DataStoreManager.saveDealNegotiation(recentChatObj, false);

                                        Log.e(TAG, "JSONParser.responseIsSuccess(jsonObject) = true" );
                                        // Create notification for driver
                                        String msg = String.format(getString(R.string.msg_paid_for_deal_dollar), recentChatObj.getQbUser().getFullName(),
                                                StringUtil.convertNumberToString(price, 1));
                                        double fare = price;
                                        if (mSelectedPayment.equals(PaymentMethodObj.CREDITS)) {
                                            fare = price - mTransactionFee;

                                            msg = String.format(getString(R.string.msg_paid_for_deal), recentChatObj.getQbUser().getFullName(),
                                                    StringUtil.convertNumberToString(fare, 1));
                                        }

                                        createEvent(recentChatObj, msg, String.valueOf(fare), mSelectedPayment);

                                        // Update user's balance
                                        UserObj userObj = DataStoreManager.getUser();
                                        userObj.setBalance((float) (userObj.getBalance() - price));
                                        DataStoreManager.saveUser(userObj);
                                        AppUtil.showToast(self,R.string.msg_paid_successfully );


                                        TransportDealsActivity.tripFinished = true;
                                        setResult(RESULT_OK);
                                        onBackPressed();
                                    }
                                }

                                @Override
                                public void onError() {
                                    Log.e(TAG, "manualTrip onError  ");
                                }
                            });
                } else if (mReservation != null) {
                    Log.e(TAG, "manualTrip mReservation != null  " + new Gson().toJson(mReservation));
                    ModelManager.manualReservation(self, String.valueOf(mReservation.getDeal_id()), ReservationObj.ACTION_PAY,
                            mReservation.getDeal_name(), String.valueOf(mReservation.getBuyer_id()), mReservation.getBuyer_name(),
                            String.valueOf(mReservation.getId()), mSelectedPayment, strPrice,
                            rate, comment, new ModelManagerListener() {
                                @Override
                                public void onSuccess(Object object) {
                                    Log.e(TAG, "manualReservation " +  new Gson().toJson(object));
                                            JSONObject jsonObject = (JSONObject) object;
                                    if (JSONParser.responseIsSuccess(jsonObject)) {
                                        // Save deal is not negotiated
                                        DealObj dealObj = mReservation.getDeal();
                                        dealObj.setBuyerId(String.valueOf(mReservation.getBuyer_id()));
                                        RecentChatObj recentChatObj = new RecentChatObj(null, dealObj, SharedPreferencesUtil.getQbUser());
                                        DataStoreManager.saveDealNegotiation(recentChatObj, false);

                                        Log.e(TAG, "manualReservation JSONParser.responseIsSuccess(jsonObject) ");
                                        // Create notification for seller
                                        String msg = String.format(getString(R.string.msg_paid_for_deal_dollar), recentChatObj.getQbUser().getFullName(),
                                                StringUtil.convertNumberToString(price, 1));
                                        double fare = price;
                                        if (mSelectedPayment.equals(PaymentMethodObj.CREDITS)) {
                                            fare = price - mTransactionFee;

                                            msg = String.format(getString(R.string.msg_paid_for_deal), recentChatObj.getQbUser().getFullName(),
                                                    StringUtil.convertNumberToString(fare, 1));
                                        }
                                        createEvent(recentChatObj, msg, String.valueOf(fare), mSelectedPayment);

                                        // Update user's balance
                                        UserObj userObj = DataStoreManager.getUser();
                                        userObj.setBalance((float) (userObj.getBalance() - price));
                                        DataStoreManager.saveUser(userObj);

                                        AppUtil.showToast(self,  R.string.msg_paid_successfully );

                                        setResult(RESULT_OK);
                                        onBackPressed();
                                    } else {
                                        AppUtil.showToast(self, JSONParser.getMessage(jsonObject) );
                                    }
                                }

                                @Override
                                public void onError() {
                                }
                            });
                }
            } else {
                GlobalFunctions.showConfirmationDialog(self, getString(R.string.msg_balance_is_not_enough_to_pay),
                        getString(R.string.button_buy_credits), getString(R.string.no_thank), true, new IConfirmation() {
                            @Override
                            public void onPositive() {
                                GlobalFunctions.startActivityWithoutAnimation(self, BuyCreditsActivity.class);
                            }

                            @Override
                            public void onNegative() {
                            }
                        });
            }
        } else {
            AppUtil.showToast(self,  R.string.msg_no_network );
        }
    }

    private void initData() {
        if (mTransportDealObj != null) {
            mLblDollar.setText(String.format(getString(R.string.dollar_value),
                    StringUtil.convertNumberToString(mTransportDealObj.getEstimateFare(), 1)));
            mLblCredits.setText(String.format(getString(R.string.value_credits),
                    StringUtil.convertNumberToString(mTransportDealObj.getEstimateFare(), 1)));

            String amount = StringUtil.convertNumberToString(mTransportDealObj.getEstimateFare(), 1);
            mTxtAmount.setText(amount);
            mLblTotal.setText(String.format(getString(R.string.value_credits),
                    StringUtil.convertNumberToString((mTransportDealObj.getEstimateFare() + mTransactionFee), 1)));
        } else if (mReservation != null) {
            setTitle(R.string.pay_deal);
            mLblDollar.setText(String.format(getString(R.string.dollar_value),
                    StringUtil.convertNumberToString(mReservation.getDeal().getSale_price(), 1)));
            mLblCredits.setText(String.format(getString(R.string.value_credits),
                    StringUtil.convertNumberToString(mReservation.getDeal().getSale_price(), 1)));

            String amount = StringUtil.convertNumberToString(mReservation.getDeal().getSale_price(), 1);
            mTxtAmount.setText(amount);
            mLblTotal.setText(String.format(getString(R.string.value_credits),
                    StringUtil.convertNumberToString((mReservation.getDeal().getSale_price() + mTransactionFee), 1)));
        }
    }

    private void initPaymentMethod() {
        PaymentMethodAdapter adapter = new PaymentMethodAdapter(new IPayment() {
            @Override
            public void onPaymentMethodSelected(PaymentMethodObj obj) {
                if (obj.getId().equals(PaymentMethodObj.CREDITS)) {
                    mLlNote.setVisibility(View.VISIBLE);
                    mLblMonetary.setText(getString(R.string.credits));
                } else {
                    mLlNote.setVisibility(View.INVISIBLE);
                    mLblMonetary.setText(getString(R.string.dollars));
                }

                updateTotalFare();
            }
        });

        mRcl.setAdapter(adapter);
    }

    private void updateTotalFare() {
        String amount = mTxtAmount.getText().toString().trim();
        if (amount.contains(",")) {
            amount = amount.replace(",", "");
        }
        if (mSelectedPayment.equals(PaymentMethodObj.CREDITS)) {
            double total = 0;
            if (amount.length() > 0) {
                total = Double.parseDouble(amount) + mTransactionFee;
            }

            mLblTotal.setText(String.format(getString(R.string.value_credits), StringUtil.convertNumberToString(total, 1)));
        } else {
            double total = 0;
            if (amount.length() > 0) {
                total = Double.parseDouble(amount);
            }

            mLblTotal.setText(String.format(getString(R.string.dollar_value), StringUtil.convertNumberToString(total, 1)));
        }
    }

    private void showRatingDialog() {
        final Dialog dialog = DialogUtil.setDialogCustomView(self,R.layout.dialog_rating, false );

        final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.rating_bar);
        final EditText txtComment = (EditText) dialog.findViewById(R.id.txt_comment);
        TextViewBold lblSubmit = (TextViewBold) dialog.findViewById(R.id.lbl_submit);
        ((LayerDrawable) ratingBar.getProgressDrawable()).getDrawable(2)
                .setColorFilter(ratingBar.getContext().getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rate, boolean b) {
                if (rate < 1) {
                    ratingBar.setRating(1);
                }
            }
        });

        lblSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ratingBar.getRating() == 0) {
                    AppUtil.showToast(self,R.string.rating_is_required );
                } else if (txtComment.getText().toString().trim().equals("")) {
                    AppUtil.showToast(self,R.string. comment_is_required);
                    txtComment.requestFocus();
                } else {
                    dialog.dismiss();

                    payDeal(ratingBar.getRating() * 2, txtComment.getText().toString().trim());
                }
            }
        });

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.ViewHolder> {

        private ArrayList<PaymentMethodObj> paymentMethodObjs;
        private IPayment iPayment;

        public PaymentMethodAdapter(IPayment iPayment) {
            this.iPayment = iPayment;

            if (paymentMethodObjs == null) {
                paymentMethodObjs = new ArrayList<>();
                paymentMethodObjs.add(new PaymentMethodObj(PaymentMethodObj.CASH, getString(R.string.cash), false));
                paymentMethodObjs.add(new PaymentMethodObj(PaymentMethodObj.PAYPAL, getString(R.string.paypal), false));
                paymentMethodObjs.add(new PaymentMethodObj(PaymentMethodObj.CREDITS, getString(R.string.credits), true));
                paymentMethodObjs.add(new PaymentMethodObj(PaymentMethodObj.STRIPE, getString(R.string.stripe), false));
                paymentMethodObjs.add(new PaymentMethodObj(PaymentMethodObj.OTHER, getString(R.string.other), false));
            }
        }

        @Override
        public PaymentMethodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_method, parent, false);

            return new PaymentMethodAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final PaymentMethodAdapter.ViewHolder holder, final int position) {
            if (getItemCount() > 0) {
                final PaymentMethodObj obj = paymentMethodObjs.get(position);
                if (obj != null) {
                    holder.lblMethod.setText(obj.getName());
                    if (obj.isSelected()) {
                        holder.lblMethod.setTextColor(ContextCompat.getColor(self, R.color.white));
                        holder.lblMethod.setBackgroundResource(R.drawable.bg_accent_shadow);
                    } else {
                        holder.lblMethod.setTextColor(ContextCompat.getColor(self, R.color.textColorPrimary));
                        holder.lblMethod.setBackgroundResource(R.drawable.bg_pressed_grey);
                    }

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!mSelectedPayment.equals(obj.getId())) {
                                for (int i = 0; i < getItemCount(); i++) {
                                    PaymentMethodObj paymentMethodObj = paymentMethodObjs.get(i);
                                    if (paymentMethodObj.isSelected()) {
                                        paymentMethodObj.setSelected(false);
                                        break;
                                    }
                                }

                                obj.setSelected(true);
                                notifyDataSetChanged();
                                mSelectedPayment = obj.getId();
                                iPayment.onPaymentMethodSelected(obj);
                            }
                        }
                    });
                }
            }
        }

        @Override
        public int getItemCount() {
            try {
                return paymentMethodObjs.size();
            } catch (NullPointerException ex) {
                return 0;
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextViewRegular lblMethod;

            public ViewHolder(View view) {
                super(view);

                lblMethod = (TextViewRegular) view.findViewById(R.id.lbl_payment_method);
            }
        }
    }

    interface IPayment {
        void onPaymentMethodSelected(PaymentMethodObj obj);
    }

    public static void start(Activity activity, TransportDealObj transportDealObj, ReservationObj reservationObj) {
        Bundle bundle = new Bundle();
        if (transportDealObj != null) {
            bundle.putParcelable(Args.TRANSPORT_DEAL_OBJ, transportDealObj);
        } else if (reservationObj != null) {
            bundle.putParcelable(Args.RESERVATION_DEAL_OBJ, reservationObj);
        }
        GlobalFunctions.startActivityWithoutAnimation(activity, TripFinishingActivity.class, bundle);
    }

    public static void startForResult(Activity activity, TransportDealObj transportDealObj, ReservationObj reservationObj, int reqCode) {
        Bundle bundle = new Bundle();
        if (transportDealObj != null) {
            bundle.putParcelable(Args.TRANSPORT_DEAL_OBJ, transportDealObj);
        } else if (reservationObj != null) {
            bundle.putParcelable(Args.RESERVATION_DEAL_OBJ, reservationObj);
        }

        Intent intent = new Intent(activity, TripFinishingActivity.class);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, reqCode);
    }

    private void createEvent(RecentChatObj recentChatObj, String msg, String fare, String paymentMethod) {
        StringifyArrayList<Integer> userIds = new StringifyArrayList<Integer>();
        if (recentChatObj.getTransportDealObj() != null) {
            userIds.add(recentChatObj.getTransportDealObj().getDriverQBId());
        } else if (recentChatObj.getDealObj() != null) {
            userIds.add(recentChatObj.getDealObj().getSellerQbId());
        }

        QBEvent event = new QBEvent();
        event.setUserIds(userIds);
        event.setEnvironment(QBEnvironment.DEVELOPMENT);
        event.setNotificationType(QBNotificationType.PUSH);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Args.MESSAGE, recentChatObj.getQbUser().getFullName() + ": " + msg);
            jsonObject.put(Args.NOTIFICATION_TYPE, ChatConfigs.QUICKBLOX_PAY_FOR_DEAL);
            jsonObject.put(Args.FARE, fare);
            jsonObject.put(Args.PAYMENT_METHOD, paymentMethod);

            jsonObject.put(Args.RECENT_CHAT_OBJ, new Gson().toJson(recentChatObj));
            Log.e(TAG, "Pay: " + new Gson().toJson(recentChatObj));
        } catch (Exception e) {
            e.printStackTrace();
        }

        event.setMessage(jsonObject.toString());

        QBPushNotifications.createEvent(event).performAsync(new QBEntityCallback<QBEvent>() {
            @Override
            public void onSuccess(QBEvent qbEvent, Bundle bundle) {
            }

            @Override
            public void onError(QBResponseException e) {
            }
        });
    }
}

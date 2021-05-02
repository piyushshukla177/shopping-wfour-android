package com.wfour.onlinestoreapp.view.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wfour.onlinestoreapp.quickblox.conversation.utils.DialogUtil;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBEvent;
import com.quickblox.messages.model.QBNotificationType;
import com.quickblox.users.model.QBUser;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.view.adapters.TransportDealAdapter;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.configs.ChatConfigs;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IConfirmation;
import com.wfour.onlinestoreapp.interfaces.ITransportDeal;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.DriverObj;
import com.wfour.onlinestoreapp.objects.RecentChatObj;
import com.wfour.onlinestoreapp.objects.SettingsObj;
import com.wfour.onlinestoreapp.objects.TransportDealObj;
import com.wfour.onlinestoreapp.objects.UserObj;
import com.wfour.onlinestoreapp.parsers.JSONParser;
import com.wfour.onlinestoreapp.quickblox.SharedPreferencesUtil;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.NetworkUtility;
import com.wfour.onlinestoreapp.utils.map.MapsUtil;
import com.wfour.onlinestoreapp.widgets.textview.TextViewBold;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TransportDealsActivity extends BaseActivity implements ITransportDeal {

    private static final String TAG = TransportDealsActivity.class.getSimpleName();

    private static final int RC_LOCATION = 1;
    private static final int RC_TURN_ON_LOCATION = 2;

    private RecyclerView mRcl;
    private TransportDealAdapter mAdapter;
    private ArrayList<TransportDealObj> mTransportDealObjs;
    private TransportDealObj mTransportDealObj;
    private int page = 1;

    private LinearLayout mLlNoData, mLlNoConection;

    public static boolean tripFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    void inflateLayout() {
        setContentView(R.layout.activity_transport_deals);
    }

    @Override
    void initUI() {
        // Show as up button
        try {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        mLlNoData = (LinearLayout) findViewById(R.id.ll_no_data);
        mLlNoConection = (LinearLayout) findViewById(R.id.ll_no_connection);
        mRcl = (RecyclerView) findViewById(R.id.rcl_transport_deal);
        mRcl.setLayoutManager(new LinearLayoutManager(self, LinearLayoutManager.VERTICAL, false));

        if (mTransportDealObjs == null) {
            mTransportDealObjs = new ArrayList<>();
        } else {
            mTransportDealObjs.clear();
        }

        mAdapter = new TransportDealAdapter(self, mTransportDealObjs, this);
        mRcl.setAdapter(mAdapter);

        getData();
    }

    @Override
    void initControl() {
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh trip list if it's finished
        if (tripFinished) {
            tripFinished = false;

            mTransportDealObjs.remove(mTransportDealObj);
            mAdapter.notifyDataSetChanged();

            showNoDataLayout();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
            if (mTransportDealObjs.size() == 0) {
                Toast.makeText(self, R.string.msg_have_no_trip_to_share, Toast.LENGTH_SHORT).show();
            } else {
                if (mTransportDealObjs.size() == 1) {
                    AppUtil.shareTrip(self, mTransportDealObjs.get(0));
                } else {
                    showTripSharingDialog();
                }
            }
        }
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_TURN_ON_LOCATION) {
            if (MapsUtil.locationIsEnable(self)) {
                gotoTripTracking();
            } else {
                turnOnLocationReminder(RC_TURN_ON_LOCATION, false);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case RC_LOCATION: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (MapsUtil.locationIsEnable(self)) {
                            gotoTripTracking();
                        } else {
                            MapsUtil.displayLocationSettingsRequest(self, RC_TURN_ON_LOCATION);
                        }
                    } else {
                        showPermissionsReminder(RC_LOCATION, false);
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onCancel(TransportDealObj obj) {
        cancelTrip(obj);
    }

    @Override
    public void onTracking(TransportDealObj obj) {
        mTransportDealObj = obj;
        gotoTripTracking();
    }

    @Override
    public void onFinish(TransportDealObj obj) {
        mTransportDealObj = obj;
        gotoTripFinishing(obj);
    }

    private void gotoTripTracking() {
        TripTrackingActivity.start(self, mTransportDealObj);
    }

    private void gotoTripFinishing(TransportDealObj obj) {
        if (!obj.getDriverId().equals(DataStoreManager.getUser().getId())) {
            TripFinishingActivity.start(self, obj, null);
        } else { // passenger
            showRatingDialog();
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
                    Toast.makeText(self, R.string.rating_is_required, Toast.LENGTH_SHORT).show();
                } else if (txtComment.getText().toString().trim().equals("")) {
                    Toast.makeText(self, R.string.comment_is_required, Toast.LENGTH_SHORT).show();
                    txtComment.requestFocus();
                } else {
                    dialog.dismiss();

                    finishTrip(ratingBar.getRating() * 2, txtComment.getText().toString().trim());
                }
            }
        });

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private void getData() {
        if (NetworkUtility.isNetworkAvailable()) {
            ModelManager.getTripHistoryList(self, page, TransportDealObj.PROCESSING, true, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject jsonObject = (JSONObject) object;

                    if (JSONParser.responseIsSuccess(jsonObject)) {
                        ArrayList<TransportDealObj> arrTemp = JSONParser.parseTrips(jsonObject);
                        if (arrTemp.size() > 0) {
                            mTransportDealObjs.addAll(arrTemp);
                            mAdapter.notifyDataSetChanged();
                        }

                        showNoDataLayout();
                    } else {
                        Toast.makeText(self, JSONParser.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError() {
                    Toast.makeText(self, R.string.msg_have_some_errors, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(self, R.string.msg_no_network, Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelTrip(final TransportDealObj obj) {
        if (NetworkUtility.isNetworkAvailable()) {
            GlobalFunctions.showConfirmationDialog(self, getString(R.string.msg_confirm_cancel_trip),
                    getString(R.string.yes), getString(R.string.no_thank), true, new IConfirmation() {
                        @Override
                        public void onPositive() {
                            ModelManager.manualTrip(self, TransportDealObj.ACTION_CANCEL, obj, "", "",
                                    0, "", new ModelManagerListener() {
                                        @Override
                                        public void onSuccess(Object object) {
                                            JSONObject jsonObject = (JSONObject) object;
                                            if (JSONParser.responseIsSuccess(jsonObject)) {
                                                // Send notification to opponent
                                                String msg = String.format(getString(R.string.user_canceled_deal), DataStoreManager.getUser().getName());
                                                createEvent(obj, msg);
                                                RecentChatObj recentChatObj = new RecentChatObj(obj, null, null);
                                                DataStoreManager.saveDealNegotiation(recentChatObj, false);

                                                mTransportDealObjs.remove(obj);
                                                mAdapter.notifyDataSetChanged();

                                                showNoDataLayout();
                                            } else {
                                                Toast.makeText(self, JSONParser.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onError() {
                                        }
                                    });
                        }

                        @Override
                        public void onNegative() {
                        }
                    });
        } else {
            Toast.makeText(self, R.string.msg_no_network, Toast.LENGTH_SHORT).show();
        }
    }

    private void showNoDataLayout() {
        if (mTransportDealObjs != null && mTransportDealObjs.size() > 0) {
            mLlNoData.setVisibility(View.GONE);
        } else {
            mLlNoData.setVisibility(View.VISIBLE);
        }
    }

    public void postComment(final float rating, final String content) {
        if (NetworkUtility.isNetworkAvailable()) {
            ModelManager.postReview(self, mTransportDealObj.getPassengerId(), Constants.PASSENGER, Constants.DRIVER,
                    mTransportDealObj.getId(), Constants.TRIP, content, rating + "", new ModelManagerListener() {
                        @Override
                        public void onSuccess(Object object) {
                            JSONObject jsonObject = (JSONObject) object;
                            ApiResponse response = new ApiResponse(jsonObject);
                            Toast.makeText(self, response.getMessage(), Toast.LENGTH_SHORT).show();

                            if (!response.isError()) {
                                mTransportDealObjs.remove(mTransportDealObj);
                                mAdapter.notifyDataSetChanged();

                                showNoDataLayout();
                            }
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(self, R.string.msg_have_some_errors, Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(self, R.string.msg_no_network, Toast.LENGTH_SHORT).show();
        }
    }

    private void showTripSharingDialog() {
        final Dialog dialog = new Dialog(self);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_share_trip);

        RecyclerView rcl = (RecyclerView) dialog.findViewById(R.id.rcl_trip);
        rcl.setLayoutManager(new LinearLayoutManager(self, LinearLayoutManager.VERTICAL, false));
        rcl.setHasFixedSize(true);
        rcl.setAdapter(new TripAdapter(dialog));

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private void finishTrip(float rate, String comment) {
        if (NetworkUtility.isNetworkAvailable()) {
            ModelManager.manualTrip(self, TransportDealObj.ACTION_FINISH, mTransportDealObj,
                    "", "", rate, comment,
                    new ModelManagerListener() {
                        @Override
                        public void onSuccess(Object object) {
                            JSONObject jsonObject = (JSONObject) object;
                            Toast.makeText(self, JSONParser.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                            if (JSONParser.responseIsSuccess(jsonObject)) {
                                mTransportDealObjs.remove(mTransportDealObj);
                                mAdapter.notifyDataSetChanged();

                                showNoDataLayout();

                                confirmDriverActivateAgain();
                            }
                        }

                        @Override
                        public void onError() {
                        }
                    });
        } else {
            Toast.makeText(self, R.string.msg_no_network, Toast.LENGTH_SHORT).show();
        }
    }

    private void createEvent(TransportDealObj transportDealObj, String msg) {
        StringifyArrayList<Integer> userIds = new StringifyArrayList<Integer>();
        if (transportDealObj.getDriverId().equals(DataStoreManager.getUser().getId())) {
            userIds.add(transportDealObj.getPassengerQBId());
        } else {
            userIds.add(transportDealObj.getDriverQBId());
        }

        QBEvent event = new QBEvent();
        event.setUserIds(userIds);
        event.setEnvironment(QBEnvironment.DEVELOPMENT);
        event.setNotificationType(QBNotificationType.PUSH);

        JSONObject jsonObject = new JSONObject();
        try {
            QBUser qbUser = SharedPreferencesUtil.getQbUser();
            qbUser.setPhone(DataStoreManager.getUser().getPhone());

            jsonObject.put(Args.MESSAGE, qbUser.getFullName() + ": " + msg);
            jsonObject.put(Args.NOTIFICATION_TYPE, ChatConfigs.QUICKBLOX_MESSAGE);

            RecentChatObj obj = new RecentChatObj(transportDealObj, null, qbUser);
            jsonObject.put(Args.RECENT_CHAT_OBJ, new Gson().toJson(obj));
        } catch (JSONException e) {
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

    private void confirmDriverActivateAgain() {
        GlobalFunctions.showConfirmationDialog(self, getString(R.string.msg_ask_driver_activate_again),
                getString(R.string.yes), getString(R.string.no), false, new IConfirmation() {
                    @Override
                    public void onPositive() {
                        changeDriverMode(Constants.ON, 0);
                    }

                    @Override
                    public void onNegative() {
                        Toast.makeText(self, R.string.msg_remind_driver_activate_again, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void changeDriverMode(final String mode, int duration) {
        if (NetworkUtility.isNetworkAvailable()) {
            ModelManager.activateDriverMode(self, mode, duration, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject jsonObject = (JSONObject) object;

                    if (JSONParser.responseIsSuccess(jsonObject)) {
                        if (mode.equals(Constants.OFF)) {
                            // Set driver is unavailable
                            UserObj userObj = DataStoreManager.getUser();
                            userObj.getDriverData().setAvailable(DriverObj.DRIVER_UNAVAILABLE);
                            DataStoreManager.saveUser(userObj);
                            Toast.makeText(self, R.string.msg_deactivate_success, Toast.LENGTH_SHORT).show();
                        } else {
                            // Set driver is available
                            UserObj userObj = DataStoreManager.getUser();
                            userObj.getDriverData().setAvailable(DriverObj.DRIVER_AVAILABLE);
                            DataStoreManager.saveUser(userObj);

                            Toast.makeText(self, R.string.msg_activate_success, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (mode.equals(Constants.ON)) {
                            showDurationBuyingDialog(R.string.msg_enter_duration_to_be_available);
                        }
                    }
                }

                @Override
                public void onError() {
                }
            });
        } else {
            Toast.makeText(self, R.string.msg_no_network, Toast.LENGTH_SHORT).show();
        }
    }

    private void showDurationBuyingDialog(int msgId) {
        final Dialog dialog = DialogUtil.setDialogCustomView(self, R.layout.dialog_buying_duration, false);

        final EditText txtDuration = (EditText) dialog.findViewById(R.id.txt_duration);
        TextViewRegular lblMsg = (TextViewRegular) dialog.findViewById(R.id.lbl_msg);
        final TextViewRegular lblFee = (TextViewRegular) dialog.findViewById(R.id.lbl_msg_fee);
        TextViewBold lblBuyCredits = (TextViewBold) dialog.findViewById(R.id.lbl_buy_credits);
        TextViewBold lblAvailable = (TextViewBold) dialog.findViewById(R.id.lbl_available);

        lblMsg.setText(msgId);
        lblFee.setText(String.format(getString(R.string.msg_subtract_credits), "0"));

        SettingsObj settingsObj = DataStoreManager.getSettingUtility();
        final int feePerHour = (settingsObj != null && settingsObj.getDeal_online_rate() != null
                && !settingsObj.getDeal_online_rate().equals("")) ?
                Integer.parseInt(settingsObj.getDeal_online_rate()) : 1;

        txtDuration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String duration = editable.toString().trim().isEmpty() ? "0" : editable.toString().trim();

                lblFee.setText(String.format(getString(R.string.msg_subtract_credits), String.valueOf((Integer.parseInt(duration) * feePerHour))));
            }
        });

        lblBuyCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                GlobalFunctions.startActivityWithoutAnimation(self, BuyCreditsActivity.class);
            }
        });

        lblAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int duration = 0;
                if (txtDuration.getText().toString().trim().length() > 0) {
                    duration = Integer.parseInt(txtDuration.getText().toString().trim());
                }

                if (duration == 0 || duration > 24) {
                    Toast.makeText(self, R.string.msg_duration_must_gt_zero, Toast.LENGTH_LONG).show();
                } else {
                    if (DataStoreManager.getUser().getBalance() < duration) {
                        Toast.makeText(self, String.format(getString(R.string.msg_balance_is_not_enough),
                                String.valueOf(duration)), Toast.LENGTH_LONG).show();
                    } else {
                        dialog.dismiss();

                        changeDriverMode(Constants.DURATION_BUYING, duration);
                    }
                }
            }
        });

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

        private Dialog dialog;

        public TripAdapter(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public TripAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_to_share, parent, false);
            return new TripAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final TripAdapter.ViewHolder holder, final int position) {
            if (getItemCount() > 0) {
                final TransportDealObj transportDealObj = mTransportDealObjs.get(position);
                if (transportDealObj != null) {
                    holder.lblTripId.setText(String.format(getString(R.string.trip_value), transportDealObj.getId()));

                    if (position < (mTransportDealObjs.size() - 1)) {
                        holder.divider.setVisibility(View.VISIBLE);
                    } else {
                        holder.divider.setVisibility(View.GONE);
                    }

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }

                            AppUtil.shareTrip(self, transportDealObj);
                        }
                    });
                }
            }
        }

        @Override
        public int getItemCount() {
            try {
                return mTransportDealObjs.size();
            } catch (NullPointerException ex) {
                return 0;
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private TextViewRegular lblTripId;
            private View divider;

            public ViewHolder(View view) {
                super(view);

                lblTripId = (TextViewRegular) view.findViewById(R.id.lbl_trip_id);
                divider = view.findViewById(R.id.vw_divider);
            }
        }
    }
}

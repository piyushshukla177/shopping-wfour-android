package com.wfour.onlinestoreapp.view.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IConfirmation;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.DriverObj;
import com.wfour.onlinestoreapp.objects.RecentChatObj;
import com.wfour.onlinestoreapp.objects.SettingsObj;
import com.wfour.onlinestoreapp.objects.TransportDealObj;
import com.wfour.onlinestoreapp.objects.UserObj;
import com.wfour.onlinestoreapp.parsers.JSONParser;
import com.wfour.onlinestoreapp.quickblox.conversation.utils.DialogUtil;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.NetworkUtility;
import com.wfour.onlinestoreapp.utils.map.IMaps;
import com.wfour.onlinestoreapp.utils.map.MapsUtil;
import com.wfour.onlinestoreapp.utils.map.direction.GMapV2Direction;
import com.wfour.onlinestoreapp.utils.map.direction.GetRouteListTask;
import com.wfour.onlinestoreapp.widgets.textview.TextViewBold;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TripTrackingActivity extends BaseActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GMapV2Direction.DirecitonReceivedListener {

    private static final String TAG = TripTrackingActivity.class.getSimpleName();

    private static final int RC_LOCATION = 1;
    private static final int RC_TURN_ON_LOCATION = 2;
    private static final int RC_DEAL_COMPLETED = 999;

    private GoogleMap mMap;

    // Use google api to get current location
    private GoogleApiClient mGoogleApiClient;
    protected Location mMyLocation;

    private Marker mDriverMarker, mFromMarker, mToMarker;

    private TransportDealObj mTransportDealObj;

    private Timer mTimer;

    private TextViewRegular mLblDealCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGoogleApiClient();
        Log.e("TripTrackingActivity", "onCreate");
    }

    @Override
    void inflateLayout() {
        setContentView(R.layout.activity_trip_tracking);
    }

    @Override
    protected void getExtraValues() {
        super.getExtraValues();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Args.TRANSPORT_DEAL_OBJ)) {
                mTransportDealObj = bundle.getParcelable(Args.TRANSPORT_DEAL_OBJ);
            }
        }
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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLblDealCompleted = (TextViewRegular) findViewById(R.id.lbl_completed);
    }

    @Override
    void initControl() {
        mLblDealCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTransportDealObj != null) {
                    if (mTransportDealObj.getDriverId().equals(DataStoreManager.getUser().getId())) {
                        showRatingDialog();
                    } else {
                        TripFinishingActivity.startForResult(self, mTransportDealObj, null, RC_DEAL_COMPLETED);
                    }
                }
            }
        });
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
            AppUtil.shareTrip(self, mTransportDealObj);
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_TURN_ON_LOCATION) {
            if (MapsUtil.locationIsEnable(self)) {
                if (GlobalFunctions.locationIsGranted(self, RC_LOCATION, null)) {
                    initMyLocation();
                }
            } else {
                turnOnLocationReminder(RC_TURN_ON_LOCATION, false);
            }
        } else if (requestCode == RC_DEAL_COMPLETED) {
            if (resultCode == RESULT_OK) {
                // Close this activity if the payment is success
                setResult(RESULT_OK);
                onBackPressed();
            }
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
                            initMyLocation();
                        } else {
                            turnOnLocationReminder(RC_TURN_ON_LOCATION, false);
                        }
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();

        // Stopping refresh events action.
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        initRoute();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Update driver's location
        updateDriverLocation();

        if (GlobalFunctions.locationIsGranted(self, RC_LOCATION, null)) {
            if (MapsUtil.locationIsEnable(self)) {
                mMyLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                initMyLocation();
            } else {
                MapsUtil.displayLocationSettingsRequest(self, RC_TURN_ON_LOCATION);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void OnDirectionListReceived(List<LatLng> mPointList) {
        if (mPointList != null) {
            PolylineOptions rectLine = new PolylineOptions().width(15).color(ContextCompat.getColor(self, R.color.colorAccent));
            for (int i = 0; i < mPointList.size(); i++) {
                rectLine.add(mPointList.get(i));
            }
            mMap.addPolyline(rectLine);
        }
    }

    private void initGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private Marker addMarker(Marker marker, LatLng latLng, int icon, String title) {
        // Clear old marker if it's
        if (marker != null) {
            marker.remove();
        }

        return MapsUtil.addMarker(mMap, latLng, title, icon, false);
    }

    private void initRoute() {
        if (mTransportDealObj != null) {
            // Add pickup maker
            mFromMarker = addMarker(mFromMarker, mTransportDealObj.getLatLngPickup(), R.drawable.ic_place_accent,
                    mTransportDealObj.getPickup());
            MapsUtil.moveCameraTo(mMap, mTransportDealObj.getLatLngPickup(), 14);

            // Add destination maker
            mToMarker = addMarker(mToMarker, mTransportDealObj.getLatLngDestination(), R.drawable.ic_place_red,
                    mTransportDealObj.getDestination());

            // Draw polyline
            new GetRouteListTask(self, mTransportDealObj.getLatLngPickup(),
                    mTransportDealObj.getLatLngDestination(), GMapV2Direction.MODE_DRIVING, this)
                    .execute();
        }
    }

    private void initMyLocation() {
        if (mMyLocation != null) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            MapsUtil.getMyLocation(mGoogleApiClient, new IMaps() {
                @Override
                public void processFinished(Object obj) {
                    mMyLocation = (Location) obj;

                    mMap.setMyLocationEnabled(true);
                }
            });
        }
    }

    private void updateDriverLocation() {
        if (!mTransportDealObj.getDriverId().equals(DataStoreManager.getUser().getId())) {
            if (mTimer == null) {
                mTimer = new Timer();

                mTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getDriverLocation();
                            }
                        });
                    }
                }, 0, Constants.TIME_TO_UPDATE);
            }
        }
    }

    private void getDriverLocation() {
        if (NetworkUtility.isNetworkAvailable()) {
            ModelManager.getDriverLocation(self, mTransportDealObj.getId(), new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject jsonObject = (JSONObject) object;
                    if (JSONParser.responseIsSuccess(jsonObject)) {
                        try {
                            JSONObject data = jsonObject.getJSONObject("data");
                            LatLng latLng = new LatLng(data.optDouble("lat"), data.optDouble("long"));
                            mDriverMarker = addMarker(mDriverMarker, latLng, getTransportIcon(mTransportDealObj), "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(self, JSONParser.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
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

    public static void start(Activity activity, TransportDealObj transportDealObj) {
        if (transportDealObj != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Args.TRANSPORT_DEAL_OBJ, transportDealObj);
            GlobalFunctions.startActivityWithoutAnimation(activity, TripTrackingActivity.class, bundle);
        }
    }

    public static void startForResult(Activity activity, TransportDealObj transportDealObj, int reqCode) {
        if (transportDealObj != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Args.TRANSPORT_DEAL_OBJ, transportDealObj);
            Intent intent = new Intent(activity, TripTrackingActivity.class);
            intent.putExtras(bundle);
            activity.startActivityForResult(intent, reqCode);
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
                                // Save deal is not negotiated
                                RecentChatObj recentChatObj = new RecentChatObj(mTransportDealObj, null, null);
                                DataStoreManager.saveDealNegotiation(recentChatObj, false);

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

                        setResult(RESULT_OK);
                        onBackPressed();
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

                        setResult(RESULT_OK);
                        onBackPressed();
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
        TextViewBold lblMsg = (TextViewBold) dialog.findViewById(R.id.lbl_msg);
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
}

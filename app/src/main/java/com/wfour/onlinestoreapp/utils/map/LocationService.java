package com.wfour.onlinestoreapp.utils.map;

import android.Manifest;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.view.activities.SplashLoginActivity;
import com.wfour.onlinestoreapp.configs.ChatConfigs;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.utils.NetworkUtility;

/**
 * Created by Suusoft on 01/03/2017.
 */

public class LocationService extends IntentService implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = LocationService.class.getSimpleName();

    private static int REQUEST_CODE = 1000;
    private static int NOTIFICATION_LOCATION_PERMISSION = 1000;
    private static int NOTIFICATION_TURN_ON_LOCATION = 1001;

    public static final String REQUEST_LOCATION = "requestLocation";
    public static final String STOP_REQUESTING_LOCATION = "stopRequestingLocation";

    private static final String ACTION = "action";

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private String mAction;

    private Handler mHandler;
    private Runnable mRunnable;

    private static boolean serviceStarted;

    public LocationService(String name) {
        super(name);
    }

    public LocationService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initGoogleApiClient();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mAction = intent.getStringExtra(ACTION);

        if (mAction != null) {
            if (mAction.equals(REQUEST_LOCATION)) {
                requestLocation();
            } else if (mAction.equals(STOP_REQUESTING_LOCATION)) {
                stopRequestingLocation();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (DataStoreManager.getUser() != null && DataStoreManager.getUser().getDriverData() != null) {
            if (DataStoreManager.getUser().getDriverData().isAvailable()) {
                updateLocation(location);
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mAction.equals(REQUEST_LOCATION)) {
            requestLocation();
        } else if (mAction.equals(STOP_REQUESTING_LOCATION)) {
            stopRequestingLocation();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void initGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            // Init location request
            initLocationRequest();
        }

        // Connect google api client
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    private void initLocationRequest() {
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(Constants.TIME_TO_UPDATE)
                .setFastestInterval(1000); // 1 second, in milliseconds
    }

    private void updateLocation(Location location) {
        checkLocation();

        if (location != null) {
            if (NetworkUtility.isNetworkAvailable()) {
                if (GlobalFunctions.isMarshmallow()) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (MapsUtil.locationIsEnable(this)) {
                            ModelManager.updateLocation(this, new LatLng(location.getLatitude(), location.getLongitude()));
                        }
                    }
                } else {
                    if (MapsUtil.locationIsEnable(this)) {
                        ModelManager.updateLocation(this, new LatLng(location.getLatitude(), location.getLongitude()));
                    }
                }
            } else {
                Toast.makeText(this, R.string.msg_no_network_to_update_location, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkLocation() {
        if (mRunnable == null) {
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    if (NetworkUtility.isNetworkAvailable()) {
                        if (DataStoreManager.getUser() != null && DataStoreManager.getUser().getDriverData() != null) {
                            if (DataStoreManager.getUser().getDriverData().isAvailable()) {
                                if (GlobalFunctions.isMarshmallow()) {
                                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                        if (!MapsUtil.locationIsEnable(LocationService.this)) {
                                            sendNotification(ChatConfigs.NOTIFICATION_TURN_ON_LOCATION);

                                            checkLocation();
                                        }
                                    } else {
                                        sendNotification(ChatConfigs.NOTIFICATION_LOCATION_PERMISSION);

                                        checkLocation();
                                    }
                                } else {
                                    if (!MapsUtil.locationIsEnable(LocationService.this)) {
                                        sendNotification(ChatConfigs.NOTIFICATION_TURN_ON_LOCATION);

                                        checkLocation();
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(LocationService.this, R.string.msg_no_network_to_update_location, Toast.LENGTH_SHORT).show();
                    }
                }
            };
        }

        if (mHandler == null) {
            mHandler = new Handler();
        } else {
            mHandler.removeCallbacks(mRunnable);
        }
        mHandler.postDelayed(mRunnable, Constants.TIME_TO_UPDATE);
    }

    private void requestLocation() {
        if (!serviceStarted) {
            if (GlobalFunctions.isMarshmallow()) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (MapsUtil.locationIsEnable(this)) {
                        if (mGoogleApiClient.isConnected()) {
                            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

                            // Reset action after requesting location listener successfully
                            mAction = "";

                            serviceStarted = true;
                        } else {
                            mGoogleApiClient.connect();
                        }
                    } else {
                        sendNotification(ChatConfigs.NOTIFICATION_TURN_ON_LOCATION);
                    }
                } else {
                    sendNotification(ChatConfigs.NOTIFICATION_LOCATION_PERMISSION);
                }
            } else {
                if (MapsUtil.locationIsEnable(this)) {
                    if (mGoogleApiClient.isConnected()) {
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

                        // Reset action after requesting location listener successfully
                        mAction = "";

                        serviceStarted = true;
                    } else {
                        mGoogleApiClient.connect();
                    }
                } else {
                    sendNotification(ChatConfigs.NOTIFICATION_TURN_ON_LOCATION);
                }
            }
        }
    }

    private void stopRequestingLocation() {
        if (mGoogleApiClient != null) {
            // Disconnect google api client
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

                // Reset action after removing location listener successfully
                mAction = "";

                mGoogleApiClient.disconnect();

                serviceStarted = false;

                // Remove callback
                if (mHandler != null && mRunnable != null) {
                    mHandler.removeCallbacks(mRunnable);
                }
            } else {
                mGoogleApiClient.connect();
            }
        }
    }

    public static void start(Context context, String action) {
        Intent intent = new Intent(context, LocationService.class);
        intent.putExtra(ACTION, action);
        context.startService(intent);
    }

    private void sendNotification(String notificationType) {
        Intent intent = new Intent(this, SplashLoginActivity.class);
        intent.putExtra(Args.NOTIFICATION_TYPE, notificationType);

        REQUEST_CODE++;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String messageBody = getString(R.string.msg_remind_user_grant_location);
        int notificationId = NOTIFICATION_LOCATION_PERMISSION;
        if (notificationType.equals(ChatConfigs.NOTIFICATION_TURN_ON_LOCATION)) {
            notificationId = NOTIFICATION_TURN_ON_LOCATION;
            messageBody = getString(R.string.msg_remind_user_turn_on_location);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}

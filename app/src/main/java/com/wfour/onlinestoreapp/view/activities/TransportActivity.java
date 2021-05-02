package com.wfour.onlinestoreapp.view.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.wfour.onlinestoreapp.quickblox.conversation.utils.DialogUtil;
import com.quickblox.users.model.QBUser;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.view.adapters.PassengerQuantityAdapter;
import com.wfour.onlinestoreapp.view.adapters.TransportAdapter;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IConfirmation;
import com.wfour.onlinestoreapp.interfaces.IPassenger;
import com.wfour.onlinestoreapp.interfaces.ITransport;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.DriverObj;
import com.wfour.onlinestoreapp.objects.RecentChatObj;
import com.wfour.onlinestoreapp.objects.SettingsObj;
import com.wfour.onlinestoreapp.objects.TransportDealObj;
import com.wfour.onlinestoreapp.objects.TransportObj;
import com.wfour.onlinestoreapp.objects.UserObj;
import com.wfour.onlinestoreapp.parsers.JSONParser;
import com.wfour.onlinestoreapp.quickblox.SharedPreferencesUtil;
import com.wfour.onlinestoreapp.utils.NetworkUtility;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.utils.map.IMaps;
import com.wfour.onlinestoreapp.utils.map.MapsUtil;
import com.wfour.onlinestoreapp.widgets.textview.TextViewBold;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TransportActivity extends BaseActivity implements View.OnClickListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMarkerClickListener {

    private static final String TAG = TransportActivity.class.getSimpleName();

    private static final int RC_LOCATION = 1;
    private static final int RC_TURN_ON_LOCATION = 2;
    private static final int RC_GET_DEPARTURE = 3;
    private static final int RC_GET_DESTINATION = 4;

    private GoogleMap mMap;
    private TextViewRegular mLblFrom, mLblTo, mLblIwanaride;
    private ImageView mImgLocDeparture, mImgLocDestination;

    // Use google api to get current location
    private GoogleApiClient mGoogleApiClient;
    private Place mMyPlace;
    private boolean mDepartureRequested, mDestinationRequested;

    private LatLng mFromLatLng, mToLatLng;
    private Marker mMyLocMarker, mFromMarker, mToMarker;

    // Select transport type and number of passengers
    private String mSelectedTransportType = TransportObj.ALL;
    private LinearLayout mLlTransport, mLlPassengerQuantity;
    private ImageView mImgTransportType;
    private TextViewRegular mLblTransportType, mLblPassengerQuantity;
    private RecyclerView mRclTransport, mRclPassengerQuantity;

    private ArrayList<TransportDealObj> mNearbyDriverDeals;
    private ArrayList<Marker> mDriverMarkers;
    private TransportDealObj mSelectedDriver;
    private RecentChatObj mRecentChatObj;
    private ArrayList<QBUser> mSelectedUsers;

    private Menu mMenu;

    private String mSelectedDriverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGoogleApiClient();

        initTransportTypes();
        initPassengerQuantity();
    }

    @Override
    void inflateLayout() {
        setContentView(R.layout.activity_transport);
    }

    @Override
    void initUI() {
        // Show as up button
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLblIwanaride = (TextViewRegular) findViewById(R.id.lbl_iwanaride);
        mLblFrom = (TextViewRegular) findViewById(R.id.lbl_from);
        mLblTo = (TextViewRegular) findViewById(R.id.lbl_to);
        mImgLocDeparture = (ImageView) findViewById(R.id.img_location_departure);
        mImgLocDestination = (ImageView) findViewById(R.id.img_location_destination);

        mLlTransport = (LinearLayout) findViewById(R.id.ll_transport_type);
        mImgTransportType = (ImageView) findViewById(R.id.img_transport_type);
        mLblTransportType = (TextViewRegular) findViewById(R.id.lbl_transport_type);
        mLlPassengerQuantity = (LinearLayout) findViewById(R.id.ll_passenger_quantity);
        mLblPassengerQuantity = (TextViewRegular) findViewById(R.id.lbl_passenger_quantity);

        mRclTransport = (RecyclerView) findViewById(R.id.rcl_transport_type);
        mRclTransport.setLayoutManager(new LinearLayoutManager(self, LinearLayoutManager.HORIZONTAL, false));
        mRclTransport.setHasFixedSize(true);

        mRclPassengerQuantity = (RecyclerView) findViewById(R.id.rcl_passenger_quantity);
        mRclPassengerQuantity.setLayoutManager(new LinearLayoutManager(self, LinearLayoutManager.HORIZONTAL, false));
        mRclPassengerQuantity.setHasFixedSize(true);
    }

    @Override
    void initControl() {
        mLblIwanaride.setOnClickListener(this);
        mLblFrom.setOnClickListener(this);
        mLblTo.setOnClickListener(this);
        mImgLocDeparture.setOnClickListener(this);
        mImgLocDestination.setOnClickListener(this);
        mLlTransport.setOnClickListener(this);
        mLlPassengerQuantity.setOnClickListener(this);
    }

    @Override
    protected void getExtraValues() {
        super.getExtraValues();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Get transport type(delivery) when user click on 'iwanadelivery' in left menu
            if (bundle.containsKey(Args.TRANSPORT_TYPE)) {
                mSelectedTransportType = bundle.getString(Args.TRANSPORT_TYPE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_transport, menu);

        mMenu = menu;
        if (DataStoreManager.getUser().getDriverData() != null
                && DataStoreManager.getUser().getDriverData().isAvailable()) {
            mMenu.findItem(R.id.action_available_for_driving).setChecked(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_available_for_driving) {
            if (DataStoreManager.getUser().getDriverData() != null) {
                if (DataStoreManager.getUser().getDriverData().isActive()) {
                    String mode = mMenu.findItem(R.id.action_available_for_driving).isChecked() ? Constants.OFF : Constants.ON;
                    changeDriverMode(mode, 0);
                } else {
                    mMenu.findItem(R.id.action_available_for_driving).setChecked(false);

                    Toast.makeText(self, R.string.msg_wait_active_driver, Toast.LENGTH_SHORT).show();
                }
            } else {
                mMenu.findItem(R.id.action_available_for_driving).setChecked(false);

                buyDuration(Constants.DURATION_BUYING);
            }
        } else if (id == R.id.action_duration_buying) {
            buyDuration(Constants.DURATION_BUYING);
        } else if (id == R.id.action_iwanaride_deals) {
            GlobalFunctions.startActivityWithoutAnimation(self, TransportDealsActivity.class);
        } else if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GET_DEPARTURE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                fillAddress(mLblFrom, place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else if (requestCode == RC_GET_DESTINATION) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                fillAddress(mLblTo, place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else if (requestCode == RC_TURN_ON_LOCATION) {
            if (MapsUtil.locationIsEnable(self)) {
                if (!mDestinationRequested) {
                    if (mMyPlace == null) {
                        MapsUtil.getMyPlace(mGoogleApiClient, new IMaps() {
                            @Override
                            public void processFinished(Object obj) {
                                mMyPlace = (Place) obj;
                                if (mMyPlace != null) {
                                    // Add marker
                                    if (mFromLatLng == null) {
                                        mMyLocMarker = addMarker(mMyLocMarker, mMyPlace.getLatLng(), R.drawable.ic_my_location, false);
                                    }

                                    fillAddress(mLblFrom, mMyPlace);
                                }
                            }
                        });
                    } else {
                        MapsUtil.moveCameraTo(mMap, mMyPlace.getLatLng());
                    }
                }
            } else {
                turnOnLocationReminder(RC_TURN_ON_LOCATION, true);
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
                            if (mMyPlace == null) {
                                MapsUtil.getMyPlace(mGoogleApiClient, new IMaps() {
                                    @Override
                                    public void processFinished(Object obj) {
                                        mMyPlace = (Place) obj;
                                        if (mMyPlace != null) {
                                            if (mDepartureRequested) {
                                                fillAddress(mLblFrom, mMyPlace);
                                            } else {
                                                MapsUtil.moveCameraTo(mMap, mMyPlace.getLatLng());

                                                // Add marker
                                                if (mFromLatLng == null) {
                                                    mMyLocMarker = addMarker(mMyLocMarker, mMyPlace.getLatLng(), R.drawable.ic_my_location, false);
                                                }
                                            }
                                        }
                                    }
                                });
                            } else {
                                if (mDepartureRequested) {
                                    fillAddress(mLblFrom, mMyPlace);
                                } else {
                                    MapsUtil.moveCameraTo(mMap, mMyPlace.getLatLng());

                                    // Add marker
                                    if (mFromLatLng == null) {
                                        mMyLocMarker = addMarker(mMyLocMarker, mMyPlace.getLatLng(), R.drawable.ic_my_location, false);
                                    }
                                }
                            }
                        } else {
                            turnOnLocationReminder(RC_TURN_ON_LOCATION, true);
                        }
                    } else {
                        showPermissionsReminder(RC_LOCATION, true);
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mLblFrom) {
            MapsUtil.getAutoCompletePlaces(self, RC_GET_DEPARTURE);
        } else if (view == mLblTo) {
            MapsUtil.getAutoCompletePlaces(self, RC_GET_DESTINATION);
        } else if (view == mImgLocDeparture) {
            showCurrentLocation(mImgLocDeparture);
        } else if (view == mImgLocDestination) {
            showCurrentLocation(mImgLocDestination);
        } else if (view == mLlTransport) {
            if (mRclTransport.getVisibility() == View.GONE) {
                mRclTransport.setVisibility(View.VISIBLE);
                mLlTransport.setBackgroundColor(getResources().getColor(R.color.transparent));
            } else {
                mRclTransport.setVisibility(View.GONE);
                mLlTransport.setBackground(getResources().getDrawable(R.drawable.bg_radius_gray));
            }
        } else if (view == mLlPassengerQuantity) {
            if (mRclPassengerQuantity.getVisibility() == View.GONE) {
                mRclPassengerQuantity.setVisibility(View.VISIBLE);
                mLlPassengerQuantity.setBackground(getResources().getDrawable(R.drawable.bg_radius_gray));
            } else {
                mRclPassengerQuantity.setVisibility(View.GONE);
                mLlPassengerQuantity.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        } else if (view == mLblIwanaride) {
            iwanaride();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (GlobalFunctions.locationIsGranted(self, RC_LOCATION, null)) {
            if (MapsUtil.locationIsEnable(self)) {
                if (mMyPlace == null) {
                    MapsUtil.getMyPlace(mGoogleApiClient, new IMaps() {
                        @Override
                        public void processFinished(Object obj) {
                            mMyPlace = (Place) obj;
                            if (mMyPlace != null) {
                                mMyLocMarker = addMarker(mMyLocMarker, mMyPlace.getLatLng(), R.drawable.ic_my_location, false);

                                if (mFromLatLng == null) {
                                    fillAddress(mLblFrom, mMyPlace);
                                }

                                // Determine whether a Geocoder is available.
                                if (!Geocoder.isPresent()) {
                                    Toast.makeText(self, R.string.service_not_available, Toast.LENGTH_LONG).show();
                                    return;
                                }

                                if (mDepartureRequested) {
                                    fillAddress(mLblFrom, mMyPlace);
                                } else if (mDestinationRequested) {
                                    fillAddress(mLblTo, mMyPlace);
                                }
                            }
                        }
                    });
                }
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
    public View getInfoWindow(Marker marker) {
        ViewGroup window = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_windowinfo_marker_driver, null);

        TextViewRegular lblMinutes = (TextViewRegular) window.findViewById(R.id.lbl_minutes);
        TextViewRegular lblName = (TextViewRegular) window.findViewById(R.id.lbl_driver_name);
        TextViewRegular lblTransport = (TextViewRegular) window.findViewById(R.id.lbl_type);
        TextViewRegular lblRateQuantity = (TextViewRegular) window.findViewById(R.id.lbl_rate_quantity);
        TextViewRegular lblFare = (TextViewRegular) window.findViewById(R.id.lbl_fare);
        RatingBar ratingBar = (RatingBar) window.findViewById(R.id.rating);

        if (!marker.equals(mMyLocMarker) && !marker.equals(mToMarker) && !marker.equals(mFromMarker)) {
            TransportDealObj transportDealObj = null;
            if (mNearbyDriverDeals != null) {
                if (mNearbyDriverDeals.size() > 0) {
                    for (int i = 0; i < mNearbyDriverDeals.size(); i++) {
                        transportDealObj = mNearbyDriverDeals.get(i);
                        if (marker.getTag().equals(transportDealObj.getDriverId())) {
                            break;
                        }
                    }
                }
            }

            if (transportDealObj != null) {
                String minutes = String.valueOf(transportDealObj.getDuration() / 60);
                lblMinutes.setText(String.format(getString(R.string.value_minutes_away), minutes));
                lblName.setText(transportDealObj.getDriverName());
                lblRateQuantity.setText(String.valueOf(transportDealObj.getRateQuantity()));
                ratingBar.setRating(transportDealObj.getRateOfDriver());

                if (transportDealObj.getEstimateFare() > 0) {
                    lblFare.setVisibility(View.VISIBLE);
                    lblFare.setText(String.format(getString(R.string.dollar_value), StringUtil.convertNumberToString(transportDealObj.getEstimateFare(), 1)));
                } else {
                    lblFare.setVisibility(View.INVISIBLE);
                }

                String transport = "";
                if (transportDealObj.getTransportType().equalsIgnoreCase(TransportObj.TAXI)) {
                    if (transportDealObj.driverIsDelivery()) {
                        transport = getString(R.string.taxi_delivery);
                    } else {
                        transport = getString(R.string.taxi);
                    }
                } else if (transportDealObj.getTransportType().equalsIgnoreCase(TransportObj.VIP)) {
                    if (transportDealObj.driverIsDelivery()) {
                        transport = getString(R.string.vip_delivery);
                    } else {
                        transport = getString(R.string.vip);
                    }
                } else if (transportDealObj.getTransportType().equalsIgnoreCase(TransportObj.LIFTS)) {
                    if (transportDealObj.driverIsDelivery()) {
                        transport = getString(R.string.lift_delivery);
                    } else {
                        transport = getString(R.string.lifts);
                    }
                } else if (transportDealObj.getTransportType().equalsIgnoreCase(TransportObj.MOTORBIKE)) {
                    if (transportDealObj.driverIsDelivery()) {
                        transport = getString(R.string.motor_delivery);
                    } else {
                        transport = getString(R.string.motorbike);
                    }
                }
                lblTransport.setText(transport);
            }

            return window;
        } else {
            return null;
        }
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        // Transfer trip info
        keepTripInfo();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Args.RECENT_CHAT_OBJ, mRecentChatObj);
        GlobalFunctions.startActivityWithoutAnimation(self, PassengerViewDriverActivity.class, bundle);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if (marker.equals(mFromMarker)) {
            fillAddress(mLblFrom, marker.getPosition());
        } else if (marker.equals(mToMarker)) {
            fillAddress(mLblTo, marker.getPosition());
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (!marker.equals(mMyLocMarker) && !marker.equals(mFromMarker) && !marker.equals(mToMarker)) {
            mSelectedDriverId = marker.getTag().toString();
        }

        return false;
    }

    private void initGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addApi(Places.GEO_DATA_API)
                    .enableAutoManage(this, this)
                    .build();
        }
    }

    private void showCurrentLocation(View view) {
        if (GlobalFunctions.locationIsGranted(self, RC_LOCATION, null)) {
            if (MapsUtil.locationIsEnable(self)) {
                // Only start the service to fetch the address if GoogleApiClient is
                // connected.
                if (mGoogleApiClient.isConnected() && mMyPlace != null) {
                    if (view == mImgLocDeparture) {
                        fillAddress(mLblFrom, mMyPlace);
                    } else if (view == mImgLocDestination) {
                        fillAddress(mLblTo, mMyPlace);
                    }
                }
            } else {
                MapsUtil.displayLocationSettingsRequest(self, RC_TURN_ON_LOCATION);
            }
        }

        // If GoogleApiClient isn't connected, process the user's request by
        // setting mAddressRequested to true. Later, when GoogleApiClient connects,
        // launch the service to fetch the address. As far as the user is
        // concerned, pressing the Fetch Address button
        // immediately kicks off the process of getting the address.
        if (view == mImgLocDeparture) {
            mDepartureRequested = true;
        } else if (view == mImgLocDestination) {
            mDestinationRequested = true;
        }
    }

    private void fillAddress(final TextViewRegular lblAddress, final LatLng latLng) {
        if (NetworkUtility.isNetworkAvailable()) {
            ModelManager.getAddressByLatlng(self, latLng, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject jsonObject = (JSONObject) object;
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            JSONObject firstResult = jsonArray.getJSONObject(0);
                            String address = firstResult.optString("formatted_address");
                            if (address.contains("\n")) {
                                address = address.replace("\n", ", ");
                            }
                            lblAddress.setText(address);

                            // Reset boolean var to void some bugs
                            if (lblAddress == mLblFrom) {
                                mDepartureRequested = false;
                                MapsUtil.moveCameraTo(mMap, latLng);

                                mFromMarker = addMarker(mFromMarker, latLng, R.drawable.ic_place_accent, true);

                                mFromLatLng = latLng;
                            } else if (lblAddress == mLblTo) {
                                mDestinationRequested = false;

                                mToMarker = addMarker(mToMarker, latLng, R.drawable.ic_place_red, true);

                                mToLatLng = latLng;
                            }

                            // Get nearby drivers
                            getNearbyDrivers();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError() {

                }
            });
        } else {
            Toast.makeText(self, R.string.msg_no_network, Toast.LENGTH_SHORT).show();
        }

        /*MapsUtil.startAddressService(self, new AddressResultReceiver(new Handler(), new IMaps() {
            @Override
            public void processFinished(Object obj) {
                String address = obj.toString();
                if (address.contains("\n")) {
                    address = address.replace("\n", ", ");
                }
                lblAddress.setText(address);

                // Reset boolean var to void some bugs
                if (lblAddress == mLblFrom) {
                    mDepartureRequested = false;
                    MapsUtil.moveCameraTo(mMap, latLng);

                    mFromMarker = addMarker(mFromMarker, latLng, R.drawable.ic_place_accent, true);

                    mFromLatLng = latLng;
                } else if (lblAddress == mLblTo) {
                    mDestinationRequested = false;

                    mToMarker = addMarker(mToMarker, latLng, R.drawable.ic_place_red, true);

                    mToLatLng = latLng;
                }

                // Get nearby drivers
                getNearbyDrivers();
            }
        }), latLng);*/
    }

    private void fillAddress(final TextViewRegular lblAddress, Place place) {
        LatLng latLng = place.getLatLng();
        String address = place.getAddress().toString();
        if (address.contains("\n")) {
            address = address.replace("\n", ", ");
        }
        lblAddress.setText(address);

        // Reset boolean var to void some bugs
        if (lblAddress == mLblFrom) {
            mDepartureRequested = false;
            MapsUtil.moveCameraTo(mMap, latLng);

            mFromMarker = addMarker(mFromMarker, latLng, R.drawable.ic_place_accent, true);

            mFromLatLng = latLng;
        } else if (lblAddress == mLblTo) {
            mDestinationRequested = false;

            mToMarker = addMarker(mToMarker, latLng, R.drawable.ic_place_red, true);

            mToLatLng = latLng;
        }

        // Get nearby drivers
        getNearbyDrivers();
    }

    private Marker addMarker(Marker marker, LatLng latLng, int icon, boolean draggable) {
        // Clear old marker if it's
        if (marker != null) {
            marker.remove();
        }

        return MapsUtil.addMarker(mMap, latLng, "", icon, draggable);
    }

    private void addDriverMarkers() {
        if (mNearbyDriverDeals != null) {
            if (mNearbyDriverDeals.size() > 0) {
                for (int i = 0; i < mNearbyDriverDeals.size(); i++) {
                    TransportDealObj transportDealObj = mNearbyDriverDeals.get(i);
                    Marker marker = addMarker(null, transportDealObj.getLatLngDriver(), getTransportIcon(transportDealObj), false);
                    marker.setTag(transportDealObj.getDriverId());
                    mDriverMarkers.add(marker);
                }
            }
        }
    }

    private void initTransportTypes() {
        ArrayList<TransportObj> transportObjs = new ArrayList<>();
        transportObjs.add(new TransportObj(TransportObj.ALL, getString(R.string.all), R.drawable.ic_wheel));
        transportObjs.add(new TransportObj(TransportObj.TAXI, getString(R.string.taxi), R.drawable.ic_taxi));
        transportObjs.add(new TransportObj(TransportObj.VIP, getString(R.string.vip), R.drawable.ic_vip));
        transportObjs.add(new TransportObj(TransportObj.LIFTS, getString(R.string.lifts), R.drawable.ic_lifts));
        transportObjs.add(new TransportObj(TransportObj.MOTORBIKE, getString(R.string.motorbike), R.drawable.ic_moto));
        transportObjs.add(new TransportObj(TransportObj.DELIVERY, getString(R.string.delivery), R.drawable.ic_delivery));

        // Fix for 'iwanadelivery' in left menu
        if (mSelectedTransportType.equalsIgnoreCase(TransportObj.DELIVERY)) {
            mImgTransportType.setImageResource(R.drawable.ic_delivery);
            mLblTransportType.setText(getString(R.string.delivery));
            setTitle(R.string.iwanadelivery);
            mLblIwanaride.setText(R.string.iwanadelivery);
        }

        TransportAdapter transportAdapter = new TransportAdapter(transportObjs, new ITransport() {
            @Override
            public void onTransportSelected(TransportObj transportObj) {
                mSelectedTransportType = transportObj.getType();
                mImgTransportType.setImageResource(transportObj.getIcon());
                mLblTransportType.setText(transportObj.getName());

                // Filter nearby drivers by transport type
                getNearbyDrivers();

                // Change text and title
                if (mSelectedTransportType.equalsIgnoreCase(TransportObj.DELIVERY)) {
                    setTitle(R.string.iwanadelivery);
                    mLblIwanaride.setText(R.string.iwanadelivery);
                } else {
                    setTitle(R.string.iwanaride);
                    mLblIwanaride.setText(R.string.iwanaride);
                }

                mRclTransport.setVisibility(View.GONE);
                mLlTransport.setBackground(getResources().getDrawable(R.drawable.bg_radius_gray));
            }
        });

        mRclTransport.setAdapter(transportAdapter);
    }

    private void initPassengerQuantity() {
        PassengerQuantityAdapter quantityAdapter = new PassengerQuantityAdapter(new IPassenger() {
            @Override
            public void onQuantitySelected(String s) {
                mLblPassengerQuantity.setText(s);

                mRclPassengerQuantity.setVisibility(View.GONE);
                mLlPassengerQuantity.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        });

        mRclPassengerQuantity.setAdapter(quantityAdapter);
    }

    private void iwanaride() {
        if (mFromLatLng == null) {
            Toast.makeText(self, R.string.msg_choose_pickup, Toast.LENGTH_SHORT).show();
        } else if (mToLatLng == null) {
            Toast.makeText(self, R.string.msg_choose_destination, Toast.LENGTH_SHORT).show();
        } else if (mSelectedDriverId == null || mSelectedDriverId.equals("")) {
            Toast.makeText(self, R.string.msg_choose_driver, Toast.LENGTH_SHORT).show();
        } else {
            // Transfer trip info
            keepTripInfo();

            RecentChatsActivity.start(self, mRecentChatObj);
        }
    }

    private void keepTripInfo() {
        if (mNearbyDriverDeals != null) {
            if (mNearbyDriverDeals.size() > 0) {
                for (int i = 0; i < mNearbyDriverDeals.size(); i++) {
                    if (mSelectedDriverId.equals(mNearbyDriverDeals.get(i).getDriverId())) {
                        mSelectedDriver = mNearbyDriverDeals.get(i);
                        break;
                    }
                }
            }
        }

        mSelectedDriver.setPassengerQuantity(Integer.parseInt(mLblPassengerQuantity.getText().toString()));
        mSelectedDriver.setPickup(mLblFrom.getText().toString());
        mSelectedDriver.setDestination(mLblTo.getText().toString());
        mSelectedDriver.setLatLngPickup(mFromLatLng);
        mSelectedDriver.setLatLngDestination(mToLatLng);
        mSelectedDriver.setPassengerId(DataStoreManager.getUser().getId());
        mSelectedDriver.setPassengerQBId(SharedPreferencesUtil.getQbUser().getId());

        mSelectedUsers = new ArrayList<>();
        /*QBUser qbUser = new QBUser();
        qbUser.setId(mSelectedDriver.getDriverQBId());
        qbUser.setLogin(mSelectedDriver.getDriverEmail());
        qbUser.setFullName(mSelectedDriver.getDriverName());
        String phone = DataStoreManager.getUser().getPhone() == null ? "" : DataStoreManager.getUser().getPhone();
        qbUser.setPhone(phone);*/

        QBUser qbUser = SharedPreferencesUtil.getQbUser();
        String phone = DataStoreManager.getUser().getPhone() == null ? "" : DataStoreManager.getUser().getPhone();
        qbUser.setPhone(phone);
        mSelectedUsers.add(qbUser);

        mRecentChatObj = new RecentChatObj(mSelectedDriver, null, qbUser);
    }

    private void getNearbyDrivers() {
        if (NetworkUtility.isNetworkAvailable()) {
            ModelManager.getNearbyDrivers(self, DataStoreManager.getUser().getToken(), mSelectedTransportType,
                    mFromLatLng, mToLatLng, new ModelManagerListener() {
                        @Override
                        public void onSuccess(Object object) {
                            JSONObject jsonObject = (JSONObject) object;
                            if (JSONParser.responseIsSuccess(jsonObject)) {
                                if (mNearbyDriverDeals == null) {
                                    mNearbyDriverDeals = new ArrayList<>();
                                    mDriverMarkers = new ArrayList<>();
                                } else {
                                    mNearbyDriverDeals.clear();
                                    // Remove old markers
                                    for (int i = 0; i < mDriverMarkers.size(); i++) {
                                        mDriverMarkers.get(i).remove();
                                    }
                                }

                                mNearbyDriverDeals = JSONParser.parseDrivers(jsonObject);

                                addDriverMarkers();
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

    private void buyDuration(String mode) {
        if (DataStoreManager.getUser().getDriverData() != null) {
            if (DataStoreManager.getUser().getDriverData().isActive()) {
                showDriverModeActivationDialog(mode);
            } else {
                Toast.makeText(self, R.string.msg_wait_active_driver, Toast.LENGTH_SHORT).show();
            }
        } else {
            GlobalFunctions.showConfirmationDialog(self, getString(R.string.msg_need_to_become_driver),
                    getString(R.string.yes), getString(R.string.no_thank), true, new IConfirmation() {
                        @Override
                        public void onPositive() {
                            GlobalFunctions.startActivityWithoutAnimation(self, BecomeAProActivity.class);
                        }

                        @Override
                        public void onNegative() {
                        }
                    });
        }
    }

    private void changeDriverMode(final String mode, int duration) {
        if (NetworkUtility.isNetworkAvailable()) {
            ModelManager.activateDriverMode(self, mode, duration, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject jsonObject = (JSONObject) object;
                    Toast.makeText(self, JSONParser.getMessage(jsonObject), Toast.LENGTH_SHORT).show();

                    if (!JSONParser.responseIsSuccess(jsonObject)) {
                        mMenu.findItem(R.id.action_available_for_driving).setChecked(mode.equals(Constants.OFF));
                    } else {
                        if (mode.equals(Constants.OFF)) {
                            // Set driver is unavailable
                            UserObj userObj = DataStoreManager.getUser();
                            userObj.getDriverData().setAvailable(DriverObj.DRIVER_UNAVAILABLE);
                            DataStoreManager.saveUser(userObj);

                            mMenu.findItem(R.id.action_available_for_driving).setChecked(false);
                        } else {
                            // Set driver is unavailable
                            UserObj userObj = DataStoreManager.getUser();
                            userObj.getDriverData().setAvailable(DriverObj.DRIVER_AVAILABLE);
                            DataStoreManager.saveUser(userObj);

                            mMenu.findItem(R.id.action_available_for_driving).setChecked(true);
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

    private void showDriverModeActivationDialog(final String mode) {
        final Dialog dialog = DialogUtil.setDialogCustomView(self, R.layout.dialog_buying_duration, false);

        final EditText txtDuration = (EditText) dialog.findViewById(R.id.txt_duration);
        final TextViewRegular lblFee = (TextViewRegular) dialog.findViewById(R.id.lbl_msg_fee);
        TextViewBold lblBuyCredits = (TextViewBold) dialog.findViewById(R.id.lbl_buy_credits);
        TextViewBold lblAvailable = (TextViewBold) dialog.findViewById(R.id.lbl_available);

        lblFee.setText(String.format(getString(R.string.msg_subtract_credits), "0"));

        SettingsObj settingsObj = DataStoreManager.getSettingUtility();
        final int feePerHour = (settingsObj != null && settingsObj.getDriver_online_rate() != null && !settingsObj.getDriver_online_rate().equals("")) ?
                Integer.parseInt(settingsObj.getDriver_online_rate()) : 1;

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

                        changeDriverMode(mode, duration);
                    }
                }
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                GlobalFunctions.closeKeyboard(self);
            }
        });

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }
}

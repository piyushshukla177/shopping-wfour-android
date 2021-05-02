package com.wfour.onlinestoreapp.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.wfour.onlinestoreapp.AppController;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.view.activities.DealDetailActivity;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.objects.DealObj;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.utils.map.MapsUtil;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suusoft on 12/8/2016.
 */

public class DealListByMapFragment extends com.wfour.onlinestoreapp.base.BaseFragment implements
        OnMapReadyCallback, GoogleMap.InfoWindowAdapter,
        GoogleMap.OnInfoWindowClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    // Use google api to get current location
    private List<DealObj> mDatas;
    private List<Marker> mMarkers;

    public static DealListByMapFragment newInstance(List<DealObj> objList) {
        DealListByMapFragment fragment = new DealListByMapFragment();
        fragment.setData(objList);
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.map;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(this);
        mMap.setOnInfoWindowClickListener(this);

        initMarker();
        mMap.setMyLocationEnabled(true);
        LatLng latLng = new LatLng(AppController.getInstance().getLatMyLocation(), AppController.getInstance().getLongMyLocation());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        mMap.animateCamera(cameraUpdate);

    }


    @Override
    public View getInfoWindow(Marker marker) {

        ViewGroup window = (ViewGroup) LayoutInflater.from(self).inflate(R.layout.layout_windowinfo_marker_deal, null);

        TextView lblName = (TextViewRegular) window.findViewById(R.id.lbl_driver_name);
        TextView lblRateQuantity = (TextViewRegular) window.findViewById(R.id.lbl_rate_quantity);
        TextView lblPrice = (TextViewRegular) window.findViewById(R.id.lbl_price);
        RatingBar ratingBar = (RatingBar) window.findViewById(R.id.rating);

        for (DealObj item : mDatas) {
            if (item.getId().equals(marker.getTag())) {
                lblName.setText(item.getName());
                lblRateQuantity.setText(String.valueOf(item.getRateQuantity()));
                lblPrice.setText(String.format(getString(R.string.dollar_value), StringUtil.convertNumberToString(item.getSale_price(), 1)));
                ratingBar.setRating(item.getRate());


            }
        }


        return window;

    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        for (DealObj item : mDatas) {
            if (item.getId().equals(marker.getTag())) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Args.KEY_DEAL_OBJECT, item);
                Intent intent = new Intent(getActivity(), DealDetailActivity.class);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 1);
                break;
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == DealDetailActivity.RC_UPDATE_DEAL) {
                DealObj dealObj = data.getExtras().getParcelable(Args.KEY_DEAL_OBJECT);
                int pos = 0;
                for (DealObj item : mDatas) {
                    if (item.getId().equals(dealObj.getId())) {
                        pos = mDatas.indexOf(item);
                        break;
                    }
                }
                mDatas.add(pos, dealObj);
                mDatas.remove(pos + 1);
                Log.e("xxxx", "xxxxxxx");
            }
        }

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

    @Override
    protected void getData() {

    }

    public void setData(List<DealObj> datas) {
        this.mDatas = datas;

    }

    private void initMarker() {
        Marker marker;
        mMarkers = new ArrayList<>();
        LatLng latLng;

        for (DealObj item : mDatas) {
            latLng = new LatLng(item.getLatitude(), item.getLongitude());
            marker = addMarker(null, latLng, R.drawable.ic_location_on_white, false);
            marker.setTag(item.getId());
            mMarkers.add(marker);
        }
    }


    private Marker addMarker(Marker marker, LatLng latLng, int icon, boolean draggable) {
        // Clear old marker if it's
        if (marker != null) {
            marker.remove();
        }

        return MapsUtil.addMarker(mMap, latLng, "", icon, draggable);
    }

}

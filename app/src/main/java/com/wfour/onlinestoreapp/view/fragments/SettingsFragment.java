package com.wfour.onlinestoreapp.view.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.SettingsObj;

import org.json.JSONObject;

/**
 * Created by Suusoft on 30/11/2016.
 */

public class SettingsFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = SettingsFragment.class.getName();
    private static final String ON = "1";
    private static final String OFF = "0";
    private Switch swNotifications, swMyFavourite, swTrans_Deli, swFoodBeverages, swLabor, swTravelling, swShopping, swNearByDeal, swNewAndEvent;

    //reskin
    private ProgressBar progressBar;
    private RelativeLayout btnSave;
    private TextView tvBtn;
    private boolean isChange = false;


    public static SettingsFragment newInstance() {

        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    View inflateLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }

    @Override
    void initUI(View view) {
        btnSave = (RelativeLayout) view.findViewById(R.id.btn_functions);
        tvBtn = (TextView) view.findViewById(R.id.tv_btn);
        tvBtn.setText(getResources().getString(R.string.button_save));
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        checkSetBg();

        swNotifications = (Switch) view.findViewById(R.id.sw_notifications);
        swMyFavourite = (Switch) view.findViewById(R.id.sw_my_favourite);
        swTrans_Deli = (Switch) view.findViewById(R.id.sw_transport_deliveries);
        swFoodBeverages = (Switch) view.findViewById(R.id.sw_food_beverages);
        swLabor = (Switch) view.findViewById(R.id.sw_labor);
        swTravelling = (Switch) view.findViewById(R.id.sw_travelling);
        swShopping = (Switch) view.findViewById(R.id.sw_shopping);
        swNearByDeal = (Switch) view.findViewById(R.id.sw_near_by_deal);
        swNewAndEvent = (Switch) view.findViewById(R.id.sw_new_and_event);

        swNotifications.setOnClickListener(this);
        swMyFavourite.setOnClickListener(this);
        swTrans_Deli.setOnClickListener(this);
        swLabor.setOnClickListener(this);
        swFoodBeverages.setOnClickListener(this);
        swTravelling.setOnClickListener(this);
        swShopping.setOnClickListener(this);
        swNearByDeal.setOnClickListener(this);
        swNewAndEvent.setOnClickListener(this);
    }

    private void checkSetBg() {
        if (isChange){
            btnSave.setOnClickListener(this);
            setBgBtnFunction();
        }else {
            btnSave.setOnClickListener(null);
            setBgBtnColor();
        }
    }

    @Override
    public void onClick(View v) {
        if (v==btnSave){
            saveSettings();
        }else {
            isChange = true;
            checkSetBg();
        }

    }

    private void setBgBtnColor(){
        btnSave.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }

    private void setBgBtnFunction(){
        btnSave.setBackgroundResource(R.drawable.bg_btn_gadient);
    }

    @Override
    void initControl() {
        swNotifications.setChecked(DataStoreManager.getSettingsNotify());
        swMyFavourite.setChecked(DataStoreManager.getSettingsMyFavourite());
        swFoodBeverages.setChecked(DataStoreManager.getSettingsFoodBeverages());
        swTrans_Deli.setChecked(DataStoreManager.getSettingsTransportDeliveries());
        swLabor.setChecked(DataStoreManager.getSettingsLabor());
        swTravelling.setChecked(DataStoreManager.getSettingsTravelling());
        swNearByDeal.setChecked(DataStoreManager.getSettingsNearByDeals());
        swShopping.setChecked(DataStoreManager.getSettingsShopping());
        swNewAndEvent.setChecked(DataStoreManager.getSettingsNewsAndEvents());

        getSettings(getActivity());
        swNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchSettings(b);
            }
        });
        if (!swNotifications.isChecked()) {
            switchSettings(false);
        }

        checkChange();
    }

    private void checkChange() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (isChange)
            saveSettings();
    }

    private void saveSettings() {
        progressBar.setVisibility(View.VISIBLE);
        String notifi;
        String notifiFav;
        String notifiFood;
        String notifiTran;
        String notifiTravel;
        String notifiLabor;
        String notifiShopping;
        String notifiNearby;
        String notifiNewAndEvent;
        if (swNotifications.isChecked()) {
            notifi = ON;
        } else {
            notifi = OFF;
        }
        if (swMyFavourite.isChecked()) {
            notifiFav = ON;
        } else {
            notifiFav = OFF;
        }
        if (swFoodBeverages.isChecked()) {
            notifiFood = ON;
        } else {
            notifiFood = OFF;
        }
        if (swTrans_Deli.isChecked()) {
            notifiTran = ON;
        } else {
            notifiTran = OFF;
        }
        if (swTravelling.isChecked()) {
            notifiTravel = ON;
        } else {
            notifiTravel = OFF;
        }
        if (swLabor.isChecked()) {
            notifiLabor = ON;
        } else {
            notifiLabor = OFF;
        }
        if (swShopping.isChecked()) {
            notifiShopping = ON;
        } else {
            notifiShopping = OFF;
        }
        if (swNearByDeal.isChecked()) {
            notifiNearby = ON;
        } else {
            notifiNearby = OFF;
        }
        if (swNewAndEvent.isChecked()) {
            notifiNewAndEvent = ON;
        } else {
            notifiNewAndEvent = OFF;
        }
        settings(notifi, notifiFav, notifiTran, notifiFood, notifiLabor, notifiTravel, notifiShopping, notifiNewAndEvent, notifiNearby);

        DataStoreManager.saveSettingsNotify(swNotifications.isChecked());
        DataStoreManager.saveSettingsMyFavourite(swMyFavourite.isChecked());
        DataStoreManager.saveSettingsFoodBeverages(swFoodBeverages.isChecked());
        DataStoreManager.saveSettingsTransportDeliveries(swTrans_Deli.isChecked());
        DataStoreManager.saveSettingsTravelling(swTravelling.isChecked());
        DataStoreManager.saveSettingsShopping(swShopping.isChecked());
        DataStoreManager.saveSettingsNearByDeals(swNearByDeal.isChecked());
        DataStoreManager.saveSettingsLabor(swLabor.isChecked());

    }

    private void settings(String notifi, String notifiFav, String notifiTran, String notifiFood, String notifiLabor, String notifiTravel, String notifiShopping, String notifiNew, String notifiNearby) {
        ModelManager.settings(getActivity(), notifi, notifiFav, notifiTran, notifiFood, notifiLabor, notifiTravel, notifiShopping, notifiNew, notifiNearby, new ModelManagerListener() {
            @Override
            public void onSuccess(Object object) {
                org.json.JSONObject jsonObject = (JSONObject) object;
                ApiResponse response = new ApiResponse(jsonObject);
                if (!response.isError()) {
                    Log.e(TAG, "Settings success");
                }

                progressBar.setVisibility(View.GONE);
                isChange = false;
                checkSetBg();

            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Settings ERROR!");
            }
        });
    }

    private void switchSettings(boolean b) {
        swMyFavourite.setEnabled(b);
        swFoodBeverages.setEnabled(b);
        swTrans_Deli.setEnabled(b);
        swLabor.setEnabled(b);
        swTravelling.setEnabled(b);
        swNearByDeal.setEnabled(b);
        swShopping.setEnabled(b);
        swNewAndEvent.setEnabled(b);
    }

    private void getSettings(Context context) {
        ModelManager.settings(context, "", "", "", "", "", "", "", "", "", new ModelManagerListener() {
            @Override
            public void onSuccess(Object object) {
                org.json.JSONObject jsonObject = (JSONObject) object;
                ApiResponse apiResponse = new ApiResponse(jsonObject);
                if (!apiResponse.isError()) {
                    SettingsObj settingsObj = apiResponse.getDataObject(SettingsObj.class);
                    if (settingsObj != null) {
                        DataStoreManager.saveSettingsNotify(settingsObj.getNotify());
                        DataStoreManager.saveSettingsMyFavourite(settingsObj.getNotify_favourite());
                        DataStoreManager.saveSettingsFoodBeverages(settingsObj.getNotify_food());
                        DataStoreManager.saveSettingsTransportDeliveries(settingsObj.getNotify_transport());
                        DataStoreManager.saveSettingsTravelling(settingsObj.getNotify_travel());
                        DataStoreManager.saveSettingsShopping(settingsObj.getNotify_shopping());
                        DataStoreManager.saveSettingsNearByDeals(settingsObj.getNotify_nearby());
                        DataStoreManager.saveSettingsLabor(settingsObj.getNotify_labor());
                        DataStoreManager.saveSettingsNewsAndEvents(settingsObj.getNotify_news());

                        swNotifications.setChecked(settingsObj.getNotify());
                        swMyFavourite.setChecked(settingsObj.getNotify_favourite());
                        swFoodBeverages.setChecked(settingsObj.getNotify_food());
                        swTrans_Deli.setChecked(settingsObj.getNotify_transport());
                        swLabor.setChecked(settingsObj.getNotify_labor());
                        swTravelling.setChecked(settingsObj.getNotify_travel());
                        swNearByDeal.setChecked(settingsObj.getNotify_nearby());
                        swShopping.setChecked(settingsObj.getNotify_shopping());
                        swNewAndEvent.setChecked(settingsObj.getNotify_news());
                    }
                }
            }

            @Override
            public void onError() {
                Log.e(TAG, "Get Setting ERROR!");
            }
        });
    }


}

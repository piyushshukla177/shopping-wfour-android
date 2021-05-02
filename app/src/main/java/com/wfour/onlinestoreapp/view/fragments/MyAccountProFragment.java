package com.wfour.onlinestoreapp.view.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.wfour.onlinestoreapp.AppController;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.view.activities.BecomeAProActivity;
import com.wfour.onlinestoreapp.view.activities.PhoneCountryListActivity;
import com.wfour.onlinestoreapp.view.adapters.SpinnerTypeOfTransportAdapter;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IConfirmation;
import com.wfour.onlinestoreapp.interfaces.IObserver;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.network1.NetworkUtility;
import com.wfour.onlinestoreapp.objects.DataPart;
import com.wfour.onlinestoreapp.objects.DriverObj;
import com.wfour.onlinestoreapp.objects.ProObj;
import com.wfour.onlinestoreapp.objects.TransportObj;
import com.wfour.onlinestoreapp.objects.UserObj;
import com.wfour.onlinestoreapp.objects.VehicleObject;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.ImageUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.utils.map.MapsUtil;
import com.wfour.onlinestoreapp.widgets.textview.TextViewBold;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Suusoft on 01/12/2016.
 */

public class MyAccountProFragment extends BaseFragment implements View.OnClickListener, TextWatcher, IObserver {
    public static final String TAG = MyAccountProFragment.class.getName();

    private static final int FLAG_CHOSE_CAR = 1;
    private static final int FLAG_CHOSE_CERTIFICATION = 2;
    private static final int FLAG_CAR = 3;
    private static final int FLAG_CERTIFICATION = 4;

    public static final int RC_CAMERA_PERMISSION = 1;

    public static final int REQUEST_CODE_CAPTURE_IMAGE = 1323;

    public static final int REQUEST_CODE_BECOME_PRO = 1234;
    private static final int RC_ADDRESS = 254;

    private static final int CACULATE = 1;
    private static final int YOUR_COST = 2;

    private EditText edtBusinessName, edtEmail, edtPhone, edtAddress;

    private EditText edtCarMake, edtModel, edtYear, edtAmountKilometresPerYear, edtNumYearUseCar, edtRoadTaxePerYear, edtAverageConsumptionOfCar, edtPricePerLiter, edtCostOfGarage, edtInsuranceCosts, edtMaintenanceCosts, edtPriceNewTyres, edtValueCarSell, edtUnexpectedCost, edtValueYourCarBought, edtCostsPerKilometer, edtCostsYouChargePerKilometer, edtCarColor, edtLicensePlateNumber, edtYourEstimateMargin;
    private ImageView imgCar, imgChoseCar, imgCertification, imgChoseCertification;

    private LinearLayout llParentDriverInfo;
    private LinearLayout llParentYourCost;
    private LinearLayout llParentCaculate;
    private TextViewRegular tvUseCaculate;
    private TextViewRegular tvUseYourCost;

    private FrameLayout frContainCheckbox;
    private TextViewRegular btnEdit;
    private ImageView imgAvatar;

    private Spinner spnTypeOfTransport;
    private Spinner spnFuel;

    private LinearLayout ll_parent_edt_email;

    private CheckBox ckbDriver;
    private CheckBox ckbDelivery;
    private CheckBox ckbBecomePro;

    private TextViewRegular tvMsgActivedDriver, lblBusinessName;

    private FrameLayout fr_divider;

    private TextViewRegular tvTermAndConditions;
    private TextViewBold btnSave, btnBecomePro;
    private TextView tvPhoneCode;

    private RelativeLayout rltRoot;
    private LinearLayout llRoot;


    private LayoutInflater inflater;
    private ViewGroup viewGroup;
    private Bundle bundle;


    private boolean isEdit = false;
    private boolean isPro;
    private int flag;
    private int flagCost;
    private String fareType;

    private ArrayAdapter mFuelAdapter;
    private ArrayAdapter mTypeOfTransportAdapter;

    private String itemFuel;
    private String itemTypeOfTransport;

    private UserObj userObj;

    private boolean isCreateView;

    private MyAccountFragment fragment;

    private View.OnFocusChangeListener listenerFocusAddress = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean isFocus) {
            if (isFocus) {
                MapsUtil.getAutoCompletePlaces(MyAccountProFragment.this, RC_ADDRESS);
            }
        }
    };

    public static MyAccountProFragment newInstance() {
        Bundle args = new Bundle();
        MyAccountProFragment fragment = new MyAccountProFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    View inflateLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        bundle = savedInstanceState;
        viewGroup = container;

        userObj = DataStoreManager.getUser();
        if (userObj.getProData() == null) {

            isCreateView = true;
//            return inflater.inflate(R.layout.layout_become_apro, container, false);
        } else {

            isCreateView = false;
//            return inflater.inflate(R.layout.activity_become_a_pro, container, false);
        }
        return inflater.inflate(R.layout.activity_become_a_pro, container, false);
    }

    @Override
    void initUI(View view) {

        edtBusinessName = (EditText) view.findViewById(R.id.edt_bussiness_name);
        edtEmail = (EditText) view.findViewById(R.id.edt_email);
        edtPhone = (EditText) view.findViewById(R.id.edt_phone_number);
        edtAddress = (EditText) view.findViewById(R.id.edt_address);
        edtCarMake = (EditText) view.findViewById(R.id.edt_car_make);
        edtModel = (EditText) view.findViewById(R.id.edt_model);
        edtYear = (EditText) view.findViewById(R.id.edt_year);
        edtAmountKilometresPerYear = (EditText) view.findViewById(R.id.edt_amount_kilometres_per_year);
        edtNumYearUseCar = (EditText) view.findViewById(R.id.edt_num_year_use_car);
        edtRoadTaxePerYear = (EditText) view.findViewById(R.id.edt_road_taxes_per_year);
        edtAverageConsumptionOfCar = (EditText) view.findViewById(R.id.edt_average_consumption_of_car);
        edtPricePerLiter = (EditText) view.findViewById(R.id.edt_price_per_liter);
        edtCostOfGarage = (EditText) view.findViewById(R.id.edt_cost_of_garage);
        edtInsuranceCosts = (EditText) view.findViewById(R.id.edt_insurance_costs);
        edtMaintenanceCosts = (EditText) view.findViewById(R.id.edt_maintenance_costs);
        edtPriceNewTyres = (EditText) view.findViewById(R.id.edt_price_new_tyres);
        edtValueCarSell = (EditText) view.findViewById(R.id.edt_value_of_car);
        edtUnexpectedCost = (EditText) view.findViewById(R.id.edt_unexpected_cost);
        edtValueYourCarBought = (EditText) view.findViewById(R.id.edt_value_your_car_bought);

        edtCostsPerKilometer = (EditText) view.findViewById(R.id.edt_cost_per_kilometer);
        edtCostsYouChargePerKilometer = (EditText) view.findViewById(R.id.edt_cost_you_charge_per_kilometer);
        edtYourEstimateMargin = (EditText) view.findViewById(R.id.edt_your_estimate);
        edtYourEstimateMargin.setEnabled(false);
        edtValueYourCarBought.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    edtCostsPerKilometer.setFocusable(false);
                    edtCostsYouChargePerKilometer.requestFocus();
                }
                return false;
            }
        });
        edtCarColor = (EditText) view.findViewById(R.id.edt_car_color);
        edtLicensePlateNumber = (EditText) view.findViewById(R.id.edt_license_plate_number);
        imgCar = (ImageView) view.findViewById(R.id.imgCar);
        imgChoseCar = (ImageView) view.findViewById(R.id.img_chose_image_car);
        imgCertification = (ImageView) view.findViewById(R.id.img_certification);
        imgChoseCertification = (ImageView) view.findViewById(R.id.img_chose_certification);
        llParentDriverInfo = (LinearLayout) view.findViewById(R.id.ll_parent_info_driver);
        frContainCheckbox = (FrameLayout) view.findViewById(R.id.fr_content);
        //btnEdit = (TextViewRegular) view.findViewById(R.id.btn_edit_infomation);
        spnTypeOfTransport = (Spinner) view.findViewById(R.id.spn_type_of_transport);
        spnFuel = (Spinner) view.findViewById(R.id.spn_fuel);
        //ll_parent_edt_email = (LinearLayout) view.findViewById(R.id.ll_parent_email);
        ckbDriver = (CheckBox) view.findViewById(R.id.ckb_become_driver);
        tvMsgActivedDriver = (TextViewRegular) view.findViewById(R.id.tv_active_driver);
        fr_divider = (FrameLayout) view.findViewById(R.id.fr_pro_divider);
        //lblBusinessName = (TextViewRegular) view.findViewById(R.id.lbl_bussiness_name);
        lblBusinessName.setText(getString(R.string.hint_bussiness_name));
        ckbDelivery = (CheckBox) view.findViewById(R.id.ckb_delivery);
        btnSave = (TextViewBold) view.findViewById(R.id.btn_finish);
        tvPhoneCode = (TextView) view.findViewById(R.id.tv_phone_code);

        llParentYourCost = (LinearLayout) view.findViewById(R.id.ll_parent_your_cost);
        llParentCaculate = (LinearLayout) view.findViewById(R.id.ll_parent_caculate);
        tvUseYourCost = (TextViewRegular) view.findViewById(R.id.tv_use_your_cost);
        tvUseCaculate = (TextViewRegular) view.findViewById(R.id.tv_use_caculate);


        rltRoot = (RelativeLayout) view.findViewById(R.id.rlt_root);
        llRoot = (LinearLayout) view.findViewById(R.id.ll_parent_content);

        ckbBecomePro = (CheckBox) view.findViewById(R.id.ckb_become_pro);
        tvTermAndConditions = (TextViewRegular) view.findViewById(R.id.tv_terms_and_conditions);
        btnBecomePro = (TextViewBold) view.findViewById(R.id.btn_become_pro);

        if (userObj.getProData() != null) {
            rltRoot.setVisibility(View.GONE);
            llRoot.setVisibility(View.VISIBLE);
        } else {
            rltRoot.setVisibility(View.VISIBLE);
            llRoot.setVisibility(View.GONE);
        }
    }

    @Override
    void initControl() {
        if (userObj.getProData() != null) {
//            imgCar.setOnClickListener(this);
            imgChoseCar.setOnClickListener(this);
//            imgCertification.setOnClickListener(this);
            imgChoseCertification.setOnClickListener(this);
            btnSave.setOnClickListener(this);
            btnEdit.setOnClickListener(this);
            tvPhoneCode.setOnClickListener(this);
            edtAddress.setTag(edtAddress.getKeyListener());
            edtAddress.setKeyListener(null);
//            edtAddress.setOnClickListener(this);
//            edtAddress.setOnFocusChangeListener(listenerFocusAddress);
            edtCostsPerKilometer.setKeyListener(null);
            llParentCaculate.setOnClickListener(this);
            llParentYourCost.setOnClickListener(this);

            if (userObj.getDriverData() != null) {
                if (userObj.getDriverData().getIs_active() == DriverObj.DRIVER_ACTIVED) {
                    if (ckbDriver.isChecked()) {
                        llParentDriverInfo.setVisibility(View.VISIBLE);
                    } else {
                        llParentDriverInfo.setVisibility(View.GONE);
                    }

                    frContainCheckbox.setVisibility(View.VISIBLE);
                    if (getActivity() != null)
                        ckbDriver.setText(getString(R.string.i_was_a_driver));
                    fr_divider.setVisibility(View.GONE);
                    ckbDriver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b) {
                                llParentDriverInfo.setVisibility(View.VISIBLE);
                            } else {
                                llParentDriverInfo.setVisibility(View.GONE);
                            }
                        }
                    });
                } else {
                    tvMsgActivedDriver.setVisibility(View.VISIBLE);
                    llParentDriverInfo.setVisibility(View.GONE);
                    frContainCheckbox.setVisibility(View.GONE);
                }
            } else {
                frContainCheckbox.setVisibility(View.VISIBLE);
                if (ckbDriver.isChecked()) {
                    llParentDriverInfo.setVisibility(View.VISIBLE);
                } else {
                    llParentDriverInfo.setVisibility(View.GONE);
                }
                ckbDriver.setText(getString(R.string.i_m_a_driver));
                ckbDriver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            llParentDriverInfo.setVisibility(View.VISIBLE);
                        } else {
                            llParentDriverInfo.setVisibility(View.GONE);
                        }
                    }
                });
            }
            setUpSpinner();
            enableEdit(false);
            setData(DataStoreManager.getUser());
        } else {
            ckbBecomePro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    isPro = b;
                }
            });
            tvTermAndConditions.setOnClickListener(this);
            btnBecomePro.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View view) {
        if (view == btnEdit) {
            isEdit = !isEdit;
            if (isEdit) {
                imgChoseCar.setVisibility(View.VISIBLE);
                imgChoseCertification.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.VISIBLE);
                btnSave.setText(R.string.button_save_and_submit);
                btnEdit.setBackgroundResource(R.drawable.bg_pressed_radius_accent);
                edtBusinessName.requestFocus();
                edtAddress.setOnFocusChangeListener(listenerFocusAddress);
            } else {
                setData(DataStoreManager.getUser());
                imgChoseCar.setVisibility(View.GONE);
                imgChoseCertification.setVisibility(View.GONE);
                btnSave.setVisibility(View.GONE);
                btnSave.setText(R.string.button_save_and_submit);
                btnEdit.setBackgroundResource(R.drawable.bg_pressed_radius_grey);
                edtAddress.setOnFocusChangeListener(null);
            }
            enableEdit(isEdit);
        } else if (view == imgChoseCar) {
            flag = FLAG_CHOSE_CAR;
            processChoseImage();
//            AppUtil.pickImage(this, AppUtil.PICK_IMAGE_REQUEST_CODE);
        } else if (view == imgChoseCertification) {
            flag = FLAG_CHOSE_CERTIFICATION;
            processChoseImage();
//            AppUtil.pickImage(this, AppUtil.PICK_IMAGE_REQUEST_CODE);
        } else if (view == imgCar) {
            flag = FLAG_CAR;
            AppUtil.pickImage(this, AppUtil.PICK_IMAGE_REQUEST_CODE);
        } else if (view == imgCertification) {
            flag = FLAG_CERTIFICATION;
            AppUtil.pickImage(this, AppUtil.PICK_IMAGE_REQUEST_CODE);
        } else if (view == btnSave) {
            if (userObj.getProData() != null) {
                if (NetworkUtility.getInstance(getActivity()).isNetworkAvailable()) {
                    if (isEdit) {
                        if (isValid()) {
                            updateProfilePro();
                        }
                    } else {
                        AppUtil.showToast(getActivity(), R.string.msg_enable_edit);
                    }
                } else {
                    AppUtil.showToast(getActivity(), R.string.msg_network_not_available);
                }
            }
        } else if (view == tvTermAndConditions) {
            AppUtil.linkToWeb(getActivity(), DataStoreManager.getSettingUtility().getTerm());

        } else if (view == btnBecomePro) {
            if (isPro) {
                startActivityForResult(new Intent(getActivity(), BecomeAProActivity.class), REQUEST_CODE_BECOME_PRO);
            } else {
                AppUtil.showToast(getActivity(), R.string.msg_accept_the_terms_conditions);
            }

        } else if (view == tvPhoneCode) {
            Intent intent = new Intent(getActivity(), PhoneCountryListActivity.class);
            startActivityForResult(intent, Args.RQ_GET_PHONE_CODE);
        } else if (view == edtAddress) {
            MapsUtil.getAutoCompletePlaces(this, RC_ADDRESS);
        } else if (view == llParentCaculate) {
            if (isEdit) {
                processChoseFareCaculate();
            }
        } else if (view == llParentYourCost) {
            if (isEdit) {
                processChoseYourFare();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppUtil.PICK_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                switch (flag) {
                    case FLAG_CAR:
                        AppUtil.setImageFromUri(imgCar, data.getData());
                        break;
                    case FLAG_CHOSE_CAR:
                        AppUtil.setImageFromUri(imgCar, data.getData());
                        break;
                    case FLAG_CERTIFICATION:
                        AppUtil.setImageFromUri(imgCertification, data.getData());
                        break;
                    case FLAG_CHOSE_CERTIFICATION:
                        AppUtil.setImageFromUri(imgCertification, data.getData());
                        break;
                }
            }
        } else if (requestCode == REQUEST_CODE_BECOME_PRO) {
            if (resultCode == BecomeAProActivity.RESULT_CODE_BECOME_PRO) {
                refreshLayout();
//                fragment.reFreshFragmentPro();
            }
        } else if (requestCode == Args.RQ_GET_PHONE_CODE && resultCode == Activity.RESULT_OK) {
            String countryCodeSelected = data.getExtras().getString(Args.KEY_PHONE_CODE);
            tvPhoneCode.setText(countryCodeSelected);
        }
        if (requestCode == RC_ADDRESS) {
            if (resultCode == -1) {
                Place place = PlaceAutocomplete.getPlace(self, data);
                AppUtil.fillAddress(getActivity(), edtAddress, place);
                edtCarMake.requestFocus();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(self, data);
                edtCarMake.requestFocus();
            } else if (resultCode == 0) {
                edtCarMake.requestFocus();
                // The user canceled the operation.
            }
        }
        if (requestCode == REQUEST_CODE_CAPTURE_IMAGE && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            if (photo != null) {
                Bitmap bitmap = ImageUtil.decodeBitmapFromBitmap(photo, 300, 300);
                if (flag == FLAG_CHOSE_CAR) {
                    imgCar.setImageBitmap(bitmap);
                } else if (flag == FLAG_CHOSE_CERTIFICATION) {
                    imgCertification.setImageBitmap(bitmap);
                }

            } else {
                if (flag == FLAG_CHOSE_CAR) {
                    AppUtil.setImageFromUri(imgCar, data.getData());
                } else if (flag == FLAG_CHOSE_CERTIFICATION) {
                    AppUtil.setImageFromUri(imgCertification, data.getData());
                }

            }

        }
    }

    private boolean isValid() {
        String bussinessName = edtBusinessName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
//        if (userObj.getAvatar().equals("")) {
//            if (imgAvatar.getTag() == null) {
//                AppUtil.showToast(getActivity(), R.string.msg_update_avatar);
//                return false;
//            }
//        }
        if (StringUtil.isEmpty(bussinessName)) {
            AppUtil.showToast(getActivity(), R.string.msg_fill_bussiness_name);
            edtBusinessName.requestFocus();
            return false;
        }
        if (StringUtil.isEmpty(email)) {
            AppUtil.showToast(getActivity(), R.string.msg_fill_email);
            edtEmail.requestFocus();
            return false;
        } else if (!StringUtil.isValidEmail(email)) {
            AppUtil.showToast(getActivity(), R.string.msg_email_is_required);
            edtEmail.requestFocus();
            return false;
        }
        if (StringUtil.isEmpty(phone)) {
            AppUtil.showToast(getActivity(), R.string.msg_phone_is_required);
            edtPhone.requestFocus();
            return false;
        }
        if (StringUtil.isEmpty(address)) {
            AppUtil.showToast(getActivity(), R.string.msg_address_is_required);
            edtAddress.requestFocus();
            return false;
        }
        if (userObj.getDriverData() != null && userObj.getDriverData().getIs_active() == DriverObj.DRIVER_ACTIVED || ckbDriver.isChecked()) {
            String carMake = edtCarMake.getText().toString().trim();
            String model = edtModel.getText().toString().trim();
            String year = edtYear.getText().toString().trim();
            String amount_kilometres_per_year = edtAmountKilometresPerYear.getText().toString().trim();
            String roadTaxesPerYear = edtRoadTaxePerYear.getText().toString().trim();
            String averageConsumptionOfCar = edtAverageConsumptionOfCar.getText().toString().trim();
            String pricePerLiter = edtPricePerLiter.getText().toString().trim();
            String insuranceCost = edtInsuranceCosts.getText().toString().trim();
            String maintenanceCost = edtMaintenanceCosts.getText().toString().trim();
            String valueYourCarBought = edtValueYourCarBought.getText().toString().trim();
            String carColor = edtCarColor.getText().toString().trim();
            String license_plate_number = edtLicensePlateNumber.getText().toString().trim();
            String costsYouChargePerKilometer = edtCostsYouChargePerKilometer.getText().toString();

            if (StringUtil.isEmpty(carMake)) {
                AppUtil.showToast(getActivity(), R.string.msg_car_make);
                edtCarMake.requestFocus();
                return false;
            }

            if (StringUtil.isEmpty(model)) {
                AppUtil.showToast(getActivity(), R.string.msg_model_is_required);
                edtModel.requestFocus();
                return false;
            }
            if (StringUtil.isEmpty(year)) {
                AppUtil.showToast(getActivity(), R.string.msg_year_is_required);
                edtYear.requestFocus();
                return false;
            } else {
                Calendar calendar = Calendar.getInstance();
                int years = calendar.get(Calendar.YEAR);
                int yearCurrent = Integer.parseInt(edtYear.getText().toString());
                if (yearCurrent > years || yearCurrent < Constants.LIMIT_YEAR) {
                    AppUtil.showToast(getActivity(), R.string.msg_year_is_invalid);
                    edtYear.requestFocus();
                    return false;
                }
            }
            if (StringUtil.isEmpty(carColor)) {
                AppUtil.showToast(getActivity(), R.string.msg_car_color);
                edtCarColor.requestFocus();
                return false;
            }
            if (StringUtil.isEmpty(license_plate_number)) {
                AppUtil.showToast(getActivity(), R.string.msg_license_plate_number);
                edtLicensePlateNumber.requestFocus();
                return false;
            }
            if (StringUtil.isEmpty(amount_kilometres_per_year)) {
                AppUtil.showToast(getActivity(), R.string.msg_amount_kilometres_is_required);
                edtAmountKilometresPerYear.requestFocus();
                return false;
            }
            if (StringUtil.isEmpty(roadTaxesPerYear)) {
                AppUtil.showToast(getActivity(), R.string.msg_road_taxes_yearly_car_required);
                edtRoadTaxePerYear.requestFocus();
                return false;
            }
            if (StringUtil.isEmpty(averageConsumptionOfCar)) {
                AppUtil.showToast(getActivity(), R.string.msg_average_consumption_of_your_car_required);
                edtAverageConsumptionOfCar.requestFocus();
                return false;
            }
            if (StringUtil.isEmpty(pricePerLiter)) {
                AppUtil.showToast(getActivity(), R.string.msg_price_per_liter_is_required);
                edtPricePerLiter.requestFocus();
                return false;
            }
            if (StringUtil.isEmpty(insuranceCost)) {
                AppUtil.showToast(getActivity(), R.string.msg_insurance_cost_is_required);
                edtInsuranceCosts.requestFocus();
                return false;
            }
            if (StringUtil.isEmpty(maintenanceCost)) {
                AppUtil.showToast(getActivity(), R.string.msg_maintence_cost_is_required);
                edtMaintenanceCosts.requestFocus();
                return false;
            }
            if (StringUtil.isEmpty(valueYourCarBought)) {
                AppUtil.showToast(getActivity(), R.string.msg_value_your_car_bought_is_required);
                edtUnexpectedCost.requestFocus();
                return false;
            }
            if (flagCost == YOUR_COST) {
                if (StringUtil.isEmpty(costsYouChargePerKilometer)) {
                    AppUtil.showToast(getActivity(), R.string.msg_cost_you_charge_per_kilometer_is_required);
                    edtCostsYouChargePerKilometer.requestFocus();
                    return false;
                }
            }

        }

        return true;
    }

    private void enableEdit(boolean isEdit) {
        edtBusinessName.setEnabled(isEdit);
        edtEmail.setEnabled(isEdit);
        edtAddress.setEnabled(isEdit);
        edtPhone.setEnabled(isEdit);
        edtCarMake.setEnabled(isEdit);
        edtModel.setEnabled(isEdit);
        edtYear.setEnabled(isEdit);
        edtAmountKilometresPerYear.setEnabled(isEdit);
        edtNumYearUseCar.setEnabled(isEdit);
        edtRoadTaxePerYear.setEnabled(isEdit);
        edtAverageConsumptionOfCar.setEnabled(isEdit);
        edtPricePerLiter.setEnabled(isEdit);
        edtCostOfGarage.setEnabled(isEdit);
        edtInsuranceCosts.setEnabled(isEdit);
        edtMaintenanceCosts.setEnabled(isEdit);
        edtPriceNewTyres.setEnabled(isEdit);
        edtValueCarSell.setEnabled(isEdit);
        edtUnexpectedCost.setEnabled(isEdit);
        edtValueYourCarBought.setEnabled(isEdit);
        tvPhoneCode.setEnabled(isEdit);
        ckbDelivery.setEnabled(isEdit);
        spnFuel.setEnabled(isEdit);
        spnTypeOfTransport.setEnabled(isEdit);
        if (flagCost == YOUR_COST) {
            edtCostsYouChargePerKilometer.setEnabled(isEdit);
        }
        edtCarColor.setEnabled(isEdit);
        edtLicensePlateNumber.setEnabled(isEdit);
        if (isEdit) {
            tvUseYourCost.setBackgroundResource(R.drawable.bg_pressed_radius_primary);
            tvUseCaculate.setBackgroundResource(R.drawable.bg_pressed_radius_primary);
        } else {
            tvUseYourCost.setBackgroundResource(R.drawable.bg_pressed_radius_grey);
            tvUseCaculate.setBackgroundResource(R.drawable.bg_pressed_radius_grey);
        }
    }

    private void getCountryCode() {
        String[] rl = getResources().getStringArray(R.array.CountryCodes);
       // int curPosition = AppUtil.getCurentPositionCountryCode(getActivity());
        int curPosition = 0;
        String phoneCode = rl[curPosition].split(",")[0];
        tvPhoneCode.setText(phoneCode);
    }

    private void setData(UserObj userObj) {
        if (getActivity() != null) {
            setTextChangeListener();
            btnSave.setVisibility(View.GONE);
            imgChoseCar.setVisibility(View.GONE);
            imgChoseCertification.setVisibility(View.GONE);
            ProObj proObj = userObj.getProData();
            DriverObj driverObj = userObj.getDriverData();
            VehicleObject vehicleObject = userObj.getVehicleObject();
            if (proObj != null) {
                edtBusinessName.setText(proObj.getname());
                edtEmail.setText(proObj.getBusiness_email());
                edtAddress.setText(proObj.getBusiness_address());
                edtPhone.setText(proObj.getPhoneNumber());
                if (proObj.getPhoneCode().isEmpty()) {
                    getCountryCode();
                } else {
                    tvPhoneCode.setText(proObj.getPhoneCode());
                }
                ll_parent_edt_email.setVisibility(View.VISIBLE);
                edtBusinessName.setHint(getString(R.string.bussiness_name));
            }

            if (driverObj != null && vehicleObject != null && driverObj.getIs_active() == DriverObj.DRIVER_ACTIVED) {
                if (vehicleObject != null) {
                    edtCarMake.setText(vehicleObject.getBrand());
                    edtModel.setText(vehicleObject.getModel());
                    edtYear.setText(vehicleObject.getYear());
                    edtCarColor.setText(vehicleObject.getColor());
                    edtLicensePlateNumber.setText(vehicleObject.getPlate());
                    edtAmountKilometresPerYear.setText(StringUtil.convertNumberToString(vehicleObject.getYearly_km()));
                    edtNumYearUseCar.setText(StringUtil.convertNumberToString(vehicleObject.getYear_intend()));
                    edtRoadTaxePerYear.setText(StringUtil.convertNumberToString(vehicleObject.getYearly_tax()));
                    edtAverageConsumptionOfCar.setText(StringUtil.convertNumberToString(vehicleObject.getAverage_consumption()));
                    edtPricePerLiter.setText(StringUtil.convertNumberToString(vehicleObject.getFuel_unit_price(), 1));
                    edtCostOfGarage.setText(StringUtil.convertNumberToString(vehicleObject.getYearly_gara()));
                    edtInsuranceCosts.setText(StringUtil.convertNumberToString(vehicleObject.getYearly_insurance()));
                    edtMaintenanceCosts.setText(StringUtil.convertNumberToString(vehicleObject.getYearly_maintenance()));
                    edtPriceNewTyres.setText(StringUtil.convertNumberToString(vehicleObject.getPrice_4_new_tyres()));
                    edtValueCarSell.setText(StringUtil.convertNumberToString(vehicleObject.getSold_value()));
                    edtUnexpectedCost.setText(StringUtil.convertNumberToString(vehicleObject.getYearly_unexpected()));
                    edtValueYourCarBought.setText(StringUtil.convertNumberToString(vehicleObject.getBought_value()));
                    ImageUtil.setImage(getActivity(), imgCar, vehicleObject.getImage(), 600, 400);
                    String[] arrFuel = getResources().getStringArray(R.array.fuel);
                    List<String> listFuels = Arrays.asList(arrFuel);
                    for (int i = 0; i < listFuels.size(); i++) {
                        if (vehicleObject.getFuel_type().equals(listFuels.get(i))) {
                            spnFuel.setSelection(i);
                        }
                    }
                }
                if (driverObj.getIs_delivery() == 1) {
                    ckbDelivery.setChecked(true);
                } else {
                    ckbDelivery.setChecked(false);
                }
                ImageUtil.setImage(getActivity(), imgCertification, driverObj.getDriver_license(), 600, 400);
                edtCostsPerKilometer.setText(StringUtil.convertNumberToString(calculateCostPerKilometers(), 1));
//            edtCostsYouChargePerKilometer.setText(StringUtil.convertNumberToString(driverObj.getFare(), 2));

                ArrayList<TransportObj> transportObjs = TransportObj.getListTranSports(getActivity());
                for (int i = 0; i < transportObjs.size(); i++) {
                    if (driverObj.getType().equals(transportObjs.get(i).getType())) {
                        spnTypeOfTransport.setSelection(i);
                    }
                }
                if (driverObj.getFare_type() != null && driverObj.getFare_type().equals(Constants.MANUAL)) {
                    processChoseYourFare();
                    edtCostsYouChargePerKilometer.setText(StringUtil.convertNumberToString(driverObj.getFare(), 1));
                    edtCostsYouChargePerKilometer.setEnabled(false);

                } else if (driverObj.getFare_type() != null && driverObj.getFare_type().equals(Constants.AUTO)) {
                    processChoseFareCaculate();
                    edtCostsYouChargePerKilometer.setText(StringUtil.convertNumberToString(0, 1));
                }

            } else {
                edtCarMake.setText("");
                edtModel.setText("");
                edtYear.setText("");
                edtAmountKilometresPerYear.setText("");
                edtNumYearUseCar.setText("");
                edtRoadTaxePerYear.setText("");
                edtAverageConsumptionOfCar.setText("");
                edtPricePerLiter.setText("");
                edtCostOfGarage.setText("");
                edtInsuranceCosts.setText("");
                edtMaintenanceCosts.setText("");
                edtPriceNewTyres.setText("");
                edtValueCarSell.setText("");
                edtUnexpectedCost.setText("");
                edtValueYourCarBought.setText("");
                edtCarColor.setText("");
                edtLicensePlateNumber.setText("");
                edtCostsPerKilometer.setText("");
                edtCostsYouChargePerKilometer.setText("");
                spnFuel.setSelection(0);
                spnTypeOfTransport.setSelection(0);
                setValuesDefaut();

                flagCost = CACULATE;
                fareType = Constants.AUTO;
                tvUseCaculate.setVisibility(View.GONE);
                tvUseYourCost.setVisibility(View.VISIBLE);
                llParentCaculate.setBackgroundResource(R.color.transparent);
                llParentYourCost.setBackgroundResource(R.color.grey);
                edtCostsYouChargePerKilometer.setEnabled(false);
                edtYourEstimateMargin.setText(StringUtil.convertNumberToString(getActivity(), 0, 1));
            }
        }

    }

    private void setValuesDefaut() {
        edtNumYearUseCar.setText("3");
        edtCostOfGarage.setText("0");
        edtUnexpectedCost.setText("0");
        edtPriceNewTyres.setText("1000");
//        double valueSell = (Double.parseDouble(edtValueYourCarBought.getText().toString()) * 10) / 100;
//        edtValueCarSell.setText(String.valueOf(valueSell));
    }

    private void setUpSpinner() {

        if (getActivity() != null) {
            String[] arrFuel = getResources().getStringArray(R.array.fuel);
            final List<String> listFuels = Arrays.asList(arrFuel);
            mFuelAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, listFuels);
            mFuelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnFuel.setAdapter(mFuelAdapter);
            spnFuel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    itemFuel = listFuels.get(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            final ArrayList<TransportObj> transportObjs = TransportObj.getListTranSports(getActivity());
            mTypeOfTransportAdapter = new SpinnerTypeOfTransportAdapter(getActivity(), android.R.layout.simple_spinner_item, transportObjs);
            mTypeOfTransportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnTypeOfTransport.setAdapter(mTypeOfTransportAdapter);
            spnTypeOfTransport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    itemTypeOfTransport = transportObjs.get(i).getType();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

    }


    private void updateProfilePro() {
        String bussinessName = edtBusinessName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phoneNumber = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String phoneCode = tvPhoneCode.getText().toString().trim();
        String phone = phoneCode + " " + phoneNumber;

        if (userObj.getDriverData() != null && userObj.getDriverData().getIs_active() == DriverObj.DRIVER_ACTIVED || ckbDriver.isChecked()) {
            if (ckbDriver.isChecked()) {
                String cost = "";
                String carMake = edtCarMake.getText().toString().trim();
                String model = edtModel.getText().toString().trim();
                String year = edtYear.getText().toString().trim();
                String amount_kilometres_per_year = edtAmountKilometresPerYear.getText().toString().trim().replaceAll(",", "");
                String numYearUseCar = edtNumYearUseCar.getText().toString().trim().replaceAll(",", "");
                String roadTaxesPerYear = edtRoadTaxePerYear.getText().toString().trim().replaceAll(",", "");
                String averageConsumptionOfCar = edtAverageConsumptionOfCar.getText().toString().trim().replaceAll(",", "");
                String pricePerLiter = edtPricePerLiter.getText().toString().trim().replaceAll(",", "");
                String costOfGarage = edtCostOfGarage.getText().toString().trim().replaceAll(",", "");
                String insuranceCost = edtInsuranceCosts.getText().toString().trim().replaceAll(",", "");
                String maintenanceCost = edtMaintenanceCosts.getText().toString().trim().replaceAll(",", "");
                String priceNewTyres = edtPriceNewTyres.getText().toString().trim().replaceAll(",", "");
                String valueCarSell = edtValueCarSell.getText().toString().trim().replaceAll(",", "");
                String unexpectedCost = edtUnexpectedCost.getText().toString().trim().replaceAll(",", "");
                String valueYourCarBought = edtValueYourCarBought.getText().toString().trim().replaceAll(",", "");
                String carColor = edtCarColor.getText().toString().trim();
                String license_plate_number = edtLicensePlateNumber.getText().toString().trim().replaceAll(",", "");
                String yourCost = edtCostsYouChargePerKilometer.getText().toString().trim().replaceAll(",", "");
                String caculate = edtCostsPerKilometer.getText().toString().trim().replaceAll(",", "");
                switch (flagCost) {
                    case CACULATE:
                        cost = caculate;
                        break;
                    case YOUR_COST:
                        cost = yourCost;
                        break;
                }

                DataPart car = null;
                DataPart certification = null;
                DataPart avatar = null;
                if (numYearUseCar.isEmpty()) {
                    numYearUseCar = "3";
                }
                if (costOfGarage.isEmpty()) {
                    costOfGarage = "0";
                }
                if (unexpectedCost.isEmpty()) {
                    unexpectedCost = "0";
                }
                if (priceNewTyres.isEmpty()) {
                    priceNewTyres = "1000";
                }
                if (valueCarSell.isEmpty()) {
                    double valueSell = (Double.parseDouble(edtValueYourCarBought.getText().toString()) * 10) / 100;
                    valueCarSell = String.valueOf(valueSell);
                }
//            if (imgAvatar.getDrawable() != null) {
//                avatar = new DataPart("avatar.jpg", AppUtil.getFileDataFromDrawable(getActivity(), imgAvatar.getDrawable()), DataPart.TYPE_IMAGE);
//            }
                if (imgCar.getDrawable() != null) {
                    car = new DataPart("car.jpg", AppUtil.getFileDataFromDrawable(getActivity(), imgCar.getDrawable()), DataPart.TYPE_IMAGE);
                }
                if (imgCertification.getDrawable() != null) {
                    certification = new DataPart("certification.jpg", AppUtil.getFileDataFromDrawable(getActivity(), imgCertification.getDrawable()), DataPart.TYPE_IMAGE);
                }
                ModelManager.updateProfileProDriver(getActivity(), bussinessName, avatar, email, phone, address, ckbDelivery.isChecked(), carMake, model, year, carColor, license_plate_number, itemTypeOfTransport, itemFuel, amount_kilometres_per_year, numYearUseCar, roadTaxesPerYear, costOfGarage, averageConsumptionOfCar, pricePerLiter, insuranceCost, maintenanceCost, priceNewTyres, unexpectedCost, valueCarSell, valueYourCarBought, cost, fareType, car, certification, new ModelManagerListener() {
                    @Override
                    public void onSuccess(Object object) {
                        try {
                            JSONObject jsonObject = new JSONObject(object.toString());
                            ApiResponse response = new ApiResponse(jsonObject);
                            if (!response.isError()) {
                                UserObj user = response.getDataObject(UserObj.class);
                                user.setToken(userObj.getToken());
                                user.setRememberMe(DataStoreManager.getUser().isRememberMe());
                                DataStoreManager.saveUser(user);
                                AppController.getInstance().setUserUpdated(true);
                                userObj = user;
                                AppUtil.showToast(getActivity(), R.string.msg_update_success);

                                enableEdit(false);
                                btnEdit.setBackgroundResource(R.drawable.bg_pressed_radius_grey);
                                isEdit = false;
                                btnSave.setVisibility(View.GONE);
                                imgChoseCar.setVisibility(View.GONE);
                                imgChoseCertification.setVisibility(View.GONE);
                                initControl();
//                                setData(user);
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
        if (userObj.getProData() != null && !ckbDriver.isChecked()) {
            DataPart avatar = null;
//            if (imgAvatar.getDrawable() != null) {
//                avatar = new DataPart("avatar.jpg", AppUtil.getFileDataFromDrawable(getActivity(), imgAvatar.getDrawable()), DataPart.TYPE_IMAGE);
//            }
            ModelManager.updateProfilePro(getActivity(), bussinessName, avatar, email, phone, address, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    try {
                        JSONObject jsonObject = new JSONObject(object.toString());
                        ApiResponse response = new ApiResponse(jsonObject);
                        if (!response.isError()) {
                            UserObj user = response.getDataObject(UserObj.class);
                            user.setToken(userObj.getToken());
                            user.setRememberMe(DataStoreManager.getUser().isRememberMe());
                            DataStoreManager.saveUser(user);
                            AppController.getInstance().setUserUpdated(true);
                            AppUtil.showToast(getActivity(), R.string.msg_update_success);

                            enableEdit(false);
                            btnEdit.setBackgroundResource(R.drawable.bg_pressed_radius_grey);
                            isEdit = false;

                            btnSave.setVisibility(View.GONE);
                            setData(user);
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

    public boolean isPro() {
        return isPro;
    }

    public void setPro(boolean pro) {
        isPro = pro;
    }


    private double calculateCostPerKilometers() {
        String amoutKilometresPerYear = edtAmountKilometresPerYear.getText().toString();
        String averageConsumptionOfCar = edtAverageConsumptionOfCar.getText().toString();
        String pricePerLiter = edtPricePerLiter.getText().toString();
        String priceNewTyres = edtPriceNewTyres.getText().toString();
        String valueYourCarBought = edtValueYourCarBought.getText().toString();
        String valueCarSell = edtValueCarSell.getText().toString();
        String numYearUseCar = edtNumYearUseCar.getText().toString();
        String roadTaxePerYear = edtRoadTaxePerYear.getText().toString();
        String costOfGarage = edtCostOfGarage.getText().toString();
        String insuranceCosts = edtInsuranceCosts.getText().toString();
        String maintenanceCosts = edtMaintenanceCosts.getText().toString();
        String unexpectedCost = edtUnexpectedCost.getText().toString();
        double valueCarSells;
        if (numYearUseCar.isEmpty()) {
            numYearUseCar = "3.0";
        }
        if (costOfGarage.isEmpty()) {
            costOfGarage = "0.0";
        }
        if (unexpectedCost.isEmpty()) {
            unexpectedCost = "0.0";
        }
        if (priceNewTyres.isEmpty()) {
            priceNewTyres = "1000.0";
        }
        if (valueCarSell.isEmpty()) {
            valueCarSells = (StringUtil.convertStringToNumberDouble(valueYourCarBought) * 10) / 100;
        } else {
            valueCarSells = StringUtil.convertStringToNumberDouble(valueCarSell);
        }
        if (!amoutKilometresPerYear.isEmpty() && !averageConsumptionOfCar.isEmpty() && !pricePerLiter.isEmpty() && !priceNewTyres.isEmpty() && !valueYourCarBought.isEmpty() && !valueCarSell.isEmpty() && !numYearUseCar.isEmpty() && !roadTaxePerYear.isEmpty() && !costOfGarage.isEmpty() && !insuranceCosts.isEmpty() && !maintenanceCosts.isEmpty() && !unexpectedCost.isEmpty()) {
            double yearly_fuel_consumption = StringUtil.convertStringToNumberDouble(amoutKilometresPerYear) / StringUtil.convertStringToNumberDouble(averageConsumptionOfCar);

            double yearly_fuel_costs = yearly_fuel_consumption * StringUtil.convertStringToNumberDouble(pricePerLiter);
            double yearly_tyre_costs = (StringUtil.convertStringToNumberDouble(priceNewTyres) / 35000) * StringUtil.convertStringToNumberDouble(amoutKilometresPerYear);
            double yearly_depreciation_costs = (StringUtil.convertStringToNumberDouble(valueYourCarBought) - valueCarSells) / StringUtil.convertStringToNumberDouble(numYearUseCar);

            double costPerKilometers = (yearly_fuel_costs + yearly_tyre_costs + yearly_depreciation_costs + StringUtil.convertStringToNumberDouble(roadTaxePerYear) + StringUtil.convertStringToNumberDouble(costOfGarage) + StringUtil.convertStringToNumberDouble(insuranceCosts) + StringUtil.convertStringToNumberDouble(maintenanceCosts) + StringUtil.convertStringToNumberDouble(unexpectedCost)) / StringUtil.convertStringToNumberDouble(amoutKilometresPerYear);

            return costPerKilometers;
        } else {
            return 0;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (!charSequence.toString().isEmpty()) {
            edtCostsPerKilometer.setText(StringUtil.convertNumberToString(calculateCostPerKilometers(), 1));

        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void setTextChangeListener() {
        edtValueYourCarBought.addTextChangedListener(this);
        edtAmountKilometresPerYear.addTextChangedListener(this);
        edtAverageConsumptionOfCar.addTextChangedListener(this);
        edtPricePerLiter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    if (StringUtil.convertStringToNumberDouble(charSequence.toString()) <= 9999.9) {
                        edtCostsPerKilometer.setText(StringUtil.convertNumberToString(calculateCostPerKilometers(), 1));
                    } else {
                        edtPricePerLiter.setText("9999.9");
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtPriceNewTyres.addTextChangedListener(this);
        edtValueYourCarBought.addTextChangedListener(this);
        edtValueCarSell.addTextChangedListener(this);
        edtNumYearUseCar.addTextChangedListener(this);
        edtRoadTaxePerYear.addTextChangedListener(this);
        edtCostOfGarage.addTextChangedListener(this);
        edtInsuranceCosts.addTextChangedListener(this);
        edtMaintenanceCosts.addTextChangedListener(this);
        edtUnexpectedCost.addTextChangedListener(this);
    }

    private void processChoseImage() {
        GlobalFunctions.showConfirmationDialog(self, getString(R.string.title_chose_image),
                getString(R.string.from_gallery), getString(R.string.from_camera), true, new IConfirmation() {
                    @Override
                    public void onPositive() {
                        AppUtil.pickImage(MyAccountProFragment.this, AppUtil.PICK_IMAGE_REQUEST_CODE);
                    }

                    @Override
                    public void onNegative() {
                        if (GlobalFunctions.isMarshmallow()) {
                            if (self.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                AppUtil.captureImage(MyAccountProFragment.this, REQUEST_CODE_CAPTURE_IMAGE);
                            } else {
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, RC_CAMERA_PERMISSION);
                            }
                        } else {
                            AppUtil.captureImage(MyAccountProFragment.this, REQUEST_CODE_CAPTURE_IMAGE);
                        }
                    }
                });
    }

    private void processChoseFareCaculate() {
        flagCost = CACULATE;
        fareType = Constants.AUTO;
        tvUseCaculate.setVisibility(View.VISIBLE);
        tvUseYourCost.setVisibility(View.GONE);
        llParentCaculate.setBackgroundResource(R.color.transparent);
        llParentYourCost.setBackgroundResource(R.color.grey);
        edtCostsYouChargePerKilometer.setEnabled(false);
        edtCostsYouChargePerKilometer.setText("0.0");
        edtCostsYouChargePerKilometer.setSelectAllOnFocus(false);
        edtYourEstimateMargin.setText(StringUtil.convertNumberToString(getActivity(), 0, 1));

    }

    private void processChoseYourFare() {
        flagCost = YOUR_COST;
        fareType = Constants.MANUAL;
        tvUseCaculate.setVisibility(View.GONE);
        tvUseYourCost.setVisibility(View.VISIBLE);
        llParentCaculate.setBackgroundResource(R.color.grey);
        llParentYourCost.setBackgroundResource(R.color.transparent);

        edtCostsYouChargePerKilometer.setEnabled(true);

        edtCostsYouChargePerKilometer.setSelectAllOnFocus(true);
        edtCostsYouChargePerKilometer.selectAll();
        edtCostsYouChargePerKilometer.requestFocus();
        AppUtil.showKeyboard(getActivity(), edtCostsYouChargePerKilometer);


        double costYourChargePerKm = StringUtil.convertStringToNumberDouble(edtCostsYouChargePerKilometer.getText().toString());
        double costCaculate = StringUtil.convertStringToNumberDouble(edtCostsPerKilometer.getText().toString());
        double costYourEstimate = costYourChargePerKm - costCaculate;
        edtYourEstimateMargin.setText(StringUtil.convertNumberToString(getActivity(), costYourEstimate, 1));
        edtCostsYouChargePerKilometer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    double costYourChargePerKm = StringUtil.convertStringToNumberDouble(charSequence.toString());
                    double costCaculate = StringUtil.convertStringToNumberDouble(edtCostsPerKilometer.getText().toString());
                    double costYourEstimate = costYourChargePerKm - costCaculate;
                    edtYourEstimateMargin.setText(StringUtil.convertNumberToString(getActivity(), costYourEstimate, 1));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public boolean isCreateView() {
        return isCreateView;
    }


    @Override
    public void update() {
        refreshLayout();
    }

    private void refreshLayout() {
        userObj = DataStoreManager.getUser();
        if (userObj.getProData() != null) {
            rltRoot.setVisibility(View.GONE);
            llRoot.setVisibility(View.VISIBLE);
        } else {
            rltRoot.setVisibility(View.VISIBLE);
            llRoot.setVisibility(View.GONE);
        }
        if (getActivity() != null)
            initControl();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RC_CAMERA_PERMISSION) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    AppUtil.captureImage(this, REQUEST_CODE_CAPTURE_IMAGE);
                } else {
                    showPermissionReminder();
                }
            }
        }
    }

    private void showPermissionReminder() {
        GlobalFunctions.showConfirmationDialog(self, getString(R.string.msg_remind_user_grant_camera_permission),
                getString(R.string.allow), getString(R.string.no), true, new IConfirmation() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onPositive() {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, RC_CAMERA_PERMISSION);
                    }

                    @Override
                    public void onNegative() {
                    }
                });
    }
}

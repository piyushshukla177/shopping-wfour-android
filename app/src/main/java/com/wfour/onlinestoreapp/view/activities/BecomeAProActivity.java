package com.wfour.onlinestoreapp.view.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.view.adapters.SpinnerTypeOfTransportAdapter;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IConfirmation;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.network1.NetworkUtility;
import com.wfour.onlinestoreapp.objects.DataPart;
import com.wfour.onlinestoreapp.objects.ProObj;
import com.wfour.onlinestoreapp.objects.TransportObj;
import com.wfour.onlinestoreapp.objects.UserObj;
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
 * Created by Suusoft on 08/12/2016.
 */

public class BecomeAProActivity extends com.wfour.onlinestoreapp.base.BaseActivity implements View.OnClickListener, TextWatcher {
    public static final int RESULT_CODE_BECOME_PRO = 34;

    private static final int FLAG_CHOSE_CAR = 1;
    private static final int FLAG_CHOSE_CERTIFICATION = 2;
    private static final int FLAG_CAR = 3;
    private static final int FLAG_CERTIFICATION = 4;

    public static final int RC_CAMERA_PERMISSION = 1455;

    public static final int REQUEST_CODE_CAPTURE_IMAGE = 1323;

    private static final int CACULATE = 1;
    private static final int YOUR_COST = 2;

    private static final String TAG = BecomeAProActivity.class.getName();
    private static final int RC_ADDRESS = 6655;

    private EditText edtBussinessName, edtEmail, edtPhone, edtAddress;

    private AppCompatCheckBox ckbDriver;
    private EditText edtCarMake, edtModel, edtYear, edtAmountKilometresPerYear, edtNumYearUseCar,
            edtRoadTaxePerYear, edtAverageConsumptionOfCar, edtPricePerLiter, edtCostOfGarage,
            edtInsuranceCosts, edtMaintenanceCosts, edtPriceNewTyres, edtValueCarSell,
            edtUnexpectedCost, edtValueYourCarBought, edtCostsPerKilometer, edtCostsYouChargePerKilometer,
            edtCarColor, edtLicensePlateNumber, edtYourEstimateMargin;
    private ImageView imgCar, imgChoseCar, imgCertification, imgChoseCertification;
    private TextViewBold btnFinish;

    private LinearLayout llParentDriverInfo;
    private LinearLayout ll_parent_edt_email;
    private FrameLayout frContainCheckbox;
    private Spinner spnTypeOfTransport;
    private Spinner spnFuel;
    private TextViewRegular btnEdit, lblBusinessName;
    private CheckBox ckbDelivery;
    private TextView tvPhoneCode;

    private LinearLayout llParentYourCost;
    private LinearLayout llParentCaculate;
    private TextViewRegular tvUseCaculate;
    private TextViewRegular tvUseYourCost;

    private int flag;
    private int flagCost;
    private String fareType;
    String cost = "0.0";
    double auto_caculate;

    private ArrayAdapter mFuelAdapter;
    private ArrayAdapter mTypeOfTransportAdapter;
    private String itemFuel;
    private String itemTypeOfTransport;


    private View.OnFocusChangeListener listenerFocusAddress = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean isFocus) {
            if (isFocus) {
                MapsUtil.getAutoCompletePlaces(BecomeAProActivity.this, RC_ADDRESS);
            }
        }
    };


    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_become_a_pro;
    }

    @Override
    protected void getExtraData(Intent intent) {

    }

    @Override
    protected void initilize() {

    }

    @Override
    protected void initView() {
        edtBussinessName = (EditText) findViewById(R.id.edt_bussiness_name);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtEmail.setEnabled(true);
        edtPhone = (EditText) findViewById(R.id.edt_phone_number);
        edtAddress = (EditText) findViewById(R.id.edt_address);
        ckbDriver = (AppCompatCheckBox) findViewById(R.id.ckb_become_driver);
        edtCarMake = (EditText) findViewById(R.id.edt_car_make);
        edtModel = (EditText) findViewById(R.id.edt_model);
        edtYear = (EditText) findViewById(R.id.edt_year);
        edtCostsPerKilometer = (EditText) findViewById(R.id.edt_cost_per_kilometer);
        edtCostsYouChargePerKilometer = (EditText) findViewById(R.id.edt_cost_you_charge_per_kilometer);
        edtCarColor = (EditText) findViewById(R.id.edt_car_color);
        edtLicensePlateNumber = (EditText) findViewById(R.id.edt_license_plate_number);

        edtAmountKilometresPerYear = (EditText) findViewById(R.id.edt_amount_kilometres_per_year);
        edtNumYearUseCar = (EditText) findViewById(R.id.edt_num_year_use_car);
        edtRoadTaxePerYear = (EditText) findViewById(R.id.edt_road_taxes_per_year);
        edtAverageConsumptionOfCar = (EditText) findViewById(R.id.edt_average_consumption_of_car);
        edtPricePerLiter = (EditText) findViewById(R.id.edt_price_per_liter);
        edtCostOfGarage = (EditText) findViewById(R.id.edt_cost_of_garage);
        edtInsuranceCosts = (EditText) findViewById(R.id.edt_insurance_costs);
        edtMaintenanceCosts = (EditText) findViewById(R.id.edt_maintenance_costs);
        edtPriceNewTyres = (EditText) findViewById(R.id.edt_price_new_tyres);
        edtValueCarSell = (EditText) findViewById(R.id.edt_value_of_car);
        edtUnexpectedCost = (EditText) findViewById(R.id.edt_unexpected_cost);
        edtValueYourCarBought = (EditText) findViewById(R.id.edt_value_your_car_bought);



        edtCostsYouChargePerKilometer.setEnabled(false);
        edtYourEstimateMargin = (EditText) findViewById(R.id.edt_your_estimate);
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


        imgCar = (ImageView) findViewById(R.id.imgCar);
        imgChoseCar = (ImageView) findViewById(R.id.img_chose_image_car);
        imgCertification = (ImageView) findViewById(R.id.img_certification);
        imgChoseCertification = (ImageView) findViewById(R.id.img_chose_certification);
        btnFinish = (TextViewBold) findViewById(R.id.btn_finish);
        llParentDriverInfo = (LinearLayout) findViewById(R.id.ll_parent_info_driver);
        frContainCheckbox = (FrameLayout) findViewById(R.id.fr_content);
        spnTypeOfTransport = (Spinner) findViewById(R.id.spn_type_of_transport);
        spnFuel = (Spinner) findViewById(R.id.spn_fuel);
        ll_parent_edt_email = (LinearLayout) findViewById(R.id.ll_parent_email);
        btnEdit = (TextViewRegular) findViewById(R.id.btn_edit_infomation);
        ckbDelivery = (CheckBox) findViewById(R.id.ckb_delivery);
        lblBusinessName = (TextViewRegular) findViewById(R.id.lbl_bussiness_name);
        lblBusinessName.setText(getString(R.string.hint_bussiness_name));
        tvPhoneCode = (TextView) findViewById(R.id.tv_phone_code);

        llParentYourCost = (LinearLayout) findViewById(R.id.ll_parent_your_cost);
        llParentCaculate = (LinearLayout) findViewById(R.id.ll_parent_caculate);
        tvUseYourCost = (TextViewRegular) findViewById(R.id.tv_use_your_cost);
        tvUseCaculate = (TextViewRegular) findViewById(R.id.tv_use_caculate);
    }

    @Override
    protected void onViewCreated() {
        setToolbarTitle(R.string.become_pro);
        setUpSpinner();
//        ckbDriver.setText(getString(R.string.i_m_a_driver));
        edtBussinessName.requestFocus();
        llParentDriverInfo.setVisibility(View.GONE);
        btnEdit.setVisibility(View.GONE);
        edtCostsPerKilometer.setKeyListener(null);
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
        ll_parent_edt_email.setVisibility(View.VISIBLE);
        btnFinish.setVisibility(View.VISIBLE);
        btnFinish.setOnClickListener(this);
//        imgCar.setOnClickListener(this);
        imgChoseCar.setOnClickListener(this);
//        imgCertification.setOnClickListener(this);
        imgChoseCertification.setOnClickListener(this);
        tvPhoneCode.setOnClickListener(this);
        edtAddress.setTag(edtAddress.getKeyListener());
        edtAddress.setKeyListener(null);
//        edtAddress.setOnClickListener(this);
        edtAddress.setOnFocusChangeListener(listenerFocusAddress);
        llParentCaculate.setOnClickListener(this);
        llParentYourCost.setOnClickListener(this);


        setData();
    }

    @Override
    public void onClick(View view) {
        if (view == imgChoseCar) {
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
        } else if (view == btnFinish) {
            if (NetworkUtility.getInstance(getApplicationContext()).isNetworkAvailable()) {
                if (isValid()) {
                    becomePro();
                }
            } else {
                AppUtil.showToast(getApplicationContext(), R.string.msg_network_not_available);
            }

        }
        else if (view == tvPhoneCode) {
            Intent intent = new Intent(this, PhoneCountryListActivity.class);
            startActivityForResult(intent, Args.RQ_GET_PHONE_CODE);
        }
        else if (view == edtAddress) {
            Log.e("EEE", "EEE");
            MapsUtil.getAutoCompletePlaces(this, RC_ADDRESS);
        } else if (view == llParentCaculate) {
            flagCost = CACULATE;
            fareType = Constants.AUTO;
            tvUseCaculate.setVisibility(View.VISIBLE);
            tvUseYourCost.setVisibility(View.GONE);
            llParentCaculate.setBackgroundResource(R.color.transparent);
            llParentYourCost.setBackgroundResource(R.color.grey);
            edtCostsYouChargePerKilometer.setEnabled(false);
            edtYourEstimateMargin.setText("0.0");
            edtCostsYouChargePerKilometer.setText("0.0");

        } else if (view == llParentYourCost) {
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
            AppUtil.showKeyboard(getApplicationContext(), edtCostsYouChargePerKilometer);

            double costYourChargePerKm = StringUtil.convertStringToNumberDouble(edtCostsYouChargePerKilometer.getText().toString());
            double costCaculate = StringUtil.convertStringToNumberDouble(edtCostsPerKilometer.getText().toString());
            double costYourEstimate = costYourChargePerKm - costCaculate;
            edtYourEstimateMargin.setText(StringUtil.convertNumberToString(this, costYourEstimate, 1));
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
                        edtYourEstimateMargin.setText(StringUtil.convertNumberToString(getApplicationContext(), costYourEstimate, 1));
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        } else if (requestCode == Args.RQ_GET_PHONE_CODE && resultCode == Activity.RESULT_OK) {
            String countryCodeSelected = data.getExtras().getString(Args.KEY_PHONE_CODE);
            tvPhoneCode.setText(countryCodeSelected);
        }
        if (requestCode == RC_ADDRESS) {
            if (resultCode == -1) {
                Place place = PlaceAutocomplete.getPlace(self, data);
                AppUtil.fillAddress(this, edtAddress, place);
                edtCarMake.requestFocus();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(self, data);
                edtCarMake.requestFocus();
                Log.e("DealNewFragment", status.getStatusMessage());
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
        String bussinessName = edtBussinessName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        if (StringUtil.isEmpty(bussinessName)) {
            AppUtil.showToast(this, R.string.msg_fill_bussiness_name);
            edtBussinessName.requestFocus();
            return false;
        }
        if (StringUtil.isEmpty(email)) {
            AppUtil.showToast(this, R.string.msg_fill_email);
            edtEmail.requestFocus();
            return false;
        } else if (!StringUtil.isValidEmail(email)) {
            AppUtil.showToast(this, R.string.msg_email_is_required);
            edtEmail.requestFocus();
            return false;
        }
        if (StringUtil.isEmpty(phone)) {
            AppUtil.showToast(this, R.string.msg_phone_is_required);
            edtPhone.requestFocus();
            return false;
        }
        if (StringUtil.isEmpty(address)) {
            AppUtil.showToast(this, R.string.msg_address_is_required);
            edtAddress.requestFocus();
            return false;
        }
        if (ckbDriver.isChecked()) {
            String carMake = edtCarMake.getText().toString().trim();
            String model = edtModel.getText().toString().trim();
            String year = edtYear.getText().toString().trim();
            String amount_kilometres_per_year = edtAmountKilometresPerYear.getText().toString().trim();
            String numYearUseCar = edtNumYearUseCar.getText().toString().trim();
            String roadTaxesPerYear = edtRoadTaxePerYear.getText().toString().trim();
            String averageConsumptionOfCar = edtAverageConsumptionOfCar.getText().toString().trim();
            String pricePerLiter = edtPricePerLiter.getText().toString().trim();
            String costOfGarage = edtCostOfGarage.getText().toString().trim();
            String insuranceCost = edtInsuranceCosts.getText().toString().trim();
            String maintenanceCost = edtMaintenanceCosts.getText().toString().trim();
            String priceNewTyres = edtPriceNewTyres.getText().toString().trim();
            String valueCarSell = edtValueCarSell.getText().toString().trim();
            String unexpectedCost = edtUnexpectedCost.getText().toString().trim();
            String valueYourCarBought = edtValueYourCarBought.getText().toString().trim();
            String carColor = edtCarColor.getText().toString().trim();
            String license_plate_number = edtLicensePlateNumber.getText().toString().trim();
            String costsYouChargePerKilometer = edtCostsYouChargePerKilometer.getText().toString();

            if (StringUtil.isEmpty(carMake)) {
                AppUtil.showToast(this, R.string.msg_car_make);
                edtCarMake.requestFocus();
                return false;
            }

            if (StringUtil.isEmpty(model)) {
                AppUtil.showToast(this, R.string.msg_model_is_required);
                edtModel.requestFocus();
                return false;
            }
            if (StringUtil.isEmpty(year)) {
                AppUtil.showToast(this, R.string.msg_year_is_required);
                edtYear.requestFocus();
                return false;
            } else {
                Calendar calendar = Calendar.getInstance();
                int years = calendar.get(Calendar.YEAR);
                int yearCurrent = Integer.parseInt(edtYear.getText().toString());
                if (yearCurrent > years || yearCurrent < Constants.LIMIT_YEAR) {
                    AppUtil.showToast(this, R.string.msg_year_is_invalid);
                    edtYear.requestFocus();
                    return false;
                }
            }

            if (StringUtil.isEmpty(carColor)) {
                AppUtil.showToast(this, R.string.msg_car_color);
                edtCarColor.requestFocus();
                return false;
            }
            if (StringUtil.isEmpty(license_plate_number)) {
                AppUtil.showToast(this, R.string.msg_license_plate_number);
                edtLicensePlateNumber.requestFocus();
                return false;
            }
            if (StringUtil.isEmpty(amount_kilometres_per_year)) {
                AppUtil.showToast(this, R.string.msg_amount_kilometres_is_required);
                edtAmountKilometresPerYear.requestFocus();
                return false;
            }
            if (StringUtil.isEmpty(amount_kilometres_per_year)) {
                AppUtil.showToast(this, R.string.msg_amount_kilometres_is_required);
                edtAmountKilometresPerYear.requestFocus();
                return false;
            }
            if (StringUtil.isEmpty(roadTaxesPerYear)) {
                AppUtil.showToast(this, R.string.msg_road_taxes_yearly_car_required);
                edtRoadTaxePerYear.requestFocus();
                return false;
            }
            if (StringUtil.isEmpty(averageConsumptionOfCar)) {
                AppUtil.showToast(this, R.string.msg_average_consumption_of_your_car_required);
                edtAverageConsumptionOfCar.requestFocus();
                return false;
            }
            if (StringUtil.isEmpty(pricePerLiter)) {
                AppUtil.showToast(this, R.string.msg_price_per_liter_is_required);
                edtPricePerLiter.requestFocus();
                return false;
            }
            if (StringUtil.isEmpty(insuranceCost)) {
                AppUtil.showToast(this, R.string.msg_insurance_cost_is_required);
                edtInsuranceCosts.requestFocus();
                return false;
            }
            if (StringUtil.isEmpty(maintenanceCost)) {
                AppUtil.showToast(this, R.string.msg_maintence_cost_is_required);
                edtMaintenanceCosts.requestFocus();
                return false;
            }
            if (StringUtil.isEmpty(valueYourCarBought)) {
                AppUtil.showToast(this, R.string.msg_value_your_car_bought_is_required);
                edtValueYourCarBought.requestFocus();
                return false;
            }
            if (StringUtil.isEmpty(unexpectedCost)) {
                AppUtil.showToast(this, R.string.msg_unexpected_cost_is_required);
                edtUnexpectedCost.requestFocus();
                return false;
            }
            if (flagCost == YOUR_COST) {
                if (StringUtil.isEmpty(costsYouChargePerKilometer)) {
                    AppUtil.showToast(this, R.string.msg_cost_you_charge_per_kilometer_is_required);
                    edtCostsYouChargePerKilometer.requestFocus();
                    return false;
                }
            }
        }
        return true;
    }

    private void setUpSpinner() {
        String[] arrFuel = getResources().getStringArray(R.array.fuel);
        final List<String> listFuels = Arrays.asList(arrFuel);
        mFuelAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listFuels);
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
        final ArrayList<TransportObj> transportObjs = TransportObj.getListTranSports(this);
        mTypeOfTransportAdapter = new SpinnerTypeOfTransportAdapter(this, android.R.layout.simple_spinner_item, transportObjs);
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

    private void setValuesDefaut() {
        edtNumYearUseCar.setText("3");
        edtCostOfGarage.setText("0");
        edtUnexpectedCost.setText("0");
        edtPriceNewTyres.setText("1000");
//        double valueSell = (Double.parseDouble(edtValueYourCarBought.getText().toString()) * 10) / 100;
//        edtValueCarSell.setText(String.valueOf(valueSell));
    }

    private void getCountryCode() {
        String[] rl = getResources().getStringArray(R.array.CountryCodes);
      //  int curPosition = AppUtil.getCurentPositionCountryCode(this);
        int curPosition = 0;
        String phoneCode = rl[curPosition].split(",")[0];
        tvPhoneCode.setText(phoneCode);
    }

    private void becomePro() {
        String bussinessName = edtBussinessName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phoneNumber = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        String phoneCode = tvPhoneCode.getText().toString().trim();
        String phone = phoneCode + " " + phoneNumber;

        if (ckbDriver.isChecked()) {
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
            String carColor = edtCarColor.getText().toString().trim().replaceAll(",", "");
            String license_plate_number = edtLicensePlateNumber.getText().toString().trim().replaceAll(",", "");
            String yourCost = edtCostsYouChargePerKilometer.getText().toString().replaceAll(",", "");
            String caculate = edtCostsPerKilometer.getText().toString();

            if (flagCost == CACULATE) {
                cost = String.valueOf(auto_caculate);
            } else if (flagCost == YOUR_COST) {
                cost = yourCost;
            }


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
            DataPart car = null;
            DataPart certification = null;
            if (imgCar.getDrawable() != null) {
                car = new DataPart("car.jpg", AppUtil.getFileDataFromDrawable(this, imgCar.getDrawable()), DataPart.TYPE_IMAGE);
            }
            if (imgCertification.getDrawable() != null) {
                certification = new DataPart("certification.jpg", AppUtil.getFileDataFromDrawable(this, imgCertification.getDrawable()), DataPart.TYPE_IMAGE);
            }
            ModelManager.updateProfileProDriver(this, bussinessName, null, email, phone, address, ckbDelivery.isChecked(), carMake, model, year, carColor, license_plate_number, itemTypeOfTransport, itemFuel, amount_kilometres_per_year, numYearUseCar, roadTaxesPerYear, costOfGarage, averageConsumptionOfCar, pricePerLiter, insuranceCost, maintenanceCost, priceNewTyres, unexpectedCost, valueCarSell, valueYourCarBought, cost, fareType, car, certification, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    try {
                        JSONObject jsonObject = new JSONObject(object.toString());
                        ApiResponse response = new ApiResponse(jsonObject);
                        if (!response.isError()) {
                            UserObj userObj = response.getDataObject(UserObj.class);
                            userObj.setToken(DataStoreManager.getUser().getToken());
                            userObj.setRememberMe(DataStoreManager.getUser().isRememberMe());
                            DataStoreManager.saveUser(userObj);
                            AppUtil.showToast(getApplicationContext(), R.string.msg_update_success);
                            setResult(RESULT_CODE_BECOME_PRO);
                            finish();
                        } else {
                            Toast.makeText(self, response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError() {
                    Log.e(TAG, "ERROR: update profile pro!");

                }
            });
        } else {
            ModelManager.updateProfilePro(this, bussinessName, null, email, phone, address, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    try {
                        JSONObject jsonObject = new JSONObject(object.toString());
                        ApiResponse response = new ApiResponse(jsonObject);
                        if (!response.isError()) {
                            UserObj userObj = response.getDataObject(UserObj.class);
                            userObj.setRememberMe(DataStoreManager.getUser().isRememberMe());
                            userObj.setToken(DataStoreManager.getUser().getToken());
                            DataStoreManager.saveUser(userObj);
                            AppUtil.showToast(getApplicationContext(), R.string.msg_success);
                            setResult(RESULT_CODE_BECOME_PRO);
                            finish();
                        } else {
                            Toast.makeText(self, response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError() {
                    Log.e(TAG, "ERROR: update profile pro!");
                }
            });
        }
    }

    private void setData() {
        UserObj userObj = DataStoreManager.getUser();
        if (userObj.getProData() == null) {
            edtBussinessName.setText(userObj.getName());
            edtBussinessName.setHint(getString(R.string.bussiness_name));
            edtEmail.setText(userObj.getEmail());
            edtAddress.setText(userObj.getAddress());
            edtPhone.setText(userObj.getPhoneNumber());
            if (userObj.getPhoneCode().isEmpty()) {
                getCountryCode();
            } else {
                tvPhoneCode.setText(userObj.getPhoneCode());
            }
        } else {
            ProObj proObj = userObj.getProData();
            edtBussinessName.setText(proObj.getname());
            edtBussinessName.setHint(getString(R.string.bussiness_name));
            edtEmail.setText(proObj.getBusiness_email());
            edtAddress.setText(proObj.getBusiness_address());
            edtPhone.setText(proObj.getPhoneNumber());
            if (proObj.getPhoneCode().isEmpty()) {
                getCountryCode();
            } else {
                tvPhoneCode.setText(proObj.getPhoneCode());
            }
            ckbDriver.setChecked(true);
            llParentDriverInfo.setVisibility(View.VISIBLE);
        }
        setValuesDefaut();
        setTextChangeListener();
        flagCost = CACULATE;
        fareType = Constants.AUTO;
        edtYourEstimateMargin.setText(StringUtil.convertNumberToString(getApplicationContext(), 0, 1));
        edtCostsYouChargePerKilometer.setText("0.0");
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
            valueCarSells = (StringUtil.convertStringToNumberDouble(valueYourCarBought) * 10) / 100;
        } else {
            valueCarSells = StringUtil.convertStringToNumberDouble(valueCarSell);
        }
        if (!amoutKilometresPerYear.isEmpty() && !averageConsumptionOfCar.isEmpty() && !pricePerLiter.isEmpty() && !priceNewTyres.isEmpty() && !valueYourCarBought.isEmpty() && !valueCarSell.isEmpty() && !numYearUseCar.isEmpty() && !roadTaxePerYear.isEmpty() && !costOfGarage.isEmpty() && !insuranceCosts.isEmpty() && !maintenanceCosts.isEmpty() && !unexpectedCost.isEmpty()) {
            double yearly_fuel_consumption = StringUtil.convertStringToNumberDouble(amoutKilometresPerYear) / StringUtil.convertStringToNumberDouble(averageConsumptionOfCar);
            double yearly_fuel_costs = yearly_fuel_consumption * Double.parseDouble(pricePerLiter);
            double yearly_tyre_costs = (StringUtil.convertStringToNumberDouble(priceNewTyres) / 35000) * StringUtil.convertStringToNumberDouble(amoutKilometresPerYear);
            double yearly_depreciation_costs = (StringUtil.convertStringToNumberDouble(valueYourCarBought) - valueCarSells) / StringUtil.convertStringToNumberDouble(numYearUseCar);

            double costPerKilometers = (yearly_fuel_costs + yearly_tyre_costs + yearly_depreciation_costs + StringUtil.convertStringToNumberDouble(roadTaxePerYear) + StringUtil.convertStringToNumberDouble(costOfGarage) + StringUtil.convertStringToNumberDouble(insuranceCosts) + StringUtil.convertStringToNumberDouble(maintenanceCosts) + StringUtil.convertStringToNumberDouble(unexpectedCost)) / StringUtil.convertStringToNumberDouble(amoutKilometresPerYear);
            auto_caculate = costPerKilometers;
            return costPerKilometers;
        } else {
            auto_caculate = 0;
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
                    if (Double.parseDouble(charSequence.toString()) <= 9999.9) {
                        edtCostsPerKilometer.setText(StringUtil.convertNumberToString(calculateCostPerKilometers(),1));
                        Log.e("EEE", "EEE");
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
                        AppUtil.pickImage(BecomeAProActivity.this, AppUtil.PICK_IMAGE_REQUEST_CODE);
                    }

                    @Override
                    public void onNegative() {
                        if (GlobalFunctions.isMarshmallow()) {
                            if (self.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                AppUtil.captureImage(BecomeAProActivity.this, REQUEST_CODE_CAPTURE_IMAGE);
                            } else {
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, RC_CAMERA_PERMISSION);
                            }
                        } else {
                            AppUtil.captureImage(BecomeAProActivity.this, REQUEST_CODE_CAPTURE_IMAGE);
                        }
                    }
                });
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

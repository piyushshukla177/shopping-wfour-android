package com.wfour.onlinestoreapp.view.fragments;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.view.adapters.SpinnerTypeOfTransportAdapter;
import com.wfour.onlinestoreapp.objects.DriverObj;
import com.wfour.onlinestoreapp.objects.ProObj;
import com.wfour.onlinestoreapp.objects.TransportObj;
import com.wfour.onlinestoreapp.objects.UserObj;
import com.wfour.onlinestoreapp.objects.VehicleObject;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.ImageUtil;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import java.util.ArrayList;

/**
 * Created by Suusoft on 09/12/2016.
 */

public class ProDriverFragment extends com.wfour.onlinestoreapp.base.BaseFragment implements View.OnClickListener {
    private EditText edtBusinessName, edtEmail, edtPhone, edtAddress, edtModel, edtCarMake, edtCarColor, edtLicensePlateNumber;

    private TextView tvPhoneCode;
    private TextViewRegular btnEdit;
    private TextViewRegular lblBusinessName, lblEmail, lblAddress, lblPhone, lblCarMake, lblCarColor, lblModel, lblLicensePlateNumber;

    private ImageView imgCar, imgCertification;
    private Spinner spnTypeOfTransport;

    private UserObj userObj;

    public static ProDriverFragment newInstance(Bundle args) {
        ProDriverFragment fragment = new ProDriverFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_driver_pro;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        edtBusinessName = (EditText) view.findViewById(R.id.edt_bussiness_name);
        edtEmail = (EditText) view.findViewById(R.id.edt_email);
        edtPhone = (EditText) view.findViewById(R.id.edt_phone_number);
        edtAddress = (EditText) view.findViewById(R.id.edt_address);
        tvPhoneCode = (TextView) view.findViewById(R.id.tv_phone_code);
        btnEdit = (TextViewRegular) view.findViewById(R.id.btn_edit_infomation);
        lblBusinessName = (TextViewRegular) view.findViewById(R.id.lbl_bussiness_name);
        lblBusinessName.setText(getString(R.string.bussiness_name));
        lblEmail = (TextViewRegular) view.findViewById(R.id.lbl_email);
        lblEmail.setText(getString(R.string.email_address));
        lblAddress = (TextViewRegular) view.findViewById(R.id.lbl_address);
        lblAddress.setText(getString(R.string.business_address));
        lblPhone = (TextViewRegular) view.findViewById(R.id.lbl_phone_number);
        lblPhone.setText(getString(R.string.phone_number));
        lblCarMake = (TextViewRegular) view.findViewById(R.id.lbl_car_make);
        lblCarMake.setText(getString(R.string.car_make));
        lblCarColor = (TextViewRegular) view.findViewById(R.id.lbl_car_color);
        lblCarColor.setText(getString(R.string.hint_car_color));
        lblModel = (TextViewRegular) view.findViewById(R.id.lbl_model);
        lblModel.setText(getString(R.string.model));
        lblLicensePlateNumber = (TextViewRegular) view.findViewById(R.id.lbl_license_plate_number);
        lblLicensePlateNumber.setText(getString(R.string.hint_license_plate_number));


        edtCarMake = (EditText) view.findViewById(R.id.edt_car_make);
        edtModel = (EditText) view.findViewById(R.id.edt_model);
        edtCarColor = (EditText) view.findViewById(R.id.edt_car_color);
        edtLicensePlateNumber = (EditText) view.findViewById(R.id.edt_license_plate_number);
        imgCar = (ImageView) view.findViewById(R.id.imgCar);
        imgCertification = (ImageView) view.findViewById(R.id.img_certification);
        spnTypeOfTransport = (Spinner) view.findViewById(R.id.spn_type_of_transport);
    }

    @Override
    protected void getData() {
        imgCar.setOnClickListener(this);
        imgCertification.setOnClickListener(this);
        enableEdit(false);
        btnEdit.setVisibility(View.GONE);
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(UserObj.DATA_USER)) {
                userObj = bundle.getParcelable(UserObj.DATA_USER);
                setData(userObj);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == imgCar) {
            showImageLarge(userObj.getVehicleObject().getImage());
        } else if (view == imgCertification) {
            showImageLarge(userObj.getDriverData().getDriver_license());
        }
    }

    private void enableEdit(boolean isEdit) {
        edtBusinessName.setEnabled(isEdit);
        edtEmail.setEnabled(isEdit);
        edtAddress.setEnabled(isEdit);
        edtPhone.setEnabled(isEdit);
        edtCarColor.setEnabled(isEdit);
        edtLicensePlateNumber.setEnabled(isEdit);
        edtModel.setEnabled(isEdit);
        edtCarMake.setEnabled(isEdit);
        spnTypeOfTransport.setEnabled(isEdit);

    }

    private void setData(UserObj userObj) {
        ProObj proObj = userObj.getProData();
        VehicleObject vehicleObject = userObj.getVehicleObject();
        DriverObj driverObj = userObj.getDriverData();
        if (proObj != null) {
            edtBusinessName.setText(proObj.getname());
            edtEmail.setText(proObj.getBusiness_email());
            edtAddress.setText(proObj.getBusiness_address());
            edtPhone.setText(proObj.getPhoneNumber());
            tvPhoneCode.setText(proObj.getPhoneCode());
            edtCarMake.setText(vehicleObject.getBrand());
            edtModel.setText(vehicleObject.getModel());
            edtCarColor.setText(vehicleObject.getColor());
            edtLicensePlateNumber.setText(vehicleObject.getPlate());
            ImageUtil.setImage(getActivity(), imgCertification, driverObj.getDriver_license(), 600, 400);
            ImageUtil.setImage(getActivity(), imgCar, vehicleObject.getImage(), 600, 400);
            ArrayList<TransportObj> transportObjs = TransportObj.getListTranSports(getActivity());
            SpinnerTypeOfTransportAdapter mTypeOfTransportAdapter = new SpinnerTypeOfTransportAdapter(getActivity(), android.R.layout.simple_spinner_item, transportObjs);
            mTypeOfTransportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnTypeOfTransport.setAdapter(mTypeOfTransportAdapter);
            for (int i = 0; i < transportObjs.size(); i++) {
                if (driverObj.getType().equals(transportObjs.get(i).getType())) {
                    spnTypeOfTransport.setSelection(i);
                }
            }
        }
    }

    private void showImageLarge(String url) {
        if (userObj != null && url != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(self);

            final ImageView imageView = new ImageView(self);
            RelativeLayout layout = new RelativeLayout(self);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            int w = params.width = AppUtil.getScreenWidth(getActivity());
            int h = params.height = w;
            imageView.setLayoutParams(params);
            layout.addView(imageView, params);
            layout.setPadding(20, 20, 20, 20);
            builder.setView(layout);
            AlertDialog dialog = builder.create();
            dialog.setCancelable(true);


            dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
            ImageUtil.setImage(self, imageView, url);

            if (!dialog.isShowing())
                dialog.show();

        }

    }
}

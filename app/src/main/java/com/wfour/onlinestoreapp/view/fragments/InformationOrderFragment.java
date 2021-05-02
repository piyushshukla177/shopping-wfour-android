package com.wfour.onlinestoreapp.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.BaseFragment;
import com.wfour.onlinestoreapp.network.modelmanager.singletonmanager.AddressManager;
import com.wfour.onlinestoreapp.objects.Person;
import com.wfour.onlinestoreapp.view.activities.CartActivity;

import java.util.ArrayList;

public class InformationOrderFragment extends BaseFragment {

    public static InformationOrderFragment getInstance(){
        Bundle bundle  = new Bundle();
        InformationOrderFragment fragment = new InformationOrderFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String NAME = "NAME";
    public static final String PHONE = "PHONE";
    public static final String ADDRESS = "ADDRESS";
    public static final String TOWN = "TOWN";
    public static final String CITY = "CITY";
    public static final String ACTION = "ACTION";

    private Toolbar toolbar;
    private TextView btn_AddAddress, btnUpdate;
    private EditText edtName, edtPhone, edtAddress, edtTown, edtCity;
    private CheckBox chkCompany, chkDefaul;
    private Intent intent;
    private String name;
    private String phone;
    private String address;
    private String city;
    private String town;

    private CartActivity mCartActivity;
    private DeliveryAddressFragment deliveryAddressFragment;
    private Person person;
    private ArrayList<Person> personList;
    private int position;

    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_information_order;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        mCartActivity = (CartActivity)getActivity();
        toolbar = view.findViewById(R.id.toolbar);
        mCartActivity.setSupportActionBar(toolbar);
        mCartActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_AddAddress = view.findViewById(R.id.btn_AddAddress);

        edtName = view.findViewById(R.id.edtName);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtAddress = view.findViewById(R.id.edtAddress);
//        edtCity = view.findViewById(R.id.edtCity);
//        edtTown = view.findViewById(R.id.edtTown);
//        chkCompany = view.findViewById(R.id.chk_company);
//        chkDefaul = view.findViewById(R.id.chk_default);



        if (mCartActivity.getAction() == "ADD" || mCartActivity.getAction() == ""){
            toolbar.setTitle("ADD");
            btn_AddAddress.setText("Add");
            btn_AddAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setData();
                }
            });
        }else  if (mCartActivity.getAction() == "EDIT"){
            toolbar.setTitle("EDIT");
            btn_AddAddress.setText("Update");

            Bundle bundle = getArguments();
            position = Integer.parseInt(bundle.getString("position"));
            Person person =(Person) bundle.getSerializable(DeliveryAddressFragment.PERSON);
            edtName.setText(person.getName());
            edtPhone.setText(person.getPhone());
//            edtCity.setText(person.getCity());
//            edtTown.setText(person.getTown());
            edtAddress.setText(person.getAddress());

            btn_AddAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   upData();
                }
            });

        }



    }

    @Override
    protected void getData() {

    }
    public void setData() {
        name = edtName.getText().toString();
        phone = String.valueOf(edtPhone.getText().toString());
        address = edtAddress.getText().toString();
//        city = edtCity.getText().toString();
//        town = edtTown.getText().toString();

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(self, "Ban phai nhap het cac muc", Toast.LENGTH_LONG).show();
        } else {
            mCartActivity.setAction("ADD");
            AddressManager.getInstance().getArray().clear();
            AddressManager.getInstance().addItem(new Person(name, phone, address ));

            FragmentTransaction fragmentTransaction = mCartActivity.getSupportFragmentManager().beginTransaction();
            if (deliveryAddressFragment == null) {
                deliveryAddressFragment = DeliveryAddressFragment.newInstance();

            }
            //deliveryAddressFragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.frgCart, deliveryAddressFragment).commit();

            dimisKeyBoard();


        }


    }

    private void dimisKeyBoard(){
        View v = getActivity().getCurrentFocus();
        if (v != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    public void upData(){
        personList = AddressManager.getInstance().getArray();
        personList.set(position, new Person(edtName.getText().toString(), edtPhone.getText().toString(),
                edtAddress.getText().toString()));

        FragmentTransaction fragmentTransaction = mCartActivity.getSupportFragmentManager().beginTransaction();
        if (deliveryAddressFragment == null) {
            deliveryAddressFragment = DeliveryAddressFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.frgCart, deliveryAddressFragment).addToBackStack("deliveryAddressFragment").commit();
        dimisKeyBoard();

    }

    @Override
    public void onStop() {
        super.onStop();
        edtName.getText().clear();
        edtPhone.getText().clear();
        edtAddress.getText().clear();
//        edtTown.getText().clear();
//        edtCity.getText().clear();

    }
}

package com.wfour.onlinestoreapp.view.fragments;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.BaseFragment;
import com.wfour.onlinestoreapp.interfaces.MyOnClick;
import com.wfour.onlinestoreapp.objects.Person;
import com.wfour.onlinestoreapp.view.activities.CartActivity;
import com.wfour.onlinestoreapp.view.adapters.AddressAdapter;


public class UpdateAddressFragment extends BaseFragment implements View.OnClickListener {

    public static UpdateAddressFragment newInstance(Bundle bundle){
        //Bundle bundle = new Bundle();
        UpdateAddressFragment fragment = new UpdateAddressFragment();
        fragment.setArguments(bundle);
//        fragment.person = person;
        return fragment;
    }

    private Toolbar toolbar;
    private EditText edtName, edtPhone, edtAddress;
    private Person person;
    private Bundle bundle;

    private CartActivity mCartActivity;
    private MyOnClick onClick;

    @Override
    public void onClick(View view) {

    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_update_address;
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
        mCartActivity.setTitle("Update Address");

        edtName = view.findViewById(R.id.edtName);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtAddress = view.findViewById(R.id.edtAddress);

        getData();

        new MyOnClick() {
            @Override
            public void DeleteItem(int position) {

            }

            @Override
            public void EditItem(Person person, int position) {

            }
        };
    }
    public void getData(){

        bundle = getArguments();
        String name = bundle.getString(AddressAdapter.NAME);
        String phone = bundle.getString(AddressAdapter.PHONE);
        String address = bundle.getString(AddressAdapter.ADDRESS);
        edtName.setText(name);
        edtPhone.setText(phone);
        edtAddress.setText(address);

    }
}

package com.wfour.onlinestoreapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.view.adapters.PhoneCountryListAdapter;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.objects.CountryPhoneObj;
import com.wfour.onlinestoreapp.utils.AppUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suusoft on 12/26/2016.
 *
 */

public class PhoneCountryListActivity extends com.wfour.onlinestoreapp.base.BaseActivity implements PhoneCountryListAdapter.IOnItemClickListener , SearchView.OnQueryTextListener {

    private RecyclerView mRclData;
    private PhoneCountryListAdapter mAdapter;
    private List<CountryPhoneObj> mData;

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.layout_single_list;
    }

    @Override
    protected void getExtraData(Intent intent) {

    }

    @Override
    protected void initilize() {

    }

    @Override
    protected void initView() {
        mRclData = (RecyclerView) findViewById(R.id.rcv_data);
        mRclData.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onViewCreated() {
        initData();
        mAdapter = new PhoneCountryListAdapter(mData, this);
        mRclData.setAdapter(mAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppUtil.hideSoftKeyboard(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_phone_country, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchItem.expandActionView();
        return true;

    }

    private void initData() {
        mData = new ArrayList<>();
        String json = loadJSONFromAsset();
        if (json != null) {
            CountryCodeJson countryCodeJson = new Gson().fromJson(loadJSONFromAsset(), CountryCodeJson.class);
            mData.addAll(countryCodeJson.getCountries());
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchCountry(newText);
        return true;
    }


    private void searchCountry(String newText) {
        mAdapter.getFilter().filter(newText);
    }

    @Override
    public void onItemSelected(String phoneCode) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(Args.KEY_PHONE_CODE, phoneCode);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    private class CountryCodeJson {
        List<CountryPhoneObj> countries;

        public List<CountryPhoneObj> getCountries() {
            return countries;
        }

        public void setCountries(List<CountryPhoneObj> countries) {
            this.countries = countries;
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("country_phone.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

}

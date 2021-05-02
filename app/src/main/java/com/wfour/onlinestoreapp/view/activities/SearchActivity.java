package com.wfour.onlinestoreapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.wfour.onlinestoreapp.AppController;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.view.adapters.SpinnerDealCateAdapter;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.objects.DealCateObj;
import com.wfour.onlinestoreapp.objects.SettingsObj;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.widgets.textview.TextViewBold;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private EditText edtKeyword;
    private AppCompatSpinner mSpnCate;
    private TextViewRegular mLblRadius, mLblDealsQuantity;
    private SeekBar mSkbRadius, mSkbQuantity;
    private CheckBox mChkAvailable;
    private TextViewBold mLblSearch;
    private SpinnerDealCateAdapter mAdapter;
    private String idCate;
    private String isActive;
    private String nameDealCate;

    private LinearLayout llParent;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    void inflateLayout() {
        setContentView(R.layout.activity_search);
    }

    @Override
    void initUI() {
        // Show as up button
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(R.string.search);

//        try {
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }

        edtKeyword = (EditText) findViewById(R.id.txt_keyword);
        mLblSearch = (TextViewBold) findViewById(R.id.lbl_search);
        mSpnCate = (AppCompatSpinner) findViewById(R.id.spn_cate);
        mLblRadius = (TextViewRegular) findViewById(R.id.lbl_radius);
        mLblDealsQuantity = (TextViewRegular) findViewById(R.id.lbl_deals_quantity);
        mSkbRadius = (SeekBar) findViewById(R.id.skb_radius);
        mSkbQuantity = (SeekBar) findViewById(R.id.skb_quantity);
        mChkAvailable = (CheckBox) findViewById(R.id.chk_available);

        llParent = (LinearLayout) findViewById(R.id.ll_parent);
        AppUtil.closeKeyboardWhenTouchOutside(this,llParent);

    }

    @Override
    void initControl() {
        SettingsObj settingsObj = DataStoreManager.getSettingUtility();
        mLblSearch.setOnClickListener(this);

        mLblDealsQuantity.setText(String.format(getString(R.string.value_best_deals), "20"));
        if (settingsObj != null) {
            if (!settingsObj.getSearching_deal_distance().isEmpty()) {
                int radius = Integer.parseInt(settingsObj.getSearching_deal_distance());
                mSkbRadius.setProgress(radius);
                mLblRadius.setText(String.format(getString(R.string.value_km), ""+radius));
            }


        } else {
            mSkbRadius.setProgress(5);
        }
        mSkbQuantity.setProgress(20);
        mSkbRadius.setOnSeekBarChangeListener(this);
        mSkbQuantity.setOnSeekBarChangeListener(this);
        setUpSpinner();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AppUtil.hideSoftKeyboard(this);
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == mLblSearch) {
            searchDeal();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (seekBar == mSkbRadius) {
            mLblRadius.setText(String.format(getString(R.string.value_km), String.valueOf(i)));
        } else if (seekBar == mSkbQuantity) {
            mLblDealsQuantity.setText(String.format(getString(R.string.value_best_deals), String.valueOf(i)));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void setUpSpinner() {
        final ArrayList<DealCateObj> listDealCates = new ArrayList<>();
        listDealCates.addAll(AppController.getInstance().getDealCategories());
        listDealCates.remove(1);
        //listDealCates.add(6, new DealCateObj(DealCateObj.NEWS_AND_EVENTS, this.getString(R.string.news_and_events)));
        mAdapter = new SpinnerDealCateAdapter(listDealCates);
        mSpnCate.setAdapter(mAdapter);
        mSpnCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idCate = listDealCates.get(i).getId();
                nameDealCate = listDealCates.get(i).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void searchDeal() {
        String keyword = edtKeyword.getText().toString().trim();
        String distance = String.valueOf(mSkbRadius.getProgress());
        String num_deal_per_pages = String.valueOf(mSkbQuantity.getProgress());
        if (mChkAvailable.isChecked()) {
            isActive = "1";
        } else {
            isActive = "0";
        }
        Bundle bundle = new Bundle();
        bundle.putString(Args.KEY_KEY_WORD, keyword);
        bundle.putString(Args.KEY_DISTANCE, distance);
        bundle.putString(Args.KEY_NUM_DEAL_PER_PAGE, num_deal_per_pages);
        bundle.putString(Args.KEY_ACTIVE_DEAL, isActive);
        bundle.putString(Args.KEY_ID_DEAL_CATE, idCate);
        bundle.putString(Args.TYPE_OF_DEAL_NAME, nameDealCate);
        bundle.putString(Args.TYPE_OF_SEARCH_DEAL, Constants.SEARCH_NEARBY);

        Intent intent = new Intent(getApplicationContext(), DealsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
//        ModelManager.getDealList(this, keyword, isActive, idCate, "", distance, num_deal_per_pages, AppController.getInstance().getLatMyLocation(), AppController.getInstance().getLongMyLocation(), 1, new ModelManagerListener() {
//            @Override
//            public void onSuccess(Object object) {
//                JSONObject jsonObject = (JSONObject) object;
//                ApiResponse response = new ApiResponse(jsonObject);
//                if (!response.isError()) {
//                    response.getDataList(DealObj.class);
//
//                } else {
//                    Toast.makeText(self, response.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onError() {
//                Toast.makeText(self, R.string.msg_have_some_errors, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

}

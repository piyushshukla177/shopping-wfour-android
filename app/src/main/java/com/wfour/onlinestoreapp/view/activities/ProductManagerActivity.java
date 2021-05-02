package com.wfour.onlinestoreapp.view.activities;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.objects.ProductObj;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.view.fragments.DealListFragment;

import java.util.ArrayList;

/**
 * Created by Suusoft on 11/18/2017.
 */

public class ProductManagerActivity extends BaseActivity implements ViewPager.OnPageChangeListener , DealListFragment.IListenerDealsChange{


    private static String TAG = ProductManagerActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AdapterViewPagger adapter ;
    private String[] tabs;
    private TextView tab1, tab2, tab3;
    private Menu mMenu;

    @Override
    void inflateLayout() {
        setContentView(R.layout.layout_tabradius_pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_seller, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_new){
            AppUtil.startActivity(self, NewDealActivity.class);
        } else if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    void initUI() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Show as up button
        try {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        setTitle(R.string.product_management);

        tabs = getResources().getStringArray(R.array.list_seller);
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new AdapterViewPagger(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(this);
        tabLayout.setupWithViewPager(viewPager);

        //createTabIcon();
    }


    @Override
    void initControl() {

    }


    private void createTabIcon() {
        tab1 = (TextView) LayoutInflater.from(self).inflate(R.layout.custom_tab, null);
        tab2 = (TextView) LayoutInflater.from(self).inflate(R.layout.custom_tab, null);
        tab3 = (TextView) LayoutInflater.from(self).inflate(R.layout.custom_tab, null);

        setSelectedItemTab(0, tab1 , tabs[0], 0);
        setUnSelectedItemTab(1, tab2 , tabs[1], 0);
        setUnSelectedItemTab(2, tab3 , tabs[2], 0);
    }

    private void setUnSelectedItemTab(int tabAt, TextView tab, String title, int drawable){

        tab.setText(title);
        tab.setTextColor(getResources().getColor(R.color.colorAccent));
        tab.setCompoundDrawablesWithIntrinsicBounds(0, 0,drawable, 0);
        tabLayout.getTabAt(tabAt).setCustomView(tab);
    }

    private void setSelectedItemTab(int tabAt, TextView tab, String title,  int drawable){
        tab.setText(title);
        tab.setTextColor(getResources().getColor(R.color.white));
        tab.setCompoundDrawablesWithIntrinsicBounds(0, 0,drawable, 0);
        tabLayout.getTabAt(tabAt).setCustomView(tab);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        switch (position){
//            case 0:
//                setSelectedItemTab(0, tab1 , tabs[0],0);
//                setUnSelectedItemTab(1, tab2 , tabs[1], 0);
//                setUnSelectedItemTab(2, tab3 , tabs[2], 0);
//                break;
//            case 1:
//                setUnSelectedItemTab(0, tab1 , tabs[0], 0);
//                setSelectedItemTab  (1, tab2 , tabs[1], 0);
//                setUnSelectedItemTab(2, tab3 , tabs[2], 0);
//                break;
//            case 2:
//                setUnSelectedItemTab(0, tab1 , tabs[0], 0);
//                setUnSelectedItemTab(1, tab2 , tabs[1], 0);
//                setSelectedItemTab  (2, tab3 , tabs[2], 0);
//                break;
//
//        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onChanged(ArrayList<ProductObj> mProductObj) {

    }

    private class AdapterViewPagger extends FragmentPagerAdapter {

        String[] tabs = getResources().getStringArray(R.array.list_seller);

        public AdapterViewPagger(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.e(TAG, "getItem "  );
            if (position==0) {
                Bundle bundle0 = new Bundle();
                Log.e("SellerFragment", "getItem +  case 0 "  );
                bundle0.putString(Args.KEY_ID_DEAL_CATE, "");
                bundle0.putString(Args.KEY_ACTIVE_DEAL, "1");
                bundle0.putString(Args.TYPE_OF_SEARCH_DEAL, Constants.SEARCH_MINE);
                return DealListFragment.newInstance(bundle0);

            } else if (position==1) {
                Bundle bundle1 = new Bundle();
                Log.e(TAG, "getItem +  case 1 "  );
                bundle1.putString(Args.KEY_ID_DEAL_CATE, "");
                bundle1.putString(Args.KEY_ACTIVE_DEAL, "0");
                bundle1.putString(Args.TYPE_OF_SEARCH_DEAL, Constants.SEARCH_MINE);
                return DealListFragment.newInstance(bundle1);

            } else if (position==2) {
                Bundle bundle2 = new Bundle();
                Log.e(TAG, "getItem +  case 2 "  );
                bundle2.putString(Args.KEY_END_TIME, "1");
                bundle2.putString(Args.KEY_ID_DEAL_CATE, "");
                bundle2.putString(Args.KEY_ACTIVE_DEAL, "0");
                bundle2.putString(Args.TYPE_OF_SEARCH_DEAL, Constants.SEARCH_MINE);
                return DealListFragment.newInstance(bundle2);
            } else {
                Bundle bundle0 = new Bundle();
                Log.e(TAG, "getItem +  case 0 "  );
                bundle0.putString(Args.KEY_ID_DEAL_CATE, "");
                bundle0.putString(Args.KEY_ACTIVE_DEAL, "1");
                bundle0.putString(Args.TYPE_OF_SEARCH_DEAL, Constants.SEARCH_MINE);
                return DealListFragment.newInstance(bundle0);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

    }
}

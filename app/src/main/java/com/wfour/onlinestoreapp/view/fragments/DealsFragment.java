package com.wfour.onlinestoreapp.view.fragments;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.widgets.tabLayout.TabLayoutRadiusBuyer;

/**
 * Created by Suusoft on 11/28/2016.
 */

public class DealsFragment extends com.wfour.onlinestoreapp.base.BaseFragment implements ViewPager.OnPageChangeListener, TabLayoutRadiusBuyer.IListenerOnTabPagerClick {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AdapterViewPagger adapter;
    private String[] tabs;
    private TextView tab1, tab2, tab3, tab4, tab5;

    public static DealsFragment newInstance() {

        Bundle args = new Bundle();

        DealsFragment fragment = new DealsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.layout_viewpager_tablayout;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        tabs = getResources().getStringArray(R.array.list_buyer);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        setAdapter();
        //createTabIcon();

    }

    private void createTabIcon() {
        tab1 = (TextView) LayoutInflater.from(self).inflate(R.layout.custom_tab, null);
        tab2 = (TextView) LayoutInflater.from(self).inflate(R.layout.custom_tab, null);
        tab3 = (TextView) LayoutInflater.from(self).inflate(R.layout.custom_tab, null);
        tab4 = (TextView) LayoutInflater.from(self).inflate(R.layout.custom_tab, null);
        tab5 = (TextView) LayoutInflater.from(self).inflate(R.layout.custom_tab, null);

        setSelectedItemTab(0, tab1 , tabs[0], 0);
        setUnSelectedItemTab(1, tab2 , tabs[1], 0);
        setUnSelectedItemTab(2, tab3 , tabs[2], 0);
        setUnSelectedItemTab(3, tab4 , tabs[3], 0);
    }

    @Override
    protected void getData() {

    }

    private void setAdapter() {
        viewPager.setAdapter(new AdapterViewPagger(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOnPageChangeListener(this);

    }

    private void setUnSelectedItemTab(int tabAt, TextView tab, String title,  int drawable){
        tab.setText(title);
        tab.setTextColor(getResources().getColor(R.color.colorTabTextUnselect));
        tab.setCompoundDrawablesWithIntrinsicBounds(0, 0,drawable, 0);
        tabLayout.getTabAt(tabAt).setCustomView(tab);
        tabLayout.getTabAt(tabAt).setCustomView(tab);
    }

    private void setSelectedItemTab(int tabAt, TextView tab, String title,  int drawable){
        tab.setText(title);
        tab.setTextColor(getResources().getColor(R.color.colorTabTextSelected));
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
//                setSelectedItemTab(0, tab1 , tabs[0], 0);
//                setUnSelectedItemTab(1, tab2 , tabs[1], 0);
//                setUnSelectedItemTab(2, tab3 , tabs[2], 0);
//                setUnSelectedItemTab(3, tab4 , tabs[3], 0);
//                break;
//            case 1:
//                setUnSelectedItemTab(0, tab1 , tabs[0], 0);
//                setSelectedItemTab  (1, tab2 , tabs[1], 0);
//                setUnSelectedItemTab(2, tab3 , tabs[2], 0);
//                setUnSelectedItemTab(3, tab4 , tabs[3], 0);
//                break;
//            case 2:
//                setUnSelectedItemTab(0, tab1 , tabs[0], 0);
//                setUnSelectedItemTab(1, tab2 , tabs[1], 0);
//                setSelectedItemTab  (2, tab3 , tabs[2], 0);
//                setUnSelectedItemTab(3, tab4 , tabs[3], 0);
//                break;
//            case 3:
//                setUnSelectedItemTab(0, tab1 , tabs[0], 0);
//                setUnSelectedItemTab(1, tab2 , tabs[1], 0);
//                setUnSelectedItemTab(2, tab3 , tabs[2], 0);
//                setSelectedItemTab  (3, tab4 , tabs[3], 0);
//                break;
//        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabClick(int p) {
        viewPager.setCurrentItem(p);
    }

    private class AdapterViewPagger extends FragmentPagerAdapter {

        String[] tabs = getResources().getStringArray(R.array.list_buyer);

        public AdapterViewPagger(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position==0) {
                Bundle bundle = new Bundle();
                bundle.putString(Args.KEY_ID_DEAL_CATE, "");
                bundle.putString(Args.TYPE_OF_SEARCH_DEAL, Constants.SEARCH_FAVORIES);
                return DealListFragment.newInstance(bundle);

            } else if (position==1) {
               Bundle bundle2 = new Bundle();
               bundle2.putString(Args.KEY_ID_DEAL_CATE, "");
               bundle2.putString(Args.TYPE_OF_DEAL_NAME, Constants.REVIEWED);
               bundle2.putString(Args.TYPE_OF_SEARCH_DEAL, Constants.REVIEWED);
               return DealListFragment.newInstance(bundle2);

            } else if (position==2) {
                Bundle bundle1 = new Bundle();
                bundle1.putString(Args.TYPE_OF_SEARCH_DEAL, Constants.BOUGH);
                return ReservationListFragment.newInstance(bundle1);

            }else if (position == 3) {
                Bundle bundle3 = new Bundle();
                bundle3.putString(Args.TYPE_OF_SEARCH_DEAL, Constants.NO_DEALS);
                return ReservationListFragment.newInstance(bundle3);

            } else{
                Bundle bundle = new Bundle();
                bundle.putString(Args.KEY_ID_DEAL_CATE, "");
                bundle.putString(Args.TYPE_OF_SEARCH_DEAL, Constants.SEARCH_FAVORIES);
                return DealListFragment.newInstance(bundle);
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

package com.wfour.onlinestoreapp.view.fragments;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.utils.AppUtil;

/**
 * Created by Suusoft on 12/8/2016.
 */

public class MyDealFragment extends com.wfour.onlinestoreapp.base.BaseFragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static MyDealFragment newInstance() {

        Bundle args = new Bundle();

        MyDealFragment fragment = new MyDealFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.layout_tab_pagger;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                AppUtil.hideSoftKeyboard(getActivity());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void getData() {
        setAdapter();
    }

    private void setAdapter() {
        viewPager.setAdapter(new AdapterViewPagger(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    private class AdapterViewPagger extends FragmentPagerAdapter {

        String[] tabs = getResources().getStringArray(R.array.list_seller);

        public AdapterViewPagger(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                Bundle bundle = new Bundle();
                bundle.putString(Args.KEY_ID_DEAL_CATE, "");
                bundle.putString(Args.KEY_ACTIVE_DEAL, "1");
                bundle.putString(Args.TYPE_OF_SEARCH_DEAL, Constants.SEARCH_MINE);

                return DealListFragment.newInstance(bundle);
            } else if (position == 1) {
                Bundle bundle = new Bundle();
                bundle.putString(Args.KEY_ID_DEAL_CATE, "");
                bundle.putString(Args.KEY_ACTIVE_DEAL, "0");
                bundle.putString(Args.TYPE_OF_SEARCH_DEAL, Constants.SEARCH_MINE);

                return DealListFragment.newInstance(bundle);

                /*mới thêm vào khi reskin*/
            } else if (position == 2) {
                Bundle bundle = new Bundle();
                bundle.putString(Args.KEY_END_TIME, "1");
                bundle.putString(Args.KEY_ID_DEAL_CATE, "");
                bundle.putString(Args.KEY_ACTIVE_DEAL, "0");
                bundle.putString(Args.TYPE_OF_SEARCH_DEAL, Constants.SEARCH_MINE);

                return DealListFragment.newInstance(bundle);
//            }
//            else if (position == 2) {
//                return DealsFragment.newInstance();
//            } else if (position == 3) {
//                return DealNewFragment.newInstance();
//            } else if (position == 4) {
//                return AccountingFragment.newInstance(AccountingFragment.TYPE_DEAL);
//            } else if (position == 5) {
//                Bundle bundle = new Bundle();
//                bundle.putString(Args.KEY_ID_DEAL_CATE, "");
//                bundle.putString(Args.TYPE_OF_SEARCH_DEAL, Constants.SEARCH_FAVORIES);
//                return DealListFragment.newInstance(bundle);
            } else return DealsFragment.newInstance();

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

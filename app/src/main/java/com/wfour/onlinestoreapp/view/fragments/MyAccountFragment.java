package com.wfour.onlinestoreapp.view.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.interfaces.IObserver;
import com.wfour.onlinestoreapp.utils.AppUtil;

import java.util.ArrayList;

/**
 * Created by Suusoft on 01/12/2016.
 */

public class MyAccountFragment extends BaseFragment implements View.OnClickListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public static MyAccountFragment newInstance() {
        Bundle args = new Bundle();
        MyAccountFragment fragment = new MyAccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    View inflateLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_my_account, container, false);
    }

    @Override
    void initUI(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
    }

    @Override
    void initControl() {
        setUpViewPager(viewPager);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppUtil.hideSoftKeyboard(getActivity());
    }

    private void setUpViewPager(final ViewPager viewPager) {
        final PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());

        adapter.addFragment(MyAccountMyInfoFragment.newInstance(), getString(R.string.me));
//        adapter.addFragment(MyAccountProFragment.newInstance(), getString(R.string.pro));
        adapter.addFragment(MyDealFragment.newInstance(), getString(R.string.deals));
        adapter.addFragment(IwanaPayFragment.newInstance(), getString(R.string.pay));

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                AppUtil.hideSoftKeyboard(getActivity());
            }

            @Override
            public void onPageSelected(int position) {

                /*không sử dụng đến vì đã loại bỏ MyAccountProFragment */
//                if (position == 1) {
//                    if (DataStoreManager.getUser().getProData() != null && ((MyAccountProFragment) adapter.getItem(position)).isCreateView()) {
//                        ((IObserver) adapter.getItem(position)).update();
//                    }
//                } else
                    if (position == 0) {
                    ((IObserver) adapter.getItem(position)).update();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class PagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> listFragments;
        private ArrayList<String> listTabs;


        public PagerAdapter(FragmentManager fm) {
            super(fm);

            listFragments = new ArrayList<>();
            listTabs = new ArrayList<>();

        }

        private void addFragment(Fragment fragment, String tab) {
            listFragments.add(fragment);
            listTabs.add(tab);
        }

        private void addFragment(Fragment fragment, int pos) {
            listFragments.add(pos, fragment);
            notifyDataSetChanged();
        }

        private void removeFragment(int pos) {
            listFragments.remove(pos);
            notifyDataSetChanged();
        }


        @Override
        public Fragment getItem(int position) {
            return listFragments.get(position);
        }

        @Override
        public int getCount() {
            return listFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return listTabs.get(position);
        }
    }

}

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

/**
 * Created by Suusoft on 30/11/2016.
 */

public class IwanabizFragment extends BaseFragment {

    public static IwanabizFragment newInstance() {
        return new IwanabizFragment();
    }

    @Override
    View inflateLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_iwanabiz, container, false);
    }

    @Override
    void initUI(View view) {
        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);

        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);

        tabs.setupWithViewPager(pager);
    }

    @Override
    void initControl() {
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private String[] TITLES;

        public PagerAdapter(FragmentManager fm) {
            super(fm);

            TITLES = new String[]{getString(R.string.my_trip), getString(R.string.accounting)};
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            try {
                return TITLES.length;
            } catch (NullPointerException e) {
                return 0;
            }
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return TripHistoryFragment.newInstance();
            } else if (position == 1) {
                return AccountingFragment.newInstance(AccountingFragment.TYPE_TRIP);
            }

            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }
}

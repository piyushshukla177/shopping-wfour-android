package com.wfour.onlinestoreapp.view.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.wfour.onlinestoreapp.R;

public class ChatDealPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    public ChatDealPagerAdapter(FragmentManager fm , Context context) {
        super(fm);
        this.mContext = context;
    }


    @Override
    public Fragment getItem(int position) {
            switch (position){
                case 0:     return null;
                case 1:     return null;
                case 2:     return null;
            }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:     return mContext.getString(R.string.chat);
            case 1:     return mContext.getString(R.string.deal_);
            case 2:     return mContext.getString(R.string.call_phone);
        }
        return super.getPageTitle(position);
    }
}

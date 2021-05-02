package com.wfour.onlinestoreapp.view.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.wfour.onlinestoreapp.objects.ProductObj;
import com.wfour.onlinestoreapp.view.fragments.FragmentPagerIndicatorCate;

import java.util.ArrayList;

/**
 * Created by SUN on 3/7/2018.
 */

public class AdapterViewPaggerCategory extends FragmentPagerAdapter {

    private ArrayList<ProductObj> productObjs;

    public AdapterViewPaggerCategory(FragmentManager fm, ArrayList<ProductObj> productObjs ) {
        super(fm);
        this.productObjs = productObjs;
    }

    public void addList(ArrayList<ProductObj> productObjs){
        this.productObjs = productObjs;
        this.notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentPagerIndicatorCate.newInstance(productObjs.get(position));
    }

    @Override
    public int getCount() {
        return (productObjs == null)? 0 : 4;
    }
}

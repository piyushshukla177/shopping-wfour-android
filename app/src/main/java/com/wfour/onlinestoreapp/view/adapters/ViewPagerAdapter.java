package com.wfour.onlinestoreapp.view.adapters;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;



import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> listFragments;
    private ArrayList<String> listTabs;


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);

        listFragments = new ArrayList<>();
        listTabs = new ArrayList<>();

    }

    public void addFragment(Fragment fragment) {
        listFragments.add(fragment);
    }

    public void addFragment(Fragment fragment, String tab) {
        listFragments.add(fragment);
        listTabs.add(tab);
    }

    public void addFragment(Fragment fragment, int pos) {
        listFragments.add(pos, fragment);
        notifyDataSetChanged();
    }

    public void removeFragment(int pos) {
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
        if (position< listTabs.size())
            return listTabs.get(position);
        return "";
    }

    public void refresh(){
        for (int i = 0; i  < listFragments.size(); i ++){
            listFragments.get(i).onResume();
        }
    }
}
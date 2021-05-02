package com.wfour.onlinestoreapp.widgets.tabLayout;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;

/**
 * Created by Suusoft on 10/30/2017.
 */

public class TabLayoutRadiusHead extends LinearLayout implements /*ViewPager.OnPageChangeListener, */View.OnClickListener {

    public static int TAB_ACTIVE = 2;
    public static int TAB_HOT = 3;
    public static int TAB_BEAUTY = 4;

    private Context mContext;
    private ViewPager mViewPager;
    private LinearLayout llTabLayout, llTabChild;
    private RelativeLayout childTab1, childTab2, childTab3;
    private ImageView imgBackgroundTab1, imgBackgroundTab2, imgBackgroundTab3, imgIcTab1, imgIcTab2, imgIcTab3;
    private TextView tvTab1, tvTab2, tvTab3;
    private TabSelected mTabSelected = TabSelected.Tab1;
    private TypeTab mTypeTab = TypeTab.TabActive;


    public TabLayoutRadiusHead(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.tablayout_radius_head, this);

            childTab1 = (RelativeLayout) findViewById(R.id.child_tab1);
            childTab2 = (RelativeLayout) findViewById(R.id.child_tab2);
            childTab3 = (RelativeLayout) findViewById(R.id.child_tab3);

            tvTab1  = (TextView) findViewById(R.id.tv_tab1);
            tvTab2  = (TextView) findViewById(R.id.tv_tab2);
            tvTab3  = (TextView) findViewById(R.id.tv_tab3);

            llTabLayout  = (LinearLayout) findViewById(R.id.ll_tab_layout);
            llTabChild  = (LinearLayout) findViewById(R.id.tab_child);

            imgBackgroundTab1  = (ImageView) findViewById(R.id.img_tab1);
            imgBackgroundTab2  = (ImageView) findViewById(R.id.img_tab2);
            imgBackgroundTab3  = (ImageView) findViewById(R.id.img_tab3);

            imgIcTab1  = (ImageView) findViewById(R.id.ic_tab1);
            imgIcTab2  = (ImageView) findViewById(R.id.ic_tab2);
            imgIcTab3  = (ImageView) findViewById(R.id.ic_tab3);

            childTab1.setOnClickListener(this);
            childTab2.setOnClickListener(this);
            childTab3.setOnClickListener(this);
            setTab(TabSelected.Tab1);
        }
    }

    public void setViewPager (ViewPager viewPager){
        mViewPager = viewPager;
//        mViewPager.setOnPageChangeListener(this);
    }

    private IListenerOnTabPagerClick iListenerOnTabPagerClick;

    public void setListenerTabClick(IListenerOnTabPagerClick iListenerOnTabPagerClick){
        this.iListenerOnTabPagerClick = iListenerOnTabPagerClick;
    }

    public interface IListenerOnTabPagerClick{
        void onTabClick(int p);
    }


    @Override
    public void onClick(View v) {
        if (v==childTab1){
            iListenerOnTabPagerClick.onTabClick(0);
        }else if (v==childTab2){
            iListenerOnTabPagerClick.onTabClick(1);
        }else if (v==childTab3){
            iListenerOnTabPagerClick.onTabClick(2);
        }
    }

    public enum TypeTab{
        TabActive,
        TabHot,
        TabView
    }

    public enum TabSelected {
        Tab1,
        Tab2,
        Tab3
    }

    private TabSelected getTabSelected(){
        return mTabSelected;
    }

    public void setTypeTab (TypeTab typeTab){
        mTypeTab = typeTab;
    }





    public void setTab(TabSelected tab){
        if (mTypeTab==TypeTab.TabHot) {
            tvTab1.setText(getResources().getString(R.string.hot));
            tvTab2.setText(getResources().getString(R.string.new_));
            tvTab3.setText(getResources().getString(R.string.all));
            imgIcTab1.setVisibility(View.VISIBLE);
        } else if (mTypeTab==TypeTab.TabActive){
            tvTab1.setText(getResources().getString(R.string.activate));
            tvTab2.setText(getResources().getString(R.string.inactivate));
            tvTab3.setText(getResources().getString(R.string.end_time_));
            imgIcTab1.setVisibility(GONE);
        } else if (mTypeTab==TypeTab.TabView){
            imgIcTab1.setVisibility(GONE);
            tvTab1.setText(getResources().getString(R.string.bougth));
            tvTab2.setText(getResources().getString(R.string.review));
            tvTab3.setText(getResources().getString(R.string.favorite));
        }
        switchTab(tab);

    }

    private void switchTab(TabSelected tab) {
        switch (tab){
            case Tab1:
                mTabSelected = TabSelected.Tab1;
                imgIcTab1.setImageDrawable(getResources().getDrawable(R.drawable.ic_fire_white));
                imgBackgroundTab1.setImageDrawable(getResources().getDrawable(R.drawable.bg_tab_layout_radius_left_red));
                imgBackgroundTab2.setImageDrawable(getResources().getDrawable(R.drawable.bg_tab_layout_rectangle_white));
                imgBackgroundTab3.setImageDrawable(getResources().getDrawable(R.drawable.bg_tab_layout_radius_right_white));
                tvTab1.setTextColor(getResources().getColor(R.color.white));
                tvTab2.setTextColor(getResources().getColor(R.color.primary));
                tvTab3.setTextColor(getResources().getColor(R.color.primary));
                break;

            case Tab2:
                mTabSelected = TabSelected.Tab2;
                imgIcTab1.setImageDrawable(getResources().getDrawable(R.drawable.ic_fire_normal));
                imgBackgroundTab1.setImageDrawable(getResources().getDrawable(R.drawable.bg_tab_layout_radius_left_white));
                imgBackgroundTab2.setImageDrawable(getResources().getDrawable(R.drawable.bg_tab_layout_rectangle_red));
                imgBackgroundTab3.setImageDrawable(getResources().getDrawable(R.drawable.bg_tab_layout_radius_right_white));
                tvTab1.setTextColor(getResources().getColor(R.color.primary));
                tvTab2.setTextColor(getResources().getColor(R.color.white));
                tvTab3.setTextColor(getResources().getColor(R.color.primary));
                break;

            case Tab3:
                mTabSelected = TabSelected.Tab3;
                imgIcTab1.setImageDrawable(getResources().getDrawable(R.drawable.ic_fire_normal));
                imgBackgroundTab1.setImageDrawable(getResources().getDrawable(R.drawable.bg_tab_layout_radius_left_white));
                imgBackgroundTab2.setImageDrawable(getResources().getDrawable(R.drawable.bg_tab_layout_rectangle_white));
                imgBackgroundTab3.setImageDrawable(getResources().getDrawable(R.drawable.bg_tab_layout_radius_right_red));
                tvTab1.setTextColor(getResources().getColor(R.color.primary));
                tvTab2.setTextColor(getResources().getColor(R.color.primary));
                tvTab3.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

}


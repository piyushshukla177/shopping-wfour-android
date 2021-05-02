package com.wfour.onlinestoreapp.widgets.tabLayout;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wfour.onlinestoreapp.R;

/**
 * Created by Suusoft on 10/26/2017.
 */

public class TabIndicatorLine extends LinearLayout implements ViewPager.OnPageChangeListener {

    private Steps mSteps = Steps.STEPS1;

    public static enum Steps{
        STEPS1,
        STEPS2,
        STEPS3
    }

    private ViewPager viewPager;
    private ImageView img1, img2, img3, img12, img23;
    private Context mContext;

    public TabIndicatorLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        intView();
    }

    private void intView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.tablayout_indicator_line, this);
            img1 = (ImageView) findViewById(R.id.img_step1);
            img2 = (ImageView) findViewById(R.id.img_step2);
            img3 = (ImageView) findViewById(R.id.img_step3);
            img12 = (ImageView) findViewById(R.id.img_line12);
            img23 = (ImageView) findViewById(R.id.img_line23);
            setSteps(mSteps);
//            img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_steps));
//            img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_circle_left_right));
//            img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_circle_left));
//            img12.setImageDrawable(getResources().getDrawable(R.drawable.ic_line_steps));
//            img23.setImageDrawable(getResources().getDrawable(R.drawable.ic_line_steps));
        }
    }


    public Steps getSteps(){
        return mSteps;
    }

    public void setSteps(Steps steps){
        switch (steps){
            case STEPS1:
                mSteps = Steps.STEPS1;
                img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_steps));
                img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_circle_left_right));
                img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_circle_left));
                img12.setImageDrawable(getResources().getDrawable(R.drawable.ic_line_steps));
                img23.setImageDrawable(getResources().getDrawable(R.drawable.ic_line_steps));
                break;

            case STEPS2:
                mSteps = Steps.STEPS2;
                img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_steps));
                img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_steps));
                img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_circle_left));
                img12.setImageDrawable(getResources().getDrawable(R.drawable.ic_line_steps_fill));
                img23.setImageDrawable(getResources().getDrawable(R.drawable.ic_line_steps));
                break;

            case STEPS3:
                mSteps = Steps.STEPS3;
                img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_steps));
                img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_steps));
                img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_steps));
                img12.setImageDrawable(getResources().getDrawable(R.drawable.ic_line_steps_fill));
                img23.setImageDrawable(getResources().getDrawable(R.drawable.ic_line_steps_fill));
                break;
        }
    }

    public void setViewPager(ViewPager pager) {
        this.viewPager = pager;
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0: {
                img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_steps));
                img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_circle_left_right));
                img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_circle_left));
                img12.setImageDrawable(getResources().getDrawable(R.drawable.ic_line_steps));
                img23.setImageDrawable(getResources().getDrawable(R.drawable.ic_line_steps));
                break;
            }
            case 1: {
                img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_steps));
                img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_steps));
                img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_circle_left));
                img12.setImageDrawable(getResources().getDrawable(R.drawable.ic_line_steps_fill));
                img23.setImageDrawable(getResources().getDrawable(R.drawable.ic_line_steps));
                break;
            }
            case 2: {
                img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_steps));
                img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_steps));
                img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_steps));
                img12.setImageDrawable(getResources().getDrawable(R.drawable.ic_line_steps_fill));
                img23.setImageDrawable(getResources().getDrawable(R.drawable.ic_line_steps_fill));
                break;
            }

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

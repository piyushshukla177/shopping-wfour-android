package com.wfour.onlinestoreapp.view.activities;

import android.content.Intent;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.BaseActivity;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.view.adapters.ViewPagerAdapter;
import com.wfour.onlinestoreapp.view.fragments.StartScreenFragment;

import me.relex.circleindicator.CircleIndicator;

public class StartScreenActivity extends BaseActivity {

    private static final int RC_PERMISSIONS = 10;
    private ViewPager viewPager;
    private CircleIndicator indicator;
    private ViewPagerAdapter adapter;

    private ImageView imgBg;
    private TextView tv1, tv2, tvLogin ;
    private View progressBar;

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NONE;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_start_screen;
    }

    @Override
    protected void getExtraData(Intent intent) {

    }

    @Override
    protected void initilize() {

    }

    @Override
    protected void initView() {
        progressBar = findViewById(R.id.progress);
        imgBg = findViewById(R.id.img_background);
        tvLogin = findViewById(R.id.tv_login);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);

        viewPager = findViewById(R.id.view_pager);
        indicator = findViewById(R.id.indicator);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(StartScreenFragment.newInstance(R.drawable.banner1));
        adapter.addFragment(StartScreenFragment.newInstance(R.drawable.banner2));
        adapter.addFragment(StartScreenFragment.newInstance(R.drawable.banner3));
        adapter.addFragment(StartScreenFragment.newInstance(R.drawable.banner4));

        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(listener);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());

    }

    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setDataPostion(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void setDataPostion(int position){
        if (position==1){
//            tvLogin.setTextColor(getResources().getColor(R.color.colorPrimary));
//            ImageUtil.setImage(imgBg,R.drawable.bg_gradient_primary );
            tv1.setText(getString(R.string.title_start_screen21));
            tv2.setText(getString(R.string.title_start_screen12));
        }else if (position==0){
//            tvLogin.setTextColor(getResources().getColor(R.color.start_screen_blue));
//            ImageUtil.setImage(imgBg,R.drawable.bg_gradient_blue);
            tv1.setText(getString(R.string.title_start_screen11));
            tv2.setText(getString(R.string.title_start_screen12));
        }else if (position==2){
//            tvLogin.setTextColor(getResources().getColor(R.color.start_screen_red));
//            ImageUtil.setImage(imgBg,R.drawable.bg_gradient_red);
            tv1.setText(getString(R.string.title_start_screen4));
            tv2.setText(getString(R.string.title_start_screen12));
        }else if (position==3){
//            tvLogin.setTextColor(getResources().getColor(R.color.start_screen_purple));
//            ImageUtil.setImage(imgBg,R.drawable.bg_gradient_purple);
            tv1.setText(getString(R.string.title_start_screen5));
            tv2.setText(getString(R.string.title_start_screen12));
        }
    }

    @Override
    protected void onViewCreated() {

    }
    public void onClickLogin(View view) {
        AppUtil.startActivity(self, MainActivity.class);
        finish();
    }
}

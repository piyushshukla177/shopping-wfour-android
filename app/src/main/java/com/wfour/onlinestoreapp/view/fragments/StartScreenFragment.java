package com.wfour.onlinestoreapp.view.fragments;

import android.view.View;
import android.widget.ImageView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.BaseFragment;
import com.wfour.onlinestoreapp.utils.ImageUtil;


public class StartScreenFragment extends BaseFragment {


    private int image;
    private ImageView img;

    public static StartScreenFragment newInstance(int image){
        StartScreenFragment fragment = new StartScreenFragment();
        fragment.image = image;
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_start_screen;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        img = view.findViewById(R.id.img);

        ImageUtil.setImage(img, image);
    }

    @Override
    protected void getData() {

    }
}


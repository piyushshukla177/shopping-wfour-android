package com.wfour.onlinestoreapp.view.adapters;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.BaseFragment;
import com.wfour.onlinestoreapp.objects.ImagePager;
import com.wfour.onlinestoreapp.utils.ImageUtil;


public class FragmentPagerDetailProduct extends BaseFragment {
    private ImagePager imagePager;
    private ImageView img;
    private String string;
   public static FragmentPagerDetailProduct newInstance(String string){
       Bundle bundle = new Bundle();
       FragmentPagerDetailProduct fragment = new FragmentPagerDetailProduct();
       fragment.setArguments(bundle);
       fragment.string = string;
       return fragment;
   }

    @Override
    protected int getLayoutInflate() {
        return R.layout.item_image_product_pager;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        img = view.findViewById(R.id.imageView);
    }

    @Override
    protected void getData() {
        if(imagePager != null){
            ImageUtil.setImage(self, img, imagePager.getUrl() );
        }
    }
}

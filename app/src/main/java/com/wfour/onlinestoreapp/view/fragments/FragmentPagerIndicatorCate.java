package com.wfour.onlinestoreapp.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.objects.ProductObj;
import com.wfour.onlinestoreapp.utils.ImageUtil;
import com.wfour.onlinestoreapp.view.activities.DealDetailActivity;

import static com.wfour.onlinestoreapp.view.adapters.DealAdapter.RQ_UPDATE_DEAL;

/**
 * Created by SUN on 3/7/2018.
 */

public class FragmentPagerIndicatorCate extends com.wfour.onlinestoreapp.base.BaseFragment {

    private ImageView imgDeal;
    private TextView tvNameDeal;
    private ProductObj productObj;

    public static FragmentPagerIndicatorCate newInstance(ProductObj productObj) {
        Bundle args = new Bundle();
        FragmentPagerIndicatorCate fragment = new FragmentPagerIndicatorCate();
        fragment.setArguments(args);
        fragment.productObj = productObj;
        return fragment;
    }


    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_pager_indicator_cate;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        //imgDeal = view.findViewById(R.id.image_deal);
        //tvNameDeal = view.findViewById(R.id.tv_name_deal);
        imgDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Args.KEY_PRODUCT_OBJECT, productObj);
                Intent intent = new Intent(self, DealDetailActivity.class);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, RQ_UPDATE_DEAL);
            }
        });
    }

    @Override
    protected void getData() {
        if (productObj!=null){
            ImageUtil.setImage(self, imgDeal, productObj.getImage());
            tvNameDeal.setText(productObj.getTitle());
        }

    }

}

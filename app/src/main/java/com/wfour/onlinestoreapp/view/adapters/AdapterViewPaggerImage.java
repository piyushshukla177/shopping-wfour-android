package com.wfour.onlinestoreapp.view.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.utils.ImageUtil;
import com.wfour.onlinestoreapp.view.activities.ImageViewerActivity;


import java.util.ArrayList;

public class AdapterViewPaggerImage extends PagerAdapter {

    private ArrayList<String> list;
    private LayoutInflater inflater;
    private LinearLayout lnlContainer;
    private ImageView imageView;
    private Context context;
    public AdapterViewPaggerImage(Context context, ArrayList<String> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public int getCount() {
        return (list == null)? 0 : list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.item_image_product_pager, container, false);
        lnlContainer = view.findViewById(R.id.lnl_container);
        imageView = view.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new Intent(ImageViewerActivity.newUrlInstance(context, list.get(position))));
                context.startActivity(intent);


            }
        });

        ImageUtil.setImage(context, imageView, list.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem( ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

package com.wfour.onlinestoreapp.view.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.BaseActivity;

import java.io.File;

public class ImageViewerActivity extends BaseActivity {
    private static final String URL = ImageViewerActivity.class.getPackage().getName() + ".URL";
    private ImageView imgClose;

    public static Intent newUrlInstance(Context context, String url) {
        Intent intent = new Intent(context, ImageViewerActivity.class);
        intent.putExtra(URL, url);
        return intent;
    }

    PhotoView photoView;

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NONE;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_zoom_image;
    }

    @Override
    protected void getExtraData(Intent intent) {

    }

    @Override
    protected void initilize() {

    }

    @Override
    protected void initView() {
        photoView = findViewById(R.id.photo_view);
        imgClose  = findViewById(R.id.img_close);

        String url = getIntent().getStringExtra(URL);
        if (url != null) {
            Glide.with(this).load(url).into(photoView);
        }
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onViewCreated() {

    }
}

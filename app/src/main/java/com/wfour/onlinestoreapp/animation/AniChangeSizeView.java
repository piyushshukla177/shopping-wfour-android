package com.wfour.onlinestoreapp.animation;

import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;

/**
 * Created by Suusoft on 11/20/2017.
 */

public class AniChangeSizeView extends Animation {
    private int initialHeight, actualHeight;
    private ViewGroup viewGroup;
    private ImageView btnDeal, btnNoDeal, btnTrackDeal, btnCompleteDeal;

    public AniChangeSizeView(ViewGroup viewGroup, ImageView btnDeal, ImageView btnNoDeal,  int actualHeight){
        this.viewGroup = viewGroup;
        this.actualHeight = actualHeight;
        this.btnDeal = btnDeal;
        this.btnNoDeal = btnNoDeal;
    }

    public AniChangeSizeView(ViewGroup viewGroup, ImageView btnDeal, ImageView btnNoDeal, ImageView btnTrackDeal, ImageView btnCompleteDeal , int actualHeight){
        this.viewGroup = viewGroup;
        this.actualHeight = actualHeight;
        this.btnDeal = btnDeal;
        this.btnNoDeal = btnNoDeal;
        this.btnTrackDeal = btnTrackDeal;
        this.btnCompleteDeal = btnCompleteDeal;
    }

    public void setChildView(ImageView btnDeal, ImageView btnNoDeal){

    }

    @Override
    public void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight;

        newHeight = (int)(initialHeight * interpolatedTime);

        viewGroup.removeAllViews();
        viewGroup.addView(btnDeal);
        viewGroup.addView(btnNoDeal);
        if (btnTrackDeal!=null)
            viewGroup.addView(btnTrackDeal);
         if (btnCompleteDeal!=null)
            viewGroup.addView(btnCompleteDeal);
        viewGroup.getLayoutParams().width = newHeight;
        viewGroup.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        initialHeight = actualHeight;
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}

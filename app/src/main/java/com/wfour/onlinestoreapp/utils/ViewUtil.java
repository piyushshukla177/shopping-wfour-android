package com.wfour.onlinestoreapp.utils;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Suusoft on 12/1/2016.
 */

public class ViewUtil {

    /**
     * draw a line on center of text
     *
     */
    public static void setTextLineCenter(TextView textView){
        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public static void setRatingbarColor(RatingBar ratingBar){
        ((LayerDrawable)ratingBar.getProgressDrawable()).getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

    }

}

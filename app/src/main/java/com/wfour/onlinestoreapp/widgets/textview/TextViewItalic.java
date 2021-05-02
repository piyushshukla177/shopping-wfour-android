package com.wfour.onlinestoreapp.widgets.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Suusoft on 12/29/2015.
 */
public class TextViewItalic extends TextView {

    public TextViewItalic(Context context) {
        super(context);

        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Italic.ttf");
        this.setTypeface(face);
    }

    public TextViewItalic(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Italic.ttf");
        this.setTypeface(face);
    }

    public TextViewItalic(Context context, AttributeSet attrs,
                          int defStyle) {
        super(context, attrs, defStyle);
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Italic.ttf");
        this.setTypeface(face);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

}

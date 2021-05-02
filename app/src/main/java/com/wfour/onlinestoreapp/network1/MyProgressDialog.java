package com.wfour.onlinestoreapp.network1;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

public class MyProgressDialog extends Dialog {

    private TextViewRegular mLblMsg;

    public MyProgressDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(
                R.drawable.bg_black_transparent);
        setContentView(R.layout.layout_progress_dialog);

        mLblMsg = (TextViewRegular) findViewById(R.id.lbl_progress_msg);
    }

    public void setMessage(String msg) {
        if (msg != null && !msg.equals("")) {
            mLblMsg.setVisibility(View.VISIBLE);
            mLblMsg.setText(msg);
        } else {
            mLblMsg.setVisibility(View.GONE);
        }
    }
}

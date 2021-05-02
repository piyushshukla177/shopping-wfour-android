package com.wfour.onlinestoreapp.view.chat.mainchat;

import android.app.Application;

import com.sendbird.android.SendBird;

public class BaseApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
//        SendBird.init(APP_ID, getApplicationContext());
    }
}

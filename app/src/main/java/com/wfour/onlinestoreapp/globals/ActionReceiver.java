package com.wfour.onlinestoreapp.globals;

import android.content.Context;
import android.content.Intent;

/**
 * Created by SUN on 3/7/2018.
 */

public class ActionReceiver {

    public static final String ACTION_FAVORITE = "ACTION_FAVORITE";


    public static void sendBroadcast(Context context, String action){
        Intent intent = new Intent(action);
        context.sendBroadcast(intent);
    }
}

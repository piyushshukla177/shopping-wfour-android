package com.wfour.onlinestoreapp.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.view.fragments.StartScreenFragment;


/**
 * Created by Suusoft on 01/03/2018.
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        onContinue();

    }

    private void onContinue() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(DataStoreManager.getOpenApp()){
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(SplashActivity.this, StartScreenActivity.class);
                    DataStoreManager.saveOpenApp(true);
                    startActivity(intent);
                    finish();
                }

            }
        }, 2000);
    }

}

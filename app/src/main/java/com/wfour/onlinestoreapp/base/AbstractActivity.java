package com.wfour.onlinestoreapp.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.utils.AppUtil;

/**
 * Created by Suusoft on 7/10/2016.
 */
public abstract class AbstractActivity extends AppCompatActivity {

    public enum ToolbarType{
        MENU_LEFT, // get menu left
        NAVI,    // get button back
        NONE   // none
    }

    protected Context self;

    protected FrameLayout contentLayout;
    // toolbar
    protected Toolbar toolbar;
    protected TextView tvTitle;

    protected abstract ToolbarType getToolbarType();
    protected abstract void getExtraData(Intent intent);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self =  this;
        getExtraData(getIntent());
        setView();
        initViewBase();
        regisNetworkListener();
    }

    private void setView() {
        if (getToolbarType() == ToolbarType.MENU_LEFT) {
            setContentView(R.layout._base_drawer);
            initToolbar();
        }else if (getToolbarType() == ToolbarType.NAVI){
            setContentView(R.layout._base_frame);
            initToolbar();
        }else if (getToolbarType() == ToolbarType.NONE){
            setContentView(R.layout.base_content);
        }
    }

    private void initViewBase(){
        contentLayout = (FrameLayout) findViewById(R.id.content_main);
        //BaseRequest.getInstance().setActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //BaseRequest.getInstance().setActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadCastNetwork);

    }
    /**
     * initialize toolbar
     *
     */
    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) toolbar.findViewById(R.id.tv_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        if (getToolbarType() == ToolbarType.NAVI){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtil.hideSoftKeyboard(AbstractActivity.this);
                    finish();
                }
            });
        }
    }

    public Toolbar getToolbar(){
        return toolbar;
    }

    /**
     * set title for toolbar
     *
     */
    public void setToolbarTitle(String title){
        tvTitle.setText(title);
    }

    public void setToolbarTitle(int title){
        tvTitle.setText(getString(title));
    }

    /**
     * start activity
     * @param clz class name
     *
     */
    public void startActivity(Class<?> clz){
        AppUtil.startActivity(self, clz);
    }

    public void startActivity(Class<?> clz, Bundle bundle){
        AppUtil.startActivity(self, clz, bundle);
    }

    /**
     * show toast message
     * @param message
     *
     */
    public void showToast(String message){
        AppUtil.showToast(self,message);
    }

    public void showToast(int message){
        AppUtil.showToast(self,getString(message));
    }

    /**
     * show snack bar message
     * @param message
     *
     */
    public void showSnackBar(int message){
        Snackbar.make(contentLayout, getString(message), Snackbar.LENGTH_LONG).show();
    }
    public void showSnackBar(String message){
        Snackbar.make(contentLayout, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Listener network state
     *
     */
    private BroadcastReceiver broadCastNetwork = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("broadCastNetwork","changed");
        }
    };

    private void regisNetworkListener() {
        registerReceiver(broadCastNetwork, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

}

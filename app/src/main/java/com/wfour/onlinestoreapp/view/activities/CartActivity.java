package com.wfour.onlinestoreapp.view.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.view.fragments.CartListFragment;
import com.wfour.onlinestoreapp.view.fragments.HomeFragment;

public class CartActivity extends BaseActivity implements View.OnClickListener  {

    private CartListFragment mFrgCartList;
    private Toolbar toolbar;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    String action = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        addFragment();
}

    @Override
    protected void getExtraValues() {
        super.getExtraValues();
    }

    @Override
    void inflateLayout() {

    }
    public void addFragment(){
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (mFrgCartList == null) {
                mFrgCartList = CartListFragment.newInstance();
            }
            fragmentTransaction.replace(R.id.frgCart, mFrgCartList).commit();
    }




    @Override
    void initUI() {

    }

    @Override
    void initControl() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            MainActivity.nav_main.getMenu().findItem(R.id.home_menu).setChecked(true);
            finish();
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

package com.wfour.onlinestoreapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.objects.CartObj;
import com.wfour.onlinestoreapp.objects.ProductObj;
import com.wfour.onlinestoreapp.view.fragments.DealListFragment;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.view.fragments.ReservationListFragment;

import java.util.ArrayList;

public class DealsActivity extends com.wfour.onlinestoreapp.base.BaseActivity implements DealListFragment.IListenerDealsChange {

    private ArrayList<ProductObj> listData;
    private MenuItem menuMapItem;
    private Menu menu;
    private Fragment curFragment;

    private String mIdProductCate;
    private String mNameCategory;
    private String mFilter;
    private Bundle bundle;

    private DealListFragment dealListFragment;
    private int count;
    private ArrayList<CartObj> cartList;

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.layout_content;
    }

    @Override
    protected void getExtraData(Intent intent) {
        bundle = intent.getExtras();
        if (bundle.containsKey(Args.KEY_ID_PRODUCT_CATE)){
            mIdProductCate = bundle.getString(Args.KEY_ID_PRODUCT_CATE);
        }

        if (bundle.containsKey(Args.TYPE_OF_PRODUCT_NAME)){
            mNameCategory = bundle.getString(Args.TYPE_OF_PRODUCT_NAME);
        }

        if(bundle.containsKey(Args.FILTER)){
            mFilter = bundle.getString(Args.FILTER);
            Log.e("hoan", mFilter + " hahahahah");
        }

    }

    @Override
    protected void initilize() {
    }

    @Override
    protected void initView() {
        if (bundle.containsKey(Args.KEY_KEY_WORD)) {
            setToolbarTitle(R.string.search_results);
        } else {
            setToolbarTitle(mNameCategory);
        }

        if (bundle.containsKey(Args.TYPE_OF_SEARCH_PRODUCT)){
            if (bundle.get(Args.TYPE_OF_SEARCH_PRODUCT).equals(Constants.SOLD)){
                replaceFragment(ReservationListFragment.newInstance(bundle));
                setToolbarTitle(R.string.sold);
            } else {
                bundle.putString(Args.FILTER, mFilter);
                dealListFragment = DealListFragment.newInstance(bundle);
                replaceFragment(dealListFragment);
            }
        }


    }

    @Override
    protected void onViewCreated() {
    }

    private void replaceFragment(Fragment fragment) {
        curFragment = fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        MenuItem menuItem = menu.findItem(R.id.action_cart);
        menuItem.setIcon(GlobalFunctions.buildCounterDrawable(self,count, R.drawable.ic_cart));
        return true;
    }
    @Override
    public void onResume() {
        GlobalFunctions.getCountCart(cartList, count);
        count = DataStoreManager.getCountCart();
        invalidateOptionsMenu();
        super.onResume();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_cart) {
            GlobalFunctions.startActivityWithoutAnimation(self, CartActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public ArrayList<ProductObj> getListData() {
        if (listData == null)
            listData = new ArrayList<>();
        return listData;
    }



    @Override
    public void onChanged(ArrayList<ProductObj> mProductObj) {
        listData = mProductObj;
    }
}

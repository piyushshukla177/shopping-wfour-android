package com.wfour.onlinestoreapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IMyOnClick;
import com.wfour.onlinestoreapp.network.ApiResponse;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.network1.NetworkUtility;
import com.wfour.onlinestoreapp.objects.CartObj;
import com.wfour.onlinestoreapp.objects.ProductObj;
import com.wfour.onlinestoreapp.view.adapters.SingleAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

public class ProductListActivity extends BaseActivity {

    private Toolbar toolbar;
    private String name, mFilter;
    private RecyclerView mRclDeal;

    private SingleAdapter mAdapter;
    private ArrayList<ProductObj> mDatas;
    private ArrayList<ProductObj> List = new ArrayList<>();
    private int count;
    private ArrayList<CartObj> cartList;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int page =1;
    private int number_per_page = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        toolbar = findViewById(R.id.toolbar);
        mRclDeal = findViewById(R.id.rcv_data);

        getExtraData();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(name);

        if (mDatas == null) {
            mDatas = new ArrayList<>();
        } else {
            mDatas.clear();
        }

        mAdapter = new SingleAdapter(self, List, new IMyOnClick() {
            @Override
            public void MyOnClick(int position, ProductObj productObj) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Args.KEY_PRODUCT_OBJECT, productObj);
                GlobalFunctions.startActivityWithoutAnimation(self, DealDetailActivity.class, bundle);
            }
        });
        mRclDeal.setAdapter(mAdapter);
        final GridLayoutManager layoutManager = new GridLayoutManager(self, 2);
        mRclDeal.setLayoutManager(layoutManager);
        mAdapter.addList(List);
        mRclDeal.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount(); // so muc hien thi
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition(); // muc hien thi truoc day


                    if (loading)
                    {

                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            Log.e("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            page = (totalItemCount/number_per_page)+ 1;
                            getData(mFilter,page, number_per_page);

                        }
                    }
                }
            }
        });

    }

    public void getExtraData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            name = bundle.getString(Args.TYPE_OF_PRODUCT_NAME);
            mFilter = bundle.getString(Args.FILTER);
        }
            getData(mFilter, page, number_per_page);

    }
    private void getData(final String mFilter, int page, int number_per_page){
        if(NetworkUtility.getInstance(self).isNetworkAvailable()){
            ModelManager.getProductFillter(self, mFilter, page, number_per_page, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject obj = (JSONObject) object;
                    ApiResponse response = new ApiResponse(obj);

                    mDatas = new ArrayList<>();
                    if(!response.isError()){
                        mDatas.addAll(response.getDataList(ProductObj.class));
                        List.addAll(mDatas);
                        mAdapter.addList(List);
                        loading = true;

                    }
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cart, menu);
        MenuItem menuItem = menu.findItem(R.id.action_cart);
        menuItem.setIcon(GlobalFunctions.buildCounterDrawable(self,count, R.drawable.ic_cart));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_cart) {
            GlobalFunctions.startActivityWithoutAnimation(self, CartActivity.class);
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalFunctions.getCountCart(cartList,count);
        count = DataStoreManager.getCountCart();
        invalidateOptionsMenu();
    }

    @Override
    void inflateLayout() {

    }

    @Override
    void initUI() {

    }

    @Override
    void initControl() {

    }



}

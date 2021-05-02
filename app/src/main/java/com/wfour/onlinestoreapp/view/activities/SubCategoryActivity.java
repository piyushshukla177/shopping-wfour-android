package com.wfour.onlinestoreapp.view.activities;

import android.content.Intent;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.base.BaseActivity;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.MyOnClickCategory;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.CartObj;
import com.wfour.onlinestoreapp.objects.Category;
import com.wfour.onlinestoreapp.objects.DealCateObj;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.view.adapters.CategoryAdapter;
import com.wfour.onlinestoreapp.widgets.recyclerview.EndlessRecyclerOnScrollListener;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.wfour.onlinestoreapp.globals.Constants.COLUM_GIRD_DEAL;
import static com.wfour.onlinestoreapp.globals.Constants.COLUM_LIST_DEAL;

public class SubCategoryActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, EndlessRecyclerOnScrollListener.OnLoadMoreListener {

    private static final String TAG = SubCategoryActivity.class.getSimpleName();
    private Bundle bundle;
    private String mIdProductCate;
    private String mNameCategory;
    private ArrayList<CartObj> cartList;
    private int count;
    private RecyclerView mRclDeal;
    //private DealAdapter mAdapter;
    private CategoryAdapter mAdapter;

    //private ArrayList<DealObj> mDatas ;
    private ArrayList<Category> mDatas;
    private LinearLayout llNoData, llNoConnection;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String productCateogyId;
    private String typeOfSearch;
    private String dealCategoryName, sortType, sortBy;
    private int page;


    private String mKeyWord;
    private String mDistance;
    private String mNumDealPerPage;
    private String isActiveDeal;
    private String mFilter;

    private GridLayoutManager layoutManager;
    private EndlessRecyclerOnScrollListener onScrollListener;

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_sub_category;
    }

    @Override
    protected void getExtraData(Intent intent) {
        bundle = intent.getExtras();
        if (bundle.containsKey(Args.KEY_ID_PRODUCT_CATE)) {
            mIdProductCate = bundle.getString(Args.KEY_ID_PRODUCT_CATE);
        }
        if (bundle.containsKey(Args.TYPE_OF_PRODUCT_NAME)) {
            mNameCategory = bundle.getString(Args.TYPE_OF_PRODUCT_NAME);
        }
    }

    @Override
    protected void initilize() {

    }

    @Override
    protected void initView() {
        setToolbarTitle(mNameCategory);
        mRclDeal = (RecyclerView) findViewById(R.id.rcv_data);
        llNoData = (LinearLayout) findViewById(R.id.ll_no_data);
        llNoConnection = (LinearLayout) findViewById(R.id.ll_no_connection);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        if (AppUtil.getWidthDp(this) > 600) {
            layoutManager = new GridLayoutManager(self, COLUM_GIRD_DEAL);
        } else {
            layoutManager = new GridLayoutManager(self, COLUM_LIST_DEAL);
        }

        onScrollListener = new EndlessRecyclerOnScrollListener(this, layoutManager);
        mRclDeal.setLayoutManager(layoutManager);
        mRclDeal.addOnScrollListener(onScrollListener);

        if (mDatas == null) {
            mDatas = new ArrayList<>();
        } else {
            mDatas.clear();
        }

        mAdapter = new CategoryAdapter(this, mDatas, new MyOnClickCategory() {
            @Override
            public void onClick(Category category, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(Args.KEY_ID_PRODUCT_CATE, category.getId() + "");
                bundle.putString(Args.TYPE_OF_PRODUCT_NAME, category.getName() + "");
                if (category.getId().equals(DealCateObj.MY_FAVORITES)) {
                    bundle.putString(Args.TYPE_OF_SEARCH_PRODUCT, Constants.SEARCH_FAVORIES);
                } else {
                    bundle.putString(Args.TYPE_OF_SEARCH_PRODUCT, Constants.SEARCH_NEARBY);
                }
                GlobalFunctions.startActivityWithoutAnimation(SubCategoryActivity.this, DealsActivity.class, bundle);
            }
        });
        mRclDeal.setAdapter(mAdapter);

        mRclDeal.setLayoutManager(new GridLayoutManager(self, 3));

    }

    @Override
    protected void onViewCreated() {
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        MenuItem menuItem = menu.findItem(R.id.action_cart);
        menuItem.setIcon(GlobalFunctions.buildCounterDrawable(self, count, R.drawable.ic_cart));
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

    @Override
    public void onRefresh() {
        mDatas.clear();
        mAdapter.notifyDataSetChanged();
        onScrollListener.setEnded(false);
        getData();
    }

    @Override
    public void onLoadMore(int page) {
        if (!mDatas.isEmpty()) {
            getData();
        }
    }

    protected void getData() {

        if (!onScrollListener.isEnded()) {
            swipeRefreshLayout.setRefreshing(true);
            ModelManager.getSubCategory(self, mIdProductCate, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject jsonObject = (JSONObject) object;
                    ApiResponse response = new ApiResponse(jsonObject);
                    if (!response.isError()) {
                        mDatas.addAll(response.getDataList(Category.class));
                        Log.e(TAG, "onSuccess: " + mDatas.toString());
                        mAdapter.notifyDataSetChanged();
                        onScrollListener.onLoadMoreComplete();
                        onScrollListener.setEnded(true);
                        swipeRefreshLayout.setRefreshing(false);
                        checkViewHideShow();

                    } else {
                        Toast.makeText(self, response.getMessage(), Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }


                }

                @Override
                public void onError() {
                    Toast.makeText(self, R.string.msg_have_some_errors, Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    private void checkViewHideShow() {

        if (mDatas.isEmpty()) {
            finish();
            Bundle bundle = new Bundle();
            bundle.putString(Args.KEY_ID_PRODUCT_CATE,mIdProductCate );
            bundle.putString(Args.TYPE_OF_PRODUCT_NAME, mNameCategory);
            if (mNameCategory.equals(DealCateObj.MY_FAVORITES)) {
                bundle.putString(Args.TYPE_OF_SEARCH_PRODUCT, Constants.SEARCH_FAVORIES);
            } else {
                bundle.putString(Args.TYPE_OF_SEARCH_PRODUCT, Constants.SEARCH_NEARBY);
            }
            GlobalFunctions.startActivityWithoutAnimation(SubCategoryActivity.this, DealsActivity.class, bundle);
           // llNoData.setVisibility(View.VISIBLE);
        } else {
            llNoData.setVisibility(View.GONE);
        }
    }
}

package com.wfour.onlinestoreapp.view.activities;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IMyOnClick;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.network1.NetworkUtility;
import com.wfour.onlinestoreapp.objects.ProductObj;
import com.wfour.onlinestoreapp.parsers.JSONParser;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.view.adapters.SearchProductAdapter;
import com.wfour.onlinestoreapp.widgets.onscroll.EndlessRecyclerOnScrollListener;
//import com.wfour.onlinestoreapp.widgets.recyclerview.EndlessRecyclerViewScrollListener;

import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity2 extends BaseActivity implements EndlessRecyclerOnScrollListener.OnLoadMoreListener {

    LinearLayout last_seen_linear;
    private Toolbar toolbar;
    private SearchProductAdapter mAdapter;
    private RecyclerView mRclDeal;
    private ArrayList<ProductObj> mDatas;
    private String textSearch;
    private LinearLayout llNoData;
    private EndlessRecyclerOnScrollListener onScrollListener;
    private LinearLayoutManager manager;
    private int page;
    private int numberpage;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);

        AppUtil.hideSoftKeyboard(this);
        mRclDeal = findViewById(R.id.rcv_data);
        llNoData = findViewById(R.id.ll_no_data);
        toolbar = findViewById(R.id.toolbar);
        last_seen_linear = findViewById(R.id.last_seen_linear);
        manager = new LinearLayoutManager(self);
        mRclDeal.setLayoutManager(manager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.search));

        if (mDatas == null) {
            mDatas = new ArrayList<>();
        } else {
            mDatas.clear();
        }

        mAdapter = new SearchProductAdapter(self, mDatas, new IMyOnClick() {
            @Override
            public void MyOnClick(int position, ProductObj productObj) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Args.KEY_PRODUCT_OBJECT, productObj);
                GlobalFunctions.startActivityWithoutAnimation(self, DealDetailActivity.class, bundle);
            }
        });
        mRclDeal.setAdapter(mAdapter);
        onScrollListener = new EndlessRecyclerOnScrollListener(this);
        mRclDeal.addOnScrollListener(onScrollListener);
        onScrollListener.setEnded(false);
//        mRclDeal.setLayoutManager(new GridLayoutManager(self, 1));

    }

    @Override
    void inflateLayout() {

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_view, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(true);
        searchView.setQueryHint(Html.fromHtml("<font color = #BDBDBD>" + getResources().getString(R.string.hintSearchMess) + "</font>"));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                mDatas.clear();
//                if (mDatas.size() != 0) {
//                    last_seen_linear.setVisibility(View.GONE);
//                }
                text = query;
                page = 1;
                // setcurrent page ve 0
                onScrollListener.setCurrentPage(page);
                getData(text, page);
                searchItem.collapseActionView();
                searchView.clearFocus();
                mDatas.clear();
                mAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void getData(String query, int number) {
        if (NetworkUtility.getInstance(self).isNetworkAvailable()) {
            ModelManager.getProductSearch(self, query, number, 20, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject jsonObject = (JSONObject) object;
                    com.wfour.onlinestoreapp.base.ApiResponse response = new com.wfour.onlinestoreapp.base.ApiResponse(jsonObject);

                    last_seen_linear.setVisibility(View.GONE);
                    if (!response.isError()) {
                        mDatas.addAll(response.getDataList(ProductObj.class));
                        mAdapter.addList(mDatas);
                        onScrollListener.onLoadMoreComplete();
                        onScrollListener.setEnded(JSONParser.isEnded(response, page));
//                        Log.e("SearchActivity2", "isEnded: "+JSONParser.isEnded(response, page) );
//                        Log.e("SearchActivity2", "onSuccess: "+ mDatas.size() );
                        checkViewHideShow();
                        AppUtil.hideSoftKeyboard(self);
                    }
                }

                @Override
                public void onError() {

                }
            });

        }
    }

    private void checkViewHideShow() {
        if (mDatas.size() == 0) {
//            llNoData.setVisibility(View.VISIBLE);
        } else {
            llNoData.setVisibility(View.GONE);
        }
    }


    @Override
    void initUI() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    void initControl() {

    }

    @Override
    public void onLoadMore(int page) {
        this.page = page;
        getData(text, page);
//        Log.e(SearchActivity2.class.getSimpleName(), "onLoadMore: " + page );
    }
}

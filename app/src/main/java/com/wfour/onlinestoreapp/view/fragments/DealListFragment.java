package com.wfour.onlinestoreapp.view.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IMyOnClick;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.ProductObj;
import com.wfour.onlinestoreapp.parsers.JSONParser;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.view.Dialogs.Dialog_Cart;
import com.wfour.onlinestoreapp.view.activities.DealDetailActivity;
import com.wfour.onlinestoreapp.view.activities.DealsActivity;
import com.wfour.onlinestoreapp.view.adapters.DealAdapter;
import com.wfour.onlinestoreapp.view.adapters.SingleAdapter;
import com.wfour.onlinestoreapp.widgets.recyclerview.EndlessRecyclerOnScrollListener;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.wfour.onlinestoreapp.globals.Constants.COLUM_GIRD_DEAL;
import static com.wfour.onlinestoreapp.globals.Constants.COLUM_LIST_DEAL;

/**
 * Created by Suusoft on 11/28/2016.
 */

public class DealListFragment extends com.wfour.onlinestoreapp.base.BaseFragment implements EndlessRecyclerOnScrollListener.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRclDeal;
    //private DealAdapter mAdapter;
    private SingleAdapter mAdapter;
    //private ArrayList<DealObj> mDatas ;
    private ArrayList<ProductObj> mDatas;
    private LinearLayout llNoData, llNoConnection;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String productCateogyId;
    private String typeOfSearch;
    private String dealCategoryName, sortType, sortBy;
    private int page;

    IListenerDealsChange listenerDealsChange;
    private String mKeyWord;
    private String mDistance;
    private String mNumDealPerPage;
    private String isActiveDeal;
    private String mFilter;

    private GridLayoutManager layoutManager;
    private EndlessRecyclerOnScrollListener onScrollListener;

    private boolean isNeedUpdate;


    private DealsActivity mDealsActivity;

    // receive when need update. from DealAboultFragment
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isNeedUpdate = true;
            int position = intent.getIntExtra(Args.POSITION, -1);
            if (position != -1) {
                boolean isFavourite = intent.getBooleanExtra(Args.IS_FAVORITE, false);
                ProductObj item = mDatas.get(position);
//                item.setFavorite(isFavourite);
//                if (isFavourite) {
//                    item.setFavoriteQuantity(item.getFavoriteQuantity() + 1);
//                } else {
//                    item.setFavoriteQuantity(item.getFavoriteQuantity() + -1);
//                }
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    public interface IListenerDealsChange {
        void onChanged(ArrayList<ProductObj> mProductObj);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listenerDealsChange = (IListenerDealsChange) (context) ;
    }

    public static DealListFragment newInstance(Bundle args ) {

        DealListFragment fragment = new DealListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.layout_single_list;
    }

    @Override
    protected void init() {
        Bundle bundle = getArguments();
        productCateogyId = bundle.getString(Args.KEY_ID_PRODUCT_CATE);
        dealCategoryName = bundle.getString(Args.TYPE_OF_PRODUCT_NAME, "");
        typeOfSearch = bundle.getString(Args.TYPE_OF_SEARCH_PRODUCT, "");
        mKeyWord = bundle.getString(Args.KEY_KEY_WORD);
        mDistance = bundle.getString(Args.KEY_DISTANCE);
        mNumDealPerPage = bundle.getString(Args.KEY_NUM_PRODUCT_PER_PAGE);

        isActiveDeal = bundle.getString(Args.KEY_ACTIVE_PRODUCT);

    }


    @Override
    protected void initView(View view) {
        //mDealsActivity = (DealsActivity)getActivity();
        mRclDeal = (RecyclerView) view.findViewById(R.id.rcv_data);
        llNoData = (LinearLayout) view.findViewById(R.id.ll_no_data);
        llNoConnection = (LinearLayout) view.findViewById(R.id.ll_no_connection);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        if (AppUtil.getWidthDp(getActivity()) > 600){
            layoutManager = new GridLayoutManager(self, COLUM_GIRD_DEAL);
        }else {
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

        mAdapter = new SingleAdapter((DealsActivity) getActivity(), mDatas, new IMyOnClick() {
            @Override
            public void MyOnClick(int position, ProductObj productObj) {
                if(productObj.getIs_prize()==1){
                    showDialog(productObj);
                }
                else {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Args.KEY_PRODUCT_OBJECT, productObj);
                    bundle.putString("DealList", "DealList");
                    GlobalFunctions.startActivityWithoutAnimation(self, DealDetailActivity.class, bundle);
                }

            }
        });
        mRclDeal.setAdapter(mAdapter);

        mRclDeal.setLayoutManager(new GridLayoutManager(self, 2));

    }
    private void showDialog(ProductObj productObj) {
        Dialog_Cart dialogCart = new Dialog_Cart();
        dialogCart.show(getChildFragmentManager(), "DialogCart");

        Bundle bundle = new Bundle();
        bundle.putParcelable(Args.KEY_PRODUCT_OBJECT, productObj);
        dialogCart.setArguments(bundle);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == DealAdapter.RQ_UPDATE_DEAL) {
            if (resultCode == DealDetailActivity.RC_UPDATE_DEAL) {
                mDatas.clear();

                mAdapter.notifyDataSetChanged();
                page = 1;
                onScrollListener.setEnded(false);
                onScrollListener.setCurrentPage(page);
                getData();
            }
        }
    }

    public void refreshData(ArrayList<ProductObj> productObjs) {
        mDatas = productObjs;
        mAdapter.notifyDataSetChanged();
    }

    public void sortData(String sortBy, String sortType) {
        this.sortBy = sortBy;
        this.sortType = sortType;
        page = 1;
        if (mDatas!=null )mDatas.clear();
        onScrollListener.setEnded(false);
        onScrollListener.setCurrentPage(1);
        getData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        if (self != null) {
//            self.unregisterReceiver(broadcastReceiver);
//        }

    }

    @Override
    public void onRefresh() {
        mDatas.clear();
        mAdapter.notifyDataSetChanged();
        page = 1;
        onScrollListener.setEnded(false);
        onScrollListener.setCurrentPage(page);
        getData();
    }

    @Override
    protected void getData() {

        if (!onScrollListener.isEnded()) {
            swipeRefreshLayout.setRefreshing(true);
            ModelManager.getProductList(self, productCateogyId , page, mNumDealPerPage, new ModelManagerListener() {
                        @Override
                        public void onSuccess(Object object) {
                            JSONObject jsonObject = (JSONObject) object;
                            ApiResponse response = new ApiResponse(jsonObject);
                            if (!response.isError()) {
                                mDatas.addAll(response.getDataList(ProductObj.class));
                                listenerDealsChange.onChanged(mDatas);
                                mAdapter.notifyDataSetChanged();
                                onScrollListener.onLoadMoreComplete();
                                onScrollListener.setEnded(JSONParser.isEnded(response, page));
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

    @Override
    public void onLoadMore(int page) {


        if (!mDatas.isEmpty() ) {
            this.page = page;
            getData();
        }
    }

    private void checkViewHideShow() {
        if (mDatas.isEmpty()) {
            llNoData.setVisibility(View.VISIBLE);
        } else {
            llNoData.setVisibility(View.GONE);
        }
    }

}

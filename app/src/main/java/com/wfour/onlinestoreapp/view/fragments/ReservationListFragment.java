package com.wfour.onlinestoreapp.view.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.interfaces.IOnItemClickMenuListener;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.DealObj;
import com.wfour.onlinestoreapp.objects.ReservationObj;
import com.wfour.onlinestoreapp.parsers.JSONParser;
import com.wfour.onlinestoreapp.quickblox.conversation.utils.DialogUtil;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.ViewUtil;
import com.wfour.onlinestoreapp.view.activities.DealDetailActivity;
import com.wfour.onlinestoreapp.view.activities.TripFinishingActivity;
import com.wfour.onlinestoreapp.view.adapters.DealAdapter;
import com.wfour.onlinestoreapp.view.adapters.ReservationAdatper;
import com.wfour.onlinestoreapp.widgets.recyclerview.EndlessRecyclerOnScrollListener;
import com.wfour.onlinestoreapp.widgets.textview.TextViewBold;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suusoft on 11/28/2016.
 */

public class ReservationListFragment extends com.wfour.onlinestoreapp.base.BaseFragment implements EndlessRecyclerOnScrollListener.OnLoadMoreListener, IOnItemClickMenuListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRclDeal;
    private ReservationAdatper mAdapter;
    private DealAdapter adapter;
    private ArrayList<DealObj> mDatas;
    private List<ReservationObj> reservationList;
    private LinearLayout llNoData, llNoConnection;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String typeOfSearch;
    private int page = 1;

    private GridLayoutManager layoutManager;
    private EndlessRecyclerOnScrollListener onScrollListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static ReservationListFragment newInstance(Bundle args) {

        ReservationListFragment fragment = new ReservationListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.layout_list_reservation;
    }

    @Override
    protected void init() {
        Bundle bundle = getArguments();
        typeOfSearch = bundle.getString(Args.TYPE_OF_SEARCH_DEAL, "");

    }

    @Override
    protected void initView(View view) {
        mRclDeal = (RecyclerView) view.findViewById(R.id.rcv_data);
        llNoData = (LinearLayout) view.findViewById(R.id.ll_no_data);
        llNoConnection = (LinearLayout) view.findViewById(R.id.ll_no_connection);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        if (AppUtil.getWidthDp(getActivity()) > 600){
            layoutManager = new GridLayoutManager(self, 2);
        }else {
            layoutManager = new GridLayoutManager(self, 1);
        }

        onScrollListener = new EndlessRecyclerOnScrollListener(this, layoutManager);
        mRclDeal.setLayoutManager(layoutManager);
        mRclDeal.addOnScrollListener(onScrollListener);

        // Init adapter first
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        } else {
            mDatas.clear();
        }
        mAdapter = new ReservationAdatper(this, mDatas, typeOfSearch, this);

//        adapter =  new DealAdapter(this, mDatas,typeOfSearch);
        mRclDeal.setAdapter(mAdapter);
    }

    @Override
    public void onLoadMore(int page) {
        Log.e("xx","more");

        if (!mDatas.isEmpty()) {
            this.page = page;
            getData();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ReservationAdatper.RQ_UPDATE_DEAL) {
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

    private void showRatingDialog(final DealObj item, final ReservationObj reservationObj) {
        final Dialog dialog = DialogUtil.setDialogCustomView(self, R.layout.dialog_rating, false );

        TextViewBold lblSubmit = (TextViewBold) dialog.findViewById(R.id.lbl_submit);
        final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.rating_bar);
        final EditText edtContent = (EditText) dialog.findViewById(R.id.txt_comment);
        ((LayerDrawable) ratingBar.getProgressDrawable()).getDrawable(2)
                .setColorFilter(ratingBar.getContext().getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        ViewUtil.setRatingbarColor(ratingBar);

        lblSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rating = ratingBar.getRating();
                String content = edtContent.getText().toString();
                if (rating == 0) {
                    AppUtil.showToast(getActivity(), R.string.msg_rating_require);
                    return;
                }
                if (content.isEmpty()) {
                    AppUtil.showToast(getActivity(), R.string.msg_content_require);
                    return;
                } else {
                    if (typeOfSearch.equals(Constants.SOLD)) {
                        postComment(rating * 2, content, String.valueOf(reservationObj.getBuyer_id()), item.getId(), Constants.DEAL);
                    } else {
                        postComment(rating * 2, content, item.getSeller_id(), item.getId(), Constants.DEAL);
                    }
                }

                dialog.dismiss();
            }
        });

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }


    @Override
    protected void getData() {
        if (!onScrollListener.isEnded()) {
            Log.e("ReservationListFragment","xxx: "+typeOfSearch+" page "+page );
            swipeRefreshLayout.setRefreshing(true);
            ModelManager.getReservationList(self, typeOfSearch, "", page, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    Log.e("ReservationListFragment", "onSuccess " + object.toString() );
                    JSONObject jsonObject = (JSONObject) object;
                    ApiResponse response = new ApiResponse(jsonObject);
                    if (!response.isError()) {
                        mDatas.clear();
                        Log.e("ReservationListFragment", "onSuccess response success" );
                        reservationList = response.getDataList(ReservationObj.class);
                        for (ReservationObj item : reservationList) {
                            mDatas.add(item.getDeal());
                            Log.e("ReservationListFragment", "add" + item.getDeal().toString() );
                        }
                        Log.e("ReservationListFragment", "add" + mDatas.toString() );
                        Log.e("ReservationListFragment", "add" + mDatas.size() );

                        mAdapter.notifyDataSetChanged();

                        onScrollListener.onLoadMoreComplete();
                        onScrollListener.setEnded(JSONParser.isEnded(response, page));

                        swipeRefreshLayout.setRefreshing(false);
                        checkViewHideShow();
                    } else {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(self, response.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onError() {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(self, R.string.msg_have_some_errors, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void postComment(final float rating, final String content, String sellerId, String objectId, String objectType) {
        String from;
        String to;
        if (typeOfSearch.equals(Constants.SOLD)) {
            from = Constants.SELLER;
            to = Constants.BUYER;
        } else {
            from = Constants.BUYER;
            to = Constants.SELLER;
        }
        ModelManager.postReview(self, sellerId, to, from,
                objectId, objectType, content, rating + "", new ModelManagerListener() {
                    @Override
                    public void onSuccess(Object object) {
                        JSONObject jsonObject = (JSONObject) object;
                        ApiResponse response = new ApiResponse(jsonObject);
                        if (!response.isError()) {
                            Toast.makeText(self, R.string.rating_success, Toast.LENGTH_SHORT).show();
                            refreshData();
                        } else {
                            Toast.makeText(self, response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(self, R.string.msg_have_some_errors, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onClickPay(int position) {
        ReservationObj item = reservationList.get(position);
        TripFinishingActivity.start(getActivity(), null, item);
    }

    @Override
    public void onClickRate(int position) {
        DealObj item = mDatas.get(position);
        showRatingDialog(item, reservationList.get(position));
    }

    private void checkViewHideShow() {
        if (mDatas.isEmpty()) {
            llNoData.setVisibility(View.VISIBLE);
        } else {
            llNoData.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    private void refreshData() {
        mDatas.clear();
        mAdapter.notifyDataSetChanged();
        page = 1;
        onScrollListener.onLoadMoreComplete();
        onScrollListener.setEnded(false);
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }
}

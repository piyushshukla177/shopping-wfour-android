package com.wfour.onlinestoreapp.view.fragments;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.view.adapters.DealReviewAdapter;
import com.wfour.onlinestoreapp.base.*;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.DealReviewObj;
import com.wfour.onlinestoreapp.parsers.JSONParser;
import com.wfour.onlinestoreapp.widgets.recyclerview.EndlessRecyclerOnScrollListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suusoft on 11/24/2016.
 */

public class DealReviewFragment extends com.wfour.onlinestoreapp.base.BaseFragment implements View.OnClickListener, EndlessRecyclerOnScrollListener.OnLoadMoreListener {

    private static final String KEY_OJECT_ID = "OJECT_ID";
    private static final String KEY_OBJECT_TYPE = "OBJECT_TYPE";
    private static final String KEY_SELLER_ID = "SELLER_ID";

    private RecyclerView rcvData;
    private LinearLayout llNoData, llNoConnection;
    private DealReviewAdapter mAdapter;
    private List<DealReviewObj> mDatas;

    private String objectId;
    private String objectType;
    private int page = 1;

    private LinearLayoutManager manager;

    private EndlessRecyclerOnScrollListener onScrollListener;

    public static DealReviewFragment newInstance(String objectId, String objectType, String sellerId) {

        Bundle args = new Bundle();
        args.putString(KEY_OJECT_ID, objectId);
        args.putString(KEY_OBJECT_TYPE, objectType);
        args.putString(KEY_SELLER_ID, sellerId);
        DealReviewFragment fragment = new DealReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_deal_revies;
    }

    @Override
    protected void init() {
        objectId = getArguments().getString(KEY_OJECT_ID);
        objectType = getArguments().getString(KEY_OBJECT_TYPE);

    }

    @Override
    protected void initView(View view) {
        rcvData = (RecyclerView) view.findViewById(R.id.rcv_data);
        manager = new LinearLayoutManager(self);
        rcvData.setLayoutManager(manager);
        llNoData = (LinearLayout) view.findViewById(R.id.ll_no_data);
        llNoConnection = (LinearLayout) view.findViewById(R.id.ll_no_connection);
        /*if (objectType.equals(Constants.PRO)) {
            btnRate.setVisibility(View.GONE);
        }*/
        initAdapter();
    }

    private void initAdapter() {
        mDatas = new ArrayList<>();
        mAdapter = new DealReviewAdapter(self, mDatas);
        rcvData.setAdapter(mAdapter);
        onScrollListener = new EndlessRecyclerOnScrollListener(this, manager);
        rcvData.addOnScrollListener(onScrollListener);
    }

    @Override
    protected void getData() {
        ModelManager.getReview(self, objectId, objectType, page, new ModelManagerListener() {
            @Override
            public void onSuccess(Object object) {
                JSONObject jsonObject = (JSONObject) object;
                ApiResponse response = new ApiResponse(jsonObject);
                if (!response.isError()) {
                    mDatas.addAll(response.getDataList(DealReviewObj.class));
                    mAdapter.notifyDataSetChanged();
                    onScrollListener.onLoadMoreComplete();
                    onScrollListener.setEnded(JSONParser.isEnded(response, page));
                    if (mDatas.isEmpty()) {
                        llNoData.setVisibility(View.VISIBLE);
                    } else {
                        llNoData.setVisibility(View.GONE);
                    }
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
    public void onClick(View view) {

    }

    @Override
    public void onLoadMore(int page) {
        this.page = page;
        getData();
    }
}

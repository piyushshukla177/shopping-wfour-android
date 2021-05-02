package com.wfour.onlinestoreapp.view.activities;

import android.content.Intent;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.view.adapters.HistoryAdapter;
import com.wfour.onlinestoreapp.base.*;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IConfirmation;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.network1.NetworkUtility;
import com.wfour.onlinestoreapp.objects.HistoryObj;
import com.wfour.onlinestoreapp.parsers.JSONParser;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.widgets.recyclerview.EndlessRecyclerOnScrollListener;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Suusoft on 15/12/2016.
 */

public class HistoryActivity extends com.wfour.onlinestoreapp.base.BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView rcvData;
    private ArrayList<HistoryObj> listHistory;
    private HistoryAdapter adapter;
    private RelativeLayout btnDeleteAll;
    private LinearLayout llNodata, llNoConnection;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int page = 1;
    private EndlessRecyclerOnScrollListener onScrollListener;
    private TextView tvBtn;

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_history;
    }

    @Override
    protected void getExtraData(Intent intent) {

    }

    @Override
    protected void initilize() {
        listHistory = new ArrayList<>();
        adapter = new HistoryAdapter(this, listHistory);
    }

    @Override
    protected void initView() {
        setToolbarTitle(R.string.history);
        rcvData = (RecyclerView) findViewById(R.id.rcv_data);
        btnDeleteAll = (RelativeLayout) findViewById(R.id.btn_functions);
        tvBtn = (TextView)findViewById(R.id.tv_btn);
        tvBtn.setText(R.string.delete_all);
        llNodata = (LinearLayout) findViewById(R.id.ll_no_data);
        llNoConnection = (LinearLayout) findViewById(R.id.ll_no_connection);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
    }

    private void setUpRecyclerView() {
        rcvData.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvData.setLayoutManager(linearLayoutManager);
        rcvData.setAdapter(adapter);
        onScrollListener = new EndlessRecyclerOnScrollListener(new EndlessRecyclerOnScrollListener.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int page) {
                getHistory(page);
            }
        }, linearLayoutManager);
        rcvData.addOnScrollListener(onScrollListener);
    }

    @Override
    protected void onViewCreated() {
        swipeRefreshLayout.setOnRefreshListener(this);
        setUpRecyclerView();
        getHistory(page);
        btnDeleteAll.setOnClickListener(this);
    }

    private void getHistory(final int page) {
        swipeRefreshLayout.setRefreshing(true);
        ModelManager.getHistory(this, String.valueOf(page), new ModelManagerListener() {
            @Override
            public void onSuccess(Object object) {
                org.json.JSONObject jsonObject = (JSONObject) object;
                ApiResponse apiResponse = new ApiResponse(jsonObject);
                if (!apiResponse.isError()) {
                    listHistory.addAll(apiResponse.getDataList(HistoryObj.class));
                    adapter.notifyDataSetChanged();
                    onScrollListener.onLoadMoreComplete();
                    onScrollListener.setEnded(JSONParser.isEnded(apiResponse, page));
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    AppUtil.showToast(getApplicationContext(), R.string.msg_have_some_errors);
                    swipeRefreshLayout.setRefreshing(false);
                }
                if (listHistory.isEmpty()) {
                    llNodata.setVisibility(View.VISIBLE);
                } else {
                    llNodata.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError() {
                swipeRefreshLayout.setRefreshing(false);
                if (listHistory.isEmpty()) {
                    llNodata.setVisibility(View.VISIBLE);
                } else {
                    llNodata.setVisibility(View.GONE);
                }
                AppUtil.showToast(getApplicationContext(), R.string.msg_have_some_errors);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == btnDeleteAll) {
            if (NetworkUtility.getInstance(getApplicationContext()).isNetworkAvailable()) {
                if (!listHistory.isEmpty()) {
                    GlobalFunctions.showConfirmationDialog(this, getString(R.string.msg_confirm_delete_history),
                            getString(R.string.yes), getString(R.string.no), true, new IConfirmation() {
                        @Override
                        public void onPositive() {
                            deleteAllHistory();
                        }

                        @Override
                        public void onNegative() {

                        }
                    });

                } else {
                    AppUtil.showToast(getApplicationContext(), R.string.msg_no_history);
                }


            } else {
                AppUtil.showToast(getApplicationContext(), R.string.msg_network_not_available);
            }

        }
    }

    private void deleteAllHistory() {
        ModelManager.deleteHistory(this, "", new ModelManagerListener() {
            @Override
            public void onSuccess(Object object) {
                org.json.JSONObject jsonObject = (JSONObject) object;
                ApiResponse apiResponse = new ApiResponse(jsonObject);
                if (!apiResponse.isError()) {
                    listHistory.clear();
                    adapter.notifyDataSetChanged();
                    AppUtil.showToast(getApplicationContext(), R.string.msg_delete_history_success);
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void onRefresh() {
        listHistory.clear();
        adapter.notifyDataSetChanged();
        onScrollListener.setEnded(false);
        onScrollListener.setCurrentPage(page);
        getHistory(page);

    }
}

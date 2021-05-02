package com.wfour.onlinestoreapp.view.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.view.adapters.TripHistoryAdapter;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IConfirmation;
import com.wfour.onlinestoreapp.interfaces.IDeleting;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.TransportDealObj;
import com.wfour.onlinestoreapp.parsers.JSONParser;
import com.wfour.onlinestoreapp.utils.NetworkUtility;
import com.wfour.onlinestoreapp.widgets.recyclerview.EndlessRecyclerOnScrollListener;
import com.wfour.onlinestoreapp.widgets.textview.TextViewBold;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Suusoft on 30/11/2016.
 */

public class TripHistoryFragment extends BaseFragment implements EndlessRecyclerOnScrollListener.OnLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwTrip;
    private RecyclerView mRcl;
    private TripHistoryAdapter mAdapter;
    private ArrayList<TransportDealObj> mTripHistories;

    private LinearLayout mLlNoData, mllNoConnection;
    private TextViewBold mLblDeleteAll;

    private int page = 1;

    private LinearLayoutManager layoutManager;
    private EndlessRecyclerOnScrollListener onScrollListener;

    public static TripHistoryFragment newInstance() {
        return new TripHistoryFragment();
    }

    @Override
    View inflateLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip_history, container, false);
    }

    @Override
    void initUI(View view) {
        mSwTrip = (SwipeRefreshLayout) view.findViewById(R.id.sw_trip);
        mSwTrip.setColorSchemeResources(R.color.primary, R.color.colorPrimaryDark, R.color.colorAccent);
        mRcl = (RecyclerView) view.findViewById(R.id.rcl_trip_history);
        mLlNoData = (LinearLayout) view.findViewById(R.id.ll_no_data);
        mllNoConnection = (LinearLayout) view.findViewById(R.id.ll_no_connection);
        mLblDeleteAll = (TextViewBold) view.findViewById(R.id.lbl_delete_all);

        layoutManager = new LinearLayoutManager(self, LinearLayoutManager.VERTICAL, false);
        onScrollListener = new EndlessRecyclerOnScrollListener(this, layoutManager);
        mRcl.setLayoutManager(layoutManager);
        mRcl.addOnScrollListener(onScrollListener);

        // Init adapter first
        if (mTripHistories == null) {
            mTripHistories = new ArrayList<>();
        } else {
            mTripHistories.clear();
        }

        mAdapter = new TripHistoryAdapter(self, mTripHistories, new IDeleting() {
            @Override
            public void onDeleted(Object obj) {
                TransportDealObj transportDealObj = (TransportDealObj) obj;
                deleteOneTrip(transportDealObj);
            }

            @Override
            public void onCancel() {
            }
        });
        mRcl.setAdapter(mAdapter);

        // Should call this method at the end of declaring UI
        getTripHistories(true);
    }

    @Override
    void initControl() {
        mSwTrip.setOnRefreshListener(this);

        mLblDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTripHistories.size() > 0) {
                    deleteAllTrip();
                } else {
                    Toast.makeText(self, R.string.msg_have_no_trip_to_delete, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getTripHistories(boolean showProgress) {
        if (NetworkUtility.isNetworkAvailable()) {
            ModelManager.getTripHistoryList(self, page, TransportDealObj.HISTORY, showProgress, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject jsonObject = (JSONObject) object;

                    if (JSONParser.responseIsSuccess(jsonObject)) {
                        ArrayList<TransportDealObj> arrTemp = JSONParser.parseTrips(jsonObject);
                        if (arrTemp.size() > 0) {
                            mTripHistories.addAll(arrTemp);
                            mAdapter.notifyDataSetChanged();
                        }

                        showNoDataLayout();

                        onScrollListener.onLoadMoreComplete();
                        onScrollListener.setEnded(page >= JSONParser.getTotalPage(jsonObject));
                    } else {
                        Toast.makeText(self, JSONParser.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                    }

                    mSwTrip.setRefreshing(false);
                }

                @Override
                public void onError() {
                    mSwTrip.setRefreshing(false);

                    Toast.makeText(self, R.string.msg_have_some_errors, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            mSwTrip.setRefreshing(false);

            Toast.makeText(self, R.string.msg_no_network, Toast.LENGTH_SHORT).show();
        }
    }

    private void showNoDataLayout() {
        if (mTripHistories != null && mTripHistories.size() > 0) {
            mLlNoData.setVisibility(View.GONE);
        } else {
            mLlNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadMore(int page) {
        if (!onScrollListener.isEnded()) {
            this.page = page;
            getTripHistories(false);
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        mTripHistories.clear();
        onScrollListener.setEnded(false);
        onScrollListener.setCurrentPage(page);
        getTripHistories(false);
    }

    private void deleteOneTrip(final TransportDealObj obj) {
        if (NetworkUtility.isNetworkAvailable()) {
            ModelManager.manualTrip(self, TransportDealObj.ACTION_DELETE, obj, "", "", 0, "", new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject jsonObject = (JSONObject) object;
                    if (JSONParser.responseIsSuccess(jsonObject)) {
                        for (int i = 0; i < mTripHistories.size(); i++) {
                            if (obj.getId().equals(mTripHistories.get(i).getId())) {
                                mTripHistories.remove(i);
                                mAdapter.notifyDataSetChanged();

                                showNoDataLayout();

                                break;
                            }
                        }
                    } else {
                        Toast.makeText(self, JSONParser.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError() {
                }
            });
        } else {
            Toast.makeText(self, R.string.msg_no_network, Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteAllTrip() {
        if (NetworkUtility.isNetworkAvailable()) {
            GlobalFunctions.showConfirmationDialog(self, getString(R.string.msg_confirm_deleting_all),
                    getString(R.string.delete), getString(R.string.no_thank), true, new IConfirmation() {
                        @Override
                        public void onPositive() {
                            ModelManager.deleteAllTrip(self, new ModelManagerListener() {
                                @Override
                                public void onSuccess(Object object) {
                                    JSONObject jsonObject = (JSONObject) object;
                                    if (JSONParser.responseIsSuccess(jsonObject)) {
                                        mTripHistories.clear();
                                        mAdapter.notifyDataSetChanged();

                                        showNoDataLayout();
                                    } else {
                                        Toast.makeText(self, JSONParser.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onError() {
                                }
                            });
                        }

                        @Override
                        public void onNegative() {
                        }
                    });
        } else {
            Toast.makeText(self, R.string.msg_no_network, Toast.LENGTH_SHORT).show();
        }
    }
}

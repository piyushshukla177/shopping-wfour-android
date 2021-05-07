package com.wfour.onlinestoreapp.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.interfaces.MyOnClickOrderHistory;
import com.wfour.onlinestoreapp.network.ApiResponse;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.OrderObj;
import com.wfour.onlinestoreapp.utils.NetworkUtility;
import com.wfour.onlinestoreapp.view.activities.BillDetailActivity;
import com.wfour.onlinestoreapp.view.activities.MainActivity;
import com.wfour.onlinestoreapp.view.adapters.BillAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

public class TabAllFragment extends Fragment {

    public static TabAllFragment ttt = null;
    private RecyclerView contentRcl;
    private BillAdapter mAdapter;
    public static final int REQUEST_CODE = 100;
    private MainActivity mMainActivity;

    public static ArrayList<OrderObj> orderObjList = new ArrayList<>();
    public static ArrayList<OrderObj> filter_list = new ArrayList<>();
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_tab, container, false);
        init(root);
        ttt = this;
        return root;
    }

    void init(View root) {
        mMainActivity = (MainActivity) getActivity();

        context = getActivity();
        contentRcl = root.findViewById(R.id.contentRcl);
//        initData();

        mAdapter = new BillAdapter(mMainActivity, orderObjList, 0, new MyOnClickOrderHistory() {
            @Override
            public void onClick(OrderObj orderObj, int position) {
                Intent intent = new Intent(context, BillDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Args.KEY_PRODUCT_OBJECT, orderObj);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        mAdapter.notifyDataSetChanged();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        contentRcl.setLayoutManager(layoutManager);
        contentRcl.setAdapter(mAdapter);

    }

    private void initData() {
        orderObjList.clear();
        filter_list.clear();
        if (NetworkUtility.isNetworkAvailable()) {
            String id = DataStoreManager.getUser().getId();

            ModelManager.productBill(context, id, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject obj = (JSONObject) object;
                    ApiResponse response = new ApiResponse(obj);
                    if (!response.isError()) {
                        orderObjList.addAll(response.getDataList(OrderObj.class));
                        filter_list = orderObjList;
                        mAdapter.addList(filter_list);
                    }
                }

                @Override
                public void onError() {

                }
            });
        } else {
            Toast.makeText(context, getString(R.string.msg_no_network), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
//                initData();
            }
        }
    }

    public void setAdapter(int index) {
        OrderObj obj;
        filter_list.clear();
        int i = 0;
        while (i < orderObjList.size()) {
            if (index == 0) {
                return;
            } else if (index == 1) {
                obj = orderObjList.get(i);
                if (obj.getStatus().equals("0")) {
                    filter_list.add(obj);
                }
            } else if (index == 2) {
                obj = orderObjList.get(i);
                if (obj.getStatus().equals("3")) {
                    filter_list.add(obj);
                }
            } else if (index == 3) {
                obj = orderObjList.get(i);
                if (obj.getStatus().equals("5")) {
                    filter_list.add(obj);
                }
            } else if (index == 4) {
                obj = orderObjList.get(i);
                if (obj.getStatus().equals("2")) {
                    filter_list.add(obj);
                }
            }
            i++;
        }
        mAdapter = new BillAdapter(mMainActivity, filter_list, index, new MyOnClickOrderHistory() {
            @Override
            public void onClick(OrderObj orderObj, int position) {
                Intent intent = new Intent(context, BillDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Args.KEY_PRODUCT_OBJECT, orderObj);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        mAdapter.addList(filter_list);
        mAdapter.notifyDataSetChanged();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        contentRcl.setLayoutManager(layoutManager);
        contentRcl.setAdapter(mAdapter);
    }
}
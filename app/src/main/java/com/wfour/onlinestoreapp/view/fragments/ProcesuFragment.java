package com.wfour.onlinestoreapp.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.interfaces.MyOnClickOrderHistory;
import com.wfour.onlinestoreapp.objects.OrderObj;
import com.wfour.onlinestoreapp.view.activities.BillDetailActivity;
import com.wfour.onlinestoreapp.view.activities.MainActivity;
import com.wfour.onlinestoreapp.view.adapters.BillAdapter;

import java.util.ArrayList;

public class ProcesuFragment extends Fragment {

    private RecyclerView contentRcl;
    private BillAdapter mAdapter;
    public static final int REQUEST_CODE = 100;
    private MainActivity mMainActivity;
    Context context;
    public  ArrayList<OrderObj> filter_list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_procesu, container, false);
        init(root);
        return root;
    }

    void init(View root)
    {
        mMainActivity = (MainActivity) getActivity();
        context = getActivity();
        contentRcl = root.findViewById(R.id.contentRcl);
        setAdapter();
    }

        public void setAdapter() {
        OrderObj obj;
        filter_list.clear();
        int i = 0;
        while (i < BillManagementFragment.orderObjList.size()) {

                obj = BillManagementFragment.orderObjList.get(i);
                if (obj.getStatus().equals("0")) {
                    filter_list.add(obj);
                }

//            else if (index == 2) {
//                obj = BillManagementFragment.orderObjList.get(i);
//                if (obj.getStatus().equals("3")) {
//                    filter_list.add(obj);
//                }
//            }
//
//            else if (index == 4) {
//                obj = BillManagementFragment.orderObjList.get(i);
//                if (obj.getStatus().equals("5")) {
//                    filter_list.add(obj);
//                }
//            } else if (index == 5) {
//                obj = BillManagementFragment.orderObjList.get(i);
//                if (obj.getStatus().equals("2")) {
//                    filter_list.add(obj);
//                }
//            } else if (index == 3) {
//                obj = BillManagementFragment.orderObjList.get(i);
//                if (obj.getPaymentMethod().equals("point")) {
//                    filter_list.add(obj);
//                }
//
//            }
            i++;
        }
        mAdapter = new BillAdapter(mMainActivity, filter_list, new MyOnClickOrderHistory() {
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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        contentRcl.setLayoutManager(layoutManager);
        contentRcl.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        Log.e("filter_list_size",String.valueOf(filter_list.size()));
    }
}
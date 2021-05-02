package com.wfour.onlinestoreapp.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.BaseFragment;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.MyOnClickOrderHistory;
import com.wfour.onlinestoreapp.network.ApiResponse;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.CartObj;
import com.wfour.onlinestoreapp.objects.OrderObj;
import com.wfour.onlinestoreapp.utils.NetworkUtility;
import com.wfour.onlinestoreapp.view.activities.BillDetailActivity;
import com.wfour.onlinestoreapp.view.activities.CartActivity;
import com.wfour.onlinestoreapp.view.activities.MainActivity;
import com.wfour.onlinestoreapp.view.adapters.BillAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

public class BillManagementFragment extends BaseFragment {

    public static final int REQUEST_CODE = 100;
    public static final String ORDER_ID = "order_id";
    private RecyclerView contentRcl;
    private BillAdapter mAdapter;

    private ArrayList<OrderObj> orderObjList;

    private MainActivity mMainActivity;
    private int count;
    private ArrayList<CartObj> cartList;


    public static BillManagementFragment newInstance() {
        Bundle bundle = new Bundle();
        BillManagementFragment fragment = new BillManagementFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_bill_management;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        setHasOptionsMenu(true);
        mMainActivity = (MainActivity) getActivity();
        contentRcl = view.findViewById(R.id.contentRcl);

        mAdapter = new BillAdapter(mMainActivity, orderObjList, new MyOnClickOrderHistory() {
            @Override
            public void onClick(OrderObj orderObj, int position) {
                Intent intent = new Intent(self, BillDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Args.KEY_PRODUCT_OBJECT, orderObj);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE);


            }
        });
        mAdapter.notifyDataSetChanged();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mMainActivity);
        contentRcl.setLayoutManager(layoutManager);
        contentRcl.setAdapter(mAdapter);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                initData();
            }
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cart, menu);
        MenuItem menuItem = menu.findItem(R.id.action_cart);
        menuItem.setIcon(GlobalFunctions.buildCounterDrawable(self,count, R.drawable.ic_cart));

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
    public void onResume() {
        super.onResume();
        GlobalFunctions.getCountCart(cartList,count);
        count = DataStoreManager.getCountCart();
        getActivity().invalidateOptionsMenu();
    }

    private void initData() {

        if (NetworkUtility.isNetworkAvailable()) {
            String id = DataStoreManager.getUser().getId();

            ModelManager.productBill(self, id, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject obj = (JSONObject) object;
                    ApiResponse response = new ApiResponse(obj);

                    orderObjList = new ArrayList<>();
                    if (!response.isError()) {
                        orderObjList.addAll(response.getDataList(OrderObj.class));
                        mAdapter.addList(orderObjList);
                    }
                }

                @Override
                public void onError() {

                }
            });
        } else {
            Toast.makeText(self, getString(R.string.msg_no_network), Toast.LENGTH_SHORT).show();

        }


    }


    @Override
    protected void getData() {
        initData();
    }
}

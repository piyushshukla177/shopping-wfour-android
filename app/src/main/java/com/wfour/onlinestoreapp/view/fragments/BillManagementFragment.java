package com.wfour.onlinestoreapp.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.BaseFragment;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.network.ApiResponse;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.CartObj;
import com.wfour.onlinestoreapp.objects.OrderObj;
import com.wfour.onlinestoreapp.utils.NetworkUtility;
import com.wfour.onlinestoreapp.view.activities.CartActivity;
import com.wfour.onlinestoreapp.view.activities.MainActivity;
import com.wfour.onlinestoreapp.view.adapters.BillAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BillManagementFragment extends BaseFragment {

    public static final int REQUEST_CODE = 100;
    public static final String ORDER_ID = "order_id";
    //    private RecyclerView contentRcl;
    private BillAdapter mAdapter;

    private ArrayList<OrderObj> orderObjList=new ArrayList<>();

    private MainActivity mMainActivity;
    private int count;
    private ArrayList<CartObj> cartList;
    ViewPager viewPager;
    Toolbar toolbar;
    TabLayout tabLayout;

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

    private MainActivity mCartActivity;

    @Override
    protected void initView(View view) {
        mCartActivity = (MainActivity) getActivity();
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(getResources().getColor(R.color.white), getResources().getColor(R.color.white));

        setHasOptionsMenu(true);
        mMainActivity = (MainActivity) getActivity();
//      contentRcl = view.findViewById(R.id.contentRcl);
        toolbar = view.findViewById(R.id.toolbar);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        mCartActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().setTitle("Bill detalle");
//        mAdapter = new BillAdapter(mMainActivity, orderObjList, new MyOnClickOrderHistory() {
//            @Override
//            public void onClick(OrderObj orderObj, int position) {
//                Intent intent = new Intent(self, BillDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable(Args.KEY_PRODUCT_OBJECT, orderObj);
//                intent.putExtras(bundle);
//                startActivityForResult(intent, REQUEST_CODE);
//            }
//        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {

                    TabAllFragment.ttt.setAdapter(0);
                } else if (tab.getPosition() == 1) {

                    TabAllFragment.ttt.setAdapter(1);
                } else if (tab.getPosition() == 2) {

                    TabAllFragment.ttt.setAdapter(2);
                } else if (tab.getPosition() == 3) {

                    TabAllFragment.ttt.setAdapter(3);
                } else if (tab.getPosition() == 4) {

                    TabAllFragment.ttt.setAdapter(4);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        initData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
//                initData();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_cart, menu);
//        MenuItem menuItem = menu.findItem(R.id.action_cart);
//        menuItem.setIcon(GlobalFunctions.buildCounterDrawable(self, count, R.drawable.ic_cart));

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
        GlobalFunctions.getCountCart(cartList, count);
        count = DataStoreManager.getCountCart();
        getActivity().invalidateOptionsMenu();
    }

    private void initData() {
        orderObjList.clear();
        TabAllFragment.orderObjList.clear();
        TabAllFragment.filter_list.clear();
        if (NetworkUtility.isNetworkAvailable()) {
            String id = DataStoreManager.getUser().getId();

            ModelManager.productBill(self, id, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject obj = (JSONObject) object;
                    ApiResponse response = new ApiResponse(obj);
                    if (!response.isError()) {
                        orderObjList.addAll(response.getDataList(OrderObj.class));
                        TabAllFragment.orderObjList=orderObjList;
                        setupViewPager(viewPager);

                        //                        mAdapter.addList(orderObjList);
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
//        initData();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new TabAllFragment(), "All");
        adapter.addFragment(new TabAllFragment(), "Prosesu");
        adapter.addFragment(new TabAllFragment(), "Haruka Ona");
        adapter.addFragment(new TabAllFragment(), "Selu Ona");
        adapter.addFragment(new TabAllFragment(), "Kansela");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

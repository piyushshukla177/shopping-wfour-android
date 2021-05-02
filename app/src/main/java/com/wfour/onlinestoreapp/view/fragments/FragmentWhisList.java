package com.wfour.onlinestoreapp.view.fragments;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.BaseFragment;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IMyOnClick;
import com.wfour.onlinestoreapp.network.ApiResponse;
import com.wfour.onlinestoreapp.network.BaseRequest;
import com.wfour.onlinestoreapp.network.modelmanager.RequestManger;
import com.wfour.onlinestoreapp.network.modelmanager.singletonmanager.CartManager;
import com.wfour.onlinestoreapp.network1.NetworkUtility;
import com.wfour.onlinestoreapp.objects.CartObj;
import com.wfour.onlinestoreapp.objects.ProductObj;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.view.activities.CartActivity;
import com.wfour.onlinestoreapp.view.activities.DealDetailActivity;
import com.wfour.onlinestoreapp.view.adapters.AdapterWhistList;
import com.wfour.onlinestoreapp.widgets.onscroll.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;

public class FragmentWhisList extends BaseFragment implements EndlessRecyclerOnScrollListener.OnLoadMoreListener, View.OnClickListener {
    private static final String TAG = FragmentWhisList.class.getSimpleName();
    private RecyclerView rcvData;
    private AdapterWhistList adapter;
    private ArrayList<ProductObj> list;
    private EndlessRecyclerOnScrollListener onScrollListener;
    private int page = 1;
    private TextView tvBuy;
    private ArrayList<CartObj> cartObjList;
    private int count;
    private CartObj cart;
    private String color,size;

    public static FragmentWhisList newInstance() {
        Bundle args = new Bundle();
        FragmentWhisList fragment = new FragmentWhisList();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_whislist;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        tvBuy = view.findViewById(R.id.btnBuy);
        tvBuy.setVisibility(View.GONE);
        rcvData = view.findViewById(R.id.rcv_data);
        list = new ArrayList<>();
        getWhistList();
        adapter = new AdapterWhistList(getActivity(), list, new IMyOnClick() {
            @Override
            public void MyOnClick(int position, ProductObj productObj) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Args.KEY_PRODUCT_OBJECT, productObj);
                GlobalFunctions.startActivityWithoutAnimation(self, DealDetailActivity.class, bundle);
            }
        });
        adapter.setListener(new AdapterWhistList.OnclickRemoveItemWhistList() {
            @Override
            public void OnItemClicked(View view, int position) {
                removeData(list.get(position).getId(), position);
            }
        });
        GridLayoutManager manager = new GridLayoutManager(self, 1);
        rcvData.setLayoutManager(manager);
        rcvData.setAdapter(adapter);
        rcvData.hasFixedSize();
        onScrollListener = new EndlessRecyclerOnScrollListener(this);
        rcvData.addOnScrollListener(onScrollListener);
        onScrollListener.setEnded(false);
        tvBuy.setOnClickListener(this);

        setAppBar(view);
    }

    public void setAppBar(View view){
        MaterialToolbar toolbars = getActivity().findViewById(R.id.toolbar);
        ShapeableImageView logoAppBar = getActivity().findViewById(R.id.logo_appbar);
        ShapeableImageView btnSearchBar = getActivity().findViewById(R.id.mis_action_search);
        ShapeableImageView btnNotifyBar = getActivity().findViewById(R.id.mis_action_notification);
        MaterialTextView titleBar = getActivity().findViewById(R.id.tv_title);
        titleBar.setText("Favoritu");
        logoAppBar.setVisibility(View.GONE);
        btnSearchBar.setVisibility(View.GONE);
        btnNotifyBar.setVisibility(View.GONE);
        toolbars.setVisibility(View.VISIBLE);
    }

    @Override
    protected void getData() {

    }

    private void getWhistList() {
        if (NetworkUtility.getInstance(self).isNetworkAvailable()) {
            RequestManger.getListFavorite(DataStoreManager.getUser().getId(), "deal", page, 10, new BaseRequest.CompleteListener() {
                @Override
                public void onSuccess(ApiResponse response) {
                    if (!response.isError()) {
                        list.addAll(response.getDataList(ProductObj.class));
                        adapter.addList(list);
                        onScrollListener.onLoadMoreComplete();
                        onScrollListener.setEnded(ApiResponse.isEnded(response, page));
                    }
                }

                @Override
                public void onError(String message) {
                    Log.e(TAG, "onError: " + message);
                }
            });
        }else {
            AppUtil.showToast(self, R.string.no_connection);
        }
    }

    @Override
    public void onLoadMore(int page) {
        this.page = page;
        getData();
        Log.e(TAG, "onLoadMore: " + page);
    }

    private void removeData(String object_id, final int position) {
        if (NetworkUtility.getInstance(self).isNetworkAvailable()) {
            RequestManger.removeFavorite(DataStoreManager.getUser().getId(), object_id, "deal", new BaseRequest.CompleteListener() {
                @Override
                public void onSuccess(ApiResponse response) {
                    AppUtil.showToast(self, getString(R.string.remove_success));
                    list.remove(position);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(String message) {
                    Log.e(TAG, "onError: " + message);
                }
            });
        } else {
            AppUtil.showToast(self, R.string.no_connection);
        }

    }

    @Override
    public void onClick(View view) {
        if (list.size() > 0) {
            for (ProductObj item : list) {
                setOrder(item);
            }
            GlobalFunctions.startActivityWithoutAnimation(self, CartActivity.class);
        }
    }

    private void setOrder(ProductObj item) {

        if(item.getColors().size()>0){
             color = item.getColors().get(0).getColor();
        }
       if(item.getSizes().size()>0){
           size = item.getSizes().get(0).getSize();
       }

        cartObjList = CartManager.getInstance().getArray();
        if (cartObjList.size() != 0) {
            boolean isExist = false;
            String id = item.getId();

            for (int i = 0; i < cartObjList.size(); i++) {
                cart = cartObjList.get(i);
                if (color != null && size != null) {
                    if (cart.getId().equals(id) && cart.getColor().equals(color) && cart.getSize().equals(size)) {
                        isExist = true;
                        break;
                    }
                } else if (color != null && size == null) {
                    if (cart.getId().equals(id) && cart.getColor().equals(color)) {
                        isExist = true;
                        break;
                    }
                } else if (color == null && size != null) {
                    if (cart.getId().equals(id) && cart.getSize().equals(size)) {
                        isExist = true;
                        break;
                    }
                } else {
                    if (cart.getId().equals(id)) {
                        isExist = true;
                        break;
                    }
                }
            }
            if (isExist) {
                int number = cart.getNumber();
                number += 1;
                cart.setNumber(number);

                double money = cart.getTotalMoney();
                money = number * cart.getPrice();
                cart.setTotalMoney(money);
            } else {
                CartManager.getInstance().addItem(new CartObj(item.getId(), item.getTitle(), item.getPrice(), item.getImage(), 1, item.getPrice(), color, size, item.getOld_price(), item.getIs_prize()));

            }
        } else {
            CartManager.getInstance().addItem(new CartObj(item.getId(), item.getTitle(), item.getPrice(), item.getImage(), 1, item.getPrice(), color, size, item.getOld_price(), item.getIs_prize()));


        }

        doIncrease();

    }

    private void doIncrease() {
        count = DataStoreManager.getCountCart();
        count++;
        //getContext().invalidateOptionsMenu();
        DataStoreManager.saveCountCart(count);
    }
}

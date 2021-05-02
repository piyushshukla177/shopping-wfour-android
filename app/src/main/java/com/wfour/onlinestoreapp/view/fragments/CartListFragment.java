package com.wfour.onlinestoreapp.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.MyInterface;
import com.wfour.onlinestoreapp.network.modelmanager.singletonmanager.CartManager;
import com.wfour.onlinestoreapp.objects.CartObj;
import com.wfour.onlinestoreapp.objects.DealObj;
import com.wfour.onlinestoreapp.objects.ProductObj;
import com.wfour.onlinestoreapp.objects.RecomendedObj;
import com.wfour.onlinestoreapp.objects.UserObj;
import com.wfour.onlinestoreapp.quickblox.conversation.utils.DialogUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.view.activities.CartActivity;
import com.wfour.onlinestoreapp.view.activities.DealDetailActivity;
import com.wfour.onlinestoreapp.view.activities.MainActivity;
import com.wfour.onlinestoreapp.view.activities.SplashLoginActivity;
import com.wfour.onlinestoreapp.view.adapters.CartAdapter;
import com.wfour.onlinestoreapp.view.adapters.RecomendedListAdapter;

import java.util.ArrayList;

public class CartListFragment extends com.wfour.onlinestoreapp.base.BaseFragment implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView tvMoney, shop_now_textview;
    private AppCompatButton btnOrder;
    private DealObj item;
    private static final int RC_ACTIVATE_DEAL = 1;
    private Bundle bundle;
    //  private ArrayList<ProductObj> cartObjList;
    private ArrayList<CartObj> cartObjList;
    private RecyclerView contentRcl;
    private CartAdapter pAdapter;

    public String title;
    public String price;
    public String icon;
    //  private MainActivity mCartActivity;
    private InformationOrderFragment mFrgOrder;
    private DealDetailActivity mDealDetailActivity;
    private DeliveryAddressFragment mDeliveryAddressFragment;
    private DealAboutFragment dealAboutFragment;
    private double totalMoney;
    private String color, size;
    private LinearLayout llNoData;

    private ArrayList<RecomendedObj> recomendedlist = new ArrayList<>();
    RecyclerView recomended_recyclerview;
    private RecomendedListAdapter recomendedAdapter;
    public static FragmentTransaction fragmentTransaction;
    public static HomeFragment mHomeFragment;

    CardView total_cardview;

    public static CartListFragment newInstance() {
        Bundle bundle = new Bundle();
        CartListFragment fragment = new CartListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_cart_list;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
//        mCartActivity = (MainActivity) getActivity();
        setHasOptionsMenu(true);

//        toolbar.setTitle("Karosa Kompra");
        MaterialToolbar toolbars = getActivity().findViewById(R.id.toolbar);
        if (toolbars != null) {
            initRemoveActionBar(view);
        }
        llNoData = (LinearLayout) view.findViewById(R.id.ll_no_data);
        total_cardview = view.findViewById(R.id.total_cardview);
        tvMoney = view.findViewById(R.id.tvMoney);
        btnOrder = view.findViewById(R.id.btnOrder);
        shop_now_textview = view.findViewById(R.id.shop_now_textview);
        recomended_recyclerview = view.findViewById(R.id.recomended_recyclerview);
        btnOrder.setOnClickListener(this);
        shop_now_textview.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        if (mHomeFragment == null) {
                            mHomeFragment = HomeFragment.newInstance(Args.TYPE_OF_CATEGORY_ALLS);
                        }
                        fragmentTransaction.replace(R.id.frl_main, mHomeFragment).commit();
                        BottomNavigationView mBottomNavigationView = getActivity().findViewById(R.id.nav_main);

                        mBottomNavigationView.setSelectedItemId(R.id.home_menu);
//                        Intent intent = new Intent(self, MainActivity.class);
//                        startActivity(intent);
//                        getActivity().finish();
                    }
                }
        );
        initData();
        contentRcl = view.findViewById(R.id.rclView);
        pAdapter = new CartAdapter(getActivity(), cartObjList, new MyInterface() {
            @Override
            public void onClick(int position, ProductObj productObj) {
            }

            @Override
            public void deleteItem(int position) {
                if (cartObjList.size() != 0) {
                    removeItem(position);
                    totalMoney = CartManager.getInstance().getTotal();
                    tvMoney.setText(totalMoney + "");
                }
            }

            @Override
            public void onInCrease(double money, int position) {
                if (cartObjList.size() != 0) {
                    cartObjList.get(position).setTotalMoney(money);
                    totalMoney = CartManager.getInstance().getTotal();
                    //tvMoney.setText(String.format("%.3f", totalMoney));

                    tvMoney.setText(totalMoney + "0");
                }
            }

            @Override
            public void onDeCrease(double money, int position) {
                if (cartObjList.size() != 0) {
                    cartObjList.get(position).setTotalMoney(money);
                    totalMoney = CartManager.getInstance().getTotal();
                    tvMoney.setText(StringUtil.convertNumberToString(totalMoney, 2));
                }

            }
        });

//        setAppBar(view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        contentRcl.setLayoutManager(layoutManager);
        contentRcl.setAdapter(pAdapter);

        setRecomendedRecyclerview();
    }

    public void initRemoveActionBar(View view) {
        MaterialToolbar toolbars = getActivity().findViewById(R.id.toolbar);
        if (toolbars != null) {
            toolbars.setVisibility(View.GONE);
        }
    }

    @Override
    protected void getData() {

    }

    private void initData() {
        cartObjList = CartManager.getInstance().getArray();
        totalMoney = CartManager.getInstance().getTotal();
        tvMoney.setText(StringUtil.convertNumberToString(totalMoney, 2));
        checkViewHideShow();
    }

    public void removeItem(int position) {
        CartManager.getInstance().removeItem(position);
        cartObjList = CartManager.getInstance().getArray();
        pAdapter.notifyItemRemoved(position);
        pAdapter.notifyDataSetChanged();
    }

    private void showDialogLogout() {
        DialogUtil.showAlertDialog(self, R.string.notification, R.string.you_need_login, new DialogUtil.IDialogConfirm() {
            @Override
            public void onClickOk() {
                goLogin();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_go_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.action_go_home:
                MainActivity.nav_main.getMenu().findItem(R.id.home_menu).setChecked(true);
                GlobalFunctions.startActivityWithoutAnimation(self, MainActivity.class);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void goLogin() {
        Intent intent = new Intent(self, SplashLoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        if (view == btnOrder) {
            if (cartObjList.size() != 0) {
                UserObj userObj = DataStoreManager.getUser();
                if (userObj != null) {
                    if (CartManager.getInstance().isPay()) {
                        Toast.makeText(getActivity(), "It is impossible to pay at the same time two types of products:point and money", Toast.LENGTH_SHORT).show();
                    } else {
                        if (DataStoreManager.getUser().getPoint() >= CartManager.getInstance().getTotalPoint()) {
                            if (mDeliveryAddressFragment == null) {
                                mDeliveryAddressFragment = DeliveryAddressFragment.newInstance();
                            }
                            fragmentTransaction.replace(R.id.frgCart, mDeliveryAddressFragment).addToBackStack("DeliveryAddressFragment").commit();

                        } else {
                            Toast.makeText(getActivity(), "Your point is not enough!", Toast.LENGTH_SHORT).show();
                        }

                    }

                } else {
                    showDialogLogout();
                }
            } else {
                Toast.makeText(self, "No Products", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void checkViewHideShow() {
        if (cartObjList.isEmpty()) {
            total_cardview.setVisibility(View.GONE);
            llNoData.setVisibility(View.VISIBLE);
        } else {
            total_cardview.setVisibility(View.VISIBLE);
            llNoData.setVisibility(View.GONE);
        }
    }

    void setRecomendedRecyclerview() {
        RecomendedObj obj;
        int i = 0;
        while (i < 4) {
            obj = new RecomendedObj();
            obj.setProduct_Name("Mie Sedaap Goreng");
            obj.setDescription("Instant 5pcs");
            obj.setActual_rate("$10");
            obj.setDiscount_rate("$14");
            recomendedlist.add(obj);
            i++;

        }
        recomended_recyclerview.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        linearLayout.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recomendLayoutManager = new LinearLayoutManager(getActivity());
        recomendedAdapter = new RecomendedListAdapter(getActivity(), recomendedlist);
        recomended_recyclerview.setLayoutManager(linearLayout);
        recomended_recyclerview.setAdapter(recomendedAdapter);

    }

//    public void setAppBar(View view) {
//        MaterialToolbar toolbars = getActivity().findViewById(R.id.toolbar);
//        ShapeableImageView logoAppBar = getActivity().findViewById(R.id.logo_appbar);
//        ShapeableImageView btnSearchBar = getActivity().findViewById(R.id.mis_action_search);
//        ShapeableImageView btnNotifyBar = getActivity().findViewById(R.id.mis_action_notification);
//        MaterialTextView titleBar = getActivity().findViewById(R.id.tv_title);
//        titleBar.setText("Karosa Kompra");
//        logoAppBar.setVisibility(View.GONE);
//        btnSearchBar.setVisibility(View.GONE);
//        btnNotifyBar.setVisibility(View.GONE);
//        toolbars.setVisibility(View.VISIBLE);
//    }
}

package com.wfour.onlinestoreapp.view.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.wfour.onlinestoreapp.AppController;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.globals.ActionReceiver;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.DealObj;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.NetworkUtility;
import com.wfour.onlinestoreapp.view.activities.AllCategoryActivity;
import com.wfour.onlinestoreapp.view.activities.CartActivity;
import com.wfour.onlinestoreapp.view.activities.DealsActivity;
import com.wfour.onlinestoreapp.view.activities.SearchActivity;
import com.wfour.onlinestoreapp.view.activities.TransportActivity;
import com.wfour.onlinestoreapp.view.adapters.AdapterViewPaggerCategory;
import com.wfour.onlinestoreapp.view.adapters.DealCateAdapter;
import com.wfour.onlinestoreapp.base.BaseActivity;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IOnItemClickListener;
import com.wfour.onlinestoreapp.objects.DealCateObj;
import com.wfour.onlinestoreapp.utils.map.MapsUtil;

import org.json.JSONObject;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllDealsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllDealsFragment extends BaseFragment implements IOnItemClickListener, View.OnClickListener {
    private static final int RC_PERMISSIONS = 1;
    private static final int RC_TURN_ON_LOCATION = 2;
    private static final String TAG = AllDealsFragment.class.getSimpleName();


    private int widthScreen;
    /*view pager*/
    private ViewPager mViewPager;
    private CircleIndicator mIndicator;
    private AdapterViewPaggerCategory adapterIndicator;
    private ArrayList<DealObj> mDealFavorites;
    private View screenFavorite;

    /*list custom cate*/
    private View itemLabor, itemHotelTraveling, itemShoppingBeauty, itemNewEvent, itemOtherDeal, itemFood;
    private ArrayList<View> items;
    private int heightItem;

    /*btn*/
    private View btnSellDeal;
    private RecyclerView mRclCate;
    private DealCateAdapter mAdapter;
    private ArrayList<DealCateObj> mDatas;

    private int curPositionClicked;
    private int typeOfCategory; // 0 is short. 1 is full category
    private FavoriteReceiver favoriteReceiver;

    public static AllDealsFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt(Args.TYPE_OF_CATEGORY, type);
        AllDealsFragment fragment = new AllDealsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("AllDealsFragment", "onCreate");
        // Show option menu
        setHasOptionsMenu(true);
        typeOfCategory = getArguments().getInt(Args.TYPE_OF_CATEGORY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initData();
        return view;
    }

    @Override
    View inflateLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_list, container, false);
    }

    @Override
    void initUI(View view) {
        widthScreen = AppUtil.getScreenWidth(getActivity());

        initViewPager(view);

        initListCustomCate(view);

        initListCate(view);

        initButtonSell(view);


    }

    private void initButtonSell(View view) {
        btnSellDeal = view.findViewById(R.id.btn_sell_deal);
        btnSellDeal.setOnClickListener(this);
    }

    private void initListCustomCate(View view) {
        itemLabor = view.findViewById(R.id.item_labor);
        itemHotelTraveling = view.findViewById(R.id.item_hotel_travel);
        itemShoppingBeauty = view.findViewById(R.id.item_shopping_beauty);
        itemNewEvent = view.findViewById(R.id.item_new_event);
        itemOtherDeal = view.findViewById(R.id.item_other_deal);
        itemFood = view.findViewById(R.id.item_food);

        items = new ArrayList<>();
        items.add(0, itemLabor);
        items.add(1, itemHotelTraveling);
        items.add(2, itemShoppingBeauty);
        items.add(3, itemNewEvent);
        items.add(4, itemFood);
        items.add(5, itemOtherDeal);

        heightItem = (int) (widthScreen * 0.33);

        for (int i = 0; i < items.size(); i++) {
            items.get(i).setOnClickListener(this);
            items.get(i).getLayoutParams().height = heightItem;
        }


    }

    private void initListCate(View view) {
        mRclCate = (RecyclerView) view.findViewById(R.id.rcv_data);
        mRclCate.setHasFixedSize(true);
        mRclCate.setLayoutManager(new LinearLayoutManager(self, LinearLayoutManager.VERTICAL, false));
    }

    private void initViewPager(View view) {
        screenFavorite = view.findViewById(R.id.screen_favorite);
        mViewPager = view.findViewById(R.id.view_pager);
        mIndicator = view.findViewById(R.id.circle_indicator);
//        mDealFavorites = new ArrayList<>();
//        adapterIndicator = new AdapterViewPaggerCategory(getChildFragmentManager(), mDealFavorites);
        setPageAdapter();

        screenFavorite.getLayoutParams().height = (int) (widthScreen * 0.47);
    }

    private void setPageAdapter() {
        mViewPager.setAdapter(adapterIndicator);
        mIndicator.setViewPager(mViewPager);
        adapterIndicator.registerDataSetObserver(mIndicator.getDataSetObserver());
    }


    @Override
    void initControl() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        inflater.inflate(R.menu.menu_cart, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.home);
        favoriteReceiver = new FavoriteReceiver();
        IntentFilter filterFavorite = new IntentFilter();
        filterFavorite.addAction(ActionReceiver.ACTION_FAVORITE);
        getActivity().registerReceiver(favoriteReceiver, filterFavorite);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            GlobalFunctions.startActivityWithoutAnimation(self, SearchActivity.class);
            return true;
        }
        if (item.getItemId() == R.id.action_cart) {
            GlobalFunctions.startActivityWithoutAnimation(self, CartActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {

        initDataFavorite();

        initDataCategory();

    }

    private void initDataFavorite() {
        if (NetworkUtility.isNetworkAvailable()) {
            ModelManager.getDealList(self, null, null, "", Constants.SEARCH_FAVORIES, null, null, AppController.getInstance().getLatMyLocation(),
                    AppController.getInstance().getLongMyLocation(), null, null, 1, new ModelManagerListener() {
                        @Override
                        public void onSuccess(Object object) {
                            Log.e(TAG, "onSuccess " + object.toString());
                            JSONObject jsonObject = (JSONObject) object;
                            ApiResponse response = new ApiResponse(jsonObject);
                            mDealFavorites.clear();
                            if (!response.isError()) {
                                Log.e(TAG, "onSuccess response success");
                                mDealFavorites.addAll(response.getDataList(DealObj.class));
                                adapterIndicator.notifyDataSetChanged();
                                adapterIndicator.unregisterDataSetObserver(mIndicator.getDataSetObserver());
                                setPageAdapter();
                            }
                        }

                        @Override
                        public void onError() {
                        }
                    });
        } else {
            AppUtil.showToast(self, R.string.msg_network_not_available);
        }
    }

    private void initDataCategory() {
        mDatas = new ArrayList<>();
        mDatas.addAll(AppController.getInstance().getDealCategories());
        mAdapter = new DealCateAdapter(getActivity(), mDatas, this);
        GridLayoutManager manager = new GridLayoutManager(self, Constants.COLUM_GRID);
        mRclCate.setLayoutManager(manager);
        mRclCate.hasFixedSize();
        mRclCate.setAdapter(mAdapter);

        //showHideItem(typeOfCategory);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RC_PERMISSIONS: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED
                            || grantResults[1] == PackageManager.PERMISSION_DENIED) {
                        ((BaseActivity) self).showPermissionsReminder(RC_PERMISSIONS, true);
                    } else {
                        processOncliked(curPositionClicked);
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        // Check permissions
        DealCateObj item = mDatas.get(position);
        curPositionClicked = position;
        if (typeOfCategory == Args.TYPE_OF_CATEGORY_SHORT) {
            Log.e("AllDealsFragment", "onItemClick typeOfCategory == Args.TYPE_OF_CATEGORY_SHORT");
            if (DealCateObj.OTHER.equals(item.getId())) {
                GlobalFunctions.startActivityWithoutAnimation(self, AllCategoryActivity.class);
                Log.e("AllDealsFragment", "onItemClick TYPE_OF_CATEGORY_SHORT start AllCategoryActivity.class");
                return;
            }

            if (DealCateObj.TRANSPORT.equals(item.getId())) {
                GlobalFunctions.startActivityWithoutAnimation(self, TransportActivity.class);
                Log.e("AllDealsFragment", "onItemClick TYPE_OF_CATEGORY_SHORT start TransportActivity.class");
            } else {
                if (GlobalFunctions.locationIsGranted(self, RC_PERMISSIONS, "")) {
                    processOncliked(position);
                }
            }
        } else {
            Log.e("AllDealsFragment", "onItemClick typeOfCategory =TYPE_OF_CATEGORY_ALL");
            if (DealCateObj.TRANSPORT.equals(item.getId())) {
                GlobalFunctions.startActivityWithoutAnimation(self, TransportActivity.class);
                Log.e("AllDealsFragment", "onItemClick TYPE_OF_CATEGORY_ALL start TransportActivity.class)");
            } else {
                if (GlobalFunctions.locationIsGranted(self, RC_PERMISSIONS, "")) {
                    processOncliked(position);
                }
            }
        }
    }

    private void showHideItem(int typeOfCategory) {
        if (typeOfCategory == Args.TYPE_OF_CATEGORY_SHORT) {
//            mDatas.remove(7);
            mDatas.remove(6);
            mDatas.remove(5);
            mDatas.remove(4);
            mDatas.remove(3);
            DealCateObj dealCateObj = new DealCateObj(DealCateObj.OTHER, this.getString(R.string.all_deals), this.getString(R.string.des_other));
            mDatas.add(dealCateObj);
        } else {
            mDatas.clear(); // remove event&
            DealCateObj dealCateObj = new DealCateObj(DealCateObj.LABOR, this.getString(R.string.labor), R.drawable.bg_item_labor, this.getString(R.string.description_labor));
            mDatas.add(dealCateObj);
            dealCateObj = new DealCateObj(DealCateObj.TRAVEL, this.getString(R.string.travel_hotel), R.drawable.bg_item_hotel, this.getString(R.string.description_hotel));
            mDatas.add(dealCateObj);
            dealCateObj = new DealCateObj(DealCateObj.SHOPPING, this.getString(R.string.shopping), R.drawable.bg_item_shopping, this.getString(R.string.description_shopping));
            mDatas.add(dealCateObj);
            dealCateObj = new DealCateObj(DealCateObj.NEWS_AND_EVENTS, this.getString(R.string.news_and_events), R.drawable.bg_item_new_event, this.getString(R.string.description_beauty));
            mDatas.add(dealCateObj);
            dealCateObj = new DealCateObj(DealCateObj.OTHER, this.getString(R.string.other_deals), R.drawable.bg_item_orther, this.getString(R.string.description_orther));
            mDatas.add(dealCateObj);

            mAdapter.setHiddenDes(false);
            mAdapter.notifyDataSetChanged();
        }

    }

    private void processOncliked(int position) {
        if (MapsUtil.locationIsEnable(self) && AppController.getInstance().getMyLocation() != null) {
            DealCateObj item = mDatas.get(position);

            Bundle bundle = new Bundle();
            bundle.putString(Args.KEY_ID_DEAL_CATE, item.getId() + "");
            bundle.putString(Args.TYPE_OF_DEAL_NAME, item.getName() + "");
            if (item.getId().equals(DealCateObj.MY_FAVORITES)) {
                bundle.putString(Args.TYPE_OF_SEARCH_DEAL, Constants.SEARCH_FAVORIES);
            } else {
                bundle.putString(Args.TYPE_OF_SEARCH_DEAL, Constants.SEARCH_NEARBY);
            }


            GlobalFunctions.startActivityWithoutAnimation(self, DealsActivity.class, bundle);
        } else {
            MapsUtil.displayLocationSettingsRequest(self, RC_TURN_ON_LOCATION);
        }
    }

    private void onClickInItem(int p) {
        if (GlobalFunctions.locationIsGranted(self, RC_PERMISSIONS, "")) {
            processOncliked(p);
        }
    }


    @Override
    public void onClick(View view) {
        if (view == itemLabor) {
            onClickInItem(0);
        } else if (view == itemHotelTraveling) {
            onClickInItem(1);
        } else if (view == itemShoppingBeauty) {
            onClickInItem(2);
        } else if (view == itemNewEvent) {
            onClickInItem(3);
        } else if (view == itemOtherDeal) {
            onClickInItem(5);
        } else if (view == itemFood) {
            onClickInItem(4);
        } else if (view == btnSellDeal) {
            FragmentTransaction manager = getFragmentManager().beginTransaction();
            manager.replace(R.id.frl_main, ProducerManagerFragment.newInstance()).addToBackStack("sell_deal").commit();
            getActivity().setTitle(R.string.sell_deals);
            AppController.fromMainToSellFragment = false;
        }
    }

    public class FavoriteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String Action = intent.getAction();
            if (Action.equals(ActionReceiver.ACTION_FAVORITE)) {
                initDataFavorite();
            }
        }
    }

    ;
}
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.sendbird.android.GroupChannel;
import com.wfour.onlinestoreapp.AppController;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.ActionReceiver;
import com.wfour.onlinestoreapp.interfaces.IMyOnClick;
import com.wfour.onlinestoreapp.network.ApiResponse;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.BannerObj;
import com.wfour.onlinestoreapp.objects.CartObj;
import com.wfour.onlinestoreapp.objects.Category;
import com.wfour.onlinestoreapp.objects.DealObj;
import com.wfour.onlinestoreapp.objects.HomeObj;
import com.wfour.onlinestoreapp.objects.ProductObj;
import com.wfour.onlinestoreapp.objects.RecomendedObj;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.NetworkUtility;
import com.wfour.onlinestoreapp.view.activities.AllCategoryActivity;
import com.wfour.onlinestoreapp.view.activities.CartActivity;
import com.wfour.onlinestoreapp.view.activities.DealsActivity;
import com.wfour.onlinestoreapp.view.activities.MainActivity;
import com.wfour.onlinestoreapp.view.activities.SearchActivity2;
import com.wfour.onlinestoreapp.view.activities.TransportActivity;
import com.wfour.onlinestoreapp.view.adapters.AdapterViewPaggerCategory;
import com.wfour.onlinestoreapp.view.adapters.CategoryAdapter;
import com.wfour.onlinestoreapp.view.adapters.DealCateAdapter;
import com.wfour.onlinestoreapp.base.BaseActivity;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IOnItemClickListener;
import com.wfour.onlinestoreapp.objects.DealCateObj;
import com.wfour.onlinestoreapp.utils.map.MapsUtil;
import com.wfour.onlinestoreapp.view.adapters.ListAdapter;
import com.wfour.onlinestoreapp.view.adapters.RecomendedListAdapter;
import com.wfour.onlinestoreapp.view.chat.mainchat.LoginChatActivity;


import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllDealsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment implements IOnItemClickListener, View.OnClickListener {
    private static final int RC_PERMISSIONS = 1;
    private static final int RC_TURN_ON_LOCATION = 2;
    private static final String TAG = HomeFragment.class.getSimpleName();
    public static final int HOT_LIST = 0;
    public static final int NEW_LIST = 1;
    public static final int FEATURE_LIST = 2;
    public static final int CATEGORY = 3;
    private GroupChannel channel;


    private int widthScreen;
    private int heightScreen;
    /*view pager*/
    private ViewPager mViewPager;
    private CircleIndicator mIndicator;
    private AdapterViewPaggerCategory adapterIndicator;
    private ArrayList<DealObj> mDealFavorites;
    private ArrayList<ProductObj> productObjList;
    private View screenFavorite;
    private ProductObj productObj;

    /*btn*/
    private View btnSellDeal;
    private RecyclerView rcvData, rcvCategory;
    private DealCateAdapter mAdapter;
    private ArrayList<DealCateObj> mDatas;
    private CircleImageView imgMessager;

    private int curPositionClicked;
    private int typeOfCategory; // 0 is short. 1 is full category
    private FavoriteReceiver favoriteReceiver;

    //Category
    private ArrayList<Category> categoryList;
    private CategoryAdapter pAdapter;
    //Popular Deal
    private ListAdapter listAdapter;
    private HomeObj homeObj;
    private ArrayList<HomeObj> homeObjList;

    private int count;
    private ArrayList<CartObj> cartList;
    private MenuItem menuItem;
    private ProductObj item;
    public static ArrayList<BannerObj> bannerObjs;

    private ShapeableImageView search_bar;


    private MainActivity mMainActivity;


    private ArrayList<RecomendedObj> recomendedlist = new ArrayList<>();
    RecyclerView recomended_recyclerview;
    private RecomendedListAdapter recomendedAdapter;

    public static HomeFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt(Args.TYPE_OF_CATEGORY, type);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show option menu
        setHasOptionsMenu(true);
        typeOfCategory = getArguments().getInt(Args.TYPE_OF_CATEGORY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mMainActivity = (MainActivity) getActivity();
        recomended_recyclerview = view.findViewById(R.id.recomended_recyclerview);
        setRecomendedRecyclerview();

        return view;
    }

    @Override
    View inflateLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_category, container, false);
    }

    @Override
    void initUI(View view) {
        widthScreen = AppUtil.getScreenWidth(getActivity());
        heightScreen = AppUtil.getScreenHeight(getActivity());
        getData();
        //initViewPager(view);
        initHomeList(view);
        initSearch(view);
        setAppBar(view);
    }

    public void setAppBar(View view) {
        MaterialToolbar toolbars = getActivity().findViewById(R.id.toolbar);
        ShapeableImageView logoAppBar = getActivity().findViewById(R.id.logo_appbar);
        ShapeableImageView btnSearchBar = getActivity().findViewById(R.id.mis_action_search);
        ShapeableImageView btnNotifyBar = getActivity().findViewById(R.id.mis_action_notification);
        MaterialTextView titleBar = getActivity().findViewById(R.id.tv_title);
        titleBar.setText("WFOUR");
        logoAppBar.setVisibility(View.VISIBLE);
        btnSearchBar.setVisibility(View.VISIBLE);
        btnNotifyBar.setVisibility(View.VISIBLE);
        toolbars.setVisibility(View.VISIBLE);
    }

    public void initSearch(View view) {
        MaterialToolbar toolbars = getActivity().findViewById(R.id.toolbar);
        toolbars.setVisibility(View.VISIBLE);
        search_bar = getActivity().findViewById(R.id.mis_action_search);
        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalFunctions.startActivityWithoutAnimation(self, SearchActivity2.class);
            }
        });
    }

    public void initHomeList(View view) {
        rcvData = view.findViewById(R.id.rcvData);
        listAdapter = new ListAdapter(self, new IMyOnClick() {
            @Override
            public void MyOnClick(int position, ProductObj productObj) {

            }
        });
        rcvData.setAdapter(listAdapter);
        rcvData.setLayoutManager(new GridLayoutManager(self, 1));
        rcvData.setNestedScrollingEnabled(true);

    }

    private void initViewPager(View view) {

        screenFavorite = view.findViewById(R.id.screen_favorite);
        mViewPager = view.findViewById(R.id.view_pager);
        mIndicator = view.findViewById(R.id.circle_indicator);

        adapterIndicator = new AdapterViewPaggerCategory(getChildFragmentManager(), productObjList);

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
        //inflater.inflate(R.menu.menu_search, menu);
        //inflater.inflate(R.menu.menu_chat_new, menu);
        //inflater.inflate(R.menu.menu_cart, menu);
        //MenuItem menuItem = menu.findItem(R.id.action_cart);
        //menuItem.setIcon(GlobalFunctions.buildCounterDrawable(self,count, R.drawable.ic_cart));
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.home);
        favoriteReceiver = new FavoriteReceiver();
        IntentFilter filterFavorite = new IntentFilter();
        filterFavorite.addAction(ActionReceiver.ACTION_FAVORITE);
        //getActivity().registerReceiver(favoriteReceiver, filterFavorite);
        GlobalFunctions.getCountCart(cartList, count);
        count = DataStoreManager.getCountCart();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        if (item.getItemId() == R.id.action_search) {
            GlobalFunctions.startActivityWithoutAnimation(self, SearchActivity2.class);
            return true;
        }
        */
        if (item.getItemId() == R.id.action_cart) {
            GlobalFunctions.startActivityWithoutAnimation(self, CartActivity.class);
            return true;
        }
        if (item.getItemId() == R.id.action_chat) {
            if (DataStoreManager.getUser() == null) {
                AppUtil.showToast(self, R.string.you_are_not_login);
            } else if (DataStoreManager.getUser() != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Args.KEY_DEAL_OBJECT, HomeFragment.this.item);
                AppUtil.startActivity(self, LoginChatActivity.class, bundle);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void getData() {
        getDataHomeList();
        getBannerList();
    }

    private void getDataHomeList() {
        if (NetworkUtility.isNetworkAvailable()) {
            ModelManager.getHomeList(self, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject jsonObject = (JSONObject) object;
                    ApiResponse response = new ApiResponse(jsonObject);
                    productObjList = new ArrayList<>();
                    if (!response.isError()) {
                        homeObj = response.getDataObject(HomeObj.class);
                        convertData();

                        productObjList = homeObj.getmListHot();
                        adapterIndicator.addList(productObjList);
                    }
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    private void getBannerList() {
        if (NetworkUtility.isNetworkAvailable()) {
            ModelManager.getBannerList(self, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject jsonObject = (JSONObject) object;
                    ApiResponse response = new ApiResponse(jsonObject);
                    bannerObjs = new ArrayList<>();
                    if (!response.isError()) {
                        bannerObjs.addAll(response.getDataList(BannerObj.class));
                        if (bannerObjs == null) {
                            BannerObj obj = new BannerObj();
                            obj.setImage("https://wfour.store/shopping/backend/web/index.php/api/file?f=1583722062586_image_banner_1.png&d=banner");
                            obj.setImage("https://wfour.store/shopping/backend/web/index.php/api/file?f=1583722062819_image_banner_2.png&d=banner");
                            obj.setImage("https://wfour.store/shopping/backend/web/index.php/api/file?f=1583722062586_image_banner_3.png&d=banner");
                            obj.setImage("https://wfour.store/shopping/backend/web/index.php/api/file?f=1583722062586_image_banner_4.png&d=banner");
                            bannerObjs.add(obj);
                        }
                        listAdapter.addList2(bannerObjs);
                    }
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    private void convertData() {
        ArrayList<HomeObj> homeObjList = new ArrayList<>();
        homeObjList.add(new HomeObj(this.getString(R.string.BANNER), "BANNER", homeObj.getmListHot()));
        homeObjList.add(new HomeObj(this.getString(R.string.CATEGORIES), homeObj.getCategoryList()));
        homeObjList.add(new HomeObj("SALE PRODUCT", "TYPE", homeObj.getmListHot()));
        homeObjList.add(new HomeObj(this.getString(R.string.PRODUCTLALAIS), "TYPE", homeObj.getmListHot()));
        homeObjList.add(new HomeObj(this.getString(R.string.PRODUTUFOUN), "FEATURE LIST", homeObj.getmListFeature()));
        homeObjList.add(new HomeObj(this.getString(R.string.PRODUCTPROMOTION), "TYPE", homeObj.getmListNew()));
        listAdapter.addList(homeObjList);
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
        Category item = categoryList.get(position);
        curPositionClicked = position;
        if (typeOfCategory == Args.TYPE_OF_CATEGORY_SHORT) {
            if (Category.OTHER.equals(item.getId())) {
                GlobalFunctions.startActivityWithoutAnimation(self, AllCategoryActivity.class);
                return;
            }

            if (Category.TRANSPORT.equals(item.getId())) {
                GlobalFunctions.startActivityWithoutAnimation(self, TransportActivity.class);
            } else {
                if (GlobalFunctions.locationIsGranted(self, RC_PERMISSIONS, "")) {
                    processOncliked(position);
                }
            }
        } else {
            if (Category.TRANSPORT.equals(item.getId())) {
                GlobalFunctions.startActivityWithoutAnimation(self, TransportActivity.class);
            } else {
                if (GlobalFunctions.locationIsGranted(self, RC_PERMISSIONS, "")) {
                    processOncliked(position);
                }
            }
        }
    }

    private void processOncliked(int position) {
        if (MapsUtil.locationIsEnable(self) && AppController.getInstance().getMyLocation() != null) {
            Category item = categoryList.get(position);

            Bundle bundle = new Bundle();
            bundle.putString(Args.KEY_ID_PRODUCT_CATE, item.getId() + "");
            bundle.putString(Args.TYPE_OF_PRODUCT_NAME, item.getName() + "");
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

    @Override
    public void onClick(View view) {
        if (view == btnSellDeal) {
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
                //initDataTopDeal();
            }
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
//      recomendLayoutManager = new LinearLayoutManager(getActivity());
        recomendedAdapter = new RecomendedListAdapter(getActivity(), recomendedlist);
        recomended_recyclerview.setLayoutManager(linearLayout);
        recomended_recyclerview.setAdapter(recomendedAdapter);
    }
}
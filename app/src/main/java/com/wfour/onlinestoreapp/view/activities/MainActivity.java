package com.wfour.onlinestoreapp.view.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.wfour.onlinestoreapp.AppController;
import com.wfour.onlinestoreapp.PacketUtility;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.configs.ChatConfigs;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IOnItemClickListener;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.network.modelmanager.singletonmanager.AddressManager;
import com.wfour.onlinestoreapp.network1.MyProgressDialog;
import com.wfour.onlinestoreapp.network1.NetworkUtility;
import com.wfour.onlinestoreapp.objects.ContactObj;
import com.wfour.onlinestoreapp.objects.MenuLeft;
import com.wfour.onlinestoreapp.objects.ProductObj;
import com.wfour.onlinestoreapp.objects.RecentChatObj;
import com.wfour.onlinestoreapp.objects.SettingsObj;
import com.wfour.onlinestoreapp.objects.UserObj;
import com.wfour.onlinestoreapp.parsers.JSONParser;
import com.wfour.onlinestoreapp.quickblox.conversation.utils.DialogUtil;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.ImageUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.utils.map.IMaps;
import com.wfour.onlinestoreapp.utils.map.LocationService;
import com.wfour.onlinestoreapp.utils.map.MapsUtil;
import com.wfour.onlinestoreapp.view.adapters.MenuLeftAdapte;
import com.wfour.onlinestoreapp.view.chat.chatutils.PreferenceUtils;
import com.wfour.onlinestoreapp.view.chat.mainchat.ConnectionManager;
import com.wfour.onlinestoreapp.view.chat.mainchat.LoginChatActivity;
import com.wfour.onlinestoreapp.view.fragments.AllDealsFragment;
import com.wfour.onlinestoreapp.view.fragments.BillManagementFragment;
import com.wfour.onlinestoreapp.view.fragments.CartListFragment;
import com.wfour.onlinestoreapp.view.fragments.ContactFragment;
import com.wfour.onlinestoreapp.view.fragments.DealListFragment;
import com.wfour.onlinestoreapp.view.fragments.DealManagerFragment;
import com.wfour.onlinestoreapp.view.fragments.DealsFragment;
import com.wfour.onlinestoreapp.view.fragments.FragmentFavorite;
import com.wfour.onlinestoreapp.view.fragments.FragmentWhisList;
import com.wfour.onlinestoreapp.view.fragments.HomeFragment;
import com.wfour.onlinestoreapp.view.fragments.IwanaPayFragment;
import com.wfour.onlinestoreapp.view.fragments.IwanabizFragment;
import com.wfour.onlinestoreapp.view.fragments.MyAccountMyInfoFragment;
import com.wfour.onlinestoreapp.view.fragments.MyDealFragment;
import com.wfour.onlinestoreapp.view.fragments.NewsFragment;
import com.wfour.onlinestoreapp.view.fragments.ProducerManagerFragment;
import com.wfour.onlinestoreapp.view.fragments.SellerFragment;
import com.wfour.onlinestoreapp.view.fragments.SettingsFragment;
import com.wfour.onlinestoreapp.view.fragments.WebViewFragment;
import com.wfour.onlinestoreapp.widgets.textview.TextViewBold;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        DealListFragment.IListenerDealsChange, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, IOnItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private CoordinatorLayout bgMain;

    private static final int RC_LOCATION_PERMISSION_TO_UPDATE_DRIVER_LOCATION = 1;
    private static final int RC_TURN_ON_LOCATION_TO_UPDATE_DRIVER_LOCATION = 2;
    private static final int RC_LOCATION_PERMISSION_TO_UPDATE_USER_LOCATION = 3;
    private static final int RC_TURN_ON_LOCATION_TO_UPDATE_USER_LOCATION = 4;

    private static final String FRAG_HOME = "Home";
    private static final String FRAG_DEAL_MANAGER = "dealManager";
    private static final String FRAG_SELLER_MANAGER = "sellerManager";
    private static final String FRAG_BUYER_MANAGER = "buyerManager";
    private static final String FRAG_ALL_DEALS = "allDeals";
    private static final String FRAG_NEWS = "newsAndEvents";
    private static final String FRAG_SETTINGS = "settings";
    private static final String FRAG_IWANA_PAY = "iwanapay";
    private static final String FRAG_MY_ACCOUNT = "myaccount";
    private static final String FRAG_MY_DEAL = "mydeal";
    private static final String FRAG_IWANA_CHAT = "FRAG_IWANA_CHAT";
    private static final String FRAG_ABOUT = "FRAG_ABOUT";
    private static final String FRAG_FAQ = "FRAG_FAQ";
    private static final String FRAG_WEBVIEW = "FRAG_WEBVIEW";
    public static final String LOG_OUT = "LOG_OUT";
    public static final String FRAG_WHISLIST = "WHISLIST";


    public static FragmentTransaction fragmentTransaction;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private MaterialToolbar toolbar;
    private ArrayList<MenuLeft> menuLefts;
    private MenuLeftAdapte adapter;
    private LinearLayout llMenuLeft;
    private RecyclerView lvMenu;
    public static CircleImageView imgAvatar;
    public static TextViewBold tvName;
    public static TextViewBold tvMoney;
    private TextViewBold tvEmail;

    private AllDealsFragment mAllDealsFragment;
    public static HomeFragment mHomeFragment;
    private NewsFragment mNewsFragment;
    private SettingsFragment mSettingsFragment;
    private IwanaPayFragment mIwanaPayFragment;
    //    private MyAccountFragment myAccountFragment;
    private MyAccountMyInfoFragment myAccountFragment;
    private CartListFragment cartlistFragment;
    private IwanabizFragment mFrgIwanabiz;
    private MyDealFragment mFrgMyDeal;
    private DealsFragment mFrgDeal;
    private DealManagerFragment mFrgDealManager;
    private ContactFragment mContactFragment;
    private WebViewFragment mWebViewFragment;
    private FragmentFavorite mFavoriteFragment;
    private SellerFragment mSellerFragment;
    private ProducerManagerFragment mProManaFragment;
    private BillManagementFragment mBillManagementFragment;
    private FragmentWhisList fragmentWhisList;

    public static BottomNavigationView nav_main;

    private GoogleApiClient mGoogleApiClient;

    private int mSelectedNav;
    private int indexMenu = 0;
    private String getString = "main";
    private ProductObj item;
    private boolean userLocationIsUpdated;
    private String text;
    private String countryCodeSelected = "";
    private TextView tvConfirm, tvPhoneCode;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initGoogleApiClient();
        // Get saved instances
        if (savedInstanceState != null) {
            mFavoriteFragment = (FragmentFavorite) getSupportFragmentManager().getFragment(savedInstanceState, FRAG_HOME);
            mFrgDealManager = (DealManagerFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAG_DEAL_MANAGER);
            mSellerFragment = (SellerFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAG_SELLER_MANAGER);
            mFrgDeal = (DealsFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAG_BUYER_MANAGER);
            mHomeFragment = (HomeFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAG_ALL_DEALS);
            mNewsFragment = (NewsFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAG_NEWS);
            mSettingsFragment = (SettingsFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAG_SETTINGS);
            mIwanaPayFragment = (IwanaPayFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAG_IWANA_PAY);
            myAccountFragment = (MyAccountMyInfoFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAG_MY_ACCOUNT);
            mFrgMyDeal = (MyDealFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAG_MY_DEAL);
            mContactFragment = (ContactFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAG_IWANA_CHAT);
            mWebViewFragment = (WebViewFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAG_WEBVIEW);
            fragmentWhisList = (FragmentWhisList) getSupportFragmentManager().getFragment(savedInstanceState, FRAG_WHISLIST);
        }

        // Init quickblox
        if (DataStoreManager.getUser() != null && (DataStoreManager.getUser().getToken() != null
                && !DataStoreManager.getUser().getToken().equals(""))) {
            initSession(savedInstanceState);
            initDialogsListener();
            initPushManager();
        }
        // Start location service in some cases(driver closes app without deactivating)
        //updateDriverLocation();

        // Get contacts from scratch
        //getContacts();
        AppUtil.logSizeMultiScreen(this);
    }

    @Override
    void inflateLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    void initUI() {

        if (DataStoreManager.getUser() != null) {
            if (DataStoreManager.getUser().getPhone().equals("") || DataStoreManager.getUser().getPhone() == null) {
                showDialogPhone();
            }
        }

        bgMain = findViewById(R.id.bg_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //imgAvatar = findViewById(R.id.img_avatar);
        //tvName = findViewById(R.id.lbl_deal_name);
        //tvMoney = findViewById(R.id.lbl_deal_money);
        tvEmail = findViewById(R.id.tv_email);
        //lvMenu = findViewById(R.id.lv_menu);
        nav_main = findViewById((R.id.nav_main));
        initNavigationView();

        //initMenuLeft();

        Intent intent = getIntent();
        getString = intent.getStringExtra(BillInformationActivity.BILL);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            text = bundle.getString(LOG_OUT);
        }

        if (getString != null && getString.equals("bill")) {
            onItemClick(Constants.MENU_BILL);
        } else if (text != null && text.equals("logout")) {
            switchMenu(Constants.MENU_ALL_DEAL);
        } else {
            switchMenu(Constants.MENU_ALL_DEAL);
        }
        //adapter.notifyDataSetChanged();
    }


    public void setbackGroundMainHaveData() {
        if(bgMain!=null)
        {
            bgMain.setBackgroundResource(R.drawable.bg_deal);
        }
    }

    public void setbackGroundMainNoData() {
        if(bgMain!=null)
        {
            bgMain.setBackgroundColor(getResources().getColor(R.color.colorBackgroundNodata));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //AppUtil.hideSoftKeyboard(self);
            onBackPressed();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initNavigationView() {
        //mDrawer = findViewById(R.id.drawer_layout);
        /*
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                AppUtil.hideSoftKeyboard(MainActivity.this);

//                updateMenuHeader();
            }
        };
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
        */
        //mNavigationView = findViewById(R.id.nav_view);
        nav_main.setOnNavigationItemSelectedListener(navListiner);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListiner =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home_menu:

                            fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            if (mHomeFragment == null) {
                                mHomeFragment = HomeFragment.newInstance(Args.TYPE_OF_CATEGORY_ALLS);
                            }
                            fragmentTransaction.replace(R.id.frl_main, mHomeFragment).commit();
                            //setTitle(R.string.home);
                            break;
                        case R.id.wishlist_menu:
                            fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            if (DataStoreManager.getUser() != null) {
                                if (fragmentWhisList == null) {
                                    fragmentWhisList = FragmentWhisList.newInstance();
                                }
                                fragmentTransaction.replace(R.id.frl_main, fragmentWhisList).commit();
                                //setTitle(R.string.favorite);
                            } else {
                                showLogout();
                            }
                            break;
                        case R.id.chats_menu:

                            if (DataStoreManager.getUser() == null) {
                                //AppUtil.showToast(self, R.string.you_are_not_login);
                                showLogout();
                            } else if (DataStoreManager.getUser() != null) {
                                Bundle bundle = new Bundle();
                                bundle.putParcelable(Args.KEY_DEAL_OBJECT, MainActivity.this.item);
                                AppUtil.startActivity(self, LoginChatActivity.class, bundle);
                            }
                            break;
                        case R.id.cart_menu:
//                            GlobalFunctions.startActivityWithoutAnimation(self, CartActivity.class);
                            fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            if (DataStoreManager.getUser() != null) {
                                if (cartlistFragment == null) {
                                    cartlistFragment = CartListFragment.newInstance();
                                }
                                fragmentTransaction.replace(R.id.frl_main, cartlistFragment).commit();

                                setTitle(R.string.profile);
                            } else {
                                showLogout();
                            }
                            break;
                        case R.id.akun_menu:

                            fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            if (DataStoreManager.getUser() != null) {
                                if (myAccountFragment == null) {
                                    myAccountFragment = MyAccountMyInfoFragment.newInstance();
                                }
                                fragmentTransaction.replace(R.id.frl_main, myAccountFragment).commit();

                                setTitle(R.string.profile);
                            } else {
                                showLogout();
                            }
                            break;
                    }
                    return true;
                }
            };

    private void initMenuLeft() {

        //initMenuLeftHeader();

        //initListMenu();

        //adapter = new MenuLeftAdapte(menuLefts, self, this);
        //LinearLayoutManager manager = new LinearLayoutManager(self);
        //manager.setOrientation(LinearLayoutManager.VERTICAL);
        //lvMenu.setAdapter(adapter);
        //lvMenu.setLayoutManager(manager);
        //lvMenu.setHasFixedSize(true);

        //adapter.notifyDataSetChanged();
    }

    private void initMenuLeftHeader() {
        UserObj userObj = DataStoreManager.getUser();
        if (userObj != null) {
            //tvName.setText(userObj.getName());
            //tvMoney.setText("POINT " +  StringUtil.convertNumberToString(DataStoreManager.getUser().getPoint(), 0));
            // tvEmail.setText(userObj.getEmail());
            //ImageUtil.setImage(self, imgAvatar, userObj.getAvatar());
        }
    }

    public void updateMenuLeftHeader() {
        if (AppController.getInstance().isUserUpdated()) {
            UserObj userObj = DataStoreManager.getUser();
            if (userObj != null) {
                //tvName.setText(userObj.getName());
                //tvMoney.setText("POINT " + StringUtil.convertNumberToString(DataStoreManager.getUser().getPoint(), 0));
                //tvEmail.setText(userObj.getEmail());
                //ImageUtil.setImage(self, imgAvatar, userObj.getAvatar());
            }

            AppController.getInstance().setUserUpdated(false);
        }
    }


    private void initListMenu() {
        menuLefts = new ArrayList<>();
        menuLefts.add(new MenuLeft(Constants.MENU_ALL_DEAL, R.drawable.home,
                getResources().getString(R.string.home), true));
//        menuLefts.add(new MenuLeft(Constants.MENU_BUYER_NAMAGER, R.drawable.ic_buyer_white,
//                getResources().getString(R.string.buy_deals), false ));
        menuLefts.add(new MenuLeft(Constants.MENU_BILL, R.drawable.bill,
                getResources().getString(R.string.bill), false));
//        menuLefts.add(new MenuLeft(Constants.MENU_PAYMENT, R.drawable.visa_199,
//                getResources().getString(R.string.payment), false));
        menuLefts.add(new MenuLeft(Constants.MENU_PROFILE, R.drawable.user_199,
                getResources().getString(R.string.profile), false));
        menuLefts.add(new MenuLeft(Constants.MENU_WHISLIST, R.drawable.ic_heart_favorite,
                getResources().getString(R.string.favorite), false));
        menuLefts.add(new MenuLeft(Constants.MENU_SHARE, R.drawable.sharethis_199,
                getResources().getString(R.string.share), false));
        menuLefts.add(new MenuLeft(Constants.MENU_FAQ, R.drawable.chat_4_199,
                getResources().getString(R.string.faq), false));
//        menuLefts.add(new MenuLeft(Constants.MENU_SETTING, R.drawable.ic_setting_white,
//                getResources().getString(R.string.settings), false ));
        menuLefts.add(new MenuLeft(Constants.MENU_HELP, R.drawable.help_199,
                getResources().getString(R.string.help), false));
        menuLefts.add(new MenuLeft(Constants.MENU_ABOUT_US, R.drawable.info_2_199,
                getResources().getString(R.string.about_us), false));
//        if (DataStoreManager.getUser() != null) {
//            if (DataStoreManager.getUser().getRole() == 30) {
//                menuLefts.add(new MenuLeft(Constants.MENU_CHAT, R.drawable.ic_facebook,
//                        getResources().getString(R.string.chat), false));
//            }
//        }

        menuLefts.add(new MenuLeft(Constants.MENU_LOGOUT, R.drawable.account_logout_199,
                getResources().getString(R.string.log_out), false));


    }

    @Override
    public void onItemClick(int position) {
        if (indexMenu != position) {
            menuLefts.get(indexMenu).setSelected(false);
            indexMenu = position;
            switchMenu(menuLefts.get(position).getId());
            menuLefts.get(position).setSelected(true);
        }
        //else mDrawer.closeDrawer(GravityCompat.START);

        //adapter.notifyDataSetChanged();
    }

    public void switchMenu(int idMenuLeft) {
        setbackGroundMainHaveData();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (idMenuLeft) {
            case Constants.MENU_ALL_DEAL:

                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.newInstance(Args.TYPE_OF_CATEGORY_ALLS);
                }
                fragmentTransaction.replace(R.id.frl_main, mHomeFragment).commit();

                setTitle(R.string.home);

                break;
            case Constants.MENU_WHISLIST:
                if (DataStoreManager.getUser() != null) {
                    if (fragmentWhisList == null) {
                        fragmentWhisList = FragmentWhisList.newInstance();
                    }
                    fragmentTransaction.replace(R.id.frl_main, fragmentWhisList).commit();
                    setTitle(R.string.favorite);
                } else {
                    showLogout();
                }
                break;

//            case Constants.MENU_BUYER_NAMAGER:
//                if (mFrgDeal == null) {
//                    mFrgDeal = DealsFragment.newInstance();
//                }
//                fragmentTransaction.replace(R.id.frl_main, mFrgDeal).commit();
//
//                setTitle(R.string.buy_deals);
//                break;
            case Constants.MENU_BILL:
                if (DataStoreManager.getUser() != null) {
                    if (mBillManagementFragment == null) {
                        mBillManagementFragment = BillManagementFragment.newInstance();
                    }
                    fragmentTransaction.replace(R.id.frl_main, mBillManagementFragment).commit();
                    setTitle(R.string.bill);
                } else {
//                    AppUtil.showToast(self, "No Account");
                    showLogout();
                }
                break;
//            case Constants.MENU_PAYMENT:
//                if (mIwanaPayFragment == null) {
//                    mIwanaPayFragment = IwanaPayFragment.newInstance();
//                }
//                fragmentTransaction.replace(R.id.frl_main, mIwanaPayFragment).commit();
//
//                setTitle(R.string.payment);
//                break;

            case Constants.MENU_PROFILE:
                if (DataStoreManager.getUser() != null) {
                    if (myAccountFragment == null) {
                        myAccountFragment = MyAccountMyInfoFragment.newInstance();
                    }
                    fragmentTransaction.replace(R.id.frl_main, myAccountFragment).commit();

                    setTitle(R.string.profile);
                } else {
                    showLogout();
                }
                break;

//            case Constants.MENU_NEW_EVENT:
//                Bundle bundle = new Bundle();
//                bundle.putString(Args.KEY_ID_DEAL_CATE, DealCateObj.NEWS_AND_EVENTS);
//                bundle.putString(Args.TYPE_OF_DEAL_NAME, getString(R.string.news_and_events));
//                bundle.putString(Args.TYPE_OF_SEARCH_DEAL, Constants.SEARCH_NEARBY);
//
//                GlobalFunctions.startActivityWithoutAnimation(self, DealsActivity.class, bundle);
//                break;

            case Constants.MENU_SHARE:
                AppUtil.share(this, "http://play.google.com/store/apps/details?id=" + new PacketUtility().getPacketName());

//                setTitle(R.string.share);

                break;

            case Constants.MENU_FAQ:
                showScreenFaq();
                break;

//            case Constants.MENU_SETTING:
//                fragmentTransaction.replace(R.id.frl_main, SettingsFragment.newInstance()).commit();
//                setTitle(R.string.settings);
//                break;

            case Constants.MENU_HELP:
                showScreenHelp();
                break;

            case Constants.MENU_ABOUT_US:
                showScreenAboutUs();
                break;

            case Constants.MENU_LOGOUT:
                if (DataStoreManager.getUser() != null) {
                    logout();

                } else {
                    AppUtil.showToast(self, "No Account");
                }
                break;
//            case Constants.MENU_CHAT:
//                if (DataStoreManager.getUser() != null && DataStoreManager.getUser().getRole() == 30) {
//                    AppUtil.startActivity(self, LoginChatActivity.class);
//                } else {
//                    showLogout();
//                }
//                break;
        }

        //mDrawer.closeDrawer(GravityCompat.START);

    }

    private void showLogout() {
        DialogUtil.showAlertDialog(self, R.string.notification, R.string.you_need_login, new DialogUtil.IDialogConfirm() {
            @Override
            public void onClickOk() {
                goLogin();
            }

        });
    }

    private void goLogin() {
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.LOG_OUT, "logout");
        AppUtil.startActivity(self, SplashLoginActivity.class, bundle);
        finish();
    }

    private void showScreenAboutUs() {

        SettingsObj setting = DataStoreManager.getSettingUtility();
        if (setting == null) {
            if (NetworkUtility.getInstance(this).isNetworkAvailable()) {
                ModelManager.getSettingUtility(this, new ModelManagerListener() {
                    @Override
                    public void onSuccess(Object object) {
                        JSONObject jsonObject = (JSONObject) object;
                        ApiResponse apiResponse = new ApiResponse(jsonObject);
                        if (!apiResponse.isError()) {
                            DataStoreManager.saveSettingUtility(jsonObject.toString());
                            SettingsObj utitlityObj = apiResponse.getDataObject(SettingsObj.class);
//                                    openWebView(getString(R.string.about_us), utitlityObj.getAbout());

                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.KEY_URL, utitlityObj.getAbout());
                            mWebViewFragment = WebViewFragment.newInstance(bundle);

                            fragmentTransaction.replace(R.id.frl_main, mWebViewFragment).commit();

                            setTitle(getString(R.string.about_us));
                        }
                    }

                    @Override
                    public void onError() {
                    }
                });
            } else {
                AppUtil.showToast(getApplicationContext(), R.string.msg_network_not_available);
            }
        } else {

            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_URL, DataStoreManager.getSettingUtility().getAbout());
            mWebViewFragment = WebViewFragment.newInstance(bundle);

            fragmentTransaction.replace(R.id.frl_main, mWebViewFragment).commit();

            setTitle(getString(R.string.about_us));
//                    openWebView(getString(R.string.about_us), DataStoreManager.getSettingUtility().getAbout());
        }
    }

    private void showScreenFaq() {
        SettingsObj setting = DataStoreManager.getSettingUtility();
        if (setting == null) {
            if (NetworkUtility.getInstance(this).isNetworkAvailable()) {
                ModelManager.getSettingUtility(this, new ModelManagerListener() {
                    @Override
                    public void onSuccess(Object object) {
                        JSONObject jsonObject = (JSONObject) object;
                        ApiResponse apiResponse = new ApiResponse(jsonObject);
                        if (!apiResponse.isError()) {
                            DataStoreManager.saveSettingUtility(jsonObject.toString());
                            SettingsObj utitlityObj = apiResponse.getDataObject(SettingsObj.class);
//                                    openWebView(getString(R.string.faq), utitlityObj.getFaq());

                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.KEY_URL, utitlityObj.getFaq());
                            mWebViewFragment = WebViewFragment.newInstance(bundle);

                            fragmentTransaction.replace(R.id.frl_main, mWebViewFragment).commit();

                            setTitle(getString(R.string.faq));

//                                    openWebView(getString(R.string.about_us), DataStoreManager.getSettingUtility().getAbout());
                        }

                    }

                    @Override
                    public void onError() {
                    }
                });
            } else {
                AppUtil.showToast(getApplicationContext(), R.string.msg_network_not_available);
            }

        } else {

            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_URL, DataStoreManager.getSettingUtility().getFaq());
            mWebViewFragment = WebViewFragment.newInstance(bundle);

            fragmentTransaction.replace(R.id.frl_main, mWebViewFragment).commit();

            setTitle(getString(R.string.faq));

//                    openWebView(getString(R.string.faq), DataStoreManager.getSettingUtility().getFaq());
        }
    }

    private void showScreenHelp() {
        SettingsObj setting = DataStoreManager.getSettingUtility();
        if (setting == null) {
            if (NetworkUtility.getInstance(this).isNetworkAvailable()) {
                ModelManager.getSettingUtility(this, new ModelManagerListener() {
                    @Override
                    public void onSuccess(Object object) {
                        JSONObject jsonObject = (JSONObject) object;
                        ApiResponse apiResponse = new ApiResponse(jsonObject);
                        if (!apiResponse.isError()) {
                            DataStoreManager.saveSettingUtility(jsonObject.toString());
                            SettingsObj utitlityObj = apiResponse.getDataObject(SettingsObj.class);
//                                    openWebView(getString(R.string.help), utitlityObj.getHelp());

                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.KEY_URL, utitlityObj.getHelp());
                            mWebViewFragment = WebViewFragment.newInstance(bundle);

                            fragmentTransaction.replace(R.id.frl_main, mWebViewFragment).commit();

                            setTitle(getString(R.string.help));

                        } else {
                            AppUtil.showToast(getApplicationContext(), apiResponse.getMessage());
                        }
                    }

                    @Override
                    public void onError() {
                        AppUtil.showToast(getApplicationContext(), "Error!");
                    }
                });
            } else {
                AppUtil.showToast(getApplicationContext(), R.string.msg_network_not_available);
            }

        } else {

            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_URL, DataStoreManager.getSettingUtility().getHelp());
            mWebViewFragment = WebViewFragment.newInstance(bundle);

            fragmentTransaction.replace(R.id.frl_main, mWebViewFragment).commit();

            setTitle(getString(R.string.help));

//                    openWebView(getString(R.string.help), DataStoreManager.getSettingUtility().getHelp());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//        switch (requestCode) {
//            case RC_LOCATION_PERMISSION_TO_UPDATE_USER_LOCATION:
//                if (grantResults.length > 0) {
//                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                        //updateUserLocation();
//                    } else {
//                        showPermissionsReminder(RC_LOCATION_PERMISSION_TO_UPDATE_USER_LOCATION, true);
//                    }
//                }
//                break;
//            case RC_LOCATION_PERMISSION_TO_UPDATE_DRIVER_LOCATION: {
//                if (grantResults.length > 0) {
//                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                        if (MapsUtil.locationIsEnable(self)) {
//                            //updateDriverLocation();
//                        } else {
//                            turnOnLocationReminder(RC_TURN_ON_LOCATION_TO_UPDATE_DRIVER_LOCATION, false);
//                        }
//                    } else {
//                        showPermissionsReminder(RC_LOCATION_PERMISSION_TO_UPDATE_DRIVER_LOCATION, true);
//                    }
//                }
//                break;
//            }
//            default:
//                break;
//        }
    }

    private void updateMenuHeader() {
        if (AppController.getInstance().isUserUpdated()) {
            //View view = mNavigationView.getHeaderView(0);
            //ImageView imageView = view.findViewById(R.id.civ_avatar);
            //TextView tvName = view.findViewById(R.id.lbl_name);
            //TextView tvEmail = view.findViewById(R.id.lbl_email);
            UserObj userObj = DataStoreManager.getUser();
            if (userObj != null) {
                //tvName.setText(userObj.getName());
                //tvEmail.setText(userObj.getEmail());
                //ImageUtil.setImage(self, imageView, userObj.getAvatar());
            }

            AppController.getInstance().setUserUpdated(false);
        }
    }

    @Override
    void initControl() {
        //mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void getExtraValues() {
        super.getExtraValues();
        getExtra(getIntent());
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (mFavoriteFragment != null) {
            getSupportFragmentManager().putFragment(outState, FRAG_HOME, mFavoriteFragment);
        }
        if (mFrgDealManager != null) {
            getSupportFragmentManager().putFragment(outState, FRAG_DEAL_MANAGER, mFrgDealManager);
        }

        if (mSellerFragment != null) {
            getSupportFragmentManager().putFragment(outState, FRAG_SELLER_MANAGER, mSellerFragment);
        }

        if (mFrgDeal != null) {
            getSupportFragmentManager().putFragment(outState, FRAG_BUYER_MANAGER, mFrgDeal);
        }

        if (mHomeFragment != null) {
            getSupportFragmentManager().putFragment(outState, FRAG_ALL_DEALS, mHomeFragment);
        }

        if (mNewsFragment != null) {
            getSupportFragmentManager().putFragment(outState, FRAG_NEWS, mNewsFragment);
        }
        if (mSettingsFragment != null) {
            getSupportFragmentManager().putFragment(outState, FRAG_SETTINGS, mSettingsFragment);
        }
        if (mIwanaPayFragment != null) {
            getSupportFragmentManager().putFragment(outState, FRAG_IWANA_PAY, mIwanaPayFragment);
        }
        if (myAccountFragment != null) {
            getSupportFragmentManager().putFragment(outState, FRAG_MY_ACCOUNT, myAccountFragment);
        }

        if (mFrgMyDeal != null) {
            getSupportFragmentManager().putFragment(outState, FRAG_MY_DEAL, mFrgMyDeal);
        }
        if (mContactFragment != null) {
            getSupportFragmentManager().putFragment(outState, FRAG_IWANA_CHAT, mContactFragment);
        }
        if (mWebViewFragment != null) {
            getSupportFragmentManager().putFragment(outState, FRAG_WEBVIEW, mWebViewFragment);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("EE", "EE: NEWINTENT MAIN");
        getExtra(intent);
    }

    @Override
    public void onBackPressed() {
        //if (mDrawer.isDrawerOpen(GravityCompat.START)) {
        //    mDrawer.closeDrawer(GravityCompat.START);
        //} else {
        super.onBackPressed();
        //}
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //initMenuLeftHeader();
        //adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        // Reset recent chat list
        DataStoreManager.clearRecentChat();
        EventBus.getDefault().unregister(this);
        // Keep conversation status
        DataStoreManager.saveConversationStatus(false);
        super.onDestroy();
    }


    @Override
    public void onStart() {
        //mGoogleApiClient.connect();
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        //mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            if (requestCode == Args.RQ_SMS) {
                updateProfile();
            } else if (requestCode == Args.RQ_GET_PHONE_CODE) {
                countryCodeSelected = data.getExtras().getString(Args.KEY_PHONE_CODE);
                tvPhoneCode.setText(countryCodeSelected);
            }
//        if (requestCode == Args.RC_TURN_ON_LOCATION) {
//            if (MapsUtil.locationIsEnable(self)) {
//                //getMyLocation();
//            } else {
//                turnOnLocationReminder(Args.RC_TURN_ON_LOCATION, true);
//            }
//        } else if (requestCode == RC_TURN_ON_LOCATION_TO_UPDATE_DRIVER_LOCATION) {
//            if (MapsUtil.locationIsEnable(self)) {
//                //updateDriverLocation();
//            } else {
//                turnOnLocationReminder(RC_TURN_ON_LOCATION_TO_UPDATE_DRIVER_LOCATION, false);
//            }
//        } else if (requestCode == RC_TURN_ON_LOCATION_TO_UPDATE_USER_LOCATION) {
//            if (MapsUtil.locationIsEnable(self)) {
//                //updateUserLocation();
//            } else {
//                turnOnLocationReminder(RC_TURN_ON_LOCATION_TO_UPDATE_USER_LOCATION, true);
//            }
//        }

    }

    private void updateProfile() {
        ModelManager.getUpdateProfile2(self, DataStoreManager.getUser().getId(), phone, DataStoreManager.getUser().getName(), new ModelManagerListener() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(object.toString());
                    ApiResponse response = new ApiResponse(jsonObject);
                    Log.e("hihi", " 2" + response.toString());

                    if (!response.isError()) {
                        UserObj userObj = response.getDataObject(UserObj.class);
                        //userObj.setToken(DataStoreManager.getUser().getToken());
                        //userObj.setRememberMe(DataStoreManager.getUser().isRememberMe());
                        userObj.setPhone(phone);
                        DataStoreManager.saveUser(userObj);
                        AppController.getInstance().setUserUpdated(true);


                    } else {
                        Toast.makeText(self, response.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    private void getMyLocation() {
        if (MapsUtil.locationIsEnable(self)) {
            MapsUtil.getMyLocation(mGoogleApiClient, new IMaps() {
                @Override
                public void processFinished(Object obj) {
                    AppController.getInstance().setMyLocation((Location) obj);
                }
            });
        } else {
            MapsUtil.displayLocationSettingsRequest(this, Args.RC_TURN_ON_LOCATION);
        }
    }

    private void initGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(self)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
//        if (GlobalFunctions.locationIsGranted(self, Args.RC_LOCATION, null)) {
//            MapsUtil.getMyLocation(mGoogleApiClient, new IMaps() {
//                @Override
//                public void processFinished(Object obj) {
//                    Location location = (Location) obj;
//                    AppController.getInstance().setMyLocation(location);
//                }
//            });

        //updateUserLocation();
//        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


    private void getExtra(Intent intent) {
        Bundle bundle = intent.getExtras();

        processNotificationNewDeal(bundle);
        if (bundle != null) {
            Log.e(TAG, "bundle != null");
            RecentChatObj recentChatObj = null;
            String notificationType = "";
            if (bundle.containsKey(Args.NOTIFICATION_TYPE)) {
                notificationType = bundle.getString(Args.NOTIFICATION_TYPE);
                if (notificationType != null) {
                    Log.e(TAG, "type is " + notificationType);
                    if (notificationType.equalsIgnoreCase(ChatConfigs.QUICKBLOX_MESSAGE)) {
                        if (bundle.containsKey(Args.RECENT_CHAT_OBJ)) {
                            Log.e(TAG, "have recentChatObj");
                            recentChatObj = bundle.getParcelable(Args.RECENT_CHAT_OBJ);
                            if (recentChatObj != null) {
                                RecentChatsActivity.start(self, recentChatObj);
                            }
                        } else {
                            Log.e(TAG, "have no recentChatObj");
                        }
                    } else if (notificationType.equalsIgnoreCase(ChatConfigs.NOTIFICATION_LOCATION_PERMISSION)) {
                        if (GlobalFunctions.locationIsGranted(self, RC_LOCATION_PERMISSION_TO_UPDATE_DRIVER_LOCATION, "")) {
                            //updateDriverLocation();
                        }
                    } else if (notificationType.equalsIgnoreCase(ChatConfigs.NOTIFICATION_TURN_ON_LOCATION)) {
                        if (MapsUtil.locationIsEnable(self)) {
                            //updateDriverLocation();
                        } else {
                            MapsUtil.displayLocationSettingsRequest(self, RC_TURN_ON_LOCATION_TO_UPDATE_DRIVER_LOCATION);
                        }
                    }
                }
            }
        } else {
            Log.e(TAG, "bundle is null");
        }
    }

    private void openWebView(String title, String url) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_TITLE, title);
        bundle.putString(Constants.KEY_URL, url);
        AppUtil.startActivity(MainActivity.this, WebViewActivity.class, bundle);
    }

    private void logout() {
        showDialogLogout();


    }

    private void showDialogLogout() {
        DialogUtil.showAlertDialog(self, R.string.log_out, R.string.you_wanto_logout, new DialogUtil.IDialogConfirm() {
            @Override
            public void onClickOk() {
                requestLogout();
            }
        });
    }

    private void requestLogout() {
        if (NetworkUtility.getInstance(self).isNetworkAvailable()) {
            final MyProgressDialog progressDialog = new MyProgressDialog(self);
            progressDialog.show();
            processBeforeLoggingOut(progressDialog);

        } else {
            Toast.makeText(self, R.string.msg_no_network, Toast.LENGTH_SHORT).show();
        }
    }

    private void processBeforeLoggingOut(MyProgressDialog progressDialog) {
        if (DataStoreManager.getUser() != null && DataStoreManager.getUser().getDriverData() != null) {
            if (DataStoreManager.getUser().getDriverData().isAvailable()) {
                LocationService.start(self, LocationService.STOP_REQUESTING_LOCATION);

                // Deactivate driver's mode before logging out
                ModelManager.activateDriverMode(self, Constants.OFF, 0, new ModelManagerListener() {
                    @Override
                    public void onSuccess(Object object) {
                    }

                    @Override
                    public void onError() {
                    }
                });
            }
        }

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        DataStoreManager.removeUser();
        AddressManager.getInstance().getArray().clear();
        AppController.getInstance().setUserUpdated(true);
        Bundle bundle = new Bundle();
        bundle.putString(LOG_OUT, "logout");
//        AppUtil.startActivity(self, SplashLoginActivity.class, bundle);
//        finish();

        //tvName.setText(null);
        //tvMoney.setText(null);
        //ImageUtil.setImage(self, imgAvatar, null);
        //imgAvatar.setImageDrawable(null);
        //onItemClick(Constants.MENU_ALL_DEAL);
        //adapter.notifyDataSetChanged();


        //SendBird Logout
        SendBird.unregisterPushTokenAllForCurrentUser(new SendBird.UnregisterPushTokenHandler() {
            @Override
            public void onUnregistered(SendBirdException e) {
                if (e != null) {
                    //Error
                    e.printStackTrace();
                    // Don't return because we still need to disconnect.
                }
                ConnectionManager.logout(new SendBird.DisconnectHandler() {
                    @Override
                    public void onDisconnected() {
                        PreferenceUtils.setConnected(self, false);
                    }
                });
            }
        });

    }

    private void updateDriverLocation() {
        if (DataStoreManager.getUser() != null && DataStoreManager.getUser().getDriverData() != null) {
            if (DataStoreManager.getUser().getDriverData().isAvailable()) {
                LocationService.start(self, LocationService.REQUEST_LOCATION);
            }
        }
    }

//    private void updateUserLocation() {
//        if (NetworkUtility.getInstance(self).isNetworkAvailable()) {
//            if (!userLocationIsUpdated) {
//                if (DataStoreManager.getUser() != null) {
//                    if ((DataStoreManager.getUser().getDriverData() != null
//                            && !DataStoreManager.getUser().getDriverData().isAvailable())
//                            || DataStoreManager.getUser().getDriverData() == null) {
//                        if (GlobalFunctions.locationIsGranted(self, RC_LOCATION_PERMISSION_TO_UPDATE_USER_LOCATION, null)) {
//                            if (MapsUtil.locationIsEnable(self)) {
//                                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//                                    MapsUtil.getMyLocation(mGoogleApiClient, new IMaps() {
//                                        @Override
//                                        public void processFinished(Object obj) {
//                                            Location location = (Location) obj;
//
//                                            ModelManager.updateLocation(self, new LatLng(location.getLatitude(), location.getLongitude()));
//                                            userLocationIsUpdated = true;
//                                        }
//                                    });
//                                }
//                            } else {
//                                MapsUtil.displayLocationSettingsRequest(self, RC_TURN_ON_LOCATION_TO_UPDATE_USER_LOCATION);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

    private void processNotificationNewDeal(Bundle args) {
        if (args != null) {
            if (args.containsKey(Args.NOTIFICATION_TYPE)) {
                String type = args.getString(Args.NOTIFICATION_TYPE);
                if (type != null && type.equalsIgnoreCase(ChatConfigs.TYPE_DEAL)) {
                    GlobalFunctions.startActivityWithoutAnimation(this, DealDetailActivity.class, args);
                }
            }
        }

    }

    private void getContacts() {
        if (NetworkUtility.getInstance(self).isNetworkAvailable()) {
            ModelManager.getContacts(self, false, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    Log.e(TAG, "contacts " + new Gson().toJson(object));
                    JSONObject jsonObject = (JSONObject) object;
                    if (JSONParser.responseIsSuccess(jsonObject)) {
                        ArrayList<ContactObj> contactObjs = JSONParser.parseContacts(jsonObject);
                        if (contactObjs.size() > 0) {
                            // Save contacts into preference
                            DataStoreManager.saveContactsList(contactObjs);
                        }
                    }
                }

                @Override
                public void onError() {
                }
            });
        }
    }

    @Override
    public void onChanged(ArrayList<ProductObj> mProductObj) {

    }

    public void showDialogPhone() {
        Dialog dialog = DialogUtil.setDialogCustomView(self, R.layout.dialog_updatephone, false);
        tvConfirm = dialog.findViewById(R.id.tv_confirm);
        tvPhoneCode = dialog.findViewById(R.id.tv_phone_code);
        EditText editText = dialog.findViewById(R.id.txt_phone);
        tvPhoneCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalFunctions.startActivityForResult(self, PhoneCountryListActivity.class, Args.RQ_GET_PHONE_CODE);
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtil.isEmpty(editText.getText().toString().trim())) {
                    Toast.makeText(MainActivity.this, "Your number phone is not blank!", Toast.LENGTH_SHORT).show();
                } else {
                    phone = tvPhoneCode.getText() + editText.getText().toString().replaceAll("\\s+", "");
                    if (!Patterns.PHONE.matcher(phone).matches()) {
                        AppUtil.showToast(self, R.string.validation_req_phone_valid);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(self);
                        builder.setTitle(phone);
                        builder.setMessage(R.string.confirm_phone);
                        builder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                            dialogInterface.dismiss();

                            Bundle bundle = new Bundle();


                            bundle.putString("phone", phone);
                            GlobalFunctions.startActivityForResult(self, VerificationActivity.class, Args.RQ_SMS, bundle);
                            //AppUtil.startActivity(self, VerificationActivity.class, bundle);
                            dialog.dismiss();
                        });
                        builder.setNegativeButton(R.string.edit, (dialogInterface, i) -> {
//                        edUser.requestFocus();
//                        AppUtil.showKeyboard();

                            dialogInterface.dismiss();
                        });
                        builder.create().show();
                    }
                }
            }
        });
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GlobalFunctions.updateBalance event) {
        getProfile();

    }

    private void getProfile() {
        if (DataStoreManager.getUser() != null) {
            String id = DataStoreManager.getUser().getId();
            final String passWord = DataStoreManager.getUser().getPassWord();
            ModelManager.getProfile(self, id, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    try {
                        JSONObject jsonObject = new JSONObject(object.toString());
                        ApiResponse response = new ApiResponse(jsonObject);
                        if (!response.isError()) {
                            UserObj userObj = response.getDataObject(UserObj.class);
                            userObj.setToken(DataStoreManager.getUser().getToken());
                            userObj.setPassWord(passWord);
                            userObj.setRememberMe(DataStoreManager.getUser().isRememberMe());
                            //tvMoney.setText("POINT " + StringUtil.convertNumberToString(userObj.getPoint(), 0));


                            DataStoreManager.saveUser(userObj);
                            AppController.getInstance().setUserUpdated(true);
                        } else {
                            Toast.makeText(self, response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError() {

                }
            });

        }
    }
}

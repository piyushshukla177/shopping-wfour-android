package com.wfour.onlinestoreapp;

import android.app.Application;
import android.location.Location;

import com.quickblox.auth.session.QBSettings;
import com.sendbird.android.SendBird;
import com.wfour.onlinestoreapp.datastore.BaseDataStore;
import com.wfour.onlinestoreapp.interfaces.IRedeem;
import com.wfour.onlinestoreapp.network.VolleyRequestManager;
import com.wfour.onlinestoreapp.objects.DealCateObj;
import com.wfour.onlinestoreapp.quickblox.conversation.utils.QBResRequestExecutor;

import java.util.ArrayList;


/**
 * Created by Suusoft on 1/8/2016.
 */
public class AppController extends Application {
    private static final String APP_ID = "9F610332-B2C1-4BF2-A283-F32B8CBD0411"; // US-1 Demo
    public static final String VERSION = "3.0.117";

    private static final String TAG = AppController.class.getSimpleName();

    private static AppController instance;

    public static boolean fromMainToSellFragment = true;


    private Location myLocation;
    private ArrayList<DealCateObj> mDealCates;

    private QBResRequestExecutor qbResRequestExecutor;

    public static AppController getInstance() {
        return instance;
    }

    // true if user is updated.
    private boolean isUserUpdated;

    private IRedeem iRedeem;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        BaseDataStore.init(this);
        isUserUpdated = true;
        initCredentials();
        ActivityLifecycle.init(this);
        VolleyRequestManager.init(this);
        SendBird.init(APP_ID, getApplicationContext());

    }

    public Location getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(Location myLocation) {
        this.myLocation = myLocation;
    }

    // Init Quickblox credentials
    private void initCredentials() {
        QBSettings.getInstance().init(getApplicationContext(),
                getResources().getString(R.string.QB_APP_ID) ,
                getResources().getString(R.string.QB_AUTH_KEY),
                getResources().getString(R.string.QB_AUTH_SECRET));
        QBSettings.getInstance().setAccountKey(
                getResources().getString(R.string.QB_ACCOUNT_KEY));
        QBSettings.getInstance().setEnablePushNotification(true);
    }


    public Double getLatMyLocation() {
        if (myLocation == null)
            return 0.0;
        return myLocation.getLatitude();
    }

    public Double getLongMyLocation() {
        if (myLocation == null)
            return 0.0;
        return myLocation.getLongitude();
    }

    public ArrayList<DealCateObj> getDealCategories() {
        if (mDealCates == null) {
            mDealCates = new ArrayList<>();
            // init and get categories of deal

//            DealCateObj dealCateObj = new DealCateObj(DealCateObj.MY_FAVORITES, this.getString(R.string.my_favorites),this.getString(R.string.des_my_favourite));
//            mDealCates.add(dealCateObj);
//            dealCateObj = new DealCateObj(DealCateObj.FOOD_AND_BEVERAGES, this.getString(R.string.food_beverages),this.getString(R.string.des_foodandbever));
//            mDealCates.add(dealCateObj);
//            dealCateObj = new DealCateObj(DealCateObj.LABOR, this.getString(R.string.labor), R.drawable.ic_labor_white );
//            mDealCates.add(dealCateObj);
//            dealCateObj = new DealCateObj(DealCateObj.TRAVEL, this.getString(R.string.travel_hotel), R.drawable.ic_hotel_white_24dp);
//            mDealCates.add(dealCateObj);
//            dealCateObj = new DealCateObj(DealCateObj.SHOPPING, this.getString(R.string.shopping), R.drawable.ic_shop_white_24dp);
//            mDealCates.add(dealCateObj);
//            dealCateObj = new DealCateObj(DealCateObj.NEWS_AND_EVENTS, this.getString(R.string.news_and_events), R.drawable.ic_event_white_24dp);
//            mDealCates.add(dealCateObj);
//            dealCateObj = new DealCateObj(DealCateObj.OTHER, this.getString(R.string.other_deals), R.drawable.ic_other_white_24dp );
//            mDealCates.add(dealCateObj);

            DealCateObj dealCateObj = new DealCateObj(DealCateObj.LABOR, this.getString(R.string.labor), R.drawable.bg_item_labor ,this.getString(R.string.description_labor));
            mDealCates.add(dealCateObj);
            dealCateObj = new DealCateObj(DealCateObj.TRAVEL, this.getString(R.string.travel_hotel), R.drawable.bg_item_hotel , this.getString(R.string.description_hotel));
            mDealCates.add(dealCateObj);
            dealCateObj = new DealCateObj(DealCateObj.SHOPPING, this.getString(R.string.shopping), R.drawable.bg_item_shopping , this.getString(R.string.description_shopping));
            mDealCates.add(dealCateObj);
            dealCateObj = new DealCateObj(DealCateObj.NEWS_AND_EVENTS, this.getString(R.string.news_and_events), R.drawable.bg_item_new_event, this.getString(R.string.description_beauty));
            mDealCates.add(dealCateObj);
            dealCateObj = new DealCateObj(DealCateObj.FOOD_AND_BEVERAGES, this.getString(R.string.food_beverages), this.getString(R.string.des_foodandbever));
            mDealCates.add(dealCateObj);
            dealCateObj = new DealCateObj(DealCateObj.OTHER, this.getString(R.string.other_deals), R.drawable.bg_item_orther  , this.getString(R.string.description_orther));
            mDealCates.add(dealCateObj);

        }
        return mDealCates;
    }

    public ArrayList<DealCateObj> getListCateForCreateNewDeals() {

        ArrayList<DealCateObj> mDealCates = new ArrayList<>();

        DealCateObj dealCateObj = new DealCateObj("0", this.getString(R.string.chose_category), R.drawable.bg_item_labor ,this.getString(R.string.description_labor));
        mDealCates.add(dealCateObj);
        dealCateObj = new DealCateObj(DealCateObj.LABOR, this.getString(R.string.labor), R.drawable.bg_item_labor ,this.getString(R.string.description_labor));
        mDealCates.add(dealCateObj);
        dealCateObj = new DealCateObj(DealCateObj.FOOD_AND_BEVERAGES, this.getString(R.string.food_beverages),this.getString(R.string.des_foodandbever));
        mDealCates.add(dealCateObj);
        dealCateObj = new DealCateObj(DealCateObj.TRAVEL, this.getString(R.string.travel_hotel), R.drawable.bg_item_hotel , this.getString(R.string.description_hotel));
        mDealCates.add(dealCateObj);
        dealCateObj = new DealCateObj(DealCateObj.SHOPPING, this.getString(R.string.shopping), R.drawable.bg_item_shopping , this.getString(R.string.description_shopping));
        mDealCates.add(dealCateObj);
        dealCateObj = new DealCateObj(DealCateObj.NEWS_AND_EVENTS, this.getString(R.string.news_and_events), R.drawable.bg_item_new_event , this.getString(R.string.description_beauty));
        mDealCates.add(dealCateObj);
        dealCateObj = new DealCateObj(DealCateObj.OTHER, this.getString(R.string.other_deals), R.drawable.bg_item_orther  , this.getString(R.string.description_orther));
        mDealCates.add(dealCateObj);

        return mDealCates;
    }

    public void setDealCategories(ArrayList<DealCateObj> dealCategories) {
        this.mDealCates = dealCategories;
    }

    public synchronized QBResRequestExecutor getQbResRequestExecutor() {
        return qbResRequestExecutor == null
                ? qbResRequestExecutor = new QBResRequestExecutor()
                : qbResRequestExecutor;
    }

    public boolean isUserUpdated() {
        return isUserUpdated;
    }

    public void setUserUpdated(boolean userUpdated) {
        isUserUpdated = userUpdated;
    }

    public IRedeem getiRedeem() {
        return iRedeem;
    }

    public void setiRedeem(IRedeem iRedeem) {
        this.iRedeem = iRedeem;
    }
}

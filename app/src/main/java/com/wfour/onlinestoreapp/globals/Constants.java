package com.wfour.onlinestoreapp.globals;

/**
 * Created by Suusoft on 01/20/2016.
 */
public class Constants {

    public static final int TIME_TO_UPDATE = 30 * 1000; // 30 seconds
    public static final int COLUM_GRID = 2;
    public static final int COLUM_GIRD_DEAL = 2;
    public static final int COLUM_LIST_DEAL = 1;

    //id menu left
    public static final int MENU_ALL_DEAL   = 0;
   // public static final int MENU_FAVORITE   = 1;
    public static final int MENU_BILL       = 1;
   // public static final int MENU_CHAT       = 2;
    //public static final int MENU_BUYER_NAMAGER = 3;
    public static final int MENU_SELLER     = 4;
    public static final int MENU_PAYMENT    = 5;
    public static final int MENU_PROFILE    = 6;

    public static final int MENU_WHISLIST  = 7;
    public static final int MENU_SHARE      = 8;
    public static final int MENU_FAQ        = 9;
    public static final int MENU_SETTING    = 10;
    public static final int MENU_HELP       = 11;
    public static final int MENU_ABOUT_US   = 12;
    public static final int MENU_LOGOUT     = 13;
    public static final int MENU_CHAT       = 14;


    // Maps
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME = "com.google.android.gms.location.sample.locationaddress";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    public static final String SEARCH_ALL = "all";
    public static final String SEARCH_NEARBY = "nearby";
    public static final String SEARCH_MINE = "mine";
    public static final String SEARCH_FAVORIES = "favourite";
    public static final String REVIEWED = "review";
    public static final String ACTIVE = "active";
    public static final String SOLD = "sold";
    public static final String BOUGH = "bought";
    public static final String NO_DEALS = "nodeal";

    public static final String PASSENGER = "passenger";
    public static final String DRIVER = "driver";
    public static final String SELLER = "seller";
    public static final String BUYER = "buyer";
    public static final String BASIC = "basic";
    public static final String PRO = "pro";
    public static final String ALL_REVIEWS = "user";
    public static final String TRIP = "trip";
    public static final String DEAL = "deal";

    public static final String PAGE = "page";
    public static final String TOTAL_PAGE = "total_page";

    //    web
    public static final String TERMS_AND_CONDITIONS = "https://www.google.com.vn/?gfe_rd=cr&ei=GuBTWKneIbPY8geejLyoBQ";

    // Driver status available/unavailable
    public static final String ON = "on";
    public static final String OFF = "off";
    public static final String DURATION_BUYING = "buy";
    public static final String KEY_URL = "url";
    public static final String KEY_TITLE = "tile";
    public static final int LIMIT_YEAR = 2000;

    public static final String SORT_ASC = "ASC";
    public static final String SORT_DESC = "DESC";
    public static final String SORT_DISTANCE = "distance";
    public static final String SORT_RATE = "rate";
    public static final String SORT_NAME = "name";
    public static final String SORT_PRICE = "sale_price";

    public static final String ACTION_ADD = "add";
    public static final String ACTION_REMOVE = "remove";

    public static final String MANUAL = "manual";
    public static final String AUTO = "auto";

    public static final String PERCENT = "percent";
    public static final String AMOUNT = "amount";

    public static final String INTENT_ACTION_UPDATE_MENU = "updateMenu";


    public static final String STR_TAG_OPEN = "[";
    public static final String STR_TAG_CLOSE = "]";

    public class Caching {
        public static final String KEY_REQUEST = "request";
        public static final String KEY_RESPONSE = "response";
        public static final String KEY_TIME_UPDATED = "time_updated";
        public static final String CACHING_PARAMS_TIME_REQUEST = "caching_time_request";
    }
}

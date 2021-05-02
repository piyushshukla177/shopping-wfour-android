package com.wfour.onlinestoreapp.configs;
import com.wfour.onlinestoreapp.AppController;
import com.wfour.onlinestoreapp.R;
public class Apis {
     public static final int REQUEST_TIME_OUT = 15000;
    public static final String APP_DOMAIN = AppController.getInstance().getString(R.string.URL_API)  + "backend/web/index.php/api/";

    // category
    public static final String CATEGORY = APP_DOMAIN + "category/list";

    // product
    public static final String URL_HOME_LIST = APP_DOMAIN + "ecommerce/home-list";
    public static final String URL_BANNER_LIST = APP_DOMAIN + "ecommerce/banner-list";
    public static final String URL_GET_PRODUCT_LIST = APP_DOMAIN + "ecommerce/product-list";
    public static final String URL_LOGIN = APP_DOMAIN  + "user/login";
    public static final String URL_ORDER_PRODUCT = APP_DOMAIN + "ecommerce/order";
    public static final String URL_PRODUCT_BILL = APP_DOMAIN + "ecommerce/order-history";
    public static final String URL_ORDER_HISTORY_DETAIL = APP_DOMAIN + "ecommerce/order-history-detail";
    public static final String URL_ORDER_UPDATE = APP_DOMAIN + "ecommerce/order-update";
    public static final String URL_DELIVERY_LIST= APP_DOMAIN + "ecommerce/delivery-list";
    public static final String URL_PRODUCT_FILTER = APP_DOMAIN + "ecommerce/product-filter";
    public static final String URL_SEARCH = APP_DOMAIN + "ecommerce/product-list";
    public static final String URL_FAVORITED = APP_DOMAIN + "favourite/index";
    public static final String URL_GETFAVORITED = APP_DOMAIN + "deal/detail";
    public static final String URL_GET_LIST_FAVORITE = APP_DOMAIN + "favourite/list";
    public static final String URL_REMOVE_LIST_FAVORITE = APP_DOMAIN + "favourite/remove";

    private static final String USER = "user/";
    private static final String DEAL = "deal/";
    private static final String TRANSPORT = "transport/";
    private static final String RESERVATION = "reservation";
    private static final String TRANSACTION = "transaction";
    private static final String UTILITY = "utility";
    private static final String CONTACT = "friend";

    public static final String URL_GET_FACEBOOK_INFO = "https://graph.facebook.com/me";

    public static final String URL_REGISTER_DEVICE = APP_DOMAIN + "device";
    public static final String URL_REGISTER_NORMAL_ACCOUNT = APP_DOMAIN + USER + "register";

    //public static final String URL_LOGIN = APP_DOMAIN + USER + "login";

    // deal
    public static final String URL_DEAL_ACTION = APP_DOMAIN + DEAL;
    public static final String URL_GET_DEAL_LIST = APP_DOMAIN + DEAL + "list";
    public static final String URL_GET_DEAL_DETAIL = APP_DOMAIN + DEAL + "detail";
    public static final String URL_FAVORITE = APP_DOMAIN + "favourite";
    public static final String URL_ACTIVATE_DEAL = APP_DOMAIN + DEAL + "switch";
    public static final String URL_UPDATE_DEAL_DURATION = APP_DOMAIN + DEAL + "update";

    // profile
    public static final String URL_UPDATE_PROFILE = APP_DOMAIN + "user/update-profile";
    public static final String URL_UPDATE_PROFILE_NORMAL = APP_DOMAIN + USER + "update-profile";
    public static final String URL_UPDATE_PROFILE_PRO = APP_DOMAIN + USER + "update-pro-profile";
    public static final String URL_PROFILE = APP_DOMAIN + "user/profile";
    public static final String URL_CHANGE_PASS_WORD = APP_DOMAIN + "user/change-password";
    public static final String URL_GET_PROFILE_DRIVER = APP_DOMAIN + USER + "profile";
    public static final String URL_FORGET_PASSWORD = APP_DOMAIN + USER + "forget-password";

    //review
    public static final String URL_POST_REVIEW = APP_DOMAIN + "review";
    public static final String URL_GET_REVIEW = APP_DOMAIN + "review/list";

    public static final String URL_RESERVATION_ACTION = APP_DOMAIN + RESERVATION;
    public static final String URL_GET_RESERVATION = APP_DOMAIN + RESERVATION + "/list";


    // Transport
    public static final String URL_GET_NEARBY_DRIVERS = APP_DOMAIN + TRANSPORT + "driver-list";
    public static final String URL_ACTIVATE_DRIVER_MODE = APP_DOMAIN + TRANSPORT + "online";
    public static final String URL_GET_TRIP_HISTORY = APP_DOMAIN + TRANSPORT + "trip-list";
    public static final String URL_TRIP_ACTION = APP_DOMAIN + TRANSPORT + "trip";
    public static final String URL_DELETE_ALL_TRIP = APP_DOMAIN + TRANSPORT + "trip-bulk-delete";

    //    settings
    public static final String URL_SETTINGS = APP_DOMAIN + USER + "setting";

    //    transaction
    public static final String URL_TRANSACTION = APP_DOMAIN + TRANSACTION;
    public static final String URL_HISTORY = APP_DOMAIN + TRANSACTION + "/list";
    public static final String URL_DELETE_HISTORY = APP_DOMAIN + TRANSACTION + "/delete";
    public static final String URL_GET_ACCOUNTING = APP_DOMAIN + TRANSACTION + "/accounting";
    //    utitlity
    public static final String URL_SETTING_UTILITY = APP_DOMAIN + UTILITY + "/setting";
    public static final String URL_UPDATE_LOCATION = APP_DOMAIN + UTILITY + "/update-location";
    public static final String URL_GET_DRIVER_LOCATION = APP_DOMAIN + TRANSPORT + "track-driver";

    public static final String URL_GET_CONTACTS = APP_DOMAIN + CONTACT + "/list";
    public static final String URL_CONTACTS_ACTION = APP_DOMAIN + CONTACT;

    public static final String URL_GET_ADDRESS_BY_LATLNG = "http://maps.googleapis.com/maps/api/geocode/json";
    public static final String URL_GET_SUBCATEGORY = APP_DOMAIN + "category/list-sub";
}

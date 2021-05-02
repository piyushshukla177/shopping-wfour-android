package com.wfour.onlinestoreapp.network.modelmanager;

import android.content.Context;

import com.wfour.onlinestoreapp.configs.Apis;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.network.BaseRequest;

import java.util.HashMap;

/**
 * Created by SuuSoft on 3/13/2018.
 */

public class RequestManger extends BaseRequest {

    private static final String PARAM_TOKEN = "token";
    private static final String PARAM_OBJECT_ID = "object_id";
    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_SEARCH_TYPE = "search_type";
    private static final String PARAM_IS_ONLINE = "is_online";
    private static final String PARAM_CATEGORY_ID = "category_id";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_LAT = "lat";
    private static final String PARAM_LONG = "long";
    private static final String PARAM_IS_PREMIUM = "is_premium";
    private static final String PARAM_ACTION = "action";
    private static final String PARAM_ACTION_CREATE = "create";
    private static final String PARAM_ACTION_UPDATE = "update";
    private static final String PARAM_ACTION_ONLINE = "online";
    private static final String PARAM_DURATION = "duration";
    private static final String PARAM_ESTIMATE_DURATION = "estimate_duration";
    private static final String PARAM_PRICE = "price";
    public static final String PARAM_DISCOUNT_TYPE = "discount_type";
    private static final String PARAM_DISCOUNT_PRICE = "discount";
    public static final String PARAM_DISCOUNT_PERCENT = "discount_rate";
    private static final String PARAM_AUTO_RENEW = "is_renew";
    private static final String PARAM_KEY_WORD = "keyword";
    private static final String PARAM_DISTANCE = "distance";
    private static final String PARAM_ESTIMATE_DISTANCE = "estimate_distance";
    private static final String PARAM_NUM_PER_PAGE = "number_per_page";
    private static final String PARAM_OBJECT_TYPE = "object_type";
    public static final String FAVORITE_TYPE_DEAL = "deal";
    public static final String PARAM_SORT_BY = "sort_by";
    public static final String PARAM_SORT_TYPE = "sort_type";

    private static final String PARAM_DESTINATION_ID = "destination_id";
    private static final String PARAM_AUTHOR_ROLE = "author_role";
    private static final String PARAM_DESTINATION_ROLE = "destination_role";
    private static final String PARAM_CONTENT = "content";
    private static final String PARAM_RATE = "rate";
    private static final String PARAM_ATTACHED = "attachment";


    private static final String PARAM_NEW_PASS = "new_password";
    private static final String PARAM_CURRENT_PASS = "current_password";
    private static final String PARAM_START_LATITUDE = "start_lat";
    private static final String PARAM_END_LATITUDE = "end_lat";
    private static final String PARAM_START_LONGITUDE = "start_long";
    private static final String PARAM_END_LONGITUDE = "end_long";
    private static final String PARAM_DEAL_ID = "deal_id";
    private static final String PARAM_DEAL_NAME = "deal_name";
    private static final String PARAM_BUYER_ID = "buyer_id";
    private static final String PARAM_BUYER_NAME = "buyer_name";
    private static final String PARAM_RESERVATION_ID = "reservation_id";
    //    settings
    // Params
    private static final String PARAM_ADDRESS = "address";

    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_IME = "ime";
    private static final String PARAM_GCM_ID = "gcm_id";
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_STATUS = "status";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_IMAGE = "image";
    private static final String PARAM_PHONE = "phone";
    private static final String PARAM_DESCRIPTION = "description";
    private static final String PARAM_FIELDS = "fields";
    private static final String PARAM_ACCESS_TOKEN = "access_token";
    private static final String PARAM_LOGIN_METHOD = "login_type";

    public static void createDeal(Context ctx, String categoryId, String idDeal, String name, String price, String typeDiscount, float discount, String address, String duration,
                                  String description, double latitude, double longitude, int isPremium, int renew, String  image, String file,
                                  final CompleteListener listener) {
        String url = Apis.URL_DEAL_ACTION;
        HashMap<String, String> params = new HashMap<>();
        if (idDeal != null) {
            params.put(PARAM_DEAL_ID, idDeal);
            params.put(PARAM_ACTION, PARAM_ACTION_UPDATE);
        } else {
            params.put(PARAM_ACTION, PARAM_ACTION_CREATE);
        }
        params.put(PARAM_TOKEN, DataStoreManager.getUser().getToken());

        params.put(PARAM_NAME, name);
        params.put(PARAM_DESCRIPTION, description);
        params.put(PARAM_PRICE, price);
        params.put(PARAM_DISCOUNT_TYPE, typeDiscount);

        params.put(PARAM_DISCOUNT_PRICE, String.valueOf(discount));


        params.put(PARAM_ADDRESS, address);
        if (duration != null && !duration.isEmpty()) {
            params.put(PARAM_DURATION, duration);
        }
        params.put(PARAM_IS_PREMIUM, String.valueOf(isPremium));
        params.put(PARAM_AUTO_RENEW, String.valueOf(renew));
        params.put(PARAM_LAT, String.valueOf(latitude));
        params.put(PARAM_LONG, String.valueOf(longitude));
        params.put(PARAM_CATEGORY_ID, categoryId);

        // file
        HashMap<String, String> fileList = new HashMap<>();

        fileList.put(PARAM_IMAGE, image);
       // fileList.put(PARAM_ATTACHED, file);

        multipart(url,params, fileList, true, listener );

    }

    public static void addFavorite(String user_id, String object_id, String object_type, CompleteListener completeListener){
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_USER_ID , user_id);
        params.put(PARAM_OBJECT_ID, object_id+"");
        params.put(PARAM_OBJECT_TYPE, object_type);
        get(Apis.URL_FAVORITED, params,  completeListener);
    }
    public static void getFavorite(String user_id, String deal_id,  CompleteListener completeListener){
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_TOKEN , user_id);
        params.put(PARAM_DEAL_ID, deal_id);

        get(Apis.URL_GETFAVORITED, params,  completeListener);
    }
    public static void getListFavorite(String user_id, String type, int page, int number_per_page, CompleteListener completeListener){
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_USER_ID, user_id);
        params.put(PARAM_TYPE, type);
        params.put(PARAM_PAGE, page+"");
        params.put(PARAM_NUM_PER_PAGE, number_per_page+"");
        get(Apis.URL_GET_LIST_FAVORITE, params, completeListener);
    }

    public static void removeFavorite(String user_id, String object_id, String object_type, CompleteListener completeListener){
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_USER_ID , user_id);
        params.put(PARAM_OBJECT_ID, object_id+"");
        params.put(PARAM_OBJECT_TYPE, object_type);
        get(Apis.URL_REMOVE_LIST_FAVORITE, params,  completeListener);
    }
}

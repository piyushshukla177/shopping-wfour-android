package com.wfour.onlinestoreapp.parsers;

import com.google.android.gms.maps.model.LatLng;
import com.quickblox.users.model.QBUser;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.objects.ContactObj;
import com.wfour.onlinestoreapp.objects.RevenueObj;
import com.wfour.onlinestoreapp.objects.TransportDealObj;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Suusoft on 10/07/2015.
 */
public class JSONParser {

    private static final String TAG = JSONParser.class.getSimpleName();

    // Common keys
    private static final String KEY_STATUS = "status";
    private static final String KEY_DATA = "data";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_TOTAL_PAGE = "total_page";
    private static final String VALUE_SUCCESS = "success";

    // Keys
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String IMAGE = "image";
    private static final String AVATAR = "avatar";
    private static final String DESCRIPTION = "description";
    private static final String TIME_WAITING = "time_waiting";
    private static final String DATE_TIME = "date";
    private static final String STATUS = "status";
    private static final String TYPE = "type";
    private static final String DRIVER_TYPE = "driver_type";
    private static final String DRIVER_IS_DELIVERY = "driver_is_delivery";
    private static final String LAST_NAME = "last_name";
    private static final String ROOM_NO = "room_no";
    private static final String URL = "url";
    private static final String LATITUDE = "lat";
    private static final String LONGITUDE = "long";
    private static final String RATE = "rate";
    private static final String ESTIMATE_FARE = "estimate_fare";
    private static final String FARE = "fare";
    private static final String ACTUAL_FARE = "actual_fare";
    private static final String RATE_COUNT = "rate_count";
    private static final String IS_DELIVERY = "is_delivery";
    private static final String EMAIL = "email";
    private static final String BUSINESS_PHONE = "business_phone";
    private static final String QB_ID = "qb_id";
    private static final String DURATION = "duration";
    private static final String ESTIMATE_DURATION = "estimate_duration";
    private static final String DISTANCE = "distance";
    private static final String ESTIMATE_DISTANCE = "estimate_distance";
    private static final String START_LOCATION = "start_location";
    private static final String END_LOCATION = "end_location";
    private static final String CREATED_DATE = "created_date";
    private static final String PAYMENT_METHOD = "payment_method";
    private static final String PAYMENT_STATUS = "payment_status";
    private static final String REVENUE = "revenue";
    private static final String EXPENSE = "expense";
    private static final String TIME = "time";
    private static final String START_LAT = "start_lat";
    private static final String START_LONG = "start_long";
    private static final String END_LAT = "end_lat";
    private static final String END_LONG = "end_long";
    private static final String DRIVER_ID = "driver_id";
    private static final String DRIVER_QB_ID = "driver_qb_id";
    private static final String PASSENGER_QB_ID = "passenger_qb_id";
    private static final String DRIVER_NAME = "driver_name";
    private static final String DRIVER_PHONE = "driver_phone";
    private static final String FRIEND_ID = "friend_id";
    private static final String PHONE = "phone";

    public static boolean responseIsSuccess(JSONObject response) {
        return response.optString(KEY_STATUS).equalsIgnoreCase(VALUE_SUCCESS);
    }

    public static String getMessage(JSONObject response) {
        return response.optString(KEY_MESSAGE);
    }

    public static int getTotalPage(JSONObject response) {
        return response.optInt(KEY_TOTAL_PAGE);
    }

    public static boolean isEnded(ApiResponse response, int curPage) {
        int totalPage = Integer.parseInt(response.getValueFromRoot(Constants.TOTAL_PAGE));
        return totalPage == 0 || curPage >= totalPage;
    }


    public static ArrayList<TransportDealObj> parseDrivers(JSONObject jsonObject) {
        ArrayList<TransportDealObj> arr = new ArrayList<>();

        try {
            JSONArray datum = jsonObject.getJSONArray(KEY_DATA);
            if (datum.length() > 0) {
                for (int i = 0; i < datum.length(); i++) {
                    JSONObject obj = datum.getJSONObject(i);

                    TransportDealObj transportDealObj = new TransportDealObj();
                    transportDealObj.setDriverName(obj.optString(NAME));
                    transportDealObj.setTransportType(obj.optString(TYPE));
                    transportDealObj.setDuration(obj.optInt(DURATION));
                    transportDealObj.setDistance(obj.optDouble(DISTANCE));

                    LatLng latLngDriver = new LatLng(obj.optDouble(LATITUDE), obj.optDouble(LONGITUDE));
                    transportDealObj.setDriverId(obj.optString(ID));
                    transportDealObj.setLatLngDriver(latLngDriver);
                    transportDealObj.setRateOfDriver((float) obj.optDouble(RATE));
                    transportDealObj.setRateQuantity(obj.optInt(RATE_COUNT));
                    transportDealObj.setDriverIsDelivery(obj.optString(IS_DELIVERY).equals("1"));
                    transportDealObj.setDriverEmail(obj.optString(EMAIL));
                    transportDealObj.setDriverPhone(obj.optString(BUSINESS_PHONE));
                    transportDealObj.setDriverQBId(obj.optInt(QB_ID));

                    transportDealObj.setRouteDistance(jsonObject.optDouble(DISTANCE));
                    transportDealObj.setRouteDuration(jsonObject.optInt(DURATION));

                    float estimateFare = (float) (obj.optDouble(FARE) * (jsonObject.optDouble(DISTANCE) / 1000));
                    transportDealObj.setEstimateFare(estimateFare);

                    arr.add(transportDealObj);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arr;
    }

    public static ArrayList<TransportDealObj> parseTrips(JSONObject jsonObject) {
        ArrayList<TransportDealObj> arr = new ArrayList<>();

        try {
            JSONArray datum = jsonObject.getJSONArray(KEY_DATA);
            if (datum.length() > 0) {
                for (int i = 0; i < datum.length(); i++) {
                    JSONObject obj = datum.getJSONObject(i);

                    TransportDealObj transportDealObj = new TransportDealObj();
                    transportDealObj.setId(obj.optString(ID));
                    transportDealObj.setActualFare((float) obj.optDouble(ACTUAL_FARE));
                    transportDealObj.setEstimateFare((float) obj.optDouble(ESTIMATE_FARE));

                    transportDealObj.setPickup(obj.optString(START_LOCATION));
                    LatLng latLngPickup = new LatLng(obj.optDouble(START_LAT), obj.optDouble(START_LONG));
                    transportDealObj.setLatLngPickup(latLngPickup);

                    transportDealObj.setDestination(obj.optString(END_LOCATION));
                    LatLng latLngDestination = new LatLng(obj.optDouble(END_LAT), obj.optDouble(END_LONG));
                    transportDealObj.setLatLngDestination(latLngDestination);

                    transportDealObj.setDriverId(obj.optString(DRIVER_ID));
                    transportDealObj.setDriverQBId(obj.optInt(DRIVER_QB_ID));
                    transportDealObj.setPassengerQBId(obj.optInt(PASSENGER_QB_ID));
                    transportDealObj.setDriverName(obj.optString(DRIVER_NAME));
                    transportDealObj.setDriverPhone(obj.optString(DRIVER_PHONE));
                    transportDealObj.setStatus(obj.optString(STATUS));
                    transportDealObj.setTime(obj.optLong(TIME));
                    transportDealObj.setDistance(0);
                    transportDealObj.setRouteDistance(obj.optDouble(ESTIMATE_DISTANCE));
                    transportDealObj.setRouteDuration(obj.optInt(ESTIMATE_DURATION));
                    transportDealObj.setPaymentMethod(obj.optString(PAYMENT_METHOD));
                    transportDealObj.setPaymentStatus(obj.optString(PAYMENT_STATUS));
                    transportDealObj.setTransportType(obj.optString(DRIVER_TYPE));
                    transportDealObj.setDriverIsDelivery(obj.optString(DRIVER_IS_DELIVERY).equals("1"));

                    arr.add(transportDealObj);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arr;
    }

    public static ArrayList<RevenueObj> parseRevenues(JSONObject jsonObject) {
        ArrayList<RevenueObj> arr = new ArrayList<>();

        try {
            JSONArray datum = jsonObject.getJSONArray(KEY_DATA);
            if (datum.length() > 0) {
                for (int i = 0; i < datum.length(); i++) {
                    JSONObject obj = datum.getJSONObject(i);

                    RevenueObj revenueObj = new RevenueObj(Float.parseFloat(obj.optString(REVENUE)),
                            Float.parseFloat(obj.optString(EXPENSE)));

                    arr.add(revenueObj);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arr;
    }

    public static ArrayList<ContactObj> parseContacts(JSONObject jsonObject) {
        ArrayList<ContactObj> arr = new ArrayList<>();

        try {
            JSONArray datum = jsonObject.getJSONArray(KEY_DATA);
            if (datum.length() > 0) {
                for (int i = 0; i < datum.length(); i++) {
                    JSONObject obj = datum.getJSONObject(i);

                    QBUser qbUser = new QBUser();
                    qbUser.setId(obj.optInt(QB_ID));
//                    qbUser.setId(80813);
                    qbUser.setLogin(obj.optString(EMAIL));
                    qbUser.setFullName(obj.optString(NAME));
                    qbUser.setPhone(obj.optString(PHONE));
                    ContactObj contactObj = new ContactObj(obj.optString(AVATAR), obj.optString(FRIEND_ID), qbUser);

                    arr.add(contactObj);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arr;
    }
}

package com.wfour.onlinestoreapp.base;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wfour.onlinestoreapp.AppController;
import com.wfour.onlinestoreapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * created by Suusoft
 *
 */

public class ApiResponse {
    public static final String KEY_NAME_ERROR = "error";
    public static final String KEY_NAME_CODE = "code";
    public static final String KEY_NAME_MESSAGE = "message";
    public static final String KEY_NAME_STATUS = "status";
    public static final String KEY_NAME_SUCCESS = "SUCCESS";
    public static final String KEY_NAME_DATA = "data";
    public static final String KEY_TOKEN = "login_token";

    public static final int ERROR_CODE_NOERROR = 0;
    public static final int ERROR_CODE_UNKOWN = -9000;
    public static final int ERROR_CODE_JSON_ERROR = -9001;
    public static final int ERROR_CODE_INVALID_RESPONSE_FORMAT = -9002;
    public static final int ERROR_CODE_REQUEST_TIMEOUT = -9003;
    public static final int ERROR_CODE_NOT_ENOUGH_MONEY = 412;

    private int code;
    private boolean isError;
    private String message;
    private String login_token;
    private JSONObject data;

    public ApiResponse(int code, String message) {
        this.setCode(code);
        this.message = message != null ? message : "";
        this.isError = code != ERROR_CODE_NOERROR;
    }

    public ApiResponse(JSONObject json) {
        if (json == null) {
            this.isError = true;
            this.message = "Empty json";
            this.code = ERROR_CODE_JSON_ERROR;
        } else {
            try {
                if (json.getString(KEY_NAME_STATUS).equalsIgnoreCase(KEY_NAME_SUCCESS)) {
                    this.code = json.optInt(KEY_NAME_CODE,-1);
                    this.message = json.optString(KEY_NAME_MESSAGE, "Success");
                    this.data = json;
                } else {
                    this.code = json.optInt(KEY_NAME_CODE,-1);
                    this.isError = true;
                    this.message = json.optString(KEY_NAME_MESSAGE, "Error");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public String getMessageFromCode() {
        Context context = AppController.getInstance();
        String message = "Unknow";
        switch (code){
            case 200:
                message = context.getString(R.string.msg_err_success);
                break;
            case 201:
                message = context.getString(R.string.msg_err_fail);
                break;
            case 202:
                message = context.getString(R.string.msg_err_missing_params);
                break;
            case 203:
                message = context.getString(R.string.msg_err_invalid_input);

                break;
            case 204:
                message = context.getString(R.string.msg_err_token_missing);
                break;
            case 205:
                message = context.getString(R.string.msg_err_token_mismatch);

                break;
            case 206:
                message = context.getString(R.string.msg_err_can_not_perform);

                break;
            case 207:
                message = context.getString(R.string.msg_err_paid_fail);

                break;
            case 221:
                message = context.getString(R.string.msg_err_user_not_found);
                break;
            case 222:
                message = context.getString(R.string.msg_err_wrong_password);

                break;
            case 223:
                message = context.getString(R.string.msg_err_email_not_exist);

                break;
            case 224:
                message = context.getString(R.string.msg_err_email_or_user_not_exist);

                break;
            case 225:
                message = context.getString(R.string.msg_err_email_existed);

                break;
            case 226:
                message = context.getString(R.string.msg_err_email_user_existed);

                break;
            case 227:
                message = context.getString(R.string.msg_err_current_password_mismatch);

                break;
            case 228:
                message = context.getString(R.string.msg_err_your_account_not_actived);

                break;
            case 229:
                message = context.getString(R.string.msg_err_mail_fail);

                break;
            case 230:
                message = context.getString(R.string.msg_err_your_balance_not_enough);

                break;
            case 231:
                message = context.getString(R.string.msg_err_update_bussiness_profile);

                break;
            case 232:
                message = context.getString(R.string.msg_err_your_request_is_pending);

                break;
            case 241:
                message = context.getString(R.string.msg_err_deal_not_found);

                break;
            case 242:
                message = context.getString(R.string.msg_err_reservation_not_found);

                break;
            case 261:
                message = context.getString(R.string.msg_err_trip_not_found);

                break;
            case 262:
                message = context.getString(R.string.msg_err_driver_role_not_actived);

                break;
            case 263:
                message = context.getString(R.string.msg_err_update_profile_driver_first);

                break;
            case 264:
                message = context.getString(R.string.msg_err_need_charge_duration);

                break;
        }
        return message;
    }

    /**
     *  Method get object from data if data responde is a object
     *  @param tClass
     *  @return Object
     *
     */

    public <T> T getDataObject(Class<T> tClass) {
        JSONObject object = getDataObject();
        T obj = new GsonBuilder().create().fromJson(object.toString(),tClass);
        return obj;
    }

    /**
     *  This get list object from data if responde is array
     *  @param tClass
     *  @return List<Object>
     *
     */
    public <T> List<T> getDataList(Class<T> tClass) {
        List<T> listObj = new ArrayList<>();
        JSONArray arr = getDataArray();
        if (data != null) {
            JSONObject jo;
            T object;
            int size = arr.length();
            Gson gson = new GsonBuilder().create();
            for (int i = 0; i < size; i++) {
                jo = arr.optJSONObject(i);
                if (jo != null) {
                    object = gson.fromJson(jo.toString(),tClass);
                    listObj.add(object);
                }

            }
        }
        return listObj;
    }


    public JSONObject getDataObject() {
        return this.data != null ? this.data.optJSONObject(KEY_NAME_DATA) : null;
    }

    public JSONArray getDataArray() {
        return this.data != null ? this.data.optJSONArray(KEY_NAME_DATA) : null;
    }

    public String getValueFromRoot(String key){
        try {
            return this.data.getString(key);
        } catch (JSONException e) {
            Log.e("getValueFromRoot()","error: the key not exist");
            return "";
        }
    }

    public JSONObject getRoot() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isError() {
        return this.isError;
    }

    public String getLogin_token() {
        return login_token;
    }

    public void setLogin_token(String login_token) {
        this.login_token = login_token;
    }
}

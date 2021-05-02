package com.wfour.onlinestoreapp.network;

import android.net.Uri;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wfour.onlinestoreapp.AppController;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.datastore.BaseDataStore;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.network.multipart.MultiPartRequest;
import com.wfour.onlinestoreapp.utils.NetworkUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Suusoft
 * <p>
 * API Helper class.
 */
public class BaseRequest {
    public static final String TAG = BaseRequest.class.getSimpleName();
    public static final int METHOD_POST = com.android.volley.Request.Method.POST;
    public static final int METHOD_GET = com.android.volley.Request.Method.GET;
    public static final int METHOD_PUT = com.android.volley.Request.Method.PUT;
    public static final int METHOD_DELETE = com.android.volley.Request.Method.DELETE;

    // default params
    private static final String PARAM_TOKEN = "token";
    private static final String PARAM_USER_ID = "user_id";

    private static BaseRequest mInstance;

    public static BaseRequest getInstance() {
        if (mInstance == null) {
            mInstance = new BaseRequest();
        }

        return mInstance;
    }

    //********************************************************************************
    public interface ListenerLoading {
        void onLoadingIsProcessing();

        void onLoadingIsCompleted();
    }

    /**
     * interface for listening progress.
     * purpose is show and hide progressbar
     */
    private ListenerLoading listenerLoading;

    public ListenerLoading getListenerLoading() {
        return listenerLoading;
    }

    public void setListenerLoading(ListenerLoading listenerLoading) {
        this.listenerLoading = listenerLoading;
    }

    /**
     * show hide progress bar
     */
    private void showProgress(boolean isOpen) {
        if (isOpen && listenerLoading != null) {
            listenerLoading.onLoadingIsProcessing();
        }
    }

    public void hideProgress(boolean isOpen) {
        if (isOpen && listenerLoading != null) {
            listenerLoading.onLoadingIsCompleted();
        }
    }

    //*******************************************************************************

    /**
     * set default pamrams
     */
    private static void addDefaultParams(HashMap<String, String> params) {
//        String userId = DataStoreManager.getUser() != null ? DataStoreManager.getUser().getId() : "";
//        params.put(PARAM_USER_ID, userId);
//        params.put(PARAM_TOKEN, AppController.getInstance().getToken());
    }

    public static void get(String url, HashMap<String, String> params, final CompleteListener listener) {
        BaseRequest.getInstance().request(METHOD_GET, url, params, true, false, listener, null);
    }

    public static void get(String url, HashMap<String, String> params, boolean isShowProgress, final CompleteListener listener) {
        BaseRequest.getInstance().request(METHOD_GET, url, params, isShowProgress, false, listener, null);
    }

    public static void get(String url, HashMap<String, String> params, boolean isShowProgress, boolean isCaching, final CompleteListener listener) {
        BaseRequest.getInstance().request(METHOD_GET, url, params, isShowProgress, isCaching, listener, null);
    }

    public static void post(String url, HashMap<String, String> params, final CompleteListener listener) {
        BaseRequest.getInstance().request(METHOD_POST, url, params, true, false, listener, null);
    }

    public static void post(String url, HashMap<String, String> params, boolean isShowProgress, final CompleteListener listener) {
        BaseRequest.getInstance().request(METHOD_POST, url, params, isShowProgress, false, listener, null);
    }

    public static void post(String url, HashMap<String, String> params, boolean isShowProgress, boolean isCaching, final CompleteListener listener) {
        BaseRequest.getInstance().request(METHOD_POST, url, params, isShowProgress, isCaching, listener, null);
    }

    public static void multipart(String url, HashMap<String, String> textParams, HashMap<String, String> fileParams, boolean isShowProgress, final CompleteListener listener) {
        BaseRequest.getInstance().requestMultipart(url, textParams, fileParams, isShowProgress, listener, null);
    }

    /**
     * checking and excute request to api.
     * 1. Checking network connection. if network is connected to api
     * else get caching data if is on or notify that not connection
     *
     * @param method         get or post.
     * @param url            root url
     * @param params         params
     * @param isShowProgress show progressbar or not
     * @param isCaching      caching or not
     * @param listener       listener for request
     * @param tag            tag of request
     */
    public void request(final int method, String url, HashMap<String, String> params, final boolean isShowProgress, final boolean isCaching, final CompleteListener listener,
                        String tag) {
        if (NetworkUtility.isNetworkAvailable()) {
            showProgress(isShowProgress);
//            addDefaultParams(params);

            Log.d(TAG, "request params:" + params.toString());
            if (method == METHOD_GET) {
                Uri.Builder b = Uri.parse(url).buildUpon();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    b.appendQueryParameter(entry.getKey(), entry.getValue());
                }
                url = b.toString();
                params.clear();
            }

            Log.d(TAG, "starting request url:" + url);
            final String finalUrl = url;

            Listener<JSONObject> completeListener = new Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    hideProgress(isShowProgress);
                    Log.d(TAG, "RESULT: " + response);
                    if (listener != null) {
                        if (listener instanceof CompleteListener) {
                            ApiResponse apiResponse = new ApiResponse(response);
                            if (!apiResponse.isError()) {
                                listener.onSuccess(apiResponse);
                                // if caching = true. data will be cached. save it to db
                                if (isCaching) {
                                    String objectRoot = apiResponse.getRoot().toString();
                                    BaseDataStore.saveCaching(finalUrl, objectRoot, apiResponse.getValueFromRoot(Constants.Caching.KEY_TIME_UPDATED));
                                }

                            } else {
                                listener.onError(apiResponse.getMessage());
                            }
                        }

                    }
                }
            };
            ErrorListener errorListener = new ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    hideProgress(isShowProgress);
                    Log.e(TAG, "volley error: " + error.getClass());
                    if (listener != null) {
                        String errorMessage = "";
                        int code = ApiResponse.ERROR_CODE_UNKOWN;
                        if (error.getClass() == TimeoutError.class) {
                            code = ApiResponse.ERROR_CODE_REQUEST_TIMEOUT;
                            errorMessage = "Request timeout";
                        } else if (error.getClass() == com.android.volley.AuthFailureError.class) {
                            errorMessage = error.getMessage() != null ? error.getMessage() : "No internet permission ?";
                        } else {
                            errorMessage = error.getMessage();
                            if (errorMessage == null || errorMessage.length() == 0)
                                errorMessage = error.toString();
                            code = error.networkResponse != null ? error.networkResponse.statusCode
                                    : ApiResponse.ERROR_CODE_UNKOWN;
                        }

                        if (listener instanceof CompleteListener) {
                            if (!isCaching) {
                                listener.onError(new ApiResponse(code, errorMessage).getMessage());
                            } else {
                                getOfflineResponse(listener, finalUrl);
                            }
                        }

                    }
                }
            };

            ApiHelperJsonObjectRequest jr = new ApiHelperJsonObjectRequest(method, url, params, completeListener,
                    errorListener);
            if (tag != null) {
                jr.setTag(tag);
            }
//            jr.setShouldCache(false);
            jr.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleyRequestManager.getRequestQueue().add(jr);
        } else {
            if (isCaching) {
                addDefaultParams(params);
                getOfflineResponse(listener, getFullUrlGet(url, params));
            } else {
                Toast.makeText(AppController.getInstance(), AppController.getInstance().getString(R.string.msg_connection_network_error)
                        , Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * get url and params of url request to api
     *
     * @param url        root url
     * @param fullParams all params
     */
    private String getFullUrlGet(String url, HashMap<String, String> fullParams) {
        Uri.Builder b = Uri.parse(url).buildUpon();
        for (Map.Entry<String, String> entry : fullParams.entrySet()) {
            b.appendQueryParameter(entry.getKey(), entry.getValue());
        }
        url = b.toString();
        fullParams.clear();
        return url;
    }

    /**
     * get response from db. Filter by song_file (action's name)
     *
     * @param url
     */
    private void getOfflineResponse(CompleteListener listener, String url) {
        try {
            listener.onSuccess(new ApiResponse(new JSONObject(BaseDataStore.getCaching(url))));
        } catch (JSONException e) {
            e.printStackTrace();
            // if caching data is not loaded before
            listener.onError("No data");
        }
    }

    /**
     * Interface listener when request api
     */
    public interface CompleteListener {
        public void onSuccess(ApiResponse response);

        public void onError(String message);

    }

    /**
     * A request for retrieving a {@link JSONObject} response body at a given
     * URL. Use for posting params along with request, instead of posting body
     * like {@link JsonObjectRequest}
     */
    private class ApiHelperJsonObjectRequest extends JsonObjectRequest {
        Map<String, String> mParams;

        public ApiHelperJsonObjectRequest(int method, String url, Map<String, String> params,
                                          Listener<JSONObject> listener, ErrorListener errorListener) {
            super(method, url, null, listener, errorListener);
            mParams = params;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<String, String>();
            params.putAll(super.getHeaders());
            //params.put("apikey", new String(Constant.API_KEY));

            Log.d(TAG, "header params:" + params.toString());
            return params;
        }

        // override getBodyContentType and getBody for prevent posting body.
        @Override
        public String getBodyContentType() {
            return null;
        }

        @Override
        public byte[] getBody() {
            if (this.getMethod() == METHOD_POST && mParams != null && mParams.size() > 0) {
                return encodeParameters(mParams, getParamsEncoding());
            }
            return null;
        }

        /**
         * Converts <code>params</code> into an
         * application/x-www-form-urlencoded encoded string.
         */
        private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
            StringBuilder encodedParams = new StringBuilder();
            try {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                    encodedParams.append('=');
                    encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                    encodedParams.append('&');
                }
                return encodedParams.toString().getBytes(paramsEncoding);
            } catch (UnsupportedEncodingException uee) {
                throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
            }

        }

    }

    private void requestMultipart(String url, HashMap<String, String> paramsString, HashMap<String, String> paramsFile, final boolean isShowProgress, final CompleteListener listener, String tag) {
        if (NetworkUtility.isNetworkAvailable()) {
            showProgress(isShowProgress);
//            addDefaultParams(paramsString);


            Log.d(TAG, "starting request url:" + url);
            Listener<JSONObject> completeListener = new Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    hideProgress(isShowProgress);
                    Log.d(TAG, "RESULT: " + response);
                    if (listener != null) {
                        if (listener instanceof CompleteListener) {
                            ApiResponse apiResponse = new ApiResponse(response);
                            if (!apiResponse.isError()) {
                                listener.onSuccess(apiResponse);
                            } else {
                                listener.onError(apiResponse.getMessage());
                            }
                        }

                    }
                }
            };
            ErrorListener errorListener = new ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    hideProgress(isShowProgress);
                    Log.e(TAG, "volley error: " + error.getClass());
                    if (listener != null) {
                        String errorMessage = "";
                        int code = ApiResponse.ERROR_CODE_UNKOWN;
                        if (error.getClass() == TimeoutError.class) {
                            code = ApiResponse.ERROR_CODE_REQUEST_TIMEOUT;
                            errorMessage = "Request timeout";
                        } else if (error.getClass() == com.android.volley.AuthFailureError.class) {
                            errorMessage = error.getMessage() != null ? error.getMessage() : "No internet permission ?";
                        } else {
                            errorMessage = error.getMessage();
                            if (errorMessage == null || errorMessage.length() == 0)
                                errorMessage = error.toString();
                            code = error.networkResponse != null ? error.networkResponse.statusCode
                                    : ApiResponse.ERROR_CODE_UNKOWN;
                        }

                        if (listener instanceof CompleteListener) {
                            listener.onError(new ApiResponse(code, errorMessage).getMessage());
                        }

                    }
                }
            };

            JsonMultipartRequest jr = new JsonMultipartRequest(url, completeListener,
                    errorListener);

            // add string params
            for (Map.Entry<String, String> params : paramsString.entrySet()) {
                jr.addStringParam(params.getKey(), params.getValue());
            }

            // add file params
            for (Map.Entry<String, String> params : paramsFile.entrySet()) {
                jr.addFile(params.getKey(), params.getValue());
            }

            if (tag != null) {
                jr.setTag(tag);
            }
            jr.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleyRequestManager.getRequestQueue().add(jr);
        }
    }

    private class JsonMultipartRequest extends MultiPartRequest {
        /**
         * Creates a new request with the given method.
         *
         * @param url           URL to fetch the string at
         * @param listener      Listener to receive the String response
         * @param errorListener Error listener, or null to ignore errors
         */
        public JsonMultipartRequest(String url, Listener listener, ErrorListener errorListener) {
            super(Request.Method.POST, url, listener, errorListener);
        }

        @Override
        protected Response parseNetworkResponse(NetworkResponse response) {
            try {
                String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                return Response.success(new JSONObject(jsonString),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }

        @Override
        public int compareTo(@NonNull Object o) {
            return 0;
        }
    }

}

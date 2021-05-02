package com.wfour.onlinestoreapp.retrofit;

import com.wfour.onlinestoreapp.configs.Apis;

public class ApiUtils {

    public static final String BASE_URL = Apis.APP_DOMAIN;

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

}

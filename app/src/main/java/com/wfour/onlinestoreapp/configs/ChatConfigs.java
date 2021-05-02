package com.wfour.onlinestoreapp.configs;


public class ChatConfigs {

    // Quickblox credentials
    public static final String QB_ACCOUNT_KEY = "shtAacgHZoLYyybpX_NE";
    public static final String QB_APP_ID = "62924";
    public static final String QB_AUTH_KEY = "UmxnrRXab3qsEM8";
    public static final String QB_AUTH_SECRET = "hVwC9ujwA-T2yPJ";
    public static final String QB_DEFAULT_PASSWORD = "a1s2d3f4g5h6"; // IMPORTANT - MUST NOT change this value(organize value is a1s2d3f4)

    // Types of notification
    public static final String QUICKBLOX_MESSAGE = "quickbloxMessage"; // IMPORTANT - MUST confirm iOS for updating this const
    public static final String QUICKBLOX_PAY_FOR_DEAL = "payForDeal"; // IMPORTANT - MUST confirm iOS for updating this const
    public static final String QUICKBLOX_CALLING = "quickbloxCalling"; // IMPORTANT - MUST confirm iOS for updating this const
    public static final String NOTIFICATION_LOCATION_PERMISSION = "locationPermission";
    public static final String NOTIFICATION_TURN_ON_LOCATION = "turnOnLocation";
    public static final String TYPE_DEAL = "deal";
    public static final String SEND_BIRD = "sendbird";
}

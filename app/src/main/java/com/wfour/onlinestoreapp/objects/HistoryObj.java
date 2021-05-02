package com.wfour.onlinestoreapp.objects;

import com.google.gson.annotations.SerializedName;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.utils.DateTimeUtil;

/**
 * Created by Suusoft on 15/12/2016.
 */

public class HistoryObj {
    public static final String CREDIT = "exchange_point";
    public static final String REDEEM = "redeem_point";
    public static final String TRANSFER = "transfer_point";
    public static final int TRANSFER_TO = 1;
    public static final int RECEIVE = 2;
    private String id;
    @SerializedName("action")
    private String type;
    private String name;
    private String time;
    @SerializedName("amount")
    private String credits;
    @SerializedName("destination_email")
    private String email;
    private int user_id;
    private int destination_id;
    private String user_email;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        switch (type) {
            case CREDIT:
                return "Buy Credit";
            case REDEEM:
                return "Redeem";
            case TRANSFER:
                return "Transfer";
        }
        return "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return DateTimeUtil.convertTimeStampToDate(time, "HH:mm, EEE dd/MM/yyyy");
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getEmail() {
        if (getTypeTransaction() == TRANSFER_TO) {
            return email;
        } else if (getTypeTransaction() == RECEIVE) {
            return user_email;
        } else {
            return "";
        }

    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTypeTransaction() {
        UserObj userObj = DataStoreManager.getUser();
        if (userObj.getId().equals(String.valueOf(user_id))) {
            return TRANSFER_TO;
        } else if (userObj.getId().equals(String.valueOf(destination_id))) {
            return RECEIVE;
        } else {
            return 0;
        }
    }

}

package com.wfour.onlinestoreapp.retrofit.respone;

import android.content.Context;

import com.google.gson.Gson;

public class BaseRespone {

    public String status;
    public int code;
    public boolean is_logged_in;
    public String message;
    public String total_page;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isIs_logged_in() {
        return is_logged_in;
    }

    public void setIs_logged_in(boolean is_logged_in) {
        this.is_logged_in = is_logged_in;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTotal_page() {
        return total_page;
    }

    public void setTotal_page(String total_page) {
        this.total_page = total_page;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public boolean isSuccess(){
        return  getCode()==200 || getCode() == 200;
    }

    public boolean isSuccess(Context sefl){
        if ( getCode()==200 || getCode() == 200)
            return  true;

        else
            return false;
    }

}

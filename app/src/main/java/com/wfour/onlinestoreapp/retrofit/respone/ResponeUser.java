package com.wfour.onlinestoreapp.retrofit.respone;

import com.wfour.onlinestoreapp.objects.UserObj;

public class ResponeUser extends BaseRespone {

    private UserObj data;

    public UserObj getData() {
        return data;
    }

    public void setData(UserObj data) {
        this.data = data;
    }
}

package com.wfour.onlinestoreapp.objects;

/**
 * Created by Suusoft on 12/26/2016.
 */

public class CountryPhoneObj {
    private String name, code;

    public CountryPhoneObj(){

    }

    public CountryPhoneObj(String name, String phoneCode) {
        this.name = name;
        this.code = phoneCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneCode() {
        return code;
    }

    public void setPhoneCode(String phoneCode) {
        this.code = phoneCode;
    }
}

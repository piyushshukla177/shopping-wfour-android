package com.wfour.onlinestoreapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Suusoft on 12/01/2016.
 */

public class ProObj implements Parcelable {

    private String business_name, business_address, business_phone, business_email, business_website, description;
    @SerializedName("rate_count")
    private int rateCount;
    private int is_active;
    private float rate;

    private DriverObj driver;

    public ProObj() {
    }

    public String getname() {
        return business_name;
    }

    public void setname(String name) {
        this.business_name = name;
    }

    public String getBusiness_address() {
        return business_address;
    }

    public void setBusiness_address(String business_address) {
        this.business_address = business_address;
    }

    public String getBusiness_phone() {
        return business_phone;
    }

    public void setBusiness_phone(String business_phone) {
        this.business_phone = business_phone;
    }

    public int getRateCount() {
        return rateCount;
    }

    public void setRateCount(int rateCount) {
        this.rateCount = rateCount;
    }

    public float getRate() {
        return (rate / 2);
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public DriverObj getDriver() {
        return driver;
    }

    public void setDriver(DriverObj driver) {
        this.driver = driver;
    }

    public String getBusiness_email() {
        return business_email;
    }

    public void setBusiness_email(String business_email) {
        this.business_email = business_email;
    }

    @Override
    public String toString() {
        return super.toString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.business_name);
        dest.writeString(this.business_address);
        dest.writeString(this.business_phone);
        dest.writeString(this.business_email);
        dest.writeString(this.business_website);
        dest.writeString(this.description);
        dest.writeInt(this.rateCount);
        dest.writeInt(this.is_active);
        dest.writeFloat(this.rate);
        dest.writeParcelable(this.driver, flags);
    }

    protected ProObj(Parcel in) {
        this.business_name = in.readString();
        this.business_address = in.readString();
        this.business_phone = in.readString();
        this.business_email = in.readString();
        this.business_website = in.readString();
        this.description = in.readString();
        this.rateCount = in.readInt();
        this.is_active = in.readInt();
        this.rate = in.readFloat();
        this.driver = in.readParcelable(DriverObj.class.getClassLoader());
    }

    public static final Parcelable.Creator<ProObj> CREATOR = new Parcelable.Creator<ProObj>() {
        @Override
        public ProObj createFromParcel(Parcel source) {
            return new ProObj(source);
        }

        @Override
        public ProObj[] newArray(int size) {
            return new ProObj[size];
        }
    };
    public String getPhoneCode() {
        if (business_phone == null) {
            return "";
        } else {
            String[] code = business_phone.split(" ");
            int length = code.length;
            if (length >= 2) {
                String phoneCode = "";
                for (int i = 0; i < length - 1; i++) {
                    phoneCode = code[i] + " " + phoneCode;
                }
                return phoneCode;
            } else {
                return "";
            }

        }

    }

    public String getPhoneNumber() {
        if (business_phone == null) {
            return "";
        } else {
            String[] code = business_phone.split(" ");
            int length = code.length;
            if (length >= 2) {
                return code[length - 1];
            } else {
                return business_phone;
            }

        }
    }
}

package com.wfour.onlinestoreapp.view.activities;

import android.os.Parcel;
import android.os.Parcelable;

public class CountObj implements Parcelable {
    private int count;

    public CountObj() {

    }

    protected CountObj(Parcel in) {
        count = in.readInt();
    }

    public static final Creator<CountObj> CREATOR = new Creator<CountObj>() {
        @Override
        public CountObj createFromParcel(Parcel in) {
            return new CountObj(in);
        }

        @Override
        public CountObj[] newArray(int size) {
            return new CountObj[size];
        }
    };

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
    }
}

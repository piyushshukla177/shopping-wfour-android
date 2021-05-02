package com.wfour.onlinestoreapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class ColorProduct implements Parcelable {
    private String id;
    private String color;

    public ColorProduct() {
    }

    protected ColorProduct(Parcel in) {
        id = in.readString();
        color = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(color);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ColorProduct> CREATOR = new Creator<ColorProduct>() {
        @Override
        public ColorProduct createFromParcel(Parcel in) {
            return new ColorProduct(in);
        }

        @Override
        public ColorProduct[] newArray(int size) {
            return new ColorProduct[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

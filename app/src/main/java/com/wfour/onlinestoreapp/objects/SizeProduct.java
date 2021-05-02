package com.wfour.onlinestoreapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class SizeProduct implements Parcelable {
   private String id;
   private String size;

    public SizeProduct() {
    }

    protected SizeProduct(Parcel in) {
        id = in.readString();
        size = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(size);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SizeProduct> CREATOR = new Creator<SizeProduct>() {
        @Override
        public SizeProduct createFromParcel(Parcel in) {
            return new SizeProduct(in);
        }

        @Override
        public SizeProduct[] newArray(int size) {
            return new SizeProduct[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}

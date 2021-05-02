package com.wfour.onlinestoreapp.objects;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.wfour.onlinestoreapp.R;

import java.util.ArrayList;

/**
 * Created by Suusoft on 11/29/2016.
 */

public class TransportObj implements Parcelable {

    // This constants MUST be match with server
    public static final String ALL = "all";
    public static final String TAXI = "taxi";
    public static final String VIP = "vip";
    public static final String LIFTS = "lift";
    public static final String MOTORBIKE = "moto";
    public static final String DELIVERY = "delivery";

    private String type, name;
    private int icon;

    public TransportObj(String type, String name, int icon) {
        this.type = type;
        this.name = name;
        this.icon = icon;
    }

    public TransportObj(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static ArrayList<TransportObj> getListTranSports(Context context) {
        final ArrayList<TransportObj> transportObjs = new ArrayList<>();
        transportObjs.add(new TransportObj(TransportObj.TAXI, context.getString(R.string.taxi)));
        transportObjs.add(new TransportObj(TransportObj.VIP, context.getString(R.string.vip)));
        transportObjs.add(new TransportObj(TransportObj.LIFTS, context.getString(R.string.lifts)));
        transportObjs.add(new TransportObj(TransportObj.MOTORBIKE, context.getString(R.string.motorbike)));
        return transportObjs;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.name);
        dest.writeInt(this.icon);
    }

    protected TransportObj(Parcel in) {
        this.type = in.readString();
        this.name = in.readString();
        this.icon = in.readInt();
    }

    public static final Parcelable.Creator<TransportObj> CREATOR = new Parcelable.Creator<TransportObj>() {
        @Override
        public TransportObj createFromParcel(Parcel source) {
            return new TransportObj(source);
        }

        @Override
        public TransportObj[] newArray(int size) {
            return new TransportObj[size];
        }
    };
}

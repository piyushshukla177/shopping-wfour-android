package com.wfour.onlinestoreapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Suusoft on 12/01/2016.
 */

public class DriverObj implements Parcelable {
    public static final int DRIVER_ACTIVED = 1; // value =1: actived, value =0 unactive;
    public static final int DRIVER_AVAILABLE = 1; // value =1: actived, value =0 unactive;
    public static final int DRIVER_UNAVAILABLE = 0; // value =1: actived, value =0 unactive;
    private LatLng latLng;
    private String driver_license, online_started;
    private float fare;
    private int user_id, driver_experience, online_duration, is_active, is_online;
    private boolean isDelivery;
    private int is_delivery;
    private String type;
    private String fare_type;
    private TransportObj transport;


    public DriverObj() {
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public float getFare() {
        return fare;
    }

    public void setFare(float fare) {
        this.fare = fare;
    }

    public boolean isAvailable() {
        return is_online == DRIVER_AVAILABLE;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public boolean isActive() {
        return getIs_active() == DRIVER_ACTIVED;
    }

    public void setAvailable(int available) {
        is_online = available;
    }

    public boolean isDelivery() {
        return isDelivery;
    }

    public void setDelivery(boolean delivery) {
        isDelivery = delivery;
    }

    public TransportObj getTransport() {
        return transport;
    }

    public void setTransport(TransportObj transport) {
        this.transport = transport;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String getDriver_license() {
        return driver_license;
    }

    public void setDriver_license(String driver_license) {
        this.driver_license = driver_license;
    }

    public int getIs_delivery() {
        return is_delivery;
    }

    public void setIs_delivery(int is_delivery) {
        this.is_delivery = is_delivery;
    }

    public String getFare_type() {
        return fare_type;
    }

    public void setFare_type(String fare_type) {
        this.fare_type = fare_type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.latLng, flags);
        dest.writeString(this.driver_license);
        dest.writeString(this.online_started);
        dest.writeFloat(this.fare);
        dest.writeInt(this.user_id);
        dest.writeInt(this.driver_experience);
        dest.writeInt(this.online_duration);
        dest.writeInt(this.is_active);
        dest.writeInt(this.is_online);
        dest.writeByte(this.isDelivery ? (byte) 1 : (byte) 0);
        dest.writeString(this.type);
        dest.writeParcelable(this.transport, flags);
        dest.writeInt(is_delivery);
        dest.writeString(fare_type);
    }

    protected DriverObj(Parcel in) {
        this.latLng = in.readParcelable(LatLng.class.getClassLoader());
        this.driver_license = in.readString();
        this.online_started = in.readString();
        this.fare = in.readFloat();
        this.user_id = in.readInt();
        this.driver_experience = in.readInt();
        this.online_duration = in.readInt();
        this.is_active = in.readInt();
        this.is_online = in.readInt();
        this.isDelivery = in.readByte() != 0;
        this.type = in.readString();
        this.transport = in.readParcelable(TransportObj.class.getClassLoader());
        this.is_delivery = in.readInt();
        fare_type = in.readString();
    }

    public static final Parcelable.Creator<DriverObj> CREATOR = new Parcelable.Creator<DriverObj>() {
        @Override
        public DriverObj createFromParcel(Parcel source) {
            return new DriverObj(source);
        }

        @Override
        public DriverObj[] newArray(int size) {
            return new DriverObj[size];
        }
    };

}

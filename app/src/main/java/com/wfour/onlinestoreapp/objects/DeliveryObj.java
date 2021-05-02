package com.wfour.onlinestoreapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class DeliveryObj implements Parcelable {
    private String id, name, description, address;
     private int is_active;
     private double price;
     private boolean isSelected;
     public  DeliveryObj(){}


    protected DeliveryObj(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        address = in.readString();
        is_active = in.readInt();
        price = in.readDouble();
        isSelected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(address);
        dest.writeInt(is_active);
        dest.writeDouble(price);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeliveryObj> CREATOR = new Creator<DeliveryObj>() {
        @Override
        public DeliveryObj createFromParcel(Parcel in) {
            return new DeliveryObj(in);
        }

        @Override
        public DeliveryObj[] newArray(int size) {
            return new DeliveryObj[size];
        }
    };

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

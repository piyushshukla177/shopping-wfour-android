package com.wfour.onlinestoreapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Suusoft on 11/24/2016.
 */

public class UserObj implements Parcelable {

    public static final String NORMAL = "n";
    public static final String SOCIAL = "s";
    public static final String DATA_USER = "data user";
    private String id, name, email, phone, address, avatar, dob, status, latitude, longitude, passWord;
    private float rate;
    private float avg_rate;
    private int total_rate_count;
    private int rate_count, is_active;
    @SerializedName("role")
    @Expose
    private int role;
    private String token;
    private boolean rememberMe;
    @SerializedName("pro_data")
    private ProObj proData;
    @SerializedName("driver_data")
    private DriverObj driverData;
    @SerializedName("vehicle_data")
    private VehicleObject vehicleObject;
    private int is_secured;

    private float balance;

    private int qb_id;
    private double point;

    protected UserObj(Parcel in) {
        id = in.readString();
        name = in.readString();
        email = in.readString();
        phone = in.readString();
        address = in.readString();
        avatar = in.readString();
        dob = in.readString();
        status = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        passWord = in.readString();
        rate = in.readFloat();
        avg_rate = in.readFloat();
        total_rate_count = in.readInt();
        rate_count = in.readInt();
        is_active = in.readInt();
        role = in.readInt();
        token = in.readString();
        rememberMe = in.readByte() != 0;
        proData = in.readParcelable(ProObj.class.getClassLoader());
        driverData = in.readParcelable(DriverObj.class.getClassLoader());
        is_secured = in.readInt();
        balance = in.readFloat();
        qb_id = in.readInt();
        point = in.readDouble();
    }

    public static final Creator<UserObj> CREATOR = new Creator<UserObj>() {
        @Override
        public UserObj createFromParcel(Parcel in) {
            return new UserObj(in);
        }

        @Override
        public UserObj[] newArray(int size) {
            return new UserObj[size];
        }
    };

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public UserObj() {
    }




    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public DriverObj getDriverData() {
        return driverData;
    }

    public void setDriverData(DriverObj driverData) {
        this.driverData = driverData;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public int getRate_count() {
        return rate_count;
    }

    public void setRate_count(int rate_count) {
        this.rate_count = rate_count;
    }

    public ProObj getProData() {
        return proData;
    }

    public void setProData(ProObj proData) {
        this.proData = proData;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public float getRate() {
        return (rate / 2);
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public boolean isOnline() {
        return !getToken().isEmpty();
    }

    public int getQb_id() {
        return qb_id;
    }

    public void setQb_id(int qb_id) {
        this.qb_id = qb_id;
    }

    public VehicleObject getVehicleObject() {
        return vehicleObject;
    }

    public void setVehicleObject(VehicleObject vehicleObject) {
        this.vehicleObject = vehicleObject;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public float getAvg_rate() {
        return avg_rate/2;
    }

    public void setAvg_rate(float avg_rate) {
        this.avg_rate = avg_rate;
    }

    public int getTotal_rate_count() {
        return total_rate_count;
    }

    public void setTotal_rate_count(int total_rate_count) {
        this.total_rate_count = total_rate_count;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return super.toString();
    }




    public boolean isSecured() {
        return is_secured == 1;
    }

    public void setIs_secured(int is_secured) {
        this.is_secured = is_secured;
    }

    public String getPhoneCode() {
        if (phone == null) {
            return "";
        } else {
            String[] code = phone.split(" ");
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
        if (phone == null) {
            return "";
        } else {
            String[] code = phone.split(" ");
            int length = code.length;
            if (length >= 2) {
                return code[length - 1];
            } else {
                return phone;
            }

        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(phone);
        parcel.writeString(address);
        parcel.writeString(avatar);
        parcel.writeString(dob);
        parcel.writeString(status);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeString(passWord);
        parcel.writeFloat(rate);
        parcel.writeFloat(avg_rate);
        parcel.writeInt(total_rate_count);
        parcel.writeInt(rate_count);
        parcel.writeInt(is_active);
        parcel.writeInt(role);
        parcel.writeString(token);
        parcel.writeByte((byte) (rememberMe ? 1 : 0));
        parcel.writeParcelable(proData, i);
        parcel.writeParcelable(driverData, i);
        parcel.writeInt(is_secured);
        parcel.writeFloat(balance);
        parcel.writeInt(qb_id);
        parcel.writeDouble(point);
    }
}

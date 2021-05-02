package com.wfour.onlinestoreapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Suusoft on 11/17/2016.
 */

public class DealObj implements Parcelable {

    public static final int DEAL_ACTIVE = 1;
    public static final int DEAL_INACTIVE = 0;

    private String image, attachment, seller_avatar;
    private String id, name, videoUrl, address, description, status, seller_id, online_started;
    private int number;
    private double totalMoney;
    @SerializedName("like_count")
    private int favoriteQuantity;
    private int is_renew, reservation_count;

    private String price, sale_price;

    private float rate;
    @SerializedName("seller_qb_id")
    private int sellerQbId;

    @SerializedName("rate_count")
    private int rateQuantity;
    private int is_premium, is_active, is_online;
    @SerializedName("discount")
    private String discount_price;

    private String discount_type;

    private int is_favourite;
    @SerializedName("lat")
    private double latitude;
    @SerializedName("long")
    private double longitude;

    private int category_id;
    private int discount_rate;
    private int online_duration;

    @SerializedName("pro_data")
    private ProObj proData;

    private int positionInList; // position of object in list
    private String buyerId;
    private String buyerName;

    private boolean isShowEndTime;

    public DealObj() {
    }


    protected DealObj(Parcel in) {
        image = in.readString();
        attachment = in.readString();
        seller_avatar = in.readString();
        id = in.readString();
        name = in.readString();
        videoUrl = in.readString();
        address = in.readString();
        description = in.readString();
        status = in.readString();
        seller_id = in.readString();
        online_started = in.readString();
        favoriteQuantity = in.readInt();
        is_renew = in.readInt();
        reservation_count = in.readInt();
        price = in.readString();
        sale_price = in.readString();
        rate = in.readFloat();
        sellerQbId = in.readInt();
        rateQuantity = in.readInt();
        is_premium = in.readInt();
        is_active = in.readInt();
        is_online = in.readInt();
        discount_price = in.readString();
        is_favourite = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        category_id = in.readInt();
        discount_rate = in.readInt();
        online_duration = in.readInt();
        proData = in.readParcelable(ProObj.class.getClassLoader());
        positionInList = in.readInt();
        buyerId = in.readString();
        buyerName = in.readString();
        isShowEndTime = in.readByte() != 0;
        discount_type = in.readString();
    }

    public static final Creator<DealObj> CREATOR = new Creator<DealObj>() {
        @Override
        public DealObj createFromParcel(Parcel in) {
            return new DealObj(in);
        }

        @Override
        public DealObj[] newArray(int size) {
            return new DealObj[size];
        }
    };

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getSeller_avatar() {
        return seller_avatar;
    }

    public void setSeller_avatar(String seller_avatar) {
        this.seller_avatar = seller_avatar;
    }

    public int getSellerQbId() {
        return sellerQbId;
    }

    public void setSellerQbId(int sellerQbId) {
        this.sellerQbId = sellerQbId;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public int getPositionInList() {
        return positionInList;
    }

    public void setPositionInList(int positionInList) {
        this.positionInList = positionInList;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public int getIs_renew() {
        return is_renew;
    }

    public void setIs_renew(int is_renew) {
        this.is_renew = is_renew;
    }

    public boolean isRenew() {
        return is_renew == 0 ? false : true;
    }

    public int getReservation_count() {
        return reservation_count;
    }

    public void setReservation_count(int reservation_count) {
        this.reservation_count = reservation_count;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIs_premium() {
        return is_premium;
    }

    public void setIs_premium(int is_premium) {
        this.is_premium = is_premium;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public ProObj getProData() {
        return proData;
    }

    public void setProData(ProObj proData) {
        this.proData = proData;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDiscount_price() {
        return Double.valueOf(discount_price);
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public double getPrice() {
        return Double.valueOf(price);
    }

    public String getStringPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public double getSale_price() {
        return Double.valueOf(sale_price);
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public String getImageUrl() {
        return image;
    }

    public void setImageUrl(String imageUrl) {
        this.image = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getFavoriteQuantity() {
        return favoriteQuantity;
    }

    public void setFavoriteQuantity(int favoriteQuantity) {
        this.favoriteQuantity = favoriteQuantity;
    }

    public int getRateQuantity() {
        return rateQuantity;
    }

    public void setRateQuantity(int rateQuantity) {
        this.rateQuantity = rateQuantity;
    }

    public float getRate() {
        return rate / 2;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public boolean isFavorite() {
        return is_favourite == 1;
    }

    public void setFavorite(boolean isFavorited) {
        is_favourite = isFavorited ? 1 : 0;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public int getIs_online() {
        return is_online;
    }

    public void setIs_online(int is_online) {
        this.is_online = is_online;
    }

    public boolean isOnline() {
        return getIs_online() == DEAL_ACTIVE;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(int discount_rate) {
        this.discount_rate = discount_rate;
    }

    public int getOnline_duration() {
        return online_duration;
    }

    public void setOnline_duration(int online_duration) {
        this.online_duration = online_duration;
    }

    public String getOnline_started() {
        return online_started;
    }

    public void setOnline_started(String online_started) {
        this.online_started = online_started;
    }

    public void isHideEndTime(boolean is) {
        isShowEndTime = is;
    }

    public boolean isHideEndTime() {
        return isShowEndTime;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(image);
        parcel.writeString(attachment);
        parcel.writeString(seller_avatar);
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(videoUrl);
        parcel.writeString(address);
        parcel.writeString(description);
        parcel.writeString(status);
        parcel.writeString(seller_id);
        parcel.writeString(online_started);
        parcel.writeInt(favoriteQuantity);
        parcel.writeInt(is_renew);
        parcel.writeInt(reservation_count);
        parcel.writeString(price);
        parcel.writeString(sale_price);
        parcel.writeFloat(rate);
        parcel.writeInt(sellerQbId);
        parcel.writeInt(rateQuantity);
        parcel.writeInt(is_premium);
        parcel.writeInt(is_active);
        parcel.writeInt(is_online);
        parcel.writeString(discount_price);
        parcel.writeInt(is_favourite);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeInt(category_id);
        parcel.writeInt(discount_rate);
        parcel.writeInt(online_duration);
        parcel.writeParcelable(proData, i);
        parcel.writeInt(positionInList);
        parcel.writeString(buyerId);
        parcel.writeString(buyerName);
        parcel.writeByte((byte) (isShowEndTime ? 1 : 0));
        parcel.writeString(discount_type);
    }

}

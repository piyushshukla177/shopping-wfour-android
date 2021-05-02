package com.wfour.onlinestoreapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Suusoft on 12/15/2016.
 */

public class ReservationObj implements Parcelable {

    public static final String ACTION_DEAL = "deal";
    public static final String ACTION_DENY = "deny";
    public static final String ACTION_DELETE = "delete";
    public static final String ACTION_FINISH = "finish";
    public static final String ACTION_PAY = "pay";
    public static final String ACTION_DETAIL = "detail";

    private int id, buyer_id, buyer_confirm, buyer_visible, seller_id, seller_confirm,
            seller_visible, deal_id, time, payment_status, is_active;
    private float price;
    private String buyer_name, seller_name, deal_name, payment_method, status, created_date, buyer_avatar, seller_avatar;
    private DealObj deal;

    public ReservationObj() {
    }

    protected ReservationObj(Parcel in) {
        id = in.readInt();
        buyer_id = in.readInt();
        buyer_confirm = in.readInt();
        buyer_visible = in.readInt();
        seller_id = in.readInt();
        seller_confirm = in.readInt();
        seller_visible = in.readInt();
        deal_id = in.readInt();
        price = in.readFloat();
        time = in.readInt();
        payment_status = in.readInt();
        is_active = in.readInt();
        buyer_name = in.readString();
        seller_name = in.readString();
        deal_name = in.readString();
        payment_method = in.readString();
        status = in.readString();
        created_date = in.readString();
        buyer_avatar = in.readString();
        seller_avatar = in.readString();
        deal = in.readParcelable(DealObj.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(buyer_id);
        dest.writeInt(buyer_confirm);
        dest.writeInt(buyer_visible);
        dest.writeInt(seller_id);
        dest.writeInt(seller_confirm);
        dest.writeInt(seller_visible);
        dest.writeInt(deal_id);
        dest.writeFloat(price);
        dest.writeInt(time);
        dest.writeInt(payment_status);
        dest.writeInt(is_active);
        dest.writeString(buyer_name);
        dest.writeString(seller_name);
        dest.writeString(deal_name);
        dest.writeString(payment_method);
        dest.writeString(status);
        dest.writeString(created_date);
        dest.writeString(buyer_avatar);
        dest.writeString(seller_avatar);
        dest.writeParcelable(deal, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReservationObj> CREATOR = new Creator<ReservationObj>() {
        @Override
        public ReservationObj createFromParcel(Parcel in) {
            return new ReservationObj(in);
        }

        @Override
        public ReservationObj[] newArray(int size) {
            return new ReservationObj[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(int buyer_id) {
        this.buyer_id = buyer_id;
    }

    public int getBuyer_confirm() {
        return buyer_confirm;
    }

    public void setBuyer_confirm(int buyer_confirm) {
        this.buyer_confirm = buyer_confirm;
    }

    public int getBuyer_visible() {
        return buyer_visible;
    }

    public void setBuyer_visible(int buyer_visible) {
        this.buyer_visible = buyer_visible;
    }

    public int getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(int seller_id) {
        this.seller_id = seller_id;
    }

    public int getSeller_confirm() {
        return seller_confirm;
    }

    public void setSeller_confirm(int seller_confirm) {
        this.seller_confirm = seller_confirm;
    }

    public int getSeller_visible() {
        return seller_visible;
    }

    public void setSeller_visible(int seller_visible) {
        this.seller_visible = seller_visible;
    }

    public int getDeal_id() {
        return deal_id;
    }

    public void setDeal_id(int deal_id) {
        this.deal_id = deal_id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(int payment_status) {
        this.payment_status = payment_status;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getDeal_name() {
        return deal_name;
    }

    public void setDeal_name(String deal_name) {
        this.deal_name = deal_name;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getBuyer_avatar() {
        return buyer_avatar;
    }

    public void setBuyer_avatar(String buyer_avatar) {
        this.buyer_avatar = buyer_avatar;
    }

    public String getSeller_avatar() {
        return seller_avatar;
    }

    public void setSeller_avatar(String seller_avatar) {
        this.seller_avatar = seller_avatar;
    }

    public DealObj getDeal() {
        return deal;
    }

    public void setDeal(DealObj deal) {
        this.deal = deal;
    }
}

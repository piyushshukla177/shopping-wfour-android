package com.wfour.onlinestoreapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.quickblox.users.model.QBUser;

import java.util.Calendar;

/**
 * Created by Suusoft on 12/20/2016.
 */

public class RecentChatObj implements Parcelable {

    private TransportDealObj transportDealObj;
    private DealObj dealObj;
    private QBUser qbUser;
    private long time;
    private String lastMessage;

    public RecentChatObj(TransportDealObj transportDealObj, DealObj dealObj, QBUser qbUser) {
        this.transportDealObj = transportDealObj;
        this.dealObj = dealObj;
        this.qbUser = qbUser;
        time = Calendar.getInstance().getTimeInMillis();
    }

    public TransportDealObj getTransportDealObj() {
        return transportDealObj;
    }

    public void setTransportDealObj(TransportDealObj transportDealObj) {
        this.transportDealObj = transportDealObj;
    }

    public DealObj getDealObj() {
        return dealObj;
    }

    public void setDealObj(DealObj dealObj) {
        this.dealObj = dealObj;
    }

    public QBUser getQbUser() {
        return qbUser;
    }

    public void setQbUser(QBUser qbUser) {
        this.qbUser = qbUser;
    }

    public boolean justForChatting() {
        return (getTransportDealObj() == null && getDealObj() == null);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Get last message to check if it's 'agree' message
     *
     * @return The last message of a conversation
     */
    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
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
        dest.writeParcelable(this.transportDealObj, flags);
        dest.writeParcelable(this.dealObj, flags);
        dest.writeSerializable(this.qbUser);
        dest.writeLong(this.time);
        dest.writeString(this.lastMessage);
    }

    protected RecentChatObj(Parcel in) {
        this.transportDealObj = in.readParcelable(TransportDealObj.class.getClassLoader());
        this.dealObj = in.readParcelable(DealObj.class.getClassLoader());
        this.qbUser = (QBUser) in.readSerializable();
        this.time = in.readLong();
        this.lastMessage = in.readString();
    }

    public static final Parcelable.Creator<RecentChatObj> CREATOR = new Parcelable.Creator<RecentChatObj>() {
        @Override
        public RecentChatObj createFromParcel(Parcel source) {
            return new RecentChatObj(source);
        }

        @Override
        public RecentChatObj[] newArray(int size) {
            return new RecentChatObj[size];
        }
    };
}

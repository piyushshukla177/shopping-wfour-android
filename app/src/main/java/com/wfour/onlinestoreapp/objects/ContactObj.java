package com.wfour.onlinestoreapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.quickblox.users.model.QBUser;

/**
 * Created by Suusoft on 14/12/2016.
 */

public class ContactObj implements Parcelable {

    private String avatar, friendId;
    private QBUser qbUser;

    public ContactObj(String avatar, String friendId, QBUser qbUser) {
        this.avatar = avatar;
        this.friendId = friendId;
        this.qbUser = qbUser;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public QBUser getQbUser() {
        return qbUser;
    }

    public void setQbUser(QBUser qbUser) {
        this.qbUser = qbUser;
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
        dest.writeString(this.avatar);
        dest.writeString(this.friendId);
        dest.writeSerializable(this.qbUser);
    }

    protected ContactObj(Parcel in) {
        this.avatar = in.readString();
        this.friendId = in.readString();
        this.qbUser = (QBUser) in.readSerializable();
    }

    public static final Parcelable.Creator<ContactObj> CREATOR = new Parcelable.Creator<ContactObj>() {
        @Override
        public ContactObj createFromParcel(Parcel source) {
            return new ContactObj(source);
        }

        @Override
        public ContactObj[] newArray(int size) {
            return new ContactObj[size];
        }
    };
}

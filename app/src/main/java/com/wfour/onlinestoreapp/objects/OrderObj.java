package com.wfour.onlinestoreapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderObj implements Parcelable {
    String id;
    String paymentMethod;
    String items;
    String user_id,type_product,billingCode, billingName,billingAddress,billingPhone,billingEmail,billingPostcode,shippingName,shippingAddress,shippingPhone,shippingEmail;
    String shippingPostcode,content,transportDes,transportType,status, status_user, token_payment, createDate;
    double vat,transportFee, total;
    public OrderObj(){}

    protected OrderObj(Parcel in) {
        id = in.readString();
        paymentMethod = in.readString();
        items = in.readString();
        user_id = in.readString();
        type_product = in.readString();
        billingCode    = in.readString();
        billingName = in.readString();
        billingAddress = in.readString();
        billingPhone = in.readString();
        billingEmail = in.readString();
        billingPostcode = in.readString();
        shippingName = in.readString();
        shippingAddress = in.readString();
        shippingPhone = in.readString();
        shippingEmail = in.readString();
        shippingPostcode = in.readString();
        content = in.readString();
        transportDes = in.readString();
        transportType = in.readString();
        status = in.readString();
        status_user = in.readString();
        token_payment = in.readString();
        createDate = in.readString();
        vat = in.readDouble();
        transportFee = in.readDouble();
        total = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(paymentMethod);
        dest.writeString(items);
        dest.writeString(user_id);
        dest.writeString(type_product);
        dest.writeString(billingCode);
        dest.writeString(billingName);
        dest.writeString(billingAddress);
        dest.writeString(billingPhone);
        dest.writeString(billingEmail);
        dest.writeString(billingPostcode);
        dest.writeString(shippingName);
        dest.writeString(shippingAddress);
        dest.writeString(shippingPhone);
        dest.writeString(shippingEmail);
        dest.writeString(shippingPostcode);
        dest.writeString(content);
        dest.writeString(transportDes);
        dest.writeString(transportType);
        dest.writeString(status);
        dest.writeString(status_user);
        dest.writeString(token_payment);
        dest.writeString(createDate);
        dest.writeDouble(vat);
        dest.writeDouble(transportFee);
        dest.writeDouble(total);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderObj> CREATOR = new Creator<OrderObj>() {
        @Override
        public OrderObj createFromParcel(Parcel in) {
            return new OrderObj(in);
        }

        @Override
        public OrderObj[] newArray(int size) {
            return new OrderObj[size];
        }
    };

    public String getToken_payment() {
        return token_payment;
    }

    public void setToken_payment(String token_payment) {
        this.token_payment = token_payment;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getType_product() {
        return type_product;
    }

    public void setType_product(String type_product) {
        this.type_product = type_product;
    }

    public String getBillCode() {
        return billingCode;
    }

    public void setBillCode(String billCode) {
        this.billingCode = billCode;
    }

    public String getBillingName() {
        return billingName;
    }

    public void setBillingName(String billingName) {
        this.billingName = billingName;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getBillingPhone() {
        return billingPhone;
    }

    public void setBillingPhone(String billingPhone) {
        this.billingPhone = billingPhone;
    }

    public String getBillingEmail() {
        return billingEmail;
    }

    public void setBillingEmail(String billingEmail) {
        this.billingEmail = billingEmail;
    }

    public String getBillingPostcode() {
        return billingPostcode;
    }

    public void setBillingPostcode(String billingPostcode) {
        this.billingPostcode = billingPostcode;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingPhone() {
        return shippingPhone;
    }

    public void setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone;
    }

    public String getShippingEmail() {
        return shippingEmail;
    }

    public void setShippingEmail(String shippingEmail) {
        this.shippingEmail = shippingEmail;
    }

    public String getShippingPostcode() {
        return shippingPostcode;
    }

    public void setShippingPostcode(String shippingPostcode) {
        this.shippingPostcode = shippingPostcode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTransportDes() {
        return transportDes;
    }

    public void setTransportDes(String transportDes) {
        this.transportDes = transportDes;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_user() {
        return status_user;
    }

    public void setStatus_user(String status_user) {
        this.status_user = status_user;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public double getTransportFee() {
        return transportFee;
    }

    public void setTransportFee(double transportFee) {
        this.transportFee = transportFee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }
}

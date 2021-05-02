package com.wfour.onlinestoreapp.objects;

/**
 * Created by Suusoft on 12/08/2016.
 */

public class PaymentMethodObj {

    public static final String CASH = "cash";
    public static final String PAYPAL = "paypal";
    public static final String CREDITS = "point";
    public static final String STRIPE = "stripe";
    public static final String OTHER = "other";

    private String id, name;
    private boolean selected;

    public PaymentMethodObj(String id, String name, boolean selected) {
        this.id = id;
        this.name = name;
        this.selected = selected;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

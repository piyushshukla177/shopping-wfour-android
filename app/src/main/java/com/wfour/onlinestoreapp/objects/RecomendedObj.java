package com.wfour.onlinestoreapp.objects;

public class RecomendedObj {

    private String Product_Name;
    private String discount_rate;
    private String actual_rate;
    private String description;


    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public String getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(String discount_rate) {
        this.discount_rate = discount_rate;
    }

    public String getActual_rate() {
        return actual_rate;
    }

    public void setActual_rate(String actual_rate) {
        this.actual_rate = actual_rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

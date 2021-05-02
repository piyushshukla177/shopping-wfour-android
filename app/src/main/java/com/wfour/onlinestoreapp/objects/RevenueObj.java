package com.wfour.onlinestoreapp.objects;

/**
 * Created by Suusoft on 12/09/2016.
 */

public class RevenueObj {

    private float revenue, expense;

    public RevenueObj(float revenue, float expense) {
        this.revenue = revenue;
        this.expense = expense;
    }

    public float getRevenue() {
        return revenue;
    }

    public void setRevenue(float revenue) {
        this.revenue = revenue;
    }

    public float getExpense() {
        return expense;
    }

    public void setExpense(float expense) {
        this.expense = expense;
    }

    public float getTotalRevenuePerMonth() {
        return getRevenue() - getExpense();
    }
}

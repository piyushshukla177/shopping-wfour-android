package com.wfour.onlinestoreapp.objects;

/**
 * Created by Suusoft on 15/12/2016.
 */

public class SettingsObj {
    private int notify;
    private int notify_favourite;
    private int notify_transport;
    private int notify_food;
    private int notify_labor;
    private int notify_travel;
    private int notify_shopping;
    private int notify_news;
    private int notify_nearby;

    private String deal_online_rate;
    private String premium_deal_online_rate;
    private String driver_online_rate;
    private String exchange_rate, deal_payment_fee, trip_payment_fee;
    private String exchange_fee;
    private String faq;
    private String about;
    private String help;
    private String term;
    private String searching_deal_distance;

    public boolean getNotify() {
        return (notify == 1) ? true : false;
    }

    public void setNotify(int notify) {
        this.notify = notify;
    }

    public boolean getNotify_favourite() {
        return (notify_favourite == 1) ? true : false;
    }

    public void setNotify_favourite(int notify_favourite) {
        this.notify_favourite = notify_favourite;
    }

    public boolean getNotify_transport() {
        return (notify_transport == 1) ? true : false;
    }

    public void setNotify_transport(int notify_transport) {
        this.notify_transport = notify_transport;
    }

    public boolean getNotify_food() {
        return (notify_food == 1) ? true : false;
    }

    public void setNotify_food(int notify_food) {
        this.notify_food = notify_food;
    }

    public boolean getNotify_labor() {
        return (notify_labor == 1) ? true : false;
    }

    public void setNotify_labor(int notify_labor) {
        this.notify_labor = notify_labor;
    }

    public boolean getNotify_travel() {
        return (notify_travel == 1) ? true : false;
    }

    public void setNotify_travel(int notify_travel) {
        this.notify_travel = notify_travel;
    }

    public boolean getNotify_shopping() {
        return (notify_shopping == 1) ? true : false;
    }

    public void setNotify_shopping(int notify_shopping) {
        this.notify_shopping = notify_shopping;
    }

    public boolean getNotify_nearby() {
        return (notify_nearby == 1) ? true : false;
    }

    public void setNotify_nearby(int notify_nearby) {
        this.notify_nearby = notify_nearby;
    }

    public boolean getNotify_news() {
        return (notify_news == 1) ? true : false;
    }

    public void setNotify_news(int notify_news) {
        this.notify_news = notify_news;
    }

    public String getDeal_online_rate() {
        return deal_online_rate;
    }

    public void setDeal_online_rate(String deal_online_rate) {
        this.deal_online_rate = deal_online_rate;
    }

    public String getPremium_deal_online_rate() {
        return premium_deal_online_rate;
    }

    public void setPremium_deal_online_rate(String premium_deal_online_rate) {
        this.premium_deal_online_rate = premium_deal_online_rate;
    }

    public String getDriver_online_rate() {
        return driver_online_rate;
    }

    public void setDriver_online_rate(String driver_online_rate) {
        this.driver_online_rate = driver_online_rate;
    }

    public String getExchange_rate() {
        return exchange_rate;
    }

    public void setExchange_rate(String exchange_rate) {
        this.exchange_rate = exchange_rate;
    }

    public String getDeal_payment_fee() {
        return deal_payment_fee;
    }

    public void setDeal_payment_fee(String deal_payment_fee) {
        this.deal_payment_fee = deal_payment_fee;
    }

    public String getTrip_payment_fee() {
        return trip_payment_fee;
    }

    public void setTrip_payment_fee(String trip_payment_fee) {
        this.trip_payment_fee = trip_payment_fee;
    }

    public String getExchange_fee() {
        return exchange_fee;
    }

    public void setExchange_fee(String exchange_fee) {
        this.exchange_fee = exchange_fee;
    }

    public String getFaq() {
        return faq;
    }

    public void setFaq(String faq) {
        this.faq = faq;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getSearching_deal_distance() {
        return searching_deal_distance;
    }

    public void setSearching_deal_distance(String searching_deal_distance) {
        this.searching_deal_distance = searching_deal_distance;
    }

}

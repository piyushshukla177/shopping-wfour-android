package com.wfour.onlinestoreapp.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Suusoft on 11/24/2016.
 */

public class DealReviewObj {

    private int author_id, destination_id, object_id;
    private String id ,content, created_date, author_role, object_type, destination_role;
    private float rate;
    @SerializedName("author_avatar")
    private String userImage;
    @SerializedName("author_name")
    private String username;

    public DealReviewObj(String username, String userImage ,String content, float rate) {
        this.username = username;
        this.content = content;
        this.rate =rate;
        this.userImage = userImage;
    }

    public DealReviewObj(String id, String username, String userImage, float rate, String content, String datetime) {
        this.id = id;
        this.username = username;
        this.userImage = userImage;
        this.rate = rate;
        this.content = content;
        this.created_date = datetime;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public int getDestination_id() {
        return destination_id;
    }

    public void setDestination_id(int destination_id) {
        this.destination_id = destination_id;
    }

    public int getObject_id() {
        return object_id;
    }

    public void setObject_id(int object_id) {
        this.object_id = object_id;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getAuthor_role() {
        return author_role;
    }

    public void setAuthor_role(String author_role) {
        this.author_role = author_role;
    }

    public String getObject_type() {
        return object_type;
    }

    public void setObject_type(String object_type) {
        this.object_type = object_type;
    }

    public String getDestination_role() {
        return destination_role;
    }

    public void setDestination_role(String destination_role) {
        this.destination_role = destination_role;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public float getRating() {
        return (rate/2);
    }

    public void setRating(float rate) {
        this.rate = rate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDatetime() {
        return created_date;
    }

    public void setDatetime(String datetime) {
        this.created_date = datetime;
    }
}

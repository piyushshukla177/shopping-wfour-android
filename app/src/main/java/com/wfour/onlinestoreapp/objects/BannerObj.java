package com.wfour.onlinestoreapp.objects;

public class BannerObj {
    private Integer id;
    private String title;
    private String description;
    private String image;
    private String position;
    private String url;
    private String createdAt;

    public BannerObj() {
    }

    public BannerObj(Integer id, String title, String description, String image, String position, String url, String createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.position = position;
        this.url = url;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

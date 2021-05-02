package com.wfour.onlinestoreapp.objects;

public class Category {

    public static final String MY_FAVORITES = "1";
    public static final String FOOD_AND_BEVERAGES = "2";
    public static final String LABOR = "3";
    public static final String TRAVEL = "4";
    public static final String SHOPPING = "5";
    public static final String NEWS_AND_EVENTS = "6";
    public static final String OTHER = "7";
    public static final String TRANSPORT = "8";

    private String id;
    private String name;
    private String parent_id;
    private String image;
    private String description;
    private String sort_order;
    private String is_active;
    private String is_top;
    private String is_hot;
    private String object_type;
    private String created_date;
    private String modified_date;
    private  boolean have_child;

    public boolean getHave_child() {
        return have_child;
    }

    public void setHave_child(boolean have_child) {
        this.have_child = have_child;
    }

    public Category() {
    }

    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getIs_top() {
        return is_top;
    }

    public void setIs_top(String is_top) {
        this.is_top = is_top;
    }

    public String getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(String is_hot) {
        this.is_hot = is_hot;
    }

    public String getObject_type() {
        return object_type;
    }

    public void setObject_type(String object_type) {
        this.object_type = object_type;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getModified_date() {
        return modified_date;
    }

    public void setModified_date(String modified_date) {
        this.modified_date = modified_date;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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


}

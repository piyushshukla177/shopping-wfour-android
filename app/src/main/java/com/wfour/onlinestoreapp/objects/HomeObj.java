package com.wfour.onlinestoreapp.objects;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HomeObj {
    public static final String HOT = "Hot Product";
    public static final String FEATURE = "Feature Product";
    public static final String NEW = "New Product";
    public String name, type, status;


    public ArrayList<ProductObj> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<ProductObj> productList) {
        this.productList = productList;
    }
    public ArrayList<ProductObj> productList;
    @SerializedName("categories")
    public ArrayList<Category> categoryList;
    @SerializedName("hot_list")
    public ArrayList<ProductObj> mListHot;
    @SerializedName("feature_list")
    private ArrayList<ProductObj> mListFeature;
    @SerializedName("new_list")
    private ArrayList<ProductObj> mListNew;

    public ArrayList<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public ArrayList<ProductObj> getmListHot() {
        if(mListHot == null){
            mListHot = new ArrayList<>();
        }
        return mListHot;
    }

    public ArrayList<ProductObj> getmListFeature() {
        return mListFeature;
    }

    public void setmListFeature(ArrayList<ProductObj> mListFeature) {
        this.mListFeature = mListFeature;
    }

    public ArrayList<ProductObj> getmListNew() {
        return mListNew;
    }

    public void setmListNew(ArrayList<ProductObj> mListNew) {
        this.mListNew = mListNew;
    }

    public void setmListHot(ArrayList<ProductObj> mListHot) {
        this.mListHot = mListHot;
    }

    public HomeObj(){}
    public HomeObj(String name, String type, ArrayList<ProductObj> list){
        this.name = name;
        this.type = type;
        this.productList = list;
    }
    public HomeObj(String name, ArrayList<Category> list){
        this.name = name;
        this.categoryList = list;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}

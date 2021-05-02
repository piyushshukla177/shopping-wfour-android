package com.wfour.onlinestoreapp.objects;

public class CartObj {
    String id;
    String title;
    double price;
    String image;
    int number;
    double totalMoney;
    String color;
    String size;
    double oldPrice;
    int is_point;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getIs_point() {
        return is_point;
    }

    public void setIs_point(int is_point) {
        this.is_point = is_point;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public CartObj() {

    }

    public CartObj(String id, String title, double price, String image, int number, double totalMoney, String color, String size,double oldPrice,int is_point) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.image = image;
        this.number = number;
        this.totalMoney = totalMoney;
        this.color = color;
        this.size = size;
        this.oldPrice = oldPrice;
        this.is_point = is_point;
    }

    public CartObj(String title, double price, String image) {
        this.title = title;
        this.price = price;
        this.image = image;
    }

    public CartObj(String id, String title, double price, String image) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.image = image;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        size = size;
    }

    public double getTotalMoney(){
        return totalMoney;
    }
    public void  setTotalMoney(double totalMoney){
        this.totalMoney = totalMoney;
    }
    public String getId(){
        return id;
    }
    public void  setId(String id){
        this.id = id;
    }
    public int getNumber(){
        return number;
    }
    public void  setNumber(int number){
        this.number = number;
    }
    public String getName() {
        return title;
    }

    public void setName(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

package com.wfour.onlinestoreapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ProductObj implements Parcelable  {
    public static final int DEAL_ACTIVE = 1;
    public static final int DEAL_INACTIVE = 0;
    private int is_active = 1;
    private int is_hot = 1;
    private int is_top = 0;
    private ArrayList<ProductObj> productObjList;


    protected ProductObj(Parcel in) {
        is_active = in.readInt();
        is_hot = in.readInt();
        is_top = in.readInt();
        productObjList = in.createTypedArrayList(ProductObj.CREATOR);
        image_files = in.createStringArrayList();
        colors = in.createTypedArrayList(ColorProduct.CREATOR);
        sizes = in.createTypedArrayList(SizeProduct.CREATOR);
        id = in.readString();
        thumbnail = in.readString();
        image = in.readString();
        banner = in.readString();
        code = in.readString();
        title = in.readString();
        overview = in.readString();
        content = in.readString();
        cost = in.readString();
        unit = in.readString();
        currency = in.readString();
        type = in.readString();
        status = in.readString();
        brand = in.readString();
        category_id = in.readString();
        is_promotion = in.readString();
        promotion_id = in.readString();
        tags = in.readString();
        quantity = in.readString();
        discount = in.readString();
        tax = in.readString();
        is_tax_included = in.readString();
        count_views = in.readString();
        count_comments = in.readString();
        count_purchase = in.readString();
        count_likes = in.readString();
        count_rates = in.readString();
        rates = in.readString();
        qrcode_image = in.readString();
        barcode_image = in.readString();
        created_date = in.readString();
        created_user = in.readString();
        modified_date = in.readString();
        modified_user = in.readString();
        application_id = in.readString();
        color = in.readString();
        size = in.readString();
        number = in.readInt();
        is_prize = in.readInt();
        is_favourite = in.readInt();
        totalMoney = in.readDouble();
        price = in.readDouble();
        old_price = in.readDouble();
    }

    public static final Creator<ProductObj> CREATOR = new Creator<ProductObj>() {
        @Override
        public ProductObj createFromParcel(Parcel in) {
            return new ProductObj(in);
        }

        @Override
        public ProductObj[] newArray(int size) {
            return new ProductObj[size];
        }
    };

    public double getOld_price() {
        return old_price;
    }

    public void setOld_price(double old_price) {
        this.old_price = old_price;
    }



    public ArrayList<ProductObj> getProductObjList() {
        return productObjList;
    }

    public void setProductObjList(ArrayList<ProductObj> productObjList) {
        this.productObjList = productObjList;
    }

    private ArrayList<String> image_files;
    private ArrayList<ColorProduct> colors;
    private ArrayList<SizeProduct> sizes;
    private String id, thumbnail, image, banner, code, title, overview, content, cost, unit, currency, type, status, brand, category_id, is_promotion, promotion_id, tags, quantity, discount, tax, is_tax_included, count_views, count_comments, count_purchase, count_likes, count_rates, rates, qrcode_image,
            barcode_image, created_date, created_user, modified_date, modified_user, application_id, color, size;
    private int number;
    private int is_prize;

    public int getIs_favourite() {
        return is_favourite;
    }

    public void setIs_favourite(int is_favourite) {
        this.is_favourite = is_favourite;
    }

    private int is_favourite;
    private double totalMoney, price,old_price;

    public int getIs_prize() {
        return is_prize;
    }

    public void setIs_prize(int is_prize) {
        this.is_prize = is_prize;
    }

    public ProductObj() {
    }

    public ProductObj(String id, String title, double price, String image, int number, double totalMoney) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.image = image;
        this.number = number;
        this.totalMoney = totalMoney;
    }

    public ArrayList<ColorProduct> getColors() {
        return colors;
    }

    public void setColors(ArrayList<ColorProduct> colors) {
        this.colors = colors;
    }

    public ArrayList<SizeProduct> getSizes() {
        return sizes;
    }

    public void setSizes(ArrayList<SizeProduct> sizes) {
        this.sizes = sizes;
    }


    public ArrayList<String> getImage_files() {
        return image_files;
    }

    public void setImage_files(ArrayList<String> image_files) {
        this.image_files = image_files;
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
        this.size = size;
    }

    public static int getDealActive() {
        return DEAL_ACTIVE;
    }

    public static int getDealInactive() {
        return DEAL_INACTIVE;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public int getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(int is_hot) {
        this.is_hot = is_hot;
    }

    public int getIs_top() {
        return is_top;
    }

    public void setIs_top(int is_top) {
        this.is_top = is_top;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getIs_promotion() {
        return is_promotion;
    }

    public void setIs_promotion(String is_promotion) {
        this.is_promotion = is_promotion;
    }

    public String getPromotion_id() {
        return promotion_id;
    }

    public void setPromotion_id(String promotion_id) {
        this.promotion_id = promotion_id;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getIs_tax_included() {
        return is_tax_included;
    }

    public void setIs_tax_included(String is_tax_included) {
        this.is_tax_included = is_tax_included;
    }

    public String getCount_views() {
        return count_views;
    }

    public void setCount_views(String count_views) {
        this.count_views = count_views;
    }

    public String getCount_comments() {
        return count_comments;
    }

    public void setCount_comments(String count_comments) {
        this.count_comments = count_comments;
    }

    public String getCount_purchase() {
        return count_purchase;
    }

    public void setCount_purchase(String count_purchase) {
        this.count_purchase = count_purchase;
    }

    public String getCount_likes() {
        return count_likes;
    }

    public void setCount_likes(String count_likes) {
        this.count_likes = count_likes;
    }

    public String getCount_rates() {
        return count_rates;
    }

    public void setCount_rates(String count_rates) {
        this.count_rates = count_rates;
    }

    public String getRates() {
        return rates;
    }

    public void setRates(String rates) {
        this.rates = rates;
    }

    public String getQrcode_image() {
        return qrcode_image;
    }

    public void setQrcode_image(String qrcode_image) {
        this.qrcode_image = qrcode_image;
    }

    public String getBarcode_image() {
        return barcode_image;
    }

    public void setBarcode_image(String barcode_image) {
        this.barcode_image = barcode_image;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getCreated_user() {
        return created_user;
    }

    public void setCreated_user(String created_user) {
        this.created_user = created_user;
    }

    public String getModified_date() {
        return modified_date;
    }

    public void setModified_date(String modified_date) {
        this.modified_date = modified_date;
    }

    public String getModified_user() {
        return modified_user;
    }

    public void setModified_user(String modified_user) {
        this.modified_user = modified_user;
    }

    public String getApplication_id() {
        return application_id;
    }

    public void setApplication_id(String application_id) {
        this.application_id = application_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(is_active);
        parcel.writeInt(is_hot);
        parcel.writeInt(is_top);
        parcel.writeTypedList(productObjList);
        parcel.writeStringList(image_files);
        parcel.writeTypedList(colors);
        parcel.writeTypedList(sizes);
        parcel.writeString(id);
        parcel.writeString(thumbnail);
        parcel.writeString(image);
        parcel.writeString(banner);
        parcel.writeString(code);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(content);
        parcel.writeString(cost);
        parcel.writeString(unit);
        parcel.writeString(currency);
        parcel.writeString(type);
        parcel.writeString(status);
        parcel.writeString(brand);
        parcel.writeString(category_id);
        parcel.writeString(is_promotion);
        parcel.writeString(promotion_id);
        parcel.writeString(tags);
        parcel.writeString(quantity);
        parcel.writeString(discount);
        parcel.writeString(tax);
        parcel.writeString(is_tax_included);
        parcel.writeString(count_views);
        parcel.writeString(count_comments);
        parcel.writeString(count_purchase);
        parcel.writeString(count_likes);
        parcel.writeString(count_rates);
        parcel.writeString(rates);
        parcel.writeString(qrcode_image);
        parcel.writeString(barcode_image);
        parcel.writeString(created_date);
        parcel.writeString(created_user);
        parcel.writeString(modified_date);
        parcel.writeString(modified_user);
        parcel.writeString(application_id);
        parcel.writeString(color);
        parcel.writeString(size);
        parcel.writeInt(number);
        parcel.writeInt(is_prize);
        parcel.writeInt(is_favourite);
        parcel.writeDouble(totalMoney);
        parcel.writeDouble(price);
        parcel.writeDouble(old_price);
    }
}

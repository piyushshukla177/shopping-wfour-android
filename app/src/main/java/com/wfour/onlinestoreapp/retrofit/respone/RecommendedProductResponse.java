package com.wfour.onlinestoreapp.retrofit.respone;

import java.util.List;

public class RecommendedProductResponse {

    private String message;
    private int code;
    private int total_items;
    private int page_index;
    private int page_size;
    private int total_page;
    private List<Data> data;
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTotal_items() {
        return total_items;
    }

    public void setTotal_items(int total_items) {
        this.total_items = total_items;
    }

    public int getPage_index() {
        return page_index;
    }

    public void setPage_index(int page_index) {
        this.page_index = page_index;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Data {
        private int is_favourite;
        private List<String> colors;
        private List<String> sizes;
        private int viewed;
        private String is_disabled;
        private String application_id;
        private String modified_user;
        private String modified_date;
        private String created_user;
        private String created_date;
        private String barcode_image;
        private String qrcode_image;
        private String rates;
        private int count_rates;
        private int count_likes;
        private int count_purchase;
        private int count_comments;
        private int count_views;
        private String is_tax_included;
        private String tax;
        private String discount;
        private String quantity;
        private String tags;
        private String promotion_id;
        private String is_recomended;
        private int is_popular;
        private String is_promotion;
        private int is_top;
        private int is_hot;
        private int is_prize;
        private int is_active;
        private String category_id;
        private String brand;
        private String status;
        private String type;
        private String currency;
        private String unit;
        private String old_price;
        private String price;
        private String cost;
        private String content;
        private String overview;
        private String title;
        private String code;
        private List<String> image_files;
        private String banner;
        private String image;
        private String thumbnail;
        private int id;

        public int getIs_favourite() {
            return is_favourite;
        }

        public void setIs_favourite(int is_favourite) {
            this.is_favourite = is_favourite;
        }

        public List<String> getColors() {
            return colors;
        }

        public void setColors(List<String> colors) {
            this.colors = colors;
        }

        public List<String> getSizes() {
            return sizes;
        }

        public void setSizes(List<String> sizes) {
            this.sizes = sizes;
        }

        public int getViewed() {
            return viewed;
        }

        public void setViewed(int viewed) {
            this.viewed = viewed;
        }

        public String getIs_disabled() {
            return is_disabled;
        }

        public void setIs_disabled(String is_disabled) {
            this.is_disabled = is_disabled;
        }

        public String getApplication_id() {
            return application_id;
        }

        public void setApplication_id(String application_id) {
            this.application_id = application_id;
        }

        public String getModified_user() {
            return modified_user;
        }

        public void setModified_user(String modified_user) {
            this.modified_user = modified_user;
        }

        public String getModified_date() {
            return modified_date;
        }

        public void setModified_date(String modified_date) {
            this.modified_date = modified_date;
        }

        public String getCreated_user() {
            return created_user;
        }

        public void setCreated_user(String created_user) {
            this.created_user = created_user;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }

        public String getBarcode_image() {
            return barcode_image;
        }

        public void setBarcode_image(String barcode_image) {
            this.barcode_image = barcode_image;
        }

        public String getQrcode_image() {
            return qrcode_image;
        }

        public void setQrcode_image(String qrcode_image) {
            this.qrcode_image = qrcode_image;
        }

        public String getRates() {
            return rates;
        }

        public void setRates(String rates) {
            this.rates = rates;
        }

        public int getCount_rates() {
            return count_rates;
        }

        public void setCount_rates(int count_rates) {
            this.count_rates = count_rates;
        }

        public int getCount_likes() {
            return count_likes;
        }

        public void setCount_likes(int count_likes) {
            this.count_likes = count_likes;
        }

        public int getCount_purchase() {
            return count_purchase;
        }

        public void setCount_purchase(int count_purchase) {
            this.count_purchase = count_purchase;
        }

        public int getCount_comments() {
            return count_comments;
        }

        public void setCount_comments(int count_comments) {
            this.count_comments = count_comments;
        }

        public int getCount_views() {
            return count_views;
        }

        public void setCount_views(int count_views) {
            this.count_views = count_views;
        }

        public String getIs_tax_included() {
            return is_tax_included;
        }

        public void setIs_tax_included(String is_tax_included) {
            this.is_tax_included = is_tax_included;
        }

        public String getTax() {
            return tax;
        }

        public void setTax(String tax) {
            this.tax = tax;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getPromotion_id() {
            return promotion_id;
        }

        public void setPromotion_id(String promotion_id) {
            this.promotion_id = promotion_id;
        }

        public String getIs_recomended() {
            return is_recomended;
        }

        public void setIs_recomended(String is_recomended) {
            this.is_recomended = is_recomended;
        }

        public int getIs_popular() {
            return is_popular;
        }

        public void setIs_popular(int is_popular) {
            this.is_popular = is_popular;
        }

        public String getIs_promotion() {
            return is_promotion;
        }

        public void setIs_promotion(String is_promotion) {
            this.is_promotion = is_promotion;
        }

        public int getIs_top() {
            return is_top;
        }

        public void setIs_top(int is_top) {
            this.is_top = is_top;
        }

        public int getIs_hot() {
            return is_hot;
        }

        public void setIs_hot(int is_hot) {
            this.is_hot = is_hot;
        }

        public int getIs_prize() {
            return is_prize;
        }

        public void setIs_prize(int is_prize) {
            this.is_prize = is_prize;
        }

        public int getIs_active() {
            return is_active;
        }

        public void setIs_active(int is_active) {
            this.is_active = is_active;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getOld_price() {
            return old_price;
        }

        public void setOld_price(String old_price) {
            this.old_price = old_price;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public List<String> getImage_files() {
            return image_files;
        }

        public void setImage_files(List<String> image_files) {
            this.image_files = image_files;
        }

        public String getBanner() {
            return banner;
        }

        public void setBanner(String banner) {
            this.banner = banner;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}

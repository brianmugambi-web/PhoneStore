package com.example.dukastore.models;

import java.io.Serializable;

public class RecommendModel implements Serializable {

    String name;

    String img_url;

    String  price;

    String description;

    String totalPrice;

    public RecommendModel() {
    }


    public RecommendModel(String name, String img_url, String price, String description, String totalPrice) {
        this.name = name;
        this.img_url = img_url;
        this.price = price;
        this.description = description;
        this.totalPrice = totalPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}


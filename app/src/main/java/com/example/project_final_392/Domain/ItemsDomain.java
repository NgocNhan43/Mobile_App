package com.example.project_final_392.Domain;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemsDomain implements Serializable {
    private String title;
    private String description;
    private ArrayList<String> picUrl;
    private double price;
    private int oldPrice;
    private int review;
    private float rating;
    private int NumberinCart;

    public ItemsDomain() {
    }

    public ItemsDomain(String title, String description, ArrayList<String> picUrl, double price, int oldPrice, int review, float rating) {
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.price = price;
        this.oldPrice = oldPrice;
        this.review = review;
        this.rating = rating;
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

    public ArrayList<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(ArrayList<String> picUrl) {
        this.picUrl = picUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(int oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getNumberinCart() {
        return NumberinCart;
    }

    public void setNumberinCart(int numberinCart) {
        this.NumberinCart = numberinCart;
    }


}

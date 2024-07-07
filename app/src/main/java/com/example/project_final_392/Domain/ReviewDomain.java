package com.example.project_final_392.Domain;

public class ReviewDomain {
    private String Name;
    private String Description;
    private String PicUrl;
    private double rating;
    private int ItemId;

    public ReviewDomain() {
    }

    public ReviewDomain(String name, String description, String picUrl, double rating, int itemId) {
        Name = name;
        Description = description;
        PicUrl = picUrl;
        this.rating = rating;
        ItemId = itemId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }
}

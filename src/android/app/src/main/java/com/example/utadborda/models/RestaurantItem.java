package com.example.utadborda.models;

import java.util.List;

/**
 * Individual restaurant stored in API
 */
public class RestaurantItem {
    private String id;
    private String name;
    private String phone;
    private String address;
    private List<String> imageUrl;
    private String website;
    private double latitute;
    private double longitute;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitute() {
        return latitute;
    }

    public void setLatitute(double latitute) {
        this.latitute = latitute;
    }

    public double getLongitute() {
        return longitute;
    }

    public void setLongitute(double longitute) {
        this.longitute = longitute;
    }

    public void setImageUrl(List<String> image) {
        this.imageUrl = image;
    }

    public String getImageUrl() {
        if (imageUrl.size() == 0) {
            return "";
        }
        return imageUrl.get(0);
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

}

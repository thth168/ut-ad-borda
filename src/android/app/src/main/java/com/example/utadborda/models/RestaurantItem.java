package com.example.utadborda.models;

import java.util.ArrayList;
import java.util.List;

public class RestaurantItem {
    private String id;
    private String name;
    private String phone;
    private String address;
    private List<String> imageUrl;
    private String website;
    private double latitude;
    private double longitude;
    private int swipes;

    public RestaurantItem() {}

    public RestaurantItem(
        String id,
        String name,
        String phone,
        String address,
        String imageUrl,
        String website,
        double latitude,
        double longitude
    ) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.imageUrl = new ArrayList<String>();
        this.imageUrl.add(imageUrl);
        this.website = website;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getSwipes() {
        return swipes;
    }

    public void setSwipes(int swipes) {
        this.swipes = swipes;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

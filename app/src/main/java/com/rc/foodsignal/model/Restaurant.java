package com.rc.foodsignal.model;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class Restaurant {

    private String id = "";
    private String name = "";
    private String image = "";
    private String lat = "";
    private String lng = "";
    private String email = "";
    private String phone = "";
    private String address = "";
    private String password = "";
    private String is_restaurant = "";
    private String sms_notification = "";
    private String push_notification = "";
    private String distance = "";
    private ArrayList<FoodItem> item_details = new ArrayList<FoodItem>();

    public Restaurant(String id, String name, String image, String lat, String lng, String email, String phone, String address, String password, String is_restaurant, String sms_notification, String push_notification, String distance, ArrayList<FoodItem> item_details) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.lat = lat;
        this.lng = lng;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.is_restaurant = is_restaurant;
        this.sms_notification = sms_notification;
        this.push_notification = push_notification;
        this.distance = distance;
        this.item_details = item_details;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIs_restaurant() {
        return is_restaurant;
    }

    public void setIs_restaurant(String is_restaurant) {
        this.is_restaurant = is_restaurant;
    }

    public String getSms_notification() {
        return sms_notification;
    }

    public void setSms_notification(String sms_notification) {
        this.sms_notification = sms_notification;
    }

    public String getPush_notification() {
        return push_notification;
    }

    public void setPush_notification(String push_notification) {
        this.push_notification = push_notification;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public ArrayList<FoodItem> getItem_details() {
        return item_details;
    }

    public void setItem_details(ArrayList<FoodItem> item_details) {
        this.item_details = item_details;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", password='" + password + '\'' +
                ", is_restaurant='" + is_restaurant + '\'' +
                ", sms_notification='" + sms_notification + '\'' +
                ", push_notification='" + push_notification + '\'' +
                ", distance='" + distance + '\'' +
                ", item_details=" + item_details +
                '}';
    }
}

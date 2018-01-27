package com.rc.foodsignal.model;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class RestaurantLoginData extends ResponseBase {

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
    private int is_address_added = 0;
    private ArrayList<Location> selected_address = new ArrayList<>();

    public RestaurantLoginData(String id, String name, String image, String lat, String lng, String email, String phone, String address, String password, String is_restaurant, String sms_notification, String push_notification, int is_address_added, ArrayList<Location> selected_address) {
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
        this.is_address_added = is_address_added;
        this.selected_address = selected_address;
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

    public int getIs_address_added() {
        return is_address_added;
    }

    public void setIs_address_added(int is_address_added) {
        this.is_address_added = is_address_added;
    }

    public ArrayList<Location> getSelected_address() {
        return selected_address;
    }

    public void setSelected_address(ArrayList<Location> selected_address) {
        this.selected_address = selected_address;
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
                ", is_address_added=" + is_address_added +
                ", selected_address=" + selected_address +
                '}';
    }
}

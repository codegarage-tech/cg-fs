package com.rc.foodsignal.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class Restaurant implements Parcelable {

    public static final transient int TYPE_RESTAURANT = 1;
    public static final transient int TYPE_IMAGE = 2;

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
    private ArrayList<FoodItem> menu_details = new ArrayList<FoodItem>();

    public Restaurant(String id, String name, String image, String lat, String lng, String email, String phone, String address, String password, String is_restaurant, String sms_notification, String push_notification, String distance, ArrayList<FoodItem> menu_details) {
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
        this.menu_details = menu_details;
    }

    public int getTypeRestaurant() {
        return ((menu_details.size() > 0 ? TYPE_RESTAURANT : TYPE_IMAGE));
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

    public ArrayList<FoodItem> getMenu_details() {
        return menu_details;
    }

    public void setMenu_details(ArrayList<FoodItem> menu_details) {
        this.menu_details = menu_details;
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
                ", menu_details=" + menu_details +
                '}';
    }

    /**************************
     * Methods for parcelable *
     **************************/
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(address);
        dest.writeString(password);
        dest.writeString(is_restaurant);
        dest.writeString(sms_notification);
        dest.writeString(push_notification);
        dest.writeString(distance);
        dest.writeList(menu_details);
    }

    // Creator
    public static final Creator CREATOR = new Creator() {
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    // "De-parcel object
    public Restaurant(Parcel in) {
        id = in.readString();
        name = in.readString();
        image = in.readString();
        lat = in.readString();
        lng = in.readString();
        email = in.readString();
        phone = in.readString();
        address = in.readString();
        password = in.readString();
        is_restaurant = in.readString();
        sms_notification = in.readString();
        push_notification = in.readString();
        distance = in.readString();
        menu_details = in.readArrayList(FoodImage.class.getClassLoader());
    }
}
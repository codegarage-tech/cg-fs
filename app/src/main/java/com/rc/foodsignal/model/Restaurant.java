package com.rc.foodsignal.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
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
    private String restaurant_category_id = "";
    private String shipping_cost="";
    private String menu_id = "";
    private String restaurant_category_name = "";
    private String distance = "";
    private ArrayList<FoodCategoryDetail> food_category_details = new ArrayList<FoodCategoryDetail>();
    private ArrayList<FoodCategoryDetail> offer_details = new ArrayList<FoodCategoryDetail>();

    public Restaurant(String id, String name, String image, String lat, String lng, String email, String phone, String address, String password, String is_restaurant, String sms_notification, String push_notification, String restaurant_category_id, String shipping_cost, String menu_id, String restaurant_category_name, String distance, ArrayList<FoodCategoryDetail> food_category_details, ArrayList<FoodCategoryDetail> offer_details) {
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
        this.restaurant_category_id = restaurant_category_id;
        this.shipping_cost = shipping_cost;
        this.menu_id = menu_id;
        this.restaurant_category_name = restaurant_category_name;
        this.distance = distance;
        this.food_category_details = food_category_details;
        this.offer_details = offer_details;
    }

    public int getTypeRestaurant() {
        return ((food_category_details != null && food_category_details.size() > 0) ? TYPE_RESTAURANT : TYPE_IMAGE);
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

    public String getRestaurant_category_id() {
        return restaurant_category_id;
    }

    public void setRestaurant_category_id(String restaurant_category_id) {
        this.restaurant_category_id = restaurant_category_id;
    }

    public String getShipping_cost() {
        return shipping_cost;
    }

    public void setShipping_cost(String shipping_cost) {
        this.shipping_cost = shipping_cost;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getRestaurant_category_name() {
        return restaurant_category_name;
    }

    public void setRestaurant_category_name(String restaurant_category_name) {
        this.restaurant_category_name = restaurant_category_name;
    }

    public ArrayList<FoodCategoryDetail> getFood_category_details() {
        return food_category_details;
    }

    public void setFood_category_details(ArrayList<FoodCategoryDetail> food_category_details) {
        this.food_category_details = food_category_details;
    }

    public ArrayList<FoodCategoryDetail> getOffer_details() {
        return offer_details;
    }

    public void setOffer_details(ArrayList<FoodCategoryDetail> offer_details) {
        this.offer_details = offer_details;
    }

    public ArrayList<FoodItem> getAllFoodItems() {
        ArrayList<FoodItem> mItems = new ArrayList<>();
        if (getFood_category_details() != null && getFood_category_details().size() > 0) {
            for (int i = 0; i < getFood_category_details().size(); i++) {
                FoodCategoryDetail foodCategoryDetail = getFood_category_details().get(i);
                if (foodCategoryDetail.getMenu_details() != null && foodCategoryDetail.getMenu_details().size() > 0) {
                    for (int j = 0; j < foodCategoryDetail.getMenu_details().size(); j++) {
                        FoodItem foodItem = foodCategoryDetail.getMenu_details().get(j);
                        mItems.add(foodItem);
                    }
                }
            }
        }
        return mItems;
    }

    public ArrayList<FoodItem> getAllOfferFoodItems() {
        ArrayList<FoodItem> mItems = new ArrayList<>();
        if (getOffer_details() != null && getOffer_details().size() > 0) {
            for (int i = 0; i < getOffer_details().size(); i++) {
                FoodCategoryDetail foodCategoryDetail = getOffer_details().get(i);
                if (foodCategoryDetail.getMenu_details() != null && foodCategoryDetail.getMenu_details().size() > 0) {
                    for (int j = 0; j < foodCategoryDetail.getMenu_details().size(); j++) {
                        FoodItem foodItem = foodCategoryDetail.getMenu_details().get(j);
                        mItems.add(foodItem);
                    }
                }
            }
        }
        return mItems;
    }

    public static int getTypeImage() {
        return TYPE_IMAGE;
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
                ", restaurant_category_id='" + restaurant_category_id + '\'' +
                ", shipping_cost='" + shipping_cost + '\'' +
                ", menu_id='" + menu_id + '\'' +
                ", restaurant_category_name='" + restaurant_category_name + '\'' +
                ", distance='" + distance + '\'' +
                ", food_category_details=" + food_category_details +
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
        dest.writeString(restaurant_category_id);
        dest.writeString(shipping_cost);
        dest.writeString(menu_id);
        dest.writeString(restaurant_category_name);
        dest.writeString(distance);
        dest.writeList(food_category_details);
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
        restaurant_category_id = in.readString();
        shipping_cost = in.readString();
        menu_id = in.readString();
        restaurant_category_name = in.readString();
        distance = in.readString();
        food_category_details = in.readArrayList(FoodCategoryDetail.class.getClassLoader());
    }
}
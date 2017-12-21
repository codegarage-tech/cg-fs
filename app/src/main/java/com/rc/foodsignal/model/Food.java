package com.rc.foodsignal.model;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class Food {

    private String id = "";
    private String name = "";
    private String menu_id = "";
    private String price = "";
    private String image = "";
    private String restaurant_id = "";
    private String lat = "";
    private String lng = "";
    private double distance = 0.0;
    private ArrayList<Restaurant> restaurants_details = new ArrayList<Restaurant>();

    public Food(String id, String name, String menu_id, String price, String image, String restaurant_id, String lat, String lng, double distance, ArrayList<Restaurant> restaurants_details) {
        this.id = id;
        this.name = name;
        this.menu_id = menu_id;
        this.price = price;
        this.image = image;
        this.restaurant_id = restaurant_id;
        this.lat = lat;
        this.lng = lng;
        this.distance = distance;
        this.restaurants_details = restaurants_details;
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

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public ArrayList<Restaurant> getRestaurants_details() {
        return restaurants_details;
    }

    public void setRestaurants_details(ArrayList<Restaurant> restaurants_details) {
        this.restaurants_details = restaurants_details;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", menu_id='" + menu_id + '\'' +
                ", price='" + price + '\'' +
                ", image='" + image + '\'' +
                ", restaurant_id='" + restaurant_id + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", distance=" + distance +
                ", restaurants_details=" + restaurants_details +
                '}';
    }
}

package com.rc.foodsignal.model;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class DataFoodItem extends ResponseBase {

    private ArrayList<FoodItem> data = new ArrayList<FoodItem>();
    private Restaurant restaurant;

    public DataFoodItem() {
    }

    public DataFoodItem(ArrayList<FoodItem> data, Restaurant restaurant) {
        this.data = data;
        this.restaurant = restaurant;
    }

    public ArrayList<FoodItem> getData() {
        return data;
    }

    public void setData(ArrayList<FoodItem> data) {
        this.data = data;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "{" +
                "data=" + data +
                ", restaurant=" + restaurant +
                '}';
    }
}
package com.rc.foodsignal.model;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class DataRestaurantCategory extends ResponseBase {

    private ArrayList<RestaurantCategory> data = new ArrayList<RestaurantCategory>();

    public DataRestaurantCategory() {
    }

    public DataRestaurantCategory(ArrayList<RestaurantCategory> data) {
        this.data = data;
    }

    public ArrayList<RestaurantCategory> getData() {
        return data;
    }

    public void setData(ArrayList<RestaurantCategory> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "data=" + data +
                '}';
    }
}

package com.rc.foodsignal.model;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class DataFoodItem extends ResponseBase {

    private ArrayList<FoodItem> data = new ArrayList<FoodItem>();

    public DataFoodItem() {
    }

    public DataFoodItem(ArrayList<FoodItem> data) {
        this.data = data;
    }

    public ArrayList<FoodItem> getData() {
        return data;
    }

    public void setData(ArrayList<FoodItem> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "data=" + data +
                '}';
    }
}
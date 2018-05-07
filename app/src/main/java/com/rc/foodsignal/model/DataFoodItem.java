package com.rc.foodsignal.model;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class DataFoodItem extends ResponseBase {

    private ArrayList<FoodItem> data = new ArrayList<FoodItem>();

    private float shippingCost = 0.0f;

    public DataFoodItem() {
    }

    public DataFoodItem(ArrayList<FoodItem> data, float shippingCost) {
        this.data = data;
        this.shippingCost = shippingCost;
    }

    public ArrayList<FoodItem> getData() {
        return data;
    }

    public void setData(ArrayList<FoodItem> data) {
        this.data = data;
    }

    public float getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(float shippingCost) {
        this.shippingCost = shippingCost;
    }

    @Override
    public String toString() {
        return "{" +
                "data=" + data +
                ", shippingCost=" + shippingCost +
                '}';
    }
}
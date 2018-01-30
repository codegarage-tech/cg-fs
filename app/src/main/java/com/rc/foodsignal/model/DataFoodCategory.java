package com.rc.foodsignal.model;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class DataFoodCategory extends ResponseBase {

    private ArrayList<FoodCategory> data = new ArrayList<FoodCategory>();

    public DataFoodCategory() {
    }

    public DataFoodCategory(ArrayList<FoodCategory> data) {
        this.data = data;
    }

    public ArrayList<FoodCategory> getData() {
        return data;
    }

    public void setData(ArrayList<FoodCategory> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "data=" + data +
                '}';
    }
}

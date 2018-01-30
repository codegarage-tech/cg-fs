package com.rc.foodsignal.model;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class ResponseRestaurantMenu extends ResponseBase {

    private String status = "";
    private ArrayList<FoodItem> data = new ArrayList<FoodItem>();

    public ResponseRestaurantMenu() {
    }

    public ResponseRestaurantMenu(String status, ArrayList<FoodItem> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<FoodItem> getData() {
        return data;
    }

    public void setData(ArrayList<FoodItem> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseRestaurantMenu{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}

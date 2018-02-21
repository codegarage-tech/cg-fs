package com.rc.foodsignal.model;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class ResponseRestaurantCategory extends ResponseBase {

    private String status = "";
    private ArrayList<RestaurantCategory> data = new ArrayList<RestaurantCategory>();

    public ResponseRestaurantCategory() {
    }

    public ResponseRestaurantCategory(String status, ArrayList<RestaurantCategory> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<RestaurantCategory> getData() {
        return data;
    }

    public void setData(ArrayList<RestaurantCategory> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseRestaurantCategory{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}

package com.rc.foodsignal.model;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class ResponseFoodCategory extends ResponseBase {

    private String status = "";
    private ArrayList<FoodCategory> data = new ArrayList<FoodCategory>();

    public ResponseFoodCategory() {
    }

    public ResponseFoodCategory(String status, ArrayList<FoodCategory> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<FoodCategory> getData() {
        return data;
    }

    public void setData(ArrayList<FoodCategory> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseFoodCategory{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}

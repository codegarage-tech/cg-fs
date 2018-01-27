package com.rc.foodsignal.model;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class ResponseRestaurantLoginData extends ResponseBase {

    private String status = "";
    private String msg = "";
    private ArrayList<RestaurantLoginData> data = new ArrayList<RestaurantLoginData>();

    public ResponseRestaurantLoginData() {
    }

    public ResponseRestaurantLoginData(String status, String msg, ArrayList<RestaurantLoginData> data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<RestaurantLoginData> getData() {
        return data;
    }

    public void setData(ArrayList<RestaurantLoginData> user_details) {
        this.data = user_details;
    }
}

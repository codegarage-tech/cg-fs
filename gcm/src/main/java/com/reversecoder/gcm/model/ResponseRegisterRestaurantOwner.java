package com.reversecoder.gcm.model;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ResponseRegisterRestaurantOwner {

    private String status = "";
    private ArrayList<RegisterRestaurantOwner> data = new ArrayList<>();

    public ResponseRegisterRestaurantOwner() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<RegisterRestaurantOwner> getData() {
        return data;
    }

    public void setData(ArrayList<RegisterRestaurantOwner> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseRegisterAppUser{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }

    /**************************
     * Methods for convertion *
     **************************/
    public static <T> T convertFromStringToObject(String jsonString, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, clazz);
    }

    public static <T> String convertFromObjectToString(T object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}
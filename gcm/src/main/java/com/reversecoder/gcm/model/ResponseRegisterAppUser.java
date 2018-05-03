package com.reversecoder.gcm.model;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class ResponseRegisterAppUser {

    private String status = "";
    private ArrayList<RegisterAppUser> data = new ArrayList<>();

    public ResponseRegisterAppUser() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<RegisterAppUser> getData() {
        return data;
    }

    public void setData(ArrayList<RegisterAppUser> data) {
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
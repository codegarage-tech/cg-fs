package com.reversecoder.gcm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class RegisterApp implements Parcelable {

    private String id = "";
    private String device_id = "";
    private String name = "";
    private String phone = "";
    private String email = "";
    private String unique_id = "";
    private String type = "";

    public RegisterApp() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", device_id='" + device_id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", unique_id='" + unique_id + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    /**************************
     * Methods for parcelable *
     **************************/
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(device_id);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(unique_id);
        dest.writeString(type);
    }

    // Creator
    public static final Creator CREATOR
            = new Creator() {
        public RegisterApp createFromParcel(Parcel in) {
            return new RegisterApp(in);
        }

        public RegisterApp[] newArray(int size) {
            return new RegisterApp[size];
        }
    };

    // "De-parcel object
    public RegisterApp(Parcel in) {
        id = in.readString();
        device_id = in.readString();
        name = in.readString();
        phone = in.readString();
        email = in.readString();
        unique_id = in.readString();
        type = in.readString();
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
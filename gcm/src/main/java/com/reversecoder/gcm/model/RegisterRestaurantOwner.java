package com.reversecoder.gcm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class RegisterRestaurantOwner implements Parcelable {

    private String id = "";
    private String device_id = "";
    private String device_type = "";
    private String unique_id = "";
    private String restaurant_id = "";

    public RegisterRestaurantOwner() {
    }

    public RegisterRestaurantOwner(String id, String device_id, String device_type, String unique_id, String restaurant_id) {
        this.id = id;
        this.device_id = device_id;
        this.device_type = device_type;
        this.unique_id = unique_id;
        this.restaurant_id = restaurant_id;
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

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegisterRestaurantOwner)) return false;

        RegisterRestaurantOwner that = (RegisterRestaurantOwner) o;

        if (!getId().equals(that.getId())) return false;
        if (!getDevice_id().equals(that.getDevice_id())) return false;
        if (!getDevice_type().equals(that.getDevice_type())) return false;
        if (!getUnique_id().equals(that.getUnique_id())) return false;
        return getRestaurant_id().equals(that.getRestaurant_id());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getDevice_id().hashCode();
        result = 31 * result + getDevice_type().hashCode();
        result = 31 * result + getUnique_id().hashCode();
        result = 31 * result + getRestaurant_id().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", device_id='" + device_id + '\'' +
                ", device_type='" + device_type + '\'' +
                ", unique_id='" + unique_id + '\'' +
                ", restaurant_id='" + restaurant_id + '\'' +
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
        dest.writeString(device_type);
        dest.writeString(unique_id);
        dest.writeString(restaurant_id);
    }

    // Creator
    public static final Creator CREATOR
            = new Creator() {
        public RegisterRestaurantOwner createFromParcel(Parcel in) {
            return new RegisterRestaurantOwner(in);
        }

        public RegisterRestaurantOwner[] newArray(int size) {
            return new RegisterRestaurantOwner[size];
        }
    };

    // "De-parcel object
    public RegisterRestaurantOwner(Parcel in) {
        id = in.readString();
        device_id = in.readString();
        device_type = in.readString();
        unique_id = in.readString();
        restaurant_id = in.readString();
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
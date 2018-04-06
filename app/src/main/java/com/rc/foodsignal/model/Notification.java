package com.rc.foodsignal.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class Notification extends ResponseBase implements Parcelable {

    private String id = "";
    private String app_user_id = "";
    private String notification = "";
    private String restaurant_id = "";
    private String datetime = "";
    private ArrayList<Offer> notification_details = new ArrayList<>();

    public Notification(String id, String app_user_id, String notification, String restaurant_id, String datetime, ArrayList<Offer> notification_details) {
        this.id = id;
        this.app_user_id = app_user_id;
        this.notification = notification;
        this.restaurant_id = restaurant_id;
        this.datetime = datetime;
        this.notification_details = notification_details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApp_user_id() {
        return app_user_id;
    }

    public void setApp_user_id(String app_user_id) {
        this.app_user_id = app_user_id;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public ArrayList<Offer> getNotification_details() {
        return notification_details;
    }

    public void setNotification_details(ArrayList<Offer> notification_details) {
        this.notification_details = notification_details;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", app_user_id='" + app_user_id + '\'' +
                ", notification='" + notification + '\'' +
                ", restaurant_id='" + restaurant_id + '\'' +
                ", datetime='" + datetime + '\'' +
                ", notification_details=" + notification_details +
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
        dest.writeString(app_user_id);
        dest.writeString(notification);
        dest.writeString(restaurant_id);
        dest.writeString(datetime);
        dest.writeList(notification_details);
    }

    // Creator
    public static final Creator CREATOR = new Creator() {
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    // "De-parcel object
    public Notification(Parcel in) {
        id = in.readString();
        app_user_id = in.readString();
        notification = in.readString();
        restaurant_id = in.readString();
        datetime = in.readString();
        notification_details = in.readArrayList(Offer.class.getClassLoader());
    }
}

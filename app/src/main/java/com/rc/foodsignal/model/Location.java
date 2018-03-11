package com.rc.foodsignal.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class Location extends ResponseBase implements Parcelable {

    private String id = "";
    private String user_id = "";
    private String street = "";
    private String city = "";
    private String state = "";
    private String zip = "";
    private String country = "";
    private String lat = "";
    private String lng = "";

    public Location(String id, String user_id, String street, String city, String state, String zip, String country, String lat, String lng) {
        this.id = id;
        this.user_id = user_id;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.lat = lat;
        this.lng = lng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;

        Location that = (Location) o;

        if (!getId().equals(that.getId())) return false;
        if (!getUser_id().equals(that.getUser_id())) return false;
        if (!getStreet().equals(that.getStreet())) return false;
        if (!getCity().equals(that.getCity())) return false;
        if (!getState().equals(that.getState())) return false;
        if (!getZip().equals(that.getZip())) return false;
        if (!getCountry().equals(that.getCountry())) return false;
        if (!getLat().equals(that.getLat())) return false;
        return getLng().equals(that.getLng());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();

        result = 31 * result + getUser_id().hashCode();
        result = 31 * result + getStreet().hashCode();
        result = 31 * result + getCity().hashCode();
        result = 31 * result + getState().hashCode();
        result = 31 * result + getZip().hashCode();
        result = 31 * result + getCountry().hashCode();
        result = 31 * result + getLat().hashCode();
        result = 31 * result + getLng().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", country='" + country + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
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
        dest.writeString(user_id);
        dest.writeString(street);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(zip);
        dest.writeString(country);
        dest.writeString(lat);
        dest.writeString(lng);
    }

    // Creator
    public static final Creator CREATOR
            = new Creator() {
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    // "De-parcel object
    public Location(Parcel in) {
        id = in.readString();
        user_id = in.readString();
        street = in.readString();
        city = in.readString();
        state = in.readString();
        zip = in.readString();
        country = in.readString();
        lat = in.readString();
        lng = in.readString();
    }
}
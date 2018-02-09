package com.rc.foodsignal.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class FoodImage extends ResponseBase implements Parcelable {

    private String id = "";
    private String item_id = "";
    private String images = "";

    public FoodImage(String id, String item_id, String images) {
        this.id = id;
        this.item_id = item_id;
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getImage() {
        return images;
    }

    public void setImage(String image) {
        this.images = image;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", item_id='" + item_id + '\'' +
                ", images='" + images + '\'' +
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
        dest.writeString(item_id);
        dest.writeString(images);
    }

    // Creator
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public FoodImage createFromParcel(Parcel in) {
            return new FoodImage(in);
        }

        public FoodImage[] newArray(int size) {
            return new FoodImage[size];
        }
    };

    // "De-parcel object
    public FoodImage(Parcel in) {
        id = in.readString();
        item_id = in.readString();
        images = in.readString();
    }
}
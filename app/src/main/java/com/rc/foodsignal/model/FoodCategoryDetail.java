package com.rc.foodsignal.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class FoodCategoryDetail extends ResponseBase implements Parcelable {

    private String id = "";
    private String name = "";
    private ArrayList<FoodItem> menu_details = new ArrayList<FoodItem>();

    public FoodCategoryDetail(String id, String name, ArrayList<FoodItem> menu_details) {
        this.id = id;
        this.name = name;
        this.menu_details = menu_details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<FoodItem> getMenu_details() {
        return menu_details;
    }

    public void setMenu_details(ArrayList<FoodItem> menu_details) {
        this.menu_details = menu_details;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", menu_details=" + menu_details +
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
        dest.writeString(name);
        dest.writeList(menu_details);
    }

    // Creator
    public static final Creator CREATOR = new Creator() {
        public FoodCategoryDetail createFromParcel(Parcel in) {
            return new FoodCategoryDetail(in);
        }

        public FoodCategoryDetail[] newArray(int size) {
            return new FoodCategoryDetail[size];
        }
    };

    // "De-parcel object
    public FoodCategoryDetail(Parcel in) {
        id = in.readString();
        name = in.readString();
        menu_details = in.readArrayList(FoodItem.class.getClassLoader());
    }
}

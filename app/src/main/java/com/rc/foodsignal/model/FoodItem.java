package com.rc.foodsignal.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class FoodItem extends ResponseBase implements Parcelable {

    private String id = "";
    private String name = "";
    private String menu_id = "";
    private String price = "";
    private String restaurant_id = "";
    private String ingredients = "";
    private String category_name = "";
    private ArrayList<FoodImage> images = new ArrayList<>();

    public FoodItem(String id, String name, String menu_id, String price, String restaurant_id, String ingredients, String category_name, ArrayList<FoodImage> images) {
        this.id = id;
        this.name = name;
        this.menu_id = menu_id;
        this.price = price;
        this.restaurant_id = restaurant_id;
        this.ingredients = ingredients;
        this.category_name = category_name;
        this.images = images;
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

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public ArrayList<FoodImage> getImages() {
        return images;
    }

    public void setImages(ArrayList<FoodImage> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", menu_id='" + menu_id + '\'' +
                ", price='" + price + '\'' +
                ", restaurant_id='" + restaurant_id + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", category_name='" + category_name + '\'' +
                ", images=" + images +
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
        dest.writeString(menu_id);
        dest.writeString(price);
        dest.writeString(restaurant_id);
        dest.writeString(ingredients);
        dest.writeString(category_name);
        dest.writeList(images);
    }

    // Creator
    public static final Creator CREATOR = new Creator() {
        public FoodItem createFromParcel(Parcel in) {
            return new FoodItem(in);
        }

        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };

    // "De-parcel object
    public FoodItem(Parcel in) {
        id = in.readString();
        name = in.readString();
        menu_id = in.readString();
        price = in.readString();
        restaurant_id = in.readString();
        ingredients = in.readString();
        category_name = in.readString();
        images = in.readArrayList(FoodImage.class.getClassLoader());
    }
}

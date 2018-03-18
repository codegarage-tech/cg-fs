package com.rc.foodsignal.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class FoodItem extends ResponseBase implements Parcelable {

    private transient boolean isExpanded = false;
    private transient boolean isSelected = false;
    private transient int offerPercentage = 0;
    private String id = "";
    private String name = "";
    private String menu_id = "";
    private String menu_image = "";
    private String price = "";
    private String restaurant_id = "";
    private String ingredients = "";
    private String category_name = "";
    private ArrayList<FoodImage> images = new ArrayList<>();
    //For offer
    private String offer_title = "";
    private String offer_price = "";

    public FoodItem(boolean isSelected, int offerPercentage, String id, String name, String menu_id, String menu_image, String price, String restaurant_id, String ingredients, String category_name, ArrayList<FoodImage> images, String offer_title, String offer_price) {
        this.isSelected = isSelected;
        this.offerPercentage = offerPercentage;
        this.id = id;
        this.name = name;
        this.menu_id = menu_id;
        this.menu_image = menu_image;
        this.price = price;
        this.restaurant_id = restaurant_id;
        this.ingredients = ingredients;
        this.category_name = category_name;
        this.images = images;
        this.offer_title = offer_title;
        this.offer_price = offer_price;
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

    public String getMenu_image() {
        return menu_image;
    }

    public void setMenu_image(String menu_image) {
        this.menu_image = menu_image;
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

    public String getOffer_title() {
        return offer_title;
    }

    public void setOffer_title(String offer_title) {
        this.offer_title = offer_title;
    }

    public String getOffer_price() {
        return offer_price;
    }

    public void setOffer_price(String offer_price) {
        this.offer_price = offer_price;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public int getOfferPercentage() {
        return offerPercentage;
    }

    public void setOfferPercentage(int offerPercentage) {
        this.offerPercentage = offerPercentage;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "{" +
                "isExpanded=" + isExpanded +
                ", isSelected=" + isSelected +
                ", offerPercentage=" + offerPercentage +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", menu_id='" + menu_id + '\'' +
                ", menu_image='" + menu_image + '\'' +
                ", price='" + price + '\'' +
                ", restaurant_id='" + restaurant_id + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", category_name='" + category_name + '\'' +
                ", images=" + images +
                ", offer_title='" + offer_title + '\'' +
                ", offer_price='" + offer_price + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FoodItem)) return false;

        FoodItem foodItem = (FoodItem) o;

        if (!getId().equals(foodItem.getId())) return false;
        if (!getName().equals(foodItem.getName())) return false;
        if (!getMenu_id().equals(foodItem.getMenu_id())) return false;
        if (!getMenu_image().equals(foodItem.getMenu_image())) return false;
        if (!getPrice().equals(foodItem.getPrice())) return false;
        if (!getRestaurant_id().equals(foodItem.getRestaurant_id())) return false;
        if (!getIngredients().equals(foodItem.getIngredients())) return false;
        if (!getCategory_name().equals(foodItem.getCategory_name())) return false;
        return getImages().equals(foodItem.getImages());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getMenu_id().hashCode();
        result = 31 * result + getMenu_image().hashCode();
        result = 31 * result + getPrice().hashCode();
        result = 31 * result + getRestaurant_id().hashCode();
        result = 31 * result + getIngredients().hashCode();
        result = 31 * result + getCategory_name().hashCode();
        result = 31 * result + getImages().hashCode();
        return result;
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
        dest.writeString(menu_image);
        dest.writeString(price);
        dest.writeString(restaurant_id);
        dest.writeString(ingredients);
        dest.writeString(category_name);
        dest.writeList(images);
        dest.writeString(offer_price);
        dest.writeString(offer_title);
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
        menu_image = in.readString();
        price = in.readString();
        restaurant_id = in.readString();
        ingredients = in.readString();
        category_name = in.readString();
        images = in.readArrayList(FoodImage.class.getClassLoader());
        offer_price = in.readString();
        offer_title = in.readString();
    }
}

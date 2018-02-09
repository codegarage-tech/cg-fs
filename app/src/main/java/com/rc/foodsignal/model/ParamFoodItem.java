package com.rc.foodsignal.model;

import java.util.Arrays;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class ParamFoodItem extends ResponseBase {

    private String id = "";
    private String name = "";
    private String menu_id = "";
    private String price = "";
    private String restaurant_id = "";
    private String ingredients = "";
    private String images[] = new String[]{};

    public ParamFoodItem(String id, String name, String menu_id, String price, String restaurant_id, String ingredients, String[] images) {
        this.id = id;
        this.name = name;
        this.menu_id = menu_id;
        this.price = price;
        this.restaurant_id = restaurant_id;
        this.ingredients = ingredients;
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

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
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
                ", images=" + Arrays.toString(images) +
                '}';
    }
}

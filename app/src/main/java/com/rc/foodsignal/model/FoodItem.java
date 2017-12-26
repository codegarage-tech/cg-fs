package com.rc.foodsignal.model;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class FoodItem extends ResponseBase {

    private String id = "";
    private String name = "";
    private String menu_id = "";
    private String price = "";
    private String image = "";
    private String restaurant_id = "";
    private String menu_name = "";

    public FoodItem(String id, String name, String menu_id, String price, String image, String restaurant_id, String menu_name) {
        this.id = id;
        this.name = name;
        this.menu_id = menu_id;
        this.price = price;
        this.image = image;
        this.restaurant_id = restaurant_id;
        this.menu_name = menu_name;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", menu_id='" + menu_id + '\'' +
                ", price='" + price + '\'' +
                ", image='" + image + '\'' +
                ", restaurant_id='" + restaurant_id + '\'' +
                ", menu_name='" + menu_name + '\'' +
                '}';
    }
}

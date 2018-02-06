package com.rc.foodsignal.model;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class FoodImage extends ResponseBase {

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
}
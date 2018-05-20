package com.rc.foodsignal.model;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class OrderListItem extends ResponseBase {

    private String id = "";
    private String restaurant_id = "";
    private String total_amount = "";
    private String user_name = "";
    private String user_address = "";
    private String user_email = "";
    private String delivery_type = "";
    private String shipping_cost = "";
    private String sub_total = "";
    private String user_phone = "";
    private ArrayList<FoodCategoryDetail> menu_details = new ArrayList<>();

    public OrderListItem(String id, String restaurant_id, String total_amount, String user_name, String user_address, String user_email, String delivery_type, String shipping_cost, String sub_total, ArrayList<FoodCategoryDetail> menu_details) {
        this.id = id;
        this.restaurant_id = restaurant_id;
        this.total_amount = total_amount;
        this.user_name = user_name;
        this.user_address = user_address;
        this.user_email = user_email;
        this.delivery_type = delivery_type;
        this.shipping_cost = shipping_cost;
        this.sub_total = sub_total;
        this.menu_details = menu_details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public String getShipping_cost() {
        return shipping_cost;
    }

    public void setShipping_cost(String shipping_cost) {
        this.shipping_cost = shipping_cost;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public ArrayList<FoodCategoryDetail> getMenu_details() {
        return menu_details;
    }

    public void setMenu_details(ArrayList<FoodCategoryDetail> menu_details) {
        this.menu_details = menu_details;
    }

    @Override
    public String toString() {
        return "OrderListItem{" +
                "id='" + id + '\'' +
                ", restaurant_id='" + restaurant_id + '\'' +
                ", total_amount='" + total_amount + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_address='" + user_address + '\'' +
                ", user_email='" + user_email + '\'' +
                ", delivery_type='" + delivery_type + '\'' +
                ", shipping_cost='" + shipping_cost + '\'' +
                ", sub_total='" + sub_total + '\'' +
                ", user_phone='" + user_phone + '\'' +
                ", menu_details=" + menu_details +
                '}';
    }
}

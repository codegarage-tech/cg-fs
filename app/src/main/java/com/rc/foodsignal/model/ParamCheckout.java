package com.rc.foodsignal.model;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ParamCheckout extends ResponseBase {

    private String restaurant_id = "";
    private float total_amount = 0.0f;
    private float sub_total = 0.0f;
    private String delivery_type = "";
    private float shipping_cost = 0.0f;
    private String user_name = "";
    private String user_address = "";
    private String user_phone = "";
    private String user_email = "";
    private String user_id = "";
    private String device_id = "";
    private String device_type = "android";
    private ArrayList<OrderItem> order_items = new ArrayList<>();

    public ParamCheckout(String restaurant_id, float total_amount, float sub_total, String delivery_type, float shipping_cost, String user_name, String user_address, String user_phone, String user_email, String user_id, String device_id, String device_type, ArrayList<OrderItem> order_items) {
        this.restaurant_id = restaurant_id;
        this.total_amount = total_amount;
        this.sub_total = sub_total;
        this.delivery_type = delivery_type;
        this.shipping_cost = shipping_cost;
        this.user_name = user_name;
        this.user_address = user_address;
        this.user_phone = user_phone;
        this.user_email = user_email;
        this.user_id = user_id;
        this.device_id = device_id;
        this.device_type = device_type;
        this.order_items = order_items;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public float getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(float total_amount) {
        this.total_amount = total_amount;
    }

    public float getSub_total() {
        return sub_total;
    }

    public void setSub_total(float sub_total) {
        this.sub_total = sub_total;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public float getShipping_cost() {
        return shipping_cost;
    }

    public void setShipping_cost(float shipping_cost) {
        this.shipping_cost = shipping_cost;
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

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public ArrayList<OrderItem> getOrder_items() {
        return order_items;
    }

    public void setOrder_items(ArrayList<OrderItem> order_items) {
        this.order_items = order_items;
    }

    @Override
    public String toString() {
        return "{" +
                "restaurant_id='" + restaurant_id + '\'' +
                ", total_amount=" + total_amount +
                ", sub_total=" + sub_total +
                ", delivery_type='" + delivery_type + '\'' +
                ", shipping_cost=" + shipping_cost +
                ", user_name='" + user_name + '\'' +
                ", user_address='" + user_address + '\'' +
                ", user_phone='" + user_phone + '\'' +
                ", user_email='" + user_email + '\'' +
                ", user_id='" + user_id + '\'' +
                ", device_id='" + device_id + '\'' +
                ", device_type='" + device_type + '\'' +
                ", order_items=" + order_items +
                '}';
    }
}
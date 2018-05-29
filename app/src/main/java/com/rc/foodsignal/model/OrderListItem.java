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
    private String user_id = "";
    private String is_order_accepted = "";
    private String device_id = "";
    private String device_type = "";
    private String refund_requested = "";
    private ArrayList<FoodCategoryDetail> menu_details = new ArrayList<>();

    public OrderListItem(String id, String restaurant_id, String total_amount, String user_name, String user_address, String user_email, String delivery_type, String shipping_cost, String sub_total, String user_phone, String user_id, String is_order_accepted, String device_id, String device_type, String refund_requested, ArrayList<FoodCategoryDetail> menu_details) {
        this.id = id;
        this.restaurant_id = restaurant_id;
        this.total_amount = total_amount;
        this.user_name = user_name;
        this.user_address = user_address;
        this.user_email = user_email;
        this.delivery_type = delivery_type;
        this.shipping_cost = shipping_cost;
        this.sub_total = sub_total;
        this.user_phone = user_phone;
        this.user_id = user_id;
        this.is_order_accepted = is_order_accepted;
        this.device_id = device_id;
        this.device_type = device_type;
        this.refund_requested = refund_requested;
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

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getIs_order_accepted() {
        return is_order_accepted;
    }

    public void setIs_order_accepted(String is_order_accepted) {
        this.is_order_accepted = is_order_accepted;
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

    public String getRefund_requested() {
        return refund_requested;
    }

    public void setRefund_requested(String refund_requested) {
        this.refund_requested = refund_requested;
    }

    public ArrayList<FoodCategoryDetail> getMenu_details() {
        return menu_details;
    }

    public void setMenu_details(ArrayList<FoodCategoryDetail> menu_details) {
        this.menu_details = menu_details;
    }

    public ArrayList<FoodItem> getAllFoodItems() {
        ArrayList<FoodItem> mItems = new ArrayList<>();
        if (getMenu_details() != null && getMenu_details().size() > 0) {
            for (int i = 0; i < getMenu_details().size(); i++) {
                FoodCategoryDetail foodCategoryDetail = getMenu_details().get(i);
                if (foodCategoryDetail.getMenu_details() != null && foodCategoryDetail.getMenu_details().size() > 0) {
                    for (int j = 0; j < foodCategoryDetail.getMenu_details().size(); j++) {
                        FoodItem foodItem = foodCategoryDetail.getMenu_details().get(j);
                        mItems.add(foodItem);
                    }
                }
            }
        }
        return mItems;
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
                ", user_id='" + user_id + '\'' +
                ", is_order_accepted='" + is_order_accepted + '\'' +
                ", device_id='" + device_id + '\'' +
                ", device_type='" + device_type + '\'' +
                ", refund_requested='" + refund_requested + '\'' +
                ", menu_details=" + menu_details +
                '}';
    }
}

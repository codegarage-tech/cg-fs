package com.rc.foodsignal.model;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class ParamSendOffer extends ResponseBase {

    private String restaurant_id = "";
    private ArrayList<Offer> menu_details = new ArrayList<>();

    public ParamSendOffer(String restaurant_id, ArrayList<Offer> menu_details) {
        this.restaurant_id = restaurant_id;
        this.menu_details = menu_details;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public ArrayList<Offer> getMenu_details() {
        return menu_details;
    }

    public void setMenu_details(ArrayList<Offer> menu_details) {
        this.menu_details = menu_details;
    }

    @Override
    public String toString() {
        return "{" +
                "restaurant_id='" + restaurant_id + '\'' +
                ", menu_details=" + menu_details +
                '}';
    }

    public static class Offer {

        private String id = "";
        private String percentage = "";

        public Offer(String id, String percentage) {
            this.id = id;
            this.percentage = percentage;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPercentage() {
            return percentage;
        }

        public void setPercentage(String percentage) {
            this.percentage = percentage;
        }

        @Override
        public String toString() {
            return "{" +
                    "id='" + id + '\'' +
                    ", percentage='" + percentage + '\'' +
                    '}';
        }
    }
}
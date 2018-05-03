package com.rc.foodsignal.util;

import android.util.Log;

import com.rc.foodsignal.model.ParamFoodItem;
import com.rc.foodsignal.model.ParamSendPush;
import com.reversecoder.library.util.AllSettingsManager;

import org.json.JSONObject;

/**
 * Author: Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AllUrls {

    private static String TAG = AllUrls.class.getSimpleName();
    private static final String BASE_URL = "http://ntstx.com/food_api/index.php/";

    public static String getRestaurantLoginUrl() {
        String url = BASE_URL + "login/index";
        Log.d(TAG, "getRestaurantLoginUrl: " + url);
        return url;
    }

    public static JSONObject getRestaurantLoginParameters(String email, String password) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("email", email)
                .addJSONParam("password", password)
                .getJSONParam();
        Log.d(TAG, "getRestaurantLoginParameters: " + params.toString());
        return params;
    }

    public static String getRestaurantSignUpUrl() {
        String url = BASE_URL + "signup/index";
        Log.d(TAG, "getRestaurantSignUpUrl: " + url);
        return url;
    }

    public static JSONObject getRestaurantSignUpParameters(String name, double lat, String address, String phone,
                                                           double lng, String email, String password, String restaurantCategoryId, String shippingCost, String image) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("id", 0)
                .addJSONParam("name", name)
                .addJSONParam("lat", lat)
                .addJSONParam("address", address)
                .addJSONParam("phone", phone)
                .addJSONParam("lng", lng)
                .addJSONParam("email", email)
                .addJSONParam("password", password)
                .addJSONParam("restaurant_category_id", restaurantCategoryId)
                .addJSONParam("is_restaurant", 1)
                .addJSONParam("shipping_cost", shippingCost)
                .addJSONParam("image", image)
                .getJSONParam();
        Log.d(TAG, "getRestaurantSignUpParameters: " + params.toString());
        return params;
    }

    public static String getRestaurantUpdateUrl() {
        String url = BASE_URL + "signup/index";
        Log.d(TAG, "getRestaurantUpdateUrl: " + url);
        return url;
    }

    public static JSONObject getRestaurantUpdateParameters(String id, String name, double lat, String address, String phone,
                                                           double lng, String email, String password, String restaurantCategoryId, String shippingCost, String image) {
        HttpRequestManager.HttpParameter httpParameter = HttpRequestManager.HttpParameter.getInstance();

        if (!AllSettingsManager.isNullOrEmpty(id)) {
            httpParameter.addJSONParam("id", id);
        }
        if (!AllSettingsManager.isNullOrEmpty(name)) {
            httpParameter.addJSONParam("name", name);
        }
        if (lat != -1) {
            httpParameter.addJSONParam("lat", lat);
        }
        if (!AllSettingsManager.isNullOrEmpty(address)) {
            httpParameter.addJSONParam("address", address);
        }
        if (!AllSettingsManager.isNullOrEmpty(phone)) {
            httpParameter.addJSONParam("phone", phone);
        }
        if (lng != -1) {
            httpParameter.addJSONParam("lng", lng);
        }
        if (!AllSettingsManager.isNullOrEmpty(email)) {
            httpParameter.addJSONParam("email", email);
        }
        if (!AllSettingsManager.isNullOrEmpty(password)) {
            httpParameter.addJSONParam("password", password);
        }
        if (!AllSettingsManager.isNullOrEmpty(restaurantCategoryId)) {
            httpParameter.addJSONParam("restaurant_category_id", restaurantCategoryId);
        }
        if (!AllSettingsManager.isNullOrEmpty(shippingCost)) {
            httpParameter.addJSONParam("shipping_cost", shippingCost);
        }
        if (!AllSettingsManager.isNullOrEmpty(image)) {
            httpParameter.addJSONParam("image", image);
        }

        JSONObject params = httpParameter.getJSONParam();
        Log.d(TAG, "getRestaurantUpdateParameters: " + params.toString());
        return params;
    }

    public static String getAllFoodCategoryUrl() {
        String url = BASE_URL + "food_menu/all";
        Log.d(TAG, "getAllFoodCategoryUrl: " + url);
        return url;
    }

    public static String getAllRestaurantCategoryUrl() {
        String url = BASE_URL + "category/all";
        Log.d(TAG, "getAllRestaurantCategoryUrl: " + url);
        return url;
    }

//    public static String getAddFoodItemUrl() {
//        String url = BASE_URL + "restaurants/addFoodItem";
//        Log.d(TAG, "getAddFoodItemUrl: " + url);
//        return url;
//    }
//
//    public static JSONObject getAddFoodItemParameters(String name, String menuId, String price, String restaurantId, String image) {
//        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
//                .addJSONParam("name", name)
//                .addJSONParam("menu_id", menuId)
//                .addJSONParam("price", price)
//                .addJSONParam("restaurant_id", restaurantId)
//                .addJSONParam("image", image)
//                .addJSONParam("id", 0)
//                .getJSONParam();
//        Log.d(TAG, "getAddFoodItemParameters: " + params.toString());
//        return params;
//    }
//
//    public static String getUpdateFoodItemUrl() {
//        String url = BASE_URL + "restaurants/addFoodItem";
//        Log.d(TAG, "getAddFoodItemUrl: " + url);
//        return url;
//    }
//
//    public static JSONObject getUpdateFoodItemParameters(String name, String menuId, String price, String restaurantId, String image, int id) {
//        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
//                .addJSONParam("name", name)
//                .addJSONParam("menu_id", menuId)
//                .addJSONParam("price", price)
//                .addJSONParam("restaurant_id", restaurantId)
//                .addJSONParam("image", image)
//                .addJSONParam("id", id)
//                .getJSONParam();
//        Log.d(TAG, "getAddFoodItemParameters: " + params.toString());
//        return params;
//    }
//
//    public static String getRegisterDeviceUrl() {
//        String url = BASE_URL + "signup/register_device";
//        Log.d(TAG, "getRegisterDeviceUrl: " + url);
//        return url;
//    }
//
//    public static JSONObject getRegisterDeviceParameters(String deviceId) {
//        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
//                .addJSONParam("deviceId", deviceId)
//                .getJSONParam();
//        Log.d(TAG, "getRegisterDeviceParameters: " + params.toString());
//        return params;
//    }

    public static String getSearchRestaurantUrl() {
        String url = BASE_URL + "restaurants/search";
        Log.d(TAG, "getSearchRestaurantUrl: " + url);
        return url;
    }

    public static JSONObject getSearchRestaurantParameters(double lat, double lng, String foodCategoryId, String restaurantCategoryId) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("lat", lat)
                .addJSONParam("lng", lng)
                .addJSONParam("category_id", foodCategoryId)
                .addJSONParam("restaurant_category_id", restaurantCategoryId)
                .getJSONParam();
        Log.d(TAG, "getSearchRestaurantParameters: " + params.toString());
        return params;
    }

    public static String getAddUserBasicInfoUrl() {
        String url = BASE_URL + "signup/add_location";
        Log.d(TAG, "getAddUserBasicInfoUrl: " + url);
        return url;
    }

    public static JSONObject getAddUserBasicInfoParameters(String street, String city, String state, String zip, String country, String latitude, String longitude, String name, String phone, String email) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("user_id", "0")
                .addJSONParam("street", street)
                .addJSONParam("city", city)
                .addJSONParam("state", state)
                .addJSONParam("zip", zip)
                .addJSONParam("country", country)
                .addJSONParam("lat", latitude)
                .addJSONParam("lng", longitude)
                .addJSONParam("name", name)
                .addJSONParam("phone", phone)
                .addJSONParam("email", email)
                .getJSONParam();
        Log.d(TAG, "getAddUserBasicInfoParameters: " + params.toString());
        return params;
    }

    public static String getAddUserLocationUrl() {
        String url = BASE_URL + "signup/add_location";
        Log.d(TAG, "getAddUserLocationUrl: " + url);
        return url;
    }

    public static JSONObject getAddUserLocationParameters(String userId, String street, String city, String state, String zip, String country, String latitude, String longitude) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("user_id", userId)
                .addJSONParam("street", street)
                .addJSONParam("city", city)
                .addJSONParam("state", state)
                .addJSONParam("zip", zip)
                .addJSONParam("country", country)
                .addJSONParam("lat", latitude)
                .addJSONParam("lng", longitude)
                .getJSONParam();
        Log.d(TAG, "getAddUserLocationParameters: " + params.toString());
        return params;
    }

//    public static String getAllRestaurantsUrl() {
//        String url = BASE_URL + "restaurants/list_via_lat_lng";
//        Log.d(TAG, "getAllRestaurantUrl: " + url);
//        return url;
//    }
//
//    public static JSONObject getAllRestaurantsParameters(double latitude, double longitude) {
//        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
//                .addJSONParam("lat", latitude)
//                .addJSONParam("lng", longitude)
//                .getJSONParam();
//        Log.d(TAG, "getAllRestaurantsParameters: " + params.toString());
//        return params;
//    }

    public static String getAllUserLocationsUrl(String userId) {
        String url = BASE_URL + "signup/all_user_location/" + userId;
        Log.d(TAG, "getAllUserLocationsUrl: " + url);
        return url;
    }

    public static String getRestaurantMenuUrl(String restaurantId) {
        String url = BASE_URL + "food_menu/getRestaurantMenu/" + restaurantId;
        Log.d(TAG, "getRestaurantMenuUrl: " + url);
        return url;
    }

    public static String getAddFoodItemUrl() {
        String url = BASE_URL + "restaurants/addFoodItem";
        Log.d(TAG, "getAddFoodItemUrl: " + url);
        return url;
    }

    public static JSONObject getAddFoodItemParameters(String name, String menuId, String price, String restaurantId, String ingredients, String images[]) {
        ParamFoodItem paramFoodItem = new ParamFoodItem("0", name, menuId, price, restaurantId, ingredients, images);
        JSONObject params;
        try {
            params = new JSONObject(ParamFoodItem.getResponseString(paramFoodItem));
        } catch (Exception exception) {
            exception.printStackTrace();
            params = new JSONObject();
        }
        Log.d(TAG, "getAddFoodItemParameters: " + params.toString());
        return params;
    }

    public static String getUpdateFoodItemUrl() {
        String url = BASE_URL + "restaurants/addFoodItem";
        Log.d(TAG, "getUpdateFoodItemUrl: " + url);
        return url;
    }

    public static JSONObject getUpdateFoodItemParameters(String name, String menuId, String price, String restaurantId, String foodId, String ingredients, String images[]) {
        ParamFoodItem paramFoodItem = new ParamFoodItem(foodId, name, menuId, price, restaurantId, ingredients, images);
        JSONObject params;
        try {
            params = new JSONObject(ParamFoodItem.getResponseString(paramFoodItem));
        } catch (Exception exception) {
            exception.printStackTrace();
            params = new JSONObject();
        }
        Log.d(TAG, "getUpdateFoodItemParameters: " + params.toString());
        return params;
    }

    public static String getRegisterDeviceUrl() {
        String url = BASE_URL + "signup/register_device";
        Log.d(TAG, "getRegisterDeviceUrl: " + url);
        return url;
    }

    public static JSONObject getRegisterDeviceParameters(String uniqueId, String pushId, String userId) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("deviceId", pushId)
                .addJSONParam("device_type", "android")
                .addJSONParam("device_unique_id", uniqueId)
                .addJSONParam("user_id", userId)
                .getJSONParam();
        Log.d(TAG, "getRegisterDeviceParameters: " + params.toString());
        return params;
    }

    public static String getRemoveDeviceUrl() {
        String url = BASE_URL + "signup/remove_device";
        Log.d(TAG, "getUnregisterDeviceUrl: " + url);
        return url;
    }

    public static JSONObject getRemoveDeviceParameters(String uniqueId) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("device_unique_id", uniqueId)
                .getJSONParam();
        Log.d(TAG, "getUnregisterDeviceParameters: " + params.toString());
        return params;
    }

    public static String getSendPushUrl() {
        String url = BASE_URL + "notification/send_push";
        Log.d(TAG, "getSendPushUrl: " + url);
        return url;
    }

    public static JSONObject getSendPushParam(ParamSendPush paramSendPush) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(ParamSendPush.getResponseString(paramSendPush));
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonObject = new JSONObject();
        }
        Log.d(TAG, "getSendPushParam: " + jsonObject.toString());
        return jsonObject;
    }

    public static String getAllNotificationsUrl(String appUserId) {
        String url = BASE_URL + "notification/get_all_users_notification/"+appUserId;
        Log.d(TAG, "getAllNotificationsUrl: " + url);
        return url;
    }
}
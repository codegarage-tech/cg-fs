package com.rc.foodsignal.util;

import android.util.Log;

import org.json.JSONObject;

/**
 * Author: Abir Ahmed
 * Email: abir.eol@gmail.com
 */
public class AllUrls {

    private static String TAG = AllUrls.class.getSimpleName();
    private static final String BASE_URL = "http://ntstx.com/food_api/index.php/";

    public static String getLoginUrl() {
        String url = BASE_URL + "login/index";
        Log.d(TAG, "getLoginUrl: " + url);
        return url;
    }

    public static JSONObject getLoginParameters(String email, String password) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("email", email)
                .addJSONParam("password", password)
                .getJSONParam();
        Log.d(TAG, "getLoginParameters: " + params.toString());
        return params;
    }

    public static String getSignUpUrl() {
        String url = BASE_URL + "signup/index";
        Log.d(TAG, "getSignUpUrl: " + url);
        return url;
    }

    public static JSONObject getSignUpParameters(String name, int lat, int address, int phone,
                                                 int lng, String email, String password, String image) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("id", 0)
                .addJSONParam("name", name)
                .addJSONParam("lat", lat)
                .addJSONParam("address", address)
                .addJSONParam("phone", phone)
                .addJSONParam("lng", lng)
                .addJSONParam("email", email)
                .addJSONParam("password", password)
                .addJSONParam("image", image)
                .getJSONParam();
        Log.d(TAG, "getSignUpParameters: " + params.toString());
        return params;
    }

    public static String getUpdateUserUrl() {
        String url = BASE_URL + "signup/index";
        Log.d(TAG, "getUpdateUserUrl: " + url);
        return url;
    }

    public static JSONObject getUpdateParameters(int id, String name, int lat, int address, int phone,
                                                 int lng, String email, String password, String image) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("id", id)
                .addJSONParam("name", name)
                .addJSONParam("lat", lat)
                .addJSONParam("address", address)
                .addJSONParam("phone", phone)
                .addJSONParam("lng", lng)
                .addJSONParam("email", email)
                .addJSONParam("password", password)
                .addJSONParam("image", image)
                .getJSONParam();
        Log.d(TAG, "getSignUpParameters: " + params.toString());
        return params;
    }

    public static String getAllFoodMenuUrl() {
        String url = BASE_URL + "food_menu/all";
        Log.d(TAG, "getAllFoodMenuUrl: " + url);
        return url;
    }

    public static String getAddFoodItemUrl() {
        String url = BASE_URL + "restaurants/addFoodItem";
        Log.d(TAG, "getAddFoodItemUrl: " + url);
        return url;
    }

    public static JSONObject getAddFoodItemParameters(String name, String menuId, String price, String restaurantId, String image) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("name", name)
                .addJSONParam("menu_id", menuId)
                .addJSONParam("price", price)
                .addJSONParam("restaurant_id", restaurantId)
                .addJSONParam("image", image)
                .addJSONParam("id", 0)
                .getJSONParam();
        Log.d(TAG, "getAddFoodItemParameters: " + params.toString());
        return params;
    }

    public static String getUpdateFoodItemUrl() {
        String url = BASE_URL + "restaurants/addFoodItem";
        Log.d(TAG, "getAddFoodItemUrl: " + url);
        return url;
    }

    public static JSONObject getUpdateFoodItemParameters(String name, String menuId, String price, String restaurantId, String image, int id) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("name", name)
                .addJSONParam("menu_id", menuId)
                .addJSONParam("price", price)
                .addJSONParam("restaurant_id", restaurantId)
                .addJSONParam("image", image)
                .addJSONParam("id", id)
                .getJSONParam();
        Log.d(TAG, "getAddFoodItemParameters: " + params.toString());
        return params;
    }

    public static String getRegisterDeviceUrl() {
        String url = BASE_URL + "signup/register_device";
        Log.d(TAG, "getRegisterDeviceUrl: " + url);
        return url;
    }

    public static JSONObject getRegisterDeviceParameters(String deviceId) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("deviceId", deviceId)
                .getJSONParam();
        Log.d(TAG, "getRegisterDeviceParameters: " + params.toString());
        return params;
    }

    public static String getSearchFoodUrl() {
        String url = BASE_URL + "restaurants/search";
        Log.d(TAG, "getSearchRestaurantsUrl: " + url);
        return url;
    }

    public static JSONObject getSearchFoodParameters(double lat, double lng, String categoryId) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("lat", lat)
                .addJSONParam("lng", lng)
                .addJSONParam("category_id", categoryId)
                .getJSONParam();
        Log.d(TAG, "getSearchRestaurantsParameters: " + params.toString());
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
}

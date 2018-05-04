package com.reversecoder.gcm.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONObject;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class GcmConfig {

    public static final String GCM_SENDER_ID = "482144882562";
    private static final String TAG = GcmConfig.class.getSimpleName();
    public static final String SESSION_GCM_REGISTER_APP_USER_DATA = "SESSION_GCM_REGISTER_APP_USER_DATA";
    public static final String INTENT_KEY_GCM_APP_USER_DATA_CONTENT = "INTENT_KEY_GCM_APP_USER_DATA_CONTENT";
    public static final String INTENT_KEY_APP_USER_INTENT_DETAIL_TYPE = "INTENT_KEY_APP_USER_INTENT_DETAIL_TYPE";
    public static final String SESSION_IS_APP_USER_GCM_NOTIFICATION = "SESSION_IS_APP_USER_GCM_NOTIFICATION";

    public static final String SESSION_GCM_REGISTER_RESTAURANT_OWNER_DATA = "SESSION_GCM_REGISTER_RESTAURANT_OWNER_DATA";
    public static final String SESSION_IS_RESTAURANT_OWNER_GCM_NOTIFICATION = "SESSION_IS_RESTAURANT_OWNER_GCM_NOTIFICATION";

    private static final String BASE_URL = "http://ntstx.com/food_api/index.php/";

    public static String getRegisterRestaurantOwnerDeviceUrl() {
        String url = BASE_URL + "orders/add_restaurant_device";
        Log.d(TAG, "getRegisterRestaurantOwnerDeviceUrl: " + url);
        return url;
    }

    public static JSONObject getRegisterRestaurantOwnerDeviceParameters(String uniqueId, String pushId, String restaurantId) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("deviceId", pushId)
                .addJSONParam("device_type", "android")
                .addJSONParam("device_unique_id", uniqueId)
                .addJSONParam("restaurant_id", restaurantId)
                .getJSONParam();
        Log.d(TAG, "getRegisterRestaurantOwnerDeviceParameters: " + params.toString());
        return params;
    }

    public static String getRegisterAppUserDeviceUrl() {
        String url = BASE_URL + "signup/register_device";
        Log.d(TAG, "getRegisterAppUserDeviceUrl: " + url);
        return url;
    }

    public static JSONObject getRegisterAppUserDeviceParameters(String uniqueId, String pushId, String userId) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("deviceId", pushId)
                .addJSONParam("device_type", "android")
                .addJSONParam("device_unique_id", uniqueId)
                .addJSONParam("user_id", userId)
                .getJSONParam();
        Log.d(TAG, "getRegisterAppUserDeviceParameters: " + params.toString());
        return params;
    }

    public static String getUnregisterAppUserDeviceUrl() {
        String url = BASE_URL + "signup/remove_device";
        Log.d(TAG, "getUnregisterAppUserDeviceUrl: " + url);
        return url;
    }

    public static JSONObject getUnregisterAppUserDeviceParameters(String uniqueId) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("device_unique_id", uniqueId)
                .getJSONParam();
        Log.d(TAG, "getUnregisterAppUserDeviceParameters: " + params.toString());
        return params;
    }

    public static boolean isNullOrEmpty(String myString) {
        return myString == null ? true : myString.length() == 0 || myString.equalsIgnoreCase("null") || myString.equalsIgnoreCase("");
    }

    public static void setStringSetting(Context context, String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStringSetting(Context context, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key, "");
    }

    public static String getStringSetting(Context context, String key, String defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key, defaultValue);
    }

    public static void setBooleanSetting(Context context, String key, boolean value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBooleanSetting(Context context, String key, boolean defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(key, defaultValue);
    }

    public static void removeSetting(Context context, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }
}
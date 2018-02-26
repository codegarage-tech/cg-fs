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
    public static final String SESSION_GCM_REGISTER_DATA = "SESSION_GCM_REGISTER_DATA";
    public static final String INTENT_KEY_GCM_DATA_CONTENT_INTENT = "INTENT_KEY_GCM_DATA_CONTENT_INTENT";

    private static final String BASE_URL = "http://ntstx.com/food_api/index.php/";

    public static String getRegisterDeviceUrl() {
        String url = BASE_URL + "signup/register_device";
        Log.d(TAG, "getRegisterDeviceUrl: " + url);
        return url;
    }

    public static JSONObject getRegisterDeviceParameters(String uniqueId, String pushId) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("deviceId", pushId)
                .addJSONParam("device_type", "android")
                .addJSONParam("device_unique_id", uniqueId)
                .getJSONParam();
        Log.d(TAG, "getRegisterDeviceParameters: " + params.toString());
        return params;
    }

    public static String getRemoveDeviceUrl() {
        String url = BASE_URL + "signup/remove_device";
        Log.d(TAG, "getRemoveDeviceUrl: " + url);
        return url;
    }

    public static JSONObject getRemoveDeviceParameters(String uniqueId) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("device_unique_id", uniqueId)
                .getJSONParam();
        Log.d(TAG, "getRemoveDeviceParameters: " + params.toString());
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
}
package com.rc.foodsignal.util;

/**
 * Author: Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AllConstants {

    public static final String PREFIX_BASE64_STRING = "data:image/jpeg;base64,";

    //Database
    public static final String REALM_NAME = "foodsignal.realm";
    public static final int REALM_SCHEMA_VERSION = 0;

    //Stripe info
    public static final String STRIPE_PUBLISHABLE_KEY = "pk_test_X8ylYKVSbfebLx4KI1NpzWVU";
    public static final String STRIPE_SECRET_KEY = "sk_test_rCGmdJQPppQZjFIlN80qfdM2";

    //Session key
    public static final String SESSION_SELECTED_NAVIGATION_MENU = "SESSION_SELECTED_NAVIGATION_MENU";
    public static final String SESSION_IS_LOCATION_ADDED = "SESSION_IS_LOCATION_ADDED";
    public static final String SESSION_SELECTED_LOCATION = "SESSION_SELECTED_LOCATION";
    public static final String SESSION_USER_BASIC_INFO = "SESSION_USER_BASIC_INFO";
    public static final String SESSION_IS_RESTAURANT_LOGGED_IN = "SESSION_IS_RESTAURANT_LOGGED_IN";
    public static final String SESSION_RESTAURANT_LOGIN_DATA = "SESSION_RESTAURANT_LOGIN_DATA";
    public static final String SESSION_FOOD_CATEGORY = "SESSION_FOOD_CATEGORY";
    public static final String SESSION_RESTAURANT_CATEGORY = "SESSION_RESTAURANT_CATEGORY";
    public static final String SESSION_SELECTED_CARD = "SESSION_SELECTED_CARD";

    //Intent request code
    public static final int INTENT_REQUEST_CODE_ADD_RESTAURANT_LOGIN = 420;
    public static final int INTENT_REQUEST_CODE_ADDRESS_LIST = 4200;
    public static final int INTENT_REQUEST_CODE_IMAGE_PICKER = 42000;
    public static final int INTENT_REQUEST_CODE_ADDRESS_SEARCH = 42001;
    public static final int INTENT_REQUEST_CODE_RESTAURANT_ADD_MENU = 42002;
    public static final int INTENT_REQUEST_CODE_RESTAURANT_MENU_DETAIL = 42003;
    public static final int INTENT_REQUEST_CODE_LOCATION_LIST = 42004;
    public static final int INTENT_REQUEST_CODE_ADD_CARD = 42005;
    public static final int INTENT_REQUEST_CODE_CARD_CHECKOUT = 42006;

    //Intent key
    public static final String INTENT_KEY_LOGIN = "INTENT_KEY_LOGIN";
    public static final String INTENT_KEY_LOCATION_LIST = "INTENT_KEY_LOCATION_LIST";
    public static final String INTENT_KEY_ADDRESS_LIST = "INTENT_KEY_ADDRESS_LIST";
    public static final String INTENT_KEY_SEARCH_ADDRESS = "INTENT_KEY_SEARCH_ADDRESS";
    public static final String INTENT_KEY_FOOD_ITEM = "INTENT_KEY_FOOD_ITEM";
    public static final String INTENT_KEY_RESTAURANT_ITEM = "INTENT_KEY_RESTAURANT_ITEM";
    public static final String INTENT_KEY_RESTAURANT_ITEM_POSITION = "INTENT_KEY_RESTAURANT_ITEM_POSITION";
    public static final String INTENT_KEY_CARD_ITEM = "INTENT_KEY_CARD_ITEM";
    public static final String INTENT_KEY_CHECKOUT_DATA = "INTENT_KEY_CHECKOUT_DATA";
    public static final String INTENT_KEY_REVIEWED_CHECKOUT_DATA = "INTENT_KEY_REVIEWED_CHECKOUT_DATA";

    public static final int NAVIGATION_DRAWER_CLOSE_DELAY = 200;

    public static final String DEFAULT_FOOD_CATEGORY = "{\"data\":[{\"id\":\"1\",\"name\":\"Burger\"},{\"id\":\"2\",\"name\":\"Soup\"},{\"id\":\"3\",\"name\":\"Pizza\"},{\"id\":\"4\",\"name\":\"Chinese Food\"},{\"id\":\"5\",\"name\":\"Mexican Food\"},{\"id\":\"6\",\"name\":\"Thai Food\"},{\"id\":\"7\",\"name\":\"Indian Food\"},{\"id\":\"8\",\"name\":\"Grill\"},{\"id\":\"9\",\"name\":\"Kabab\"},{\"id\":\"10\",\"name\":\"Sea Food\"}]}";

    public static final String DEFAULT_RESTAURANT_CATEGORY = "{\"data\":[{\"id\":\"1\",\"name\":\"Indian Restaurant\"},{\"id\":\"2\",\"name\":\"Italian Restaurant\"},{\"id\":\"3\",\"name\":\"Mexican Restaurant\"}]}";
}

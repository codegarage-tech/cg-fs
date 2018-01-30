package com.rc.foodsignal.util;

/**
 * Author: Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AllConstants {

    public static final String PREFIX_BASE64_STRING = "data:image/jpeg;base64,";

    //Session key
    public static final String SESSION_SELECTED_NAVIGATION_MENU = "SESSION_SELECTED_NAVIGATION_MENU";
    public static final String SESSION_IS_LOCATION_ADDED = "SESSION_IS_LOCATION_ADDED";
    public static final String SESSION_SELECTED_LOCATION = "SESSION_SELECTED_LOCATION";
    public static final String SESSION_USER_BASIC_INFO = "SESSION_USER_BASIC_INFO";
    public static final String SESSION_IS_RESTAURANT_LOGGED_IN = "SESSION_IS_RESTAURANT_LOGGED_IN";
    public static final String SESSION_RESTAURANT_LOGIN_DATA = "SESSION_RESTAURANT_LOGIN_DATA";
    public static final String SESSION_FOOD_CATEGORY = "SESSION_FOOD_CATEGORY";

    //Intent request code
    public static final int INTENT_REQUEST_CODE_ADD_RESTAURANT_LOGIN = 420;
    public static final int INTENT_REQUEST_CODE_ADDRESS_LIST = 4200;
    public static final int INTENT_REQUEST_CODE_IMAGE_PICKER = 42000;
    public static final int INTENT_REQUEST_CODE_ADDRESS_SEARCH = 42001;

    //Intent key
    public static final String INTENT_KEY_LOGIN = "INTENT_KEY_LOGIN";
    public static final String INTENT_KEY_ADDRESS_LIST = "INTENT_KEY_ADDRESS_LIST";
    public static final String INTENT_KEY_SEARCH_ADDRESS = "INTENT_KEY_SEARCH_ADDRESS";

    public static final int NAVIGATION_DRAWER_CLOSE_DELAY = 200;

    public static final String DEFAULT_FOOD_CATEGORY = "{\"data\":[{\"id\":\"1\",\"name\":\"Burger\",\"ingredients\":\"Tomatoes, Onions, Dried Parsley, Salt, Maltodextrin, Sugar, Modified Corn Starch, Lime Juice Concentrate\"},{\"id\":\"2\",\"name\":\"Soup\",\"ingredients\":\"carrots, garlic powder,  mixed chopped vegetables, oregano, onion\"},{\"id\":\"3\",\"name\":\"Pizza\",\"ingredients\":\"Feta cheese,Italian sausage, Pepperoni,Onions, Parmesan cheese, Bell peppers, Mushrooms\"},{\"id\":\"4\",\"name\":\"Chinese Food\",\"ingredients\":\"Noodles, Pasta, Soup\"},{\"id\":\"5\",\"name\":\"Mexican Food\",\"ingredients\":\"\"},{\"id\":\"6\",\"name\":\"Thai Food\",\"ingredients\":\"Kosher salt, Vegetable oil,  barbecue sauce, Salt\"},{\"id\":\"7\",\"name\":\"Indian Food\",\"ingredients\":\"Fish fry, Biriyani, Teheri, Khichuri\"},{\"id\":\"8\",\"name\":\"Grill\",\"ingredients\":\"Kosher salt, Vegetable oil,  barbecue sauce, Salt\"},{\"id\":\"9\",\"name\":\"Kabab\",\"ingredients\":\"Kosher salt, Vegetable oil,  barbecue sauce, Salt\"},{\"id\":\"10\",\"name\":\"Sea Food\",\"ingredients\":\"Tuna Fish, Shrimp, Shark, Prawns, Pomfret\"}]}";

}

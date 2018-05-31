package com.rc.foodsignal.application;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatDelegate;

import com.rc.foodsignal.activity.RestaurantOrderListActivity;
import com.rc.foodsignal.activity.RestaurantDetailActivity;
import com.rc.foodsignal.activity.UserOrderListActivity;
import com.reversecoder.gcm.GcmManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.rc.foodsignal.util.AllConstants.REALM_NAME;
import static com.rc.foodsignal.util.AllConstants.REALM_SCHEMA_VERSION;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class FoodSignalApplication extends Application {

    private static Context mContext;
    private static final String CANARO_EXTRA_BOLD_PATH = "fonts/canaro_extra_bold.otf";
    public static Typeface canaroExtraBold;

    @Override
    public void onCreate() {
        super.onCreate();

        //For using vector drawable
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        mContext = this;
        initTypeface();

        //Initialize GCM content class
        GcmManager.initGcmManager(mContext, new GcmManager.GcmBuilder()
                .setContentRestaurantDetailClass(RestaurantDetailActivity.class)
                .setContentUserOrderListClass(UserOrderListActivity.class)
                .setContentRestaurantOrderListClass(RestaurantOrderListActivity.class)
                .buildGcmManager());

        //Realm Database
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(REALM_NAME)
                .schemaVersion(REALM_SCHEMA_VERSION)
//                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    private void initTypeface() {
        canaroExtraBold = Typeface.createFromAsset(getAssets(), CANARO_EXTRA_BOLD_PATH);
    }

    public static Context getGlobalContext() {
        return mContext;
    }
}

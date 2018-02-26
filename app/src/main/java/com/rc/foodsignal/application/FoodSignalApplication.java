package com.rc.foodsignal.application;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatDelegate;

import com.rc.foodsignal.activity.HomeActivity;
import com.reversecoder.gcm.GcmManager;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
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
        GcmManager.initGcmManager(mContext, new GcmManager.GcmBuilder().setContentClass(HomeActivity.class).buildGcmManager());
    }

    private void initTypeface() {
        canaroExtraBold = Typeface.createFromAsset(getAssets(), CANARO_EXTRA_BOLD_PATH);
    }

    public static Context getGlobalContext() {
        return mContext;
    }
}

package com.reversecoder.gcm;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import static com.reversecoder.gcm.util.GcmConfig.GCM_SENDER_ID;

/**
 * @author Md. Rashadul Alam
 */
public class GcmManager {

    private static GcmBuilder mGcmBuilder;
    private Context mContext;

    public static GcmManager initGcmManager(Context context, GcmBuilder gcmBuilder) {
        return new GcmManager(context, gcmBuilder);
    }

    private GcmManager(Context context, GcmBuilder gcmBuilder) {
        mContext = context;
        mGcmBuilder = gcmBuilder;
    }

    public static Class<?> getContentRestaurantDetailClass() {
        return mGcmBuilder.getContentRestaurantDetailClass();
    }

    public static Class<?> getContentOrderListClass() {
        return mGcmBuilder.getContentOrderListClass();
    }

    public static class GcmBuilder {
        private Class<?> classRestaurantDetail;
        private Class<?> classOrderList;

        public GcmBuilder() {
        }

        public GcmBuilder setContentRestaurantDetailClass(Class<?> restaurantDetailClass) {
            this.classRestaurantDetail = restaurantDetailClass;
            return this;
        }

        public Class<?> getContentRestaurantDetailClass() {
            return classRestaurantDetail;
        }

        public Class<?> getContentOrderListClass() {
            return classOrderList;
        }

        public GcmBuilder setContentOrderListClass(Class<?> classOrderList) {
            this.classOrderList = classOrderList;
            return this;
        }

        public GcmBuilder buildGcmManager() {
            return this;
        }
    }

    public static String getDevicePushId(Context context) {
        //Get GCM registration id
        String mPushId = "";
        try {
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
            mPushId = gcm.register(GCM_SENDER_ID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.d(GcmManager.class.getSimpleName(), "mPushId: " + mPushId);
        return mPushId;
    }
}
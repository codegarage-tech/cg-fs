package com.reversecoder.gcm;

import android.content.Context;

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
}
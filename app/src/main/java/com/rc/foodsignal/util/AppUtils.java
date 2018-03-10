package com.rc.foodsignal.util;

import android.app.ActivityManager;
import android.content.Context;
import android.telephony.TelephonyManager;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class AppUtils {

    public static int toPx(Context context, int value) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (value * density);
    }

    public static boolean isNullOrEmpty(String myString) {
        return myString == null ? true : myString.length() == 0 || myString.equalsIgnoreCase("null") || myString.equalsIgnoreCase("");
    }

    public static String getTagName(Class<?> cls) {
        return cls.getSimpleName();
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSimSupport(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return !(tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT);

    }
}
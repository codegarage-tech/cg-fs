package com.reversecoder.gcm.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.reversecoder.gcm.listener.GcmResultListener;
import com.reversecoder.gcm.model.RegisterRestaurantOwner;
import com.reversecoder.gcm.model.ResponseRegisterRestaurantOwner;
import com.reversecoder.gcm.util.GcmConfig;
import com.reversecoder.gcm.util.HttpRequestManager;
import com.reversecoder.gcm.util.UniqueIdManager;

import static com.reversecoder.gcm.util.GcmConfig.GCM_SENDER_ID;
import static com.reversecoder.gcm.util.GcmConfig.SESSION_GCM_REGISTER_RESTAURANT_OWNER_DATA;
import static com.reversecoder.gcm.util.GcmConfig.SESSION_IS_RESTAURANT_OWNER_GCM_NOTIFICATION;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class RegisterRestaurantOwnerTask extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

    private static final String TAG = RegisterRestaurantOwnerTask.class.getSimpleName();
    private Context mContext;
    private String mRestaurantId = "";
    private GcmResultListener mGcmResultListener;

    public RegisterRestaurantOwnerTask(Context context, String restaurantId, GcmResultListener gcmResultListener) {
        this.mContext = context;
        this.mRestaurantId = restaurantId;
        this.mGcmResultListener = gcmResultListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected HttpRequestManager.HttpResponse doInBackground(String... params) {

        HttpRequestManager.HttpResponse response = null;
        try {

            //Get GCM registration id
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(mContext);
            String mPushId = gcm.register(GCM_SENDER_ID);
            Log.d(TAG, "mPushId: " + mPushId);

            //Get GCM unique id for each device
            String mUniqueId = UniqueIdManager.getAndroidId(mContext);
            Log.d(TAG, "mUniqueId: " + mUniqueId);

            //Check if same data
            if (!GcmConfig.isNullOrEmpty(GcmConfig.getStringSetting(mContext, SESSION_GCM_REGISTER_RESTAURANT_OWNER_DATA))) {
                //Session data
                RegisterRestaurantOwner registerRestaurantOwner = RegisterRestaurantOwner.convertFromStringToObject(GcmConfig.getStringSetting(mContext, SESSION_GCM_REGISTER_RESTAURANT_OWNER_DATA), RegisterRestaurantOwner.class);
                if (registerRestaurantOwner != null) {
                    Log.d(TAG, "Task: Session data: " + registerRestaurantOwner.toString());
                    //Current data
                    RegisterRestaurantOwner mRegisterRestaurantOwner = new RegisterRestaurantOwner(registerRestaurantOwner.getId(), mPushId, registerRestaurantOwner.getDevice_type(), mUniqueId, registerRestaurantOwner.getRestaurant_id());
                    Log.d(TAG, "Task: New data: " + registerRestaurantOwner.toString());
                    if (registerRestaurantOwner.equals(mRegisterRestaurantOwner)) {
                        Log.d(TAG, "Task: Stopping registering device due to same push id.");
                        return response;
                    }
                }
            }

            //Send response to the server
            response = HttpRequestManager.doRestPostRequest(GcmConfig.getRegisterRestaurantOwnerDeviceUrl(), GcmConfig.getRegisterRestaurantOwnerDeviceParameters(mUniqueId, mPushId, mRestaurantId), null);

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(HttpRequestManager.HttpResponse result) {
        if (result != null) {

            if (result.isSuccess() && !GcmConfig.isNullOrEmpty(result.getResult().toString())) {
                Log.d(TAG, "Task: success response(web): " + result.getResult().toString());
                ResponseRegisterRestaurantOwner responseRegisterRestaurantOwner = ResponseRegisterRestaurantOwner.convertFromStringToObject(result.getResult().toString(), ResponseRegisterRestaurantOwner.class);
                Log.d(TAG, "Task: success response: " + responseRegisterRestaurantOwner.toString());

                if (responseRegisterRestaurantOwner != null) {
                    boolean isRegistered = false;
                    if (responseRegisterRestaurantOwner.getStatus().equalsIgnoreCase("success") && responseRegisterRestaurantOwner.getData().size() > 0) {
                        isRegistered = true;
                        GcmConfig.setStringSetting(mContext, SESSION_GCM_REGISTER_RESTAURANT_OWNER_DATA, RegisterRestaurantOwner.convertFromObjectToString(responseRegisterRestaurantOwner.getData().get(0)));
                        Log.d(TAG, "Task: Session data: " + GcmConfig.getStringSetting(mContext, SESSION_GCM_REGISTER_RESTAURANT_OWNER_DATA));
                        GcmConfig.setBooleanSetting(mContext, SESSION_IS_RESTAURANT_OWNER_GCM_NOTIFICATION, true);
                    }

                    //Send response to the parent activity
                    mGcmResultListener.onGcmResult(isRegistered);
                }else{
                    mGcmResultListener.onGcmResult(false);
                }
            }else{
                mGcmResultListener.onGcmResult(false);
            }
        } else {
            mGcmResultListener.onGcmResult(false);
            Log.d(TAG, "Task: success response: " + "No response found for gcm");
        }
    }
}
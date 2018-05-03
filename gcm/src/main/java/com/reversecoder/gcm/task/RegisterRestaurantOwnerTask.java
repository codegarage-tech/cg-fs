package com.reversecoder.gcm.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.reversecoder.gcm.listener.GcmResultListener;
import com.reversecoder.gcm.model.RegisterAppUser;
import com.reversecoder.gcm.model.ResponseRegisterAppUser;
import com.reversecoder.gcm.util.GcmConfig;
import com.reversecoder.gcm.util.HttpRequestManager;
import com.reversecoder.gcm.util.UniqueIdManager;

import static com.reversecoder.gcm.util.GcmConfig.GCM_SENDER_ID;
import static com.reversecoder.gcm.util.GcmConfig.SESSION_GCM_REGISTER_APP_USER_DATA;
import static com.reversecoder.gcm.util.GcmConfig.SESSION_IS_APP_USER_GCM_NOTIFICATION;

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
            if (!GcmConfig.isNullOrEmpty(GcmConfig.getStringSetting(mContext, SESSION_GCM_REGISTER_APP_USER_DATA))) {
                //Session data
                RegisterAppUser registerAppUser = RegisterAppUser.convertFromStringToObject(GcmConfig.getStringSetting(mContext, SESSION_GCM_REGISTER_APP_USER_DATA), RegisterAppUser.class);
                if (registerAppUser != null) {
                    Log.d(TAG, "Session data: " + registerAppUser.toString());
                    //Current data
                    RegisterAppUser mRegisterAppUser = new RegisterAppUser(registerAppUser.getId(), mPushId, registerAppUser.getName(), registerAppUser.getPhone(), registerAppUser.getEmail(), mUniqueId, registerAppUser.getType());
                    Log.d(TAG, "New data: " + registerAppUser.toString());
                    if (registerAppUser.equals(mRegisterAppUser)) {
                        Log.d(TAG, "Stopping registering device due to same push id.");
                        return response;
                    }
                }
            }

            //Send response to the server
            response = HttpRequestManager.doRestPostRequest(GcmConfig.getRegisterAppUserDeviceUrl(), GcmConfig.getRegisterAppUserDeviceParameters(mUniqueId, mPushId, mRestaurantId), null);

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(HttpRequestManager.HttpResponse result) {
        if (result != null) {

            if (result.isSuccess() && !GcmConfig.isNullOrEmpty(result.getResult().toString())) {
                Log.d(TAG, "success response(web): " + result.getResult().toString());
                ResponseRegisterAppUser responseRegisterAppUser = ResponseRegisterAppUser.convertFromStringToObject(result.getResult().toString(), ResponseRegisterAppUser.class);
                Log.d(TAG, "success response: " + responseRegisterAppUser.toString());

                if (responseRegisterAppUser != null) {
                    boolean isRegistered = false;
                    if (responseRegisterAppUser.getStatus().equalsIgnoreCase("success") && responseRegisterAppUser.getData().size() > 0) {
                        isRegistered = true;
                        GcmConfig.setStringSetting(mContext, SESSION_GCM_REGISTER_APP_USER_DATA, RegisterAppUser.convertFromObjectToString(responseRegisterAppUser.getData().get(0)));
                        Log.d(TAG, "Session data: " + GcmConfig.getStringSetting(mContext, SESSION_GCM_REGISTER_APP_USER_DATA));
                        GcmConfig.setBooleanSetting(mContext, SESSION_IS_APP_USER_GCM_NOTIFICATION, true);
                    }

                    //Send response to the parent activity
                    mGcmResultListener.onGcmResult(isRegistered);
                }
            }
        } else {
            Log.d(TAG, "success response: " + "No response found for gcm");
        }
    }
}
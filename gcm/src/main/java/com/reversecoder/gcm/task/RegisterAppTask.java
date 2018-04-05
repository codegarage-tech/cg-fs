package com.reversecoder.gcm.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.reversecoder.gcm.listener.GcmResultListener;
import com.reversecoder.gcm.model.RegisterApp;
import com.reversecoder.gcm.model.ResponseRegisterApp;
import com.reversecoder.gcm.util.GcmConfig;
import com.reversecoder.gcm.util.HttpRequestManager;
import com.reversecoder.gcm.util.UniqueIdManager;

import static com.reversecoder.gcm.util.GcmConfig.GCM_SENDER_ID;
import static com.reversecoder.gcm.util.GcmConfig.SESSION_GCM_REGISTER_DATA;
import static com.reversecoder.gcm.util.GcmConfig.SESSION_IS_GCM_NOTIFICATION;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class RegisterAppTask extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

    private static final String TAG = RegisterAppTask.class.getSimpleName();
    private Context mContext;
    private String mUserId = "";
    private GcmResultListener mGcmResultListener;

    public RegisterAppTask(Context context, String userId, GcmResultListener gcmResultListener) {
        this.mContext = context;
        this.mUserId = userId;
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
            if (!GcmConfig.isNullOrEmpty(GcmConfig.getStringSetting(mContext, SESSION_GCM_REGISTER_DATA))) {
                //Session data
                RegisterApp registerApp = RegisterApp.convertFromStringToObject(GcmConfig.getStringSetting(mContext, SESSION_GCM_REGISTER_DATA), RegisterApp.class);
                if (registerApp != null) {
                    Log.d(TAG, "Session data: " + registerApp.toString());
                    //Current data
                    RegisterApp mRegisterApp = new RegisterApp(registerApp.getId(), mPushId, registerApp.getName(), registerApp.getPhone(), registerApp.getEmail(), mUniqueId, registerApp.getType());
                    Log.d(TAG, "New data: " + registerApp.toString());
                    if (registerApp.equals(mRegisterApp)) {
                        Log.d(TAG, "Stopping registering device due to same push id.");
                        return response;
                    }
                }
            }

            //Send response to the server
            response = HttpRequestManager.doRestPostRequest(GcmConfig.getRegisterDeviceUrl(), GcmConfig.getRegisterDeviceParameters(mUniqueId, mPushId, mUserId), null);

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
                ResponseRegisterApp responseRegisterApp = ResponseRegisterApp.convertFromStringToObject(result.getResult().toString(), ResponseRegisterApp.class);
                Log.d(TAG, "success response: " + responseRegisterApp.toString());

                if (responseRegisterApp != null) {
                    boolean isRegistered = false;
                    if (responseRegisterApp.getStatus().equalsIgnoreCase("success") && responseRegisterApp.getData().size() > 0) {
                        isRegistered = true;
                        GcmConfig.setStringSetting(mContext, SESSION_GCM_REGISTER_DATA, RegisterApp.convertFromObjectToString(responseRegisterApp.getData().get(0)));
                        Log.d(TAG, "Session data: " + GcmConfig.getStringSetting(mContext, SESSION_GCM_REGISTER_DATA));
                        GcmConfig.setBooleanSetting(mContext, SESSION_IS_GCM_NOTIFICATION, true);
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
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

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class RegisterAppTask extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

    private static final String TAG = RegisterAppTask.class.getSimpleName();
    private Context mContext;
    private GcmResultListener mGcmResultListener;

    public RegisterAppTask(Context context, GcmResultListener gcmResultListener) {
        this.mContext = context;
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
//            Log.d(TAG, "mPushId: " + mPushId);

            //Get GCM unique id for each device
            String mUniqueId = UniqueIdManager.getAndroidId(mContext);
//            Log.d(TAG, "mUniqueId: " + mUniqueId);

            //Send response to the server
            response = HttpRequestManager.doRestPostRequest(GcmConfig.getRegisterDeviceUrl(), GcmConfig.getRegisterDeviceParameters(mUniqueId, mPushId), null);

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(HttpRequestManager.HttpResponse result) {
        if (result != null) {

            if (result.isSuccess() && !GcmConfig.isNullOrEmpty(result.getResult().toString())) {
//                Log.d(TAG, "success response(web): " + result.getResult().toString());
                ResponseRegisterApp responseRegisterApp = ResponseRegisterApp.convertFromStringToObject(result.getResult().toString(), ResponseRegisterApp.class);

                if (responseRegisterApp != null) {

                    boolean isRegistered = false;

                    if (responseRegisterApp.getStatus().equalsIgnoreCase("success") && responseRegisterApp.getData().size() > 0) {

//                    Log.d(TAG, "success response: " + responseRegisterApp.toString());
                        isRegistered = true;
                        GcmConfig.setBooleanSetting(mContext, GcmConfig.SESSION_IS_NOTIFICATION, true);
                        GcmConfig.setStringSetting(mContext, GcmConfig.SESSION_GCM_REGISTER_DATA, RegisterApp.convertFromObjectToString(responseRegisterApp.getData().get(0)));
//                    Log.d(TAG, "Session data: " + GcmConfig.getStringSetting(mContext, GcmConfig.SESSION_GCM_REGISTER_DATA));
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
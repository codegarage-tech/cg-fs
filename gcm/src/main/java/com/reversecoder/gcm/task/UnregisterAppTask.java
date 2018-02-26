package com.reversecoder.gcm.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.reversecoder.gcm.listener.GcmResultListener;
import com.reversecoder.gcm.model.RegisterApp;
import com.reversecoder.gcm.model.ResponseUnregisterApp;
import com.reversecoder.gcm.util.GcmConfig;
import com.reversecoder.gcm.util.HttpRequestManager;
import com.reversecoder.gcm.util.UniqueIdManager;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class UnregisterAppTask extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

    private static final String TAG = UnregisterAppTask.class.getSimpleName();
    private Context mContext;
    private GcmResultListener mGcmResultListener;

    public UnregisterAppTask(Context context, GcmResultListener gcmResultListener) {
        this.mContext = context;
        this.mGcmResultListener = gcmResultListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected HttpRequestManager.HttpResponse doInBackground(String... params) {

        String mUniqueId = "";

        if (!GcmConfig.isNullOrEmpty(GcmConfig.getStringSetting(mContext, GcmConfig.SESSION_GCM_REGISTER_DATA))) {
            RegisterApp registerApp = RegisterApp.convertFromStringToObject(GcmConfig.getStringSetting(mContext, GcmConfig.SESSION_GCM_REGISTER_DATA), RegisterApp.class);
            mUniqueId = registerApp.getUnique_id();
        } else {
            //Get GCM unique id for each device
            mUniqueId = UniqueIdManager.getAndroidId(mContext);
//            Log.d(TAG, "mUniqueId: " + mUniqueId);
        }

        return HttpRequestManager.doRestPostRequest(GcmConfig.getUnregisterDeviceUrl(), GcmConfig.getUnregisterDeviceParameters(mUniqueId), null);
    }

    @Override
    protected void onPostExecute(HttpRequestManager.HttpResponse result) {
        if (result != null) {

            if (result.isSuccess() && !GcmConfig.isNullOrEmpty(result.getResult().toString())) {
//                Log.d(TAG, "success response(web): " + result.getResult().toString());
                ResponseUnregisterApp responseUnregisterApp = ResponseUnregisterApp.convertFromStringToObject(result.getResult().toString(), ResponseUnregisterApp.class);

                if (responseUnregisterApp != null) {

                    boolean isUnregistered = false;

                    if (responseUnregisterApp.getStatus().equalsIgnoreCase("success")) {

//                    Log.d(TAG, "success response: " + responseRegisterApp.toString());
                        isUnregistered = true;
                        GcmConfig.removeSetting(mContext, GcmConfig.SESSION_GCM_REGISTER_DATA);
                        GcmConfig.setBooleanSetting(mContext, GcmConfig.SESSION_IS_NOTIFICATION, false);
//                    Log.d(TAG, "Session data: " + GcmConfig.getStringSetting(mContext, GcmConfig.SESSION_GCM_REGISTER_DATA));
                    }

                    //Send response to the parent activity
                    mGcmResultListener.onGcmResult(isUnregistered);
                }
            }
        } else {
            Log.d(TAG, "success response: " + "No response found for gcm");
        }
    }
}
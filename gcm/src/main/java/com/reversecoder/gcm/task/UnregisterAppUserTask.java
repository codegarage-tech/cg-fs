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

import static com.reversecoder.gcm.util.GcmConfig.SESSION_IS_APP_USER_GCM_NOTIFICATION;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class UnregisterAppUserTask extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

    private static final String TAG = UnregisterAppUserTask.class.getSimpleName();
    private Context mContext;
    private GcmResultListener mGcmResultListener;

    public UnregisterAppUserTask(Context context, GcmResultListener gcmResultListener) {
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

        if (!GcmConfig.isNullOrEmpty(GcmConfig.getStringSetting(mContext, GcmConfig.SESSION_GCM_REGISTER_APP_USER_DATA))) {
            RegisterApp registerApp = RegisterApp.convertFromStringToObject(GcmConfig.getStringSetting(mContext, GcmConfig.SESSION_GCM_REGISTER_APP_USER_DATA), RegisterApp.class);
            mUniqueId = registerApp.getUnique_id();
        } else {
            //Get GCM unique id for each device
            mUniqueId = UniqueIdManager.getAndroidId(mContext);
//            Log.d(TAG, "mUniqueId: " + mUniqueId);
        }

        return HttpRequestManager.doRestPostRequest(GcmConfig.getUnregisterAppUserDeviceUrl(), GcmConfig.getUnregisterAppUserDeviceParameters(mUniqueId), null);
    }

    @Override
    protected void onPostExecute(HttpRequestManager.HttpResponse result) {
        if (result != null) {

            if (result.isSuccess() && !GcmConfig.isNullOrEmpty(result.getResult().toString())) {
                Log.d(TAG, "success response(web): " + result.getResult().toString());
                ResponseUnregisterApp responseUnregisterApp = ResponseUnregisterApp.convertFromStringToObject(result.getResult().toString(), ResponseUnregisterApp.class);

                if (responseUnregisterApp != null) {
                    boolean isUnregistered = false;
                    Log.d(TAG, "success response: " + responseUnregisterApp.toString());

                    if (responseUnregisterApp.getStatus().equalsIgnoreCase("success")) {
                        isUnregistered = true;
                        GcmConfig.removeSetting(mContext, GcmConfig.SESSION_GCM_REGISTER_APP_USER_DATA);
                        GcmConfig.setBooleanSetting(mContext, SESSION_IS_APP_USER_GCM_NOTIFICATION, false);
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
package com.reversecoder.gcm.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.reversecoder.gcm.listener.RegisterAppListener;
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
    private RegisterAppListener mRegisterAppListener;

    public RegisterAppTask(Context context, RegisterAppListener registerAppListener) {
        this.mContext = context;
        this.mRegisterAppListener = registerAppListener;
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

//                ResponseRegisterApp responseRegisterApp = ResponseRegisterApp.convertFromStringToObject(result.getResult().toString(), ResponseRegisterApp.class);
//
//                if (responseRegisterApp.getStatus().equalsIgnoreCase("1") && responseRegisterApp.getData() != null) {
//
//                    Log.d(TAG, "success response: " + responseRegisterApp.toString());
//                    GcmConfig.setStringSetting(mContext, GcmConfig.SESSION_GCM_REGISTER_DATA, RegisterApp.convertFromObjectToString(responseRegisterApp.getData()));
//                    //Send response to the parent activity
//                    mRegisterAppListener.registerApp(result);
//                }
            }
        } else {
            Log.d(TAG, "success response: " + "No response found for gcm");
        }
    }
}
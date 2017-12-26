//package com.rc.foodsignal.activity.trash;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.rc.foodsignal.R;
//import com.rc.foodsignal.model.ResponseRegisterDevice;
//import com.rc.foodsignal.util.AllUrls;
//import com.rc.foodsignal.util.AppUtils;
//import com.rc.foodsignal.util.HttpRequestManager;
//
///**
// * Author: Abir Ahmed
// * Email: abir.eol@gmail.com
// */
//
//public class RegisterDeviceActivity extends AppCompatActivity {
//
//    TextView tvRegisterDevice;
//    GetRegisterDevice doGetRegisterDevice;
//    String TAG = AppUtils.getTagName(RegisterDeviceActivity.class);
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_register_device);
//        initRegisterDeviceUI();
//        initRegisterDeviceAction();
//    }
//
//    private void initRegisterDeviceUI(){
//        tvRegisterDevice = (TextView) findViewById(R.id.tv_registerDevice);
//
//    }
//
//    private void initRegisterDeviceAction(){
//        String mDeviceId = "23445gsdget3";
//
//        doGetRegisterDevice = new GetRegisterDevice(RegisterDeviceActivity.this, mDeviceId);
//        doGetRegisterDevice.execute();
//    }
//
//    private class GetRegisterDevice extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {
//
//        private Context mContext;
//        private String mDeviceId = "";
//
//        public GetRegisterDevice(Context context, String deviceId) {
//            this.mContext = context;
//            this.mDeviceId = deviceId;
//        }
//
//        @Override
//        protected void onPreExecute() {
//        }
//
//        @Override
//        protected HttpRequestManager.HttpResponse doInBackground(String... params) {
//            HttpRequestManager.HttpResponse response = HttpRequestManager.doRestPostRequest(AllUrls.getRegisterDeviceUrl(), AllUrls.getRegisterDeviceParameters(mDeviceId), null);
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(HttpRequestManager.HttpResponse result) {
//
//            if (result.isSuccess() && !AppUtils.isNullOrEmpty(result.getResult().toString())) {
//                Log.d(TAG, "success response from web: " + result.getResult().toString());
//
//                ResponseRegisterDevice responseData = ResponseRegisterDevice.getResponseObject(result.getResult().toString(),ResponseRegisterDevice.class);
//
//                if ((responseData.getStatus().equalsIgnoreCase("success")) && (responseData.getData().size() > 0)) {
//                    Log.d(TAG, "success response from object: " + responseData.toString());
//
//                    tvRegisterDevice.setText(responseData.toString());
//                } else {
//                    Toast.makeText(RegisterDeviceActivity.this, getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(RegisterDeviceActivity.this, getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//}

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
//import com.rc.foodsignal.util.AllUrls;
//import com.rc.foodsignal.util.AppUtils;
//import com.rc.foodsignal.util.HttpRequestManager;
//
///**
// * Author: Abir Ahmed
// * Email: abir.eol@gmail.com
// */
//
//public class SignUpActivity extends AppCompatActivity {
//
//    TextView tvSignup;
//    DoSignUp doSignUpUser;
//    String TAG = AppUtils.getTagName(SignUpActivity.class);
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_signup);
//        initRegistrationUI();
//        initRegistrationAction();
//    }
//
//    private void initRegistrationUI() {
//        tvSignup = (TextView) findViewById(R.id.tv_signup);
//    }
//
//    private void initRegistrationAction() {
//
//        int mLat = 2343, mLng = 2343, mAddress = 3, mPhone = 2343;
//
//        String mName = "niloy",
//                mEmail = "niloy.cste1@gmail.com",
//                mPassword = "1234567",
//                mImage = "";
//
//        doSignUpUser = new DoSignUp(SignUpActivity.this, mName, mLat, mAddress, mPhone, mLng, mEmail, mPassword, mImage);
//        doSignUpUser.execute();
//    }
//
//    public class DoSignUp extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {
//
//        private Context mContext;
//        private int mLat, mLng, mAddress, mPhone;
//        private String mName = "", mEmail = "", mPassword = "", mImage = "";
//
//        public DoSignUp(Context context, String name, int lat, int address, int phone, int lng, String email, String password, String image) {
//            this.mContext = context;
//            this.mName = name;
//            this.mLat = lat;
//            this.mAddress = address;
//            this.mPhone = phone;
//            this.mLng = lng;
//            this.mEmail = email;
//            this.mPassword = password;
//            this.mImage = image;
//        }
//
//        @Override
//        protected void onPreExecute() {
//        }
//
//        @Override
//        protected HttpRequestManager.HttpResponse doInBackground(String... params) {
//            HttpRequestManager.HttpResponse response = HttpRequestManager.doRestPostRequest(AllUrls.getSignUpUrl(), AllUrls.getSignUpParameters(mName, mLat, mAddress, mPhone, mLng, mEmail, mPassword, mImage), null);
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(HttpRequestManager.HttpResponse result) {
//
//            if (result.isSuccess() && !AppUtils.isNullOrEmpty(result.getResult().toString())) {
//                Log.d(TAG, "success response: " + result.getResult().toString());
//
//                tvSignup.setText(result.getResult().toString());
//
//            } else {
//                Toast.makeText(SignUpActivity.this, getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//}

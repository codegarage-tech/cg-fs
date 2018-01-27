package com.rc.foodsignal.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rc.foodsignal.R;
import com.rc.foodsignal.model.ResponseRestaurantLoginData;
import com.rc.foodsignal.util.AllUrls;
import com.rc.foodsignal.util.AppUtils;
import com.rc.foodsignal.util.HttpRequestManager;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import static com.rc.foodsignal.util.AllConstants.INTENT_KEY_LOGIN;
import static com.rc.foodsignal.util.AllConstants.SESSION_IS_LOCATION_ADDED;
import static com.rc.foodsignal.util.AllConstants.SESSION_IS_RESTAURANT_LOGGED_IN;
import static com.rc.foodsignal.util.AllConstants.SESSION_SELECTED_LOCATION;
import static com.rc.foodsignal.util.AllConstants.SESSION_RESTAURANT_LOGIN_DATA;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class RestaurantLoginActivity extends AppCompatActivity {

    Button btnSignIn, btnRegistration;
    EditText edtEmail, edtPassword;
    //Toolbar
    TextView tvTitle;
    ImageView ivBack;
    ProgressDialog loadingDialog;

    DoLogin doLoginUser;
    String TAG = AppUtils.getTagName(RestaurantLoginActivity.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_login);

        initLoginUI();
        initLoginAction();
    }

    private void initLoginUI() {
        //Toolbar
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.setText(getString(R.string.title_activity_restaurant_login));
        //Activity view
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        btnRegistration = (Button) findViewById(R.id.btn_registration);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPassword = (EditText) findViewById(R.id.edt_password);

//        edtEmail.setText("niloy.cste1@gmail.com");
//        edtPassword.setText("1234567");

        edtEmail.setText("rashadul.alam@gmail.com");
        edtPassword.setText("123456");
    }

    private void initLoginAction() {

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = edtEmail.getText().toString(), mPassword = edtPassword.getText().toString();

                if (mEmail.equalsIgnoreCase("")) {
                    Toast.makeText(RestaurantLoginActivity.this, getResources().getString(R.string.toast_empty_email_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mPassword.equalsIgnoreCase("")) {
                    Toast.makeText(RestaurantLoginActivity.this, getResources().getString(R.string.toast_empty_password_field), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!NetworkManager.isConnected(RestaurantLoginActivity.this)) {
                    Toast.makeText(RestaurantLoginActivity.this, getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }

                doLoginUser = new DoLogin(RestaurantLoginActivity.this, mEmail, mPassword);
                doLoginUser.execute();
            }
        });

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantLoginActivity.this, RestaurantSignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private class DoLogin extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;
        private String mEmail = "", mPassword = "";

        public DoLogin(Context context, String email, String password) {
            mContext = context;
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            loadingDialog = new ProgressDialog(mContext);
            loadingDialog.setMessage(getResources().getString(R.string.txt_loading));
            loadingDialog.setIndeterminate(false);
            loadingDialog.setCancelable(true);
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.show();
            loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    if (loadingDialog != null
                            && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                }
            });
        }

        @Override
        protected HttpRequestManager.HttpResponse doInBackground(String... params) {
            HttpRequestManager.HttpResponse response = HttpRequestManager.doRestPostRequest(AllUrls.getRestaurantLoginUrl(), AllUrls.getRestaurantLoginParameters(mEmail, mPassword), null);
            return response;
        }

        @Override
        protected void onPostExecute(HttpRequestManager.HttpResponse result) {

            if (loadingDialog != null
                    && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }

            if (result.isSuccess() && !AppUtils.isNullOrEmpty(result.getResult().toString())) {
                Log.d(TAG, "success response from web: " + result.getResult().toString());
                ResponseRestaurantLoginData responseData = ResponseRestaurantLoginData.getResponseObject(result.getResult().toString(), ResponseRestaurantLoginData.class);

                if (responseData.getStatus().equalsIgnoreCase("1") && (responseData.getData().size() > 0)) {
                    Log.d(TAG, "success wrapper: " + responseData.getData().get(0).toString());
                    SessionManager.setStringSetting(RestaurantLoginActivity.this, SESSION_RESTAURANT_LOGIN_DATA, responseData.getData().get(0).toString());
                    SessionManager.setBooleanSetting(RestaurantLoginActivity.this, SESSION_IS_RESTAURANT_LOGGED_IN, true);

//                    //Save location added status
//                    if (responseData.getData().get(0).getIs_address_added() == 1) {
//                        SessionManager.setBooleanSetting(RestaurantLoginActivity.this, SESSION_IS_LOCATION_ADDED, true);
//                        if (AllSettingsManager.isNullOrEmpty(SessionManager.getStringSetting(RestaurantLoginActivity.this, SESSION_SELECTED_LOCATION))) {
//                            if (responseData.getData().get(0).getSelected_address().size() > 0) {
//                                SessionManager.setStringSetting(RestaurantLoginActivity.this, SESSION_SELECTED_LOCATION, responseData.getData().get(0).getSelected_address().get(0).toString());
//                            }
//                        }
//                    }

                    //Send login status to the navigation drawer activity
                    Intent intent = new Intent();
                    intent.putExtra(INTENT_KEY_LOGIN, true);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(RestaurantLoginActivity.this, getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(RestaurantLoginActivity.this, getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

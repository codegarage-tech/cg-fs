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
import com.rc.foodsignal.model.ResponseUserData;
import com.rc.foodsignal.util.AllUrls;
import com.rc.foodsignal.util.AppUtils;
import com.rc.foodsignal.util.HttpRequestManager;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import static com.rc.foodsignal.util.AllConstants.SESSION_IS_LOCATION_ADDED;
import static com.rc.foodsignal.util.AllConstants.SESSION_IS_USER_LOGGED_IN;
import static com.rc.foodsignal.util.AllConstants.SESSION_SELECTED_LOCATION;
import static com.rc.foodsignal.util.AllConstants.SESSION_USER_DATA;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class LoginActivity extends AppCompatActivity {

    Button btnSignIn, btnRegistration;
    EditText edtEmail, edtPassword;
    TextView tvTitle;
    ImageView ivBack;
    ProgressDialog loadingDialog;

    DoLogin doLoginUser;
    String TAG = AppUtils.getTagName(LoginActivity.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initLoginUI();
        initLoginAction();
    }

    private void initLoginUI() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.setText(getString(R.string.title_activity_login));
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        btnRegistration = (Button) findViewById(R.id.btn_registration);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPassword = (EditText) findViewById(R.id.edt_password);

        edtEmail.setText("niloy.cste1@gmail.com");
        edtPassword.setText("1234567");
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
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.toast_empty_email_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mPassword.equalsIgnoreCase("")) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.toast_empty_password_field), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!NetworkManager.isConnected(LoginActivity.this)) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }

                doLoginUser = new DoLogin(LoginActivity.this, mEmail, mPassword);
                doLoginUser.execute();
            }
        });

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
//                startActivity(intent);
//                finish();
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
            HttpRequestManager.HttpResponse response = HttpRequestManager.doRestPostRequest(AllUrls.getLoginUrl(), AllUrls.getLoginParameters(mEmail, mPassword), null);
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
                ResponseUserData responseData = ResponseUserData.getResponseObject(result.getResult().toString(), ResponseUserData.class);

                if (responseData.getStatus().equalsIgnoreCase("1") && (responseData.getData().size() > 0)) {
                    Log.d(TAG, "success wrapper: " + responseData.getData().get(0).toString());
                    SessionManager.setStringSetting(LoginActivity.this, SESSION_USER_DATA, responseData.getData().get(0).toString());
                    SessionManager.setBooleanSetting(LoginActivity.this, SESSION_IS_USER_LOGGED_IN, true);

                    //Save location added status
                    Intent intent;
                    if (responseData.getData().get(0).getIs_address_added() == 1) {
                        SessionManager.setBooleanSetting(LoginActivity.this, SESSION_IS_LOCATION_ADDED, true);
                        if (AllSettingsManager.isNullOrEmpty(SessionManager.getStringSetting(LoginActivity.this, SESSION_SELECTED_LOCATION))) {
                            if (responseData.getData().get(0).getSelected_address().size() > 0) {
                                SessionManager.setStringSetting(LoginActivity.this, SESSION_SELECTED_LOCATION, responseData.getData().get(0).getSelected_address().get(0).toString());
                            }
                        }
                        intent = new Intent(LoginActivity.this, HomeActivity.class);
                    } else {
                        intent = new Intent(LoginActivity.this, AddLocationLocationActivity.class);
                    }
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

package com.rc.foodsignal.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rc.foodsignal.R;
import com.rc.foodsignal.util.AllUrls;
import com.rc.foodsignal.util.AppUtils;
import com.rc.foodsignal.util.HttpRequestManager;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class RestaurantSignUpActivity extends AppCompatActivity {

    DoSignUp doSignUpUser;
    String TAG = AppUtils.getTagName(RestaurantSignUpActivity.class);
    //Toolbar
    Toolbar toolbar;
    TextView tvTitle;
    ImageView ivBack;

    ImageView ivUser;
    EditText edtName, edtEmail, edtAddress, edtPhone, edtPassword;
    ToggleButton toggleButtonDone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_restaurant_signup);
        initRegistrationUI();
        initRegistrationAction();
    }

    private void initRegistrationUI() {
        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.setText(getString(R.string.title_activity_restaurant_signup));

        ivUser = (ImageView) findViewById(R.id.iv_user);
        edtName = (EditText) findViewById(R.id.edt_name);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtAddress = (EditText) findViewById(R.id.edt_address);
        edtPhone = (EditText) findViewById(R.id.edt_phone);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        toggleButtonDone = (ToggleButton) findViewById(R.id.toggle_button_done);

        if (!toggleButtonDone.isChecked()) {
            setEditMode(true);
        }else{
            setEditMode(false);
        }

        Glide
                .with(RestaurantSignUpActivity.this)
                .load(R.drawable.ic_rashed)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .apply(new RequestOptions().circleCropTransform())
                .into(ivUser);
    }

    private void setEditMode(boolean isEditMode){
        if(isEditMode){
            ivUser.setEnabled(true);
            edtName.setEnabled(true);
            edtEmail.setEnabled(true);
            edtPhone.setEnabled(true);
            edtPassword.setEnabled(true);
        }else{
            ivUser.setEnabled(false);
            edtName.setEnabled(false);
            edtEmail.setEnabled(false);
            edtPhone.setEnabled(false);
            edtPassword.setEnabled(false);
        }

        edtAddress.setEnabled(false);
    }

    private void initRegistrationAction() {

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toggleButtonDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setEditMode(false);
                }else{
                    setEditMode(true);
                }
            }
        });

//        int mLat = 2343, mLng = 2343, mAddress = 3, mPhone = 2343;
//
//        String mName = "niloy",
//                mEmail = "niloy.cste123@gmail.com",
//                mPassword = "1234567",
//                mImage = "";
//
//        doSignUpUser = new DoSignUp(RestaurantSignUpActivity.this, mName, mLat, mAddress, mPhone, mLng, mEmail, mPassword, mImage);
//        doSignUpUser.execute();
    }

    public class DoSignUp extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;
        private int mLat, mLng, mAddress, mPhone;
        private String mName = "", mEmail = "", mPassword = "", mImage = "";

        public DoSignUp(Context context, String name, int lat, int address, int phone, int lng, String email, String password, String image) {
            this.mContext = context;
            this.mName = name;
            this.mLat = lat;
            this.mAddress = address;
            this.mPhone = phone;
            this.mLng = lng;
            this.mEmail = email;
            this.mPassword = password;
            this.mImage = image;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected HttpRequestManager.HttpResponse doInBackground(String... params) {
            HttpRequestManager.HttpResponse response = HttpRequestManager.doRestPostRequest(AllUrls.getSignUpUrl(), AllUrls.getSignUpParameters(mName, mLat, mAddress, mPhone, mLng, mEmail, mPassword, mImage), null);
            return response;
        }

        @Override
        protected void onPostExecute(HttpRequestManager.HttpResponse result) {

            if (result.isSuccess() && !AppUtils.isNullOrEmpty(result.getResult().toString())) {
                Log.d(TAG, "success response: " + result.getResult().toString());
            } else {
                Toast.makeText(RestaurantSignUpActivity.this, getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

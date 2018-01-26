package com.rc.foodsignal.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rc.foodsignal.R;
import com.rc.foodsignal.util.AllUrls;
import com.rc.foodsignal.util.AppUtils;
import com.rc.foodsignal.util.HttpRequestManager;
import com.reversecoder.library.network.NetworkManager;
import com.seatgeek.placesautocomplete.geocoding.LocationAddressListener;
import com.seatgeek.placesautocomplete.geocoding.ReverseGeocoderTask;
import com.seatgeek.placesautocomplete.geocoding.UserLocationAddress;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.List;

import static com.rc.foodsignal.util.AllConstants.INTENT_REQUEST_CODE_IMAGE_PICKER;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class RestaurantSignUpActivity extends BaseLocationActivity {

    DoSignUp doSignUpUser;
    String TAG = AppUtils.getTagName(RestaurantSignUpActivity.class);
    //Toolbar
    Toolbar toolbar;
    TextView tvTitle;
    ImageView ivBack;

    ImageView ivUser;
    EditText edtName, edtEmail, edtAddress, edtPhone, edtPassword;
    LinearLayout llDone;

    ReverseGeocoderTask currentLocationTask;
    UserLocationAddress userLocationAddress;

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
        llDone = (LinearLayout) findViewById(R.id.ll_done);

//        setEditMode(true);

        Glide
                .with(RestaurantSignUpActivity.this)
                .load(R.drawable.ic_rashed)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .apply(new RequestOptions().circleCropTransform())
                .into(ivUser);
    }

    private void setEditMode(boolean isEditMode) {
        if (isEditMode) {
            ivUser.setEnabled(true);
            edtName.setEnabled(true);
            edtEmail.setEnabled(true);
            edtPhone.setEnabled(true);
            edtPassword.setEnabled(true);
        } else {
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

        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse.from(RestaurantSignUpActivity.this)
                        .choose(MimeType.ofImage())
                        .theme(R.style.Matisse_Zhihu)
                        .capture(true)
                        .setDefaultCaptureStrategy()
                        .countable(false)
                        .maxSelectable(1)
                        .imageEngine(new GlideEngine())
                        .forResult(INTENT_REQUEST_CODE_IMAGE_PICKER);
            }
        });

        llDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mName = edtName.getText().toString(),
                        mEmail = edtEmail.getText().toString(),
                        mAddress = edtAddress.getText().toString(),
                        mPhone = edtPhone.getText().toString(),
                        mPassword = edtPassword.getText().toString();

                if (mName.equalsIgnoreCase("")) {
                    Toast.makeText(RestaurantSignUpActivity.this, getResources().getString(R.string.toast_empty_name_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEmail.equalsIgnoreCase("")) {
                    Toast.makeText(RestaurantSignUpActivity.this, getResources().getString(R.string.toast_empty_email_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mAddress.equalsIgnoreCase("")) {
                    Toast.makeText(RestaurantSignUpActivity.this, getResources().getString(R.string.toast_empty_address_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mPhone.equalsIgnoreCase("")) {
                    Toast.makeText(RestaurantSignUpActivity.this, getResources().getString(R.string.toast_empty_phone_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mPassword.equalsIgnoreCase("")) {
                    Toast.makeText(RestaurantSignUpActivity.this, getResources().getString(R.string.toast_empty_password_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!NetworkManager.isConnected(RestaurantSignUpActivity.this)) {
                    Toast.makeText(RestaurantSignUpActivity.this, getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }

//                doSignUpUser = new DoSignUp(RestaurantSignUpActivity.this, mName, userLocationAddress.getLatitude(), mAddress, mPhone, userLocationAddress.getLongitude(), mEmail, mPassword, mImage);
//                doSignUpUser.execute();
            }
        });
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

    /****************************
     * Location data processing *
     ****************************/
    @Override
    public void onLocationFound(Location location) {
        if (location != null) {
            setCurrentLocationDetail(location);
        }
    }

    private void setCurrentLocationDetail(Location location) {
        if ((currentLocationTask != null) && (currentLocationTask.getStatus() == AsyncTask.Status.RUNNING)) {
            currentLocationTask.cancel(true);
        }

        currentLocationTask = new ReverseGeocoderTask(RestaurantSignUpActivity.this, new LocationAddressListener() {
            @Override
            public void getLocationAddress(UserLocationAddress locationAddress) {

                Log.d(TAG, "UserLocationAddress: " + locationAddress.toString());
//                String addressText = String.format("%s, %s, %s, %s", locationAddress.getStreetAddress(), locationAddress.getCity(), locationAddress.getState(), locationAddress.getCountry());
                userLocationAddress = locationAddress;
                edtAddress.setText(locationAddress.getAddressLine());
            }
        });
        currentLocationTask.execute(location);
    }

    /****************************
     * Location data processing *
     ****************************/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_CODE_IMAGE_PICKER && resultCode == RESULT_OK) {
//            mAdapter.setData(Matisse.obtainResult(data), Matisse.obtainPathResult(data));
            List<String> mData = Matisse.obtainPathResult(data);
            Log.d("MatisseImage: ",mData.get(0));
            Glide
                    .with(RestaurantSignUpActivity.this)
                    .load((mData.size() == 1) ? mData.get(0) : "")
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                    .apply(new RequestOptions().circleCropTransform())
                    .into(ivUser);
        }
    }
}
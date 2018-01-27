package com.rc.foodsignal.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.developers.imagezipper.ImageZipper;
import com.rc.foodsignal.R;
import com.rc.foodsignal.model.Location;
import com.rc.foodsignal.model.ResponseRestaurantLoginData;
import com.rc.foodsignal.model.RestaurantLoginData;
import com.rc.foodsignal.util.AllUrls;
import com.rc.foodsignal.util.AppUtils;
import com.rc.foodsignal.util.HttpRequestManager;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.net.URL;
import java.util.List;

import static com.rc.foodsignal.util.AllConstants.INTENT_KEY_SEARCH_ADDRESS;
import static com.rc.foodsignal.util.AllConstants.INTENT_REQUEST_CODE_ADDRESS_SEARCH;
import static com.rc.foodsignal.util.AllConstants.INTENT_REQUEST_CODE_IMAGE_PICKER;
import static com.rc.foodsignal.util.AllConstants.PREFIX_BASE64_STRING;
import static com.rc.foodsignal.util.AllConstants.SESSION_IS_RESTAURANT_LOGGED_IN;
import static com.rc.foodsignal.util.AllConstants.SESSION_RESTAURANT_LOGIN_DATA;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class AboutRestaurantActivity extends AppCompatActivity {

    DoUpdateRestaurantInfo doUpdateRestaurantInfo;
    String TAG = AppUtils.getTagName(AboutRestaurantActivity.class);
    //Toolbar
    Toolbar toolbar;
    TextView tvTitle;
    ImageView ivBack;
    ProgressDialog loadingDialog;

    ImageView ivUser;
    EditText edtName, edtEmail, edtPhone, edtPassword;
    TextView tvAddress;
    LinearLayout llDone;
    String mBase64 = "";
    Location mLocation;

    RestaurantLoginData restaurantLoginData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_restaurant);

        initRegistrationUI();
        initRegistrationAction();
    }

    private void initRegistrationUI() {
        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.setText(getString(R.string.title_activity_about_restaurant));

        ivUser = (ImageView) findViewById(R.id.iv_user);
        edtName = (EditText) findViewById(R.id.edt_name);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        edtPhone = (EditText) findViewById(R.id.edt_phone);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        llDone = (LinearLayout) findViewById(R.id.ll_done);

        if (!AllSettingsManager.isNullOrEmpty(SessionManager.getStringSetting(AboutRestaurantActivity.this, SESSION_RESTAURANT_LOGIN_DATA))) {
            restaurantLoginData = RestaurantLoginData.getResponseObject(SessionManager.getStringSetting(AboutRestaurantActivity.this, SESSION_RESTAURANT_LOGIN_DATA), RestaurantLoginData.class);
            Log.d("LoginUser: ", restaurantLoginData.toString());

            if (restaurantLoginData != null) {
                setRestaurantInfo(restaurantLoginData);
            }
        }
    }

    private void setRestaurantInfo(RestaurantLoginData restaurantLoginInfo) {

        Glide
                .with(AboutRestaurantActivity.this)
                .load((!AllSettingsManager.isNullOrEmpty(restaurantLoginInfo.getImage())) ? restaurantLoginInfo.getImage() : R.drawable.ic_default_avatar)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .apply(new RequestOptions().circleCropTransform())
                .into(ivUser);
        edtName.setText((!AllSettingsManager.isNullOrEmpty(restaurantLoginInfo.getName())) ? restaurantLoginInfo.getName() : "");
        edtEmail.setText((!AllSettingsManager.isNullOrEmpty(restaurantLoginInfo.getEmail())) ? restaurantLoginInfo.getEmail() : "");
        tvAddress.setText((!AllSettingsManager.isNullOrEmpty(restaurantLoginInfo.getAddress())) ? restaurantLoginInfo.getAddress() : "");
        edtPhone.setText((!AllSettingsManager.isNullOrEmpty(restaurantLoginInfo.getPhone())) ? restaurantLoginInfo.getPhone() : "");
        edtPassword.setText((!AllSettingsManager.isNullOrEmpty(restaurantLoginInfo.getPassword())) ? restaurantLoginInfo.getPassword() : "");
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
                Matisse.from(AboutRestaurantActivity.this)
                        .choose(MimeType.ofImage())
                        .theme(R.style.Matisse_Dracula)
                        .capture(true)
                        .setDefaultCaptureStrategy()
                        .countable(false)
                        .maxSelectable(1)
                        .imageEngine(new GlideEngine())
                        .forResult(INTENT_REQUEST_CODE_IMAGE_PICKER);
            }
        });

        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddress = new Intent(AboutRestaurantActivity.this, SearchLocationActivity.class);
                startActivityForResult(intentAddress, INTENT_REQUEST_CODE_ADDRESS_SEARCH);
            }
        });

        llDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mName = edtName.getText().toString(),
                        mEmail = edtEmail.getText().toString(),
                        mAddress = tvAddress.getText().toString(),
                        mPhone = edtPhone.getText().toString(),
                        mPassword = edtPassword.getText().toString();

                if (mName.equalsIgnoreCase("")) {
                    Toast.makeText(AboutRestaurantActivity.this, getResources().getString(R.string.toast_empty_name_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEmail.equalsIgnoreCase("")) {
                    Toast.makeText(AboutRestaurantActivity.this, getResources().getString(R.string.toast_empty_email_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mAddress.equalsIgnoreCase("")) {
                    Toast.makeText(AboutRestaurantActivity.this, getResources().getString(R.string.toast_empty_address_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mPhone.equalsIgnoreCase("")) {
                    Toast.makeText(AboutRestaurantActivity.this, getResources().getString(R.string.toast_empty_phone_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mPassword.equalsIgnoreCase("")) {
                    Toast.makeText(AboutRestaurantActivity.this, getResources().getString(R.string.toast_empty_password_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!NetworkManager.isConnected(AboutRestaurantActivity.this)) {
                    Toast.makeText(AboutRestaurantActivity.this, getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (AllSettingsManager.isNullOrEmpty(restaurantLoginData.getImage())) {
                    Bitmap bmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_default_avatar);
                    mBase64 = PREFIX_BASE64_STRING + ImageZipper.getBase64forImage(bmap);
                    Log.d("Base64(default): ", mBase64);
                } else {
                    try {
                        URL url = new URL(restaurantLoginData.getImage());
                        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        mBase64 = PREFIX_BASE64_STRING + ImageZipper.getBase64forImage(image);
                        Log.d("Base64(url): ", mBase64);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }

                if (mAddress.equalsIgnoreCase(restaurantLoginData.getAddress())) {
                    doUpdateRestaurantInfo = new DoUpdateRestaurantInfo(AboutRestaurantActivity.this, mName, Double.parseDouble(restaurantLoginData.getLat()), mAddress, mPhone, Double.parseDouble(restaurantLoginData.getLng()), mEmail, mPassword, mBase64);
                } else {
                    doUpdateRestaurantInfo = new DoUpdateRestaurantInfo(AboutRestaurantActivity.this, mName, Double.parseDouble(mLocation.getLat()), mAddress, mPhone, Double.parseDouble(mLocation.getLng()), mEmail, mPassword, mBase64);
                }
                doUpdateRestaurantInfo.execute();
            }
        });
    }

    public class DoUpdateRestaurantInfo extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;
        private double mLat, mLng;
        private String mName = "", mAddress = "", mPhone = "", mEmail = "", mPassword = "", mImage = "";

        public DoUpdateRestaurantInfo(Context context, String name, double lat, String address, String phone, double lng, String email, String password, String image) {
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
            HttpRequestManager.HttpResponse response = HttpRequestManager.doRestPostRequest(AllUrls.getRestaurantUpdateUrl(), AllUrls.getRestaurantUpdateParameters(restaurantLoginData.getId(), mName, mLat, mAddress, mPhone, mLng, mEmail, mPassword, mImage), null);
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

                    //Update session data for login user
                    SessionManager.setStringSetting(AboutRestaurantActivity.this, SESSION_RESTAURANT_LOGIN_DATA, responseData.getData().get(0).toString());
                    SessionManager.setBooleanSetting(AboutRestaurantActivity.this, SESSION_IS_RESTAURANT_LOGGED_IN, true);

                    Toast.makeText(AboutRestaurantActivity.this, responseData.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AboutRestaurantActivity.this, responseData.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AboutRestaurantActivity.this, getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_CODE_IMAGE_PICKER && resultCode == RESULT_OK) {
//            mAdapter.setData(Matisse.obtainResult(data), Matisse.obtainPathResult(data));
            List<String> mData = Matisse.obtainPathResult(data);

            if (mData.size() == 1) {
                Log.d("MatisseImage: ", mData.get(0));
                Glide
                        .with(AboutRestaurantActivity.this)
                        .load(mData.get(0))
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                        .apply(new RequestOptions().circleCropTransform())
                        .into(ivUser);

                try {
                    File imageZipperFile = new ImageZipper(AboutRestaurantActivity.this)
                            .setQuality(100)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .compressToFile(new File(mData.get(0)));
                    mBase64 = PREFIX_BASE64_STRING + ImageZipper.getBase64forImage(imageZipperFile);
                    Log.d("MatisseImage(base64): ", mBase64);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else if (requestCode == INTENT_REQUEST_CODE_ADDRESS_SEARCH && resultCode == RESULT_OK) {
            if (data.getParcelableExtra(INTENT_KEY_SEARCH_ADDRESS) != null) {
                mLocation = data.getParcelableExtra(INTENT_KEY_SEARCH_ADDRESS);
                String addressText = String.format("%s, %s, %s, %s", mLocation.getStreet(), mLocation.getCity(), mLocation.getState(), mLocation.getCountry());
                Log.d("SearchLocationResult: ", mLocation.toString());
                tvAddress.setText(addressText);
            }
        }
    }
}
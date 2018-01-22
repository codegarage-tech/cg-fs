package com.rc.foodsignal.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rc.foodsignal.R;
import com.rc.foodsignal.animation.Techniques;
import com.rc.foodsignal.animation.YoYo;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.permission.activity.PermissionListActivity;

import static com.rc.foodsignal.util.AllConstants.SESSION_IS_LOCATION_ADDED;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class SplashActivity extends AppCompatActivity {

    SplashCountDownTimer splashCountDownTimer;
    private final long splashTime = 4 * 1000;
    private final long interval = 1 * 1000;
    TextView tvAppVersion;
    ImageView ivLoading;
    TextView tvLoadingMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        initSplashUI();
    }

    private void initSplashUI() {

        tvAppVersion = (TextView) findViewById(R.id.application_version);
        tvAppVersion.setText(getString(R.string.app_version_text) + " " + getString(R.string.app_version_name));

        tvLoadingMessage = (TextView) findViewById(R.id.tv_loading_message);
        tvLoadingMessage.setText(getString(R.string.txt_loading_message));

        YoYo.with(Techniques.Shake)
                .duration(1000)
                .playOn(findViewById(R.id.tv_app_name));

        //loading gif
        ivLoading = (ImageView) findViewById(R.id.iv_loading);
        Glide
                .with(SplashActivity.this)
                .load(R.drawable.gif_loading)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .into(ivLoading);

        splashCountDownTimer = new SplashCountDownTimer(splashTime, interval);
        splashCountDownTimer.start();
    }

    public class SplashCountDownTimer extends CountDownTimer {
        public SplashCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Intent intent = new Intent(SplashActivity.this, PermissionListActivity.class);
                startActivityForResult(intent, PermissionListActivity.REQUEST_CODE_PERMISSIONS);
            } else {
                navigateHomeActivity();
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }
    }

    private void navigateHomeActivity() {
        Intent intent;
        if (SessionManager.getBooleanSetting(SplashActivity.this, SESSION_IS_LOCATION_ADDED, false)) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, AddUserBasicInfoActivity.class);
        }
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PermissionListActivity.REQUEST_CODE_PERMISSIONS) {
            if (resultCode == RESULT_OK) {
                navigateHomeActivity();
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }
}

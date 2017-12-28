package com.rc.foodsignal.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rc.foodsignal.R;
import com.rc.foodsignal.model.UserBasicInfo;
import com.rc.foodsignal.util.AppUtils;
import com.reversecoder.library.storage.SessionManager;

import static com.rc.foodsignal.util.AllConstants.SESSION_USER_BASIC_INFO;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class ProfileActivity extends AppCompatActivity {

    String TAG = AppUtils.getTagName(ProfileActivity.class);
    TextView tvTitle;
    ImageView ivBack;
    LinearLayout llDone;

    TextView edtName, edtPhone, edtEmail;
    UserBasicInfo userBasicInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initUI();
        initActions();
    }

    private void initUI() {
        if (!AppUtils.isNullOrEmpty(SessionManager.getStringSetting(ProfileActivity.this, SESSION_USER_BASIC_INFO))) {
            Log.d(TAG, "Session data: " + SessionManager.getStringSetting(ProfileActivity.this, SESSION_USER_BASIC_INFO));
            userBasicInfo = UserBasicInfo.getResponseObject(SessionManager.getStringSetting(ProfileActivity.this, SESSION_USER_BASIC_INFO), UserBasicInfo.class);
        }

        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.setText(getString(R.string.title_activity_profile));
        llDone = (LinearLayout) findViewById(R.id.ll_done);
        edtName = (TextView) findViewById(R.id.tv_name);
        edtPhone = (TextView) findViewById(R.id.tv_phone);
        edtEmail = (TextView) findViewById(R.id.tv_email);

        if (userBasicInfo != null) {
            edtName.setText(userBasicInfo.getName());
            edtPhone.setText(userBasicInfo.getPhone());
            edtEmail.setText(userBasicInfo.getEmail());
        }
    }

    private void initActions() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        llDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
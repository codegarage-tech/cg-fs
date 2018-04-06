package com.rc.foodsignal.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.zagum.switchicon.SwitchIconView;
import com.rc.foodsignal.R;
import com.rc.foodsignal.model.UserBasicInfo;
import com.rc.foodsignal.util.AppUtils;
import com.reversecoder.gcm.listener.GcmResultListener;
import com.reversecoder.gcm.task.RegisterAppTask;
import com.reversecoder.gcm.task.UnregisterAppTask;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;

import static com.rc.foodsignal.util.AllConstants.SESSION_USER_BASIC_INFO;
import static com.reversecoder.gcm.util.GcmConfig.SESSION_IS_GCM_NOTIFICATION;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class NotificationListActivity extends AppCompatActivity {

    //Toolbar
    TextView tvTitle;
    ImageView ivBack;
    LinearLayout llNotification;
    SwitchIconView switchIconViewNotification;

    UserBasicInfo userBasicInfo;
    String TAG = AppUtils.getTagName(NotificationListActivity.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        initUI();
        initActions();
    }

    private void initUI() {
        //Toolbar
        ivBack = (ImageView) findViewById(R.id.iv_back);
        llNotification = (LinearLayout) findViewById(R.id.ll_notification);
        switchIconViewNotification = (SwitchIconView) findViewById(R.id.switch_notification);
        tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.setText(getString(R.string.title_activity_notification));

        if (SessionManager.getBooleanSetting(NotificationListActivity.this, SESSION_IS_GCM_NOTIFICATION, true)) {
            switchIconViewNotification.setIconEnabled(true);
        } else {
            switchIconViewNotification.setIconEnabled(false);
        }

        if (!AppUtils.isNullOrEmpty(SessionManager.getStringSetting(NotificationListActivity.this, SESSION_USER_BASIC_INFO))) {
            Log.d(TAG, "Session data: " + SessionManager.getStringSetting(NotificationListActivity.this, SESSION_USER_BASIC_INFO));
            userBasicInfo = UserBasicInfo.getResponseObject(SessionManager.getStringSetting(NotificationListActivity.this, SESSION_USER_BASIC_INFO), UserBasicInfo.class);
        }
    }

    private void initActions() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        llNotification.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (NetworkManager.isConnected(NotificationListActivity.this)) {

                    if (SessionManager.getBooleanSetting(NotificationListActivity.this, SESSION_IS_GCM_NOTIFICATION, true)) {

                        //Off notification
                        new UnregisterAppTask(NotificationListActivity.this, new GcmResultListener() {
                            @Override
                            public void onGcmResult(final Object result) {
                                boolean isUnregistered = (boolean) result;
                                if (isUnregistered) {
                                    switchIconViewNotification.setIconEnabled(false);
                                } else {
                                    switchIconViewNotification.setIconEnabled(true);
                                }
                            }
                        }).execute();
                    } else {
                        //On notification
                        new RegisterAppTask(NotificationListActivity.this, userBasicInfo.getUser_id(), new GcmResultListener() {
                            @Override
                            public void onGcmResult(final Object result) {
                                boolean isRegistered = (boolean) result;
                                if (isRegistered) {
                                    switchIconViewNotification.setIconEnabled(true);
                                } else {
                                    switchIconViewNotification.setIconEnabled(false);
                                }
                            }
                        }).execute();
                    }
                } else {
                    if (!NetworkManager.isConnected(NotificationListActivity.this)) {
                        Toast.makeText(NotificationListActivity.this, getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
package com.rc.foodsignal.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rc.foodsignal.R;
import com.rc.foodsignal.util.AppUtils;
import com.reversecoder.gcm.listener.GcmResultListener;
import com.reversecoder.gcm.task.RegisterAppTask;
import com.reversecoder.gcm.task.UnregisterAppTask;
import com.reversecoder.library.storage.SessionManager;

import in.shadowfax.proswipebutton.ProSwipeButton;

import static com.reversecoder.gcm.util.GcmConfig.SESSION_IS_NOTIFICATION;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class NotificationActivity extends AppCompatActivity {

    TextView tvTitle;
    ImageView ivBack;
    ProSwipeButton proSwipeButton;
    TextView tvNotificationStatus;
    String TAG = AppUtils.getTagName(NotificationActivity.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initUI();
        initActions();
    }

    private void initUI() {
        //Toolbar
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.setText(getString(R.string.title_activity_notification));

        //Pro Swipe Button
        proSwipeButton = (ProSwipeButton) findViewById(R.id.proswipebutton_notification);
        setProSwipeButton();
        tvNotificationStatus = (TextView) findViewById(R.id.tv_notification_status);
    }

    private void setProSwipeButton(){
        if (SessionManager.getBooleanSetting(NotificationActivity.this, SESSION_IS_NOTIFICATION, true)) {
            //Off notification
            proSwipeButton.setText(getString(R.string.txt_off_notification));
        } else {
            //On notification
            proSwipeButton.setText(getString(R.string.txt_on_notification));
        }
    }

    private void initActions() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        proSwipeButton.setOnSwipeListener(new ProSwipeButton.OnSwipeListener() {
            @Override
            public void onSwipeConfirm() {
                if (SessionManager.getBooleanSetting(NotificationActivity.this, SESSION_IS_NOTIFICATION, true)) {
                    //Off notification
                    new UnregisterAppTask(NotificationActivity.this, new GcmResultListener() {
                        @Override
                        public void onGcmResult(Object result) {
                            boolean isUnregistered = (boolean) result;
                            if (isUnregistered) {
                                proSwipeButton.showResultIcon(true);
                                tvNotificationStatus.setText(getString(R.string.txt_notification_setting_is_disabled));

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        proSwipeButton.showResultIcon(false);
                                        setProSwipeButton();
                                    }
                                }, 2000);
                            } else {
                                proSwipeButton.showResultIcon(false);
                                tvNotificationStatus.setText(getString(R.string.txt_notification_setting_is_enabled));
                            }
                        }
                    }).execute();
                } else {
                    //On notification
                    new RegisterAppTask(NotificationActivity.this, new GcmResultListener() {
                        @Override
                        public void onGcmResult(Object result) {
                            boolean isRegistered = (boolean) result;
                            if (isRegistered) {
                                proSwipeButton.showResultIcon(true);
                                tvNotificationStatus.setText(getString(R.string.txt_notification_setting_is_enabled));

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        proSwipeButton.showResultIcon(false);
                                        setProSwipeButton();
                                    }
                                }, 2000);
                            } else {
                                proSwipeButton.showResultIcon(false);
                                tvNotificationStatus.setText(getString(R.string.txt_notification_setting_is_disabled));
                            }
                        }
                    }).execute();
                }
            }
        });
    }


}

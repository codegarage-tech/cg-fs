package com.rc.foodsignal.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.zagum.switchicon.SwitchIconView;
import com.rc.foodsignal.R;
import com.rc.foodsignal.adapter.NotificationListViewAdapter;
import com.rc.foodsignal.model.ResponseNotification;
import com.rc.foodsignal.model.UserBasicInfo;
import com.rc.foodsignal.util.AllUrls;
import com.rc.foodsignal.util.AppUtils;
import com.rc.foodsignal.util.HttpRequestManager;
import com.reversecoder.gcm.listener.GcmResultListener;
import com.reversecoder.gcm.task.RegisterAppUserTask;
import com.reversecoder.gcm.task.UnregisterAppUserTask;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;

import static com.rc.foodsignal.util.AllConstants.SESSION_USER_BASIC_INFO;
import static com.reversecoder.gcm.util.GcmConfig.SESSION_IS_APP_USER_GCM_NOTIFICATION;

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
    ProgressDialog loadingDialog;
    ListView lvNotification;
    GetNotificationList getNotificationList;
    NotificationListViewAdapter notificationListViewAdapter;

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
        tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.setText(getString(R.string.title_activity_notification));

        lvNotification = (ListView) findViewById(R.id.lv_notification);
        notificationListViewAdapter = new NotificationListViewAdapter(NotificationListActivity.this);
        lvNotification.setAdapter(notificationListViewAdapter);

        switchIconViewNotification = (SwitchIconView) findViewById(R.id.switch_notification);
        if (SessionManager.getBooleanSetting(NotificationListActivity.this, SESSION_IS_APP_USER_GCM_NOTIFICATION, true)) {
            switchIconViewNotification.setIconEnabled(true);
        } else {
            switchIconViewNotification.setIconEnabled(false);
        }

        if (!AppUtils.isNullOrEmpty(SessionManager.getStringSetting(NotificationListActivity.this, SESSION_USER_BASIC_INFO))) {
            Log.d(TAG, "Session data: " + SessionManager.getStringSetting(NotificationListActivity.this, SESSION_USER_BASIC_INFO));
            userBasicInfo = UserBasicInfo.getResponseObject(SessionManager.getStringSetting(NotificationListActivity.this, SESSION_USER_BASIC_INFO), UserBasicInfo.class);

            if (!NetworkManager.isConnected(NotificationListActivity.this)) {
                Toast.makeText(NotificationListActivity.this, getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            } else {
                getNotificationList = new GetNotificationList(NotificationListActivity.this, userBasicInfo.getUser_id());
                getNotificationList.execute();
            }
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

                    if (SessionManager.getBooleanSetting(NotificationListActivity.this, SESSION_IS_APP_USER_GCM_NOTIFICATION, true)) {

                        //Off notification
                        new UnregisterAppUserTask(NotificationListActivity.this, new GcmResultListener() {
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
                        new RegisterAppUserTask(NotificationListActivity.this, userBasicInfo.getUser_id(), new GcmResultListener() {
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

    private class GetNotificationList extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;
        private String mAppUserId = "";

        public GetNotificationList(Context context, String appUserId) {
            mContext = context;
            mAppUserId = appUserId;
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
            HttpRequestManager.HttpResponse response = HttpRequestManager.doGetRequest(AllUrls.getAllNotificationsUrl(mAppUserId));
            return response;
        }

        @Override
        protected void onPostExecute(HttpRequestManager.HttpResponse result) {

            if (loadingDialog != null
                    && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }

            if (result != null && result.isSuccess() && !AppUtils.isNullOrEmpty(result.getResult().toString())) {
                Log.d(TAG, "success response from web: " + result.getResult().toString());
                ResponseNotification responseData = ResponseNotification.getResponseObject(result.getResult().toString().replace("\\", "").trim(), ResponseNotification.class);
                Log.d(TAG, "success response from object: " + responseData.toString());

                if (responseData.getStatus().equalsIgnoreCase("success") && (responseData.getData().size() > 0)) {
                    Log.d(TAG, "success wrapper: " + responseData.getData().toString());

                    //Update list view
                    notificationListViewAdapter.setData(responseData.getData());
                } else {
                    Toast.makeText(NotificationListActivity.this, getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(NotificationListActivity.this, getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
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
import android.widget.TextView;
import android.widget.Toast;

import com.rc.foodsignal.R;
import com.rc.foodsignal.model.RestaurantLoginData;
import com.rc.foodsignal.util.AllUrls;
import com.rc.foodsignal.util.AppUtils;
import com.rc.foodsignal.util.HttpRequestManager;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import static com.rc.foodsignal.util.AllConstants.SESSION_RESTAURANT_LOGIN_DATA;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class OrderListActivity extends AppCompatActivity {

    //Toolbar
    TextView tvTitle;
    ImageView ivBack;
    LinearLayout llNotification;
    RestaurantLoginData restaurantLoginData;
    OrderListTask orderListTask;

    String TAG = AppUtils.getTagName(OrderListActivity.class);
    ProgressDialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        initUI();
        initActions();
    }

    private void initUI() {
        //Toolbar
        ivBack = (ImageView) findViewById(R.id.iv_back);
        llNotification = (LinearLayout) findViewById(R.id.ll_notification);
        tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.setText(getString(R.string.title_activity_order));

        //set restaurant information
        if (!AllSettingsManager.isNullOrEmpty(SessionManager.getStringSetting(OrderListActivity.this, SESSION_RESTAURANT_LOGIN_DATA))) {
            restaurantLoginData = RestaurantLoginData.getResponseObject(SessionManager.getStringSetting(OrderListActivity.this, SESSION_RESTAURANT_LOGIN_DATA), RestaurantLoginData.class);
            Log.d("LoginUser: ", restaurantLoginData.toString());

            if (restaurantLoginData != null) {
                if (!NetworkManager.isConnected(OrderListActivity.this)) {
                    Toast.makeText(OrderListActivity.this, getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                } else {
                    orderListTask = new OrderListTask(OrderListActivity.this, restaurantLoginData.getId());
                    orderListTask.execute();
                }
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
    }

    private class OrderListTask extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;
        private String mRestaurantId = "";

        public OrderListTask(Context context, String restaurantId) {
            mContext = context;
            mRestaurantId = restaurantId;
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
            HttpRequestManager.HttpResponse response = HttpRequestManager.doGetRequest(AllUrls.getAllOrdersUrl(mRestaurantId));
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
//                ResponseNotification responseData = ResponseNotification.getResponseObject(result.getResult().toString().replace("\\", "").trim(), ResponseNotification.class);
//                Log.d(TAG, "success response from object: " + responseData.toString());
//
//                if (responseData.getStatus().equalsIgnoreCase("success") && (responseData.getData().size() > 0)) {
//                    Log.d(TAG, "success wrapper: " + responseData.getData().toString());
//
//                    //Update list view
//                    notificationListViewAdapter.setData(responseData.getData());
//                } else {
//                    Toast.makeText(OrderListActivity.this, getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
//                }

            } else {
                Toast.makeText(OrderListActivity.this, getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
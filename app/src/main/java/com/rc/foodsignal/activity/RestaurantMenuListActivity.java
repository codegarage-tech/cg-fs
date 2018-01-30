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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rc.foodsignal.R;
import com.rc.foodsignal.adapter.RestaurantMenuListViewAdapter;
import com.rc.foodsignal.model.ResponseRestaurantMenu;
import com.rc.foodsignal.model.RestaurantLoginData;
import com.rc.foodsignal.util.AllUrls;
import com.rc.foodsignal.util.AppUtils;
import com.rc.foodsignal.util.HttpRequestManager;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;

import static com.rc.foodsignal.util.AllConstants.INTENT_REQUEST_CODE_RESTAURANT_MENU_LIST;
import static com.rc.foodsignal.util.AllConstants.SESSION_RESTAURANT_LOGIN_DATA;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class RestaurantMenuListActivity extends AppCompatActivity {

    TextView tvTitle;
    ImageView ivBack;
    LinearLayout llAddRestaurantMenu;
    ProgressDialog loadingDialog;

    RestaurantLoginData restaurantLoginData;
    GetRestaurantMenuList getRestaurantMenuList;
    RestaurantMenuListViewAdapter restaurantMenuListViewAdapter;
    ListView lvRestaurantMenu;
    String TAG = AppUtils.getTagName(RestaurantMenuListActivity.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu_list);

        initUI();
        initActions();
    }

    private void initUI() {
        if (!AppUtils.isNullOrEmpty(SessionManager.getStringSetting(RestaurantMenuListActivity.this, SESSION_RESTAURANT_LOGIN_DATA))) {
            Log.d(TAG, "Session data: " + SessionManager.getStringSetting(RestaurantMenuListActivity.this, SESSION_RESTAURANT_LOGIN_DATA));
            restaurantLoginData = RestaurantLoginData.getResponseObject(SessionManager.getStringSetting(RestaurantMenuListActivity.this, SESSION_RESTAURANT_LOGIN_DATA), RestaurantLoginData.class);
        }

        ivBack = (ImageView) findViewById(R.id.iv_back);
        llAddRestaurantMenu = (LinearLayout) findViewById(R.id.ll_done);

        tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.setText(getString(R.string.title_activity_restaurant_menu));

        lvRestaurantMenu = (ListView) findViewById(R.id.lv_menu);
        restaurantMenuListViewAdapter = new RestaurantMenuListViewAdapter(RestaurantMenuListActivity.this);
        lvRestaurantMenu.setAdapter(restaurantMenuListViewAdapter);

        if (!NetworkManager.isConnected(RestaurantMenuListActivity.this)) {
            Toast.makeText(RestaurantMenuListActivity.this, getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
        } else {
            getRestaurantMenuList = new GetRestaurantMenuList(RestaurantMenuListActivity.this, restaurantLoginData.getId());
            getRestaurantMenuList.execute();
        }
    }

    private void initActions() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        llAddRestaurantMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddress = new Intent(RestaurantMenuListActivity.this, AddRestaurantMenuActivity.class);
                startActivityForResult(intentAddress, INTENT_REQUEST_CODE_RESTAURANT_MENU_LIST);
            }
        });
    }

    private class GetRestaurantMenuList extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;
        private String mRestaurantId = "";

        public GetRestaurantMenuList(Context context, String restaurantId) {
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
            HttpRequestManager.HttpResponse response = HttpRequestManager.doGetRequest(AllUrls.getRestaurantMenuUrl("6"));
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
                ResponseRestaurantMenu responseData = ResponseRestaurantMenu.getResponseObject(result.getResult().toString(), ResponseRestaurantMenu.class);
                Log.d(TAG, "success response from object: " + responseData.toString());

                if (responseData.getStatus().equalsIgnoreCase("success") && (responseData.getData().size() > 0)) {
                    Log.d(TAG, "success wrapper: " + responseData.getData().get(0).toString());

                    //Update listview
                    restaurantMenuListViewAdapter.setData(responseData.getData());
                } else {
                    Toast.makeText(RestaurantMenuListActivity.this, getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(RestaurantMenuListActivity.this, getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case INTENT_REQUEST_CODE_RESTAURANT_MENU_LIST: {
                if (data != null && resultCode == RESULT_OK) {
//                    if (data.getBooleanExtra(INTENT_KEY_ADDRESS_LIST, false)) {
//
//                        //Update address list
//                        Location mLocation = Location.getResponseObject(SessionManager.getStringSetting(RestaurantMenuListActivity.this, SESSION_SELECTED_LOCATION), Location.class);
//                        restaurantMenuListViewAdapter.addData(mLocation);
//                    }
                }
                break;
            }
            default:
                break;
        }
    }
}

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
import com.rc.foodsignal.adapter.AddressListViewAdapter;
import com.rc.foodsignal.model.Location;
import com.rc.foodsignal.model.ResponseAddressList;
import com.rc.foodsignal.model.UserBasicInfo;
import com.rc.foodsignal.util.AllUrls;
import com.rc.foodsignal.util.AppUtils;
import com.rc.foodsignal.util.HttpRequestManager;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;

import static com.rc.foodsignal.util.AllConstants.INTENT_KEY_ADDRESS_LIST;
import static com.rc.foodsignal.util.AllConstants.INTENT_REQUEST_CODE_ADDRESS_LIST;
import static com.rc.foodsignal.util.AllConstants.SESSION_SELECTED_LOCATION;
import static com.rc.foodsignal.util.AllConstants.SESSION_USER_BASIC_INFO;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class AddressListActivity extends AppCompatActivity {

    TextView tvTitle;
    ImageView ivBack;
    LinearLayout llAddAddress;
    ProgressDialog loadingDialog;

    UserBasicInfo userBasicInfo;
    GetAddressList getAddressList;
    AddressListViewAdapter addressListViewAdapter;
    ListView lvAddress;
    String TAG = AppUtils.getTagName(AddressListActivity.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        initUI();
        initActions();
    }

    private void initUI() {
        if (!AppUtils.isNullOrEmpty(SessionManager.getStringSetting(AddressListActivity.this, SESSION_USER_BASIC_INFO))) {
            Log.d(TAG, "Session data: " + SessionManager.getStringSetting(AddressListActivity.this, SESSION_USER_BASIC_INFO));
            userBasicInfo = UserBasicInfo.getResponseObject(SessionManager.getStringSetting(AddressListActivity.this, SESSION_USER_BASIC_INFO), UserBasicInfo.class);
        }

        ivBack = (ImageView) findViewById(R.id.iv_back);
        llAddAddress = (LinearLayout) findViewById(R.id.ll_done);

        tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.setText(getString(R.string.title_activity_address_list));

        lvAddress = (ListView) findViewById(R.id.lv_address);
        addressListViewAdapter = new AddressListViewAdapter(AddressListActivity.this);
        lvAddress.setAdapter(addressListViewAdapter);

        if (!NetworkManager.isConnected(AddressListActivity.this)) {
            Toast.makeText(AddressListActivity.this, getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
        } else {
            getAddressList = new GetAddressList(AddressListActivity.this, userBasicInfo.getUser_id());
            getAddressList.execute();
        }
    }

    private void initActions() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        llAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddress = new Intent(AddressListActivity.this, AddAddressActivity.class);
                startActivityForResult(intentAddress, INTENT_REQUEST_CODE_ADDRESS_LIST);
            }
        });
    }

    private class GetAddressList extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;
        private String mUserId = "";

        public GetAddressList(Context context, String userId) {
            mContext = context;
            mUserId = userId;
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
            HttpRequestManager.HttpResponse response = HttpRequestManager.doGetRequest(AllUrls.getAllUserLocationsUrl(mUserId));
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
                ResponseAddressList responseData = ResponseAddressList.getResponseObject(result.getResult().toString(), ResponseAddressList.class);
                Log.d(TAG, "success response from object: " + responseData.toString());

                if (responseData.getStatus().equalsIgnoreCase("success") && (responseData.getData().size() > 0)) {
                    Log.d(TAG, "success wrapper: " + responseData.getData().get(0).toString());

                    //Update listview
                    addressListViewAdapter.setData(responseData.getData());
                } else {
                    Toast.makeText(AddressListActivity.this, getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(AddressListActivity.this, getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case INTENT_REQUEST_CODE_ADDRESS_LIST: {
                if (data != null && resultCode == RESULT_OK) {
                    if (data.getBooleanExtra(INTENT_KEY_ADDRESS_LIST, false)) {

                        //Update address list
                        Location mLocation = Location.getResponseObject(SessionManager.getStringSetting(AddressListActivity.this, SESSION_SELECTED_LOCATION), Location.class);
                        addressListViewAdapter.addData(mLocation);
                    }
                }
                break;
            }
            default:
                break;
        }
    }
}

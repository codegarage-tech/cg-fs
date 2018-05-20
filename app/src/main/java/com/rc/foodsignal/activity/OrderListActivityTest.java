package com.rc.foodsignal.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;
import com.rc.foodsignal.R;
import com.rc.foodsignal.model.FoodItem;
import com.rc.foodsignal.model.OrderListItem;
import com.rc.foodsignal.model.ResponseOrderList;
import com.rc.foodsignal.model.RestaurantLoginData;
import com.rc.foodsignal.util.AllUrls;
import com.rc.foodsignal.util.AppUtils;
import com.rc.foodsignal.util.HttpRequestManager;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import java.util.ArrayList;

import static com.rc.foodsignal.util.AllConstants.SESSION_RESTAURANT_LOGIN_DATA;
import static com.rc.foodsignal.util.AppUtils.isSimSupport;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class OrderListActivityTest extends AppCompatActivity {

    //Toolbar
    TextView tvTitle;
    ImageView ivBack;
    LinearLayout llNotification;
    RestaurantLoginData restaurantLoginData;
    OrderListTask orderListTask;

    String TAG = AppUtils.getTagName(OrderListActivityTest.class);
    ProgressDialog loadingDialog;
    ExpandingList expandingListOrder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list_test);

        initUI();
        initActions();
    }

    private void initUI() {
        //Toolbar
        ivBack = (ImageView) findViewById(R.id.iv_back);
        llNotification = (LinearLayout) findViewById(R.id.ll_notification);
        tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.setText(getString(R.string.title_activity_order));

        expandingListOrder = findViewById(R.id.expanding_list_order);

        //set restaurant information
        if (!AllSettingsManager.isNullOrEmpty(SessionManager.getStringSetting(OrderListActivityTest.this, SESSION_RESTAURANT_LOGIN_DATA))) {
            restaurantLoginData = RestaurantLoginData.getResponseObject(SessionManager.getStringSetting(OrderListActivityTest.this, SESSION_RESTAURANT_LOGIN_DATA), RestaurantLoginData.class);
            Log.d("LoginUser: ", restaurantLoginData.toString());

            if (restaurantLoginData != null) {
                if (!NetworkManager.isConnected(OrderListActivityTest.this)) {
                    Toast.makeText(OrderListActivityTest.this, getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                } else {
                    orderListTask = new OrderListTask(OrderListActivityTest.this, restaurantLoginData.getId());
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

    private void createExpandingItems(ArrayList<OrderListItem> orderListItems) {
        for (int i = 0; i < orderListItems.size(); i++) {
            addItem(orderListItems.get(i), R.color.blue, R.drawable.ic_ghost);
        }
    }

    private void addItem(final OrderListItem orderListItem, int colorRes, int iconRes) {
        //Let's create an item with R.layout.expanding_layout
        final ExpandingItem item = expandingListOrder.createNewItem(R.layout.expanding_layout);

        //If item creation is successful, let's configure it
        if (item != null) {
            item.setIndicatorColorRes(colorRes);
            item.setIndicatorIconRes(iconRes);
            //It is possible to get any view inside the inflated layout. Let's set the text in the item
            ((TextView) item.findViewById(R.id.tv_user_name)).setText(orderListItem.getUser_name());
            ((TextView) item.findViewById(R.id.tv_user_address)).setText(orderListItem.getUser_address());

            //We can create items in batch.
            ArrayList<FoodItem> foodItems = orderListItem.getAllFoodItems();
            item.createSubItems(foodItems.size());
            for (int i = 0; i < foodItems.size(); i++) {
                //Let's get the created sub item by its index
                final View view = item.getSubItemView(i);

                //Let's set some values in
                if (i == (item.getSubItemsCount() - 1)) {
                    ((View) view.findViewById(R.id.view_divider)).setVisibility(View.VISIBLE);
                } else {
                    ((View) view.findViewById(R.id.view_divider)).setVisibility(View.GONE);
                }
                configureSubItem(item, view, foodItems.get(i));
            }
            item.findViewById(R.id.iv_user_email).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!AllSettingsManager.isNullOrEmpty(orderListItem.getUser_email().trim())) {
                        /***********************************
                         * Need to check internet connection
                         **********************************/
                        Intent callIntent = new Intent(Intent.ACTION_SENDTO);
                        callIntent.setData(Uri.parse("mailto:" + orderListItem.getUser_email().trim()));
                        startActivity(callIntent);
                    }
                }
            });

            item.findViewById(R.id.iv_user_phone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!AllSettingsManager.isNullOrEmpty(orderListItem.getUser_phone().trim())) {
                        if (isSimSupport(OrderListActivityTest.this)) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + orderListItem.getUser_phone().trim()));
                            startActivity(callIntent);
                        } else {
                            Toast.makeText(OrderListActivityTest.this, getString(R.string.toast_your_sim_card_is_absent), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    private void configureSubItem(final ExpandingItem item, final View view, FoodItem foodItem) {
        ((TextView) view.findViewById(R.id.tv_item_name)).setText(foodItem.getName());
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
                ResponseOrderList responseData = ResponseOrderList.getResponseObject(result.getResult().toString(), ResponseOrderList.class);
                Log.d(TAG, "success response from object: " + responseData.toString());

                if (responseData.getStatus().equalsIgnoreCase("1") && (responseData.getData().size() > 0)) {
                    Log.d(TAG, "success wrapper: " + responseData.getData().toString());

                    //Update list view
                    createExpandingItems(responseData.getData());
                } else {
                    Toast.makeText(OrderListActivityTest.this, getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(OrderListActivityTest.this, getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
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
import com.rc.foodsignal.dialog.RefundProcessingDialog;
import com.rc.foodsignal.model.FoodItem;
import com.rc.foodsignal.model.OrderListItem;
import com.rc.foodsignal.model.ResponseOrderList;
import com.rc.foodsignal.model.UserBasicInfo;
import com.rc.foodsignal.util.AllUrls;
import com.rc.foodsignal.util.AppUtils;
import com.rc.foodsignal.util.HttpRequestManager;
import com.reversecoder.gcm.util.DetailIntentType;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import java.util.ArrayList;

import static com.rc.foodsignal.util.AllConstants.SESSION_IS_RESTAURANT_LOGGED_IN;
import static com.rc.foodsignal.util.AllConstants.SESSION_USER_BASIC_INFO;
import static com.rc.foodsignal.util.AppUtils.isSimSupport;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class UserOrderListActivity extends AppCompatActivity {

    //Toolbar
    TextView tvTitle;
    ImageView ivBack;
    LinearLayout llNotification;

    UserBasicInfo userBasicInfo;
    OrderListTask orderListTask;
    DetailIntentType mDetailIntentType = DetailIntentType.OTHER;
    String TAG = AppUtils.getTagName(UserOrderListActivity.class);
    ProgressDialog loadingDialog;
    ExpandingList expandingListOrder;

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
        tvTitle.setText(getString(R.string.title_activity_order_user));

        expandingListOrder = findViewById(R.id.expanding_list_order);

        //set restaurant information
        setData();
    }

    private void initActions() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setData() {
        if (!SessionManager.getBooleanSetting(UserOrderListActivity.this, SESSION_IS_RESTAURANT_LOGGED_IN, false)) {

            if (!AppUtils.isNullOrEmpty(SessionManager.getStringSetting(UserOrderListActivity.this, SESSION_USER_BASIC_INFO))) {
                Log.d(TAG, "Session data: " + SessionManager.getStringSetting(UserOrderListActivity.this, SESSION_USER_BASIC_INFO));
                userBasicInfo = UserBasicInfo.getResponseObject(SessionManager.getStringSetting(UserOrderListActivity.this, SESSION_USER_BASIC_INFO), UserBasicInfo.class);

                if (userBasicInfo != null) {
                    if (!NetworkManager.isConnected(UserOrderListActivity.this)) {
                        Toast.makeText(UserOrderListActivity.this, getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                    } else {
                        orderListTask = new OrderListTask(UserOrderListActivity.this, userBasicInfo.getUser_id());
                        orderListTask.execute();
                    }
                }
            }
        } else {
            Toast.makeText(UserOrderListActivity.this, getResources().getString(R.string.toast_you_need_to_logout_for_being_app_user), Toast.LENGTH_LONG).show();
            onBackPressed();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "New intent is called");
        setData();
    }

    /**************************
     * Expanding view methods *
     **************************/
    private void createExpandingItems(ArrayList<OrderListItem> orderListItems) {
        for (int i = 0; i < orderListItems.size(); i++) {
            addItem(orderListItems.get(i), R.color.blue, R.drawable.ic_vector_bag_empty_white);
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

            //Order status
            setStatusData(item, orderListItem.getIs_order_accepted(), orderListItem.getIs_refunded());

            //We can create items in batch.
            ArrayList<FoodItem> foodItems = orderListItem.getAllFoodItems();
            if (foodItems.size() > 0) {
                //Here plus 3 for subtotal, shipping cost and total cost
                item.createSubItems(foodItems.size() + 3);
                for (int i = 0; i < foodItems.size(); i++) {
                    //Let's get the created sub item by its index
                    final View view = item.getSubItemView(i);

                    //Let's set some values in
                    if (i == (foodItems.size() - 1)) {
                        ((View) view.findViewById(R.id.view_divider)).setVisibility(View.VISIBLE);
                    } else {
                        ((View) view.findViewById(R.id.view_divider)).setVisibility(View.GONE);
                    }
                    configureSubItem(item, view, foodItems.get(i));
                }

                //For subtotal cost items
                final View subTotlaView = item.getSubItemView(foodItems.size());
                configureSubItem(item, subTotlaView, new FoodItem(getString(R.string.txt_subtotal), orderListItem.getSub_total()));
                //For shipping cost items
                final View shippingCostView = item.getSubItemView(foodItems.size() + 1);
                configureSubItem(item, shippingCostView, new FoodItem(getString(R.string.txt_shipping_cost), (orderListItem.getDelivery_type().equalsIgnoreCase(getString(R.string.txt_delivery))) ? orderListItem.getShipping_cost() : "00.00"));
                //For total cost items
                final View totalCostView = item.getSubItemView(foodItems.size() + 2);
                configureSubItem(item, totalCostView, new FoodItem(getString(R.string.txt_total), orderListItem.getTotal_amount()));
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
                        if (isSimSupport(UserOrderListActivity.this)) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + orderListItem.getUser_phone().trim()));
                            startActivity(callIntent);
                        } else {
                            Toast.makeText(UserOrderListActivity.this, getString(R.string.toast_your_sim_card_is_absent), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            item.findViewById(R.id.iv_order_process).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RefundProcessingDialog refundProcessingDialog = new RefundProcessingDialog(UserOrderListActivity.this, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            int orderProcessingStatus = -1;
                            switch (which) {
                                case DialogInterface.BUTTON_NEGATIVE:
//                                    orderProcessingStatus = 0;
                                    break;
                                case DialogInterface.BUTTON_POSITIVE:
//                                    orderProcessingStatus = 1;
                                    break;
                            }
//
//                            if (!NetworkManager.isConnected(UserOrderListActivity.this)) {
//                                Toast.makeText(UserOrderListActivity.this, getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
//                            } else {
//                                if (orderProcessingStatus != -1) {
//                                    orderProcessingTask = new RestaurantOrderListActivity.OrderProcessingTask(RestaurantOrderListActivity.this, item, orderListItem.getId(), orderProcessingStatus + "");
//                                    orderProcessingTask.execute();
//                                }
//                            }
                        }
                    });
                    refundProcessingDialog.createView().show();
                }
            });
        }
    }

    private void configureSubItem(final ExpandingItem item, final View view, FoodItem foodItem) {
        if (foodItem.getName().contains(getString(R.string.txt_subtotal))
                || foodItem.getName().contains(getString(R.string.txt_shipping_cost))
                || foodItem.getName().contains(getString(R.string.txt_total))) {
            ((TextView) view.findViewById(R.id.tv_item_name)).setText(foodItem.getName());
            ((TextView) view.findViewById(R.id.tv_item_calculation)).setText("$" + foodItem.getPrice());
        } else {
            int itemQuantity = 0;
            float itemPrice = 0.0f;
            float priceWithQuantity = 0.0f;
            try {
                itemQuantity = Integer.parseInt(foodItem.getOrder_item_quantity());
                itemPrice = Float.parseFloat(foodItem.getOrder_item_price());
                Log.d(TAG, "total(itemQuantity): " + itemQuantity);
                Log.d(TAG, "total(itemPrice): " + itemPrice);
                priceWithQuantity = itemQuantity * itemPrice;
                Log.d(TAG, "total(priceWithQuantity): " + priceWithQuantity);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            ((TextView) view.findViewById(R.id.tv_item_name)).setText(foodItem.getName());
            ((TextView) view.findViewById(R.id.tv_item_calculation)).setText(foodItem.getOrder_item_quantity() + " x $" + foodItem.getOrder_item_price() + " = $" + priceWithQuantity);
        }
    }

    private class OrderListTask extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;
        private String mUserId = "";

        public OrderListTask(Context context, String userId) {
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
            HttpRequestManager.HttpResponse response = HttpRequestManager.doGetRequest(AllUrls.getUserOrdersUrl(mUserId));
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
                    Toast.makeText(UserOrderListActivity.this, getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(UserOrderListActivity.this, getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setStatusData(ExpandingItem expandingItem, String isOrderAccepted, String isRefunded) {
        TextView tvOrderStatus = (TextView) expandingItem.findViewById(R.id.tv_order_status);
        ImageView ivOrderStatus = (ImageView) expandingItem.findViewById(R.id.iv_order_process);
        String strOrderStatus = "";

        if (isOrderAccepted.equalsIgnoreCase("1")) {
            strOrderStatus = getString(R.string.txt_request_accepted);

            tvOrderStatus.setText(strOrderStatus);
            tvOrderStatus.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            ivOrderStatus.setBackgroundResource(R.drawable.ic_vector_accepted);
            ivOrderStatus.setEnabled(false);
        } else if (isOrderAccepted.equalsIgnoreCase("0")) {
            if(isRefunded.equalsIgnoreCase("0")){
                strOrderStatus = getString(R.string.txt_request_for_refund);
                ivOrderStatus.setEnabled(true);
            }else if(isRefunded.equalsIgnoreCase("1")){
                strOrderStatus = getString(R.string.txt_refunded);
                ivOrderStatus.setEnabled(false);
            }

            tvOrderStatus.setText(strOrderStatus);
            tvOrderStatus.setTextColor(getResources().getColor(R.color.red));

            ivOrderStatus.setBackgroundResource(R.drawable.ic_vector_canceled);
        } else {
            strOrderStatus = getString(R.string.txt_request_pending);

            tvOrderStatus.setText(strOrderStatus);
            tvOrderStatus.setTextColor(getResources().getColor(R.color.blue));

            ivOrderStatus.setBackgroundResource(R.drawable.ic_vector_pending);
            ivOrderStatus.setEnabled(false);
        }
    }
}
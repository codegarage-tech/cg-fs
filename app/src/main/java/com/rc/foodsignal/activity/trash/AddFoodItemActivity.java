//package com.rc.foodsignal.activity.trash;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.rc.foodsignal.R;
//import com.rc.foodsignal.util.AllUrls;
//import com.rc.foodsignal.util.AppUtils;
//import com.rc.foodsignal.util.HttpRequestManager;
//
///**
// * Author: Abir Ahmed
// * Email: abir.eol@gmail.com
// */
//
//public class AddFoodItemActivity extends AppCompatActivity {
//
//    TextView tvAddFoodItem;
//    GetAddFoodItem doAddFoodItem;
//    String TAG = AppUtils.getTagName(AddFoodItemActivity.class);
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_add_food_item);
//        initLoginUI();
//        initAddFoodItemAction();
//    }
//
//    private void initLoginUI(){
//        tvAddFoodItem = (TextView) findViewById(R.id.tv_addFoodItem);
//    }
//
//    private void initAddFoodItemAction(){
//        String mName = "test burger", mMenuId = "1", mPrice = "50.00", mRestaurantId ="6", mImage = "";
//        int mId = 0;
//
//        doAddFoodItem = new GetAddFoodItem(AddFoodItemActivity.this, mName, mMenuId, mPrice, mRestaurantId, mImage, mId);
//        doAddFoodItem.execute();
//    }
//
//    private class GetAddFoodItem extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {
//
//        private Context mContext;
//        private String mName = "", mMenuId = "", mPrice = "", mRestaurantId = "", mImage = "";
//
//        public GetAddFoodItem(Context context, String name, String menuId, String price, String restaurantId, String image, int id) {
//            mContext = context;
//            mName = name;
//            mMenuId = menuId;
//            mPrice = price;
//            mRestaurantId = restaurantId;
//            mImage = image;
//        }
//
//        @Override
//        protected HttpRequestManager.HttpResponse doInBackground(String... params) {
//            HttpRequestManager.HttpResponse response = HttpRequestManager.doRestPostRequest(AllUrls.getAddFoodItemUrl(), AllUrls.getAddFoodItemParameters(mName, mMenuId, mPrice, mRestaurantId, mImage), null);
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(HttpRequestManager.HttpResponse result) {
//
//            if (result.isSuccess() && !AppUtils.isNullOrEmpty(result.getResult().toString())) {
//                Log.d(TAG, "success response: " + result.getResult().toString());
//
//                tvAddFoodItem.setText(result.getResult().toString());
//
//            } else {
//                Toast.makeText(AddFoodItemActivity.this, getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//}

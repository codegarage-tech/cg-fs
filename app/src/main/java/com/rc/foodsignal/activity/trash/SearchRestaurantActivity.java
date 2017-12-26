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
//public class SearchRestaurantActivity extends AppCompatActivity {
//
//    TextView tvSearchRestaurant;
//    GetSearchRestaurants doGetSearchRestaurants;
//    String TAG = AppUtils.getTagName(RegisterDeviceActivity.class);
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_search_restaurant);
//        initSearchRestaurantUI();
//        initSearchRestaurantAction();
//    }
//
//    private void initSearchRestaurantUI() {
//        tvSearchRestaurant = (TextView) findViewById(R.id.tv_searchRestaurant);
//    }
//
//    private void initSearchRestaurantAction() {
//
//        int mLat = 2343;
//        double mLng = 23.43;
//        String mCategoryId = "1";
//
//        doGetSearchRestaurants = new GetSearchRestaurants(SearchRestaurantActivity.this, mLat, mLng, mCategoryId);
//        doGetSearchRestaurants.execute();
//
//    }
//
//    public class GetSearchRestaurants extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {
//
//        private Context mContext;
//        private int mLat = 0;
//        private double mLng = 0.00;
//        private String mCategoryId = "";
//
//        public GetSearchRestaurants(Context context, int lat, double lng, String categoryId) {
//            this.mContext = context;
//            this.mLat = lat;
//            this.mLng = lng;
//            this.mCategoryId = categoryId;
//        }
//
//        @Override
//        protected void onPreExecute() {
//        }
//
//        @Override
//        protected HttpRequestManager.HttpResponse doInBackground(String... params) {
//            HttpRequestManager.HttpResponse response = HttpRequestManager.doRestPostRequest(AllUrls.getSearchFoodUrl(), AllUrls.getSearchFoodParameters(mLat, mLng, mCategoryId), null);
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(HttpRequestManager.HttpResponse result) {
//
//            if (result.isSuccess() && !AppUtils.isNullOrEmpty(result.getResult().toString())) {
//                Log.d(TAG, "success response: " + result.getResult().toString());
//
//                tvSearchRestaurant.setText(result.getResult().toString());
//
//            } else {
//                Toast.makeText(SearchRestaurantActivity.this, getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//}

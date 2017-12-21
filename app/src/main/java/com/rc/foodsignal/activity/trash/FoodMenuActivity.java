package com.rc.foodsignal.activity.trash;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.rc.foodsignal.R;
import com.rc.foodsignal.model.ResponseMenu;
import com.rc.foodsignal.util.AllUrls;
import com.rc.foodsignal.util.AppUtils;
import com.rc.foodsignal.util.HttpRequestManager;

/**
 * Author: Abir Ahmed
 * Email: abir.eol@gmail.com
 */

public class FoodMenuActivity extends AppCompatActivity {

    TextView tvFoodMenu;
    private static final String TAG = FoodMenuActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        initUI();
        initFoodMenuAction();
    }

    private void initUI(){
        tvFoodMenu = (TextView) findViewById(R.id.tv_foodMenu);
    }

    private void initFoodMenuAction(){
        new GetAllFoodMenu(FoodMenuActivity.this).execute();
    }

    private class GetAllFoodMenu extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;

        public GetAllFoodMenu(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected HttpRequestManager.HttpResponse doInBackground(String... params) {
            HttpRequestManager.HttpResponse response = HttpRequestManager.doGetRequest(AllUrls.getAllFoodMenuUrl());
            return response;
        }

        @Override
        protected void onPostExecute(HttpRequestManager.HttpResponse result) {

            if (result.isSuccess() && !AppUtils.isNullOrEmpty(result.getResult().toString())) {
                Log.d(TAG, "success response from web: " + result.getResult().toString());

                ResponseMenu responseMenu = ResponseMenu.getResponseObject(result.getResult().toString(), ResponseMenu.class);

                if(responseMenu.getStatus().equalsIgnoreCase("1") && (responseMenu.getData().size() > 0)){
                    Log.d(TAG, "success response from web: " + responseMenu.toString());

                    tvFoodMenu.setText(responseMenu.toString());
                } else {
                    Toast.makeText(FoodMenuActivity.this, getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(FoodMenuActivity.this, getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

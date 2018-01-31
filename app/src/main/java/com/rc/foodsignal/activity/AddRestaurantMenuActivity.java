package com.rc.foodsignal.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.nex3z.flowlayout.FlowLayout;
import com.nex3z.flowlayout.FlowLayoutManager;
import com.rc.foodsignal.R;
import com.rc.foodsignal.model.DataFoodCategory;
import com.rc.foodsignal.model.FoodCategory;
import com.rc.foodsignal.model.ResponseFoodCategory;
import com.rc.foodsignal.model.RestaurantLoginData;
import com.rc.foodsignal.util.AllUrls;
import com.rc.foodsignal.util.AppUtils;
import com.rc.foodsignal.util.HttpRequestManager;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.rc.foodsignal.util.AllConstants.DEFAULT_FOOD_CATEGORY;
import static com.rc.foodsignal.util.AllConstants.SESSION_FOOD_CATEGORY;
import static com.rc.foodsignal.util.AllConstants.SESSION_RESTAURANT_LOGIN_DATA;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class AddRestaurantMenuActivity extends AppCompatActivity {

    String TAG = AppUtils.getTagName(AddRestaurantMenuActivity.class);
    TextView tvTitle;
    ImageView ivBack;
    LinearLayout llDone;
    RestaurantLoginData restaurantLoginData;

    ImageView ivRestaurantMenu;

    // Flow Layout
    GetAllFoodCategory getAllFoodCategory;
    ArrayList<FoodCategory> mCategory = new ArrayList<>();
    List<String> mCategoryKey = new ArrayList<>();
    FlowLayout flowLayout;
    FlowLayoutManager flowLayoutManager;
    TextView tvSelectedFoodCategory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant_menu);

        initUI();
        initActions();
    }

    private void initUI() {
        if (!AppUtils.isNullOrEmpty(SessionManager.getStringSetting(AddRestaurantMenuActivity.this, SESSION_RESTAURANT_LOGIN_DATA))) {
            Log.d(TAG, "Session data: " + SessionManager.getStringSetting(AddRestaurantMenuActivity.this, SESSION_RESTAURANT_LOGIN_DATA));
            restaurantLoginData = RestaurantLoginData.getResponseObject(SessionManager.getStringSetting(AddRestaurantMenuActivity.this, SESSION_RESTAURANT_LOGIN_DATA), RestaurantLoginData.class);
        }

        ivBack = (ImageView) findViewById(R.id.iv_back);
        llDone = (LinearLayout) findViewById(R.id.ll_done);

        tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.setText(getString(R.string.title_activity_add_restaurant_menu));

        ivRestaurantMenu = (ImageView) findViewById(R.id.iv_restaurant_menu);
        Glide
                .with(AddRestaurantMenuActivity.this)
                .asBitmap()
                .load(R.drawable.ic_default_avatar)
                .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
//                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .apply(new RequestOptions().circleCropTransform())
                .into(ivRestaurantMenu);

        flowLayout = (FlowLayout) findViewById(R.id.fl_food_category);
        tvSelectedFoodCategory = (TextView) findViewById(R.id.tv_selected_food_category);

        if (!NetworkManager.isConnected(AddRestaurantMenuActivity.this)) {
            Toast.makeText(AddRestaurantMenuActivity.this, getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();

            setDefaultFoodCategory();

        } else {
            getAllFoodCategory = new GetAllFoodCategory(AddRestaurantMenuActivity.this);
            getAllFoodCategory.execute();
        }
    }

    private void initActions() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        llDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**********************
     * Methods for filter *
     **********************/
    private void initFilter(ArrayList<FoodCategory> foodCategories) {
        mCategory = foodCategories;
        mCategoryKey = getUniqueCategoryKeys(foodCategories);

        if (mCategoryKey.size() > 0) {
            flowLayoutManager = new FlowLayoutManager.FlowViewBuilder(AddRestaurantMenuActivity.this, flowLayout, mCategoryKey, new FlowLayoutManager.onFlowViewClick() {
                @Override
                public void flowViewClick(TextView updatedTextView) {
                    List<TextView> selectedCategory = flowLayoutManager.getSelectedFlowViews();
                    tvSelectedFoodCategory.setText((selectedCategory.size() > 0) ? selectedCategory.get(0).getText().toString() : "");
                }
            })
                    .setSingleChoice(true)
                    .build();
        }
    }

    public List<String> getUniqueCategoryKeys(ArrayList<FoodCategory> foodCategories) {
        List<String> categories = new ArrayList<>();
        for (FoodCategory foodCategory : foodCategories) {
            categories.add(foodCategory.getName());
        }
        Collections.sort(categories);
        return categories;
    }

    private void setDefaultFoodCategory() {
        if (!AllSettingsManager.isNullOrEmpty(SessionManager.getStringSetting(AddRestaurantMenuActivity.this, SESSION_FOOD_CATEGORY))) {
            initFilter(DataFoodCategory.getResponseObject(SessionManager.getStringSetting(AddRestaurantMenuActivity.this, SESSION_FOOD_CATEGORY), DataFoodCategory.class).getData());
        } else {
            initFilter(DataFoodCategory.getResponseObject(DEFAULT_FOOD_CATEGORY, DataFoodCategory.class).getData());
        }
    }

    private class GetAllFoodCategory extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;

        public GetAllFoodCategory(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected HttpRequestManager.HttpResponse doInBackground(String... params) {
            HttpRequestManager.HttpResponse response = HttpRequestManager.doGetRequest(AllUrls.getAllFoodCategoryUrl());
            return response;
        }

        @Override
        protected void onPostExecute(HttpRequestManager.HttpResponse result) {

            if (result.isSuccess() && !AppUtils.isNullOrEmpty(result.getResult().toString())) {
                Log.d(TAG, "success response from web: " + result.getResult().toString());

                ResponseFoodCategory responseFoodCategory = ResponseFoodCategory.getResponseObject(result.getResult().toString(), ResponseFoodCategory.class);

                if (responseFoodCategory.getStatus().equalsIgnoreCase("1") && (responseFoodCategory.getData().size() > 0)) {
                    Log.d(TAG, "success response from web: " + responseFoodCategory.toString());

                    //Save food category into session
                    DataFoodCategory dataFoodCategory = new DataFoodCategory(responseFoodCategory.getData());
                    SessionManager.setStringSetting(AddRestaurantMenuActivity.this, SESSION_FOOD_CATEGORY, DataFoodCategory.getResponseString(dataFoodCategory));

                    initFilter(responseFoodCategory.getData());

                } else {

                    setDefaultFoodCategory();
//                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                }
            } else {

                setDefaultFoodCategory();
//                Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
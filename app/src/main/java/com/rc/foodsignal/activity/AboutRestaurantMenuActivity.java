package com.rc.foodsignal.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.developers.imagezipper.ImageZipper;
import com.nex3z.flowlayout.FlowLayout;
import com.nex3z.flowlayout.FlowLayoutManager;
import com.rc.foodsignal.R;
import com.rc.foodsignal.model.DataFoodCategory;
import com.rc.foodsignal.model.FoodCategory;
import com.rc.foodsignal.model.FoodItem;
import com.rc.foodsignal.model.ResponseFoodCategory;
import com.rc.foodsignal.model.ResponseRestaurantMenu;
import com.rc.foodsignal.model.RestaurantLoginData;
import com.rc.foodsignal.util.AllUrls;
import com.rc.foodsignal.util.AppUtils;
import com.rc.foodsignal.util.HttpRequestManager;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.rc.foodsignal.util.AllConstants.DEFAULT_FOOD_CATEGORY;
import static com.rc.foodsignal.util.AllConstants.INTENT_KEY_FOOD_ITEM;
import static com.rc.foodsignal.util.AllConstants.INTENT_REQUEST_CODE_IMAGE_PICKER;
import static com.rc.foodsignal.util.AllConstants.PREFIX_BASE64_STRING;
import static com.rc.foodsignal.util.AllConstants.SESSION_FOOD_CATEGORY;
import static com.rc.foodsignal.util.AllConstants.SESSION_RESTAURANT_LOGIN_DATA;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class AboutRestaurantMenuActivity extends AppCompatActivity {

    String TAG = AppUtils.getTagName(AboutRestaurantMenuActivity.class);
    TextView tvTitle;
    ImageView ivBack;
    LinearLayout llDone;
    RestaurantLoginData restaurantLoginData;
    ProgressDialog loadingDialog;
    FoodItem foodItem;

    ImageView ivRestaurantMenu;
    EditText edtName, edtPrice, edtIngredient;
    String mBase64 = "";

    // Flow Layout
    GetAllFoodCategory getAllFoodCategory;
    ArrayList<FoodCategory> mCategory = new ArrayList<>();
    List<String> mCategoryKey = new ArrayList<>();
    FlowLayout flowLayout;
    FlowLayoutManager flowLayoutManager;
    TextView tvCategory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_restaurant_menu);

        initUI();
        initActions();
    }

    private void initUI() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        llDone = (LinearLayout) findViewById(R.id.ll_done);
        llDone.setVisibility(View.VISIBLE);
        tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.setText(getString(R.string.title_activity_about_restaurant_menu));

        ivRestaurantMenu = (ImageView) findViewById(R.id.iv_restaurant_menu);
        flowLayout = (FlowLayout) findViewById(R.id.fl_food_category);
        tvCategory = (TextView) findViewById(R.id.tv_selected_food_category);
        edtName = (EditText) findViewById(R.id.edt_name);
        edtName = (EditText) findViewById(R.id.edt_name);
        edtPrice = (EditText) findViewById(R.id.edt_price);
        edtIngredient = (EditText) findViewById(R.id.edt_ingredient);

        if (!AppUtils.isNullOrEmpty(SessionManager.getStringSetting(AboutRestaurantMenuActivity.this, SESSION_RESTAURANT_LOGIN_DATA))) {
            Log.d(TAG, "Session data: " + SessionManager.getStringSetting(AboutRestaurantMenuActivity.this, SESSION_RESTAURANT_LOGIN_DATA));
            restaurantLoginData = RestaurantLoginData.getResponseObject(SessionManager.getStringSetting(AboutRestaurantMenuActivity.this, SESSION_RESTAURANT_LOGIN_DATA), RestaurantLoginData.class);
        }

        // Get current menu data
        Intent intent = getIntent();
        if (intent.getParcelableExtra(INTENT_KEY_FOOD_ITEM) != null) {
            foodItem = intent.getParcelableExtra(INTENT_KEY_FOOD_ITEM);
        }

        if (!NetworkManager.isConnected(AboutRestaurantMenuActivity.this)) {
            setDefaultFoodCategory();

            // Set menu detail
            setMenuDetail(foodItem);
        } else {
            getAllFoodCategory = new GetAllFoodCategory(AboutRestaurantMenuActivity.this);
            getAllFoodCategory.execute();
        }
    }

    private void setMenuDetail(FoodItem mFoodItem) {
        Glide
                .with(AboutRestaurantMenuActivity.this)
                .asBitmap()
                .load((mFoodItem.getImages().size() > 0) ? mFoodItem.getImages().get(0).getImage() : R.drawable.ic_default_restaurant_menu)
                .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
//                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .apply(new RequestOptions().placeholder(R.drawable.ic_default_restaurant_menu))
                .apply(new RequestOptions().error(R.drawable.ic_default_restaurant_menu))
                .apply(new RequestOptions().circleCropTransform())
                .into(ivRestaurantMenu);

        flowLayoutManager.clickFlowView(mFoodItem.getCategory_name());
//            tvCategory.setText(mFoodItem.getCategory_name());
        edtName.setText(mFoodItem.getName());
        edtPrice.setText("$" + mFoodItem.getPrice());
        edtIngredient.setText(mFoodItem.getIngredients());

    }

    private void initActions() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ivRestaurantMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse.from(AboutRestaurantMenuActivity.this)
                        .choose(MimeType.ofImage())
                        .theme(R.style.Matisse_Dracula)
                        .capture(true)
                        .setDefaultCaptureStrategy()
                        .countable(false)
                        .maxSelectable(1)
                        .imageEngine(new GlideEngine())
                        .forResult(INTENT_REQUEST_CODE_IMAGE_PICKER);
            }
        });

        llDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mCategory = tvCategory.getText().toString(),
                        mName = edtName.getText().toString(),
                        mPrice = edtPrice.getText().toString(),
                        mIngredient = edtIngredient.getText().toString();

                if (mCategory.equalsIgnoreCase("")) {
                    Toast.makeText(AboutRestaurantMenuActivity.this, getResources().getString(R.string.toast_empty_category_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mName.equalsIgnoreCase("")) {
                    Toast.makeText(AboutRestaurantMenuActivity.this, getResources().getString(R.string.toast_empty_name_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mPrice.equalsIgnoreCase("")) {
                    Toast.makeText(AboutRestaurantMenuActivity.this, getResources().getString(R.string.toast_empty_price_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mIngredient.equalsIgnoreCase("")) {
                    Toast.makeText(AboutRestaurantMenuActivity.this, getResources().getString(R.string.toast_empty_ingredient_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!NetworkManager.isConnected(AboutRestaurantMenuActivity.this)) {
                    Toast.makeText(AboutRestaurantMenuActivity.this, getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }

                //Select final param for update
//                String finalName = mName.equalsIgnoreCase(foodItem.getName()) ? null : mName;
//                String finalCategory = mCategory.equalsIgnoreCase(foodItem.getCategory_name()) ? null : mCategory;
//                String finalPrice = mPrice.substring(1,mPrice.length()).equalsIgnoreCase(foodItem.getPrice()) ? null : mPrice.substring(1,mPrice.length());
//                String finalRestaurantId = restaurantLoginData.getId().equalsIgnoreCase(foodItem.getRestaurant_id()) ? null : restaurantLoginData.getId();
//                String finalFoodId = mName.equalsIgnoreCase(foodItem.getName()) ? null : mName;
//                String finalName = mName.equalsIgnoreCase(foodItem.getName()) ? null : mName;

                if (mBase64.equalsIgnoreCase("")) {
                    Bitmap bmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_default_restaurant_menu);
                    mBase64 = PREFIX_BASE64_STRING + ImageZipper.getBase64forImage(bmap);
                    Log.d("Default(base64): ", mBase64);
                }

                new DoUpdateFoodItem(AboutRestaurantMenuActivity.this, mName, getFoodCategory(mCategory).getId(), mPrice, restaurantLoginData.getId(), foodItem.getId(), mIngredient, new String[]{mBase64}).execute();
            }
        });
    }

    private class DoUpdateFoodItem extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;
        private String mName = "";
        private String mMenuId = "";
        private String mPrice = "";
        private String mRestaurantId = "";
        private String mFoodId = "";
        private String mIngredients = "";
        private String mImages[] = new String[]{};

        public DoUpdateFoodItem(Context context, String name, String menuId, String price, String restaurantId, String foodId, String ingredients, String images[]) {
            mContext = context;
            mName = name;
            mMenuId = menuId;
            mPrice = price;
            mRestaurantId = restaurantId;
            mFoodId = foodId;
            mIngredients = ingredients;
            mImages = images;
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
            HttpRequestManager.HttpResponse response = HttpRequestManager.doRestPostRequest(AllUrls.getUpdateFoodItemUrl(), AllUrls.getUpdateFoodItemParameters(mName, mMenuId, mPrice, mRestaurantId, mFoodId, mIngredients, mImages), null);
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

                if (responseData.getStatus().equalsIgnoreCase("1") && (responseData.getData().size() > 0)) {
                    Log.d(TAG, "success wrapper: " + responseData.getData().get(0).toString());

                    //Send added menu to the restaurant menu list
//                    Intent intent = new Intent();
//                    intent.putExtra(INTENT_KEY_FOOD_ITEM, responseData.getData().get(0));
//                    setResult(RESULT_OK, intent);
//                    finish();

                } else {
                    Toast.makeText(AboutRestaurantMenuActivity.this, getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(AboutRestaurantMenuActivity.this, getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**********************
     * Methods for filter *
     **********************/
    private void initFilter(ArrayList<FoodCategory> foodCategories) {
        mCategory = foodCategories;
        mCategoryKey = getUniqueCategoryKeys(foodCategories);

        if (mCategoryKey.size() > 0) {
            flowLayoutManager = new FlowLayoutManager.FlowViewBuilder(AboutRestaurantMenuActivity.this, flowLayout, mCategoryKey, new FlowLayoutManager.onFlowViewClick() {
                @Override
                public void flowViewClick(TextView updatedTextView) {
                    List<TextView> selectedCategory = flowLayoutManager.getSelectedFlowViews();
                    tvCategory.setText((selectedCategory.size() > 0) ? selectedCategory.get(0).getText().toString() : "");
                }
            })
                    .setSingleChoice(true)
                    .build();
        }
    }

    private FoodCategory getFoodCategory(String name) {
        if (name != null && mCategory != null && mCategory.size() > 0) {
            for (FoodCategory foodCategory : mCategory) {
                if (foodCategory.getName().equalsIgnoreCase(name)) {
                    return foodCategory;
                }
            }
        }
        return null;
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
        if (!AllSettingsManager.isNullOrEmpty(SessionManager.getStringSetting(AboutRestaurantMenuActivity.this, SESSION_FOOD_CATEGORY))) {
            initFilter(DataFoodCategory.getResponseObject(SessionManager.getStringSetting(AboutRestaurantMenuActivity.this, SESSION_FOOD_CATEGORY), DataFoodCategory.class).getData());
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
                    SessionManager.setStringSetting(AboutRestaurantMenuActivity.this, SESSION_FOOD_CATEGORY, DataFoodCategory.getResponseString(dataFoodCategory));

                    initFilter(responseFoodCategory.getData());

                    // Set menu detail
                    setMenuDetail(foodItem);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_CODE_IMAGE_PICKER && resultCode == RESULT_OK) {
//            mAdapter.setData(Matisse.obtainResult(data), Matisse.obtainPathResult(data));
            List<String> mData = Matisse.obtainPathResult(data);

            if (mData.size() == 1) {
                Log.d("MatisseImage: ", mData.get(0));
                Glide
                        .with(AboutRestaurantMenuActivity.this)
                        .asBitmap()
                        .load(mData.get(0))
                        .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
//                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                        .apply(new RequestOptions().placeholder(R.drawable.ic_default_restaurant_menu))
                        .apply(new RequestOptions().error(R.drawable.ic_default_restaurant_menu))
                        .apply(new RequestOptions().circleCropTransform())
                        .into(ivRestaurantMenu);

                try {
                    File imageZipperFile = new ImageZipper(AboutRestaurantMenuActivity.this)
                            .setQuality(100)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .compressToFile(new File(mData.get(0)));
                    mBase64 = PREFIX_BASE64_STRING + ImageZipper.getBase64forImage(imageZipperFile);
                    Log.d("MatisseImage(base64): ", mBase64);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
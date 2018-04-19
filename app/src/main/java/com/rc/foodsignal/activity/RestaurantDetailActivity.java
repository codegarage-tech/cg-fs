package com.rc.foodsignal.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;
import com.rc.foodsignal.R;
import com.rc.foodsignal.adapter.FoodItemSliderAdapter;
import com.rc.foodsignal.factory.TextViewFactory;
import com.rc.foodsignal.fragment.RestaurantDetailFilterFragment;
import com.rc.foodsignal.model.FoodCategoryDetail;
import com.rc.foodsignal.model.FoodItem;
import com.rc.foodsignal.model.ResponseGcmRestaurantItem;
import com.rc.foodsignal.model.Restaurant;
import com.rc.foodsignal.util.AppUtils;
import com.reversecoder.gcm.model.GcmData;
import com.reversecoder.gcm.util.DetailIntentType;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.util.AllSettingsManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.rc.foodsignal.util.AllConstants.INTENT_KEY_RESTAURANT_ITEM;
import static com.rc.foodsignal.util.AllConstants.INTENT_KEY_RESTAURANT_ITEM_POSITION;
import static com.rc.foodsignal.util.AppUtils.isSimSupport;
import static com.reversecoder.gcm.util.GcmConfig.INTENT_KEY_GCM_DATA_CONTENT;
import static com.reversecoder.gcm.util.GcmConfig.INTENT_KEY_INTENT_DETAIL_TYPE;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class RestaurantDetailActivity extends AppCompatActivity implements AAH_FabulousFragment.Callbacks, AAH_FabulousFragment.AnimationListener {

    //Toolbar
    TextView tvTitle, tvOrderCounter;
    ImageView ivBack;
    RelativeLayout rlSendOrder;
    String TAG = AppUtils.getTagName(RestaurantDetailActivity.class);

    Restaurant mRestaurant;
    int mSelectedPosition = -1;
    DetailIntentType mDetailIntentType = DetailIntentType.OTHER;
    GcmData mGcmData;

    TextSwitcher tsFoodItemName, tsFoodItemIngredient, tsFoodItemPrice, tsFoodItemOffer;
    private int lastPagePosition = 0;
    TextView tvRestaurantEmail, tvRestaurantPhone, tvRestaurantAddress;
    SupportMapFragment mapFragment;

    //Food slider
    RecyclerView rvFoodItemSlider;
    FoodItemSliderAdapter foodItemSliderAdapter;
    CardSliderLayoutManager foodItemSliderLayoutManager;
    String ALL_FOOD_KEY = "All Categories";

    //Fabulous Filter
    FloatingActionButton fabFilter;
    private ArrayMap<String, List<String>> appliedFilters = new ArrayMap<>();
    RestaurantDetailFilterFragment dialogFrag;
    ArrayList<FoodCategoryDetail> mFoodCategory = new ArrayList<>();
    List<String> mFoodCategoryKey = new ArrayList<>();
    String selectedFoodCategory = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        initUI();
        initActions();
    }

    private void initUI() {
        //Toolbar
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.text_title);
        tvOrderCounter = (TextView) findViewById(R.id.tv_order_counter);
        rlSendOrder = (RelativeLayout) findViewById(R.id.rl_send_order);
        fabFilter = (FloatingActionButton) findViewById(R.id.fab_filter);

        //Intent data
        handleNewIntent(getIntent());

        initTextSwitcher();

        tvRestaurantEmail = (TextView) findViewById(R.id.tv_restaurant_email);
        tvRestaurantPhone = (TextView) findViewById(R.id.tv_restaurant_phone);
        tvRestaurantAddress = (TextView) findViewById(R.id.tv_restaurant_address);

        initFoodSlider();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        setData(mRestaurant);

        initFilterFoodCategory(mRestaurant.getFood_category_details());
    }

    private void setData(Restaurant restaurant) {
        if (restaurant != null) {
            Log.d(TAG, "mRestaurant: " + restaurant.toString());

            setFoodItemSliderData(ALL_FOOD_KEY);

            setGoogleMapWithMarker(restaurant);
        }
    }

    private void initFilterFoodCategory(ArrayList<FoodCategoryDetail> foodCategories) {
        mFoodCategory = foodCategories;

        if (mDetailIntentType == DetailIntentType.OTHER) {
            //Below line is for adding one extra filter key
            mFoodCategory.add(new FoodCategoryDetail("420", ALL_FOOD_KEY));
            mFoodCategoryKey = getUniqueFoodCategoryKeys(mFoodCategory);

            //Set all category as default value for the very first time
            selectedFoodCategory = ALL_FOOD_KEY;
            appliedFilters.put("food_category", new ArrayList<String>() {{
                add(ALL_FOOD_KEY);
            }});
        }
    }

    private void setGoogleMapWithMarker(final Restaurant restaurant) {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                try {
                    double lat = Double.parseDouble(restaurant.getLat());
                    double lng = Double.parseDouble(restaurant.getLng());

                    LatLng location = new LatLng(lat, lng);
                    googleMap.addMarker(new MarkerOptions().position(location)
                            .title(restaurant.getAddress()));

                    //Move the camera to the user's location and zoom in!
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16.0f));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void initTextSwitcher() {
        tsFoodItemName = (TextSwitcher) findViewById(R.id.ts_food_item_name);
        tsFoodItemName.setFactory(new TextViewFactory(RestaurantDetailActivity.this, R.style.TextSwitcherTitle, true));

        tsFoodItemPrice = (TextSwitcher) findViewById(R.id.ts_food_item_price);
        tsFoodItemPrice.setFactory(new TextViewFactory(RestaurantDetailActivity.this, R.style.TextSwitcherPrice, true));
        TextView paintText = (TextView) tsFoodItemPrice.getNextView();
        paintText.setPaintFlags(paintText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        tsFoodItemOffer = (TextSwitcher) findViewById(R.id.ts_food_item_offer);
        tsFoodItemOffer.setFactory(new TextViewFactory(RestaurantDetailActivity.this, R.style.TextSwitcherPriceOffer, true));

        tsFoodItemIngredient = (TextSwitcher) findViewById(R.id.ts_food_item_ingredient);
        tsFoodItemIngredient.setFactory(new TextViewFactory(RestaurantDetailActivity.this, R.style.TextSwitcherIngredient, true));
    }

    private void initFoodSlider() {
        rvFoodItemSlider = (RecyclerView) findViewById(R.id.rv_food_item_slider);
        foodItemSliderAdapter = new FoodItemSliderAdapter(RestaurantDetailActivity.this, rlSendOrder, tvOrderCounter);
        rvFoodItemSlider.setAdapter(foodItemSliderAdapter);
        rvFoodItemSlider.setHasFixedSize(true);

        rvFoodItemSlider.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    switchCounter();
                }
            }
        });

        foodItemSliderLayoutManager = new CardSliderLayoutManager(RestaurantDetailActivity.this);
        rvFoodItemSlider.setLayoutManager(foodItemSliderLayoutManager);
        new CardSnapHelper().attachToRecyclerView(rvFoodItemSlider);
    }

    private void setFoodItemSliderData(String foodKey) {
        tvTitle.setText(mRestaurant.getName());
        foodItemSliderAdapter.clear();
        foodItemSliderAdapter.notifyDataSetChanged();
        foodItemSliderAdapter.addAll((mDetailIntentType == DetailIntentType.OTHER) ? (foodKey.equalsIgnoreCase(ALL_FOOD_KEY) ? mRestaurant.getAllFoodItems() : getSelectedFoodCategory(selectedFoodCategory).getMenu_details()) : mRestaurant.getAllOfferFoodItems());
        foodItemSliderAdapter.notifyDataSetChanged();
        lastPagePosition = (mSelectedPosition != -1) ? mSelectedPosition : 0;
        FoodItem foodItem = foodItemSliderAdapter.getItem(lastPagePosition);
        rvFoodItemSlider.scrollToPosition(lastPagePosition);
        tsFoodItemName.setText(foodItem.getName());

        setStrike(foodItem);
        String price = "$" + foodItem.getPrice();
        tsFoodItemPrice.setText(price);

        tsFoodItemOffer.setVisibility(((!AllSettingsManager.isNullOrEmpty(foodItem.getOffer_price())) && (!foodItem.getOffer_price().contains("0.00")) ? View.VISIBLE : View.GONE));
        AppUtils.flashView(tsFoodItemOffer, 1500);
        tsFoodItemOffer.setText("$" + foodItem.getOffer_price());
        tsFoodItemIngredient.setText(foodItem.getIngredients());
        tvRestaurantEmail.setText(mRestaurant.getEmail());
        tvRestaurantPhone.setText(mRestaurant.getPhone());
        tvRestaurantAddress.setText(mRestaurant.getAddress());
    }

    private void setStrike(FoodItem foodItem) {
        TextView paintText = (TextView) tsFoodItemPrice.getNextView();
        if (!AllSettingsManager.isNullOrEmpty(foodItem.getOffer_price()) && Float.parseFloat(foodItem.getOffer_price()) != 0.00) {
            paintText.setPaintFlags(paintText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            paintText.setPaintFlags(paintText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    private void switchCounter() {
        final CardSliderLayoutManager lm = (CardSliderLayoutManager) rvFoodItemSlider.getLayoutManager();
        if (lm.isSmoothScrolling()) {
            return;
        }

        final int currentPosition = lm.getActiveCardPosition();
        if (currentPosition == RecyclerView.NO_POSITION || currentPosition == lastPagePosition) {
            return;
        }

        int animH[] = new int[]{R.anim.slide_in_right, R.anim.slide_out_left};
        int animV[] = new int[]{R.anim.slide_in_top, R.anim.slide_out_bottom};

        final boolean left2right = currentPosition < lastPagePosition;

        if (left2right) {
            animH[0] = R.anim.slide_in_left;
            animH[1] = R.anim.slide_out_right;

            animV[0] = R.anim.slide_in_bottom;
            animV[1] = R.anim.slide_out_top;
        }

        FoodItem foodItem = foodItemSliderAdapter.getItem(currentPosition);

        tsFoodItemName.setInAnimation(RestaurantDetailActivity.this, animH[0]);
        tsFoodItemName.setOutAnimation(RestaurantDetailActivity.this, animH[1]);
        tsFoodItemName.setText(foodItem.getName());

        tsFoodItemPrice.setInAnimation(RestaurantDetailActivity.this, animH[0]);
        tsFoodItemPrice.setOutAnimation(RestaurantDetailActivity.this, animH[1]);

        setStrike(foodItem);
        String price = "$" + foodItem.getPrice();
        tsFoodItemPrice.setText(price);

        tsFoodItemOffer.setInAnimation(RestaurantDetailActivity.this, animH[0]);
        tsFoodItemOffer.setOutAnimation(RestaurantDetailActivity.this, animH[1]);
        tsFoodItemOffer.setText("$" + foodItem.getOffer_price());

        tsFoodItemIngredient.setInAnimation(RestaurantDetailActivity.this, animV[0]);
        tsFoodItemIngredient.setOutAnimation(RestaurantDetailActivity.this, animV[1]);
        tsFoodItemIngredient.setText(foodItem.getIngredients());

        lastPagePosition = currentPosition;
    }

    private void initActions() {
        rlSendOrder.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

            }
        });

        fabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFrag = RestaurantDetailFilterFragment.newInstance(appliedFilters);
                dialogFrag.setParentFab(fabFilter);
                dialogFrag.setCallbacks(RestaurantDetailActivity.this);
                dialogFrag.setAnimationListener(RestaurantDetailActivity.this);

                dialogFrag.show(getSupportFragmentManager(), dialogFrag.getTag());
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvRestaurantEmail.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (!AllSettingsManager.isNullOrEmpty(tvRestaurantEmail.getText().toString().trim())) {
                    /***********************************
                     * Need to check internet connection
                     **********************************/
                    Intent callIntent = new Intent(Intent.ACTION_SENDTO);
                    callIntent.setData(Uri.parse("mailto:" + tvRestaurantEmail.getText().toString().trim()));
                    startActivity(callIntent);
                }
            }
        });

        tvRestaurantPhone.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (!AllSettingsManager.isNullOrEmpty(tvRestaurantPhone.getText().toString().trim())) {
                    if (isSimSupport(RestaurantDetailActivity.this)) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + tvRestaurantPhone.getText().toString().trim()));
                        startActivity(callIntent);
                    } else {
                        Toast.makeText(RestaurantDetailActivity.this, getString(R.string.toast_your_sim_card_is_absent), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void handleNewIntent(Intent intent) {

        if (intent != null && intent.getStringExtra(INTENT_KEY_INTENT_DETAIL_TYPE) != null) {
            mDetailIntentType = DetailIntentType.valueOf(intent.getStringExtra(INTENT_KEY_INTENT_DETAIL_TYPE));

            switch (mDetailIntentType) {
                case OTHER:
                    mRestaurant = intent.getParcelableExtra(INTENT_KEY_RESTAURANT_ITEM);
                    mSelectedPosition = intent.getIntExtra(INTENT_KEY_RESTAURANT_ITEM_POSITION, -1);
                    fabFilter.setVisibility(View.VISIBLE);
                    break;
                case GCM:
                    mGcmData = intent.getParcelableExtra(INTENT_KEY_GCM_DATA_CONTENT);
                    ResponseGcmRestaurantItem responseGcmRestaurantItem = ResponseGcmRestaurantItem.getResponseObject(mGcmData.getMessage(), ResponseGcmRestaurantItem.class);

                    if (responseGcmRestaurantItem.getStatus().equalsIgnoreCase("1")) {
                        mRestaurant = responseGcmRestaurantItem.getData();
                        mSelectedPosition = 0;
                        fabFilter.setVisibility(View.GONE);
                        Log.d(TAG, "Offer data: " + mRestaurant.toString());
                    }
                    break;
            }
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleNewIntent(intent);
        setData(mRestaurant);
    }

    /***************************
     * Fabulous Filter methods *
     ***************************/
    public void onResult(Object result) {
        Log.d(TAG, "onResult: " + result.toString());

        if (result != null) {

            if (result.toString().equalsIgnoreCase("swiped_down")) {
                //do something or nothing
            } else {
                appliedFilters = (ArrayMap<String, List<String>>) result;
                ArrayMap<String, List<String>> appliedFilters = (ArrayMap<String, List<String>>) result;
                if (appliedFilters.size() != 0) {

                    if (appliedFilters.get("food_category") != null) {
                        if (appliedFilters.get("food_category").size() == 1) {
                            selectedFoodCategory = appliedFilters.get("food_category").get(0);
                        } else {
                            selectedFoodCategory = "";
                        }
                    } else {
                        selectedFoodCategory = "";
                    }
                } else {
                    selectedFoodCategory = "";
                }

                mSelectedPosition = 0;
                setFoodItemSliderData(selectedFoodCategory);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (dialogFrag.isAdded()) {
            dialogFrag.dismiss();
            dialogFrag.show(getSupportFragmentManager(), dialogFrag.getTag());
        }
    }

    @Override
    public void onOpenAnimationStart() {
        Log.d("aah_animation", "onOpenAnimationStart: ");
    }

    @Override
    public void onOpenAnimationEnd() {
        Log.d("aah_animation", "onOpenAnimationEnd: ");
    }

    @Override
    public void onCloseAnimationStart() {
        Log.d("aah_animation", "onCloseAnimationStart: ");
    }

    @Override
    public void onCloseAnimationEnd() {
        Log.d("aah_animation", "onCloseAnimationEnd: ");
    }

    public List<String> getUniqueFoodCategoryKeys(ArrayList<FoodCategoryDetail> foodCategories) {
        List<String> categories = new ArrayList<>();
        for (FoodCategoryDetail foodCategory : foodCategories) {
            categories.add(foodCategory.getName());
        }
        Collections.sort(categories);
        return categories;
    }

    public List<String> getFoodCategoryKey() {
        return mFoodCategoryKey;
    }

    public FoodCategoryDetail getSelectedFoodCategory(String foodCategory) {
        for (FoodCategoryDetail category : mFoodCategory) {
            if (category.getName().equalsIgnoreCase(foodCategory)) {
                return category;
            }
        }

        //Selected category 0 is for nothing
        return new FoodCategoryDetail("0", "");
    }
}
package com.rc.foodsignal.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

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
import com.rc.foodsignal.model.FoodItem;
import com.rc.foodsignal.model.Restaurant;
import com.rc.foodsignal.util.AppUtils;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.util.AllSettingsManager;

import java.util.ArrayList;

import static com.rc.foodsignal.util.AllConstants.INTENT_KEY_RESTAURANT_ITEM;
import static com.rc.foodsignal.util.AllConstants.INTENT_KEY_RESTAURANT_ITEM_POSITION;
import static com.rc.foodsignal.util.AppUtils.isSimSupport;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class RestaurantDetailActivity extends AppCompatActivity {

    //Toolbar
    TextView tvTitle;
    ImageView ivBack;
    String TAG = AppUtils.getTagName(RestaurantDetailActivity.class);

    Restaurant mRestaurant;
    int mSelectedPosition = -1;

    TextSwitcher tsFoodItemName, tsFoodItemIngredient, tsFoodItemPrice, tsFoodItemOffer;
    private int lastPagePosition = 0;
    TextView tvRestaurantEmail, tvRestaurantPhone, tvRestaurantAddress;
    SupportMapFragment mapFragment;

    //Food slider
    RecyclerView rvFoodItemSlider;
    FoodItemSliderAdapter foodItemSliderAdapter;
    CardSliderLayoutManager foodItemSliderLayoutManager;

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
        tvTitle.setText(getString(R.string.title_activity_restaurant_detail));

        initTextSwitcher();

        tvRestaurantEmail = (TextView) findViewById(R.id.tv_restaurant_email);
        tvRestaurantPhone = (TextView) findViewById(R.id.tv_restaurant_phone);
        tvRestaurantAddress = (TextView) findViewById(R.id.tv_restaurant_address);

        mRestaurant = getIntent().getParcelableExtra(INTENT_KEY_RESTAURANT_ITEM);
        mSelectedPosition = getIntent().getIntExtra(INTENT_KEY_RESTAURANT_ITEM_POSITION, -1);
        if (mRestaurant != null) {
            Log.d(TAG, "mRestaurant: " + mRestaurant.toString());
            Log.d(TAG, "mSelectedPosition: " + mSelectedPosition);

            initFoodSlider((mRestaurant.getMenu_details().size() > 0) ? mRestaurant.getMenu_details() : new ArrayList<FoodItem>());

            setFoodItemSliderData();

            initGoogleMapWithMarker(mRestaurant);
        }
    }

    private void initGoogleMapWithMarker(final Restaurant restaurant) {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                try {
                    double lat = Double.parseDouble(restaurant.getLat());
                    double lng = Double.parseDouble(restaurant.getLng());

                    LatLng location = new LatLng(lat, lng);
                    googleMap.addMarker(new MarkerOptions().position(location)
                            .title(mRestaurant.getAddress()));

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

        tsFoodItemOffer = (TextSwitcher) findViewById(R.id.ts_food_item_offer);
        tsFoodItemOffer.setFactory(new TextViewFactory(RestaurantDetailActivity.this, R.style.TextSwitcherPrice, true));

        tsFoodItemIngredient = (TextSwitcher) findViewById(R.id.ts_food_item_ingredient);
        tsFoodItemIngredient.setFactory(new TextViewFactory(RestaurantDetailActivity.this, R.style.TextSwitcherIngredient, true));
    }

    private void initFoodSlider(ArrayList<FoodItem> foodItems) {
        rvFoodItemSlider = (RecyclerView) findViewById(R.id.rv_food_item_slider);
        foodItemSliderAdapter = new FoodItemSliderAdapter(RestaurantDetailActivity.this);
        foodItemSliderAdapter.addAll(foodItems);
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

    private void setFoodItemSliderData() {
        //Select food item slider for the first time
        tvTitle.setText(mRestaurant.getName());
        lastPagePosition = (mSelectedPosition != -1) ? mSelectedPosition : 0;
        FoodItem foodItem = foodItemSliderAdapter.getItem(lastPagePosition);
        rvFoodItemSlider.scrollToPosition(lastPagePosition);
        tsFoodItemName.setText(foodItem.getName());
        tsFoodItemPrice.setText("$" + foodItem.getPrice());
        tsFoodItemOffer.setText("$" + foodItem.getPrice());
        tsFoodItemIngredient.setText(foodItem.getIngredients());
        tvRestaurantEmail.setText(mRestaurant.getEmail());
        tvRestaurantPhone.setText(mRestaurant.getPhone());
        tvRestaurantAddress.setText(mRestaurant.getAddress());
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
        tsFoodItemPrice.setText("$" + foodItem.getPrice());

        tsFoodItemOffer.setInAnimation(RestaurantDetailActivity.this, animH[0]);
        tsFoodItemOffer.setOutAnimation(RestaurantDetailActivity.this, animH[1]);
        tsFoodItemOffer.setText("$" + foodItem.getPrice());

        tsFoodItemIngredient.setInAnimation(RestaurantDetailActivity.this, animV[0]);
        tsFoodItemIngredient.setOutAnimation(RestaurantDetailActivity.this, animV[1]);
        tsFoodItemIngredient.setText(foodItem.getIngredients());

        lastPagePosition = currentPosition;
    }

    private void initActions() {
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
}
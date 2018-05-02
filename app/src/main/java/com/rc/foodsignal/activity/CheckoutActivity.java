package com.rc.foodsignal.activity;

import android.content.Intent;
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
import com.rc.foodsignal.adapter.CheckoutMenuListViewAdapter;
import com.rc.foodsignal.interfaces.OnQuantityChangeListener;
import com.rc.foodsignal.model.DataFoodItem;
import com.rc.foodsignal.model.FoodItem;
import com.rc.foodsignal.util.AppUtils;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.util.AllSettingsManager;

import java.util.ArrayList;

import static com.rc.foodsignal.util.AllConstants.INTENT_KEY_CARD_LIST_CHECKOUT_AMOUNT;
import static com.rc.foodsignal.util.AllConstants.INTENT_KEY_CHECKOUT_DATA;
import static com.rc.foodsignal.util.AllConstants.INTENT_KEY_REVIEWED_CHECKOUT_DATA;
import static com.rc.foodsignal.util.AllConstants.INTENT_REQUEST_CODE_CARD_LIST;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class CheckoutActivity extends AppCompatActivity {

    TextView tvTitle;
    ImageView ivBack;
    LinearLayout llDone;

    ListView lvSelectedMenu;
    CheckoutMenuListViewAdapter checkoutMenuListViewAdapter;
    DataFoodItem mDataFoodItem;
    TextView tvSubtotal, tvTotal;
    String TAG = AppUtils.getTagName(CheckoutActivity.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initUI();
        initActions();
    }

    private void initUI() {
        Intent intent = getIntent();
        if (!AllSettingsManager.isNullOrEmpty(intent.getStringExtra(INTENT_KEY_CHECKOUT_DATA))) {
            mDataFoodItem = DataFoodItem.getResponseObject(intent.getStringExtra(INTENT_KEY_CHECKOUT_DATA), DataFoodItem.class);
        }

        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.setText(getString(R.string.title_activity_checkout));
        llDone = (LinearLayout) findViewById(R.id.ll_done);
        llDone.setVisibility(View.VISIBLE);

        tvSubtotal = (TextView) findViewById(R.id.tv_subtotal);
        tvTotal = (TextView) findViewById(R.id.tv_total);
        lvSelectedMenu = (ListView) findViewById(R.id.lv_selected_menu);
        checkoutMenuListViewAdapter = new CheckoutMenuListViewAdapter(CheckoutActivity.this, new OnQuantityChangeListener() {
            @Override
            public void OnQuantityChange() {
                float subTotalPrice = getSubtotalPrice();
                tvSubtotal.setText("$" + subTotalPrice);
                tvTotal.setText("$" + subTotalPrice);
            }
        });
        lvSelectedMenu.setAdapter(checkoutMenuListViewAdapter);
        if (mDataFoodItem != null) {
            ArrayList<FoodItem> foodItems = new ArrayList<>(mDataFoodItem.getData());
            checkoutMenuListViewAdapter.setData(foodItems);
        }

        //set item cost
        float subTotalPrice = getSubtotalPrice();
        tvSubtotal.setText("$" + subTotalPrice);
        tvTotal.setText("$" + subTotalPrice);
    }

    public float getSubtotalPrice() {
        float subTotalPrice = 0.0f;
        try {
            for (int i = 0; i < checkoutMenuListViewAdapter.getCount(); i++) {
                FoodItem mFoodItem = checkoutMenuListViewAdapter.getData().get(i);
                float itemPrice = getProductPrice(mFoodItem);
                Log.d(TAG, "total(itemPrice): " + itemPrice);
                Log.d(TAG, "total(itemQuantity): " + mFoodItem.getQuantity());
                float priceWithQuantity = getProductPrice(mFoodItem) * mFoodItem.getQuantity();
                Log.d(TAG, "total(priceWithQuantity): " + priceWithQuantity);
                subTotalPrice = subTotalPrice + priceWithQuantity;
                Log.d(TAG, "total(subTotalPrice): " + subTotalPrice);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return subTotalPrice;
    }

    public float getProductPrice(FoodItem foodItem) {
        float price = 0.0f;
        if (foodItem != null) {
            try {
                if ((!AllSettingsManager.isNullOrEmpty(foodItem.getOffer_price()) && (Float.parseFloat(foodItem.getOffer_price()) > 0))) {
                    price = Float.parseFloat(foodItem.getOffer_price());
                } else if ((!AllSettingsManager.isNullOrEmpty(foodItem.getPrice()) && (Float.parseFloat(foodItem.getPrice()) > 0))) {
                    price = Float.parseFloat(foodItem.getPrice());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return price;
    }

    private void initActions() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        llDone.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                float total = getSubtotalPrice();
                if (total >= 10) {
                    Intent intentCardList = new Intent(CheckoutActivity.this, CardListActivity.class);
                    intentCardList.putExtra(INTENT_KEY_CARD_LIST_CHECKOUT_AMOUNT, total);
                    startActivityForResult(intentCardList, INTENT_REQUEST_CODE_CARD_LIST);
                } else {
                    Toast.makeText(CheckoutActivity.this, getString(R.string.toast_order_price_must_be_atleast_ten_dollar), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDataFoodItem.getData().size() != checkoutMenuListViewAdapter.getCount()) {
            Log.d(TAG, "Selected item change found");
            DataFoodItem dataFoodItem = new DataFoodItem(checkoutMenuListViewAdapter.getData());
            Intent intent = new Intent();
            intent.putExtra(INTENT_KEY_REVIEWED_CHECKOUT_DATA, DataFoodItem.getResponseString(dataFoodItem));
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Log.d(TAG, "Selected item change not found");
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case INTENT_REQUEST_CODE_CARD_LIST: {
                if (data != null && resultCode == RESULT_OK) {
                    //Order successful, that's why returning to restaurant detail page
                    checkoutMenuListViewAdapter.getData().clear();
                    onBackPressed();
                }
                break;
            }

            default:
                break;
        }
    }
}
package com.rc.foodsignal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rc.foodsignal.R;
import com.rc.foodsignal.adapter.CheckoutMenuListViewAdapter;
import com.rc.foodsignal.model.DataFoodItem;
import com.rc.foodsignal.util.AppUtils;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.util.AllSettingsManager;

import static com.rc.foodsignal.util.AllConstants.INTENT_KEY_CHECKOUT_DATA;

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

        lvSelectedMenu = (ListView) findViewById(R.id.lv_selected_menu);
        checkoutMenuListViewAdapter = new CheckoutMenuListViewAdapter(CheckoutActivity.this);
        lvSelectedMenu.setAdapter(checkoutMenuListViewAdapter);
        if (mDataFoodItem != null) {
            checkoutMenuListViewAdapter.setData(mDataFoodItem.getData());
        }
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
                Intent intentCardList = new Intent(CheckoutActivity.this, CardListActivity.class);
                startActivity(intentCardList);
            }
        });
    }
}
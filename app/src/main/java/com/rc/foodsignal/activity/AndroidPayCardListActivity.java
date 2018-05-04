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
import com.rc.foodsignal.adapter.AndroidPayCardListViewAdapter;
import com.rc.foodsignal.model.realm.RealmController;
import com.rc.foodsignal.model.realm.StripeCard;
import com.rc.foodsignal.util.AppUtils;
import com.reversecoder.library.util.AllSettingsManager;

import java.util.ArrayList;

import static com.rc.foodsignal.util.AllConstants.INTENT_KEY_CARD_ITEM;
import static com.rc.foodsignal.util.AllConstants.INTENT_REQUEST_CODE_ADD_CARD;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AndroidPayCardListActivity extends AppCompatActivity {

    TextView tvTitle;
    ImageView ivBack;
    LinearLayout llAddCard;

    AndroidPayCardListViewAdapter androidPaycardListViewAdapter;
    ListView lvAndroidPayCard;
    RealmController realmController;
    String TAG = AppUtils.getTagName(AndroidPayCardListActivity.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_pay_card_list);

        initUI();
        initActions();
    }

    private void initUI() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        llAddCard = (LinearLayout) findViewById(R.id.ll_done);

        tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.setText(getString(R.string.title_activity_android_pay));

        lvAndroidPayCard = (ListView) findViewById(R.id.lv_android_pay_card);
        androidPaycardListViewAdapter = new AndroidPayCardListViewAdapter(AndroidPayCardListActivity.this);
        lvAndroidPayCard.setAdapter(androidPaycardListViewAdapter);

        //Load data into listview
        realmController = RealmController.with(AndroidPayCardListActivity.this);
        androidPaycardListViewAdapter.setData(new ArrayList<StripeCard>(realmController.getCards()));
    }

    private void initActions() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        llAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddress = new Intent(AndroidPayCardListActivity.this, AddCardActivity.class);
                startActivityForResult(intentAddress, INTENT_REQUEST_CODE_ADD_CARD);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case INTENT_REQUEST_CODE_ADD_CARD: {
                if (data != null && resultCode == RESULT_OK) {
                    if (!AllSettingsManager.isNullOrEmpty(data.getStringExtra(INTENT_KEY_CARD_ITEM))) {
                        if (androidPaycardListViewAdapter != null) {
                            androidPaycardListViewAdapter.setData(new ArrayList<StripeCard>(realmController.getCards()));
                        }
                    }
                }
                break;
            }

            default:
                break;
        }
    }
}
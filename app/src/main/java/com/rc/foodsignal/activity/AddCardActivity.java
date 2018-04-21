package com.rc.foodsignal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rc.foodsignal.R;
import com.rc.foodsignal.model.realm.RealmController;
import com.rc.foodsignal.model.realm.StripeCard;
import com.rc.foodsignal.util.AppUtils;
import com.stripe.android.model.Card;
import com.stripe.android.view.CardMultilineWidget;

import io.realm.RealmObject;

import static com.rc.foodsignal.util.AllConstants.INTENT_KEY_CARD_ITEM;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AddCardActivity extends AppCompatActivity {

    TextView tvTitle;
    ImageView ivBack;
    LinearLayout llAddCard;

    CardMultilineWidget mCardMultilineWidget;
    String TAG = AppUtils.getTagName(AddCardActivity.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        initUI();
        initActions();
    }

    private void initUI() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        llAddCard = (LinearLayout) findViewById(R.id.ll_done);
        llAddCard.setVisibility(View.VISIBLE);
        tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.setText(getString(R.string.title_activity_add_card));

        mCardMultilineWidget = findViewById(R.id.card_multiline_widget);
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
                if (!mCardMultilineWidget.validateAllFields()) {
                    Toast.makeText(AddCardActivity.this, getString(R.string.toast_please_input_valid_card_information), Toast.LENGTH_SHORT).show();
                    return;
                }

                Card card = mCardMultilineWidget.getCard();
                if (card == null) {
                    Toast.makeText(AddCardActivity.this, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                    return;
                }

                //Save card into database
                RealmController realmController = RealmController.with(AddCardActivity.this);
                realmController.setOnRealmDataChangeListener(new RealmController.onRealmDataChangeListener() {
                    @Override
                    public void onInsert(RealmObject realmObject) {
                        Log.d(TAG, "Inserted data: " + ((StripeCard) realmObject).toString());

                        //Send saved card to the card list
                        Intent intent = new Intent();
                        intent.putExtra(INTENT_KEY_CARD_ITEM, ((StripeCard) realmObject).toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onUpdate(RealmObject realmObject) {

                    }

                    @Override
                    public void onDelete(RealmObject realmObject) {

                    }
                });
                realmController.setCard(new StripeCard(card.getNumber(), card.getLast4(), card.getName(), card.getExpMonth(), card.getExpYear(), card.getCVC(), card.getAddressZip()));
            }
        });
    }
}
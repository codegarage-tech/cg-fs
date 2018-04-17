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
import com.rc.foodsignal.util.AppUtils;
import com.stripe.android.model.Card;
import com.stripe.android.view.CardInputListener;
import com.stripe.android.view.CardMultilineWidget;

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

        mCardMultilineWidget.setCardInputListener(new CardInputListener() {
            @Override
            public void onFocusChange(String focusField) {
            }

            @Override
            public void onCardComplete() {
                if (mCardMultilineWidget.validateAllFields()) {
                    llAddCard.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onExpirationComplete() {
                if (mCardMultilineWidget.validateAllFields()) {
                    llAddCard.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCvcComplete() {
                if (mCardMultilineWidget.validateAllFields()) {
                    llAddCard.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPostalCodeComplete() {
                if (mCardMultilineWidget.validateAllFields()) {
                    llAddCard.setVisibility(View.VISIBLE);
                }
            }
        });

        llAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card card = mCardMultilineWidget.getCard();
                if (card == null) {
                    Toast.makeText(AddCardActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Send saved card to the card list
                Log.d(TAG, "Card Item: " + card.getNumber());
                Intent intent = new Intent();
                intent.putExtra(INTENT_KEY_CARD_ITEM, card.toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
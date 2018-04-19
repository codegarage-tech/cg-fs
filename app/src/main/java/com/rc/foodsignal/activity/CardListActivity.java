package com.rc.foodsignal.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rc.foodsignal.R;
import com.rc.foodsignal.adapter.CardListViewAdapter;
import com.rc.foodsignal.model.realm.RealmController;
import com.rc.foodsignal.model.realm.StripeCard;
import com.rc.foodsignal.util.AppUtils;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.model.Charge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.rc.foodsignal.util.AllConstants.INTENT_KEY_CARD_ITEM;
import static com.rc.foodsignal.util.AllConstants.INTENT_REQUEST_CODE_ADD_CARD;
import static com.rc.foodsignal.util.AllConstants.SESSION_SELECTED_CARD;
import static com.rc.foodsignal.util.AllConstants.STRIPE_PUBLISHABLE_KEY;
import static com.rc.foodsignal.util.AllConstants.STRIPE_SECRET_KEY;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class CardListActivity extends AppCompatActivity {

    TextView tvTitle;
    ImageView ivBack;
    LinearLayout llAddCard, llSendOrder;
    ProgressDialog loadingDialog;

    CardListViewAdapter cardListViewAdapter;
    ListView lvCard;
    RealmController realmController;
    String TAG = AppUtils.getTagName(CardListActivity.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        initUI();
        initActions();
    }

    private void initUI() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        llAddCard = (LinearLayout) findViewById(R.id.ll_add_card);
        llSendOrder = (LinearLayout) findViewById(R.id.ll_send_order);

        tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.setText(getString(R.string.title_activity_card_list));

        lvCard = (ListView) findViewById(R.id.lv_card);
        cardListViewAdapter = new CardListViewAdapter(CardListActivity.this);
        lvCard.setAdapter(cardListViewAdapter);

        //Load data into listview
        realmController = RealmController.with(CardListActivity.this);
        cardListViewAdapter.setData(new ArrayList<StripeCard>(realmController.getCards()));
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
                Intent intentAddress = new Intent(CardListActivity.this, AddCardActivity.class);
                startActivityForResult(intentAddress, INTENT_REQUEST_CODE_ADD_CARD);
            }
        });

        llSendOrder.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                submitCardInfo();
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
                        if (cardListViewAdapter != null) {
                            cardListViewAdapter.setData(new ArrayList<StripeCard>(realmController.getCards()));
                        }
                    }
                }
                break;
            }

            default:
                break;
        }
    }

    /******************
     * Stripe methods *
     ******************/
    private void submitCardInfo() {

        if (!NetworkManager.isConnected(CardListActivity.this)) {
            Toast.makeText(CardListActivity.this, getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            return;
        }

        initProgressDialog();

        StripeCard stripeCard = ((StripeCard) StripeCard.getResponseObject(SessionManager.getStringSetting(CardListActivity.this, SESSION_SELECTED_CARD), StripeCard.class));

        if (stripeCard != null) {
            Card card = new Card(stripeCard.getCardNumber(), stripeCard.getCardExpireMonth(), stripeCard.getCardExpireYear(), stripeCard.getCardCvc());

            if (card.validateCard()) {
                Stripe stripe = new Stripe(CardListActivity.this, STRIPE_PUBLISHABLE_KEY);
                stripe.createToken(
                        card,
                        new TokenCallback() {
                            public void onSuccess(Token token) {
                                //Token successfully created.
                                //Create a charge or save token to the server and use it later

                                new doCharge(CardListActivity.this, token, 10).execute();
                            }

                            public void onError(Exception error) {
                                dismissProgressDialog();

                                Toast.makeText(getApplicationContext(), getString(R.string.toast_error_on_creating_token), Toast.LENGTH_LONG).show();
                            }
                        }
                );
            } else {
                dismissProgressDialog();

                Toast.makeText(CardListActivity.this, getString(R.string.toast_invalid_card), Toast.LENGTH_SHORT).show();
            }
        } else {
            dismissProgressDialog();

            Toast.makeText(CardListActivity.this, getString(R.string.toast_please_select_card), Toast.LENGTH_SHORT).show();
        }
    }

    private class doCharge extends AsyncTask<String, Long, Charge> {

        private Context mContext;
        private Token mToken;
        private int mAmount = -1;

        public doCharge(Context context, Token token, int amount) {
            mContext = context;
            mToken = token;
            mAmount = amount;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Charge doInBackground(String... params) {
            try {
                Map<String, Object> chargeParams = new HashMap<String, Object>();
                chargeParams.put("amount", mAmount * 100);
                chargeParams.put("currency", "usd");
                chargeParams.put("description", "Charged $" + mAmount + " from Android");
                chargeParams.put("source", mToken.getId());

                com.stripe.Stripe.apiKey = STRIPE_SECRET_KEY;

                return Charge.create(chargeParams);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Charge charge) {

            dismissProgressDialog();

            if (charge != null && charge.getPaid() && charge.getStatus().equalsIgnoreCase("succeeded")) {
                Toast.makeText(getApplicationContext(), getString(R.string.toast_payment_successfull), Toast.LENGTH_LONG).show();
            } else {

            }
        }
    }

    private void dismissProgressDialog() {
        if (loadingDialog != null
                && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    private void initProgressDialog() {
        loadingDialog = new ProgressDialog(CardListActivity.this);
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
}
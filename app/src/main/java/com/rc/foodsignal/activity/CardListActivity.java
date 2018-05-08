package com.rc.foodsignal.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rc.foodsignal.R;
import com.rc.foodsignal.adapter.CardListViewAdapter;
import com.rc.foodsignal.model.ParamCheckout;
import com.rc.foodsignal.model.ResponseSendOrderNotification;
import com.rc.foodsignal.model.realm.RealmController;
import com.rc.foodsignal.model.realm.StripeCard;
import com.rc.foodsignal.util.AllUrls;
import com.rc.foodsignal.util.AppUtils;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.httprequest.HttpRequestManager;
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
import static com.rc.foodsignal.util.AllConstants.INTENT_KEY_CARD_LIST_CHECKOUT_DATA;
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

    LinearLayout llDeliveryInfo;
    EditText edtReceiverName, edtReceiverAddress, edtReceiverEmailAddress;
    CardListViewAdapter cardListViewAdapter;
    ListView lvCard;
    RealmController realmController;
    ParamCheckout paramCheckout;
    String TAG = AppUtils.getTagName(CardListActivity.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        initUI();
        initActions();
    }

    private void initUI() {
        Intent intent = getIntent();
        String data = intent.getStringExtra(INTENT_KEY_CARD_LIST_CHECKOUT_DATA);
        if (!AllSettingsManager.isNullOrEmpty(data)) {
            paramCheckout = ParamCheckout.getResponseObject(data, ParamCheckout.class);
        }

        ivBack = (ImageView) findViewById(R.id.iv_back);
        llAddCard = (LinearLayout) findViewById(R.id.ll_add_card);
        llSendOrder = (LinearLayout) findViewById(R.id.ll_send_order);

        tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.setText(getString(R.string.title_activity_card_list));

        edtReceiverName = (EditText) findViewById(R.id.edt_receiver_name);
        edtReceiverAddress = (EditText) findViewById(R.id.edt_receiver_address);
        edtReceiverEmailAddress = (EditText) findViewById(R.id.edt_receiver_email);
        lvCard = (ListView) findViewById(R.id.lv_card);
        cardListViewAdapter = new CardListViewAdapter(CardListActivity.this);
        lvCard.setAdapter(cardListViewAdapter);

        //Load data into listview
        realmController = RealmController.with(CardListActivity.this);
        cardListViewAdapter.setData(new ArrayList<StripeCard>(realmController.getCards()));

        //Set delivery info panel
        llDeliveryInfo = (LinearLayout) findViewById(R.id.ll_delivery_info);
        if (AllSettingsManager.isNullOrEmpty(SessionManager.getStringSetting(CardListActivity.this, SESSION_SELECTED_CARD))) {
            llDeliveryInfo.setVisibility(View.GONE);
        } else {
            llDeliveryInfo.setVisibility(View.VISIBLE);
        }
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

                            if (AllSettingsManager.isNullOrEmpty(SessionManager.getStringSetting(CardListActivity.this, SESSION_SELECTED_CARD))) {
                                llDeliveryInfo.setVisibility(View.GONE);
                            } else {
                                llDeliveryInfo.setVisibility(View.VISIBLE);
                            }
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
        if (AllSettingsManager.isNullOrEmpty(SessionManager.getStringSetting(CardListActivity.this, SESSION_SELECTED_CARD))) {
            Toast.makeText(CardListActivity.this, getResources().getString(R.string.toast_please_add_atleast_one_card), Toast.LENGTH_SHORT).show();
            return;
        }
        String mReceiverName = edtReceiverName.getText().toString(), mReceiverAddress = edtReceiverAddress.getText().toString(), mReceiverEmailAddress = edtReceiverEmailAddress.getText().toString();
        if (AllSettingsManager.isNullOrEmpty(mReceiverName)) {
            Toast.makeText(CardListActivity.this, getString(R.string.toast_please_input_receiver_name), Toast.LENGTH_SHORT).show();
            return;
        }
        if (AllSettingsManager.isNullOrEmpty(mReceiverAddress)) {
            Toast.makeText(CardListActivity.this, getString(R.string.toast_please_input_receiver_address), Toast.LENGTH_SHORT).show();
            return;
        }
        if (AllSettingsManager.isNullOrEmpty(mReceiverEmailAddress)) {
            Toast.makeText(CardListActivity.this, getString(R.string.toast_please_input_receiver_email), Toast.LENGTH_SHORT).show();
            return;
        }

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

                                new doCharge(CardListActivity.this, token, paramCheckout.getTotal_amount()).execute();
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
        private float mAmount = 0.0f;

        public doCharge(Context context, Token token, float amount) {
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
                int amount = (int) (mAmount * 100);
                Log.d(TAG, "ChargeAmount: " + amount + " cent");
                Log.d(TAG, "ChargeAmount: $" + (int) mAmount);

                Map<String, Object> chargeParams = new HashMap<String, Object>();
                chargeParams.put("amount", amount);
                chargeParams.put("currency", "usd");
                chargeParams.put("description", "Charged $" + (int) mAmount + " from Android");
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
            if (charge != null && charge.getPaid() && charge.getStatus().equalsIgnoreCase("succeeded")) {
                Log.d(TAG, "ChargeInfo: " + charge.toString());
//                Toast.makeText(getApplicationContext(), getString(R.string.toast_payment_is_successful), Toast.LENGTH_LONG).show();

                new DoOrderTask(mContext, paramCheckout).execute();
            } else {
                dismissProgressDialog();
                Toast.makeText(getApplicationContext(), getString(R.string.toast_payment_is_not_successful), Toast.LENGTH_LONG).show();
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

    private class DoOrderTask extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;
        private ParamCheckout mParamCheckout;

        public DoOrderTask(Context context, ParamCheckout paramCheckout) {
            mContext = context;
            mParamCheckout = paramCheckout;
        }

        @Override
        protected HttpRequestManager.HttpResponse doInBackground(String... params) {
            HttpRequestManager.HttpResponse response = HttpRequestManager.doRestPostRequest(AllUrls.getSendOrderUrl(), AllUrls.getSendOrderParam(mParamCheckout), null);
            return response;
        }

        @Override
        protected void onPostExecute(HttpRequestManager.HttpResponse result) {
            dismissProgressDialog();

            if (result != null && result.isSuccess() && !AppUtils.isNullOrEmpty(result.getResult().toString())) {
                Log.d(TAG, "success response from web: " + result.getResult().toString());
                ResponseSendOrderNotification responseData = ResponseSendOrderNotification.getResponseObject(result.getResult().toString(), ResponseSendOrderNotification.class);
                Log.d(TAG, "success response from object: " + responseData.toString());
//
                if (responseData.getStatus().equalsIgnoreCase("1") && (responseData.getData() != null)) {
                    Log.d(TAG, "success wrapper: " + responseData.getData().toString());
                    Toast.makeText(CardListActivity.this, getString(R.string.toast_payment_is_successful) + "\n" + responseData.getMsg(), Toast.LENGTH_SHORT).show();

                    //Send send successful payment status to checkout activity
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(CardListActivity.this, getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CardListActivity.this, getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
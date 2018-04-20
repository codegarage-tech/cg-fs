package com.rc.foodsignal.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rc.foodsignal.R;
import com.rc.foodsignal.util.AppUtils;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class CheckoutActivity extends AppCompatActivity {

    TextView tvTitle;
    ImageView ivBack;
    LinearLayout llDone;

    ListView lvSelectedMenu;
    String TAG = AppUtils.getTagName(CheckoutActivity.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initUI();
        initActions();
    }

    private void initUI() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.setText(getString(R.string.title_activity_checkout));
        llDone = (LinearLayout) findViewById(R.id.ll_done);
        llDone.setVisibility(View.VISIBLE);

        lvSelectedMenu = (ListView) findViewById(R.id.lv_selected_menu);
    }

    private void initActions() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
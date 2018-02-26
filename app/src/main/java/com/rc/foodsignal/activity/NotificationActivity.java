package com.rc.foodsignal.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rc.foodsignal.R;
import com.rc.foodsignal.util.AppUtils;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class NotificationActivity extends AppCompatActivity {

    TextView tvTitle;
    ImageView ivBack;
    String TAG = AppUtils.getTagName(NotificationActivity.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initUI();
        initActions();
    }

    private void initUI() {
        ivBack = (ImageView) findViewById(R.id.iv_back);

        tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.setText(getString(R.string.title_activity_notification));
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

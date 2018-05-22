package com.rc.foodsignal.dialog;

import android.app.Activity;

import com.rc.foodsignal.R;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class DeleteRestaurantMenuDialog extends BaseAlertDialog {

    private Activity mActivity;
    public String mTitle;
    private OnClickListener mOnClickListener;

    public DeleteRestaurantMenuDialog(Activity context, OnClickListener onClickListener) {
        super(context);
        mActivity = context;
        mOnClickListener = onClickListener;
    }

    @Override
    public Builder createView() {
        Builder builder = this.createView(R.string.dialog_delete_menu_title, 0, R.string.dialog_btn_cancel, R.string.dialog_btn_ok, mOnClickListener);

        return builder;
    }
}

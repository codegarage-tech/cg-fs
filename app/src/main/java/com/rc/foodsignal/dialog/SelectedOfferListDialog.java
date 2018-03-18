package com.rc.foodsignal.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.BaseEffects;
import com.rc.foodsignal.R;
import com.rc.foodsignal.model.FoodItem;

import java.util.ArrayList;

/**
 * Author: Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SelectedOfferListDialog extends Dialog {

    private DialogActionListener dialogActionListener;
    private ArrayList<FoodItem> mFoodItems = new ArrayList<>();
    Context mContext;

    public SelectedOfferListDialog(Context context, ArrayList<FoodItem> foodItems, DialogActionListener dialogActionListener) {
        super(context);
        this.mContext = context;
        this.dialogActionListener = dialogActionListener;
        this.mFoodItems = foodItems;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setBackgroundColor(
                getContext().getResources().getColor(android.R.color.transparent));
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        getWindow().setLayout(metrics.widthPixels * 90 / 100, LayoutParams.WRAP_CONTENT);

        View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_selected_offer_list, null);
        setContentView(dialogLayout);

        BaseEffects animator= Effectstype.Shake.getAnimator();
        animator.start(dialogLayout);

        setCancelable(true);
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialogActionListener.onCancelButtonClick();
            }
        });

        ListView lvSelectedOffer = (ListView) dialogLayout.findViewById(R.id.lv_selected_offer);
        SelectedOfferListAdapter selectedOfferListAdapter = new SelectedOfferListAdapter(mContext, mFoodItems, new DialogActionListener() {
            @Override
            public void onOkButtonClick() {
            }

            @Override
            public void onCancelButtonClick() {
                dismiss();
            }

            @Override
            public void onDeleteButtonClick(FoodItem foodItem) {
                if (foodItem != null) {
                    dialogActionListener.onDeleteButtonClick(foodItem);
                }
            }
        });
        lvSelectedOffer.setAdapter(selectedOfferListAdapter);

        Button btnOk = (Button) dialogLayout.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogActionListener.onOkButtonClick();
                dismiss();
            }
        });

        Button btnCancel = (Button) dialogLayout.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogActionListener.onCancelButtonClick();
                dismiss();
            }
        });
    }

    public interface DialogActionListener {
        public void onOkButtonClick();

        public void onCancelButtonClick();

        public void onDeleteButtonClick(FoodItem foodItem);
    }

    private class SelectedOfferListAdapter extends BaseAdapter {

        private Context mActivity;
        private ArrayList<FoodItem> mData = new ArrayList<>();
        private LayoutInflater inflater = null;
        private String TAG = SelectedOfferListAdapter.class.getSimpleName();
        private DialogActionListener mDialogActionListener;

        public SelectedOfferListAdapter(Context activity, ArrayList<FoodItem> data, DialogActionListener dialogActionListener) {
            mActivity = activity;
            mData = data;
            mDialogActionListener = dialogActionListener;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public ArrayList<FoodItem> getData() {
            return mData;
        }

        public void setData(ArrayList<FoodItem> data) {
            mData = data;
            notifyDataSetChanged();
        }

        public void addData(FoodItem foodItem) {
            if (getCount() > 0) {
                mData.add(foodItem);
                notifyDataSetChanged();
            }
        }

        public void updateData(FoodItem foodItem) {
            if (getCount() > 0) {
                int itemPosition = getItemPosition(foodItem);
                mData.remove(itemPosition);
                mData.add(foodItem);
                notifyDataSetChanged();
            }
        }

        public int getItemPosition(FoodItem foodItem) {
            for (int i = 0; i < mData.size(); i++) {
                if ((mData.get(i)).getId().contains(foodItem.getId())) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public FoodItem getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            long id = 0;
            try {

                id = (long) Integer.parseInt(mData.get(position).getId());
            } catch (Exception ex) {
                ex.printStackTrace();
                id = 0;
            }
            return id;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {

            View vi = convertView;
            if (convertView == null)
                vi = inflater.inflate(R.layout.dialog_list_row_selected_offer, null);

            FoodItem foodItem = getItem(position);

            TextView tvFoodOfferName = (TextView) vi.findViewById(R.id.tv_food_offer_name);
            TextView tvFoodOfferPercentage = (TextView) vi.findViewById(R.id.tv_food_offer_percentage);
            ImageView ivFoodOfferClose = (ImageView) vi.findViewById(R.id.iv_food_offer_close);

            tvFoodOfferName.setText(foodItem.getName());
            tvFoodOfferPercentage.setText(String.format("%02d%%", foodItem.getOfferPercentage()));
            ivFoodOfferClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialogActionListener.onDeleteButtonClick(mData.remove(position));
                    notifyDataSetChanged();
                    if(mData.size()==0){
                        mDialogActionListener.onCancelButtonClick();
                    }
                }
            });

            return vi;
        }
    }
}

package com.rc.foodsignal.adapter;


import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.rc.foodsignal.activity.RestaurantDetailActivity;
import com.rc.foodsignal.animation.flytocart.CircleAnimationUtil;
import com.rc.foodsignal.model.FoodItem;
import com.rc.foodsignal.viewholder.FoodItemViewHolder;
import com.steelkiwi.library.IncrementProductView;

import java.security.InvalidParameterException;
import java.util.ArrayList;

public class FoodItemSliderAdapter extends RecyclerArrayAdapter<FoodItem> {

    public static final transient int TYPE_FOOD = 1;
    private View mDestinationView;
    private TextView tvOrderCounter;
    private ArrayList<FoodItem> mSelectedData;

    public FoodItemSliderAdapter(Context context, View destinationView, TextView orderCounterView) {
        super(context);

        mDestinationView = destinationView;
        tvOrderCounter = orderCounterView;
        mSelectedData = new ArrayList<FoodItem>();
    }

    @Override
    public int getViewType(int position) {
        return TYPE_FOOD;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_FOOD:
                return new FoodItemViewHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }

    /*************************
     * Fly to cart animation *
     *************************/
    private void makeFlyAnimation(Activity activity, final View sourceView, final View sourceViewCopy, final View destinationView, final TextView counterView, final int newCounter) {

        new CircleAnimationUtil().attachActivity(activity)
                .setTargetView(sourceView)
                .setTargetViewCopy(sourceViewCopy)
                .setMoveDuration(1000)
                .setDestView(destinationView)
                .setAnimationListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        counterView.setText(newCounter + "");
                        resetCounterView();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).startAnimation();
    }

    public void resetCounterView() {
        if (mSelectedData.size() > 0) {
            tvOrderCounter.setText(mSelectedData.size() + "");
            tvOrderCounter.setVisibility(View.VISIBLE);
        } else {
            tvOrderCounter.setVisibility(View.GONE);
        }
    }

    private void addDataToCart(FoodItem foodItem, IncrementProductView incrementProductView, IncrementProductView incrementProductViewCopy) {
        // New food item instance is created for avoiding logic due runtime changes
        FoodItem mFoodItem = new FoodItem(true, foodItem.getOfferPercentage(), foodItem.getId(),
                foodItem.getName(), foodItem.getMenu_id(), foodItem.getMenu_image(), foodItem.getPrice(),
                foodItem.getRestaurant_id(), foodItem.getIngredients(), foodItem.getCategory_name(),
                foodItem.getImages(), foodItem.getOffer_title(), foodItem.getOffer_price());
        mSelectedData.add(mFoodItem);
        makeFlyAnimation(((RestaurantDetailActivity)getContext()), incrementProductView, incrementProductViewCopy, mDestinationView, tvOrderCounter, mSelectedData.size());
    }
}

package com.rc.foodsignal.adapter;


import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.rc.foodsignal.model.FoodItem;
import com.rc.foodsignal.viewholder.FoodItemViewHolder;

import java.security.InvalidParameterException;

public class FoodItemSliderAdapter extends RecyclerArrayAdapter<FoodItem> {

    public static final transient int TYPE_FOOD = 1;

    public FoodItemSliderAdapter(Context context) {
        super(context);
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
}

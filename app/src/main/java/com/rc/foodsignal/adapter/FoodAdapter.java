package com.rc.foodsignal.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.rc.foodsignal.model.Food;
import com.rc.foodsignal.viewholder.FoodViewHolder;

import java.security.InvalidParameterException;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class FoodAdapter extends RecyclerArrayAdapter<Food> {

    public static final int TYPE_FOOD = 1;

    public FoodAdapter(Context context) {
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
                return new FoodViewHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }
}
package com.rc.foodsignal.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.rc.foodsignal.model.Restaurant;
import com.rc.foodsignal.viewholder.RestaurantViewHolder;

import java.security.InvalidParameterException;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class RestaurantAdapter extends RecyclerArrayAdapter<Restaurant> {

    public static final int TYPE_FOOD = 1;

    public RestaurantAdapter(Context context) {
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
                return new RestaurantViewHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }
}
package com.rc.foodsignal.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.rc.foodsignal.model.Restaurant;
import com.rc.foodsignal.viewholder.ImageViewHolder;
import com.rc.foodsignal.viewholder.RestaurantViewHolder;

import java.security.InvalidParameterException;

import static com.rc.foodsignal.model.Restaurant.TYPE_IMAGE;
import static com.rc.foodsignal.model.Restaurant.TYPE_RESTAURANT;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class RestaurantAdapter extends RecyclerArrayAdapter<Restaurant> {

    public RestaurantAdapter(Context context) {
        super(context);
    }

    @Override
    public int getViewType(int position) {
        return getAllData().get(position).getTypeRestaurant();
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_RESTAURANT:
                return new RestaurantViewHolder(parent);
            case TYPE_IMAGE:
                return new ImageViewHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }
}
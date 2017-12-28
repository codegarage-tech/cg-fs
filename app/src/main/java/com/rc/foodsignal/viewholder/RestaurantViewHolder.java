package com.rc.foodsignal.viewholder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.rc.foodsignal.R;
import com.rc.foodsignal.model.Restaurant;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class RestaurantViewHolder extends BaseViewHolder<Restaurant> {

    private ImageView ivFoodImage;
    private TextView tvFoodPrice;
    private TextView tvRestaurantName;
    private TextView tvRestaurantAddress;
    private TextView tvRestaurantReview;

    public RestaurantViewHolder(ViewGroup parent) {
        super(parent, R.layout.recyclerview_item_food);

        ivFoodImage = $(R.id.iv_food_image);
        tvFoodPrice = $(R.id.tv_food_price);
        tvRestaurantName = $(R.id.tv_restaurant_name);
        tvRestaurantAddress = $(R.id.tv_restaurant_address);
        tvRestaurantReview = $(R.id.tv_restaurant_review);
    }

    @Override
    public void setData(final Restaurant data) {

        Glide
                .with(getContext())
                .load((data.getItem_details().size() > 0) ? data.getItem_details().get(0).getImage() : data.getImage())
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .apply(new RequestOptions().centerInside())
                .into(ivFoodImage);

        tvFoodPrice.setText((data.getItem_details().size() > 0) ? ("$" + data.getItem_details().get(0).getPrice()) : "");
        tvRestaurantName.setText(data.getName());
        tvRestaurantAddress.setText("Address: " + data.getAddress());
        tvRestaurantReview.setText("Review: " + "---");
    }
}

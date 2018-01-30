package com.rc.foodsignal.viewholder;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
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
    private LinearLayout llPriceLabel;

    public RestaurantViewHolder(ViewGroup parent) {
        super(parent, R.layout.recyclerview_item_restaurant);

        ivFoodImage = $(R.id.iv_food_image);
        tvFoodPrice = $(R.id.tv_food_price);
        tvRestaurantName = $(R.id.tv_restaurant_name);
        tvRestaurantAddress = $(R.id.tv_restaurant_address);
        tvRestaurantReview = $(R.id.tv_restaurant_review);
        llPriceLabel = $(R.id.ll_price_label);
    }

    @Override
    public void setData(final Restaurant data) {

//        llPriceLabel.setVisibility((data.getMenu_details().size() > 0) ? View.VISIBLE : View.GONE);

//        Log.d("setData",(data.getMenu_details().size() > 0) ? data.getMenu_details().get(0).getImage() : data.getImage());
        Glide
                .with(getContext())
                .asBitmap()
//                .load((data.getMenu_details().size() > 0) ? data.getMenu_details().get(0).getImage() : data.getImage())
                .load(data.getImage())
//                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                .into(ivFoodImage);

//        tvFoodPrice.setText((data.getMenu_details().size() > 0) ? ("$" + data.getMenu_details().get(0).getPrice()) : "");
        tvRestaurantName.setText(data.getName());
        tvRestaurantAddress.setText("Address: " + data.getAddress());
        tvRestaurantReview.setText("Review: " + "---");
    }
}

package com.rc.foodsignal.viewholder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.rc.foodsignal.R;
import com.rc.foodsignal.model.Food;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class FoodViewHolder extends BaseViewHolder<Food> {

    private ImageView ivFoodImage;
    private TextView tvFoodPrice;
    private TextView tvFoodName;

    public FoodViewHolder(ViewGroup parent) {
        super(parent, R.layout.recyclerview_item_food);

        ivFoodImage = $(R.id.iv_food_image);
        tvFoodPrice = $(R.id.tv_food_price);
        tvFoodName = $(R.id.tv_food_name);
    }

    @Override
    public void setData(final Food data) {

        Glide
                .with(getContext())
                .load(data.getImage())
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .apply(new RequestOptions().centerInside())
                .into(ivFoodImage);

        tvFoodPrice.setText("$"+data.getPrice());
        tvFoodName.setText(data.getName());
    }
}

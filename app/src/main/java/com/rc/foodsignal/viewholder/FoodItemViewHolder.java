package com.rc.foodsignal.viewholder;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.rc.foodsignal.R;
import com.rc.foodsignal.model.FoodItem;
import com.reversecoder.library.random.RandomManager;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class FoodItemViewHolder extends BaseViewHolder<FoodItem> {

    private ImageView ivFoodImage;

    public FoodItemViewHolder(ViewGroup parent) {
        super(parent, R.layout.card_slider_row_food_item);

        ivFoodImage = $(R.id.iv_food_image);
    }

    @Override
    public void setData(final FoodItem data) {

        Glide
                .with(getContext())
                .asBitmap()
                .load((data.getImages().size() > 0) ? data.getImages().get(RandomManager.getRandom((data.getImages().size() - 1))).getImage() : "")
                .apply(new RequestOptions().error(R.drawable.ic_default_food))
                .into(ivFoodImage);
    }
}

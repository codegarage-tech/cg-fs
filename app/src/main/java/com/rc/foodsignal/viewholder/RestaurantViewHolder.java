package com.rc.foodsignal.viewholder;

import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.rc.foodsignal.R;
import com.rc.foodsignal.adapter.RestaurantMenuPagerAdapter;
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
    private ViewPager vpRestaurantMenu;

    public RestaurantViewHolder(ViewGroup parent) {
        super(parent, R.layout.recyclerview_item_restaurant);

        vpRestaurantMenu = (ViewPager) $(R.id.vp_restaurant_menu);
//        ivFoodImage = $(R.id.iv_food_image);
//        tvFoodPrice = $(R.id.tv_food_price);
//        tvRestaurantName = $(R.id.tv_restaurant_name);
//        tvRestaurantAddress = $(R.id.tv_restaurant_address);
//        tvRestaurantReview = $(R.id.tv_restaurant_review);
//        llPriceLabel = $(R.id.ll_price_label);
    }

    @Override
    public void setData(final Restaurant data) {

        RestaurantMenuPagerAdapter restaurantMenuPagerAdapter = new RestaurantMenuPagerAdapter(getContext(), data);
        vpRestaurantMenu.setAdapter(restaurantMenuPagerAdapter);


//        llPriceLabel.setVisibility((data.getMenu_details().size() > 0) ? View.VISIBLE : View.GONE);
//
//        Log.d("setData",(data.getMenu_details().size() > 0) ? ((data.getMenu_details().get(0).getImages().size() > 0) ? data.getMenu_details().get(0).getImages().get(0).getImage() : "") : data.getImage());
//        Glide
//                .with(getContext())
//                .asBitmap()
//                .load((data.getMenu_details().size() > 0) ? ((data.getMenu_details().get(0).getImages().size() > 0) ? data.getMenu_details().get(0).getImages().get(0).getImage() : R.drawable.ic_default_food) : data.getImage())
////                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
//                .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
//                .into(ivFoodImage);
//
//        tvFoodPrice.setText((data.getMenu_details().size() > 0) ? ("$" + data.getMenu_details().get(0).getPrice()) : "");
//        tvRestaurantName.setText(data.getName());
//        tvRestaurantAddress.setText("Address: " + data.getAddress());
//        tvRestaurantReview.setText("Review: " + "---");
    }
}

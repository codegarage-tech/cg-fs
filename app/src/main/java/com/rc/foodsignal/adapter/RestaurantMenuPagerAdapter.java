package com.rc.foodsignal.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rc.foodsignal.R;
import com.rc.foodsignal.interfaces.OnPagerItemClickListener;
import com.rc.foodsignal.model.FoodItem;
import com.rc.foodsignal.model.Restaurant;
import com.rc.foodsignal.util.AppUtils;
import com.reversecoder.library.random.RandomManager;
import com.reversecoder.library.util.AllSettingsManager;

import java.util.List;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class RestaurantMenuPagerAdapter extends PagerAdapter {

    private static final String TAG = RestaurantMenuPagerAdapter.class.getSimpleName();
    private Context mContext;
    private Restaurant mRestaurant;
    private final List<FoodItem> mItems;
    private LayoutInflater mLayoutInflater;
    private OnPagerItemClickListener mOnItemClickListener;

    public RestaurantMenuPagerAdapter(@NonNull Context context, @NonNull Restaurant restaurant) {
        super();
        mContext = context;
        mRestaurant = restaurant;
        mItems = mRestaurant.getAllFoodItems();
    }

    @Override
    public int getCount() {
        return null == mItems ? 0 : mItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = mLayoutInflater.inflate(R.layout.viewpager_item_restaurant_menu, container, false);

        //Initialize view
        ImageView ivFoodImage = (ImageView) itemView.findViewById(R.id.iv_food_image);
        TextView tvFoodPrice = itemView.findViewById(R.id.tv_food_price);
        TextView tvFoodOfferPrice = itemView.findViewById(R.id.tv_food_offer_price);
        TextView tvFoodName = itemView.findViewById(R.id.tv_food_name);
        TextView tvFoodIngredient = itemView.findViewById(R.id.tv_food_ingredient);
        TextView tvRestaurantName = itemView.findViewById(R.id.tv_restaurant_name);
        TextView tvRestaurantAddress = itemView.findViewById(R.id.tv_restaurant_address);
//        LinearLayout llPriceLabel = itemView.findViewById(R.id.ll_price_label);

        Glide
                .with(mContext)
                .asBitmap()
                .load((mItems.get(position).getImages().size() > 0) ? mItems.get(position).getImages().get(RandomManager.getRandom((mItems.get(position).getImages().size() - 1))).getImage() : mRestaurant.getImage())
//                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
//                .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
//                .apply(new RequestOptions().placeholder(R.drawable.ic_default_food))
                .apply(new RequestOptions().error(R.drawable.ic_default_food))
                .into(ivFoodImage);
//        llPriceLabel.setVisibility((mItems.size() > 0) ? View.VISIBLE : View.GONE);
        tvFoodOfferPrice.setVisibility((mItems.size() > 0) ? ((!AllSettingsManager.isNullOrEmpty(mItems.get(position).getOffer_price()) && Float.parseFloat(mItems.get(position).getOffer_price()) != 0.00) ? View.VISIBLE : View.INVISIBLE) : View.INVISIBLE);
        tvFoodOfferPrice.setText((mItems.size() > 0) ? ((!AllSettingsManager.isNullOrEmpty(mItems.get(position).getOffer_price())) ? ("$" + mItems.get(position).getOffer_price()) : "") : "");
        tvFoodPrice.setText((mItems.size() > 0) ? "$" + mItems.get(position).getPrice() : "");
        if (mItems.size() > 0 && !AllSettingsManager.isNullOrEmpty(mItems.get(position).getOffer_price()) && Float.parseFloat(mItems.get(position).getOffer_price()) != 0.00) {
            tvFoodPrice.setPaintFlags(tvFoodPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvFoodPrice.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
        } else {
            tvFoodPrice.setPaintFlags(tvFoodPrice.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            tvFoodPrice.setTextColor(mContext.getResources().getColor(R.color.black));
        }
        tvFoodName.setText((mItems.size() > 0) ? mItems.get(position).getName() : "");
        tvFoodIngredient.setText("Ingredient: " + ((mItems.size() > 0) ? mItems.get(position).getIngredients() : ""));
        tvRestaurantName.setText("Restaurant: " + mRestaurant.getName());
        tvRestaurantAddress.setText("Address: " + AppUtils.formatLocationInfo(mRestaurant.getAddress()));

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnItemClickListener) {
                    mOnItemClickListener.onItemClicked(position);
                }
            }
        });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    public void setOnItemClickListener(OnPagerItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
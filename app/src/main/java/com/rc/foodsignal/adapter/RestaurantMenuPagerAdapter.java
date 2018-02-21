package com.rc.foodsignal.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.rc.foodsignal.R;
import com.rc.foodsignal.interfaces.OnPagerItemClickListener;
import com.rc.foodsignal.model.FoodItem;
import com.rc.foodsignal.model.Restaurant;

import java.util.List;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class RestaurantMenuPagerAdapter extends PagerAdapter {

    private static final String TAG = RestaurantMenuPagerAdapter.class.getSimpleName();
    private Context mContext;
    private Restaurant mRestaurant;
    private final List<FoodItem> mItems;
    private final LayoutInflater mLayoutInflater;
    private OnPagerItemClickListener mOnItemClickListener;

    public RestaurantMenuPagerAdapter(@NonNull Context context, @NonNull Restaurant restaurant) {
        super();
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRestaurant = restaurant;
        mItems = restaurant.getMenu_details();
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
        View itemView = mLayoutInflater.inflate(R.layout.viewpager_item_restaurant_menu, container, false);

        ImageView ivFoodImage = (ImageView) itemView.findViewById(R.id.iv_food_image);
        Glide
                .with(mContext)
                .asBitmap()
                .load((mItems.size() > 0) ? ((mItems.get(0).getImages().size() > 0) ? mItems.get(0).getImages().get(0).getImage() : R.drawable.ic_default_food) : mRestaurant.getImage())
//                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                .into(ivFoodImage);

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
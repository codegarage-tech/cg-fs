package com.rc.foodsignal.viewholder;

import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.rc.foodsignal.R;
import com.rc.foodsignal.adapter.RestaurantMenuPagerAdapter;
import com.rc.foodsignal.model.Restaurant;
import com.shuhart.bubblepagerindicator.BubblePageIndicator;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class RestaurantViewHolder extends BaseViewHolder<Restaurant> {

    private ViewPager vpRestaurantMenu;
    private BubblePageIndicator bpiFoodItem;
    private RestaurantMenuPagerAdapter restaurantMenuPagerAdapter;

    public RestaurantViewHolder(ViewGroup parent) {
        super(parent, R.layout.recyclerview_item_restaurant);

        vpRestaurantMenu = (ViewPager) $(R.id.vp_restaurant_menu);
        bpiFoodItem = (BubblePageIndicator) $(R.id.bubble_pager_indicator_food_item);
    }

    @Override
    public void setData(final Restaurant data) {

        restaurantMenuPagerAdapter = new RestaurantMenuPagerAdapter(getContext(), data);
        vpRestaurantMenu.setAdapter(restaurantMenuPagerAdapter);
        vpRestaurantMenu.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                addBottomDots(position, ((TextTypeViewHolder) holder).ll_dots);
//                page = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bpiFoodItem.setViewPager(vpRestaurantMenu);
        bpiFoodItem.setFillColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        bpiFoodItem.setPageColor(getContext().getResources().getColor(R.color.white));
    }
}

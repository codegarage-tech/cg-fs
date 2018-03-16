package com.rc.foodsignal.adapter;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.github.florent37.expansionpanel.ExpansionHeader;
import com.github.florent37.expansionpanel.ExpansionLayout;
import com.github.florent37.expansionpanel.OnExpansionListener;
import com.rc.foodsignal.R;
import com.rc.foodsignal.activity.AboutRestaurantMenuActivity;
import com.rc.foodsignal.animation.flytocart.CircleAnimationUtil;
import com.rc.foodsignal.model.FoodItem;
import com.steelkiwi.library.IncrementProductView;
import com.steelkiwi.library.listener.OnStateListener;

import java.util.ArrayList;

import static com.rc.foodsignal.util.AllConstants.INTENT_KEY_FOOD_ITEM;
import static com.rc.foodsignal.util.AllConstants.INTENT_REQUEST_CODE_RESTAURANT_MENU_DETAIL;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class RestaurantMenuListViewAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<FoodItem> mData;
    private static LayoutInflater inflater = null;
    private String TAG = RestaurantMenuListViewAdapter.class.getSimpleName();
    private View mDestinationView;
    private TextView tvOfferCounter;
    private ArrayList<FoodItem> mSelectedData;

    public RestaurantMenuListViewAdapter(Activity activity, View destinationView, TextView offerCounterView) {
        mActivity = activity;
        mDestinationView = destinationView;
        tvOfferCounter = offerCounterView;
        mData = new ArrayList<FoodItem>();
        mSelectedData = new ArrayList<FoodItem>();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<FoodItem> getData() {
        return mData;
    }

    public void setData(ArrayList<FoodItem> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void addData(FoodItem foodItem) {
        if (getCount() > 0) {
            mData.add(foodItem);
            notifyDataSetChanged();
        }
    }

    public void updateData(FoodItem foodItem) {
        if (getCount() > 0) {
            int itemPosition = getItemPosition(foodItem);
            mData.remove(itemPosition);
            mData.add(foodItem);
            notifyDataSetChanged();
        }
    }

    public int getItemPosition(FoodItem foodItem) {
        for (int i = 0; i < mData.size(); i++) {
            if ((mData.get(i)).getId().contains(foodItem.getId())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public FoodItem getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        long id = 0;
        try {

            id = (long) Integer.parseInt(mData.get(position).getId());
        } catch (Exception ex) {
            ex.printStackTrace();
            id = 0;
        }
        return id;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_row_restaurant_menu, null);

        final FoodItem foodItem = getItem(position);

        ImageView ivMenu = (ImageView) vi.findViewById(R.id.iv_menu);
        Glide
                .with(mActivity)
                .asBitmap()
                .load((foodItem.getImages().size() > 0) ? foodItem.getImages().get(0).getImage() : R.drawable.ic_default_restaurant_menu)
                .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
//                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .apply(new RequestOptions().placeholder(R.drawable.ic_default_restaurant_menu))
                .apply(new RequestOptions().error(R.drawable.ic_default_restaurant_menu))
                .apply(new RequestOptions().circleCropTransform())
                .into(ivMenu);

        TextView tvMenuName = (TextView) vi.findViewById(R.id.tv_menu_name);
        tvMenuName.setText(foodItem.getName());

        TextView tvMenuPrice = (TextView) vi.findViewById(R.id.tv_menu_price);
        tvMenuPrice.setText("Price: " + "$" + foodItem.getPrice());

        final TextView tvMenuIngredient = (TextView) vi.findViewById(R.id.tv_menu_ingredient);
        tvMenuIngredient.setText("Ingredient: " + foodItem.getIngredients());

        //Expansion panel
        ExpansionHeader expansionHeader = (ExpansionHeader) vi.findViewById(R.id.expansion_header);
        ExpansionLayout expansionLayout = (ExpansionLayout) vi.findViewById(R.id.expansion_layout);
        if (foodItem.isExpanded()) {
            if (!expansionLayout.isExpanded()) {
                expansionLayout.toggle(true);
            }
        } else {
            if (expansionLayout.isExpanded()) {
                expansionLayout.toggle(true);
            }
        }
        expansionHeader.setOnExpansionListener(new OnExpansionListener() {
            @Override
            public void onExpanded(boolean isExpanded) {
                foodItem.setExpanded(isExpanded);
//                updateData(foodItem);
            }
        });
        vi.findViewById(R.id.header_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMenuDetail = new Intent(mActivity, AboutRestaurantMenuActivity.class);
                intentMenuDetail.putExtra(INTENT_KEY_FOOD_ITEM, foodItem);
                mActivity.startActivityForResult(intentMenuDetail, INTENT_REQUEST_CODE_RESTAURANT_MENU_DETAIL);
            }
        });

        //Increment product view
        final IncrementProductView incrementProductView = (IncrementProductView) expansionLayout.findViewById(R.id.increment_product_view);
        final IncrementProductView incrementProductViewCopy = (IncrementProductView) expansionLayout.findViewById(R.id.increment_product_view_copy);

        //Set selected Value into increment product view
        if (foodItem.getOfferPercentage() > 0) {
            incrementProductView.setBoardCount(foodItem.getOfferPercentage());
        }
        incrementProductView.setOnStateListener(new OnStateListener() {
            @Override
            public void onCountChange(int count) {
            }

            @Override
            public void onConfirm(int count) {
                Log.d(TAG, "onConfirm: " + count);

                foodItem.setOfferPercentage(count);
            }

            @Override
            public void onClose() {
                Log.d(TAG, "onClose");

                if (foodItem.getOfferPercentage() > 0) {
                    // if View is not destroyed in listview it will work fine otherwise you have to set it after initializing view
                    incrementProductView.setBoardCount(foodItem.getOfferPercentage());

                    Log.d(TAG, "Valid item is chose");
                    if (!mSelectedData.contains(foodItem)) {
                        Log.d(TAG, "Adding new item into cart");
                        addDataToCart(foodItem, incrementProductView, incrementProductViewCopy);
                    } else {
                        Log.d(TAG, "Data already exist");
                        int position = mSelectedData.indexOf(foodItem);
                        Log.d(TAG, "mSelectedData position: " + position);

                        if (mSelectedData.get(position).getOfferPercentage() != foodItem.getOfferPercentage()) {
                            Log.d(TAG, "Updating existing data");
                            mSelectedData.remove(position);
                            addDataToCart(foodItem, incrementProductView, incrementProductViewCopy);
                        } else {
                            Log.d(TAG, "No update found");
                        }
                    }
                } else {
                    Log.d(TAG, "No item is chose");
                    if (mSelectedData.contains(foodItem)) {
                        Log.d(TAG, "Selected item is unselected");
                        int position = mSelectedData.indexOf(foodItem);
                        mSelectedData.remove(position);
                        resetCounterView();
                        Log.d(TAG, "Selected item is removed and reset");
                    }
                }
            }
        });

        return vi;
    }

    private void addDataToCart(FoodItem foodItem, IncrementProductView incrementProductView, IncrementProductView incrementProductViewCopy) {
        // New food item instance is created for avoiding logic due runtime changes
        FoodItem mFoodItem = new FoodItem(foodItem.getOfferPercentage(), foodItem.getId(),
                foodItem.getName(), foodItem.getMenu_id(), foodItem.getMenu_image(), foodItem.getPrice(),
                foodItem.getRestaurant_id(), foodItem.getIngredients(), foodItem.getCategory_name(),
                foodItem.getImages(), foodItem.getOffer_title(), foodItem.getOffer_price());
        mSelectedData.add(mFoodItem);
        makeFlyAnimation(mActivity, incrementProductView, incrementProductViewCopy, mDestinationView, tvOfferCounter, mSelectedData.size());
    }

    private void resetCounterView() {
        if (mSelectedData.size() > 0) {
            tvOfferCounter.setVisibility(View.VISIBLE);
        } else {
            tvOfferCounter.setVisibility(View.GONE);
        }
    }

    /*************************
     * Fly to cart animation *
     *************************/
    private void makeFlyAnimation(Activity activity, final View sourceView, final View sourceViewCopy, final View destinationView, final TextView counterView, final int newCounter) {

        new CircleAnimationUtil().attachActivity(activity)
                .setTargetView(sourceView)
                .setTargetViewCopy(sourceViewCopy)
                .setMoveDuration(1000)
                .setDestView(destinationView)
                .setAnimationListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        counterView.setText(newCounter + "");
                        resetCounterView();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).startAnimation();
    }
}
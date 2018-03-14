package com.rc.foodsignal.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.rc.foodsignal.model.FoodItem;

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

    public RestaurantMenuListViewAdapter(Activity activity) {
        mActivity = activity;
        mData = new ArrayList<FoodItem>();
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
    public View getView(final int position, View convertView, final ViewGroup parent) {

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

        TextView tvMenuIngredient = (TextView) vi.findViewById(R.id.tv_menu_ingredient);
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

        return vi;
    }
}
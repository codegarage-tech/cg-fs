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
import com.rc.foodsignal.R;
import com.rc.foodsignal.activity.RestaurantDetailActivity;
import com.rc.foodsignal.model.Notification;
import com.rc.foodsignal.model.ResponseGcmRestaurantItem;
import com.reversecoder.gcm.model.GcmData;
import com.reversecoder.gcm.util.DetailIntentType;
import com.reversecoder.library.event.OnSingleClickListener;

import java.util.ArrayList;

import static com.reversecoder.gcm.util.GcmConfig.INTENT_KEY_APP_USER_INTENT_DETAIL_TYPE;
import static com.reversecoder.gcm.util.GcmConfig.INTENT_KEY_GCM_APP_USER_DATA_CONTENT;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class NotificationListViewAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<Notification> mData = new ArrayList<>();
    private static LayoutInflater inflater = null;
    private String TAG = NotificationListViewAdapter.class.getSimpleName();

    public NotificationListViewAdapter(Activity activity) {
        mActivity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<Notification> getData() {
        return mData;
    }

    public void setData(ArrayList<Notification> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void addData(Notification notification) {
        if (getCount() > 0) {
            mData.add(notification);
            notifyDataSetChanged();
        }
    }

    public void updateData(Notification notification) {
        if (getCount() > 0) {
            int itemPosition = getItemPosition(notification);
            mData.remove(itemPosition);
            mData.add(notification);
            notifyDataSetChanged();
        }
    }

    public int getItemPosition(Notification notification) {
        for (int i = 0; i < mData.size(); i++) {
            if ((mData.get(i)).getId().contains(notification.getId())) {
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
    public Notification getItem(int position) {
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
            vi = inflater.inflate(R.layout.list_row_notification, null);

        final Notification notification = getItem(position);

        ImageView ivRestaurant = (ImageView) vi.findViewById(R.id.iv_restaurant);
        Glide
                .with(mActivity)
                .asBitmap()
                .load(notification.getDetails().getImage())
                .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
//                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .apply(new RequestOptions().placeholder(R.drawable.ic_default_restaurant))
                .apply(new RequestOptions().error(R.drawable.ic_default_restaurant))
//                .apply(new RequestOptions().circleCropTransform())
                .into(ivRestaurant);

        TextView tvOfferTitle = (TextView) vi.findViewById(R.id.tv_offer_title);
        tvOfferTitle.setText(notification.getNotification());

        TextView tvRestaurantName = (TextView) vi.findViewById(R.id.tv_restaurant_name);
        tvRestaurantName.setText(notification.getDetails().getName());

        TextView tvRestaurantAddress = (TextView) vi.findViewById(R.id.tv_restaurant_address);
        tvRestaurantAddress.setText(notification.getDetails().getAddress());

        vi.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                ResponseGcmRestaurantItem responseGcmRestaurantItem = new ResponseGcmRestaurantItem("1", notification.getDetails());
                GcmData gcmData = new GcmData(notification.getNotification(), mActivity.getString(com.reversecoder.gcm.R.string.txt_tap_for_more_details), ResponseGcmRestaurantItem.getResponseString(responseGcmRestaurantItem), "", "", "", "");

                Intent intentRestaurantDetail = new Intent(mActivity, RestaurantDetailActivity.class);
                intentRestaurantDetail.putExtra(INTENT_KEY_APP_USER_INTENT_DETAIL_TYPE, DetailIntentType.GCM.name());
                intentRestaurantDetail.putExtra(INTENT_KEY_GCM_APP_USER_DATA_CONTENT, gcmData);
                mActivity.startActivity(intentRestaurantDetail);
            }
        });

        return vi;
    }
}
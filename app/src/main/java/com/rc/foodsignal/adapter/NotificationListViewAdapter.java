package com.rc.foodsignal.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rc.foodsignal.R;
import com.rc.foodsignal.model.Notification;

import java.util.ArrayList;

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

        TextView tvOfferTitle = (TextView) vi.findViewById(R.id.tv_offer_title);
        tvOfferTitle.setText(notification.getNotification());

        TextView tvRestaurantName = (TextView) vi.findViewById(R.id.tv_restaurant_name);
//        tvRestaurantName.setText(notification.getNotification());

        return vi;
    }
}
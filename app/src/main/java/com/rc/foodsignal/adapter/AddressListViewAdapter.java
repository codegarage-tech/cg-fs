package com.rc.foodsignal.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rc.foodsignal.R;
import com.rc.foodsignal.model.Location;
import com.reversecoder.library.storage.SessionManager;

import java.util.ArrayList;

import static com.rc.foodsignal.util.AllConstants.SESSION_IS_LOCATION_ADDED;
import static com.rc.foodsignal.util.AllConstants.SESSION_SELECTED_LOCATION;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class AddressListViewAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<Location> mData;
    private static LayoutInflater inflater = null;
    private String TAG = AddressListViewAdapter.class.getSimpleName();
    Location mLocation;

    public AddressListViewAdapter(Activity activity) {
        mActivity = activity;
        mData = new ArrayList<Location>();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mLocation = Location.getResponseObject(SessionManager.getStringSetting(mActivity, SESSION_SELECTED_LOCATION), Location.class);
    }

    public ArrayList<Location> getData() {
        return mData;
    }

    public void setData(ArrayList<Location> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void addData(Location location) {
        mData.add(location);

        //For refreshing last added data as selected
        mLocation = Location.getResponseObject(SessionManager.getStringSetting(mActivity, SESSION_SELECTED_LOCATION), Location.class);
        notifyDataSetChanged();
    }

    public int getItemPosition(Location music) {
        for (int i = 0; i < mData.size(); i++) {
            if ((mData.get(i)).getStreet().contains(music.getStreet())) {
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
    public Location getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_row_address, null);

        final Location location = getItem(position);

        TextView tvStreet = (TextView) vi.findViewById(R.id.tv_street);
        tvStreet.setText(location.getStreet());
        TextView tvAddress = (TextView) vi.findViewById(R.id.tv_address);
        tvAddress.setText(String.format("%s, %s, %s", location.getCity(), location.getState(), location.getCountry()));

        LinearLayout llAddressCheck = (LinearLayout) vi.findViewById(R.id.ll_address_check);
        if (mLocation.getStreet().equalsIgnoreCase(location.getStreet())) {
            llAddressCheck.setVisibility(View.VISIBLE);

            tvStreet.setTextColor(mActivity.getResources().getColor(R.color.colorPrimaryDark));
            tvAddress.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
        } else {
            llAddressCheck.setVisibility(View.GONE);

            tvStreet.setTextColor(mActivity.getResources().getColor(R.color.textColorPrimary));
            tvAddress.setTextColor(mActivity.getResources().getColor(R.color.textColorSecondary));
        }

        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocation = location;
                SessionManager.setStringSetting(mActivity, SESSION_SELECTED_LOCATION, mLocation.toString());
                SessionManager.setBooleanSetting(mActivity, SESSION_IS_LOCATION_ADDED, true);
                notifyDataSetChanged();
            }
        });

        return vi;
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d(TAG, "onActivityResult");
//
//        switch (requestCode) {
//            case REQUEST_CODE_PAYPAL: {
//                if (resultCode == RESULT_OK) {
//                    Music music = data.getParcelableExtra(INTENT_KEY_PAYPAL_UPDATE_MUSIC_ITEM);
//                    Log.d(TAG, "updated music: " + music.toString());
//
//                    updateMusic(music);
//                }
//                break;
//            }
//        }
//    }
}
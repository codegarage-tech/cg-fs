package com.rc.foodsignal.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rc.foodsignal.R;
import com.rc.foodsignal.model.Location;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class AddressListViewAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<Location> mData;
    private static LayoutInflater inflater = null;
    private String TAG = AddressListViewAdapter.class.getSimpleName();

    public AddressListViewAdapter(Activity activity) {
        mActivity = activity;
        mData = new ArrayList<Location>();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        final Location mLocation = getItem(position);

        TextView tvStreet = (TextView) vi.findViewById(R.id.tv_street);
        tvStreet.setText(mLocation.getStreet());
        TextView tvAddress = (TextView) vi.findViewById(R.id.tv_address);
        tvAddress.setText(String.format("%s, %s, %s", mLocation.getCity(), mLocation.getState(), mLocation.getCountry()));

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
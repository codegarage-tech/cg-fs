package com.rc.foodsignal.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.rc.foodsignal.R;
import com.rc.foodsignal.animation.Techniques;
import com.rc.foodsignal.animation.YoYo;
import com.rc.foodsignal.model.Location;
import com.rc.foodsignal.model.ResponseDeleteLocation;
import com.rc.foodsignal.util.AllUrls;
import com.rc.foodsignal.util.AppUtils;
import com.rc.foodsignal.util.HttpRequestManager;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;

import java.util.ArrayList;

import static com.rc.foodsignal.util.AllConstants.SESSION_IS_LOCATION_ADDED;
import static com.rc.foodsignal.util.AllConstants.SESSION_SELECTED_LOCATION;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AddressListViewAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<Location> mData;
    private static LayoutInflater inflater = null;
    private String TAG = AddressListViewAdapter.class.getSimpleName();
    private Location mLocation;
    private ProgressDialog loadingDialog;

    public AddressListViewAdapter(Activity activity) {
        mActivity = activity;
        mData = new ArrayList<Location>();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mLocation = Location.getResponseObject(SessionManager.getStringSetting(mActivity, SESSION_SELECTED_LOCATION), Location.class);
//        setMode(Attributes.Mode.Single);
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

    public void removeData(Location location){
        int position = getItemPosition(location);
        mData.remove(position);
        notifyDataSetChanged();
    }

    public int getItemPosition(Location location) {
        for (int i = 0; i < mData.size(); i++) {
            if ((mData.get(i)).getStreet().contains(location.getStreet())) {
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
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_row_address, null);

        final SwipeLayout swipeLayout = (SwipeLayout) vi.findViewById(R.id.swipe);
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.iv_trash));
            }
        });

        final Location location = getItem(position);

        TextView tvStreet = (TextView) vi.findViewById(R.id.tv_street);
        tvStreet.setText(location.getStreet());
        TextView tvAddress = (TextView) vi.findViewById(R.id.tv_address);
        tvAddress.setText(AppUtils.formatLocationInfo(String.format("%s, %s, %s", location.getCity(), location.getState(), location.getCountry())));

        LinearLayout llAddressCheck = (LinearLayout) vi.findViewById(R.id.ll_address_check);
        if (mLocation.getId().equalsIgnoreCase(location.getId())) {
            llAddressCheck.setVisibility(View.VISIBLE);

            tvStreet.setTextColor(mActivity.getResources().getColor(R.color.colorPrimaryDark));
            tvAddress.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
        } else {
            llAddressCheck.setVisibility(View.GONE);

            tvStreet.setTextColor(mActivity.getResources().getColor(R.color.textColorPrimary));
            tvAddress.setTextColor(mActivity.getResources().getColor(R.color.textColorSecondary));
        }

        vi.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLayout.close(true);
                if (mLocation.getId().equalsIgnoreCase(location.getId())) {
                    Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_you_can_not_delete_the_selected_item), Toast.LENGTH_SHORT).show();
                } else {
                    if (!NetworkManager.isConnected(mActivity)) {
                        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    new DeleteLocationTask(mActivity, location).execute();
                }
            }
        });

        vi.findViewById(R.id.ll_item_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocation = location;
                SessionManager.setStringSetting(mActivity, SESSION_SELECTED_LOCATION, mLocation.toString());
                SessionManager.setBooleanSetting(mActivity, SESSION_IS_LOCATION_ADDED, true);
                notifyDataSetChanged();
            }
        });

        vi.findViewById(R.id.ll_item_view).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                swipeLayout.open(true);
                return false;
            }
        });

        return vi;
    }

    private class DeleteLocationTask extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;
        private Location mLocation;

        public DeleteLocationTask(Context context, Location location) {
            this.mContext = context;
            this.mLocation = location;
        }

        @Override
        protected void onPreExecute() {
            loadingDialog = new ProgressDialog(mContext);
            loadingDialog.setMessage(mContext.getResources().getString(R.string.txt_loading));
            loadingDialog.setIndeterminate(false);
            loadingDialog.setCancelable(true);
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.show();
            loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    if (loadingDialog != null
                            && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                }
            });
        }

        @Override
        protected HttpRequestManager.HttpResponse doInBackground(String... params) {
            HttpRequestManager.HttpResponse response = HttpRequestManager.doGetRequest(AllUrls.getDeleteLocationUrl(mLocation.getId()));
            return response;
        }

        @Override
        protected void onPostExecute(HttpRequestManager.HttpResponse result) {

            if (loadingDialog != null
                    && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }

            if (result != null && result.isSuccess() && !AppUtils.isNullOrEmpty(result.getResult().toString())) {
                Log.d(TAG, "success response from web: " + result.getResult().toString());
                ResponseDeleteLocation responseData = ResponseDeleteLocation.getResponseObject(result.getResult().toString(), ResponseDeleteLocation.class);

                if (responseData.getStatus().equalsIgnoreCase("success")) {
                    Log.d(TAG, "success wrapper: " + responseData.toString());
                    Toast.makeText(mContext, responseData.getMsg(), Toast.LENGTH_SHORT).show();

                    //Update listview
                    removeData(mLocation);
                } else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
            }
        }
    }

//    @Override
//    public int getSwipeLayoutResourceId(int position) {
//        return R.id.swipe;
//    }
//
//    @Override
//    public void fillValues(int position, View convertView) {
//
//    }
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
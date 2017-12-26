package com.rc.foodsignal.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rc.foodsignal.R;
import com.rc.foodsignal.adapter.RestaurantAdapter;
import com.rc.foodsignal.interfaces.OnFragmentBackPressedListener;
import com.rc.foodsignal.model.Location;
import com.rc.foodsignal.model.ResponseFoodItem;
import com.rc.foodsignal.util.AllUrls;
import com.rc.foodsignal.util.AppUtils;
import com.rc.foodsignal.util.HttpRequestManager;
import com.reversecoder.library.storage.SessionManager;

import static com.rc.foodsignal.util.AllConstants.SESSION_SELECTED_LOCATION;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class HomeFragment extends Fragment implements OnFragmentBackPressedListener {

    String TAG = AppUtils.getTagName(HomeFragment.class);
    private View parentView;
    DoSearchFood doSearchFood;
    Location mLocation;
    RecyclerView recyclerViewFood;
    RestaurantAdapter restaurantAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_home, container, false);

        initHomeFragmentViews();

        return parentView;
    }

    private void initHomeFragmentViews() {
        recyclerViewFood = (RecyclerView) parentView.findViewById(R.id.rv_food);
        restaurantAdapter = new RestaurantAdapter(getActivity());
        recyclerViewFood.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewFood.setAdapter(restaurantAdapter);

        if (!AppUtils.isNullOrEmpty(SessionManager.getStringSetting(getActivity(), SESSION_SELECTED_LOCATION))) {
            Log.d(TAG, "Session data: " + SessionManager.getStringSetting(getActivity(), SESSION_SELECTED_LOCATION));
            mLocation = Location.getResponseObject(SessionManager.getStringSetting(getActivity(), SESSION_SELECTED_LOCATION), Location.class);
        }
        doSearchFood = new DoSearchFood(getActivity(), Double.parseDouble(mLocation.getLat()), Double.parseDouble(mLocation.getLng()));
        doSearchFood.execute();
    }

    @Override
    public void onFragmentBackPressed() {
        getActivity().finish();
    }

    private class DoSearchFood extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;
        private double mLat = 0.00;
        private double mLng = 0.00;

        public DoSearchFood(Context context, double lat, double lng) {
            this.mContext = context;
            this.mLat = lat;
            this.mLng = lng;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected HttpRequestManager.HttpResponse doInBackground(String... params) {
            HttpRequestManager.HttpResponse response = HttpRequestManager.doRestPostRequest(AllUrls.getAllRestaurantsUrl(), AllUrls.getAllRestaurantsParameters(mLat, mLng), null);
            return response;
        }

        @Override
        protected void onPostExecute(HttpRequestManager.HttpResponse result) {

            if (result.isSuccess() && !AppUtils.isNullOrEmpty(result.getResult().toString())) {
                Log.d(TAG, "success response from web: " + result.getResult().toString());
                ResponseFoodItem responseData = ResponseFoodItem.getResponseObject(result.getResult().toString(), ResponseFoodItem.class);

                if (responseData.getStatus().equalsIgnoreCase("1") && (responseData.getData().size() > 0)) {
                    Log.d(TAG, "success wrapper: " + responseData.toString());

                    restaurantAdapter.addAll(responseData.getData());
                    restaurantAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
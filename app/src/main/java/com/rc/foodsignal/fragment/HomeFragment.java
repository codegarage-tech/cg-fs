package com.rc.foodsignal.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment.AnimationListener;
import com.allattentionhere.fabulousfilter.AAH_FabulousFragment.Callbacks;
import com.rc.foodsignal.R;
import com.rc.foodsignal.activity.HomeActivity;
import com.rc.foodsignal.adapter.RestaurantAdapter;
import com.rc.foodsignal.interfaces.OnFragmentBackPressedListener;
import com.rc.foodsignal.model.FoodCategory;
import com.rc.foodsignal.model.Location;
import com.rc.foodsignal.model.ResponseRestaurantItem;
import com.rc.foodsignal.model.Restaurant;
import com.rc.foodsignal.util.AllUrls;
import com.rc.foodsignal.util.AppUtils;
import com.rc.foodsignal.util.HttpRequestManager;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;

import java.util.ArrayList;
import java.util.List;

import static com.rc.foodsignal.util.AllConstants.SESSION_SELECTED_LOCATION;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class HomeFragment extends Fragment implements OnFragmentBackPressedListener {

    String TAG = AppUtils.getTagName(HomeFragment.class);
    private View parentView;
    DoSearchRestaurant doSearchRestaurant;
    Location mLocation;
    RecyclerView recyclerViewFood;
    RestaurantAdapter restaurantAdapter;

    //Fabulous Filter
    private ArrayMap<String, List<String>> appliedFilters = new ArrayMap<>();
    FilterFragment dialogFrag;
    FloatingActionButton fabFilter;
    List<Restaurant> mList = new ArrayList<>();
    List<String> mCategoryKey = new ArrayList<>();
    ArrayList<FoodCategory> mCategory = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_home, container, false);

        initHomeFragmentViews();
        initHomeFragmentActions();

        return parentView;
    }

    private void initHomeFragmentViews() {
        recyclerViewFood = (RecyclerView) parentView.findViewById(R.id.rv_food);
        fabFilter = (FloatingActionButton) parentView.findViewById(R.id.fab_filter);

        ((HomeActivity) getActivity()).setToolbarTitle(getString(R.string.title_fragment_home));

        restaurantAdapter = new RestaurantAdapter(getActivity());
        recyclerViewFood.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewFood.setAdapter(restaurantAdapter);

        if (!AppUtils.isNullOrEmpty(SessionManager.getStringSetting(getActivity(), SESSION_SELECTED_LOCATION))) {
            Log.d(TAG, "Session data: " + SessionManager.getStringSetting(getActivity(), SESSION_SELECTED_LOCATION));
            mLocation = Location.getResponseObject(SessionManager.getStringSetting(getActivity(), SESSION_SELECTED_LOCATION), Location.class);
        }

        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
        } else {
            doSearchRestaurant = new DoSearchRestaurant(getActivity(), Double.parseDouble(mLocation.getLat()), Double.parseDouble(mLocation.getLng()));
            doSearchRestaurant.execute();
        }
    }

    private void initHomeFragmentActions() {
        fabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogFrag = FilterFragment.newInstance(appliedFilters);
                dialogFrag.setParentFab(fabFilter);
                dialogFrag.setCallbacks((Callbacks) getActivity());
                dialogFrag.setAnimationListener((AnimationListener) getActivity());

                dialogFrag.show(getActivity().getSupportFragmentManager(), dialogFrag.getTag());
            }
        });
    }

    @Override
    public void onFragmentBackPressed() {
        getActivity().finish();
    }

    private class DoSearchRestaurant extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;
        private double mLat = 0.00;
        private double mLng = 0.00;

        public DoSearchRestaurant(Context context, double lat, double lng) {
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
                ResponseRestaurantItem responseData = ResponseRestaurantItem.getResponseObject(result.getResult().toString(), ResponseRestaurantItem.class);

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

    /***************************
     * Fabulous Filter methods *
     ***************************/
    public void onResult(Object result) {
        Log.d(TAG, "onResult: " + result.toString());

        if (result != null) {

            if (result.toString().equalsIgnoreCase("swiped_down")) {
                //do something or nothing
            } else {

//                appliedFilters = (ArrayMap<String, List<String>>) result;
//                ArrayMap<String, List<String>> appliedFilters = (ArrayMap<String, List<String>>) result;
//                if (appliedFilters.size() != 0) {
//
//                    if (appliedFilters.get("category") != null) {
//                        if (appliedFilters.get("category").size() == 1) {
//                            selectedMusicCategory = appliedFilters.get("category").get(0);
//                        } else {
//                            selectedMusicCategory = "";
//                        }
//                    } else {
//                        selectedMusicCategory = "";
//                    }
//
//                    if (appliedFilters.get("state") != null) {
//                        if (appliedFilters.get("state").size() == 1) {
//                            selectedState = appliedFilters.get("state").get(0);
//                        }
//                    }
//
//                    searchMusic(getSelectedMusicCategory(selectedMusicCategory).getId(), getSelectedCity(selectedState).getId());
            }
        }
        //handle result
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (dialogFrag.isAdded()) {
            dialogFrag.dismiss();
            dialogFrag.show(getActivity().getSupportFragmentManager(), dialogFrag.getTag());
        }
    }

//    @Override
//    public void onOpenAnimationStart() {
//        Log.d("aah_animation", "onOpenAnimationStart: ");
//    }
//
//    @Override
//    public void onOpenAnimationEnd() {
//        Log.d("aah_animation", "onOpenAnimationEnd: ");
//    }
//
//    @Override
//    public void onCloseAnimationStart() {
//        Log.d("aah_animation", "onCloseAnimationStart: ");
//    }
//
//    @Override
//    public void onCloseAnimationEnd() {
//        Log.d("aah_animation", "onCloseAnimationEnd: ");
//    }

//    public List<String> getUniqueCategoryKeys(ArrayList<MusicCategory> musicCategories) {
//        List<String> categories = new ArrayList<>();
//        for (MusicCategory musicCategory : musicCategories) {
//            categories.add(musicCategory.getName());
//        }
//        Collections.sort(categories);
//        return categories;
//    }
//
//    public List<String> getUniqueStateKeys(ArrayList<City> states) {
//        List<String> cities = new ArrayList<>();
//        for (City city : states) {
//            cities.add(city.getName());
//        }
//        Collections.sort(cities);
//        return cities;
//    }
//
//    public List<String> getCategoryKey() {
//        return mCategoryKey;
//    }
//
//    public List<String> getStateKey() {
//        return mStateKey;
//    }
//
//    public MusicCategory getSelectedMusicCategory(String category) {
//        for (MusicCategory musicCategory : mCategory) {
//            if (musicCategory.getName().equalsIgnoreCase(category)) {
//                return musicCategory;
//            }
//        }
//        return new MusicCategory("", "");
//    }
//
//    public City getSelectedCity(String city) {
//        for (City mCity : mState) {
//            if (mCity.getName().equalsIgnoreCase(city)) {
//                return mCity;
//            }
//        }
//        return new City("", "");
//    }
}
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
import com.rc.foodsignal.model.DataFoodCategory;
import com.rc.foodsignal.model.FoodCategory;
import com.rc.foodsignal.model.Location;
import com.rc.foodsignal.model.ResponseFoodCategory;
import com.rc.foodsignal.model.ResponseRestaurantItem;
import com.rc.foodsignal.model.Restaurant;
import com.rc.foodsignal.util.AllUrls;
import com.rc.foodsignal.util.AppUtils;
import com.rc.foodsignal.util.HttpRequestManager;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.rc.foodsignal.util.AllConstants.DEFAULT_FOOD_CATEGORY;
import static com.rc.foodsignal.util.AllConstants.SESSION_FOOD_CATEGORY;
import static com.rc.foodsignal.util.AllConstants.SESSION_SELECTED_LOCATION;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class HomeFragment extends Fragment implements OnFragmentBackPressedListener {

    String TAG = AppUtils.getTagName(HomeFragment.class);
    private View parentView;
    GetAllFoodCategory getAllFoodCategory;
    Location mLocation;
    RecyclerView recyclerViewFood;
    RestaurantAdapter restaurantAdapter;

    //Fabulous Filter
    private ArrayMap<String, List<String>> appliedFilters = new ArrayMap<>();
    FilterFragment dialogFrag;
    FloatingActionButton fabFilter;
    ArrayList<FoodCategory> mCategory = new ArrayList<>();
    List<String> mCategoryKey = new ArrayList<>();
    String selectedCategory = "";

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

            setDefaultFoodCategory();

        } else {
            getAllFoodCategory = new GetAllFoodCategory(getActivity());
            getAllFoodCategory.execute();

            searchRestaurant(mLocation, getSelectedFoodCategory(selectedCategory).getId());
        }
    }

    private void setDefaultFoodCategory(){
        if (!AllSettingsManager.isNullOrEmpty(SessionManager.getStringSetting(getActivity(), SESSION_FOOD_CATEGORY))) {
            initFabulousFilter(DataFoodCategory.getResponseObject(SessionManager.getStringSetting(getActivity(), SESSION_FOOD_CATEGORY), DataFoodCategory.class).getData());
        } else {
            initFabulousFilter(DataFoodCategory.getResponseObject(DEFAULT_FOOD_CATEGORY, DataFoodCategory.class).getData());
        }
    }

    private void initFabulousFilter(ArrayList<FoodCategory> foodCategories) {
        mCategory = foodCategories;
        mCategoryKey = getUniqueCategoryKeys(foodCategories);
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

    private class DoSearchRestaurants extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;
        private Location mLocation;
        private String mCategoryId;

        public DoSearchRestaurants(Context context, Location location, String categoryId) {
            this.mContext = context;
            this.mLocation = location;
            this.mCategoryId = categoryId;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected HttpRequestManager.HttpResponse doInBackground(String... params) {
            HttpRequestManager.HttpResponse response = HttpRequestManager.doRestPostRequest(AllUrls.getSearchRestaurantUrl(), AllUrls.getSearchRestaurantParameters(Double.parseDouble(mLocation.getLat()), Double.parseDouble(mLocation.getLng()), mCategoryId), null);
            return response;
        }

        @Override
        protected void onPostExecute(HttpRequestManager.HttpResponse result) {

            if (result.isSuccess() && !AppUtils.isNullOrEmpty(result.getResult().toString())) {
                Log.d(TAG, "success response from web: " + result.getResult().toString());
                ResponseRestaurantItem responseData = ResponseRestaurantItem.getResponseObject(result.getResult().toString(), ResponseRestaurantItem.class);

                if (responseData.getStatus().equalsIgnoreCase("1") && (responseData.getData().size() > 0)) {
                    Log.d(TAG, "success wrapper: " + responseData.toString());

                    if (restaurantAdapter.getAllData().size() > 0) {
                        restaurantAdapter.removeAll();
                    }
                    restaurantAdapter.addAll(responseData.getData());
                    restaurantAdapter.notifyDataSetChanged();

                } else {
                    restaurantAdapter.removeAll();
                    restaurantAdapter.notifyDataSetChanged();

                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetAllFoodCategory extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;

        public GetAllFoodCategory(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected HttpRequestManager.HttpResponse doInBackground(String... params) {
            HttpRequestManager.HttpResponse response = HttpRequestManager.doGetRequest(AllUrls.getAllFoodCategoryUrl());
            return response;
        }

        @Override
        protected void onPostExecute(HttpRequestManager.HttpResponse result) {

            if (result.isSuccess() && !AppUtils.isNullOrEmpty(result.getResult().toString())) {
                Log.d(TAG, "success response from web: " + result.getResult().toString());

                ResponseFoodCategory responseFoodCategory = ResponseFoodCategory.getResponseObject(result.getResult().toString(), ResponseFoodCategory.class);

                if (responseFoodCategory.getStatus().equalsIgnoreCase("1") && (responseFoodCategory.getData().size() > 0)) {
                    Log.d(TAG, "success response from web: " + responseFoodCategory.toString());

                    //Save food category into session
                    DataFoodCategory dataFoodCategory = new DataFoodCategory(responseFoodCategory.getData());
                    SessionManager.setStringSetting(getActivity(), SESSION_FOOD_CATEGORY, DataFoodCategory.getResponseString(dataFoodCategory));

                    initFabulousFilter(responseFoodCategory.getData());

                } else {

                    setDefaultFoodCategory();
//                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                }
            } else {

                setDefaultFoodCategory();
//                Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void searchRestaurant(Location location, String categoryId) {
        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            return;
        }

        new DoSearchRestaurants(getActivity(), location, categoryId).execute();
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

                appliedFilters = (ArrayMap<String, List<String>>) result;
                ArrayMap<String, List<String>> appliedFilters = (ArrayMap<String, List<String>>) result;
                if (appliedFilters.size() != 0) {

                    if (appliedFilters.get("category") != null) {
                        if (appliedFilters.get("category").size() == 1) {
                            selectedCategory = appliedFilters.get("category").get(0);
                        } else {
                            selectedCategory = "";
                        }
                    } else {
                        selectedCategory = "";
                    }

                    searchRestaurant(mLocation, getSelectedFoodCategory(selectedCategory).getId());
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (dialogFrag.isAdded()) {
            dialogFrag.dismiss();
            dialogFrag.show(getActivity().getSupportFragmentManager(), dialogFrag.getTag());
        }
    }

    public List<String> getUniqueCategoryKeys(ArrayList<FoodCategory> foodCategories) {
        List<String> categories = new ArrayList<>();
        for (FoodCategory foodCategory : foodCategories) {
            categories.add(foodCategory.getName());
        }
        Collections.sort(categories);
        return categories;
    }

    public ArrayList<FoodCategory> getCategory() {
        return mCategory;
    }

    public List<String> getCategoryKey() {
        return mCategoryKey;
    }

    public FoodCategory getSelectedFoodCategory(String category) {
        for (FoodCategory foodCategory : mCategory) {
            if (foodCategory.getName().equalsIgnoreCase(category)) {
                return foodCategory;
            }
        }

        //Selected category 0 is for nothing
        return new FoodCategory("0", "", "");
    }
}
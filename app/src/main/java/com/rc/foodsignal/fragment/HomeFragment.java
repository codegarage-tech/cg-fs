package com.rc.foodsignal.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.rc.foodsignal.model.DataRestaurantCategory;
import com.rc.foodsignal.model.FoodCategory;
import com.rc.foodsignal.model.Location;
import com.rc.foodsignal.model.ResponseFoodCategory;
import com.rc.foodsignal.model.ResponseRestaurantCategory;
import com.rc.foodsignal.model.ResponseRestaurantItem;
import com.rc.foodsignal.model.RestaurantCategory;
import com.rc.foodsignal.util.AllUrls;
import com.rc.foodsignal.util.AppUtils;
import com.rc.foodsignal.util.HttpRequestManager;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.rc.foodsignal.util.AllConstants.DEFAULT_FOOD_CATEGORY;
import static com.rc.foodsignal.util.AllConstants.DEFAULT_RESTAURANT_CATEGORY;
import static com.rc.foodsignal.util.AllConstants.INTENT_KEY_LOCATION_LIST;
import static com.rc.foodsignal.util.AllConstants.INTENT_REQUEST_CODE_LOCATION_LIST;
import static com.rc.foodsignal.util.AllConstants.SESSION_FOOD_CATEGORY;
import static com.rc.foodsignal.util.AllConstants.SESSION_RESTAURANT_CATEGORY;
import static com.rc.foodsignal.util.AllConstants.SESSION_SELECTED_LOCATION;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class HomeFragment extends Fragment implements OnFragmentBackPressedListener {

    String TAG = AppUtils.getTagName(HomeFragment.class);
    private View parentView;
    GetAllFoodCategory getAllFoodCategory;
    GetAllRestaurantCategory getAllRestaurantCategory;
    Location mLocation;
    RecyclerView recyclerViewFood;
    RestaurantAdapter restaurantAdapter;
    ProgressDialog loadingDialog;

    //Fabulous Filter
    private ArrayMap<String, List<String>> appliedFilters = new ArrayMap<>();
    RestaurantFilterFragment dialogFrag;
    FloatingActionButton fabFilter;
    ArrayList<FoodCategory> mFoodCategory = new ArrayList<>();
    List<String> mFoodCategoryKey = new ArrayList<>();
    String selectedFoodCategory = "";
    ArrayList<RestaurantCategory> mRestaurantCategory = new ArrayList<>();
    List<String> mRestaurantCategoryKey = new ArrayList<>();
    String selectedRestaurantCategory = "";

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

        restaurantAdapter = new RestaurantAdapter(getActivity());
        recyclerViewFood.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewFood.setAdapter(restaurantAdapter);

        if (!AppUtils.isNullOrEmpty(SessionManager.getStringSetting(getActivity(), SESSION_SELECTED_LOCATION))) {
            Log.d(TAG, "Session data: " + SessionManager.getStringSetting(getActivity(), SESSION_SELECTED_LOCATION));
            mLocation = Location.getResponseObject(SessionManager.getStringSetting(getActivity(), SESSION_SELECTED_LOCATION), Location.class);

            ((HomeActivity) getActivity()).setToolbarTitle(AppUtils.formatLocationInfo(String.format("%s, %s, %s, %s", mLocation.getStreet(), mLocation.getCity(), mLocation.getState(), mLocation.getCountry())));
        }

        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();

            setDefaultFoodCategory();
            setDefaultRestaurantCategory();

        } else {
            getAllFoodCategory = new GetAllFoodCategory(getActivity());
            getAllFoodCategory.execute();

            getAllRestaurantCategory = new GetAllRestaurantCategory(getActivity());
            getAllRestaurantCategory.execute();

            searchRestaurant(mLocation, getSelectedFoodCategory(selectedFoodCategory).getId(), getSelectedRestaurantCategory(selectedRestaurantCategory).getId());
        }
    }

    private void setDefaultFoodCategory() {
        if (!AllSettingsManager.isNullOrEmpty(SessionManager.getStringSetting(getActivity(), SESSION_FOOD_CATEGORY))) {
            initFilterFoodCategory(DataFoodCategory.getResponseObject(SessionManager.getStringSetting(getActivity(), SESSION_FOOD_CATEGORY), DataFoodCategory.class).getData());
        } else {
            initFilterFoodCategory(DataFoodCategory.getResponseObject(DEFAULT_FOOD_CATEGORY, DataFoodCategory.class).getData());
        }
    }

    private void setDefaultRestaurantCategory() {
        if (!AllSettingsManager.isNullOrEmpty(SessionManager.getStringSetting(getActivity(), SESSION_RESTAURANT_CATEGORY))) {
            initFilterRestaurantCategory(DataRestaurantCategory.getResponseObject(SessionManager.getStringSetting(getActivity(), SESSION_RESTAURANT_CATEGORY), DataRestaurantCategory.class).getData());
        } else {
            initFilterRestaurantCategory(DataRestaurantCategory.getResponseObject(DEFAULT_RESTAURANT_CATEGORY, DataRestaurantCategory.class).getData());
        }
    }

    private void initFilterFoodCategory(ArrayList<FoodCategory> foodCategories) {
        mFoodCategory = foodCategories;
        mFoodCategoryKey = getUniqueFoodCategoryKeys(foodCategories);
    }

    private void initFilterRestaurantCategory(ArrayList<RestaurantCategory> restaurantCategories) {
        mRestaurantCategory = restaurantCategories;
        mRestaurantCategoryKey = getUniqueRestaurantCategoryKeys(restaurantCategories);
    }

    private void initHomeFragmentActions() {
        fabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogFrag = RestaurantFilterFragment.newInstance(appliedFilters);
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
        private String mFoodCategoryId;
        private String mRestaurantCategoryId;

        public DoSearchRestaurants(Context context, Location location, String foodCategoryId, String restaurantCategoryId) {
            this.mContext = context;
            this.mLocation = location;
            this.mFoodCategoryId = foodCategoryId;
            this.mRestaurantCategoryId = restaurantCategoryId;
        }

        @Override
        protected void onPreExecute() {
            loadingDialog = new ProgressDialog(mContext);
            loadingDialog.setMessage(getResources().getString(R.string.txt_loading));
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
            HttpRequestManager.HttpResponse response = HttpRequestManager.doRestPostRequest(AllUrls.getSearchRestaurantUrl(), AllUrls.getSearchRestaurantParameters(Double.parseDouble(mLocation.getLat()), Double.parseDouble(mLocation.getLng()), mFoodCategoryId, mRestaurantCategoryId), null);
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

                    initFilterFoodCategory(responseFoodCategory.getData());

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

    private class GetAllRestaurantCategory extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;

        public GetAllRestaurantCategory(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected HttpRequestManager.HttpResponse doInBackground(String... params) {
            HttpRequestManager.HttpResponse response = HttpRequestManager.doGetRequest(AllUrls.getAllRestaurantCategoryUrl());
            return response;
        }

        @Override
        protected void onPostExecute(HttpRequestManager.HttpResponse result) {

            if (result.isSuccess() && !AppUtils.isNullOrEmpty(result.getResult().toString())) {
                Log.d(TAG, "success response from web: " + result.getResult().toString());

                ResponseRestaurantCategory responseRestaurantCategory = ResponseRestaurantCategory.getResponseObject(result.getResult().toString(), ResponseRestaurantCategory.class);

                if (responseRestaurantCategory.getStatus().equalsIgnoreCase("1") && (responseRestaurantCategory.getData().size() > 0)) {
                    Log.d(TAG, "success response from object: " + responseRestaurantCategory.toString());

                    //Save restaurant category into session
                    DataRestaurantCategory dataRestaurantCategory = new DataRestaurantCategory(responseRestaurantCategory.getData());
                    SessionManager.setStringSetting(getActivity(), SESSION_RESTAURANT_CATEGORY, dataRestaurantCategory.getResponseString(dataRestaurantCategory));

                    initFilterRestaurantCategory(dataRestaurantCategory.getData());

                } else {

                    setDefaultRestaurantCategory();
                }
            } else {

                setDefaultRestaurantCategory();
            }
        }
    }

    private void searchRestaurant(Location location, String foodCategoryId, String restaurantCategoryId) {
        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            return;
        }

        new DoSearchRestaurants(getActivity(), location, foodCategoryId, restaurantCategoryId).execute();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INTENT_REQUEST_CODE_LOCATION_LIST: {
                if (data != null && resultCode == RESULT_OK) {
                    if (data.getBooleanExtra(INTENT_KEY_LOCATION_LIST, false)) {
                        //Update new selected location
                        if (!AppUtils.isNullOrEmpty(SessionManager.getStringSetting(getActivity(), SESSION_SELECTED_LOCATION))) {
                            Log.d(TAG, "Session data: " + SessionManager.getStringSetting(getActivity(), SESSION_SELECTED_LOCATION));
                            mLocation = Location.getResponseObject(SessionManager.getStringSetting(getActivity(), SESSION_SELECTED_LOCATION), Location.class);

                            ((HomeActivity) getActivity()).setToolbarTitle(AppUtils.formatLocationInfo(String.format("%s, %s, %s, %s", mLocation.getStreet(), mLocation.getCity(), mLocation.getState(), mLocation.getCountry())));
                        }

                        //Search result depending on new location
                        searchRestaurant(mLocation, getSelectedFoodCategory(selectedFoodCategory).getId(), getSelectedRestaurantCategory(selectedRestaurantCategory).getId());
                    }
                }
                break;
            }
            default:
                break;
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

                appliedFilters = (ArrayMap<String, List<String>>) result;
                ArrayMap<String, List<String>> appliedFilters = (ArrayMap<String, List<String>>) result;
                if (appliedFilters.size() != 0) {

                    if (appliedFilters.get("food_category") != null) {
                        if (appliedFilters.get("food_category").size() == 1) {
                            selectedFoodCategory = appliedFilters.get("food_category").get(0);
                        } else {
                            selectedFoodCategory = "";
                        }
                    } else {
                        selectedFoodCategory = "";
                    }

                    if (appliedFilters.get("restaurant_category") != null) {
                        if (appliedFilters.get("restaurant_category").size() == 1) {
                            selectedRestaurantCategory = appliedFilters.get("restaurant_category").get(0);
                        } else {
                            selectedRestaurantCategory = "";
                        }
                    } else {
                        selectedRestaurantCategory = "";
                    }
                } else {
                    selectedFoodCategory = "";
                    selectedRestaurantCategory = "";
                }

                searchRestaurant(mLocation, getSelectedFoodCategory(selectedFoodCategory).getId(), getSelectedRestaurantCategory(selectedRestaurantCategory).getId());
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

    public List<String> getUniqueFoodCategoryKeys(ArrayList<FoodCategory> foodCategories) {
        List<String> categories = new ArrayList<>();
        for (FoodCategory foodCategory : foodCategories) {
            categories.add(foodCategory.getName());
        }
        Collections.sort(categories);
        return categories;
    }

    public List<String> getUniqueRestaurantCategoryKeys(ArrayList<RestaurantCategory> restaurantCategories) {
        List<String> categories = new ArrayList<>();
        for (RestaurantCategory restaurantCategory : restaurantCategories) {
            categories.add(restaurantCategory.getName());
        }
        Collections.sort(categories);
        return categories;
    }

    public List<String> getFoodCategoryKey() {
        return mFoodCategoryKey;
    }

    public List<String> getRestaurantCategoryKey() {
        return mRestaurantCategoryKey;
    }

    public FoodCategory getSelectedFoodCategory(String foodCategory) {
        for (FoodCategory category : mFoodCategory) {
            if (category.getName().equalsIgnoreCase(foodCategory)) {
                return category;
            }
        }

        //Selected category 0 is for nothing
        return new FoodCategory("0", "");
    }

    public RestaurantCategory getSelectedRestaurantCategory(String restaurantCategory) {
        for (RestaurantCategory category : mRestaurantCategory) {
            if (category.getName().equalsIgnoreCase(restaurantCategory)) {
                return category;
            }
        }

        //Selected category 0 is for nothing
        return new RestaurantCategory("0", "");
    }
}
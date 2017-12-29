package com.rc.foodsignal.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rc.foodsignal.R;
import com.rc.foodsignal.model.ResponseAddUserBasicInfo;
import com.rc.foodsignal.model.UserBasicInfo;
import com.rc.foodsignal.model.UserData;
import com.rc.foodsignal.util.AllUrls;
import com.rc.foodsignal.util.AppUtils;
import com.reversecoder.library.httprequest.HttpRequestManager;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.segmentradiogroup.SegmentedRadioGroup;
import com.reversecoder.library.storage.SessionManager;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.geocoding.LocationAddressListener;
import com.seatgeek.placesautocomplete.geocoding.ReverseGeocoderTask;
import com.seatgeek.placesautocomplete.geocoding.UserLocationAddress;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;
import com.seatgeek.placesautocomplete.model.PlaceLocation;

import static com.rc.foodsignal.util.AllConstants.SESSION_IS_LOCATION_ADDED;
import static com.rc.foodsignal.util.AllConstants.SESSION_SELECTED_LOCATION;
import static com.rc.foodsignal.util.AllConstants.SESSION_USER_BASIC_INFO;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class AddUserBasicInfoActivity extends BaseLocationActivity {

    String TAG = AppUtils.getTagName(AddUserBasicInfoActivity.class);
    TextView tvTitle;
    ImageView ivBack;
    LinearLayout llAddLocation, llLocationDetail, llStepOne, llStepTwo;
    UserData userData;

    //User info
    boolean isStepOneSelected = true;
    EditText edtName, edtPhone, edtEmail;

    //Place search
    PlacesAutocompleteTextView acPlace;
    TextView tvStreet, tvCity, tvState, tvCountry;
    ProgressDialog loadingDialog;
    PlaceLocation selectedPlaceAutocomplete;
    Place selectedPlace;
    String selectedAddress = "";

    //Segmented Radio group
    SegmentedRadioGroup segmentedRadioGroup;
    RadioButton segmentedRadioButtonCurrentLocation, segmentedRadioButtonSearchLocation;

    ReverseGeocoderTask currentLocationTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_basic_info);

        initAddLocationUI();
        initAddLocationActions();

        if (!NetworkManager.isConnected(AddUserBasicInfoActivity.this)) {
            Toast.makeText(AddUserBasicInfoActivity.this, getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public void onLocationFound(Location location) {
        if (segmentedRadioButtonCurrentLocation.isChecked() && location != null) {
            setCurrentLocationDetail(location);
        }
    }

    private void initAddLocationUI() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.setText(getString(R.string.title_activity_add_location));
        llAddLocation = (LinearLayout) findViewById(R.id.ll_done);
        llLocationDetail = (LinearLayout) findViewById(R.id.ll_location_detail);
        llStepOne = (LinearLayout) findViewById(R.id.ll_step_one);
        llStepTwo = (LinearLayout) findViewById(R.id.ll_step_two);
        acPlace = (PlacesAutocompleteTextView) findViewById(R.id.autocomplete);
        acPlace.dismissDropDown();
        tvStreet = (TextView) findViewById(R.id.street);
        tvCity = (TextView) findViewById(R.id.city);
        tvState = (TextView) findViewById(R.id.state);
        tvCountry = (TextView) findViewById(R.id.country);
        edtName = (EditText) findViewById(R.id.edt_name);
        edtPhone = (EditText) findViewById(R.id.edt_phone);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        segmentedRadioGroup = (SegmentedRadioGroup) findViewById(R.id.sr_location_type);
        segmentedRadioButtonCurrentLocation = (RadioButton) findViewById(R.id.segmented_rbtn_current_location);
        segmentedRadioButtonSearchLocation = (RadioButton) findViewById(R.id.segmented_rbtn_search_location);
    }

    private void initAddLocationActions() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        segmentedRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                           @Override
                                                           public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                               switch (checkedId) {
                                                                   case R.id.segmented_rbtn_current_location:
                                                                       clearAllView();
                                                                       acPlace.setEnabled(false);
                                                                       acPlace.setHint("");
                                                                       acPlace.dismissDropDown();

                                                                       //Set previous data if exist
                                                                       if (mCurrentLocation != null) {
                                                                           setCurrentLocationDetail(mCurrentLocation);
                                                                       }
                                                                       break;
                                                                   case R.id.segmented_rbtn_search_location:
                                                                       clearAllView();
                                                                       acPlace.setEnabled(true);
                                                                       acPlace.setHint("Type Your Address");
                                                                       acPlace.dismissDropDown();

                                                                       //Set previous data if exist
                                                                       if (selectedPlace != null && !selectedAddress.equalsIgnoreCase("")) {
                                                                           acPlace.setText(selectedAddress);
                                                                           acPlace.dismissDropDown();
                                                                           setPlaceSearchDetail(selectedPlace);
                                                                       }
                                                                       break;
                                                               }
                                                           }
                                                       }

        );

        llAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String street = "";
                String city = "";
                String state = "";
                String zip = "";
                String country = "";
                String latitude = "";
                String longitude = "";
                String name = "";
                String phone = "";
                String email = "";

                if (isStepOneSelected) {
                    if (!NetworkManager.isConnected(AddUserBasicInfoActivity.this)) {
                        Toast.makeText(AddUserBasicInfoActivity.this, getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (acPlace.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(AddUserBasicInfoActivity.this, getResources().getString(R.string.toast_empty_address_field), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    setUserInfoPage();
                } else {
                    if (edtName.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(AddUserBasicInfoActivity.this, getResources().getString(R.string.toast_empty_name_field), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (edtPhone.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(AddUserBasicInfoActivity.this, getResources().getString(R.string.toast_empty_phone_field), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (edtEmail.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(AddUserBasicInfoActivity.this, getResources().getString(R.string.toast_empty_email_field), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    street = (!tvStreet.getText().toString().equalsIgnoreCase("___")) ? tvStreet.getText().toString() : "";
                    city = (!tvCity.getText().toString().equalsIgnoreCase("___")) ? tvCity.getText().toString() : "";
                    state = (!tvState.getText().toString().equalsIgnoreCase("___")) ? tvState.getText().toString() : "";
                    zip = "";
                    country = (!tvCountry.getText().toString().equalsIgnoreCase("___")) ? tvCountry.getText().toString() : "";
                    latitude = (segmentedRadioButtonCurrentLocation.isChecked()) ? mCurrentLocation.getLatitude() + "" : selectedPlaceAutocomplete.lat + "";
                    longitude = (segmentedRadioButtonCurrentLocation.isChecked()) ? mCurrentLocation.getLongitude() + "" : selectedPlaceAutocomplete.lng + "";

                    name = edtName.getText().toString();
                    phone = edtPhone.getText().toString();
                    email = (!edtEmail.getText().toString().equalsIgnoreCase("")) ? edtEmail.getText().toString() : "";

                    new DoAddAddUserBasicInfo(AddUserBasicInfoActivity.this, street, city, state, zip, country, latitude, longitude, name, phone, email).execute();
                }
            }
        });

        acPlace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    clearPreviousData();
                    llAddLocation.setVisibility(View.GONE);
                    llLocationDetail.setVisibility(View.GONE);
                }
            }
        });

        acPlace.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(final Place place) {
                selectedAddress = acPlace.getText().toString();
                selectedPlace = place;
                setPlaceSearchDetail(selectedPlace);
            }
        });
    }

    private void setUserInfoPage() {
        tvTitle.setText(getString(R.string.title_activity_add_user_basic_info));
        llStepOne.setVisibility(View.GONE);
        llStepTwo.setVisibility(View.VISIBLE);
        isStepOneSelected = false;
    }

    private void setLocationPage() {
        tvTitle.setText(getString(R.string.title_activity_add_location));
        llStepTwo.setVisibility(View.GONE);
        llStepOne.setVisibility(View.VISIBLE);
        isStepOneSelected = true;
    }

    private void setPlaceSearchDetail(Place place) {
        acPlace.getDetailsFor(place, new DetailsCallback() {
            @Override
            public void onSuccess(final PlaceDetails details) {

                clearPreviousData();
                llAddLocation.setVisibility(View.VISIBLE);
                llLocationDetail.setVisibility(View.VISIBLE);

                Log.d(TAG, "details: " + details);
                tvStreet.setText(details.name);
                selectedPlaceAutocomplete = details.geometry.location;

                for (AddressComponent component : details.address_components) {
                    Log.d(TAG, "component: " + component.toString());
                    for (AddressComponentType type : component.types) {
                        switch (type) {
                            case STREET_NUMBER:
                                break;
                            case ROUTE:
                                break;
                            case NEIGHBORHOOD:
                                break;
                            case SUBLOCALITY_LEVEL_1:
                                break;
                            case SUBLOCALITY:
                                break;
                            case LOCALITY:
                                tvCity.setText(component.long_name);
                                break;
                            case ADMINISTRATIVE_AREA_LEVEL_1:
                                tvState.setText(component.short_name);
                                break;
                            case ADMINISTRATIVE_AREA_LEVEL_2:
                                break;
                            case COUNTRY:
                                tvCountry.setText(component.long_name);
                                break;
                            case POSTAL_CODE:
//                                        mZip.setText(component.long_name);
                                break;
                            case POLITICAL:
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(final Throwable failure) {
                Log.d("test", "failure " + failure);
            }
        });
    }

    private void setCurrentLocationDetail(Location location) {
        if ((currentLocationTask != null) && (currentLocationTask.getStatus() == AsyncTask.Status.RUNNING)) {
            currentLocationTask.cancel(true);
        }

        currentLocationTask = new ReverseGeocoderTask(AddUserBasicInfoActivity.this, new LocationAddressListener() {
            @Override
            public void getLocationAddress(UserLocationAddress locationAddress) {

                clearPreviousData();
                llAddLocation.setVisibility(View.VISIBLE);
                llLocationDetail.setVisibility(View.VISIBLE);

                Log.d(TAG, "UserLocationAddress: " + locationAddress.toString());
                String addressText = String.format("%s, %s, %s, %s", locationAddress.getStreetAddress(), locationAddress.getCity(), locationAddress.getState(), locationAddress.getCountry());
                acPlace.setText(addressText);
                acPlace.dismissDropDown();

                tvStreet.setText(locationAddress.getStreetAddress());
                tvCity.setText(locationAddress.getCity());
                tvState.setText(locationAddress.getState());
                tvCountry.setText(locationAddress.getCountry());
            }
        });
        currentLocationTask.execute(location);


    }

    private void clearAllView() {
        clearPreviousData();
        acPlace.setText("");
        acPlace.dismissDropDown();
        llAddLocation.setVisibility(View.GONE);
        llLocationDetail.setVisibility(View.GONE);
    }

    private void clearPreviousData() {
        tvStreet.setText("");
        tvCity.setText("");
        tvState.setText("");
        tvCountry.setText("");
    }

    private class DoAddAddUserBasicInfo extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;
        private String mStreet = "", mCity = "", mState = "", mZip = "", mCountry = "", mLatitude = "", mLongitude = "", mName = "", mPhone = "", mEmail = "";

        public DoAddAddUserBasicInfo(Context context, String street, String city, String state, String zip, String country, String latitude, String longitude, String name, String phone, String email) {
            mContext = context;
            mStreet = street;
            mCity = city;
            mState = state;
            mZip = zip;
            mCountry = country;
            mLatitude = latitude;
            mLongitude = longitude;
            mName = name;
            mPhone = phone;
            mEmail = email;
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
            HttpRequestManager.HttpResponse response = HttpRequestManager.doRestPostRequest(AllUrls.getAddUserBasicInfoUrl(), AllUrls.getAddUserBasicInfoParameters(mStreet, mCity, mState, mZip, mCountry, mLatitude, mLongitude, mName, mPhone, mEmail), null);
            return response;
        }

        @Override
        protected void onPostExecute(HttpRequestManager.HttpResponse result) {

            if (loadingDialog != null
                    && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }

            if (result.isSuccess() && !AppUtils.isNullOrEmpty(result.getResult().toString())) {
                Log.d(TAG, "success response from web: " + result.getResult().toString());
                ResponseAddUserBasicInfo responseData = ResponseAddUserBasicInfo.getResponseObject(result.getResult().toString(), ResponseAddUserBasicInfo.class);

                if (responseData.getStatus().equalsIgnoreCase("success") && (responseData.getData().size() > 0)) {
                    Log.d(TAG, "success wrapper: " + responseData.getData().get(0).toString());
                    SessionManager.setStringSetting(AddUserBasicInfoActivity.this, SESSION_SELECTED_LOCATION, responseData.getData().get(0).toString());
                    SessionManager.setBooleanSetting(AddUserBasicInfoActivity.this, SESSION_IS_LOCATION_ADDED, true);

                    UserBasicInfo userBasicInfo = new UserBasicInfo(responseData.getData().get(0).getUser_id(), mName, mPhone, mEmail);
                    SessionManager.setStringSetting(AddUserBasicInfoActivity.this, SESSION_USER_BASIC_INFO, userBasicInfo.toString());

                    Intent intent = new Intent(AddUserBasicInfoActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AddUserBasicInfoActivity.this, getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(AddUserBasicInfoActivity.this, getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isStepOneSelected) {
            super.onBackPressed();
        } else {
            setLocationPage();
        }
    }
}
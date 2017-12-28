package com.rc.foodsignal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rc.foodsignal.R;
import com.rc.foodsignal.activity.AddressListActivity;
import com.rc.foodsignal.activity.LoginActivity;
import com.rc.foodsignal.activity.ProfileActivity;
import com.rc.foodsignal.interfaces.OnFragmentBackPressedListener;
import com.rc.foodsignal.model.Location;
import com.rc.foodsignal.model.UserBasicInfo;
import com.rc.foodsignal.util.AppUtils;
import com.reversecoder.library.storage.SessionManager;

import static android.app.Activity.RESULT_OK;
import static com.rc.foodsignal.util.AllConstants.INTENT_KEY_LOGIN;
import static com.rc.foodsignal.util.AllConstants.INTENT_REQUEST_CODE_ACCOUNT;
import static com.rc.foodsignal.util.AllConstants.SESSION_IS_USER_LOGGED_IN;
import static com.rc.foodsignal.util.AllConstants.SESSION_SELECTED_LOCATION;
import static com.rc.foodsignal.util.AllConstants.SESSION_USER_BASIC_INFO;
import static com.rc.foodsignal.util.AllConstants.SESSION_USER_DATA;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class AccountFragment extends Fragment implements OnFragmentBackPressedListener {

    private View parentView;
    private LinearLayout llProfile, llAddress, llNotification, llPaymentCard, llAddRestaurant, llMenu, llAboutRestaurant, llLogout, llRestaurantOwner;
    private static String TAG = AccountFragment.class.getSimpleName();
    UserBasicInfo userBasicInfo;
    Location location;
    TextView tvProfile, tvAddress, tvNotification, tvPaymentCard, tvAddRestaurant, tvMenu, tvAboutRestaurant, tvLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_account, container, false);

        setUpViews();

        setUpActions();

        return parentView;
    }

    private void setUpViews() {
        if (!AppUtils.isNullOrEmpty(SessionManager.getStringSetting(getActivity(), SESSION_USER_BASIC_INFO))) {
            Log.d(TAG, "Session data: " + SessionManager.getStringSetting(getActivity(), SESSION_USER_BASIC_INFO));
            userBasicInfo = UserBasicInfo.getResponseObject(SessionManager.getStringSetting(getActivity(), SESSION_USER_BASIC_INFO), UserBasicInfo.class);
        }

        if (!AppUtils.isNullOrEmpty(SessionManager.getStringSetting(getActivity(), SESSION_SELECTED_LOCATION))) {
            Log.d(TAG, "Session data: " + SessionManager.getStringSetting(getActivity(), SESSION_SELECTED_LOCATION));
            location = Location.getResponseObject(SessionManager.getStringSetting(getActivity(), SESSION_SELECTED_LOCATION), Location.class);
        }

        llProfile = (LinearLayout) parentView.findViewById(R.id.ll_profile);
        llAddress = (LinearLayout) parentView.findViewById(R.id.ll_address);
        llNotification = (LinearLayout) parentView.findViewById(R.id.ll_notification);
        llPaymentCard = (LinearLayout) parentView.findViewById(R.id.ll_payment_card);
        llAddRestaurant = (LinearLayout) parentView.findViewById(R.id.ll_add_restaurant);
        llMenu = (LinearLayout) parentView.findViewById(R.id.ll_menu);
        llAboutRestaurant = (LinearLayout) parentView.findViewById(R.id.ll_about_restaurant);
        llLogout = (LinearLayout) parentView.findViewById(R.id.ll_logout);
        llRestaurantOwner = (LinearLayout) parentView.findViewById(R.id.ll_restaurant_owner);
        tvProfile = (TextView) parentView.findViewById(R.id.tv_profile_txt);
        tvAddress = (TextView) parentView.findViewById(R.id.tv_address_txt);
        tvNotification = (TextView) parentView.findViewById(R.id.tv_notification_txt);
        tvPaymentCard = (TextView) parentView.findViewById(R.id.tv_payment_card_txt);
        tvAddRestaurant = (TextView) parentView.findViewById(R.id.tv_add_restaurant_txt);
        tvMenu = (TextView) parentView.findViewById(R.id.tv_menu_txt);
        tvAboutRestaurant = (TextView) parentView.findViewById(R.id.tv_about_restaurant_txt);
        tvLogout = (TextView) parentView.findViewById(R.id.tv_logout_txt);

        if (SessionManager.getBooleanSetting(getActivity(), SESSION_IS_USER_LOGGED_IN, false)) {
            llRestaurantOwner.setVisibility(View.VISIBLE);
        } else {
            llRestaurantOwner.setVisibility(View.GONE);
        }

        setSubtitle();
    }

    private void setSubtitle() {
        if (userBasicInfo != null) {
            tvProfile.setText(userBasicInfo.getName());
        }
        if (location != null) {
            tvAddress.setText(String.format("%s, %s, %s, %s", location.getStreet(), location.getCity(), location.getState(), location.getCountry()));
        }
    }

    private void setUpActions() {
        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProfile = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intentProfile);
            }
        });
        llAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddress = new Intent(getActivity(), AddressListActivity.class);
                startActivity(intentAddress);
            }
        });
        llAddRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SessionManager.getBooleanSetting(getActivity(), SESSION_IS_USER_LOGGED_IN, false)) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, INTENT_REQUEST_CODE_ACCOUNT);
                }
            }
        });

        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager.removeSetting(getActivity(), SESSION_IS_USER_LOGGED_IN);
                SessionManager.removeSetting(getActivity(), SESSION_USER_DATA);

                llRestaurantOwner.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onFragmentBackPressed() {
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case INTENT_REQUEST_CODE_ACCOUNT: {
                if (data != null && resultCode == RESULT_OK) {
                    if (data.getBooleanExtra(INTENT_KEY_LOGIN, false)) {
                        llRestaurantOwner.setVisibility(View.VISIBLE);
                    } else {
                        llRestaurantOwner.setVisibility(View.GONE);
                    }
                }
                break;
            }
            default:
                break;
        }
    }
}



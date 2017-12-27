package com.rc.foodsignal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rc.foodsignal.R;
import com.rc.foodsignal.activity.LoginActivity;
import com.rc.foodsignal.interfaces.OnFragmentBackPressedListener;
import com.reversecoder.library.storage.SessionManager;

import static android.app.Activity.RESULT_OK;
import static com.rc.foodsignal.util.AllConstants.INTENT_KEY_LOGIN;
import static com.rc.foodsignal.util.AllConstants.INTENT_REQUEST_CODE_ACCOUNT;
import static com.rc.foodsignal.util.AllConstants.SESSION_IS_USER_LOGGED_IN;
import static com.rc.foodsignal.util.AllConstants.SESSION_USER_DATA;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class AccountFragment extends Fragment implements OnFragmentBackPressedListener {

    private View parentView;
    private LinearLayout llAddRestaurant, llRestaurantOwner, llLogout;
    private static String TAG = AccountFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_account, container, false);

        setUpViews();

        setUpActions();

        return parentView;
    }

    private void setUpViews() {
        llAddRestaurant = (LinearLayout) parentView.findViewById(R.id.ll_add_restaurant);
        llLogout = (LinearLayout) parentView.findViewById(R.id.ll_logout);
        llRestaurantOwner = (LinearLayout) parentView.findViewById(R.id.ll_restaurant_owner);
        if (SessionManager.getBooleanSetting(getActivity(), SESSION_IS_USER_LOGGED_IN, false)) {
            llRestaurantOwner.setVisibility(View.VISIBLE);
        } else {
            llRestaurantOwner.setVisibility(View.GONE);
        }
    }

    private void setUpActions() {
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



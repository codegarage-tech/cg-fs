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

import static com.rc.foodsignal.util.AllConstants.SESSION_IS_USER_LOGGED_IN;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class AccountFragment extends Fragment implements OnFragmentBackPressedListener {

    private View parentView;
    private LinearLayout llAddRestaurant, llRestaurantOwner, llLogout;

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
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onFragmentBackPressed() {
        getActivity().finish();
    }
}



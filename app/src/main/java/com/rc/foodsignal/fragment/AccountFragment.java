package com.rc.foodsignal.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rc.foodsignal.R;
import com.rc.foodsignal.interfaces.OnFragmentBackPressedListener;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class AccountFragment extends Fragment implements OnFragmentBackPressedListener {

    private View parentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_account, container, false);

        return parentView;
    }

    private void setUpViews() {
    }

    @Override
    public void onFragmentBackPressed() {
        getActivity().finish();
    }
}



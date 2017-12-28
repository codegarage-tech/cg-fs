package com.rc.foodsignal.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.rc.foodsignal.interfaces.OnFragmentBackPressedListener;
import com.rc.foodsignal.interfaces.OnFragmentResultListener;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class BaseFragment extends Fragment implements OnFragmentBackPressedListener, OnFragmentResultListener {

    @Override
    public void onFragmentBackPressed() {
        getActivity().finish();
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Intent data) {
    }
}



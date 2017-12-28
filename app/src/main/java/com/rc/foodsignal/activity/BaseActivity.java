package com.rc.foodsignal.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.rc.foodsignal.interfaces.OnFragmentBackPressedListener;
import com.rc.foodsignal.interfaces.OnFragmentResultListener;

import java.util.List;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class BaseActivity extends AppCompatActivity {

    private String TAG = BaseActivity.class.getSimpleName();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //send on activity result event to the fragment
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof OnFragmentResultListener) {
                    ((OnFragmentResultListener) fragment).onFragmentResult(requestCode, resultCode, data);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {

        //send back press event to the fragment
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof OnFragmentBackPressedListener) {
                    ((OnFragmentBackPressedListener) fragment).onFragmentBackPressed();
                }
            }
        } else {
            super.onBackPressed();
        }
    }
}
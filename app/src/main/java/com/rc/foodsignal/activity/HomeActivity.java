package com.rc.foodsignal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rc.foodsignal.R;
import com.rc.foodsignal.fragment.HomeFragment;
import com.rc.foodsignal.util.AllConstants;
import com.rc.foodsignal.util.FragmentUtilsManager;
import com.rc.foodsignal.view.CanaroTextView;
import com.reversecoder.gcm.listener.GcmResultListener;
import com.reversecoder.gcm.task.RegisterAppTask;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;

import static com.rc.foodsignal.util.AllConstants.INTENT_KEY_LOGIN;
import static com.rc.foodsignal.util.AllConstants.INTENT_REQUEST_CODE_ADD_RESTAURANT_LOGIN;
import static com.rc.foodsignal.util.AllConstants.INTENT_REQUEST_CODE_LOCATION_LIST;
import static com.rc.foodsignal.util.AllConstants.SESSION_IS_RESTAURANT_LOGGED_IN;
import static com.rc.foodsignal.util.AllConstants.SESSION_RESTAURANT_LOGIN_DATA;
import static com.rc.foodsignal.util.AllConstants.SESSION_SELECTED_NAVIGATION_MENU;
import static com.reversecoder.gcm.util.GcmConfig.SESSION_IS_GCM_NOTIFICATION;

public class HomeActivity extends BaseActivity implements AAH_FabulousFragment.Callbacks, AAH_FabulousFragment.AnimationListener {

    //Toolvar
    Toolbar toolbar;
    CanaroTextView tvToolbarTitle;

    //Navigation drawer
    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    MenuItem previousSelectedMenuItem;
    ImageView userAvatar;
    TextView userName, userInfo;
    private String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        if (savedInstanceState == null) {
            //Set home fragment is any navigation item is not selected
            goFragmentScreen(getString(R.string.title_fragment_home), new HomeFragment());

            //Check navigation menu item()
            checkNavigationMenuItem(navigationView.getMenu().findItem(R.id.nav_home));
        }

        //initialize push notification
        initPushNotification();
    }

    private void goFragmentScreen(String currentTag, Fragment fragment) {
        FragmentUtilsManager.changeSupportFragment(HomeActivity.this, fragment, currentTag);
    }

    private void initView() {
        initToolbar();
        initNavigationDrawer();
    }

    private void initToolbar() {
        //Toolbar setup
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        tvToolbarTitle = (CanaroTextView) findViewById(R.id.tv_toolbar_title);
    }

    private void initNavigationDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.inflateHeaderView(R.layout.nav_header_main);
        if (SessionManager.getBooleanSetting(HomeActivity.this, SESSION_IS_RESTAURANT_LOGGED_IN, false)) {
            navigationView.inflateMenu(R.menu.activity_main_drawer_logged_in);
        } else {
            navigationView.inflateMenu(R.menu.activity_main_drawer_guest);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
                if (SessionManager.getIntegerSetting(HomeActivity.this, SESSION_SELECTED_NAVIGATION_MENU, -1) != item.getItemId()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            handleNavigationItemChange(item);
                        }
                    }, AllConstants.NAVIGATION_DRAWER_CLOSE_DELAY);
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        userAvatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.userAvatar);
        userName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userName);
        userInfo = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userInfo);

        Glide
                .with(HomeActivity.this)
                .load(R.mipmap.ic_launcher_round)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .apply(new RequestOptions().circleCropTransform())
                .into(userAvatar);
        userName.setText(getString(R.string.app_name));
        userInfo.setText(getString(R.string.app_version_name));
    }

    public void setToolbarTitle(String title) {
        tvToolbarTitle.setText(title);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void checkNavigationMenuItem(MenuItem item) {
        //Store current menu in session
        SessionManager.setIntegerSetting(HomeActivity.this, SESSION_SELECTED_NAVIGATION_MENU, item.getItemId());

        //Set checkable for each menu item
        if (previousSelectedMenuItem != null) {
            previousSelectedMenuItem.setChecked(false);
        }

        item.setCheckable(true);
        item.setChecked(true);
        previousSelectedMenuItem = item;
    }

    private void refreshNavigationMenu() {
        navigationView.getMenu().clear();
        if (SessionManager.getBooleanSetting(HomeActivity.this, SESSION_IS_RESTAURANT_LOGGED_IN, false)) {
            navigationView.inflateMenu(R.menu.activity_main_drawer_logged_in);
        } else {
            navigationView.inflateMenu(R.menu.activity_main_drawer_guest);
        }

        checkNavigationMenuItem(navigationView.getMenu().findItem(SessionManager.getIntegerSetting(HomeActivity.this, SESSION_SELECTED_NAVIGATION_MENU, -1)));
    }

    private void handleNavigationItemChange(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            goFragmentScreen(getString(R.string.title_fragment_home), new HomeFragment());
            checkNavigationMenuItem(item);
        } else if (id == R.id.nav_location) {
            Intent intentAddress = new Intent(HomeActivity.this, LocationListActivity.class);
            startActivityForResult(intentAddress, INTENT_REQUEST_CODE_LOCATION_LIST);
        } else if (id == R.id.nav_notification) {
            Intent intentNotification = new Intent(HomeActivity.this, NotificationActivity.class);
            startActivity(intentNotification);
        } else if (id == R.id.nav_google_pay) {

        } else if (id == R.id.nav_add_restaurants) {
            Intent intent = new Intent(HomeActivity.this, RestaurantLoginActivity.class);
            startActivityForResult(intent, INTENT_REQUEST_CODE_ADD_RESTAURANT_LOGIN);
        } else if (id == R.id.nav_faq) {

        } else if (id == R.id.nav_privacy_policy) {

        } else if (id == R.id.nav_about_restaurant) {
            Intent intentRestaurant = new Intent(HomeActivity.this, AboutRestaurantActivity.class);
            startActivity(intentRestaurant);
        } else if (id == R.id.nav_menu) {
            Intent intentRestaurantMenu = new Intent(HomeActivity.this, RestaurantMenuListActivity.class);
            startActivity(intentRestaurantMenu);
        } else if (id == R.id.nav_social_activity) {

        } else if (id == R.id.nav_web_admin) {

        } else if (id == R.id.nav_marketing_tools) {

        } else if (id == R.id.nav_logout) {
            SessionManager.removeSetting(HomeActivity.this, SESSION_IS_RESTAURANT_LOGGED_IN);
            SessionManager.removeSetting(HomeActivity.this, SESSION_RESTAURANT_LOGIN_DATA);

            refreshNavigationMenu();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INTENT_REQUEST_CODE_ADD_RESTAURANT_LOGIN: {
                if (data != null && resultCode == RESULT_OK) {
                    if (data.getBooleanExtra(INTENT_KEY_LOGIN, false)) {

                        refreshNavigationMenu();

                        //Send user to the about restaurant due to successful registration
                        Intent intentRestaurant = new Intent(HomeActivity.this, AboutRestaurantActivity.class);
                        startActivity(intentRestaurant);
                    }
                }
                break;
            }
            case INTENT_REQUEST_CODE_LOCATION_LIST: {
                //Selected location is changed so new restaurant search is needed for new location, so notify home fragment
                ((HomeFragment) FragmentUtilsManager.getVisibleSupportFragment(HomeActivity.this, getString(R.string.title_fragment_home))).onActivityResult(requestCode, resultCode, data);
                break;
            }
            default:
                break;
        }
    }

    /***************************
     * Fabulous Filter methods *
     ***************************/
    @Override
    public void onResult(Object result) {
        Log.d(TAG, "onResult: " + result.toString());
        ((HomeFragment) FragmentUtilsManager.getVisibleSupportFragment(HomeActivity.this, getString(R.string.title_fragment_home))).onResult(result);
    }

    @Override
    public void onOpenAnimationStart() {
        Log.d("aah_animation", "onOpenAnimationStart: ");
    }

    @Override
    public void onOpenAnimationEnd() {
        Log.d("aah_animation", "onOpenAnimationEnd: ");
    }

    @Override
    public void onCloseAnimationStart() {
        Log.d("aah_animation", "onCloseAnimationStart: ");
    }

    @Override
    public void onCloseAnimationEnd() {
        Log.d("aah_animation", "onCloseAnimationEnd: ");
    }

    /*****************************************
     * Register device for push notification *
     *****************************************/
    private void initPushNotification() {
        if (NetworkManager.isConnected(HomeActivity.this)) {

            if (SessionManager.getBooleanSetting(HomeActivity.this, SESSION_IS_GCM_NOTIFICATION, true)) {
                new RegisterAppTask(HomeActivity.this, new GcmResultListener() {
                    @Override
                    public void onGcmResult(Object result) {
                        //Do whatever you want with the response
                    }
                }).execute();
            }
        }
    }
}

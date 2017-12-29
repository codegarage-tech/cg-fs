package com.rc.foodsignal.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.rc.foodsignal.R;
import com.rc.foodsignal.fragment.HomeFragment;
import com.rc.foodsignal.util.AllConstants;
import com.rc.foodsignal.util.AppUtils;
import com.rc.foodsignal.util.FragmentUtilsManager;
import com.reversecoder.library.storage.SessionManager;

import io.armcha.ribble.presentation.navigation.NavigationState;
import io.armcha.ribble.presentation.utils.extensions.ViewExKt;
import io.armcha.ribble.presentation.widget.AnimatedImageView;
import io.armcha.ribble.presentation.widget.AnimatedTextView;
import io.armcha.ribble.presentation.widget.ArcView;
import io.armcha.ribble.presentation.widget.navigation_view.NavigationDrawerView;
import io.armcha.ribble.presentation.widget.navigation_view.NavigationId;
import io.armcha.ribble.presentation.widget.navigation_view.NavigationItem;
import io.armcha.ribble.presentation.widget.navigation_view.NavigationItemSelectedListener;

import static com.rc.foodsignal.util.AllConstants.INTENT_KEY_LOGIN;
import static com.rc.foodsignal.util.AllConstants.INTENT_REQUEST_CODE_ADD_RESTAURANT_LOGIN;
import static com.rc.foodsignal.util.AllConstants.SESSION_IS_USER_LOGGED_IN;
import static com.rc.foodsignal.util.AllConstants.SESSION_SELECTED_RIBBLE_MENU;
import static com.rc.foodsignal.util.AllConstants.SESSION_USER_DATA;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class HomeActivity extends BaseActivity {

    private String TRANSLATION_X_KEY = "TRANSLATION_X_KEY";
    private String CARD_ELEVATION_KEY = "CARD_ELEVATION_KEY";
    private String SCALE_KEY = "SCALE_KEY";

    Toolbar toolbar;
    NavigationDrawerView navDrawerView;
    DrawerLayout drawerLayout;
    CardView contentHome;
    public ArcView arcMenuView;
    public AnimatedImageView arcMenuImage;
    AnimatedTextView toolbarTitle;

    //User info into menu
//    ImageView userAvatar;
//    TextView userName;
//    TextView userInfo;
//    UserBasicInfo userBasicInfo;

    private boolean isArcIcon = false;
    private boolean isDrawerOpened = false;
    private String activeTitle = "";
    private NavigationState navigationState = null;
    private int currentNavigationSelectedItem = 0;

    private String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();

        if (isArcIcon || isDrawerOpened) {
            setArcArrowState();
        } else {
            setArcHamburgerIconState();
        }
        setToolBarTitle(activeTitle);
//        updateUserInfo();

        if (savedInstanceState == null && navDrawerView != null) {
            handleMenuItemsChanges(navDrawerView.getItemList().get(0));
        }
    }

    private void initViews() {

        initToolBar();

        navDrawerView = (NavigationDrawerView) findViewById(R.id.navigation_drawer_view);
//        userAvatar = (ImageView) navDrawerView.getHeader().findViewById(R.id.userAvatar);
//        userName = (TextView) navDrawerView.getHeader().findViewById(R.id.userName);
//        userInfo = (TextView) navDrawerView.getHeader().findViewById(R.id.userInfo);
//
//        if (!AppUtils.isNullOrEmpty(SessionManager.getStringSetting(HomeActivity.this, SESSION_USER_BASIC_INFO))) {
//            Log.d(TAG, "Session data: " + SessionManager.getStringSetting(HomeActivity.this, SESSION_USER_BASIC_INFO));
//            userBasicInfo = UserBasicInfo.getResponseObject(SessionManager.getStringSetting(HomeActivity.this, SESSION_USER_BASIC_INFO), UserBasicInfo.class);
//        }

        contentHome = (CardView) findViewById(R.id.mainView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navDrawerView.setNavigationItemSelectListener(new NavigationItemSelectedListener() {
            @Override
            public void onNavigationItemSelected(final NavigationItem item) {
                if (!getNavigatorState().getActiveTag().equalsIgnoreCase(item.getId().getName())) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            handleMenuItemsChanges(item);
                        }
                    }, AllConstants.NAVIGATION_DRAWER_CLOSE_DELAY);
                }
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        drawerLayout.setDrawerElevation(0f);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                float moveFactor = navDrawerView.getWidth() * slideOffset;
                contentHome.setTranslationX(moveFactor);
                ViewExKt.setScale(contentHome, 1 - slideOffset / 4);
                contentHome.setCardElevation(slideOffset * AppUtils.toPx(HomeActivity.this, 10));

                //Restaurant owner status
                if (!SessionManager.getBooleanSetting(HomeActivity.this, SESSION_IS_USER_LOGGED_IN, false)) {
                    visibleMenuItems(false);
                } else {
                    visibleMenuItems(true);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                handleDrawerOpen();

                //Restaurant owner status
                if (!SessionManager.getBooleanSetting(HomeActivity.this, SESSION_IS_USER_LOGGED_IN, false)) {
                    visibleMenuItems(false);
                } else {
                    visibleMenuItems(true);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                handleDrawerClose();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        });

        drawerLayout.setScrimColor(Color.TRANSPARENT);
    }

    private void initToolBar() {
        toolbarTitle = (AnimatedTextView) findViewById(R.id.toolbarTitle);
        arcMenuImage = (AnimatedImageView) findViewById(R.id.arcImage);
        arcMenuView = (ArcView) findViewById(R.id.arcView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbarTitle.setAnimatedText(getString(R.string.title_activity_home), 0L);
        arcMenuImage.setAnimatedImage(R.drawable.arrow_left, 0L);
        arcMenuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void handleMenuItemsChanges(NavigationItem item) {
        //Store current ribble menu in session
        SessionManager.setStringSetting(HomeActivity.this, SESSION_SELECTED_RIBBLE_MENU, item.getId().getName());

        int checkPosition = -1;

        if (item.getName().equalsIgnoreCase(NavigationId.HOME.INSTANCE.getName())) {
            saveNavigatorState(new NavigationState(item.getName(), toolbarTitle.getText().toString(), false));
            checkPosition = 0;
            checkNavigationItem(checkPosition);
            setToolBarTitle(item.getName());
            goFragmentScreen(item.getName(), new HomeFragment());
        } else if (item.getName().equalsIgnoreCase(NavigationId.LOCATION.INSTANCE.getName())) {
            Intent intentAddress = new Intent(HomeActivity.this, LocationListActivity.class);
            startActivity(intentAddress);
        } else if (item.getName().equalsIgnoreCase(NavigationId.NOTIFICATION.INSTANCE.getName())) {
            checkPosition = 2;
            Toast.makeText(HomeActivity.this, getString(R.string.toast_under_development), Toast.LENGTH_SHORT).show();
        } else if (item.getName().equalsIgnoreCase(NavigationId.PAYMENT_CARD.INSTANCE.getName())) {
            checkPosition = 3;
            Toast.makeText(HomeActivity.this, getString(R.string.toast_under_development), Toast.LENGTH_SHORT).show();
        } else if (item.getName().equalsIgnoreCase(NavigationId.RESTAURANT.INSTANCE.getName())) {
            if (!SessionManager.getBooleanSetting(HomeActivity.this, SESSION_IS_USER_LOGGED_IN, false)) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivityForResult(intent, INTENT_REQUEST_CODE_ADD_RESTAURANT_LOGIN);
            } else {
                Intent intentRestaurant = new Intent(HomeActivity.this, RestaurantListActivity.class);
                startActivity(intentRestaurant);
            }
        } else if (item.getName().equalsIgnoreCase(NavigationId.MENU.INSTANCE.getName())) {
            checkPosition = 5;
            Toast.makeText(HomeActivity.this, getString(R.string.toast_under_development), Toast.LENGTH_SHORT).show();
        } else if (item.getName().equalsIgnoreCase(NavigationId.PROFILE.INSTANCE.getName())) {
            checkPosition = 6;
            Toast.makeText(HomeActivity.this, getString(R.string.toast_under_development), Toast.LENGTH_SHORT).show();
        } else if (item.getName().equalsIgnoreCase(NavigationId.LOG_OUT.INSTANCE.getName())) {
            SessionManager.removeSetting(HomeActivity.this, SESSION_IS_USER_LOGGED_IN);
            SessionManager.removeSetting(HomeActivity.this, SESSION_USER_DATA);

            visibleMenuItems(false);
        }
    }

    private void visibleMenuItems(boolean isVisible) {
        if (isVisible) {
            navDrawerView.getRecyclerView().findViewHolderForAdapterPosition(5).itemView.setVisibility(View.VISIBLE);
            navDrawerView.getRecyclerView().findViewHolderForAdapterPosition(6).itemView.setVisibility(View.VISIBLE);
            navDrawerView.getRecyclerView().findViewHolderForAdapterPosition(7).itemView.setVisibility(View.VISIBLE);
        } else {
            navDrawerView.getRecyclerView().findViewHolderForAdapterPosition(5).itemView.setVisibility(View.GONE);
            navDrawerView.getRecyclerView().findViewHolderForAdapterPosition(6).itemView.setVisibility(View.GONE);
            navDrawerView.getRecyclerView().findViewHolderForAdapterPosition(7).itemView.setVisibility(View.GONE);
        }
    }

    private void goFragmentScreen(String currentTag, Fragment fragment) {
        FragmentUtilsManager.changeSupportFragment(HomeActivity.this, fragment, currentTag);
    }

    public void setArcArrowState() {
        arcMenuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        arcMenuImage.setAnimatedImage(R.drawable.arrow_left, 0L);
    }

    public void setArcHamburgerIconState() {
        arcMenuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        arcMenuImage.setAnimatedImage(R.drawable.hamb, 0L);
    }

    public String getToolbarTitle() {
        return toolbarTitle.getText().toString();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void checkNavigationItem(int position) {
        if (currentNavigationSelectedItem != position) {
            currentNavigationSelectedItem = position;
            navDrawerView.setChecked(currentNavigationSelectedItem);
        }
    }

    public void hideNavigationItem(int position) {

//        navDrawerView.getRecyclerView().get
    }

    public void setToolBarTitle(String title) {
        activeTitle = title;
        toolbarTitle.setAnimatedText(title, 0L);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putFloat(TRANSLATION_X_KEY, contentHome.getTranslationX());
            outState.putFloat(CARD_ELEVATION_KEY, ViewExKt.getScale(contentHome));
            outState.putFloat(SCALE_KEY, contentHome.getCardElevation());
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        if (savedState != null) {
            contentHome.setTranslationX(savedState.getFloat(TRANSLATION_X_KEY));
            ViewExKt.setScale(contentHome, savedState.getFloat(CARD_ELEVATION_KEY));
            contentHome.setCardElevation(savedState.getFloat(SCALE_KEY));
        }
    }

    public void saveNavigatorState(NavigationState state) {
        this.navigationState = state;

        if (isArcIcon) {
            isArcIcon = false;
            setArcHamburgerIconState();
        }
    }

    public NavigationState getNavigatorState() {
        return navigationState;
    }

    public void handleDrawerOpen() {
        if (!isArcIcon)
            setArcArrowState();
        isDrawerOpened = true;
    }

    public void handleDrawerClose() {
        if (!isArcIcon && isDrawerOpened)
            setArcHamburgerIconState();
        isDrawerOpened = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INTENT_REQUEST_CODE_ADD_RESTAURANT_LOGIN: {
                if (data != null && resultCode == RESULT_OK) {
                    if (data.getBooleanExtra(INTENT_KEY_LOGIN, false)) {
                        visibleMenuItems(true);

                        Intent intentRestaurant = new Intent(HomeActivity.this, RestaurantListActivity.class);
                        startActivity(intentRestaurant);
                    } else {
                        visibleMenuItems(true);
                    }
                }
                break;
            }
            default:
                break;
        }
    }

//    public void updateUserInfo() {
//        Glide
//                .with(HomeActivity.this)
//                .load(R.mipmap.ic_launcher_round)
//                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
//                .apply(new RequestOptions().circleCropTransform())
//                .into(userAvatar);
//        userName.setText(getString(R.string.app_name));
//        userInfo.setText(getString(R.string.app_version_name));
//    }
}
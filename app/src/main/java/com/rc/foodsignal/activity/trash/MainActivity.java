package com.rc.foodsignal.activity.trash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rc.foodsignal.R;
import com.rc.foodsignal.activity.LoginActivity;

public class MainActivity extends AppCompatActivity {

    Button btnLogin, btnFoodMenu;
    Button addFoodItem, btnSignup;
    Button btnRegisterDevice, btnSearchRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        btnFoodMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent foodMenuIntent = new Intent(MainActivity.this, FoodMenuActivity.class);
                startActivity(foodMenuIntent);
            }
        });

        addFoodItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addfoodItemIntent = new Intent(MainActivity.this, AddFoodItemActivity.class);
                startActivity(addfoodItemIntent);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });

        btnRegisterDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerDeviceIntent = new Intent(MainActivity.this, RegisterDeviceActivity.class);
                startActivity(registerDeviceIntent);
            }
        });

        btnSearchRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchRestaurantIntent = new Intent(MainActivity.this, SearchRestaurantActivity.class);
                startActivity(searchRestaurantIntent);
            }
        });
    }

    private void initView(){

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnFoodMenu = (Button) findViewById(R.id.btn_foodMenu);
        addFoodItem = (Button) findViewById(R.id.btn_addfoodItem);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnRegisterDevice = (Button) findViewById(R.id.btn_registerDevice);
        btnSearchRestaurant = (Button) findViewById(R.id.btn_searchRestaurant);
    }

}

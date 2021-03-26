package com.example.saktikrupafurniture;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ShopInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_info);
        getSupportActionBar().hide();
    }
}
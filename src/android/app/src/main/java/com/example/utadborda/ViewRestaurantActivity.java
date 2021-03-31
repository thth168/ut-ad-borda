package com.example.utadborda;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewRestaurantActivity extends AppCompatActivity {


    private ImageView restaurantPicture;
    private TextView restaurantName;
    private TextView restaurantAddress;
    private TextView restaurantPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_restaurant);

        restaurantPicture =  findViewById(R.id.restaurant_image);
        restaurantName = findViewById(R.id.restaurant_name);
        restaurantPhone = findViewById(R.id.restaurant_phone);
        restaurantAddress = findViewById(R.id.restaurant_address);

    }
}
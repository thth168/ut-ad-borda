package com.example.utadborda;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewRestaurantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_restaurant);
        ImageView restaurantPicture = findViewById(R.id.restaurant_image);
        TextView restaurantName = findViewById(R.id.restaurant_name);
        TextView restaurantPhone = findViewById(R.id.restaurant_phone);
        TextView restaurantAddress = findViewById(R.id.restaurant_address);
    }
}

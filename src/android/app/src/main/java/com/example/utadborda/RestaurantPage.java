package com.example.utadborda;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.utadborda.models.RestaurantItem;

import org.w3c.dom.Text;

public class RestaurantPage extends AppCompatActivity {
    RestaurantItem restaurantItem;
    TextView name;
    TextView phone;
    TextView address;
    ImageView imageView;
    TextView website;
    TextView latitude;
    TextView longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_page);
        Bundle bundle = getIntent().getExtras();
        restaurantItem = new RestaurantItem(
                bundle.getString("id"),
                bundle.getString("name"),
                bundle.getString("phone"),
                bundle.getString("address"),
                bundle.getString("imageUrl"),
                bundle.getString("website"),
                bundle.getDouble("latitude"),
                bundle.getDouble("longitude")
        );
        name = findViewById(R.id.name);
        name.setText(restaurantItem.getName());
        phone = findViewById(R.id.phone);
        phone.setText(restaurantItem.getPhone());
        address = findViewById(R.id.address);
        address.setText(restaurantItem.getAddress());
        imageView = findViewById(R.id.imageView);
        Glide.with(this).load(restaurantItem.getImageUrl()).into(imageView);
        website = findViewById(R.id.website);
        website.setText(restaurantItem.getWebsite());
        latitude = findViewById(R.id.latitude);
        latitude.setText(String.valueOf(restaurantItem.getLatitude()));
        longitude = findViewById(R.id.longitude);
        longitude.setText(String.valueOf(restaurantItem.getLongitude()));
    }
}

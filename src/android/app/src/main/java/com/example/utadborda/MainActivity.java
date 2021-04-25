package com.example.utadborda;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View activityView = findViewById(R.id.main_pane);
        Button restaurantListButton = (Button) findViewById(R.id.restaurantList_button);
        Button sessionButton = (Button) findViewById(R.id.match_button);
        restaurantListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RestaurantListActivity.class));
            }
        });
        sessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SessionActivity.class));
            }
        });
    }
}

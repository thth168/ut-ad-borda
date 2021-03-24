package com.example.utadborda;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utadborda.ui.login.LoginActivity;

import static android.util.Log.*;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button loginButton;
    private Button restaurantListButton;
    private Button matchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button) findViewById(R.id.login_button);
        restaurantListButton = (Button) findViewById(R.id.restaurantList_button);
        matchButton = (Button) findViewById(R.id.match_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
               startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        restaurantListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RestaurantListActivity.class));
            }
        });
        matchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MatchActivity.class));
            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();
        //TODO: IMPLEMENT onStop
    }
}

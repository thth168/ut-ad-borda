package com.example.utadborda;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.example.utadborda.models.RestaurantItem;
import com.example.utadborda.networking.Fetcher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MatchActivity extends AppCompatActivity {
    private MatchCardFragment matchCardFragment;
    private ArrayList<String> restaurantIds;
    private ArrayList<RestaurantItem> restaurantQueue;
    private RestaurantItem currentRestaurant;
    private ArrayList<String> swipeLeft;
    private ArrayList<String> swipeRight;
    private String playerName = "";
    private Long playerCount;
    private String sessionKey;
    private DatabaseReference sessionRef;
    private DatabaseReference restaurantRef;
    private DataSnapshot restaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        ImageButton buttonLike = (ImageButton) findViewById(R.id.buttonLike);
        ImageButton buttonDislike = (ImageButton) findViewById(R.id.buttonDislike);
        matchCardFragment = (MatchCardFragment) getSupportFragmentManager().findFragmentById(R.id.matchingCard);
        readyImageButton(buttonLike, R.color.greenMint, R.color.greenMint_dark, true);
        readyImageButton(buttonDislike, R.color.red, R.color.red_dark, false);
        swipeLeft = new ArrayList<>();
        swipeRight = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playerName = extras.getString("playerName");
            playerCount = extras.getLong("playerCount");
            sessionKey = extras.getString("sessionName");
            sessionRef = database.getReference("sessions/" + sessionKey);
            restaurantRef = sessionRef.child("/restaurants");
        }
        addRoomsEventListener();
    }

    private void addRoomsEventListener() {
        sessionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> restaurants = snapshot.child("/restaurants").getChildren();
                restaurantIds = new ArrayList<>();
                for(DataSnapshot dataSnapshot : restaurants) {
                    restaurantIds.add(dataSnapshot.getKey());
                }
                restaurantQueue = new ArrayList<>();
                for (String restaurantID : restaurantIds) {
                    AsyncTask<String,?,RestaurantItem> restaurantTask = new Fetcher.AsyncFetchTask();
                    try {
                        restaurantQueue.add(restaurantTask.execute(restaurantID).get());
                    } catch (Exception e) {
                        return;
                    }
                }
                if (restaurantQueue.size() != 0) {
                    Collections.shuffle(restaurantQueue);
                    currentRestaurant = restaurantQueue.remove(0);
                    matchCardFragment.setData(currentRestaurant);
                } else {
                    endMatching();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MatchActivity.this, "Database error occured", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void swipe(boolean right) {
        if (right) {
            swipeRight.add(currentRestaurant.getId());
            String restaurantId = currentRestaurant.getId();
            addRestaurantIncrementEventListener(restaurantId);
        } else {
            swipeLeft.add(currentRestaurant.getId());
        }
        if (restaurantQueue.size() != 0) {
            currentRestaurant = restaurantQueue.remove(0);
            matchCardFragment.setData(currentRestaurant);
        } else {
            endMatching();
        }
    }

    private void endMatching() {
        Intent intent = new Intent(getApplicationContext(), MatchEndActivity.class);
        intent.putExtra("sessionKey", sessionKey);
        intent.putExtra("playerCount", playerCount);
        intent.putExtra("playerName", playerName);
        intent.putStringArrayListExtra("restaurantIds", restaurantIds);
        intent.putStringArrayListExtra("swipeLeft", swipeLeft);
        intent.putStringArrayListExtra("swipeRight", swipeRight);
        startActivity(intent);
        finish();
    }

    private void addRestaurantIncrementEventListener(final String restaurantId){
        restaurantRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot restaurantMatches = snapshot.child(restaurantId);
                int currentMatches = restaurantMatches.getValue(Integer.class);
                restaurantRef.child(restaurantId).setValue(currentMatches + 1);
                currentRestaurant.setSwipes(currentMatches + 1);
                Log.i("MatchActiviity", String.valueOf(currentRestaurant.getSwipes()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void readyImageButton(
            final ImageButton button,
            final int colorLight,
            final int colorDark,
            final boolean swipeValue
    ) {
        button.getBackground().setColorFilter(
                getResources().getColor(colorLight),
                PorterDuff.Mode.SRC_ATOP
        );
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageButton view = (ImageButton) v;
                        view.getBackground().setColorFilter(
                                getResources().getColor(colorDark),
                                PorterDuff.Mode.SRC_ATOP
                        );
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        swipe(swipeValue);
                    }
                    case MotionEvent.ACTION_CANCEL: {
                        ImageButton view = (ImageButton) v;
                        button.getBackground().setColorFilter(
                                getResources().getColor(colorLight),
                                PorterDuff.Mode.SRC_ATOP
                        );
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });
    }
}

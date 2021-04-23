package com.example.utadborda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import com.example.utadborda.models.RestaurantItem;
import com.example.utadborda.models.RestaurantItemAdapter;
import com.example.utadborda.networking.Fetcher;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MatchEndActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private String sessionKey;
    private Long playerCount;
    private String playerName;
    private ArrayList<String> swipeLeft;
    private ArrayList<String> swipeRight;
    private DatabaseReference sessionRef;
    private DatabaseReference restaurantRef;
    private List<Pair<RestaurantItem, Integer>> KVPair;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_end);
        recyclerView = (RecyclerView) findViewById(R.id.lv_restaurantList);
        recyclerView.setHasFixedSize(true);
        database = FirebaseDatabase.getInstance();
        extras = getIntent().getExtras();
        if (extras != null) {
            sessionKey = extras.getString("sessionKey");
            playerCount = extras.getLong("playerCount");
            playerName = extras.getString("playerName");
            swipeLeft = extras.getStringArrayList("swipeLeft");
            swipeRight = extras.getStringArrayList("swipeRight");
            sessionRef = database.getReference("sessions/" + sessionKey);
            restaurantRef = sessionRef.child("/restaurants");
        }
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        AsyncTask<?,?,?> restaurantTask = new AsyncFetchTask();
        restaurantTask.execute();
    }

    private void sortLikes(List<Pair<RestaurantItem, Integer>> sessionLikes) {
        Collections.sort(sessionLikes, new Comparator<Pair<RestaurantItem, Integer>>() {
            @Override
            public int compare(Pair<RestaurantItem, Integer> o1, Pair<RestaurantItem, Integer> o2) {
                if (o1.second > o2.second) {
                    return -1;
                } else if (o1.second.equals(o2.second)) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
    }

    private class AsyncFetchTask extends AsyncTask<Object, Void, List<RestaurantItem>> {
        @Override
        protected List<RestaurantItem> doInBackground(Object... params) {
            return Fetcher.fetchRestaurants("");
        }

        @Override
        protected void onPostExecute(List<RestaurantItem> restaurantItems) {
            recyclerView.setAdapter(new RestaurantItemAdapter(restaurantItems, true, MatchEndActivity.this));
        }
    }

}
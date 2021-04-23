package com.example.utadborda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import com.example.utadborda.models.RestaurantItem;
import com.example.utadborda.models.RestaurantItemAdapter;
import com.example.utadborda.networking.Fetcher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MatchEndActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<String> restaurantIds;
    private ArrayList<RestaurantItem> restaurantQueue;
    private DatabaseReference restaurantRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_end);
        recyclerView = (RecyclerView) findViewById(R.id.lv_restaurantList);
        restaurantQueue = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String sessionKey = extras.getString("sessionKey");
            Long playerCount = extras.getLong("playerCount");
            String playerName = extras.getString("playerName");
            ArrayList<String> swipeLeft = extras.getStringArrayList("swipeLeft");
            ArrayList<String> swipeRight = extras.getStringArrayList("swipeRight");
            restaurantIds = extras.getStringArrayList("restaurantIds");
            DatabaseReference sessionRef = database.getReference("sessions/" + sessionKey);
            restaurantRef = sessionRef.child("/restaurants");
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        Log.i("MatchEnd - IDS", String.valueOf(restaurantIds));
        for (final String restaurantID : restaurantIds) {
            AsyncTask<String,?,RestaurantItem> restaurantTask = new Fetcher.AsyncFetchTask();
            try {
                final RestaurantItem restaurant = restaurantTask.execute(restaurantID).get();
                restaurantRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int index = restaurantQueue.indexOf(restaurant);
                        restaurant.setSwipes(snapshot.child(restaurantID).getValue(Integer.class));
                        restaurantQueue.set(index, restaurant);
                        recyclerView.getAdapter().notifyItemChanged(index);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
                restaurantQueue.add(restaurant);
                Log.i("MatchEnd- restaurantQue", String.valueOf(restaurantQueue));
            } catch (Exception e) {
                Log.e("Matchend - e", String.valueOf(e));
            }
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new RestaurantItemAdapter(restaurantQueue, true, MatchEndActivity.this));
        /*AsyncTask<?,?,?> restaurantTask = new AsyncFetchTask();
        restaurantTask.execute();*/
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

        }
    }

}

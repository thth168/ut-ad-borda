package com.example.utadborda;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MatchEndActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private String sessionKey;
    private Long playerCount;
    private String playerName;
    private ArrayList<String> swipeLeft;
    private ArrayList<String> swipeRight;
    private DatabaseReference sessionRef;
    private DatabaseReference restaurantRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_end);
        database = FirebaseDatabase.getInstance();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sessionKey = extras.getString("sessionKey");
            playerCount = extras.getLong("playerCount");
            playerName = extras.getString("playerName");
            swipeLeft = extras.getStringArrayList("swipeLeft");
            swipeRight = extras.getStringArrayList("swipeRight");
            sessionRef = database.getReference("sessions/" + sessionKey);
            restaurantRef = sessionRef.child("/restaurants");
        }
    }


    private void sortLikes(List<Pair<String, Integer>> sessionLikes) {
        Collections.sort(sessionLikes, new Comparator<Pair<String, Integer>>() {
            @Override
            public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {
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
}

package com.example.utadborda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.utadborda.models.RestaurantItem;
import com.example.utadborda.models.Tag;
import com.example.utadborda.models.TagItemAdapter;
import com.example.utadborda.networking.Fetcher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class WaitingRoomActivity extends AppCompatActivity {
    private ListView userListView;
    private GridView tagListView;
    private Button mSubmitButton;
    private TextView mSessionKey;
    private String playerName = "";
    private String sessionKey= "";
    private Long playerCount;
    private int waitingCount;
    private List<String> userList;
    private List<Tag> tagList;
    private FirebaseDatabase database;
    private DatabaseReference sessionRef;
    private DatabaseReference restaurantRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
        userListView = (ListView) findViewById(R.id.user_list);
        tagListView = (GridView) findViewById(R.id.tag_gridView);
        mSubmitButton = (Button) findViewById(R.id.submit_button);
        mSessionKey = (TextView) findViewById(R.id.session_key_text);
        database = FirebaseDatabase.getInstance();
        userList = new ArrayList<>();
        tagList =  new ArrayList<Tag>();
        tagList.add(new Tag("Burger"));
        tagList.add(new Tag("Fries"));
        tagList.add(new Tag("Pizza"));
        tagList.add(new Tag("Healthy"));
        tagList.add(new Tag("Sushi"));
        tagList.add(new Tag("Noodles"));
        tagList.add(new Tag("Keto"));
        tagList.add(new Tag("Vegan"));
        tagList.add(new Tag("Vegetarian"));
        tagList.add(new Tag("Fast Food"));
        tagList.add(new Tag("Desert"));
        tagList.add(new Tag("Coffee and Tea"));
        tagList.add(new Tag("Alcohol"));
        tagList.add(new Tag("Indian"));
        tagList.add(new Tag("Comfort Food"));
        //playerName = preferences.getString("playerName", "");


//        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
//            @Override
//            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
////                if(isChecked){
//                    Log.i("WaitingRoom", String.valueOf(checkedId));
////                }
//            }
//        });

        ArrayAdapter adapter = new TagItemAdapter(WaitingRoomActivity.this, android.R.layout.simple_list_item_1, tagList);
        tagListView.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playerName = extras.getString("playerName");
            playerCount = extras.getLong("playerCount");
            sessionKey = extras.getString("sessionName");
            waitingCount = extras.getInt("waitingCount");
            database.getReference("sessions/"+ sessionKey + "/players/player-" + playerCount).setValue(playerName);
            mSessionKey.setText(sessionKey);
        }
        sessionRef = database.getReference("sessions/"+ sessionKey);
        restaurantRef = sessionRef.child("/restaurants");
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waitingCount = waitingCount-1;
                database.getReference("sessions/"+ sessionKey).child("waiting-for-players").setValue(waitingCount);
                if (waitingCount <= 0){
                    AsyncTask<?,?,List<RestaurantItem>> restaurantTask = new AsyncFetchTask();
                    try {
                        restaurantTask.execute().get();
                    } catch (Exception e) {
                        return;
                    }

                    Intent intent = new Intent(getApplicationContext(), MatchActivity.class);
                    intent.putExtra("sessionName", sessionKey);
                    intent.putExtra("playerCount", playerCount);
                    intent.putExtra("playerName", playerName);
                    startActivity(intent);
                    finish();
                }

//                startSessionEventListener();

            }
        });
        addRoomsEventListener();


    }

    private void addRoomsEventListener() {
        sessionRef = database.getReference("sessions/" + sessionKey);
        sessionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //show list of users
                userList.clear();
                Iterable<DataSnapshot> users = snapshot.child("/players").getChildren();
                Iterable<DataSnapshot> tags = snapshot.child("/tags").getChildren();
                for(DataSnapshot dataSnapshot : users) {
                    userList.add(dataSnapshot.getValue().toString());
                }
                ArrayAdapter adapter = new ArrayAdapter<String>(WaitingRoomActivity.this, android.R.layout.simple_list_item_1, userList);
                userListView.setAdapter(adapter);

//                fillViewList(userList, userListView, users);
//                fillViewList();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //error - nothing

            }
        });
    }


    private class AsyncFetchTask extends AsyncTask<Object, Void, List<RestaurantItem>> {
        @Override
        protected List<RestaurantItem> doInBackground(Object... params) {
            return Fetcher.fetchRestaurants();
        }

        @Override
        protected void onPostExecute(final List<RestaurantItem> restaurantItems) {
            sessionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(RestaurantItem item : restaurantItems){
                        Log.i("Restaurant", item.getName());
                        restaurantRef = sessionRef.child("restaurants").child(item.getId());
                        if(!snapshot.child("restaurants").child(item.getId()).exists()){
                            restaurantRef.setValue(0);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        }
    }
}

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.utadborda.models.RestaurantItem;
import com.example.utadborda.models.Tag;
import com.example.utadborda.networking.Fetcher;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class WaitingRoomActivity extends AppCompatActivity {
    private ListView userListView;
    private LinearLayout tagListView;
    private Button mSubmitButton;
    private TextView mSessionKey;
    private MaterialButtonToggleGroup toggleGroup;
    private String toggledTagText;
    private String tagQuery = "?";
    private String playerName = "";
    private String sessionKey= "";
    private Long playerCount;
    private int waitingCount;
    private List<String> userList;
    private List<Tag> tagList;
    private List<String> userTags;
    private FirebaseDatabase database;
    private DatabaseReference sessionRef;
    private DatabaseReference restaurantRef;
    private List<RestaurantItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
        toggleGroup = (MaterialButtonToggleGroup) findViewById(R.id.MaterialButtonToggleGroup1);
        userListView = (ListView) findViewById(R.id.user_list);

//        View view = (View) findViewById(R.id.toggle_group);
        tagListView = (LinearLayout) findViewById(R.id.tag_gridView);
        mSubmitButton = (Button) findViewById(R.id.submit_button);
        mSessionKey = (TextView) findViewById(R.id.session_key_text);
        database = FirebaseDatabase.getInstance();
        userList = new ArrayList<>();
        userTags = new ArrayList<>();
        tagList =  new ArrayList<Tag>();
        tagList.add(new Tag("Hamburger", "218e0238-83ed-46e4-9d4d-0f18597a8d67"));
        tagList.add(new Tag("Fries", "775d92d3-1a51-4e19-8585-84ca26e9a440"));
        tagList.add(new Tag("Pizza", "4099c172-2eb4-4b48-9db5-f33b460e9e7c"));
        tagList.add(new Tag("Healthy", "35542744-e728-48c9-beef-30d55e19a1ca"));
        tagList.add(new Tag("Sushi", "9f7553eb-4902-484d-b901-91a4324b9583"));
        tagList.add(new Tag("Noodles", "b2fd7be5-fd04-4ef1-b302-69594142796e"));
        tagList.add(new Tag("Keto", "0570b5c0-d0e1-47a8-aa15-43b977d9faa1"));
        tagList.add(new Tag("Vegan", "dde6bd9b-ab13-4992-be11-7d6fd9b66595"));
        tagList.add(new Tag("Vegetarian", "61a8fa1b-0a2e-4232-b828-0a6f9d4c75bc"));
        tagList.add(new Tag("Fast Food", "b4eab4a0-2489-49ca-b197-d8b21f432043"));
        tagList.add(new Tag("Dessert", "f48da935-7ba1-4b40-ac69-5cd096330d5a"));
        tagList.add(new Tag("Coffee and Tea", "1b9cd157-eebe-45f7-8970-f34f25e7c059"));
        tagList.add(new Tag("Alcohol", "0f81ba44-e94c-4a1c-a750-a1d56439d833"));
        tagList.add(new Tag("Indian", "3702f847-3c6e-45e4-904e-042613aa47c3"));
        tagList.add(new Tag("Comfort Food", "2750344b-a0a0-4bdd-a445-f9a0c3ccde82"));
        //playerName = preferences.getString("playerName", "");



        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, final int checkedId, final boolean isChecked) {
                Log.i("isChecked", String.valueOf(isChecked));
//                if(isChecked){
                    toggledTagText = (String) ((MaterialButton) findViewById(checkedId)).getText();
//                    userTags.add(toggledTagText);

                    sessionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int tagCount;
                            Log.i("isChecked - datachnage", String.valueOf(isChecked));
                            if (isChecked) {
                                toggledTagText = (String) ((MaterialButton) findViewById(checkedId)).getText();
                                userTags.add(toggledTagText);


                                if (snapshot.child(toggledTagText).exists()) {
                                    tagCount = snapshot.child("tags").child(toggledTagText).getValue(Integer.class);
                                } else {
                                    tagCount = 1;
                                }
                                sessionRef.child("tags").child(toggledTagText).setValue(tagCount);
                            } else {
//                                Toast.makeText(WaitingRoomActivity.this, "Unchecked tag", Toast.LENGTH_SHORT).show();
//                                Log.i("isChecked false", "something is happening here");
                                tagCount = snapshot.child("tags").child(toggledTagText).getValue(Integer.class);
                                sessionRef.child("tags").child(toggledTagText).setValue(tagCount-1);
                                userTags.remove(toggledTagText);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
        });

//        ArrayAdapter adapter = new TagItemAdapter(WaitingRoomActivity.this, android.R.layout.simple_list_item_1, tagList);
//        tagListView.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playerName = extras.getString("playerName");
            playerCount = extras.getLong("playerCount");
            sessionKey = extras.getString("sessionName");
            waitingCount = extras.getInt("waitingCount");
            database.getReference("sessions/"+ sessionKey + "/players/player-" + playerCount).setValue(playerName);
            sessionRef = database.getReference("sessions/"+ sessionKey);
            restaurantRef = sessionRef.child("/restaurants");
            mSessionKey.setText(sessionKey);
        }



        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Collect tags
                //waiting for players
                if(waitingCount > 0){
                    waitingCount = waitingCount-1;
                    database.getReference("sessions/"+ sessionKey).child("waiting-for-players").setValue(waitingCount);
                }
                //if all players have chosen
//                String query = "?";
//                try{
//                    Iterable<DataSnapshot> userTags = database.getReference("sessions/" + sessionKey + "/tags").get().getResult().getChildren();
//
//                for (DataSnapshot s: userTags) {
//                    if(s.getValue(Integer.class) > 0){
//                        for (Tag tag : tagList) {
//                            if (tag.getTagName().equals(s)) {
//                                query += "tag=" + tag.getTagId() + "&";
//                                break;
//                            }
//                        }
//                    }
//                }
//                } catch (Exception e){
//                    Log.e("Firebase Error", String.valueOf(e));
//                    Toast.makeText(WaitingRoomActivity.this, "Error!", Toast.LENGTH_SHORT).show();
//                }

                if (waitingCount == 0) {
                    getTagsFromDatabase(tagList);
                }

//                startSessionEventListener();

            }
        });
        addRoomsEventListener();


    }

    private void getTagsFromDatabase(final List<Tag> tagList) {
        sessionRef = database.getReference("sessions/"+ sessionKey);
//        sessionRef.child("tags").get().getResult().getChildren();
        sessionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> playerTags = snapshot.child("tags").getChildren();
                for(DataSnapshot s : playerTags){
                    Log.i("Tags", s.getKey());
                    if(s.getValue(Integer.class) > 0){
                        for (Tag tag : tagList) {
                            Log.i("Tags", s.getKey());
                            if (tag.getTagName().equals(s.getKey())) {
                                Log.i("Tags sem komast 'i gegn", tag.getTagName());
                                tagQuery += "tag=" + tag.getTagId() + "&";
                                break;
                            }
                        }
                    }
                }
                try {
                    AsyncTask<String,?,List<RestaurantItem>> restaurantTask = new AsyncFetchTask();
                    Log.i("jæja", tagQuery);
                    restaurantTask.execute(tagQuery).get();
                } catch (Exception e) {
                    Toast.makeText(WaitingRoomActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(getApplicationContext(), MatchActivity.class);
                intent.putExtra("sessionName", sessionKey);
                intent.putExtra("playerCount", playerCount);
                intent.putExtra("playerName", playerName);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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


    /**
     * Fetches restaurant data from API asyncronously
     * Sets data in RecyclerView
     */
    private class AsyncFetchTask extends AsyncTask<String, Void, List<RestaurantItem>> {
        /**
         *
         * @param params
         * @return
         */
        @Override
        protected List<RestaurantItem> doInBackground(String... params) {
            return Fetcher.fetchRestaurants(params[0]);
        }

        /**
         * Bind data retrieved from API to RecyclerView
         * @param restaurantItems
         */
        @Override
        protected void onPostExecute(final List<RestaurantItem> restaurantItems) {
            // rsetaurantRef = database.getReference("sessions/" + sessionKey + "restaurants")
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
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
    }

}
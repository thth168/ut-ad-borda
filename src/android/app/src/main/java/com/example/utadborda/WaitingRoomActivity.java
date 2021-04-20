package com.example.utadborda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.example.utadborda.models.Tag;
import com.example.utadborda.models.TagItemAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WaitingRoomActivity extends AppCompatActivity {

    ListView userListView;
    GridView tagListView;

    String playerName = "";
    String sessionKey= "";
    Long playerCount;
    List<String> userList;
    List<Tag> tagList;

    FirebaseDatabase database;
    DatabaseReference sessionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
        userListView = (ListView) findViewById(R.id.user_list);
        tagListView = (GridView) findViewById(R.id.tag_gridView);
        database = FirebaseDatabase.getInstance();
        userList = new ArrayList<>();
        tagList =  new ArrayList<Tag>();
        tagList.add(new Tag("Hamburger"));
        tagList.add(new Tag("Pizza"));
        tagList.add(new Tag("Healthy"));
        tagList.add(new Tag("Sushi"));
        tagList.add(new Tag("Noodles"));
        tagList.add(new Tag("Vegan"));
        tagList.add(new Tag("Vegetarian"));
        tagList.add(new Tag("Fast food"));
        tagList.add(new Tag("Desert"));
        tagList.add(new Tag("Coffee and Tea"));
        tagList.add(new Tag("Alcohol"));
        tagList.add(new Tag("Indian"));
        tagList.add(new Tag("Comfort Food"));
        //playerName = preferences.getString("playerName", "");

        ArrayAdapter adapter = new TagItemAdapter(WaitingRoomActivity.this, android.R.layout.simple_list_item_1, tagList);
        tagListView.setAdapter(adapter);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playerName = extras.getString("playerName");
            playerCount = extras.getLong("playerCount");
            sessionKey = extras.getString("sessionName");
            sessionRef = database.getReference("sessions/"+ sessionKey + "/players/player-" + playerCount);
            sessionRef.setValue(playerName);
        }

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

//    private void fillViewList(List<String> list, ListView view, Iterable<DataSnapshot> data){
//        list.clear();
//        for(DataSnapshot dataSnapshot : data) {
//            list.add(dataSnapshot.getValue().toString());
//        }
//        ArrayAdapter adapter = new ArrayAdapter<String>(WaitingRoomActivity.this, android.R.layout.simple_list_item_1, list);
//        view.setAdapter(adapter);
//    }
}
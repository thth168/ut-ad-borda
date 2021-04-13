package com.example.utadborda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SessionActivity extends AppCompatActivity {

    private static final String TAG = "SessionActivity";

    EditText editText;
    Button mNewSession;
    ListView listView;

    List<String> roomsList;

    String sessionKey = "halló!";
    String playerName = "";
    String roomName = "";
    int playerNo = 1;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseDatabase database;
    private DatabaseReference sessionRef;
    private DatabaseReference sessionsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        Bundle bundle = new Bundle();


//        readData();
        editText = findViewById(R.id.sessionKey);
        mNewSession = findViewById(R.id.new_session);
        listView = findViewById(R.id.play_button);
        roomsList = new ArrayList<>();
//
        database = FirebaseDatabase.getInstance();
        sessionRef = database.getReference("message");
        sessionRef.setValue(sessionKey);
        //check if the player exists and get reference

        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        sessionKey = preferences.getString("sessionKey", "");
        Log.i("SessionActivity", sessionKey);
//        sessionRef.setValue(sessionKey, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(DatabaseError databaseError, DatabaseReference dataReference) {
//
//            }
//        });
        if(!sessionKey.equals("")) {
            sessionRef = database.getReference("sessions/"+ sessionKey);
            addEventListener();
            sessionRef.child("session").setValue(sessionKey);
        }

        mNewSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionKey = editText.getText().toString();
                Log.i("SessionActivity", sessionKey);
                editText.setText("");
                if(!sessionKey.equals("")){
                    mNewSession.setText("Creating room");
                    playerName = "player" +playerNo;
                    sessionRef = database.getReference("sessions/"+ sessionKey + "/" + playerName );
                    playerNo++;
                    sessionRef.setValue(sessionKey);
                    //addRoomEventListener();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                roomName = roomsList.get(position);
                sessionRef = database.getReference("sessions/" + sessionKey + "/player" + playerNo);
                playerNo++;
                Intent intent = new Intent(getApplicationContext(), MatchActivity.class);
                intent.putExtra("roomName", roomName);
                intent.putExtra("playerName", playerName);
                startActivity(intent);
                //addRoomEventListener(roomName);
                sessionRef.setValue("");
            }
        });

        addRoomsEventListener();
    }

    public void readData(){
        FirebaseDatabase database;
        DatabaseReference sessionRef;
        database = FirebaseDatabase.getInstance();
        sessionRef = database.getReference();
        
        sessionRef.setValue(sessionKey);

        sessionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class).toString();
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void addRoomEventListener(){
        sessionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mNewSession.setText("CREATE ROOM");
                mNewSession.setEnabled(true);
                Intent intent = new Intent(getApplicationContext(), MatchActivity.class);
                intent.putExtra("roomName", sessionKey);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mNewSession.setText("Create a room");
                mNewSession.setEnabled(true);
                Toast.makeText(SessionActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
        //Show if new room is available
        //addRoomsEventListener();
    }

    private void addEventListener(){
        sessionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //success - continue to the next screen after saving the session name
                if(!sessionKey.equals("")){
                    SharedPreferences preferences = getSharedPreferences("PREFS", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("sessionKey", sessionKey);
                    editor.apply();

                    //startActivity(new Intent(getApplicationContext(), MatchActivity.class));
                    //finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mNewSession.setText("Create a room");
                mNewSession.setEnabled(true);
                Toast.makeText(SessionActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addRoomsEventListener() {
        sessionRef = database.getReference().child("sessions");
        sessionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //show list of rooms
                roomsList.clear();
                Iterable<DataSnapshot> rooms = snapshot.getChildren();
                for(DataSnapshot dataSnapshot : rooms) {
                    roomsList.add(dataSnapshot.getKey());

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(SessionActivity.this, android.R.layout.simple_list_item_1, roomsList);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //error - nothing

            }
        });
    }
}
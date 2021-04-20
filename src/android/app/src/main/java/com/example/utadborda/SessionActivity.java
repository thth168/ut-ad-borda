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
import java.util.Random;

public class SessionActivity extends AppCompatActivity {

    private static final String TAG = "SessionActivity";

    EditText sessionText;
    EditText nameText;
    Button mNewSession;
    Button mPlayButton;

    String sessionKey = "";
    String playerName = "";
    Boolean newSession = false;

    private FirebaseDatabase database;
    private DatabaseReference sessionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        nameText = findViewById(R.id.nickName);
        sessionText = findViewById(R.id.sessionKey);
        mNewSession = findViewById(R.id.new_session);
        mPlayButton = findViewById(R.id.play_button);

        database = FirebaseDatabase.getInstance();
        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        sessionKey = preferences.getString("sessionKey", "");

        mNewSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionKey = randomString();
                playerName = nameText.getText().toString();
                sessionText.setText("");
                if(!sessionKey.equals("")) {
                    mNewSession.setText("Creating room");
                    sessionRef = database.getReference( "sessions/" );
                    newSession = true;
                    addRoomEventListener();
                }
            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionKey = sessionText.getText().toString();
                sessionRef = database.getReference("sessions/");
                addRoomEventListener();
            }
        });
//        addRoomEventListener();
    }

    private void addRoomEventListener(){
        sessionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            // Check if session exists, add player and go to next activity
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("RoomEvent- new session", String.valueOf(newSession));
                Log.i("RoomEvent - snapshot", String.valueOf(snapshot.child(sessionKey).exists()));
//                if(snapshot.child(sessionKey).exists()){
                if(snapshot.child(sessionKey).exists() || newSession){
                    newSession = false;
                    long playerCount = snapshot.child(sessionKey).getChildrenCount();
                    playerName = nameText.getText().toString();
                    sessionRef = database.getReference("sessions/"+ sessionKey + "/player-" + playerCount);
                    sessionRef.setValue(playerName);

                    Intent intent = new Intent(getApplicationContext(), MatchActivity.class);
                    intent.putExtra("sessionName", sessionKey);
                    intent.putExtra("playerCount", playerCount);
                    intent.putExtra("playerName", playerName);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SessionActivity.this, "Invalid room key", Toast.LENGTH_SHORT).show();
                }
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

                    startActivity(new Intent(getApplicationContext(), waitingRoomFragment.class));
                    finish();
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
//
//    private void addRoomsEventListener() {
//        sessionRef = database.getReference().child("sessions");
//        sessionRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                //show list of rooms
//                roomsList.clear();
//                Iterable<DataSnapshot> rooms = snapshot.getChildren();
//                for(DataSnapshot dataSnapshot : rooms) {
//                    roomsList.add(dataSnapshot.getKey());
//                    ArrayAdapter<String> adapter = new ArrayAdapter<>(SessionActivity.this, android.R.layout.simple_list_item_1, roomsList);
//                    listView.setAdapter(adapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                //error - nothing
//            }
//        });
//    }

    public String randomString() {

        // create a string of uppercase and lowercase characters and numbers
        String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        // combine all strings
        String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;

        // create random string builder
        StringBuilder sb = new StringBuilder();

        // create an object of Random class
        Random random = new Random();

        // specify length of random string
        int length = 4;

        for(int i = 0; i < length; i++) {

            // generate random index number
            int index = random.nextInt(alphaNumeric.length());

            // get character specified by index
            // from the string
            char randomChar = alphaNumeric.charAt(index);

            // append the character to string builder
            sb.append(randomChar);
        }

        String randomString = sb.toString();
        return randomString;
    }
}
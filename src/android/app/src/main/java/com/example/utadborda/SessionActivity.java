package com.example.utadborda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        final RequestQueue randomStringRequestQueue = Volley.newRequestQueue(this);
        mNewSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://ut-ad-borda.herokuapp.com/api/random-3-words";
                mNewSession.setText("Creating room");
                StringRequest randomStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response);
                        sessionKey = response.replaceAll(" ", "");
                        playerName = nameText.getText().toString();
                        sessionText.setText("");
                        if(!sessionKey.equals("")) {
                            sessionRef = database.getReference( "sessions/" );
                            newSession = true;
                            addRoomEventListener();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage());
                    }
                });
                randomStringRequestQueue.add(randomStringRequest);
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
                    long playerCount = snapshot.child(sessionKey).child("/players").getChildrenCount();
                    playerName = nameText.getText().toString();
                    sessionRef = database.getReference("sessions/"+ sessionKey + "/players/player-" + playerCount);
                    sessionRef.setValue(playerName);

                    Intent intent = new Intent(getApplicationContext(), WaitingRoomActivity.class);
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
}
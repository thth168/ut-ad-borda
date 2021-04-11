package com.example.utadborda;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MatchActivity extends AppCompatActivity {

    private Button leftButton;
    Button rightButton;

    String playerName = "";
    String sessionName = "";
    String role = "";
    String message = "";

    FirebaseDatabase database;
    DatabaseReference messageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        leftButton = (Button) findViewById(R.id.left_button);
        rightButton = (Button) findViewById(R.id.right_button);

        database = FirebaseDatabase.getInstance();

        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        //playerName = preferences.getString("playerName", "");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playerName = extras.getString("playerName");
            sessionName = extras.getString("roomName");
            if (playerName.equals("player1")){
                role = "host";
            } else {
                role = "guest";
            }
        }

        messageRef = database.getReference().child("sessions").child(sessionName);
        message = role + ": poked!";
        messageRef.child("message").setValue(message);
        addRoomEventListener();

    }

    private void addRoomEventListener() {
        messageRef.child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                //message received
                if(role.equals("host")){
                    if(snapshot.getValue(String.class).contains("guest")){
                        leftButton.setEnabled(false);
                        Toast.makeText(MatchActivity.this, "" +
                                snapshot.getValue(String.class).replace("guest", ""), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(snapshot.getValue(String.class).contains("host")){
                        leftButton.setEnabled(true);
                        Toast.makeText(MatchActivity.this, "" +
                                snapshot.getValue(String.class).replace("host", ""), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //error - retry
                messageRef.setValue(message);
            }
        });
    }
}
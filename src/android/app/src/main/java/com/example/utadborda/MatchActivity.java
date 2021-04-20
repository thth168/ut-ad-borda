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
import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class MatchActivity extends AppCompatActivity {
    private ImageButton button_dislike;
    private ImageButton button_like;

    String playerName = "";
    String sessionKey= "";
    Long playerCount;
    String role = "";
    String message = "";

    FirebaseDatabase database;
    DatabaseReference sessionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        database = FirebaseDatabase.getInstance();

        SharedPreferences preferences = getSharedPreferences("PREFS", 0);

        //playerName = preferences.getString("playerName", "");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playerName = extras.getString("playerName");
            playerCount = extras.getLong("playerCount");
            sessionKey = extras.getString("sessionName");
            sessionRef = database.getReference("sessions/"+ sessionKey + "/player-" + playerCount);
            sessionRef.setValue(playerName);
        }

//        messageRef = database.getReference().child("sessions").child(sessionName);
//        message = role + ": poked!";
//        messageRef.child("message").setValue(message);
//        addRoomEventListener();

//    }
//
//    private void addRoomEventListener() {
//        messageRef.child("message").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NotNull DataSnapshot snapshot) {
//                //message received
//                if(role.equals("host")){
//                    if(snapshot.getValue(String.class).contains("guest")){
//                        leftButton.setEnabled(false);
//                        Toast.makeText(MatchActivity.this, "" +
//                                snapshot.getValue(String.class).replace("guest", ""), Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    if(snapshot.getValue(String.class).contains("host")){
//                        leftButton.setEnabled(true);
//                        Toast.makeText(MatchActivity.this, "" +
//                                snapshot.getValue(String.class).replace("host", ""), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                //error - retry
//                messageRef.setValue(message);
//            }
//        });
//    }
//}
        button_dislike = (ImageButton) findViewById(R.id.button_dislike);
        button_like = (ImageButton) findViewById(R.id.button_like);
        readyImageButton(button_dislike, R.color.red, R.color.red_dark,false);
        readyImageButton(button_like, R.color.greenMint, R.color.greenMint_dark,true);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void readyImageButton(
            final ImageButton button,
            final int colorLight,
            final int colorDark,
            final boolean swipeValue
    ) {
        button.getBackground().setColorFilter(
                getResources().getColor(colorLight),
                PorterDuff.Mode.SRC_ATOP
        );
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageButton view = (ImageButton ) v;
                        view.getBackground().setColorFilter(
                                getResources().getColor(colorDark),
                                PorterDuff.Mode.SRC_ATOP
                        );
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        swipe(swipeValue);
                    }
                    case MotionEvent.ACTION_CANCEL: {
                        ImageButton view = (ImageButton) v;
                        button.getBackground().setColorFilter(
                                getResources().getColor(colorLight),
                                PorterDuff.Mode.SRC_ATOP
                        );
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });
    }



    private void swipe(boolean right) {

    }

}

package com.example.utadborda;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import java.util.List;

public class MatchActivity extends AppCompatActivity {
    List<String> userRightSwipes;
    List<String> userLeftSwipes;
    List<String> matchedIds;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        //session = new Session(session_key, this);
    }

    private boolean isMatch(String restaurantId) {
        if (matchedIds.contains(restaurantId)) {
            return true;
        }
        return false;
    }

    private void recieve(List<String> restaurantIds) {
        /*
        if () {

        } else {

        }
        */
    }

    private void onSwipe(boolean directionRight, String restaurantId) {
        // session get new swipes from users
        //session.
        // session notify swipe (direction)
        if (directionRight) {userRightSwipes.add(restaurantId);}
        else userLeftSwipes.add(restaurantId);

    }


}

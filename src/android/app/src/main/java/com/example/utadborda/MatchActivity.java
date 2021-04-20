package com.example.utadborda;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import androidx.fragment.app.FragmentContainerView;
import android.widget.RelativeLayout;
import java.util.Collections;
import java.util.List;
import com.example.utadborda.models.RestaurantItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;


public class MatchActivity extends AppCompatActivity {
    private RelativeLayout matchingCardContainer;
    private FragmentContainerView matchingCard;
    private RelativeLayout buttonContainer;
    private ImageButton buttonLike;
    private ImageButton buttonDislike;
    private MatchCardFragment matchCardFragment;

    private List<RestaurantItem> restaurantQueue;
    private RestaurantItem currentRestaurant;
    private List<String> swipeLeft;
    private List<String> swipeRight;

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
        matchingCardContainer = (RelativeLayout) findViewById(R.id.matchingCardContainer);
        matchingCard = (FragmentContainerView) findViewById(R.id.matchingCard);
        buttonContainer = (RelativeLayout) findViewById(R.id.buttonContainer);
        buttonLike = (ImageButton) findViewById(R.id.buttonLike);
        buttonDislike = (ImageButton) findViewById(R.id.buttonDislike);
        matchCardFragment = (MatchCardFragment) getSupportFragmentManager().findFragmentById(R.id.matchingCard);
        readyImageButton(buttonLike, R.color.greenMint, R.color.greenMint_dark, true);
        readyImageButton(buttonDislike, R.color.red, R.color.red_dark, false);


        // get restaurants for session
        //if (restaurantQueue.size() != 0) {
        //    Collections.shuffle(restaurantQueue);
        //    currentRestaurant = restaurantQueue.remove(0);
        //}
        // else { swiping done }
        // display currentRestaurant
        // matchCardFragment.setData();


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
    }


    public void swipe(boolean right) {
        //if (right) {
            //swipeRight.add(currentRestaurant.getId());
        //} else {
            //swipeLeft.add(currentRestaurant.getId());
        //}
        //if (restaurantQueue.size() != 0) {
        //    currentRestaurant = restaurantQueue.remove(0);
        //}
        //else {// matching finished return;}

        // display currentRestaurant
        // matchCardFragment.setData();

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
}

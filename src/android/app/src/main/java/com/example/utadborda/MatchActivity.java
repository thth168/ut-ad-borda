package com.example.utadborda;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
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

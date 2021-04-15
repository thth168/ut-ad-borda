package com.example.utadborda;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class TouchListener implements OnTouchListener {
    private GestureDetector gestureDetector;

    public TouchListener(Context context) {
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            TouchListener.this.onTap();
            return super.onSingleTapUp(event);
        }

        @Override
        public boolean onDoubleTap(MotionEvent event) {
            TouchListener.this.onDoubleTap();
            return super.onDoubleTap(event);
        }

        @Override
        public void onLongPress(MotionEvent event) {
            TouchListener.this.onLongPress();
            super.onLongPress(event);
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = event2.getY() - event1.getY();
                float diffX = event2.getX() - event1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            TouchListener.this.onSwipeRight();
                        } else {
                            TouchListener.this.onSwipeLeft();
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            TouchListener.this.onSwipeDown();
                        } else {
                            TouchListener.this.onSwipeUp();
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onTap() {}
    public void onDoubleTap() {}
    public void onLongPress() {}
    public void onSwipeRight() {}
    public void onSwipeLeft() {}
    public void onSwipeDown() {}
    public void onSwipeUp() {}
}

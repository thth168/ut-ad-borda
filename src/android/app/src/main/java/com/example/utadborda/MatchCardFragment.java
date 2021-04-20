package com.example.utadborda;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class MatchCardFragment extends Fragment {
    private MatchActivity parent;
    private ImageView restaurantImage;
    private RelativeLayout restaurantInfoContainer;
    private TextView restaurantName;
    private ImageView gpsIcon;
    private TextView restaurantPrice;
    private TextView restaurantDistance;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        parent = (MatchActivity) getActivity();
        restaurantImage = (ImageView) view.findViewById(R.id.restaurantImage);
        restaurantImage.setOnTouchListener(new TouchListener(parent.getApplicationContext()) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                parent.swipe(false);
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                parent.swipe(true);
            }
        });
        restaurantInfoContainer = (RelativeLayout) view.findViewById(R.id.restaurantInfoContainer);
        restaurantName = (TextView) view.findViewById(R.id.restaurantName);
        gpsIcon = (ImageView) view.findViewById(R.id.gpsIcon);
        restaurantPrice = (TextView) view.findViewById(R.id.restaurantPrice);
        restaurantDistance = (TextView) view.findViewById(R.id.restaurantDistance);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setData(
            int imageSource,
            String name,
            String price,
            String distance
    ) {
        restaurantImage.setImageResource(imageSource);
        restaurantName.setText(name);
        restaurantPrice.setText(price);
        restaurantDistance.setText(distance);
    }

}

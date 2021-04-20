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
import java.text.DecimalFormat;

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
            double longitude,
            double latitude
    ) {
        restaurantImage.setImageResource(imageSource);
        restaurantName.setText(name);
        restaurantPrice.setText(price);




        //restaurantDistance.setText(distance);
    }

    /**
     * @param distance - Distance in meters.
     * @return Returns string representation of distance with appropriate unit suffix.
     */
    public String generateDistanceString(double distance) {
        DecimalFormat df;
        if (distance <= 1000) return (int)distance + " meters away";
        df = new DecimalFormat("#.#");
        return df.format(distance/1000) + " kilometers away";
    }

    /**
     * @return distance between two points on the globe in meters.
     */
    private double calculateDistance(double lat1, double lat2, double lon1, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = (
                Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)
        );
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c * 1000;
    }
}

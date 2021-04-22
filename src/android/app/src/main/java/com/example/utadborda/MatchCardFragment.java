package com.example.utadborda;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.utadborda.models.RestaurantItem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

import static android.os.FileUtils.copy;

public class MatchCardFragment extends Fragment {
    private MatchActivity parent;
    private ImageView restaurantImage;
    private RelativeLayout restaurantInfoContainer;
    private TextView restaurantName;
    private ImageView gpsIcon;
    private TextView restaurantPrice;
    private TextView restaurantDistance;
    private static final int IO_BUFFER_SIZE = 8 * 1024;

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
        restaurantDistance = (TextView) view.findViewById(R.id.restaurantDistance);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static Bitmap loadBitmap(String url) throws IOException {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try {
            URL _url = new URL(url);
            in = new BufferedInputStream(_url.openStream(), IO_BUFFER_SIZE);

            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
            copy(in, out);
            out.flush();

            final byte[] data = dataStream.toByteArray();
            BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inSampleSize = 1;

            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        } catch (IOException e) {
            Log.e("image fetch error", e.getMessage());
        }
        return bitmap;
    }

    public void setData(RestaurantItem restaurantItem) {
        if (restaurantItem.getImageUrl() != "") {
            Glide.with(this).load(restaurantItem.getImageUrl()).into(restaurantImage);
        } else {
            restaurantImage.setImageResource(R.drawable.generic);
        }
        if (restaurantItem.getName() != null) {
            restaurantName.setText(restaurantItem.getName());
        }
        if (restaurantItem.getAddress() != "") {
            restaurantDistance.setText(restaurantItem.getAddress());
        } else {
            restaurantDistance.setText("");
        }
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

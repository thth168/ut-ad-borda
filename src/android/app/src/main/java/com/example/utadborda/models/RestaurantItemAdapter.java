package com.example.utadborda.models;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.utadborda.R;

import java.util.List;

public class RestaurantItemAdapter extends ArrayAdapter<RestaurantItem> {
    public RestaurantItemAdapter(Context context, List<RestaurantItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RestaurantItem restaurant = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_restaurant, parent, false);
        }
        TextView restaurantName = (TextView) convertView.findViewById(R.id.restaurant_name);
        TextView restaurantId = (TextView) convertView.findViewById(R.id.restaurant_id);
        TextView restaurantPhone = (TextView) convertView.findViewById(R.id.restaurant_phone);
        TextView restaurantAddress = (TextView) convertView.findViewById(R.id.restaurant_address);

        restaurantName.setText(restaurant.getName());
        restaurantId.setText(restaurant.getId());
        restaurantPhone.setText(restaurant.getPhone());
        restaurantAddress.setText(restaurant.getAddress());
        return convertView;
    }
}

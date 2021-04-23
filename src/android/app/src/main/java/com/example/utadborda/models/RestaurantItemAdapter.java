package com.example.utadborda.models;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.utadborda.R;
import com.example.utadborda.RestaurantPage;

import java.util.Collections;
import java.util.List;

public class RestaurantItemAdapter extends RecyclerView.Adapter<RestaurantItemAdapter.getViewHolder> {
    List<RestaurantItem> restaurantList = Collections.emptyList();
    Context context;
    boolean drawSwipes;

    public RestaurantItemAdapter(List<RestaurantItem> restaurantList, boolean swipe, Context context){
        this.drawSwipes = swipe;
        this.restaurantList = restaurantList;
        this.context = context;
    }

    @NonNull
    @Override
    public getViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_restaurant, parent, false);
        getViewHolder holder = new getViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull getViewHolder holder, final int position) {
        final RestaurantItem restaurant = restaurantList.get(position);
        holder.restaurantName.setText(restaurant.getName());
        if (restaurant.getPhone() == "null" || restaurant.getPhone() == null) {
            holder.restaurantPhone.setText("");
        } else {
            holder.restaurantPhone.setText(restaurant.getPhone());
        }
        holder.restaurantAddress.setText(restaurant.getAddress());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            /**
             * Display clicked restaurant information to user
             * @param v
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RestaurantPage.class);
                intent.putExtra("id", restaurant.getId());
                intent.putExtra("name", restaurant.getName());
                intent.putExtra("phone", restaurant.getPhone());
                intent.putExtra("address", restaurant.getAddress());
                intent.putExtra("imageUrl", restaurant.getImageUrl());
                intent.putExtra("website", restaurant.getWebsite());
                intent.putExtra("latitude", restaurant.getLatitude());
                intent.putExtra("longitude", restaurant.getLongitude());
                context.startActivity(intent);
            }
        });
        if (restaurant.getImageUrl() != "") {
            Glide.with(this.context).load(restaurant.getImageUrl()).into(holder.restaurantPicture);
        } else {
            holder.restaurantPicture.setImageResource(R.drawable.generic);
        }
        if (drawSwipes) {
            holder.swipeCount.setText(String.valueOf(restaurant.getSwipes()));
        } else {
            holder.swipeCount.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public class getViewHolder extends RecyclerView.ViewHolder{
        ImageView restaurantPicture;
        TextView restaurantName;
        TextView restaurantPhone;
        TextView restaurantAddress;
        TextView swipeCount;
        ConstraintLayout parentLayout;
        public getViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantPicture =  itemView.findViewById(R.id.restaurant_image);
            restaurantPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
            restaurantName = itemView.findViewById(R.id.restaurant_name);
            restaurantPhone = itemView.findViewById(R.id.restaurant_phone);
            restaurantAddress = itemView.findViewById(R.id.restaurant_address);
            swipeCount = itemView.findViewById(R.id.swipeCount);
            parentLayout = itemView.findViewById(R.id.oneLineRestaurantLayout);
        }
    }
}

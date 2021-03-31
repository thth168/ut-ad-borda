package com.example.utadborda.models;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.utadborda.R;
import com.example.utadborda.RestaurantFragment;

import java.util.Collections;
import java.util.List;

public class RestaurantItemAdapter extends RecyclerView.Adapter<RestaurantItemAdapter.getViewHolder> {
    List<RestaurantItem> restaurantList = Collections.emptyList();
    Context context;

    public RestaurantItemAdapter(List<RestaurantItem> restaurantList, Context context){
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
//        holder.restaurantId.setText(restaurant.getId());
        holder.restaurantPhone.setText(restaurant.getPhone());
        holder.restaurantAddress.setText(restaurant.getAddress());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, RestaurantFragment.class);
//                intent.putExtra("id", restaurant.getId());
//                context.startActivity(intent);
                Toast.makeText(context, "Clicked" + restaurant.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        Glide.with(this.context).load(restaurant.getImageUrl()).into(holder.restaurantPicture);
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public class getViewHolder extends RecyclerView.ViewHolder{
        ImageView restaurantPicture;
        TextView restaurantName;
        TextView restaurantId;
        TextView restaurantPhone;
        TextView restaurantAddress;
        ConstraintLayout parentLayout;

        public getViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantPicture =  itemView.findViewById(R.id.restaurant_image);
            restaurantName = itemView.findViewById(R.id.restaurant_name);
//            restaurantId = itemView.findViewById(R.id.restaurant_id);
            restaurantPhone = itemView.findViewById(R.id.restaurant_phone);
            restaurantAddress = itemView.findViewById(R.id.restaurant_address);
            parentLayout = itemView.findViewById(R.id.oneLineRestaurantLayout);
        }
    }

    public void setNewList(List<RestaurantItem> restaurantList){
        this.restaurantList = restaurantList;
        notifyDataSetChanged();
    }
}

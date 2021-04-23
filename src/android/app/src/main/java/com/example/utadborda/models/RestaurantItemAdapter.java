package com.example.utadborda.models;

import android.content.Context;
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
import java.util.Collections;
import java.util.List;

/**
 * Adapter class to bind data to RecyclerViewer in RestaurantListActivity
 */
public class RestaurantItemAdapter extends RecyclerView.Adapter<RestaurantItemAdapter.getViewHolder> {
    List<RestaurantItem> restaurantList = Collections.emptyList();
    Context context;

    /**
     * Constructor for restaurant lists to be added to the restaurant list display
     *
     * @param restaurantList list of items of type restaurantItem
     * @param context Activity context
     */
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

    /**
     * Set item values and onClick listener for listed items
     *
     * @param holder
     * @param position
     */
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
//                Intent intent = new Intent(context, RestaurantFragment.class);
//                intent.putExtra("id", restaurant.getId());
//                context.startActivity(intent);
                Toast.makeText(context, "Clicked" + restaurant.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        if (restaurant.getImageUrl() != "") {
            Glide.with(this.context).load(restaurant.getImageUrl()).into(holder.restaurantPicture);
        } else {
            holder.restaurantPicture.setImageResource(R.drawable.generic);
        }
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    /**
     * Initialize item variables to be bound later
     */
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
            restaurantPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
            restaurantName = itemView.findViewById(R.id.restaurant_name);
            restaurantPhone = itemView.findViewById(R.id.restaurant_phone);
            restaurantAddress = itemView.findViewById(R.id.restaurant_address);
            parentLayout = itemView.findViewById(R.id.oneLineRestaurantLayout);
        }
    }

}

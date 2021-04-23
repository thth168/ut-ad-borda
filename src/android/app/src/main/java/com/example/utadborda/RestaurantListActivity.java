package com.example.utadborda;

import android.os.AsyncTask;
import android.os.Bundle;
import com.example.utadborda.models.RestaurantItem;
import com.example.utadborda.models.RestaurantItemAdapter;
import com.example.utadborda.networking.Fetcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class RestaurantListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<RestaurantItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        recyclerView = (RecyclerView) findViewById(R.id.lv_restaurantList);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        items = new ArrayList<RestaurantItem>();
        AsyncTask<?,?,?> restaurantTask = new AsyncFetchTask();
        restaurantTask.execute();
    }

    private void addRestaurantsToList() {
        String TAG = "Restaurant List";
        Log.i(TAG, "Loaded items: " + items.size());
    }

    private class AsyncFetchTask extends AsyncTask<Object, Void, List<RestaurantItem>> {
        @Override
        protected List<RestaurantItem> doInBackground(Object... params) {
            return Fetcher.fetchRestaurants("");
        }

        @Override
        protected void onPostExecute(List<RestaurantItem> restaurantItems) {
            recyclerView.setAdapter(new RestaurantItemAdapter(restaurantItems, false, RestaurantListActivity.this));
            addRestaurantsToList();
        }
    }
}

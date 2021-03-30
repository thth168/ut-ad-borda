package com.example.utadborda;

import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.utadborda.models.RestaurantItem;
import com.example.utadborda.models.RestaurantItemAdapter;
import com.example.utadborda.networking.Fetcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class RestaurantListActivity extends AppCompatActivity {
    //Initialize variable
    ListView listView;
    private final String TAG = "Restaurant List";
    private List<RestaurantItem> items;
    ArrayAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        items = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list_view);
        listAdapter = new RestaurantItemAdapter(this, items);
        listView.setAdapter(listAdapter);
        AsyncTask<?,?,?> restaurantTask = new AsyncFetchTask();
        restaurantTask.execute();
    }

    private void addRestaurantsToList() {
        Log.i(TAG, "Loaded items: " + items.size());
    }

    private class AsyncFetchTask extends AsyncTask<Object, Void, List<RestaurantItem>> {
        @Override
        protected List<RestaurantItem> doInBackground(Object... params) {
            return Fetcher.fetchRestaurants();
        }
        @Override
        protected void onPostExecute(List<RestaurantItem> restaurantItems) {
            listAdapter.addAll(restaurantItems);
            addRestaurantsToList();
        }
    }
}
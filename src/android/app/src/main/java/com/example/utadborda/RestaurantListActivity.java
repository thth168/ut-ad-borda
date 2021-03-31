package com.example.utadborda;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.utadborda.models.RestaurantItem;
import com.example.utadborda.models.RestaurantItemAdapter;
import com.example.utadborda.networking.Fetcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class RestaurantListActivity extends AppCompatActivity {
    //Initialize variable
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    ListView listView;
    ListView restaurantListItem;
    private List<RestaurantItem> items;
    private ArrayAdapter listAdapter;

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
//        mAdapter = new RestaurantItemAdapter(items, RestaurantListActivity.this);
//        recyclerView.setAdapter(mAdapter);

        /*listView.setAdapter(listAdapter);
        restaurantListItem = (ListView) findViewById(R.id.list_view);
        listView.OnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(RestaurantListActivity.this, "Clicked on restaurant", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    private void addRestaurantsToList() {
        String TAG = "Restaurant List";
        Log.i(TAG, "Loaded items: " + items.size());
    }

    private class AsyncFetchTask extends AsyncTask<Object, Void, List<RestaurantItem>> {
        RestaurantItemAdapter restaurantAdapter;
        @Override
        protected List<RestaurantItem> doInBackground(Object... params) {
            return Fetcher.fetchRestaurants();
        }
        @Override
        protected void onPostExecute(List<RestaurantItem> restaurantItems) {
            recyclerView.setAdapter(new RestaurantItemAdapter(restaurantItems, RestaurantListActivity.this));
            addRestaurantsToList();
        }
    }
}
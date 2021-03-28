package com.example.utadborda;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RestaurantListActivity extends AppCompatActivity {
    //Initialize variable
    ListView listView;

    public ArrayList<String> Id = new ArrayList<String>();
    public ArrayList<String> Name = new ArrayList<String>();
    public ArrayList<String> Phone = new ArrayList<String>();
    public ArrayList<String> Address = new ArrayList<String>();
    public ArrayList<String> openTime = new ArrayList<String>();
    public ArrayList<String> closeTime = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        //assign
        listView = findViewById(R.id.list_view);

        String restaurants_array = "{\"restaurants\" : [\n" +
                "\t{ \"id\":\"1\", \"name\":\"Kaffihus Bakarans\",\"phone\":\"+354 456 4771\",\"address\":\"Ísafjörður, Iceland\", \"openTime\":\"07:00:00\",\"closeTime\":\"18:00:00\" },\n" +
                "\t{ \"id\":\"2\", \"name\":\"Brauð & Co\",\"phone\":\"+354 456 7777\",\"address\":\"Melhagi, Reykjavík, Iceland\", \"openTime\":\"07:00:00\",\"closeTime\":\"18:00:00\" },\n" +
                "\t{ \"id\":\"3\", \"name\":\"Sauðárkróksbakarí\",\"phone\":\"+354 455 5000\",\"address\":\"Aðalgata 5, Sauðárkrókur, \"openTime\":\"07:00:00\",\"closeTime\":\"18:00:00\" },\n" +
                "}]\n" +
                "}";

        try{
            JSONObject jsonObject = new JSONObject(restaurants_array);
            JSONArray jsonArray = jsonObject.getJSONArray("restaurants");
            for (int i=0; i<jsonArray.length(); i++ ){
                JSONObject object = jsonArray.getJSONObject(i);
                String restaurantID = object.getString("id");
                String restaurantName = object.getString("name");
                String restaurantPhone = object.getString("phone");
                String restaurantAddress = object.getString("address");
                String openTime = object.getString("openTime");
                String closeTime = object.getString("closeTime");
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        ArrayAdapter<String> restaurantAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, )

    }
}
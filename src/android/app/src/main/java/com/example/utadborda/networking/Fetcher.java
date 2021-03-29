package com.example.utadborda.networking;

import android.net.Uri;
import android.util.Log;

import com.example.utadborda.models.RestaurantItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Fetcher {
    private static void parseItems(List<RestaurantItem> items, JSONArray jsonBody)
            throws IOException, JSONException {
        for (int i = 0; i < jsonBody.length(); i++) {
            JSONObject restaurantJSONObject = jsonBody.getJSONObject(i);
            RestaurantItem restaurantItem = new RestaurantItem();

            restaurantItem.setId(restaurantJSONObject.getString("id"));
            restaurantItem.setName(restaurantJSONObject.getString("name"));
            restaurantItem.setAddress(restaurantJSONObject.getString("address"));
            restaurantItem.setPhone(restaurantJSONObject.getString("phone"));

            items.add(restaurantItem);
        }
    }

    public static byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = conn.getInputStream();
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(conn.getResponseMessage() + ": with " + urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            conn.disconnect();
        }
    }

    public static String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public static List<RestaurantItem> fetchRestaurants() {
        List<RestaurantItem> items = new ArrayList<RestaurantItem>();
        try {
            String url = Uri.parse("https://ut-ad-borda.herokuapp.com/api").buildUpon().build().toString();
            String jsonString = getUrlString(url);
            JSONArray jsonBody = new JSONArray(jsonString);
            parseItems(items, jsonBody);
            Log.i("Restaurant fetch", "JSON data loaded");
        } catch (IOException ioe) {
            Log.e("Restuarant fetch", "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e("Restaurant fetch", "Failed to parse JSON", je);
        }
        return items;
    }
}
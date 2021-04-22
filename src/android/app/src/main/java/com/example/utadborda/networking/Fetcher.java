package com.example.utadborda.networking;

import android.net.Uri;
import android.os.AsyncTask;
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

/**
 * Fetches JSON data from API and returns a list of RestaurantItems to be loaded into the RecyclerView
 */

public class Fetcher {
    /**
     * @param items Empty array to be filled with RestaurantItems
     * @param jsonBody List of JSON objects
     * @throws IOException
     * @throws JSONException
     */
    private static void parseItems(List<RestaurantItem> items, JSONArray jsonBody)
            throws IOException, JSONException {
        for (int i = 0; i < jsonBody.length(); i++) {
            JSONObject restaurantJSONObject = jsonBody.getJSONObject(i);
            RestaurantItem restaurantItem = new RestaurantItem();
            restaurantItem.setId(restaurantJSONObject.getString("id"));
            restaurantItem.setName(restaurantJSONObject.getString("name"));
            restaurantItem.setAddress(restaurantJSONObject.getString("address"));
            restaurantItem.setPhone(restaurantJSONObject.getString("phone"));

            List<String> photos = new ArrayList<>();
            JSONArray array = restaurantJSONObject.getJSONArray("photos");
            for (int j = 0; j < array.length(); j++) {
                photos.add(array.getString(j));
            }
            restaurantItem.setImageUrl(photos);

            items.add(restaurantItem);
            Log.i("Restaurant item:", "items: " + items);
        }
    }

    /**
     *
     * @param urlSpec
     * @return
     * @throws IOException
     */
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
        } catch (IOException exception) {
            Log.e("error", exception.getMessage());
        } finally {
            conn.disconnect();
        }
        return null;
    }

    public static String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    /**
     * Fetch restaurants from API asyncronously and send them to main thread
     * @return
     */
    public static List<RestaurantItem> fetchRestaurants() {
        List<RestaurantItem> items = new ArrayList<RestaurantItem>();
        try {
            String url = Uri.parse("https://ut-ad-borda.herokuapp.com/api/allRestaurants?limit=10&page=0").buildUpon().build().toString();
            String jsonString = getUrlString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
            JSONArray restaurants = (JSONArray) jsonBody.getJSONArray("restaurants");
            parseItems(items, restaurants);
            Log.i("Restaurant fetch", "JSON data loaded");
        } catch (IOException ioe) {
            Log.e("Restuarant fetch", "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e("Restaurant fetch", "Failed to parse JSON", je);
        }

        return items;
    }

    public static class AsyncFetchTask extends AsyncTask<String, Void, RestaurantItem> {
        @Override
        protected RestaurantItem doInBackground(String... strings) {
            return Fetcher.fetchRestaurant(strings[0]);
        }
    }

    public static RestaurantItem fetchRestaurant(String id) {
        RestaurantItem restaurantItem = new RestaurantItem();
        try {
            String url = Uri.parse("https://ut-ad-borda.herokuapp.com/api/restaurant?id=" + id).buildUpon().build().toString();
            String jsonString = getUrlString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
            restaurantItem.setId(jsonBody.getString("id"));
            restaurantItem.setName(jsonBody.getString("name"));
            restaurantItem.setPhone(jsonBody.getString("phone"));
            restaurantItem.setAddress(jsonBody.getString("address"));

            List<String> photos = new ArrayList<>();
            JSONArray array = jsonBody.getJSONArray("photos");
            for (int i = 0; i < array.length(); i++) {
                photos.add(array.getString(i));
            }
            restaurantItem.setImageUrl(photos);

            restaurantItem.setWebsite(jsonBody.getString("website"));
            restaurantItem.setLatitute(Double.parseDouble(jsonBody.getString("posLat")));
            restaurantItem.setLongitute(Double.parseDouble(jsonBody.getString("posLng")));
            Log.i("Restaurant fetch", "JSON data loaded");
        } catch (IOException ioe) {
            Log.e("Restuarant fetch", "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e("Restaurant fetch", "Failed to parse JSON", je);
        }
        return restaurantItem;
    }
}
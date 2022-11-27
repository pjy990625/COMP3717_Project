package org.techtown.comp3717_project;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.controls.ControlsProviderService;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONArray;
import org.json.JSONObject;
import org.techtown.comp3717_project.adapters.ItemsMyRecyclerAdapter;

import java.util.Collections;
import java.util.List;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.techtown.comp3717_project.adapters.ItemsMyRecyclerAdapter;
import org.techtown.comp3717_project.ui.history.HistoryFragment;
import org.techtown.comp3717_project.ui.setting.SettingFragment;
public class InfoActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    PlacesClient placesClient;
    HistoryFragment historyFragment = new HistoryFragment();
    SettingFragment settingFragment = new SettingFragment();
    BottomNavigationView bottomNavigationView;

    RecyclerView items, services;
    String[] itemNames, serviceNames;
    int[] dutyFreeIcons = {R.drawable.ic_baseline_smoking_rooms_24,
            R.drawable.ic_baseline_local_drink_24, R.drawable.ic_baseline_shopping_cart_24};
    int[] servicesIcons = {R.drawable.ic_baseline_car_rental_24, R.drawable.ic_baseline_hotel_24,
            R.drawable.ic_baseline_local_taxi_24};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Places.initialize(this, BuildConfig.MAPS_API_KEY);
        placesClient = Places.createClient(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String airportName = bundle.getString("Airport");
        String airportLocation = bundle.getString("Location");
        TextView name = findViewById(R.id.textViewAirportName);
        name.setText(airportName);
        TextView location = findViewById(R.id.textViewLocationCode);
        location.setText(airportLocation);
        String url = bundle.getString("url");
        PhotoAsyncTask asyncTask = new PhotoAsyncTask();
        asyncTask.execute(url);
        items = findViewById(R.id.recyclerViewItems);
        services = findViewById(R.id.recyclerViewServices);
        itemNames = getResources().getStringArray(R.array.items);
        serviceNames = getResources().getStringArray(R.array.services);
        ItemsMyRecyclerAdapter itemsMyRecyclerAdapter1 = new ItemsMyRecyclerAdapter(this,
                itemNames, dutyFreeIcons);
        ItemsMyRecyclerAdapter itemsMyRecyclerAdapter2 = new ItemsMyRecyclerAdapter(this,
                serviceNames, servicesIcons);
        items.setAdapter(itemsMyRecyclerAdapter1);
        items.setLayoutManager(new LinearLayoutManager(this.getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false));
        services.setAdapter(itemsMyRecyclerAdapter2);
        services.setLayoutManager(new LinearLayoutManager(this.getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false));
    }

    class PhotoAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            RequestQueue queue = Volley.newRequestQueue(InfoActivity.this);
            final String[] placeID = {""};
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, strings[0], null, (Response.Listener<JSONObject>) response -> {
                try {
                    JSONArray jsonArrayResults = response.getJSONArray("results");
                    JSONObject jsonObjectResults = jsonArrayResults.getJSONObject(0);
                    placeID[0] = jsonObjectResults.getString("place_id");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            final List<Place.Field> fields = Collections.singletonList(Place.Field.PHOTO_METADATAS);
            final FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeID[0], fields);
            placesClient.fetchPlace(placeRequest).addOnSuccessListener((fetchPlaceResponse -> {
                final Place place = fetchPlaceResponse.getPlace();
                final List<PhotoMetadata> metadata = place.getPhotoMetadatas();
                if (metadata == null || metadata.isEmpty()) {
                    Log.w(TAG, "No metadata found.");
                }

                final PhotoMetadata photoMetadata = metadata.get(0);
                final String attributions = photoMetadata.getAttributions();
                final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxWidth(500).setMaxHeight(300).build();
                placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse -> {
                    Bitmap bitmap = fetchPhotoResponse.getBitmap();
                    ImageView imageView = findViewById(R.id.imageView);
                    imageView.setImageBitmap(bitmap);
                })).addOnFailureListener((e -> {
                    if (e instanceof ApiException) {
                        final ApiException exception = (ApiException) e;
                        Log.e(TAG, "Place not found: " + exception.getMessage());
                    }
                }));
            }));
            final List<Place.Field> fields1 = Collections.singletonList(Place.Field.RATING);
            final FetchPlaceRequest placeRequest1 = FetchPlaceRequest.newInstance(placeID[0], fields1);
            placesClient.fetchPlace(placeRequest1).addOnSuccessListener(fetchPlaceResponse -> {
                final Place place = fetchPlaceResponse.getPlace();
                try {
                    final double rating = place.getRating();
                    RatingBar ratingBar = findViewById(R.id.ratingBar);
                    ratingBar.setRating((float) rating);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }).addOnFailureListener(e -> {
                if (e instanceof ApiException) {
                    final ApiException exception = (ApiException) e;
                    Log.e(TAG, "Place not found: " + exception.getMessage());
                }
            });
            }, error -> Log.e(TAG, error.toString()));
            queue.add(request);
            return "Task Completed";
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.navigation_home) {
            Intent switchActivityIntent = new Intent(this, MainActivity.class);
            startActivity(switchActivityIntent);
            return true;
        } else if (item.getItemId() == R.id.navigation_history) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.info_container, historyFragment).commit();
            return true;
        } else if (item.getItemId() == R.id.navigation_setting) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.info_container, settingFragment).commit();
            return true;
        }
        return false;
    }
}
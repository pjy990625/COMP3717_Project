package org.techtown.comp3717_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Location;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchActivity extends AppCompatActivity {

    PlacesClient placesClient;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        EditText input = findViewById(R.id.editTextAirportName);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if(!keyword.isEmpty()) {
                    try {
                        updateAirportList(AmadeusManager.getManager().getAirports(keyword));
                    } catch (ResponseException e) {
                        Log.d("Amadeus", e.toString());
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    void updateAirportList(Location[] locations) throws ResponseException, ExecutionException, InterruptedException {
        ArrayList<String> listItems = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        ListView list = findViewById(R.id.airportList);
        list.setAdapter(adapter);

        for (Location location : locations) {
            adapter.add(location.getName());
        }

        list.setOnItemClickListener((adapterView, view1, i, l) -> {
            Intent intent = new Intent(adapterView.getContext(), InfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("Airport", locations[i].getName() + " ("
                    + locations[i].getIataCode() + ")");
            MapAsyncTask task = new MapAsyncTask();
            try {
                List<Address> addresses = task.execute(locations[i].getIataCode() + " airport").get();
                if (addresses.size() == 0) {
                    Toast.makeText(this, "Airport is either private or doesn't exist.",
                            Toast.LENGTH_SHORT).show();
                }
                else if (addresses.get(0).getSubThoroughfare() != null) {
                    bundle.putString("Location", addresses.get(0).getSubThoroughfare() + " "
                            + addresses.get(0).getThoroughfare() + ", " +
                            addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea()
                            + ", " + addresses.get(0).getCountryName());
                } else if (addresses.get(0).getThoroughfare() != null) {
                    bundle.putString("Location", addresses.get(0).getThoroughfare() + ", " +
                            addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea()
                            + ", " + addresses.get(0).getCountryName());
                } else {
                    if (addresses.get(0).getPostalCode() != null) {
                        if (addresses.get(0).getLocality() != null) {
                            bundle.putString("Location", addresses.get(0).getPostalCode() + " " +
                                    addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea()
                                    + ", " + addresses.get(0).getCountryName());
                        } else if (addresses.get(0).getSubLocality() != null) {
                            bundle.putString("Location", addresses.get(0).getPostalCode() + " " +
                                    addresses.get(0).getSubLocality() + ", " + addresses.get(0).getAdminArea()
                                    + ", " + addresses.get(0).getCountryName());
                        }
                    } else {
                        if (addresses.get(0).getLocality() != null) {
                            bundle.putString("Location",
                                    addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea()
                                    + ", " + addresses.get(0).getCountryName());
                        } else if (addresses.get(0).getSubLocality() != null) {
                            bundle.putString("Location",
                                    addresses.get(0).getSubLocality() + ", " + addresses.get(0).getAdminArea()
                                    + ", " + addresses.get(0).getCountryName());
                        } else {
                            bundle.putString("Location", locations[i].getAddress().getCityName() + ", " +
                                    locations[i].getAddress().getCountryName());
                        }
                    }
                }
                url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
                        + addresses.get(0).getLatitude() + ", " + addresses.get(0).getLongitude()
                        + "&key=" + BuildConfig.MAPS_API_KEY;
                PhotoAsyncTask task2 = new PhotoAsyncTask();
                task2.execute(url);
                intent.putExtras(bundle);
                intent.putExtras(bundle);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            startActivity(intent);
        });
    }

    class MapAsyncTask extends AsyncTask<String, LatLng, List<Address>> {
        @Override
        protected List<Address> doInBackground(String... name) {
            List<Address> addresses = new ArrayList<>();
            Geocoder geocoder = new Geocoder(getApplicationContext());
            try {
                addresses = geocoder.getFromLocationName(name[0], 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }
    }
    class PhotoAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            RequestQueue queue = Volley.newRequestQueue(SearchActivity.this);
            final String[] placeID = {new String()};
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, strings[0], null, (Response.Listener<JSONObject>) response -> {
                try {
                    JSONArray jsonArrayResults = response.getJSONArray("results");
                    JSONObject jsonObjectResults = jsonArrayResults.getJSONObject(0);
                    placeID[0] = jsonObjectResults.getString("place_id");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, error -> Toast.makeText(SearchActivity.this, error.toString(), Toast.LENGTH_SHORT).show());
            queue.add(request);
            return placeID[0];
        }
    }
}
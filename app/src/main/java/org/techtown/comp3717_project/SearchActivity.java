package org.techtown.comp3717_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.amadeus.Amadeus;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Location;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.techtown.comp3717_project.ui.history.HistoryFragment;
import org.techtown.comp3717_project.ui.setting.SettingFragment;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
public class SearchActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    String url;
    PlacesClient placesClient;
    BottomNavigationView bottomNavigationView;
    Amadeus amadeus;
    HistoryFragment historyFragment = new HistoryFragment();
    SettingFragment settingFragment = new SettingFragment();
    private SearchActivity myActivity = this;
    private String apiurl = "https://www.airport-data.com/api/ap_info.json?iata=";
    private String link = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnItemSelectedListener(this);

        amadeus = Amadeus
                .builder(BuildConfig.API_KEY, BuildConfig.API_SECRET)
                .build();

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
                    searchAirports(keyword);
                }
            }
        });
    }

    private void searchAirports(String keyword) {
        new Thread() {
            public void run() {
                try {
                    Location locations[] = AmadeusManager.getManager().getAirports(keyword);
                    myActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                updateAirportList(locations);
                            } catch (ResponseException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (ResponseException e) {
                    Log.d("Amadeus", e.toString());
                }
            }
        }.start();
    }

    void updateAirportList(Location[] locations) throws ResponseException, ExecutionException, InterruptedException {
        ArrayList<String> listItems = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
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
                bundle.putString("url", url);
                String tempUrl = apiurl + locations[i].getIataCode();
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute(tempUrl);
                bundle.putString("Link", link);
                intent.putExtras(bundle);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            startActivity(intent);
        });
    }

    private class MyAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            RequestQueue queue = Volley.newRequestQueue(myActivity);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, strings[0], null, response -> {
                try {
                    link = response.getString("link");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> Toast.makeText(myActivity, error.toString(), Toast.LENGTH_SHORT).show());
            queue.add(request);
            return null;
        }
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.navigation_home) {
            Intent switchActivityIntent = new Intent(this, MainActivity.class);
            startActivity(switchActivityIntent);
            return true;
        } else if (item.getItemId() == R.id.navigation_history) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.search_container, historyFragment).commit();
            return true;
        } else if (item.getItemId() == R.id.navigation_setting) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.search_container, settingFragment).commit();
            return true;
        }
        return false;
    }

}
package org.techtown.comp3717_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amadeus.resources.ItineraryPriceMetric;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.comp3717_project.ui.compare.EnterTicketFragment;
import org.techtown.comp3717_project.ui.compare.ViewTicketFragment;
import org.techtown.comp3717_project.ui.history.HistoryFragment;
import org.techtown.comp3717_project.ui.setting.SettingFragment;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

public class CompareActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {


    private EnterTicketFragment fragmentEnterTicket;
    private ViewTicketFragment fragmentViewTicket;
    BottomNavigationView bottomNavigationView;
    HistoryFragment historyFragment = new HistoryFragment();
    SettingFragment settingFragment = new SettingFragment();

    String departure_name;
    String destination_name;
    String flight_date;

    final int MAX_HISTORY = 10; // maximum number of records

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnItemSelectedListener(this);

        fragmentEnterTicket = new EnterTicketFragment();
        fragmentViewTicket = new ViewTicketFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ticket_fragment_frame,fragmentEnterTicket)
                .commit();
    }

    public void submitTravelInfo(double price, ItineraryPriceMetric[] results) {
        Bundle bundle = new Bundle(); // create bundle to send result to the fragment
        if (results.length == 0) {
            bundle.putString("isCheaper", "unknown"); // if no result is retrieved then it means no data was found
        } else {
            double medium = Double.parseDouble(results[0].getPriceMetrics()[2].getAmount()); // get the medium average price from the result
            String currency = Currency.getInstance(results[0].getCurrencyCode()).getSymbol();
            bundle.putString("price", String.valueOf(price));
            bundle.putString("medium", String.valueOf(medium));
            bundle.putString("isCheaper", price < medium ? "true" : "false");
            bundle.putString("currency", currency); // extract and convert the currency code to currency symbol
            bundle.putString("departureDate", String.valueOf(results[0].getDepartureDate()));
            bundle.putString("departureLocation", String.valueOf(results[0].getOrigin().getIataCode()));
            bundle.putString("destinationLocation", String.valueOf(results[0].getDestination().getIataCode()));
            saveTicketResult(price, medium, currency);
        }

        fragmentViewTicket.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ticket_fragment_frame,fragmentViewTicket)
                .commit();
    }

    public EnterTicketFragment getEnterTicketFragment() {
        return fragmentEnterTicket;
    }

    void saveTicketResult(double price, double medium, String currency) {
        SharedPreferences prefs = getSharedPreferences("history", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> historySet = prefs.getStringSet("history", new HashSet<>());
        ArrayList<String> historyList = new ArrayList<>(historySet); // convert Set to ArrayList
        try {
            JSONObject json = new JSONObject();
            json.put("ticketPrice", "Your ticket: " + currency + price);
            json.put("marketPrice", "Market price: " + currency + medium);
            json.put("route", "from " + departure_name + "\nto " + destination_name);
            json.put("date", flight_date);
            historyList.add(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        while (historyList.size() > MAX_HISTORY) historyList.remove(0); // if 10+ records, delete the oldest (first)
        editor.putStringSet("history", new HashSet<>(historyList)); // convert back to a Set and save
        editor.apply();
    }

    public void setDeparture_name(String departure_name) {
        this.departure_name = departure_name;
    }

    public void setDestination_name(String destination_name) {
        this.destination_name = destination_name;
    }

    public void setFlight_date(String flight_date) {
        this.flight_date = flight_date;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.navigation_home) {
            Intent switchActivityIntent = new Intent(this, MainActivity.class);
            startActivity(switchActivityIntent);
            return true;
        } else if (item.getItemId() == R.id.navigation_history) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.ticket_fragment_frame, historyFragment).commit();
            return true;
        } else if (item.getItemId() == R.id.navigation_setting) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.ticket_fragment_frame, settingFragment).commit();
            return true;
        }
        return false;
    }
}
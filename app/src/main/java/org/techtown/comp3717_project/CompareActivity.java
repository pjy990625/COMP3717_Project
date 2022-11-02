package org.techtown.comp3717_project;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.ItineraryPriceMetric;
import com.amadeus.resources.Location;

import org.techtown.comp3717_project.ui.compare.EnterTicketFragment;
import org.techtown.comp3717_project.ui.compare.ViewTicketFragment;

import java.util.Arrays;

public class CompareActivity extends AppCompatActivity {

    private EnterTicketFragment fragmentEnter;
    private ViewTicketFragment fragmentView;

    Amadeus amadeus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        fragmentEnter = new EnterTicketFragment();
        fragmentView = new ViewTicketFragment();

        amadeus = Amadeus
                .builder(BuildConfig.API_KEY, BuildConfig.API_SECRET)
                .build();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ticket_fragment_frame,fragmentEnter);
        fragmentTransaction.commit();
    }

    public String[] getAirports(String keyword) throws ResponseException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Location[] locations = amadeus.referenceData.locations.get(Params
                .with("keyword", keyword)
                .and("subType", "AIRPORT"));
        String[] results = new String[locations.length];
        for (int i = 0; i < locations.length; i++) results[i] = locations[i].getName();
        return results;
    }

    /**
     * Calls the ticket price analysis API and swaps the fragment to the result page.
     *
     * @param departure IATA code of the departure airport.
     * @param destination IATA code of the destination airport.
     * @param date Date of departure in the format of "yyyy-mm-dd".
     * @param price Price of the ticket.
     * @param currency Currency code.
     * @param isOneWay Whether the trip is a one way trip or not.
     */
    public void submitTravelInfo(String departure, String destination, String date, double price, String currency, boolean isOneWay) throws ResponseException {
        // without modifying something called StrictMode this code doesn't work but honestly I don't know what that is so I will ask our instructor during lab
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ItineraryPriceMetric[] metrics = amadeus.analytics.itineraryPriceMetrics.get(Params
                .with("originIataCode", departure)
                .and("destinationIataCode", destination)
                .and("departureDate", date)
                .and("currencyCode", currency)
                .and("oneWay", isOneWay ? "true" : "false"));
        double medium = Double.parseDouble(metrics[0].getPriceMetrics()[2].getAmount()); // get the medium average price from the result
        Bundle bundle = new Bundle(); // create bundle to send result to the fragment
        bundle.putString("medium", String.valueOf(medium));
        bundle.putString("howCheap", price < medium ? "true" : "false");
        fragmentView.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ticket_fragment_frame, fragmentView).commit();
    }

    public void fragmentChange(int index) throws ResponseException {
        if(index == 1) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.ticket_fragment_frame, fragmentEnter).commit();
        }
        else if(index == 2) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.ticket_fragment_frame, fragmentView).commit();
        }
    }

}
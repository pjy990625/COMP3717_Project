package org.techtown.comp3717_project;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.ItineraryPriceMetric;
import com.amadeus.resources.Location;

public class AmadeusManager {

    static AmadeusManager manager;
    static com.amadeus.Amadeus amadeus;

    // gets the Amadeus APi singleton
    public static AmadeusManager getManager() {
        if (manager == null) manager = new AmadeusManager();
        return manager;
    }

    private AmadeusManager() {
        // network configuration that prevents errors
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        amadeus = com.amadeus.Amadeus
                .builder(BuildConfig.API_KEY, BuildConfig.API_SECRET)
                .build();
    }

    /**
     * Calls the ticket price analysis API and swaps the fragment to the result page.
     *
     * @param keyword keyword for airport search.
     */
    public Location[] getAirports(String keyword) throws ResponseException {
        return amadeus.referenceData.locations.get(Params
                .with("keyword", keyword)
                .and("subType", "AIRPORT"));
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
    public ItineraryPriceMetric[] getTicketPriceAnalysis(String departure, String destination, String date, double price, String currency, boolean isOneWay) throws ResponseException {
        return amadeus.analytics.itineraryPriceMetrics.get(Params
                .with("originIataCode", departure)
                .and("destinationIataCode", destination)
                .and("departureDate", date)
                .and("currencyCode", currency)
                .and("oneWay", isOneWay ? "true" : "false"));
    }

}

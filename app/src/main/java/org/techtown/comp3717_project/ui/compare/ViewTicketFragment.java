package org.techtown.comp3717_project.ui.compare;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;

import org.techtown.comp3717_project.AmadeusManager;
import org.techtown.comp3717_project.R;

import java.util.Arrays;
import java.util.Objects;

public class ViewTicketFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_view_ticket, container, false);
        TextView headingText = rootView.findViewById(R.id.ticketComparisonHeadingText);
        TextView resultText = rootView.findViewById(R.id.ticketComparisonResultText);
        ImageView resultImage = rootView.findViewById(R.id.ticketComparisonResultImage);

        Bundle bundle = getArguments();

        try {
            rootView = ticketSuggestion(rootView, AmadeusManager.getManager().getFlightOffers(bundle.getString("departureLocation"), bundle.getString("destinationLocation"), bundle.getString("departureDate")));
        } catch (ResponseException e) {
            e.printStackTrace();
            if (e.getResponse().getStatusCode() == 400) {
                TextView suggestionLabel = rootView.findViewById(R.id.suggestionLabel);
                suggestionLabel.setText(R.string.other_tickets_unavailable);
            }
        }

        // check whether isCheaper is unknown - if it is, that means there was no data retrieved from api
        if (bundle.getString("isCheaper").equals("unknown")) {
            headingText.setText(R.string.comparison_heading_unknown);
            resultText.setText(R.string.comparison_result_unknown);
            resultImage.setImageResource(R.drawable.image_ticket_comparison_unknown);
            return rootView;
        }
        // otherwise, given values from bundle (results of ticket price comparison), update the result page
        headingText.setText(getString(R.string.comparison_heading_message, bundle.getString("currency") + bundle.getString("price")));
        resultText.setText(getString(R.string.comparison_result_message, bundle.getString("currency") + bundle.getString("medium")));
        resultImage.setImageResource(bundle.getString("isCheaper").equals("true") ? R.drawable.image_ticket_comparison_cheaper : R.drawable.image_ticket_comparison_not_cheaper);
        return rootView;
    }

    ViewGroup ticketSuggestion(ViewGroup rootView, FlightOfferSearch[] flightOffers) {
        TextView option1Date = rootView.findViewById(R.id.date1);
        TextView option2Date = rootView.findViewById(R.id.date2);
        TextView option3Date = rootView.findViewById(R.id.date3);
        TextView option1Time = rootView.findViewById(R.id.time1);
        TextView option2Time = rootView.findViewById(R.id.time2);
        TextView option3Time = rootView.findViewById(R.id.time3);
        TextView option1Airline = rootView.findViewById(R.id.airline1);
        TextView option2Airline = rootView.findViewById(R.id.airline2);
        TextView option3Airline = rootView.findViewById(R.id.airline3);
        TextView option1Price = rootView.findViewById(R.id.price1);
        TextView option2Price = rootView.findViewById(R.id.price2);
        TextView option3Price = rootView.findViewById(R.id.price3);

        option1Date.setText(flightOffers[0].getItineraries()[0].getSegments()[0].getDeparture().getAt().split("T")[0]);
        option1Time.setText(flightOffers[0].getItineraries()[0].getSegments()[0].getDeparture().getAt().split("T")[1]);
        option1Airline.setText(flightOffers[0].getItineraries()[0].getSegments()[0].getCarrierCode());
        option1Price.setText(String.format("$%s", flightOffers[0].getPrice().getTotal()));

        for (FlightOfferSearch flightOffer : flightOffers) {
            String date = flightOffer.getItineraries()[0].getSegments()[0].getDeparture().getAt().split("T")[0];
            String time = flightOffer.getItineraries()[0].getSegments()[0].getDeparture().getAt().split("T")[1];
            String airline = flightOffer.getItineraries()[0].getSegments()[0].getCarrierCode();
            String price = flightOffer.getPrice().getTotal();

            if (option2Price.getText().equals("N/A") && !option1Airline.getText().equals(airline)) {
                option2Date.setText(date);
                option2Time.setText(time);
                option2Airline.setText(airline);
                option2Price.setText(String.format("$%s", price));
            }
            if (!option2Price.getText().equals("N/A") && option3Price.getText().equals("N/A")
                    && !option1Airline.getText().equals(airline)
                    && !option2Airline.getText().equals(airline)) {
                option3Date.setText(date);
                option3Time.setText(time);
                option3Airline.setText(airline);
                option3Price.setText(String.format("$%s", price));
            }
        }
        return rootView;
    }

}
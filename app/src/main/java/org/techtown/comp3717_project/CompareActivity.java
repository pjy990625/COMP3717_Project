package org.techtown.comp3717_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.ItineraryPriceMetric;

import org.techtown.comp3717_project.ui.compare.EnterTicketFragment;
import org.techtown.comp3717_project.ui.compare.ViewTicketFragment;

public class CompareActivity extends AppCompatActivity {

    private EnterTicketFragment fragmentEnterTicket;
    private ViewTicketFragment fragmentViewTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        fragmentEnterTicket = new EnterTicketFragment();
        fragmentViewTicket = new ViewTicketFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ticket_fragment_frame,fragmentEnterTicket)
                .commit();
    }


    public void submitTravelInfo(double price, ItineraryPriceMetric[] results) {
        Bundle bundle = new Bundle(); // create bundle to send result to the fragment
        if (results.length == 0) {
            bundle.putString("isCheaper", "unknown");
        } else {
            double medium = Double.parseDouble(results[0].getPriceMetrics()[2].getAmount()); // get the medium average price from the result
            bundle.putString("price", String.valueOf(price));
            bundle.putString("medium", String.valueOf(medium));
            bundle.putString("isCheaper", price < medium ? "true" : "false");
        }
        fragmentViewTicket.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ticket_fragment_frame,fragmentViewTicket)
                .commit();
    }

    public EnterTicketFragment getEnterTicketFragment() {
        return fragmentEnterTicket;
    }

}
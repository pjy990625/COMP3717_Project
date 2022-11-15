package org.techtown.comp3717_project.ui.compare;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.amadeus.exceptions.ResponseException;

import org.techtown.comp3717_project.AmadeusManager;
import org.techtown.comp3717_project.CompareActivity;
import org.techtown.comp3717_project.R;

import java.util.Locale;

public class EnterTicketFragment extends Fragment {

    CompareActivity compareActivity;

    Button button_departure;
    Button button_destination;
    Button button_date;

    String departureIATA = "";
    String destinationIATA = "";

    String date = "";
    String[] currency = {"USD", "CAD", "EUR", "JPY"}; // bound to strings/currencies - make sure the orders match

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        compareActivity = (CompareActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        compareActivity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_enter_ticket, container, false);
        initializeButtons(rootView);
        return rootView;
    }

    // add onClickListeners to buttons on the fragment
    void initializeButtons(ViewGroup rootView) {
        button_departure = rootView.findViewById(R.id.departureAirport);
        button_departure.setOnClickListener(v -> openAirportSelectionDialog(true));
        button_destination = rootView.findViewById(R.id.destinationAirport);
        button_destination.setOnClickListener(v -> openAirportSelectionDialog(false));
        button_date = rootView.findViewById(R.id.departureDate);
        button_date.setOnClickListener(v -> {
            FlightDateDialogFragment flightDateDialogFragment = new FlightDateDialogFragment();
            flightDateDialogFragment.show(getParentFragmentManager(), "datePicker");
        });
        Button button_submit = rootView.findViewById(R.id.submit);
        button_submit.setOnClickListener(v -> {
            // after verifying all inputs are valid, submit them to compareActivity so that it can initiate ticket comparison request
            if (departureIATA.isEmpty() || destinationIATA.isEmpty()) {
                Toast.makeText(getActivity(),"Enter your departure and destination airports",Toast.LENGTH_SHORT).show();
                return;
            }
            if (departureIATA.equals(destinationIATA)) {
                Toast.makeText(getActivity(),"Departure and destination airports can't be the same",Toast.LENGTH_SHORT).show();
                return;
            }
            String priceStr = ((EditText) rootView.findViewById(R.id.price)).getText().toString().trim();
            if (priceStr.length() == 0) {
                Toast.makeText(getActivity(),"Specify the ticket price you purchased",Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                double price = Double.parseDouble(priceStr);
                Spinner currencySpinner = rootView.findViewById(R.id.currencySpinner);
                compareActivity.submitTravelInfo(price, AmadeusManager.getManager().getTicketPriceAnalysis(departureIATA, destinationIATA, date, price, currency[currencySpinner.getSelectedItemPosition()], false));
            } catch (ResponseException e) {
                Log.d("Amadeus", e.toString());
            }
        });
    }

    void openAirportSelectionDialog(boolean isDeparture) {
        AirportDialogFragment airportDialogFragment = new AirportDialogFragment(isDeparture);
        airportDialogFragment.show(getParentFragmentManager(), "airport");
    }

    public void setAirport(String name, String IATA, boolean isDeparture) {
        if (isDeparture) {
            button_departure.setText(name);
            departureIATA = IATA;
        } else {
            button_destination.setText(name);
            destinationIATA = IATA;
        }
    }

    public void setDate(int year, int month, int day) {
        date = year + "-" + String.format(Locale.getDefault(), "%02d", month) + "-" + String.format(Locale.getDefault(), "%02d", day);
        button_date.setText(date);
    }
}
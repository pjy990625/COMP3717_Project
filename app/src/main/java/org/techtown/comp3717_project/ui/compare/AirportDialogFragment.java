package org.techtown.comp3717_project.ui.compare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Location;

import org.techtown.comp3717_project.AmadeusManager;
import org.techtown.comp3717_project.CompareActivity;
import org.techtown.comp3717_project.InfoActivity;
import org.techtown.comp3717_project.R;

import java.util.ArrayList;

public class AirportDialogFragment extends DialogFragment {

    CompareActivity compareActivity;
    boolean isDeparture;

    String name;
    String IATA;

    public AirportDialogFragment(boolean isDeparture) {
        this.isDeparture = isDeparture;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_airport_dialog, container, false);
        TextView titleText = rootView.findViewById(R.id.searchAirportLabel);
        titleText.setText(getString(R.string.dialog_main_message, isDeparture ? "Departure" : "Arrive"));
        EditText input = rootView.findViewById(R.id.searchAirportInput);
        input.setOnClickListener(v -> input.setText(null));
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
                    }
                }
            }
        });
        Button confirm = rootView.findViewById(R.id.confirm);
        confirm.setOnClickListener(v -> saveAirportInfo());
        Button cancel = rootView.findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> dismiss());
        return rootView;
    }

    void updateAirportList(Location[] locations) {
        ArrayList<String> listItems = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                listItems);
        ListView list = getView().findViewById(R.id.searchAirportList);
        list.setAdapter(adapter);
        list.setVisibility(View.VISIBLE);

        for (Location location : locations) {
            adapter.add(location.getName());
        }

        list.setOnItemClickListener((adapterView, view1, i, l) -> {
            name = locations[i].getName();
            IATA = locations[i].getIataCode();
            EditText input = getView().findViewById(R.id.searchAirportInput);
            input.setText(name);
            input.clearFocus();
            input.requestFocus(EditText.FOCUS_DOWN);
            adapterView.setVisibility(View.INVISIBLE);
        });
    }

    void saveAirportInfo() {
        compareActivity.getEnterTicketFragment().setAirport(name, IATA, isDeparture);
        dismiss();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        compareActivity = (CompareActivity) getActivity();
    }
}
package org.techtown.comp3717_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Location;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        EditText input = findViewById(R.id.editTextAirportName);
        input.setTextColor(com.google.android.material.R.attr.colorOnSecondary);
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
    }

    void updateAirportList(Location[] locations) {
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
                    + locations[i].getAddress().getCityCode() + ")");
            bundle.putString("Location", locations[i].getAddress().getCityName() + ", " +
                    locations[i].getAddress().getCountryName());
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }
}
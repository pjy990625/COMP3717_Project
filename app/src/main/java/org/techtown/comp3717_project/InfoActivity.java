package org.techtown.comp3717_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.techtown.comp3717_project.adapters.ItemsMyRecyclerAdapter;

public class InfoActivity extends AppCompatActivity {

    private String link = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String airportName = bundle.getString("Airport");
        String airportLocation = bundle.getString("Location");
        String airportLink = bundle.getString("Link");
        this.link = airportLink;
        TextView name = findViewById(R.id.textViewAirportName);
        name.setText(airportName);
        TextView location = findViewById(R.id.textViewLocationCode);
        location.setText(airportLocation);
        Button linkButton = findViewById(R.id.buttonWebsiteCode);

        linkButton.setOnClickListener(v -> {
            if (link.compareTo("") != 0) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
            }
        });
    }
}
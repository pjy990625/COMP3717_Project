package org.techtown.comp3717_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amadeus.resources.ItineraryPriceMetric;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.techtown.comp3717_project.ui.compare.EnterTicketFragment;
import org.techtown.comp3717_project.ui.compare.ViewTicketFragment;
import org.techtown.comp3717_project.ui.history.HistoryFragment;
import org.techtown.comp3717_project.ui.setting.SettingFragment;

import java.util.Currency;

public class CompareActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {


    private EnterTicketFragment fragmentEnterTicket;
    private ViewTicketFragment fragmentViewTicket;
    BottomNavigationView bottomNavigationView;
    HistoryFragment historyFragment = new HistoryFragment();
    SettingFragment settingFragment = new SettingFragment();

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
            bundle.putString("price", String.valueOf(price));
            bundle.putString("medium", String.valueOf(medium));
            bundle.putString("isCheaper", price < medium ? "true" : "false");
            bundle.putString("currency", Currency.getInstance(results[0].getCurrencyCode()).getSymbol()); // extract and convert the currency code to currency symbol
        }
        fragmentViewTicket.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ticket_fragment_frame,fragmentViewTicket)
                .commit();
    }

    public EnterTicketFragment getEnterTicketFragment() {
        return fragmentEnterTicket;
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
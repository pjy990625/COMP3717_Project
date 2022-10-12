package org.techtown.comp3717_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.techtown.comp3717_project.ui.EnterTicketFragment;
import org.techtown.comp3717_project.ui.ViewTicketFragment;

public class CompareActivity extends AppCompatActivity {

    private EnterTicketFragment fragmentEnter;
    private ViewTicketFragment fragmentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        fragmentEnter = new EnterTicketFragment();
        fragmentView = new ViewTicketFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ticket_fragment_frame,fragmentEnter);
        fragmentTransaction.commit();
    }

    public void fragmentChange(int index){
        if(index == 1){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.ticket_fragment_frame, fragmentEnter).commit();
        }
        else if(index == 2){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.ticket_fragment_frame, fragmentView).commit();
        }
    }

}
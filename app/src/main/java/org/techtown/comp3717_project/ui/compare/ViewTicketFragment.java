package org.techtown.comp3717_project.ui.compare;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.techtown.comp3717_project.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewTicketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewTicketFragment extends Fragment {


    public ViewTicketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewTicket.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewTicketFragment newInstance(String param1, String param2) {
        return new ViewTicketFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_view_ticket, container, false);
        Bundle bundle = getArguments();
        TextView headingText = rootView.findViewById(R.id.ticketComparisonHeadingText);
        headingText.setText(getString(R.string.comparison_heading_message, bundle.getString("price")));
        TextView resultText = rootView.findViewById(R.id.ticketComparisonResultText);
        resultText.setText(getString(R.string.comparison_result_message, bundle.getString("medium")));
        return rootView;
    }

}
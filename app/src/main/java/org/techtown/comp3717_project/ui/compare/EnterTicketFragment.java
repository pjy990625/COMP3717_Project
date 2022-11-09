package org.techtown.comp3717_project.ui.compare;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.amadeus.exceptions.ResponseException;

import org.techtown.comp3717_project.CompareActivity;
import org.techtown.comp3717_project.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EnterTicketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnterTicketFragment extends Fragment {

    Button button;
    CompareActivity compareActivity;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText departureInput;
    EditText destinationInput;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EnterTicketFragment() {
        // Get a reference to the AutoCompleteTextView in the layout
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EnterTicketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EnterTicketFragment newInstance(String param1, String param2) {
        EnterTicketFragment fragment = new EnterTicketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        departureInput = rootView.findViewById(R.id.departureAirport);
        destinationInput = rootView.findViewById(R.id.destinationAirport);
        button = rootView.findViewById(R.id.submit);
        button.setOnClickListener(v -> {
            try {
                double price = Double.parseDouble(((EditText) rootView.findViewById(R.id.price)).getText().toString());
                compareActivity.submitTravelInfo("MAD", "CDG", "2021-03-21", price, "CAD", false);
            } catch (ResponseException e) {
                Log.d("Amadeus", e.toString());
            }
        });
        return rootView;
    }

}
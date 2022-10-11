package org.techtown.comp3717_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Airport#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Airport extends Fragment {
    RecyclerView items, services;
    String[] itemNames, serviceNames;
    int[] dutyFreeIcons = {R.drawable.ic_baseline_smoking_rooms_24,
            R.drawable.ic_baseline_local_drink_24, R.drawable.ic_baseline_shopping_cart_24};
    int[] servicesIcons = {R.drawable.ic_baseline_car_rental_24, R.drawable.ic_baseline_hotel_24,
    R.drawable.ic_baseline_local_taxi_24};

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String NAME = "AirPort";

    // TODO: Rename and change types of parameters
    private String name;

    public Airport() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param name Parameter 1.
     * @return A new instance of fragment Airport.
     */
    public static Airport newInstance(String name) {
        Airport fragment = new Airport();
        Bundle args = new Bundle();
        args.putString(NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString("Airport");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_airport, container, false);
        TextView textView = view.findViewById(R.id.textViewAirportName);
        textView.setText(name);
        items = view.findViewById(R.id.recyclerViewItems);
        services = view.findViewById(R.id.recyclerViewServices);
        itemNames = getResources().getStringArray(R.array.items);
        serviceNames = getResources().getStringArray(R.array.services);
        ItemsMyRecyclerAdapter itemsMyRecyclerAdapter1 = new ItemsMyRecyclerAdapter(getActivity(),
                itemNames, dutyFreeIcons);
        ItemsMyRecyclerAdapter itemsMyRecyclerAdapter2 = new ItemsMyRecyclerAdapter(getActivity(),
                serviceNames, servicesIcons);
        items.setAdapter(itemsMyRecyclerAdapter1);
        items.setLayoutManager(new LinearLayoutManager(this.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        services.setAdapter(itemsMyRecyclerAdapter2);
        services.setLayoutManager(new LinearLayoutManager(this.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        return view;
    }
}
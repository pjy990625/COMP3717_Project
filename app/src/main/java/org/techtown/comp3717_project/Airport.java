package org.techtown.comp3717_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Airport() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Airport.
     */
    // TODO: Rename and change types and number of parameters
    public static Airport newInstance(String param1, String param2) {
        Airport fragment = new Airport();
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
        itemNames = getResources().getStringArray(R.array.items);
        ItemsMyRecyclerAdapter itemsMyRecyclerAdapter = new ItemsMyRecyclerAdapter(this.getContext(),
                itemNames, dutyFreeIcons);
        items.setAdapter(itemsMyRecyclerAdapter);
        items.setLayoutManager(new LinearLayoutManager(this.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_airport, container, false);
    }
}
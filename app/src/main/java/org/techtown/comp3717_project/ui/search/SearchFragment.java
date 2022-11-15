package org.techtown.comp3717_project.ui.search;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.techtown.comp3717_project.InfoActivity;
import org.techtown.comp3717_project.R;

public class SearchFragment extends Fragment {

    String[] names;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        names = getResources().getStringArray(R.array.airports);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(inflater.getContext(), android.R.layout.simple_list_item_1, names);
        ListView list = view.findViewById(R.id.list);
        list.setOnItemClickListener((adapterView, view1, i, l) -> {
            Intent intent = new Intent(this.getContext(), InfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("Airport", names[i]);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        list.setAdapter(adapter);
        return view;
    }
}
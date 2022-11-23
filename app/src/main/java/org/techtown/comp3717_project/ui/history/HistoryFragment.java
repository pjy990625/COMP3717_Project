package org.techtown.comp3717_project.ui.history;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.techtown.comp3717_project.R;

import java.util.HashSet;
import java.util.Set;

public class HistoryFragment extends Fragment {

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_history, container, false);
        Context context = rootView.getContext();
        SharedPreferences prefs = context.getSharedPreferences("history", Context.MODE_PRIVATE);
        Set<String> historySet = prefs.getStringSet("history", new HashSet<>());
        RecyclerView recyclerView = rootView.findViewById(R.id.historyView);
        HistoryRecyclerViewAdapter adapter = new HistoryRecyclerViewAdapter(context, historySet);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        Log.d("TAG", String.valueOf(historySet.size()));
        return rootView;
    }
}
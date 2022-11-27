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
import android.widget.Button;
import android.widget.TextView;

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
        Button reset = rootView.findViewById(R.id.resetHistory);
        TextView ph = rootView.findViewById(R.id.historyPlaceholder);
        if (historySet.size() > 0) {
            ph.setVisibility(View.GONE);
            RecyclerView recyclerView = rootView.findViewById(R.id.historyView);
            HistoryRecyclerViewAdapter adapter = new HistoryRecyclerViewAdapter(context, historySet);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
            reset.setOnClickListener(v -> {
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear().apply();
                recyclerView.setVisibility(View.GONE);
                reset.setVisibility(View.GONE);
                ph.setVisibility(View.VISIBLE);
            });
        } else {
            reset.setVisibility(View.GONE);
        }
        return rootView;
    }
}
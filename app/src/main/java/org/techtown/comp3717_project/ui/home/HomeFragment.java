package org.techtown.comp3717_project.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.techtown.comp3717_project.CompareActivity;
import org.techtown.comp3717_project.R;
import org.techtown.comp3717_project.SearchActivity;
import org.techtown.comp3717_project.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.welcome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        Button services = root.findViewById(R.id.service);
        services.setOnClickListener((view1 -> {
            Intent intent = new Intent(this.getContext(), SearchActivity.class);
            startActivity(intent);
        }));
        Button compare = root.findViewById(R.id.compare);
        compare.setOnClickListener((view1 -> {
            Intent intent = new Intent(this.getContext(), CompareActivity.class);
            startActivity(intent);
        }));
//        Button history = root.findViewById(R.id.history);
//        history.setOnClickListener((view1 -> {
//            Intent intent = new Intent(this.getContext(), HistoryActivity.class);
//            startActivity(intent);
//        }));
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
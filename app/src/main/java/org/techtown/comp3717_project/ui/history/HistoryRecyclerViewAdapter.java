package org.techtown.comp3717_project.ui.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.comp3717_project.R;

import java.util.ArrayList;
import java.util.Set;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> items;

    public HistoryRecyclerViewAdapter(Context context, Set<String> items) {
        this.context = context;
        this.items = new ArrayList<>(items);
    }

    @NonNull
    @Override
    public HistoryRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new HistoryRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryRecyclerViewAdapter.MyViewHolder holder, int position) {
        try {
            JSONObject json = new JSONObject(items.get(position));
            holder.textView_content.setText(R.string.history_template));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView_content;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_content = itemView.findViewById(R.id.content);
        }
    }
}

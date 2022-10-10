package org.techtown.comp3717_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemsMyRecyclerAdapter extends RecyclerView.Adapter<ItemsMyRecyclerAdapter.ItemsViewHolder>{
    private Context c;
    private String names[];
    private int images[];
    public ItemsMyRecyclerAdapter (Context c, String[] names, int[] images) {
        this.c = c;
        this.names = names;
        this.images = images;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View view = inflater.inflate(R.layout.card_layout, parent, false);
        return new ItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        holder.name.setText(names[position]);
        holder.icon.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class ItemsViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView icon;
        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewName);
            icon = itemView.findViewById(R.id.imageViewIcon);
        }
    }


}

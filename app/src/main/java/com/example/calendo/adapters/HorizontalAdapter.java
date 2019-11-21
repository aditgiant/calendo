package com.example.calendo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendo.R;

public class HorizontalAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private String[] list;
    private Context context;
    public HorizontalAdapter(Context c ,String[] list ) {
        this.context = c;
        this.list = list;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.taskCategory.setText(list[position]);

    }

    @Override
    public int getItemCount() {
        return list.length;
    }
}

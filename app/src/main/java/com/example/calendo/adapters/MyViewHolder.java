package com.example.calendo.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.calendo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView taskCategory;
    public FloatingActionButton buttonCategory;


    public MyViewHolder(View v) {
        super(v);
        taskCategory = v.findViewById(R.id.taskCategory);
        buttonCategory = v.findViewById(R.id.buttonCategory);

    }


}
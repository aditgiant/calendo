package com.example.calendo.adapters;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.calendo.R;

public class MyViewHolder extends RecyclerView.ViewHolder{

    public Button taskCategory;

    public MyViewHolder(View v) {
        super(v);
        taskCategory = v.findViewById(R.id.categoryTask);

    }


}
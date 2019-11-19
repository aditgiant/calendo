package com.example.calendo;

import android.view.View;
import android.widget.Button;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public Button taskCategory;

    public MyViewHolder(View v) {
        super(v);
        taskCategory = v.findViewById(R.id.categoryTask);

    }
}
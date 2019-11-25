package com.example.calendo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.calendo.R;

//Private class used in the on create method
public class MyAdapter extends ArrayAdapter<String> {
    Context context;
    String[] rTitle;
    String[] rDescription;
    String[] rDue;
    private TextView myTitle, myDescription, myDue;


    public MyAdapter(Context c, String[] title, String[] description, String[] due) {
        super (c, R.layout.row, R.id.titleTodo, title);

        this.context = c;
        this.rTitle = title;
        this.rDescription = description;
        this.rDue = due;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row, parent, false);
        }
        myTitle = convertView.findViewById(R.id.titleTodo);
        myDue = convertView.findViewById(R.id.dueTodo);
        myDescription = convertView.findViewById(R.id.descriptionTodo);

        myTitle.setText(rTitle[position]);
        myDue.setText(rDue[position]);
        myDescription.setText(rDescription[position]);


        return convertView;
    }
}
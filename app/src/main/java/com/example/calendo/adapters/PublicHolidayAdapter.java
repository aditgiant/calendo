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

public class PublicHolidayAdapter extends ArrayAdapter<String> {

    String[] date;
    String[] name;
    String[] description;
    Context context;
    TextView EventName, EventDescription, EventDate;

    public PublicHolidayAdapter(Context context, String[] name, String[] description, String[] date){

        super (context, R.layout.public_holiday_row, R.id.event_name, name);
        this.context = context;
        this.name = name;
        this.description = description;
        this.date = date;


    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.public_holiday_row, parent, false);
        }

        EventName = convertView.findViewById(R.id.event_name);
        EventDescription = convertView.findViewById(R.id.event_description);
        EventDate = convertView.findViewById(R.id.event_date);

        EventName.setText(name[position]);
        EventDescription.setText(description[position]);
        EventDate.setText(date[position]);


        return convertView;
    }

    public String[] getDate() {
        return date;
    }

    public void setDate(String[] date) {
        this.date = date;
    }

    public String[] getName() {
        return name;
    }

    public void setName(String[] name) {
        this.name = name;
    }

    public String[] getDescription() {
        return description;
    }

    public void setDescription(String[] description) {
        this.description = description;
    }
}



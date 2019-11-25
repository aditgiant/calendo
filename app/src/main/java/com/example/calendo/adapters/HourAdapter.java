package com.example.calendo.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.calendo.R;
import com.example.calendo.fragments.calendar.TabWeekFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//Private class used in the on create method
public class HourAdapter extends ArrayAdapter<String> {
    private final Context mContext;
    private final String[] activities;

    public HourAdapter(Context context, String[] activities) {
        super(context,R.layout.hourtable,R.id.activityLabel, activities);
        this.mContext = context;
        this.activities = activities;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //find the proper book for this cell by using the position index
        final String activity = activities[position];
        // 2
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.hourtable, null);
        }
        //refer to hourtable.xml
        final TextView mActivity = (TextView) convertView.findViewById(R.id.activityLabel);
        //set content
        mActivity.setText(activity);
        return convertView;
    }
}

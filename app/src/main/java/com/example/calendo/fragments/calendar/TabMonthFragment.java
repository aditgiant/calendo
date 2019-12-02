package com.example.calendo.fragments.calendar;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.calendo.AddNewTaskActivity;
import com.example.calendo.MainActivity;

import com.example.calendo.R;
import com.example.calendo.RetrievedTask;
import com.example.calendo.fragments.StatisticsFragment;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabMonthFragment extends Fragment{


    public TabMonthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tab_month, container, false);
        CalendarView c = view.findViewById(R.id.simpleCalendarView);
        c.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                int yearselect = year;
                int monthselect = month;
                int dayselect = day;
                Bundle bundle = new Bundle();
                bundle.putInt("yearselect", yearselect);
                bundle.putInt("monthselect", monthselect);
                bundle.putInt("dayselect", dayselect);
                Fragment tabWeekFragment = new TabWeekFragment();
                tabWeekFragment.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.pager, tabWeekFragment, "TabWeekFragment")
                        .addToBackStack("TabWeekFragment").commit();


//                Intent intent = new Intent(getActivity().getBaseContext(), MainActivity.class);
//                intent.putExtra("yearselect", yearselect);
//                intent.putExtra("monthselect", monthselect);
//                intent.putExtra("dayselect", dayselect);
//                getActivity().startActivity(intent);
            }
        });

        // Create a new instance of DatePickerDialog and return it.
        return view;
    }
}

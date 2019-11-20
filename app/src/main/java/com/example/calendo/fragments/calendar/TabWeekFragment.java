package com.example.calendo.fragments.calendar;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.calendo.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabWeekFragment extends Fragment {


    public TabWeekFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_week, container, false);
    }

}

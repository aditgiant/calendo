package com.example.calendo.fragments.calendar;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.calendo.R;
import com.example.calendo.adapters.HourAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabWeekFragment extends Fragment {
    /*private static final String TAG = "TabWeekFragment";
    String activity[] = {".","00.00","","","","","","","",
            "01.00","","","","","","","",
            "02.00","","","","","","","",
            "03.00","","","","","","","",
            "04.00","","","","","","","",
            "05.00","","","","","","","",
            "06.00","","","","","","","",
            "07.00","","","","","","","",
            "08.00","","","","","","","",
            "09.00","","","","","","","",
            "10.00","","","","","","","",
            "11.00","","","","","","","",
            "12.00","","","","","","","",
            "13.00","","","","","","","",
            "14.00","","","","","","","",
            "15.00","","","","","","","",
            "16.00","","","","","","","",
            "17.00","","","","","","","",
            "18.00","","","","","","","",
            "19.00","","","","","","","",
            "20.00","","","","","","","",
            "21.00","","","","","","","",
            "22.00","","","","","","","",
            "23.00","","","","","","",""};

//    String activity[] = {"lala", "lili", "lolo", "12.00", "lalala","23.00", "12.00","agdg","dsgdywegd","ououuuiuoiuouiuoiuouoiouiuo"};
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_week, container, false);
//        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            int rdayselect = bundle.getInt("dayselect");
//            Log.d("TES", ""+rdayselect);
//        }
        //Intent receiveIntent = getActivity().getIntent();
        //Log.d("Date selected : ", receiveIntent.getStringExtra("yearselect") );

        /*
        GridView hourtable = view.findViewById(R.id.hourtable);
        HourAdapter hourAdapter = new HourAdapter(this.getContext(), activity);
        hourtable.setAdapter(hourAdapter);
        Log.d(TAG, hourAdapter.toString());

         */
        return view;
    }


}

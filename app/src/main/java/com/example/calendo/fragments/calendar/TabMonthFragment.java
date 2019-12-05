package com.example.calendo.fragments.calendar;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.calendo.R;
import com.example.calendo.adapters.PublicHolidayAdapter;
import com.example.calendo.utils.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.calendo.Constants.API_KEY;
import static com.example.calendo.Constants.ENDPOINT;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabMonthFragment extends Fragment {
    public  static ListView data;

    public  TabMonthFragment() {
        // Required empty public constructor
        Context context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tab_month, container, false);
        CalendarView c = view.findViewById(R.id.simpleCalendarView);

        data = view.findViewById(R.id.public_holiday);
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
            RetrievedTask retrievedTask = new RetrievedTask();
            retrievedTask.execute();


        // Create a new instance of DatePickerDialog and return it.
        return view;
    }

    public class RetrievedTask extends AsyncTask<Void, Void, Void> {

        private ArrayList<String> event_name_list = new ArrayList<>();
        private ArrayList<String> event_description_list  = new ArrayList<>();;
        private ArrayList<String> event_date_list  = new ArrayList<>();;

        private String parameter ="&country=SE&year=2019&month=12";


        @Override
        protected Void doInBackground(Void... voids) {
            String url = ENDPOINT+"api_key="+API_KEY+parameter;
            Log.d(TAG, url);
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject response = jsonObj.getJSONObject("response");
                    JSONArray holiday = response.getJSONArray("holidays");

                    for (int i = 0; i < holiday.length(); i++) {
                        JSONObject c = holiday.getJSONObject(i);
                        String event_name = c.getString("name");
                        String event_description = c.getString("description");
                        String event_date = c.getString("date");
                        JSONObject jsonDate = new JSONObject(event_date);
                        String date_event = jsonDate.getString("iso");
                        event_name_list.add(event_name);
                        event_description_list.add(event_description);
                        event_date_list.add(date_event);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            PublicHolidayAdapter publicHolidayAdapter = new PublicHolidayAdapter(getContext(), event_name_list.toArray(new String[0]), event_description_list.toArray(new String[0]), event_date_list.toArray(new String[0]));
            TabMonthFragment.data.setAdapter(publicHolidayAdapter);
        }
    }


}

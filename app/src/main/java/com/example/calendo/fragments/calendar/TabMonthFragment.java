package com.example.calendo.fragments.calendar;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.example.calendo.R;
import com.example.calendo.adapters.PublicHolidayAdapter;
import com.example.calendo.fragments.todolist.Task;
import com.example.calendo.utils.HttpHandler;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.example.calendo.Constants.API_KEY;
import static com.example.calendo.Constants.ENDPOINT;
import static com.example.calendo.utils.User.MY_PREFS_NAME;



/**
 * A simple {@link Fragment} subclass.
 */
public class TabMonthFragment extends Fragment {
    public  static ListView data;


    //Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("Users");

    public TabMonthFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();
        renderEvents();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        // Add Events from the Firebase
        View view = inflater.inflate(R.layout.fragment_tab_month, container, false);
        renderEvents();





//        calendarView.setOnDayClickListener(eventDay ->
//                Toast.makeText(view.getContext(),
//                        userID+eventDay.getCalendar().getTime().toString() + " "
//                                + eventDay.isEnabled(),
//                        Toast.LENGTH_SHORT).show());

//
//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
//                int yearselect = year;
//                int monthselect = month;
//                int dayselect = day;
//                Bundle bundle = new Bundle();
//                bundle.putInt("yearselect", yearselect);
//                bundle.putInt("monthselect", monthselect);
//                bundle.putInt("dayselect", dayselect);
//                Fragment tabWeekFragment = new TabWeekFragment();
//                tabWeekFragment.setArguments(bundle);
//                getFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.pager, tabWeekFragment, "TabWeekFragment")
//                        .addToBackStack("TabWeekFragment").commit();
//
//

       // CalendarView c = view.findViewById(R.id.simpleCalendarView);
        data = view.findViewById(R.id.public_holiday);



    RetrievedTask retrievedTask = new RetrievedTask();
    retrievedTask.execute();

        // Create a new instance of DatePickerDialog and return it.
        return view;
    }


    public void renderEvents() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String userID = sharedPref.getString("userID", "NOUSERFOUND");

        CollectionReference tasklist = db.collection("Users").document(userID).collection("list");

        tasklist.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                 retrieveDate(queryDocumentSnapshots);

            }
        });


    }

    public void retrieveDate(QuerySnapshot queryDocumentSnapshots) {
        List<EventDay> events = new ArrayList<>();
        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
            Task todolist = documentSnapshot.toObject(Task.class);
//            Calendar calendarMonth =
//
            String date = todolist.getDate();
            int dayInt=0; int monthInt=0; int yearInt=0;

            if(date!=""){
                String day = date.substring(6, 8);
                //get date integer
                 dayInt = Integer.parseInt(day);
                //get month
                String month = date.substring(4, 6);
                 monthInt = Integer.parseInt(month);
                //get year
                String year = date.substring(0, 4);
                 yearInt = Integer.parseInt(year);
            }

            //date format 20200131

            Calendar calendar1 = Calendar.getInstance();
            //get date

            //calendar1.add(Integer.parseInt(day), 0);
            Log.d(TAG, "retrieveMonth: " + Calendar.MONTH);
            Log.d(TAG, "retrieveDay: " + Calendar.DAY_OF_MONTH);

            //26122019
            Log.d(TAG, "retrieveDate: " + date);

//              calendar1.add(dayInt,0);
                calendar1.set(yearInt, monthInt-1, dayInt);
                events.add(new EventDay(calendar1, R.drawable.ic_to_do_list));
                CalendarView calendarView = getActivity().findViewById(R.id.calendarView);
                calendarView.setEvents(events);


        }
    }




        public class RetrievedTask extends AsyncTask<Void, Void, Void> {

            private ArrayList<String> event_name_list = new ArrayList<>();
            private ArrayList<String> event_description_list = new ArrayList<>();
            ;
            private ArrayList<String> event_date_list = new ArrayList<>();
            ;

            private String parameter = "&country=US&year=2019&month=12";



            @Override
            protected Void doInBackground(Void... voids) {
                String url = ENDPOINT + "api_key=" + API_KEY + parameter;
                Log.d(TAG, url);
                HttpHandler sh = new HttpHandler();
                String jsonStr = sh.makeServiceCall(url);
                Log.e(TAG, "Response from url: " + jsonStr);
                List<EventDay> events = new ArrayList<>();


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

//                            //render to calendar
//                            Calendar calendar1 = Calendar.getInstance();
//                            //get date
//                            //format date 2018-12-31
//                            String date = date_event;
//                            int dayInt=0; int monthInt=0; int yearInt=0;
//
//                            if(!date.equals("")){
//                                String day = date.substring(8, 10);
//                                //get date integer
//                                dayInt = Integer.parseInt(day);
//                                //get month
//                                String month = date.substring(5, 7);
//                                monthInt = Integer.parseInt(month);
//                                //get year
//                                String year = date.substring(0, 4);
//                                yearInt = Integer.parseInt(year);
//                            }
//                            //calendar1.add(Integer.parseInt(day), 0);
//                            Log.d(TAG, "retrieveMonth: " + Calendar.MONTH);
//                            Log.d(TAG, "retrieveDay: " + Calendar.DAY_OF_MONTH);
//
//                            //26122019
//                            Log.d(TAG, "retrieveDate: " + date);
//
//            //              calendar1.add(dayInt,0);
//                            calendar1.set(yearInt, monthInt-1, dayInt);
//                            events.add(new EventDay(calendar1, R.drawable.ic_to_do_list));
//                            CalendarView calendarView = getActivity().findViewById(R.id.calendarView);
//                            calendarView.setEvents(events);



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
                if(getContext()!=null) {
                    PublicHolidayAdapter publicHolidayAdapter = new PublicHolidayAdapter(getContext(), event_name_list.toArray(new String[0]), event_description_list.toArray(new String[0]), event_date_list.toArray(new String[0]));
                    TabMonthFragment.data.setAdapter(publicHolidayAdapter);
                }
            }

        }

        }


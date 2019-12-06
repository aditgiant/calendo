package com.example.calendo.fragments.calendar;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.calendo.AddNewTaskActivity;
import com.example.calendo.MainActivity;

import com.example.calendo.R;
import com.example.calendo.fragments.StatisticsFragment;
import com.example.calendo.fragments.todolist.Task;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.example.calendo.utils.User.MY_PREFS_NAME;
import static com.google.protobuf.WireFormat.JavaType.INT;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabMonthFragment extends Fragment {

    //Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("Users");

    public TabMonthFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // Add Events from the Firebase
        View view = inflater.inflate(R.layout.fragment_tab_month, container, false);
//        List<EventDay> events = new ArrayList<>();

//        Calendar calendar1 = Calendar.getInstance();
//       // calendar1.add(Calendar.DAY_OF_MONTH, 10);
//        events.add(new EventDay(calendar1, R.drawable.ic_to_do_list));
//        CalendarView calendarView = view.findViewById(R.id.calendarView);
//        calendarView.setEvents(events);

        renderEvents();

        //Retrieve the userID
//        SharedPreferences sharedPref = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
//        String userID = sharedPref.getString("userID", "NOUSERFOUND");
//        CollectionReference tasklist = db.collection("Users").document(userID).collection("list");
//


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
//                Intent intent = new Intent(getActivity().getBaseContext(), MainActivity.class);
//                intent.putExtra("yearselect", yearselect);
//                intent.putExtra("monthselect", monthselect);
//                intent.putExtra("dayselect", dayselect);
//                getActivity().startActivity(intent);
//            }
//        });

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

    public void retrieveDate(QuerySnapshot queryDocumentSnapshots){
        List<EventDay> events = new ArrayList<>();
        for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
            Task todolist = documentSnapshot.toObject(Task.class);
//            Calendar calendarMonth =
//
            String date = todolist.getDate();

            Calendar calendar1 = Calendar.getInstance();
            //get date
            String day=date.substring(3,5);
            //get date integer
            int dayInt= Integer.parseInt(day)-1;
            //get month
            String month = date.substring(0,2);
            int monthInt= Integer.parseInt(month);
           //get year
            String year = date.substring(6,10);
            int yearInt= Integer.parseInt(year);
            //calendar1.add(Integer.parseInt(day), 0);
            Log.d(TAG, "retrieveMonth: " + Calendar.MONTH);
            Log.d(TAG, "retrieveDate: " + date);
            if(monthInt == (Calendar.MONTH+10)){
                calendar1.add(Calendar.DAY_OF_MONTH, dayInt-Calendar.DAY_OF_MONTH);
                events.add(new EventDay(calendar1, R.drawable.ic_to_do_list));
                CalendarView calendarView = getActivity().findViewById(R.id.calendarView);
                calendarView.setEvents(events);

            }









        }







    }


}

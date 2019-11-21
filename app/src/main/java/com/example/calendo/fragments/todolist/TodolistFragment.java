package com.example.calendo.fragments.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendo.AddNewTaskActivity;
import com.example.calendo.adapters.HorizontalAdapter;
import com.example.calendo.R;

import java.util.ArrayList;
import java.util.Arrays;

import static android.app.Activity.RESULT_OK;
import static com.example.calendo.AddNewTaskActivity.TASK_TITLE;

public class TodolistFragment extends Fragment {
    private TextView myTitle, myDescription, myDue;
    private CheckBox checkBox;
    private ListView listView;
    private RecyclerView recyclerView;

    //Data
    private ArrayList<Task> todolist;
    private ArrayList<String> categories;


    //Temp attribute for this long string
    private static final String temp="ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua";
    public static final int TEXT_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.to_do_list_fragment, container, false);

        listView = view.findViewById(R.id.listView);
        recyclerView = view.findViewById(R.id.categoryList);
        checkBox = view.findViewById(R.id.checkboxTask);

        /*---------- List ----------
        //Fill the todolist with fake tasks

        todolist.add(new Task("0", "Laundry", "Todo", temp, "12/12/2019"));
        todolist.add(new Task("1", "Homework", "Reminder", temp, "12/12/2019"));
        todolist.add(new Task("2", "Group Meeting", "Appointment", temp, "12/12/2019"));
        todolist.add(new Task("3", "Shopping", "Todo", temp, "12/12/2019"));
        todolist.add(new Task("4", "Dating", "Todo", temp, "12/12/2019"));
        todolist.add(new Task("5", "Assignment HCI Seminar", "Todo", temp, "12/12/2019"));*/


        //Update the list
        todolist = new ArrayList<>();
        updateTodolist();


        //Fill the categories list with fake categories
        categories = new ArrayList<>();
        categories.addAll(Arrays.asList("All", "Todo", "Reminder", "Appointment", "Personal Goals"));

        //Categories list
        HorizontalAdapter horizontalAdapter = new HorizontalAdapter(this.getContext(), categories.toArray(new String[0]));
        recyclerView.setAdapter(horizontalAdapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(this.getContext());
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (categories.size() > 0 & recyclerView != null) {
            recyclerView.setAdapter(new HorizontalAdapter(this.getContext(), categories.toArray(new String[0])));
        }

        recyclerView.setLayoutManager(MyLayoutManager);

        /*--------------------*/


        return view;
    }

    public void putArguments(Bundle bundleforFragment) {
        todolist.add(new Task("#", bundleforFragment.getString(TASK_TITLE), "Todo", temp, "12/12/2019"));
        updateTodolist();

    }

    public void updateTodolist(){

        //The list of task should be an Array-list, of course the different lists will be retrieved from the DB
        //I am extracting the lists
        ArrayList<String> t = new ArrayList<>();
        ArrayList<String> d = new ArrayList<>();
        ArrayList<String> dd = new ArrayList<>();
        for (int i=0; i<todolist.size(); i++){
            t.add(todolist.get(i).getTitle());
            d.add(todolist.get(i).getDescription());
            dd.add(todolist.get(i).getDuedate());
        }


        //Task list
        MyAdapter adapter = new MyAdapter(this.getContext(), t.toArray(new String[0]) ,d.toArray(new String[0]), dd.toArray(new String[0]));
        listView.setAdapter(adapter);
    }


    //Private class used in the on create method
    private class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String[] rTitle;
        String[] rDescription;
        String[] rDue;


        MyAdapter(Context c, String[] title, String[] description, String[] due) {
            super (c, R.layout.row, R.id.titleTodo, title);

            this.context = c;
            this.rTitle = title;
            this.rDescription = description;
            this.rDue = due;


        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);

            myTitle = row.findViewById(R.id.titleTodo);
            myDue = row.findViewById(R.id.dueTodo);
            myDescription = row.findViewById(R.id.descriptionTodo);

            myTitle.setText(rTitle[position]);
            myDue.setText(rDue[position]);
            myDescription.setText(rDescription[position]);


            return row;
        }
    }
}

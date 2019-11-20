package com.example.calendo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendo.HorizontalAdapter;
import com.example.calendo.MainActivity;
import com.example.calendo.R;

public class TodolistFragment extends Fragment {
    private TextView myTitle, myDescription, myDue;
    private CheckBox checkBox;
    private ListView listView;
    private RecyclerView recyclerView;

    String[] mCategory = {"All", "Todo", "Reminder", "Appointment", "Personal Goals"};
    String[] mTitle = {"Laundry", "Homework", "Group Meeting", "Shopping", "Dating", "Assignment HCI Seminar"};
    String[] mDescription = {"ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
            "ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
            "ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
            "ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
            "ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
            "ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"};

    String[] mDue = {"12/12/2019", "12/12/2019", "13/12/2019", "14/12/2019", "15/12/2019", "16/12/2019"};


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.to_do_list_fragment, container, false);

        listView = view.findViewById(R.id.listView);
        recyclerView = view.findViewById(R.id.categoryList);
        checkBox = view.findViewById(R.id.checkboxTask);

        /*---------- List ----------*/

        MyAdapter adapter = new MyAdapter(this.getContext(), mTitle, mDescription, mDue);
        listView.setAdapter(adapter);

        HorizontalAdapter horizontalAdapter = new HorizontalAdapter(this.getContext(), mCategory);
        recyclerView.setAdapter(horizontalAdapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(this.getContext());
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (mCategory.length > 0 & recyclerView != null) {
            recyclerView.setAdapter(new HorizontalAdapter(this.getContext(), mCategory));
        }

        recyclerView.setLayoutManager(MyLayoutManager);

        /*--------------------*/


        return view;
    }




















    //Private class used in the on create method
    private class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String rTitle[];
        String rDescription[];
        String rDue[];


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

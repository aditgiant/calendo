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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendo.AddNewTaskActivity;
import com.example.calendo.adapters.HorizontalAdapter;
import com.example.calendo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.example.calendo.AddNewTaskActivity.TASK_DATE;
import static com.example.calendo.AddNewTaskActivity.TASK_DESCRIPTION;
import static com.example.calendo.AddNewTaskActivity.TASK_TITLE;

public class TodolistFragment extends Fragment {
    private TextView myTitle, myDescription, myDue;
    private CheckBox checkBox;
    private ListView listView;
    private RecyclerView recyclerView;
    private TextView emptyTodo;

    //Data
    private ArrayList<Task> todolist;
    private ArrayList<String> categories;

    public static final String TASK_TITLE="title";
    public static final String TASK_CATEGORY="category";
    public static final String TASK_DATE="duedate";
    public static final String TASK_DESCRIPTION="description";
    private static final String TAG = "TodolistFragment";


    //Temp attribute for this long string
    private static final String temp="ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua";
    public static final int TEXT_REQUEST = 1;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteRef = db.collection("Todolist").document("MyTodo");
    private ListenerRegistration todolistenerRegistration;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.to_do_list_fragment, container, false);

        listView = view.findViewById(R.id.listView);
        recyclerView = view.findViewById(R.id.categoryList);
        checkBox = view.findViewById(R.id.checkboxTask);
        emptyTodo= view.findViewById(R.id.emptyTodo);



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



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        todolistenerRegistration =noteRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e!= null){
                    Toast.makeText(getContext(), "Error while loading", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                    return;
                }else{
                    if(documentSnapshot.exists()){
                        String title = documentSnapshot.getString(TASK_TITLE);
                        String category = documentSnapshot.getString(TASK_CATEGORY);
                        String duedate = documentSnapshot.getString(TASK_DATE);
                        String notes = documentSnapshot.getString(TASK_DESCRIPTION);

                        emptyTodo.setText(title);

                    }
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        todolistenerRegistration.remove();
    }

    public void putArguments(Bundle bundleforFragment) {
        todolist.add(new Task("#",
                    bundleforFragment.getString(TASK_TITLE),
                    "Todo",
                    bundleforFragment.getString(TASK_DESCRIPTION),
                    bundleforFragment.getString(TASK_DATE)));


        updateTodolist();

    }

    public void updateTodolist(){

        ArrayList<String> t = new ArrayList<>();
        ArrayList<String> d = new ArrayList<>();
        ArrayList<String> dd = new ArrayList<>();

        //The list of task should be an Array-list, of course the different lists will be retrieved from the DB
        //I am extracting the lists

        for (int i=0; i<todolist.size(); i++){
            t.add(todolist.get(i).getTitle());
            d.add(todolist.get(i).getDescription());
            dd.add(todolist.get(i).getDuedate());
        }

        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists()){
                            String title = documentSnapshot.getString(TASK_TITLE);
                            String category = documentSnapshot.getString(TASK_CATEGORY);
                            String duedate = documentSnapshot.getString(TASK_DATE);
                            String notes = documentSnapshot.getString(TASK_DESCRIPTION);

                           emptyTodo.setText(title);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        if(todolist.size()>0){
            emptyTodo.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
        else{
            emptyTodo.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
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

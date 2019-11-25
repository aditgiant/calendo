package com.example.calendo.fragments.todolist;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.calendo.adapters.HorizontalAdapter;
import com.example.calendo.R;
import com.example.calendo.adapters.MyAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;


public class TodolistFragment extends Fragment {

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
    private CollectionReference todoRef = db.collection("Todolist");


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

        todoRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!= null){
                    Toast.makeText(getContext(), "Error while loading", Toast.LENGTH_SHORT).show();
                }

                ArrayList<String> t = new ArrayList<>();
                ArrayList<String> d = new ArrayList<>();
                ArrayList<String> dd = new ArrayList<>();

                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    Task todolist = documentSnapshot.toObject(Task.class);
                    todolist.setId(documentSnapshot.getId());
                    String id = todolist.getId();
                    String title = todolist.getTitle();
                    String description = todolist.getDescription();
                    String duedate = todolist.getDuedate();
                    String category = todolist.getCategory();

                    t.add(title);
                    d.add(description);
                    dd.add(duedate);

                }

                listView.setVisibility(View.VISIBLE);
                emptyTodo.setVisibility(View.GONE);

                MyAdapter adapter = new MyAdapter(getContext(), t.toArray(new String[0]) ,d.toArray(new String[0]), dd.toArray(new String[0]));
                listView.setAdapter(adapter);

            }


    });
    }

    @Override
    public void onStop() {
        super.onStop();

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

      todoRef.get()
              .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                  @Override
                  public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                      ArrayList<String> t = new ArrayList<>();
                      ArrayList<String> d = new ArrayList<>();
                      ArrayList<String> dd = new ArrayList<>();

                        if(queryDocumentSnapshots.size()==0){
                            listView.setVisibility(View.GONE);
                            emptyTodo.setVisibility(View.VISIBLE);

                        }
                        for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Task todolist = documentSnapshot.toObject(Task.class);
                            todolist.setId(documentSnapshot.getId());
                            String id = todolist.getId();
                            String title = todolist.getTitle();
                            String description = todolist.getDescription();
                            String duedate = todolist.getDuedate();
                            String category = todolist.getCategory();

                            t.add(title);
                            d.add(description);
                            dd.add(duedate);

                        }

                      listView.setVisibility(View.VISIBLE);
                      emptyTodo.setVisibility(View.GONE);


                      MyAdapter adapter = new MyAdapter(getContext(), t.toArray(new String[0]) ,d.toArray(new String[0]), dd.toArray(new String[0]));
                      listView.setAdapter(adapter);

                  }
              });


    }



}

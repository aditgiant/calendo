package com.example.calendo.fragments.todolist;


import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.example.calendo.App.Channel1;


public class TodolistFragment extends Fragment   {

    private CheckBox checkBox;
    private ListView listView;
    private RecyclerView recyclerView;
    private TextView emptyTodo;
    private NotificationManagerCompat notificationManager;

    //Data
    private ArrayList<Task> todolist;
    private ArrayList<String> categories;


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
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(this.getContext());
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (categories.size() > 0 & recyclerView != null) {
            recyclerView.setAdapter(new HorizontalAdapter(this.getContext(), categories.toArray(new String[0]),new HorizontalAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerView.ViewHolder holder) {

                    filterList(categories.get(holder.getAdapterPosition()));

                   // Toast.makeText(getContext(), "Item position"+ categories.get(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                }
            }));
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

                renderList(queryDocumentSnapshots);

            }
    });
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public void putArguments(Bundle bundleforFragment) {
        updateTodolist();

    }

    public void updateTodolist(){

      todoRef.get()
              .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                  @Override
                  public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                      renderList(queryDocumentSnapshots);

                  }
              });


    }

    private void renderList(QuerySnapshot queryDocumentSnapshots){
        final ArrayList<String> t = new ArrayList<>();
        final ArrayList<String> d = new ArrayList<>();
        final ArrayList<String> dd = new ArrayList<>();
        final ArrayList<String> IDs = new ArrayList<>();

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
            IDs.add(id);

            SimpleDateFormat formatter= new SimpleDateFormat("MM/dd/yyyy");
            Date date = new Date(System.currentTimeMillis());
            notificationManager = NotificationManagerCompat.from(this.getContext());
            String newDate = formatter.format(date);
            System.out.println(formatter.format(date));
            if(newDate.equals(duedate)){
               renderPushNotification(title);

            }

        }

        listView.setVisibility(View.VISIBLE);
        emptyTodo.setVisibility(View.GONE);


        MyAdapter adapter = new MyAdapter(getContext(), t.toArray(new String[0]) ,d.toArray(new String[0]), dd.toArray(new String[0]), IDs.toArray(new String[0]));
        listView.setAdapter(adapter);

        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //UI effects
                onSelectCheckBox(view);

                //Data managmenet

                //Here I need the ID of the selected item
                todoRef.document(IDs.get(position)).delete();
                updateTodolist();



            }
        });

         */
    }

    private void renderPushNotification(String title){
        Notification notification = new NotificationCompat.Builder(this.getContext(),Channel1)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Please complete by today")
                .setContentText(title)
                .setAutoCancel(true)
                .setColor(Color.RED)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        notificationManager.notify(1,notification);


    }

    private void filterList (String item){
        if(item.equals("All")){
            updateTodolist();
        }else {
            todoRef.whereEqualTo("category", item)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            renderList(queryDocumentSnapshots);

                        }
                    });

        }

    }

}

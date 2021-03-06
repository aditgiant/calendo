package com.example.calendo.fragments.todolist;


import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendo.EditTaskActivity;
import com.example.calendo.MainActivity;
import com.example.calendo.NewCategoryActivity;
import com.example.calendo.R;
import com.example.calendo.adapters.HorizontalAdapter;
import com.example.calendo.adapters.MyAdapter;
import com.example.calendo.adapters.MyViewHolder;
import com.google.android.gms.common.util.Hex;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.calendo.App.Channel1;
import static com.example.calendo.utils.User.MY_PREFS_NAME;


public class TodolistFragment extends Fragment   {

    private ListView listView;
    private RecyclerView recyclerView;
    private ConstraintLayout emptyTodo;
    private NotificationManagerCompat notificationManager;
    private TextView categoryName;

    //Data
    private ArrayList<Task> todolist;
    private ArrayList<String> categories;
    private String userID;


    public static final int TEXT_REQUEST = 1;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();




    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.to_do_list_fragment, container, false);

        listView = view.findViewById(R.id.listView);
        recyclerView = view.findViewById(R.id.categoryList);
        emptyTodo= view.findViewById(R.id.emptyTodo);
        categoryName = view.findViewById(R.id.categoryName);

        //Retrieve userID
        SharedPreferences sharedPref = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        this.userID = sharedPref.getString("userID", "NOUSERFOUND");


        //Update the list
        todolist = new ArrayList<>();


        //Fill the categories list with user categories, attach a listener method that updates automatically

        CollectionReference usersRef2 = db.collection("Users").document(this.userID).collection("categories");

        //We need this listener each time we delete a category
        usersRef2.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {              if(e!= null){
                Toast.makeText(getContext(), "Error while loading", Toast.LENGTH_SHORT).show();
            }

                getCategories();

            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        //Update to do list

        CollectionReference usersRef = db.collection("Users").document(this.userID).collection("list");

        //We need this listener each time we complete a task to update the list

        usersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!= null){
                    Toast.makeText(getContext(), "Error while loading", Toast.LENGTH_SHORT).show();
               }

                updateTodolist();

            }
        });



    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public void updateTodolist(){

        CollectionReference usersRef = db.collection("Users").document(this.userID).collection("list");

        //Take just the uncompleted tasks
        usersRef.whereEqualTo("status", "uncompleted").get()
           .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                  @Override
                  public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                      renderList(queryDocumentSnapshots);

                      //You are refreshing the whole list
                      categoryName.setText("All");

                  }
              });


    }



    public void getCategories(){

        categories = new ArrayList<>();
        categories.add("All");

        //Retrieve user categories
        CollectionReference usersRef = db.collection("Users").document(this.userID).collection("categories");

        usersRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                categories.add(document.getString("categoryName"));

                            }

                            //Add the last category
                            categories.add("New category");

                            //Display the categories
                            renderCategories();

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    private void renderList(QuerySnapshot queryDocumentSnapshots){
        final ArrayList<String> t = new ArrayList<>();
        final ArrayList<String> n = new ArrayList<>();
        final ArrayList<String> d = new ArrayList<>();
        final ArrayList<String> IDs = new ArrayList<>();
        final ArrayList<String> c = new ArrayList<>();
        final ArrayList<String> s = new ArrayList<>();

        if(queryDocumentSnapshots.size()==0){
            listView.setVisibility(View.GONE);
            emptyTodo.setVisibility(View.VISIBLE);

        }
        for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
            Task todolist = documentSnapshot.toObject(Task.class);


            todolist.setId(documentSnapshot.getId());
            String id = todolist.getId();
            String title = todolist.getTitle();
            String notes = todolist.getNotes();
            String dates = todolist.getDate();
            String category = todolist.getCategory();
            String status = todolist.getStatus();
            String date ="";

           if(dates != null && !dates.equals("")) {

                date = dates.substring(6, 8) + "/" + dates.substring(4, 6) + "/" + dates.substring(0, 4);
           }


            t.add(title);
            n.add(notes);
            d.add(date);
            IDs.add(id);
            c.add(category); //Not passed to the adapter
            s.add(status);

            if(getContext()!=null) {

                MyAdapter adapter = new MyAdapter(getContext(), t.toArray(new String[0]), n.toArray(new String[0]), d.toArray(new String[0]), IDs.toArray(new String[0]), s.toArray(new String[0]));
                listView.setAdapter(adapter);
            }


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    String selectedTitle = adapterView.getItemAtPosition(position).toString();
                    Toast.makeText(getActivity(), listView.getAdapter().getItem(position).toString() +" is selected", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), EditTaskActivity.class);
                    intent.putExtra("Title", selectedTitle);
                    startActivity(intent);
                }
            });


            /*------- DATE TO BE FIXED ------
            SimpleDateFormat formatter= new SimpleDateFormat("MM/dd/yyyy");
            Date date = new Date(System.currentTimeMillis());
            notificationManager = NotificationManagerCompat.from(this.getContext());
            String newDate = formatter.format(date);
            System.out.println(formatter.format(date));
            if(newDate.equals(duedate)){
               renderPushNotification(title);

            }

             */

        }

        if(queryDocumentSnapshots.isEmpty()){
            listView.setVisibility(View.GONE);
            categoryName.setVisibility(View.VISIBLE);
            emptyTodo.setVisibility(View.VISIBLE);

        }else {
            listView.setVisibility(View.VISIBLE);
            categoryName.setVisibility(View.VISIBLE);
            emptyTodo.setVisibility(View.GONE);
        }


    }

    private void renderCategories(){
        //Categories list
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(this.getContext());
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (categories.size() > 0 & recyclerView != null) {
            recyclerView.setAdapter(new HorizontalAdapter(this.getContext(), categories.toArray(new String[0]),new HorizontalAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerView.ViewHolder holder) {

                    //Filter list
                    filterList(categories.get(holder.getAdapterPosition()));

                    //Set the list title if it is not the last one
                    if(!categories.get(holder.getAdapterPosition()).equals("New category")){
                        categoryName.setText(categories.get(holder.getAdapterPosition()));
                    }

                    // Toast.makeText(getContext(), "Item position"+ categories.get(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                }
            }));
        }

        recyclerView.setLayoutManager(MyLayoutManager);

        //Terminate loading spinner started by the activity
        if(getActivity() != null){
            ((MainActivity)getActivity()).endLoadingSpinner();
        }


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

        switch (item){
            case "All":
                updateTodolist();
                break;

            case "New category":
                Intent i = new Intent( getActivity(), NewCategoryActivity.class);
                startActivity(i);
                break;

                default:

                    CollectionReference usersRef = db.collection("Users").document(this.userID).collection("list");

                    usersRef.whereEqualTo("category", item).whereEqualTo("status", "uncompleted")
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                    renderList(queryDocumentSnapshots);

                                }
                            });


        }

    }

    public TextView getCategoryName() {
        return categoryName;
    }
}

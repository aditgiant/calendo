package com.example.calendo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.calendo.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

//Private class used in the on create method
public class MyAdapter extends ArrayAdapter<String> {
    Context context;
    String[] rTitle;
    String[] rDescription;
    String[] rDue;
    String[] IDs;
    private TextView myTitle, myDescription, myDue;
    private CheckBox myCheckbox;

    //DB Connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference todoRef = db.collection("Todolist");


    public MyAdapter(Context c, String[] title, String[] description, String[] due, String[] IDs) {
        super (c, R.layout.row, R.id.titleTodo, title);

        this.context = c;
        this.rTitle = title;
        this.rDescription = description;
        this.rDue = due;
        this.IDs= IDs;


    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row, parent, false);
        }
        myTitle = convertView.findViewById(R.id.titleTodo);
        myDue = convertView.findViewById(R.id.dueTodo);
        myDescription = convertView.findViewById(R.id.descriptionTodo);
        myCheckbox = convertView.findViewById(R.id.checkboxTask);

        myTitle.setText(rTitle[position]);
        myDue.setText(rDue[position]);
        myDescription.setText(rDescription[position]);



        //Listener to to delete the item when you check the checkbox
        myCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 0,75s = 750ms

                        //Delete the content
                        //Here I need the ID of the selected item
                        todoRef.document(IDs[position]).delete();
                    }
                }, 750);



            }
        });



        return convertView;
    }
}
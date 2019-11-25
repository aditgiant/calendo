package com.example.calendo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calendo.fragments.DatePickerFragment;
import com.example.calendo.fragments.todolist.Task;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddNewTaskActivity extends AppCompatActivity {
    private FloatingActionButton fab_save;
    private EditText title;
    private TextView date;
    private EditText notes;
    private Spinner dropdownCategory;

    //Data
    private ArrayList<String> categories;

    public static final String TASK_TITLE="title";
    public static final String TASK_CATEGORY="category";
    public static final String TASK_DATE="duedate";
    public static final String TASK_DESCRIPTION="description";
    private static final String TAG = "AddNewTaskActivity";

    //Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference todoRef = db.collection("Todolist");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);

        //Link UI elements
        fab_save = findViewById(R.id.fab_save);
        title = findViewById(R.id.TaskName);
        date = findViewById(R.id.TaskTimeLabel);
        notes = findViewById(R.id.notes);
        dropdownCategory = findViewById(R.id.categories);

        //Fill dropdown
        // you need to have a list of data that you want the spinner to display
        categories =  new ArrayList<String>();
        categories.add("To do");
        categories.add("Reminder");
        categories.add("Appointment");
        categories.add("Personal Goals");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownCategory.setAdapter(adapter);

    }

    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }

    public void processDatePickerResult(int year, int month, int day) {
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (month_string +
                "/" + day_string + "/" + year_string);
        TextView tasktimelabel = findViewById(R.id.TaskTimeLabel);
        tasktimelabel.setText(dateMessage);
        Toast.makeText(this, "Date selected", Toast.LENGTH_SHORT).show();
    }

    public void saveTask(View view){

        Task todolist = new Task("#", title.getText().toString(),dropdownCategory.getSelectedItem().toString(),notes.getText().toString(), date.getText().toString() );
        todoRef.add(todolist);
        finish();


    }
}

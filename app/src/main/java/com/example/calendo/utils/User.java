package com.example.calendo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.example.calendo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class User {
    private String userID;
    private String name;
    private String lastName;
    private String email;
    private ArrayList<String> categories;

    private TextView drawerName;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static final String MY_PREFS_NAME = "MyPrefsFile";


    public User(String userID, final TextView drawerName, Activity activity) {
        this.userID = userID;

        //Store user id in the stared preferences to avoid login each time
        SharedPreferences sharedPref = activity.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("userID", userID);
        editor.apply();



        //Retrieve user information
        CollectionReference usersRef = db.collection("Users");

        usersRef.whereEqualTo(FieldPath.documentId(),userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                setName(document.getString("name"));
                                setLastName(document.getString("lastName"));
                                setEmail(document.getString("email"));

                                //Set drawerName
                                drawerName.setText(getName()+" "+getLastName());

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



    }

    //Getter and Setter


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

package com.example.calendo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.calendo.fragments.todolist.Task;
import com.example.calendo.utils.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.calendo.utils.User.MY_PREFS_NAME;

public class EditCategoryActivity extends AppCompatActivity {
    private EditText categoryName;
    private FloatingActionButton save;
    private String oldCategory;


    //Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        //Link UI elements
        categoryName = findViewById(R.id.categoryName);
        save = findViewById(R.id.b_save_category);

        //Get the value of the old category
        Intent i = getIntent();
        oldCategory = i.getStringExtra("categoryName");

        categoryName.setText(oldCategory);
    }

    public void saveCategory(View view) {

        //The only mandatory field is the title

        if(categoryName.getText().toString().equals("")){
            Toast.makeText(this, "You did not enter a name for the category!", Toast.LENGTH_SHORT).show();
        } else {
            //Category name has been inserted

            //Retrieve the userID
            SharedPreferences sharedPref = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String userID = sharedPref.getString("userID", "NOUSERFOUND");


            //Now edit the category

            //Modify all the items of that category setting the new category name
            usersRef.document(userID).collection("list")
                    .whereEqualTo("category", oldCategory)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                System.out.println("Here");

                                System.out.println(task.getResult().size());

                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    //Update the document known the ID of the task
                                    System.out.println(document.getId());
                                    usersRef.document(userID).collection("list").document(document.getId()).update("category",categoryName.getText().toString());

                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });


            //Update the category name
            usersRef.document(userID).collection("categories")
                    .whereEqualTo("categoryName", oldCategory)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    //Update the document known the ID of the task
                                    usersRef.document(userID).collection("categories").document(document.getId()).update("categoryName",categoryName.getText().toString());

                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });



            //Once the update is finished
            finish();

        }

    }



}



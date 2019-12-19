package com.example.calendo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.calendo.fragments.todolist.Task;
import com.example.calendo.utils.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.calendo.utils.User.MY_PREFS_NAME;

public class NewCategoryActivity extends AppCompatActivity {
    private EditText categoryName;
    private FloatingActionButton save;


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


            //Now save the category
            usersRef.document(userID).collection("categories").add(new Category(categoryName.getText().toString()));
            finish();

        }

    }
}

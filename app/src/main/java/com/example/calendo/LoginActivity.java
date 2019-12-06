package com.example.calendo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class LoginActivity extends AppCompatActivity {
    private EditText email;
    private EditText pw;
    private Button login_b;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Link UI elements
        email = findViewById(R.id.emailtext);
        email.setText("ciao@gmail.com");
        pw = findViewById(R.id.pw);
        pw.setText("1234");
        login_b = findViewById(R.id.login_button);

    }


    public void login(View view) {

        //README: Enable the line below and comment the Intent to connect to the DB

        validateLogin(email.getText().toString(), pw.getText().toString());

/*
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();

 */

    }

    public boolean validateLogin(String email, final String pw){

        //Search for users with the inserted email

        this.db.collection("Users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if(task.getResult().isEmpty()){
                                //Case user not present
                                Toast.makeText(getApplicationContext(), "This user does not exists!", Toast.LENGTH_SHORT).show();
                            } else {

                                //Case user present
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData().get("pw"));


                                    if(pw.equals(document.getData().get("pw"))){

                                        //Correct password
                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);

                                        //Send to the main activity the root element of the DB -> User ID
                                        i.putExtra("userID", document.getId());
                                        startActivity(i);
                                        finish();
                                    } else {
                                        //Wrong password
                                        Toast.makeText(getApplicationContext(), "Wrong password, try again!", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        return true;

    }



}

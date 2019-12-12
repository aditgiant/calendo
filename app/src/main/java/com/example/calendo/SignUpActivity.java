package com.example.calendo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.calendo.utils.InputValidator;
import com.example.calendo.utils.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SignUpActivity extends AppCompatActivity {
    private EditText name;
    private EditText lastName;
    private EditText email;
    private EditText pw1;
    private EditText pw2;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Link UI elements
        name = findViewById(R.id.name);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        pw1 = findViewById(R.id.pw);
        pw2 = findViewById(R.id.r_pw);
    }


    public void validateSignup(View view) {

        if(InputValidator.isEmpty(name) ||
                InputValidator.isEmpty(lastName) ||
                InputValidator.isEmpty(email) ||
                InputValidator.isEmpty(pw1) ||
                InputValidator.isEmpty(pw2)){

            //Display toast
            Toast.makeText(getApplicationContext(), "A field is empty!", Toast.LENGTH_SHORT).show();
        } else {
            if(InputValidator.samePassword(pw1,pw2)){
                //Case where each field contains information and the two password are the same

                //Verify that the user is not already present with that password and in case make the entry
                //TEMPORARY: Password is stored plaintext, security issues are not our goal now
                User user = new User(name.getText().toString(), lastName.getText().toString(), email.getText().toString(), pw1.getText().toString());
                InputValidator.finalizeSignUp(this, email, user);

            } else {
                //Display toast
                Toast.makeText(getApplicationContext(), "You typed two different passwords", Toast.LENGTH_SHORT).show();
            }
        }

    }
}

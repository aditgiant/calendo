package com.example.calendo.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.calendo.LoginActivity;
import com.example.calendo.MainActivity;
import com.example.calendo.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class InputValidator {

    public static boolean isEmpty(EditText field){
        if(field.getText().toString().equals("")){
            return true;
        } else {
            return false;
        }


    }

    public static boolean samePassword(EditText pw1, EditText pw2){
        if(pw1.getText().toString().equals(pw2.getText().toString())){
            return true;
        } else {
            return false;
        }
    }

    public static boolean finalizeSignUp(Context c, EditText email, User user){

        //Check that the user is not present

        FirebaseFirestore.getInstance().collection("Users")
                .whereEqualTo("email", email.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if(task.getResult().isEmpty()){
                                //Case user not present make the insert
                                FirebaseFirestore.getInstance().collection("Users")
                                        .add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                        //Once the account is created automatically login
                                        Intent i = new Intent(c, MainActivity.class);

                                        //Retrieve the new created ID

                                        //Send to the main activity the root element of the DB -> User ID
                                        i.putExtra("userID", task.getResult().getId());
                                        c.startActivity(i);

                                    }
                                });


                            } else {

                                //Case user present
                                Toast.makeText(c, "Account with this email already present!", Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        return true;

    }

}

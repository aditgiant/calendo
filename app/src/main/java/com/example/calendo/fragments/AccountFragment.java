package com.example.calendo.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.calendo.LoginActivity;
import com.example.calendo.MainActivity;
import com.example.calendo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.calendo.utils.User.MY_PREFS_NAME;

public class AccountFragment extends Fragment {
    private EditText name;
    private EditText lastName;
    private EditText email;
    private EditText pw;
    private EditText new_pw;
    private EditText r_new_pw;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        //Link UI items
        name = view.findViewById(R.id.account_name);
        lastName = view.findViewById(R.id.account_lastname);
        email = view.findViewById(R.id.account_email);
        pw = view.findViewById(R.id.account_pw);
        new_pw = view.findViewById(R.id.account_new_pw);
        r_new_pw = view.findViewById(R.id.account_r_new_pw);


        //Retrieve the userID
        SharedPreferences sharedPref = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String userID = sharedPref.getString("userID", "NOUSERFOUND");

        //Fill the editTexts with the actual data
        DocumentReference usersRef = db.collection("Users").document(userID);

        usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        //Insert the text in the editText
                        name.setText(document.get("name").toString());
                        lastName.setText(document.get("lastName").toString());
                        email.setText(document.get("email").toString());


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        return view;
    }

    public void saveAccount(View view) {

        //Retrieve the userID
        SharedPreferences sharedPref = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String userID = sharedPref.getString("userID", "NOUSERFOUND");

        //Get actual password
        DocumentReference usersRef = db.collection("Users").document(userID);

        usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        if(pw.getText().toString().equals(document.get("pw"))){
                            //Password is correct update allowed

                            usersRef.update("name",name.getText().toString());
                            usersRef.update("lastName",lastName.getText().toString());
                            usersRef.update("email",email.getText().toString());

                            //Check if the user wants to change passwords
                            if(!new_pw.getText().toString().equals("")){
                                //If the user wants to change the password
                                if(new_pw.getText().toString().equals(r_new_pw.getText().toString())){
                                    //The user inserted the same passwords
                                    usersRef.update("pw",new_pw.getText().toString());

                                    //Restart the MainActivity
                                    Intent i = new Intent(getActivity(), MainActivity.class);

                                    //Send to the main activity the root element of the DB -> User ID
                                    i.putExtra("userID", document.getId());
                                    startActivity(i);
                                    getActivity().finish();

                                } else {
                                    //Wrong password
                                    Toast.makeText(getContext(), "You inserted two different passwords!", Toast.LENGTH_SHORT).show();
                                }


                            } else {

                                //The password remains the same

                                //Restart the MainActivity
                                Intent i = new Intent(getActivity(), MainActivity.class);

                                //Send to the main activity the root element of the DB -> User ID
                                i.putExtra("userID", document.getId());
                                startActivity(i);
                                getActivity().finish();
                            }





                        } else {
                            //Wrong password
                            Toast.makeText(getContext(), "Wrong password, try again!", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }

}



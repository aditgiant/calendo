package com.example.calendo.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.calendo.R;
import com.example.calendo.fragments.todolist.Task;
import com.example.calendo.utils.QuotesService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.Context.MODE_PRIVATE;
import static com.example.calendo.utils.User.MY_PREFS_NAME;

//Private class used in the on create method
public class MyAdapter extends ArrayAdapter<String> {
    Context context;
    String[] rTitle;
    String[] rDescription;
    String[] rDue;
    String[] IDs;
    String[] stat;
    private TextView myTitle, myDescription, myDue;
    private CheckBox myCheckbox;
    private QuotesService quotesService;

    //DB Connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference todoRef = db.collection("Todolist");
    private CollectionReference usersRef = db.collection("Users");
    SharedPreferences sharedPref = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
    final String userID = sharedPref.getString("userID", "NOUSERFOUND");
    private SparseBooleanArray mCheckStates;


    public MyAdapter(Context c, String[] title, String[] description, String[] due, String[] IDs, String[] status) {
        super (c, R.layout.row, R.id.titleTodo, title);

        this.context = c;
        this.rTitle = title;
        this.rDescription = description;
        this.rDue = due;
        this.IDs= IDs;
        this.stat = status;

        //Link the quote service, will be used on the checked handler
        this.quotesService = new QuotesService();

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
        myCheckbox.setChecked(stat[position].equals("completed"));


        final String[] status = new String[1];



            //Listener to to delete the item when you check the checkbox
            myCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {



                    //Show the inspirational quotes once the task has been completed
                    quotesService.retrieveQuote(getContext());

                    //Retrieve userID

                    //Retrieve the userID




                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 0,75s = 750ms

                            //Delete the content
                            //Here I need the ID of the selected item
                            usersRef.document(userID).collection("list").document(IDs[position]).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Toast.makeText(context, documentSnapshot.get("title").toString(), Toast.LENGTH_SHORT).show();


                                    if(documentSnapshot.get("status").toString().equals("uncompleted")){
                                        status[0] = "completed";
                                    }else{
                                       status[0] = "uncompleted";
                                    }
                                    Task updateStatus = new Task("#", documentSnapshot.get("title").toString(), documentSnapshot.get("category").toString(), documentSnapshot.get("notes").toString(), documentSnapshot.get("date").toString(), status[0]);

                                    usersRef.document(userID).collection("list").document(IDs[position]).set(updateStatus);


                                }
                            });


                        }
                    }, 750);


                }
            });




        return convertView;
    }

    @Override
    public String getItem(int position){
        return rTitle[position];

    }




}
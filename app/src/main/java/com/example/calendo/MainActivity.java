package com.example.calendo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calendo.fragments.AccountFragment;
import com.example.calendo.fragments.StatisticsFragment;
import com.example.calendo.fragments.todolist.TodolistFragment;
import com.example.calendo.fragments.calendar.CalendarFragment;
import com.example.calendo.utils.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.calendo.AddNewTaskActivity.TASK_DATE;
import static com.example.calendo.AddNewTaskActivity.TASK_DESCRIPTION;
import static com.example.calendo.AddNewTaskActivity.TASK_TITLE;
import static com.example.calendo.fragments.todolist.TodolistFragment.TEXT_REQUEST;
import static com.example.calendo.utils.User.MY_PREFS_NAME;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private TextView drawerName;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    private Boolean calendarViewOn= false;
    private Bundle bundleforFragment;
    private TodolistFragment todolistFragment;

    private ProgressDialog nDialog;

    //Reference to the logged user
    private User user;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AccountFragment account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Show loading Spinenr
        nDialog = new ProgressDialog(this);
        showLoadingSpinner();

        //Link UI elements
        fab = findViewById(R.id.fab);

        //Set the name in the Drawer
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        drawerName = (TextView) headerView.findViewById(R.id.drawerName);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set the drawer to change the displayed fragment
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //Make the toggle rotating
        actionBarDrawerToggle.syncState();


        //Handle if the activity is created from the login or the create new task
        Intent intent= getIntent();

        if(intent.getStringExtra("userID")==null){
            //Retrieve userID
            SharedPreferences sharedPref = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String userID = sharedPref.getString("userID", "NOUSERFOUND");

            //Compose the user object and set the name in the drawer taking the id from the shared preferences
            user = new User(userID, drawerName, this);
        } else {

            //Compose the user object and set the name in the drawer taking the id from the login page
            user = new User(intent.getStringExtra("userID"), drawerName, this);
        }


        //Load the correct home pages listView or Calendar View
        if(calendarViewOn){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new CalendarFragment()).commit();
        } else {
            todolistFragment = new TodolistFragment();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    todolistFragment).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the toolbar if it is present.
        getMenuInflater().inflate(R.menu.bar_menu, menu);
        return true;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml

        switch (item.getItemId()) {
            case R.id.calendar_button:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CalendarFragment()).commit();

                calendarViewOn=true;
                toolbar.setTitle("Calendar");

                fab.setVisibility(View.VISIBLE);


                break;
            case R.id.todolist_button:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TodolistFragment()).commit();
                toolbar.setTitle("Todo List");


                calendarViewOn= false;

                fab.setVisibility(View.VISIBLE);

                break;
        }


        return super.onOptionsItemSelected(item);
    }


    //Implements the method to select the fragment, I inserted implements to do this
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_statistics:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new StatisticsFragment()).commit();
                toolbar.setTitle("Statistics");

                fab.setVisibility(View.INVISIBLE);


                break;
            case R.id.nav_account:
                account = new AccountFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        account).commit();
                toolbar.setTitle("Account");

                fab.setVisibility(View.INVISIBLE);

                break;

            case R.id.nav_logout:

                logOut();

                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void addNewTask(View view) {
        Intent intent = new Intent(MainActivity.this, AddNewTaskActivity.class);
        intent.putExtra("category", todolistFragment.getCategoryName().getText().toString());
        startActivityForResult(intent, TEXT_REQUEST);

    }

    public void logOut(){
        SharedPreferences sharedPref = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("userID");
        editor.apply();

        //Back to the login
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        finish();

    }

    //Loading Spinner Methods

    public void showLoadingSpinner(){

        //Activate progress spinner
        nDialog.setMessage("Wait a moment..");
        nDialog.setTitle("Loading");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
    }

    public void endLoadingSpinner(){
        nDialog.dismiss();
    }

    public void saveAccount(View view) {

        account.saveAccount(view);

    }

}



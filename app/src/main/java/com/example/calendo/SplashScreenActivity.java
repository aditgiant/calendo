package com.example.calendo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

import static com.example.calendo.utils.User.MY_PREFS_NAME;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        SharedPreferences sharedPref = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String value = sharedPref.getString("userID", "NOUSERFOUND");
        System.out.println(value);

        new Handler().postDelayed(new myRunnable(value), 2000);


    }


    private class myRunnable implements Runnable {
        private String userID;

        public myRunnable(String userID) {
            this.userID = userID;
        }

        @Override
        public void run() {

            Intent i;

            //Based on the presence of userID in the shared preferences decide to
            // show the log in or the main activity

            if(this.userID.equals("NOUSERFOUND")){
                i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                System.out.println("1");
            } else {
                i = new Intent(SplashScreenActivity.this, MainActivity.class);
                i.putExtra("userID", this.userID);
                System.out.println("2");
            }

            startActivity(i);
            finish();

        }
    }


}

package com.example.calendo.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.calendo.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.VolleyLog.TAG;

public class QuotesService {


    public void retrieveQuote(final Context context){
        //Retrieve the content from Inspirational Quotes API

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="https://api.forismatic.com/api/1.0/?method=getQuote&lang=en&format=json";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    //Do it with this it will work
                    JSONObject person = new JSONObject(response);
                    String quote = person.getString("quoteText");
                    String author = person.getString("quoteAuthor");

                    //Log.d(TAG, quote+author);

                    //Visualize Alert to show quote
                    showQuote(quote,author,context);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               //
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void showQuote(String quote, String author, Context context){

        new AlertDialog.Builder(context)
                .setTitle("Inspirational quote")
                .setMessage(Html.fromHtml("<i><p>"+quote+"<p></i>"+"<b><i>"+author+"<i></b>"))

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("Dismiss", null)
                .setIcon(R.drawable.ic_favorite)
                .show();

    }

}

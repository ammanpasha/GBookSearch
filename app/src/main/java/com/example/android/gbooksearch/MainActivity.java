package com.example.android.gbooksearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String initURL = "https://www.googleapis.com/books/v1/volumes?";
    private String finalURL = "";
    private final String LOG_TAG = "Logged: ";
    private String searchQuery;
    private String parm1;
    private String parm2;
    private EditText searchView ;
    private Spinner sort_spinner;
    private Spinner result_spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        searchView = (EditText)findViewById(R.id.search_view);

        sort_spinner = (Spinner)findViewById(R.id.sort_by_spinner);
        List<String> sort_options =  new ArrayList<String>();
        sort_options.add("Relevance");
        sort_options.add("Newest");
        ArrayAdapter<String> sort_options_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, sort_options);
        sort_options_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort_spinner.setAdapter(sort_options_adapter);


        result_spinner = (Spinner)findViewById(R.id.max_results_spinner);
        List<String> max_result_options =  new ArrayList<String>();
        max_result_options.add("5");
        max_result_options.add("10");
        max_result_options.add("20");
        max_result_options.add("All");
        ArrayAdapter<String> max_result_options_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, max_result_options);
        max_result_options_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        result_spinner.setAdapter(max_result_options_adapter);

    }

    public void searchBtn(View v)
    {

        if(searchView.getText().toString().matches(""))
        {
            Toast.makeText(this, "Please enter a search query.", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG,"Seachview query is NULL");

        }
        else
        {
            searchQuery = searchView.getText().toString();
            parm1 = sort_spinner.getSelectedItem().toString();
            parm2 = result_spinner.getSelectedItem().toString();
            Log.i(LOG_TAG,"searchQuery: " + searchQuery + "\nparm1: " + parm1 + "\nparm2: " + parm2);
            finalURL = modifyURL(initURL);
            Intent i = new Intent(MainActivity.this,ResultActivity.class);
            i.putExtra("url",finalURL); //passing finalURL to ResultActivity
            startActivity(i); //launches new activity.
        }

    }

    private String modifyURL(String initURL)
    {
        if(parm2.matches("All"))
        {
            finalURL = initURL + "q=" + searchQuery + "&orderBy=" + parm1;
        }
        else
        {
            finalURL = initURL + "q=" + searchQuery + "&maxResults=" + parm2 + "&orderBy=" + parm1;
        }
        Log.i(LOG_TAG,"Logged: Final URL is: " + finalURL);
        return finalURL;
    }


}

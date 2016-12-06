package com.florafinder.invasive_species;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SpeciesPickerActivity extends AppCompatActivity {

    private double lat, lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_species_picker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        lat = getIntent().getExtras().getDouble("lat");
        lang = getIntent().getExtras().getDouble("lang");
    }

    private void setUpList(){

        final ListView view = (ListView) findViewById(R.id.list_species);
        String[] values = new String[] {"BuckThorn"};

        final ArrayList<String> listStrings = new ArrayList();

        for(String string: values){
            listStrings.add(string);
        }

        final ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, listStrings);
        view.setAdapter(adapter);
    }

    /**
     * Return to the map with a selected string
     * @param view
     */
    public void returnToMap(View view){

        Intent returnIntent = new Intent();
        returnIntent.putExtra("species", ((TextView) view).getText());
        returnIntent.putExtra("lat", lat);
        returnIntent.putExtra("lang", lang);
        setResult(Activity.RESULT_OK,returnIntent);

        this.finish(); //kill activity
    }
}

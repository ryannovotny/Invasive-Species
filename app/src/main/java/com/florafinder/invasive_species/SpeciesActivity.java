package com.florafinder.invasive_species;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Ryan on 11/2/16.
 */
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SpeciesActivity extends AppCompatActivity {

    TextView speciesName;
    TextView speciesDescription;
    ImageView speciesPhoto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_species);

        speciesName = (TextView)findViewById(R.id.person_name);
        speciesDescription = (TextView)findViewById(R.id.person_age);
        speciesPhoto = (ImageView)findViewById(R.id.person_photo);

        speciesName.setText("Buckthorn");
        speciesDescription.setText("23 years old");
        speciesPhoto.setImageResource(R.drawable.buckthorn);
    }
}
package com.florafinder.invasive_species;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SpeciesActivity extends Activity {

    TextView speciesName;
    TextView speciesDescription;
    ImageView speciesPhoto;
    TextView speciesRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.species_activity);
        speciesName = (TextView)findViewById(R.id.species_name);
        speciesDescription = (TextView)findViewById(R.id.species_description);
        speciesPhoto = (ImageView)findViewById(R.id.species_photo);
        speciesRemove = (TextView) findViewById(R.id.species_remove);

        speciesName.setText("Buckthorn");
        speciesDescription.setText("23 years old");
        speciesPhoto.setImageResource(R.drawable.buckthorn);
        speciesRemove.setText("How to remove: ");
    }
}
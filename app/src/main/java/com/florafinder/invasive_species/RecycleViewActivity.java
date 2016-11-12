package com.florafinder.invasive_species;

/**
 * Created by lando on 11/12/2016.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends Activity {

    private List<InvasiveSpecies> species;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recyclerview_activity);

        rv=(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();
    }

    private void initializeData(){
        species = new ArrayList<>();
        species.add(new InvasiveSpecies("Buckthorn", "23 years old", R.drawable.buckthorn));
        species.add(new InvasiveSpecies("Creeping Charlie", "25 years old", R.drawable.charlie));
        species.add(new InvasiveSpecies("Poison Hemlock", "35 years old", R.drawable.hemlock));
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(species);
        rv.setAdapter(adapter);
    }
}
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

    private List<Species> species;
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
        species.add(new Species("Buckthorn", "A shrub or small tree of the buckthorn family, typically bearing thorns. Some kinds yield dyes, and others have been used medicinally.", R.drawable.buckthorn));
        species.add(new Species("British Yellowhead", "A Eurasian species of plants in the genus Inula within the daisy family. It is widespread across much of Europe and Asia, and sparingly naturalized in scattered locations in North America", R.drawable.british));
        species.add(new Species("Common Thissle", "Thistle is the common name of a group of flowering plants characterised by leaves with sharp prickles on the margins, mostly in the family Asteraceae. " +
                " The term thistle is sometimes taken to mean exactly those plants in the tribe Cynareae (synonym: Cardueae), especially the genera Carduus, Cirsium, and Onopordum.", R.drawable.thistle));
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(species);
        rv.setAdapter(adapter);
    }
}

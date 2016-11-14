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
        species.add(new Species("Japanese Knotweed", "Fallopia japonica, synonym Reynoutria japonica, is a large, herbaceous perennial plant of the knotweed and buckwheat family Polygonaceae. It is native to East Asia in Japan, China and Korea.", R.drawable.japan));
        species.add(new Species("Common Tansy", "Tanacetum vulgare, is a perennial, herbaceous flowering plant of the aster family, native to temperate Europe and Asia. It has been introduced to other parts of the world including North America, and in some areas has become invasive. It is also known as common tansy bitter buttons, cow bitter, or golden buttons.", R.drawable.tansy));
        species.add(new Species("Garlic Mustand", "Alliaria petiolata is a biennial flowering plant in the Mustard family, Brassicaceae. It is native to Europe, western and central Asia, and northwestern Africa, from Morocco, Iberia and the British Isles, north to northern Scandinavia, and east to northern Pakistan and western China", R.drawable.garlic));
        species.add(new Species("Exotic Honeysuckles", "Lonicera tatarica is a species of honeysuckle known by the common name Tartarian honeysuckle. It is native to Siberia and other parts of eastern Asia, but it is probably better known in North America. " +
                "Lonicera morrowii, the Morrow's honeysuckle, is a deciduous honeysuckle in the family Caprifoliaceae, native to Japan, Korea, and Northeast China. It is a shrub, reaching a height of 2-2.5 m, with oblong leaves 4–6 cm long. " +
                "Bell's honeysuckle (Lonicera x bella) is a dense, multi-stemmed, deciduous shrub that grows up to 20’ tall. Young stems are slightly hairy and light brown while older stems may have shaggy, peeling bark and are often hollow between the nodes. ", R.drawable.honey));

    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(species);
        rv.setAdapter(adapter);
    }
}

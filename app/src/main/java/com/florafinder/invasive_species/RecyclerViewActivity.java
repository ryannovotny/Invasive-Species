package com.florafinder.invasive_species;

/**
 * Created by lando on 11/12/2016.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends FragmentActivity {

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
        species.add(new Species("Buckthorn", "A shrub or small tree of the buckthorn family, typically bearing thorns. Some kinds yield dyes, and others have been used medicinally.", R.drawable.buckthorn, "-----------------------------------------\nControl Methods\n" +
                "\n" +
                "Pulling individual plants" +
                "\n" +
                " - If less than 3/8 inch in diameter, plants can be removed by hand. Small seedlings can be pulled and will not re-sprout. If greater than 3/8 inch, use a hand tool that pulls the shrub out, such as an \"Uprooter\" or \"Root Talon\". Removing by hand is easier if the soil is moist. Before you pull or dig buckthorn out of your soil, Contact Gopher State One Call to ensure there are no buried utilities in the area. Hand-pulling tools can cause soil disturbance so work to minimize soil disturbance and tap soil and plants back into place after pulling plants. Disturbed soil may result in increased seed germination." +
                "\n" +
                "\nIf pulling individual plants is impractical" +
                "\n" +
                " - Spray foliage of short buckthorn or seedlings with a herbicide. Glyphosate (one brand name is Roundup) will kill all actively growing vegetation on which it is sprayed. Triclopyr will kill broadleaf plants and will not harm grasses when applied properly. If you wish to use a cutting method, see the section below." +
                "\n" +
                "\nControlling large buckthorn\n" +
                " - Buckthorn plants that are two inches in diameter or larger are best controlled by cutting the stem at the soil surface and then covering or treating the stump to prevent re-sprouting. Cutting can be effectively done with hand tools (for a few plants), chain saws or brush cutters.\n--------------------\n" +

                " - Non-chemical control options for cut stumps include covering the cut stump with a tin can or black plastic (such as a \"Buckthorn Baggie\") to prevent re-sprouting. After cutting the tree, apply the can or plastic over the cut stump and root flare.  Use nails to affix the can or a tie to affix the black plastic. Leave in place for one to two years.\n--------------------\n" +

                        " - Chemical control options for cut stumps include treating the stump immediately after cutting (within 2 hours) with a herbicide containing triclopyr (Garlon 3A/Vastlan, Garlon 4, or other brush killers with triclopyr) or glyphosate (Roundup) to prevent re-sprouting. Always follow label instructions for herbicides."
        , "\n To learn more, visit the Minnesota DNR's page at this link https://goo.gl/RCMwhG"));
        species.add(new Species("Japanese Knotweed", "Fallopia japonica, synonym Reynoutria japonica, is a large, herbaceous perennial plant of the knotweed and buckwheat family Polygonaceae. It is native to East Asia in Japan, China and Korea.", R.drawable.japan, "-----------------------------------------\nControl Methods\n" +
                "\n" +
                "Mechanical" +
                "\n" +
                " - Digging plants is effective for small infestations and in sensitive areas" +
                "\n" +
                " - Pulling of juvenile plants\n" +
                "\n" +
                "Chemical" +
                "\n" +
                " - Cut stem treatment with glyphosate or triclopyr" +
                "\n" +
                " - Foliar spray in large single species populations ", "\n" +
                " To learn more, visit the Minnesota DNR's page at this link https://goo.gl/RtaKRd"));
        species.add(new Species("Common Tansy", "Tanacetum vulgare, is a perennial, herbaceous flowering plant of the aster family, native to temperate Europe and Asia. It has been introduced to other parts of the world including North America, and in some areas has become invasive. It is also known as common tansy bitter buttons, cow bitter, or golden buttons.", R.drawable.tansy, "-----------------------------------------\nControl Methods\n\n Grazing" +
                "\n" +
                " - Tansy is distasteful and even toxic to some grazing animals, however, one source claims that sheep graze it and are not affected" +
                "\n\n" +
                "Chemical" +
                "\n" +
                " - Spot-spraying with selective broadleaf herbicide such as clopyralid, metsulfuron or 2,4-D ",
                "\n" +
                        " To learn more, visit the Minnesota DNR's page at this link https://goo.gl/fGYKc8"));
        species.add(new Species("Garlic Mustard", "Alliaria petiolata is a biennial flowering plant in the Mustard family, Brassicaceae. It is native to Europe, western and central Asia, and northwestern Africa, from Morocco, Iberia and the British Isles, north to northern Scandinavia, and east to northern Pakistan and western China", R.drawable.garlic, "-----------------------------------------\nControl Methods\n" +
                "\n" +
                "Mechanical\n" +
                " - Pulling in areas of light infestations" +
                "\n" +
                " - Flowering stem cutting at ground level" +
                "\n" +
                " - Prescribed burning if there is enough fuel to carry the flames\n" +
                "\n" +
                "Chemical\n" +
                " - Spot application of 2% glyphosate in early spring or late fall when native plants are dormant\n" +
                "\n", "\n" +
                " To learn more, visit the Minnesota DNR's page at this link https://goo.gl/9jgtU9"));
        species.add(new Species("Exotic Honeysuckles", "Lonicera tatarica is a species of honeysuckle known by the common name Tartarian honeysuckle. It is native to Siberia and other parts of eastern Asia, but it is probably better known in North America. " +
                "Lonicera morrowii, the Morrow's honeysuckle, is a deciduous honeysuckle in the family Caprifoliaceae, native to Japan, Korea, and Northeast China. It is a shrub, reaching a height of 2-2.5 m, with oblong leaves 4–6 cm long. " +
                "Bell's honeysuckle (Lonicera x bella) is a dense, multi-stemmed, deciduous shrub that grows up to 20’ tall. Young stems are slightly hairy and light brown while older stems may have shaggy, peeling bark and are often hollow between the nodes. ", R.drawable.honey, "-----------------------------------------\nControl Methods\n" +
                "\n" +
                "Mechanical\n" +
                " - Pulling seedlings in small infestations when soil is moist" +
                "\n" +
                " - Prescribed burning will kill seedlings and top kill mature shrubs, repeated burns may be needed to control infestations\n" +
                "\n" +
                "Chemical\n" +
                " - Cut-stump treatment with glyphosate; cut-stump or basal bark spray treatment around the stem with triclopyr" +
                "\n" +
                " - Foliage spraying with glyphosate solution, where burning is not possible, prior to leaf out of native species",
                "\n" +
                        " To learn more, visit the Minnesota DNR's page at this link https://goo.gl/gEgcct"));

    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(species);
        rv.setAdapter(adapter);
    }
}

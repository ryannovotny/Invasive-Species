package com.florafinder.invasive_species;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SpeciesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SpeciesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpeciesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Species> species;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SpeciesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpeciesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpeciesFragment newInstance(String param1, String param2) {
        SpeciesFragment fragment = new SpeciesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_species, container, false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        initializeData();

        RVAdapter adapter = new RVAdapter(species);
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

package com.florafinder.invasive_species;

import android.content.Context;
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
        species.add(new Species(R.string.buckthorn, R.string.buckthorn_scientific, R.string.buck_description, R.drawable.buckthorn, R.string.buck_remove, R.string.buck_link));
        species.add(new Species(R.string.knotweed, R.string.knotweed_scientific, R.string.knot_description, R.drawable.japan,R.string.knot_remove,R.string.knot_link));
        species.add(new Species(R.string.tansy, R.string.tansy_scientific, R.string.tansy_description, R.drawable.tansy, R.string.tansy_remove, R.string.tansy_link));
        species.add(new Species(R.string.garlic, R.string.garlic_scientific, R.string.garlic_description, R.drawable.garlic, R.string.garlic_remove, R.string.garlic_link));
        species.add(new Species(R.string.honeysuckle, R.string.honeysuckle_scientific, R.string.honeysuckle_description, R.drawable.honey, R.string.tansy_remove, R.string.garlic_link));

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

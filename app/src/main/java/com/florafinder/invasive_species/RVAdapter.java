package com.florafinder.invasive_species;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {

    static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView speciesName;
        TextView speciesScientific;
        TextView speciesDescription;
        ImageView speciesPhoto;

        PersonViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);


            speciesName = (TextView)itemView.findViewById(R.id.species_name);
            speciesScientific = (TextView) itemView.findViewById(R.id.species_scientific);
            speciesDescription = (TextView)itemView.findViewById(R.id.species_description);
            speciesPhoto = (ImageView)itemView.findViewById(R.id.species_photo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View view) {
                    MainActivity mainActivity = (MainActivity) cv.getContext();
                    Fragment fragment = new SpeciesExpandedFragment();

                    /*Slide slideTransition = new Slide(Gravity.END);
                    slideTransition.setDuration(1000);
                    ChangeBounds changeBounds = TransitionInflater.from(this).inflateTransition(R.transition.changebounds);
                    fragment.setSharedElementEnterTransition(changeBounds);*/

                    FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    transaction.replace(R.id.main_container, fragment).addToBackStack("species_expanded").commit();
                }
            });
        }
    }

    private List<Species> species;

    RVAdapter(List<Species> species){
        this.species = species;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new PersonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.speciesName.setText(species.get(i).name);
        personViewHolder.speciesScientific.setText(species.get(i).scienceName);
        personViewHolder.speciesDescription.setText(species.get(i).description);
        personViewHolder.speciesPhoto.setImageResource(species.get(i).photoId);
    }

    @Override
    public int getItemCount() {
        return species.size();
    }
}

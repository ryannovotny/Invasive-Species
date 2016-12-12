package com.florafinder.invasive_species;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
                @Override
                public void onClick(View view) {

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

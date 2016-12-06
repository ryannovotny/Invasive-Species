package com.florafinder.invasive_species;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView speciesName;
        TextView speciesDescription;
        ImageView speciesPhoto;
        TextView speciesRemove;
        TextView Link;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            cv.setCardBackgroundColor(Color.rgb(142,210,122));

            speciesName = (TextView)itemView.findViewById(R.id.species_name);
            speciesDescription = (TextView)itemView.findViewById(R.id.species_description);
            speciesPhoto = (ImageView)itemView.findViewById(R.id.species_photo);
            speciesRemove= (TextView)itemView.findViewById(R.id.species_remove);
            Link= (TextView)itemView.findViewById(R.id.link);
        }
    }

    List<Species> species;

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
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.speciesName.setText(species.get(i).name);
        personViewHolder.speciesDescription.setText(species.get(i).description);
        personViewHolder.speciesPhoto.setImageResource(species.get(i).photoId);
        personViewHolder.speciesRemove.setText(species.get(i).remove);
        personViewHolder.Link.setText(species.get(i).link);


    }

    @Override
    public int getItemCount() {
        return species.size();
    }
}

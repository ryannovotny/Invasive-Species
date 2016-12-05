package com.florafinder.invasive_species;

import com.google.android.gms.maps.model.Polygon;

import java.util.ArrayList;

/**
 * Tracks a polygon and store data associated with it
 * Created by Fitzy on 12/5/2016.
 */

public class InvPolygon{

    private final Polygon polygon;
    private final ArrayList<String> speciesList;

    public InvPolygon(Polygon polygon) {
        this.polygon = polygon;
        this.speciesList = new ArrayList();
    }

    /**
     * Getter method for polygon
     * @return
     */
    public Polygon getPolygon() {
        return this.polygon;
    }

    /**
     * Adds a species to the list of what is being tracked
     * Checks to make sure the species is not already being tracked
     * @param name
     */
    public void addSpecies(String name) {

        if(speciesList.contains(name)){
            speciesList.add(name);
        }
    }
}

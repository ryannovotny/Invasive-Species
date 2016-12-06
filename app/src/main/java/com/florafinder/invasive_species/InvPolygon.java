package com.florafinder.invasive_species;

import com.google.android.gms.maps.model.Polygon;

import java.util.ArrayList;

public class InvPolygon{

    private double lat, lang;
    private final ArrayList<String> speciesList;

    public InvPolygon(double lat, double lang) {
        this.lat = lat;
        this.lang = lang;
        this.speciesList = new ArrayList();
    }

    public double getLat() {
        return this.lat;
    }

    public double getLang(){
        return this.lang;
    }

    public boolean hasLatLang(double lat, double lang){
        return lat == this.lat && lang == this.lang;
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
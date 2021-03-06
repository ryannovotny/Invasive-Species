package com.florafinder.invasive_species;

import java.util.ArrayList;

public class InvPolygonList{

    private final ArrayList<InvPolygon> list = new ArrayList<InvPolygon>();

    public InvPolygonList() {
    }

    /**
     * Adds polygon by lat and lang
     * @param lat
     * @param lang
     */
    public void add(double lat, double lang) {
        list.add(new InvPolygon(lat, lang));
    }

    /**
     * Adds polygon
     * @param polygon
     */
    public void add(InvPolygon polygon){
        list.add(polygon);
    }

    public boolean hasTile(double lat, double lang) {
        boolean exists = false;

        for(int i = 0; i < list.size() && !exists; ++i) {
            if(list.get(i).hasLatLang(lat,lang)){
                exists = true;
            }
        }

        return exists;
    }

    public InvPolygon getTile(double lat, double lang) {
        boolean exists = false;
        InvPolygon rtrnPoly = null;

        for (int i = 0; i < list.size() && !exists; ++i) {
            if (list.get(i).hasLatLang(lat, lang)) {
                exists = true;
                rtrnPoly = list.get(i);
            }
        }

        return rtrnPoly;
    }

    /**
     * Clears all data
     */
    public void clear(){
        list.clear();
    }
}
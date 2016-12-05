package com.florafinder.invasive_species;

/**
 * Created by lando on 11/12/2016.
 */


class Species {
    String name;
    String description;
    int photoId;
    String remove;
    String link;

    Species(String name, String description, int photoId, String remove, String link) {
        this.name = name;
        this.description = description;
        this.photoId = photoId;
        this.remove = remove;
        this.link = link;
    }
}
package com.florafinder.invasive_species;

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
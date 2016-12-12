package com.florafinder.invasive_species;

class Species {
    int name;
    int scienceName;
    int description;
    int photoId;

    Species(int name, int scienceName, int description, int photoId) {
        this.name = name;
        this.scienceName = scienceName;
        this.description = description;
        this.photoId = photoId;
    }
}
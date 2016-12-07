package com.florafinder.invasive_species;

class Species {
    int name;
    int scienceName;
    int description;
    int photoId;
    int remove;
    int link;

    Species(int name, int scienceName, int description, int photoId, int remove, int link) {
        this.name = name;
        this.scienceName = scienceName;
        this.description = description;
        this.photoId = photoId;
        this.remove = remove;
        this.link = link;
    }
}
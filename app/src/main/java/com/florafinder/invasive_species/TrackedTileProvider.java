package com.florafinder.invasive_species;

import android.content.Context;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

/**
 * A custom TileProvider that provides tiles that track
 * certain invasive species in an area and a status on how long
 * it's been since they were monitored
 * Created by Owner on 10/29/2016.
 */

public class TrackedTileProvider implements TileProvider {

    public TrackedTileProvider(int height, int width, Context context){

    }

    @Override
    public final Tile getTile(int x, int y, int zoom){

        return null;
    }
}

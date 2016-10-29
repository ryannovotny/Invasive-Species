package com.florafinder.invasive_species;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

/**
 * An area on the map that, when a point is clicked, will appear on the
 * google map. The tile will be auto-aligned so all tiles line up in a uniform fashion
 * Created by Owner on 10/28/2016.
 */

public class TrackedTile{

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                              Private Data

    private int width;
    private int height;
    private Context context;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                              Public Methods

    public TrackedTile(Context context, int width, int height) {

        this.width = width;
        this.height = height;
        this.context = context;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                              Private Methods

    /**
     * Initializes the colors the tiles can use
     */
    private void initColors(){


    }
}

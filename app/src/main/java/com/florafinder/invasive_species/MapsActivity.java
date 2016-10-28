package com.florafinder.invasive_species;

import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                              Private Data
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    //For resolving connection issues
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    //Data involving permissions
    private final static String permissionFine = Manifest.permission.ACCESS_FINE_LOCATION;
    private final static String permissionCoarse = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int REQUEST_CODE_PERMISSION = 2;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                              Public.Protected Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Create GoogleApiClient object
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create a LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(30 * 1000) //25 seconds, in milliseconds
                .setFastestInterval(5 * 1000); //5 seconds, in milliseconds

        Log.d("Map Activity: ", "Map launch successful");
    }


    /**
     * Sets up the map and connects the GoogleApiClient
     * Method is designed this way to ensure that the map is ready
     * on location update
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("onMapReady:", "Attempting to connect to GoogleApiClient");
        mGoogleApiClient.connect();
    }

    /**
     * Upon connection to location services
     * Sets up Location Request Object
     * Zoom to the user's current location
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.d("Connected; ", "onConnected executed successfully");

        requestPermissionsAndLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    /*
     * Google Play services can resolve some errors it detects.
     * If the error has a resolution, try sending an Intent to
     * start a Google Play services activity that can resolve
     * error.
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            /*
             * Thrown if Google Play services canceled the original
             * PendingIntent
             */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
        /*
         * If no resolution is available, display a dialog to the
         * user with the error.
         */
            Log.i("Connect Fail", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * Updates map on change in location
     * This change does not zoom
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {

        double currentLat = location.getLatitude();
        double currentLong = location.getLongitude();

        LatLng latLng = new LatLng(currentLat, currentLong);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    /**
     * Handles permission requests during runtime.
     * This is called when the location permissions aren't already registered as defined on runtime,
     * meaning the user must enter them
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Req Code", "" + requestCode);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED ) {

                //Try zoom again after successful request
                requestPermissionsAndLocation();
            }
            else{

                Log.e("Permission", "Request for " + permissions[0] + " failed");
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                              Private Methods

    /**
     * The initial zoom to the user's location upon launch
     */
    private void initialZoom(Location location){

        float zoomLevel = 15.0f;
        double currentLat = location.getLatitude();
        double currentLong = location.getLongitude();

        LatLng latLng = new LatLng(currentLat, currentLong);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        addTileOverlay();
    }

    /**
     * Requests the permissions to get fine and coarse location, and attempts to zoom
     * to the user's location on successful request
     */
    private void requestPermissionsAndLocation(){

        //Check for Fine Location Permissions
        if(ActivityCompat.checkSelfPermission(this, permissionFine)
                != PackageManager.PERMISSION_GRANTED){

            Log.e("Permissions: ", "Error loading fine location permission");
            ActivityCompat.requestPermissions(this, new String[] {permissionFine}, REQUEST_CODE_PERMISSION);

        }// end if
        else{

            //Check for Fine Location Permissions
            if(ActivityCompat.checkSelfPermission(this, permissionCoarse)
                    != PackageManager.PERMISSION_GRANTED) {

                Log.e("Permissions: ", "Error loading coarse location permission");
                ActivityCompat.requestPermissions(this, new String[]{permissionFine}, REQUEST_CODE_PERMISSION);
            }//endif

            else{

                Log.d("Permissions:", "Fine and Coarse location permissions granted");
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

                //Initialize user's location on Connect
                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                //attempt to get location until one is found
                int attempt = 1;
                do{
                    if (location == null) {
                        ++attempt;
                        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        Log.i("Location", "Attempt #" + attempt);

                    } else {
                        initialZoom(location);
                        Log.i("Location", "Found on attempt #" + attempt);
                        Log.i("Location", location.toString());
                    }
                }while(location == null);

            }//end else
        }// end else
    }

    /**
     * Temporary method that adds the tile overlay to the map.
     * This will be phased out for better code later
     */
    private void addTileOverlay(){
        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {

    /* Define the URL pattern for the tile images */
                String s = String.format("http://i.imgur.com/fjYoUui.png",
                        zoom, x, y);

                if (!checkTileExists(x, y, zoom)) {
                    return null;
                }

                try {
                    return new URL(s);
                } catch (MalformedURLException e) {
                    Log.e("Tile URL", "Tile URL not found.");
                    throw new AssertionError(e);
                }
            }

            /*
             * Check that the tile server supports the requested x, y and zoom.
             * Complete this stub according to the tile range you support.
             * If you support a limited range of tiles at different zoom levels, then you
             * need to define the supported x, y range at each zoom level.
             */
            private boolean checkTileExists(int x, int y, int zoom) {
                int minZoom = 12;
                int maxZoom = 16;

                if ((zoom < minZoom || zoom > maxZoom)) {
                    Log.d("Tile Zoom", "Out of zoom range for tile visibility");
                    return false;
                }

                return true;
            }
        };

        TileOverlay tileOverlay = mMap.addTileOverlay(new TileOverlayOptions()
                .tileProvider(tileProvider));

    }
}

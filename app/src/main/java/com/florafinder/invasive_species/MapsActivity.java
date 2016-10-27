package com.florafinder.invasive_species;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.Manifest;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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

        // Create a LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(30 * 1000) //25 seconds, in milliseconds
                .setFastestInterval(5 * 1000); //5 seconds, in milliseconds

        //Check for Location Permissions
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){

            Log.d("Permissions:", "Fine and Coarse location permissions granted");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            //Initialize user's location on Connect
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            initialZoom(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
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
    }
}

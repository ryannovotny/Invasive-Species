package com.florafinder.invasive_species;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
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

        // Create a LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(30 * 1000) //25 seconds, in milliseconds
                .setFastestInterval(5 * 1000); //5 seconds, in milliseconds

        Log.d("Map Activity: ", "Map launch successful");
    }


    /**
     * Sets our map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    /**
     * Upon connection to location services, zoom to the user's current location
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
            else {
                initialZoom(location);
            }
        }
        catch(SecurityException err){
            Log.e("Permission Error: ", "Location Permissions Not Given");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                              Private Methods

    /**
     * Sets up the map if onMapReady has not been called yet
     */
    private void setUpMapIfNeeded(){
    }

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

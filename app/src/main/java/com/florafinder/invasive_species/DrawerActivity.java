package com.florafinder.invasive_species;

import android.*;
import android.content.Intent;
import android.app.FragmentManager;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.graphics.Color;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnMapLongClickListener {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                              Private Data
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Circle mCircle;
    private Marker mMarker;

    //For resolving connection issues
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    //Data involving permissions
    private final static String permissionFine = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private final static String permissionCoarse = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int REQUEST_CODE_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        //***********************************LAYOUT AND UI START***********************************

        //Sets up the Toolbar and makes it the ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Sets up the floating action "menu" for button expansion with minis. After, creates
        //onClick event handlers for us to implement new actions
        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        findViewById(R.id.add_marker).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(DrawerActivity.this, "Add", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        findViewById(R.id.filter).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(DrawerActivity.this, "Filter", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        //Initializes the drawer layout and assigns some event handlers
        //to it for opening and closing the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Initializes the drawer view so that the drawer works
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //***************************************END***********************************************

        //***********************************MAPS START********************************************

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //***************************************END***********************************************

        //******************************LOCATION HANDLING START************************************
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
        //***************************************END***********************************************
    }

    /**
     * When the app is resumed, start requesting location updates and re-zoom
     */
    @Override
    protected void onResume(){
        super.onResume();

        if(mMap != null){
            Log.d("Lifecycle", "Map activity resumed");
            requestPermissionsAndLocation();
        }
    }

    /**
     * When the app is paused, stop requesting location updates
     */
    @Override
    protected void onPause(){
        super.onPause();
        Log.d("Lifecycle","Map activity paused");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    /**
     * When the app is stopped, stop requesting location updates
     */
    @Override
    protected void onStop(){
        super.onStop();
        Log.d("Lifecycle","Map activity stopped");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    //Overrides system's onBackPress (tapping the back button on the phone) to simply
    //close the drawer if it is open
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Inflates the navigation drawers menu options (Settings, Grid Toggle, etc.)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    //Click events for the ActionBar (Three dots, Search...)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Toast toast = Toast.makeText(this, "Search will be implemented later", Toast.LENGTH_SHORT);
            toast.show();
        }

        return super.onOptionsItemSelected(item);
    }

    //Click events for when an item is selected in the Navigation Drawer
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            Toast toast = Toast.makeText(this, "Open the map fragment activity", Toast.LENGTH_SHORT);
            toast.show();
        } else if (id == R.id.nav_species) {
            Intent intentSpecies = new Intent(DrawerActivity.this, SpeciesActivity.class);
            startActivity(intentSpecies);
        } else if (id == R.id.nav_toggle) {
            Toast toast = Toast.makeText(this, "Toggle the grid overlay on or off", Toast.LENGTH_SHORT);
            toast.show();
        } else if (id == R.id.nav_settings) {
            Intent intentSettings = new Intent(DrawerActivity.this, SettingsActivity.class);
            startActivity(intentSettings);
        }

        //Closes the drawer when an option is selected
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //make the circle be able to pop up when user clicks on the map
    @Override
    public void onMapLongClick(LatLng point) {
        CircleOptions circleOptions = new CircleOptions()
                .center(point)   //set center
                .radius(100)   //set radius in meters
                .fillColor(0x40ff0000)  //semi-transparent
                .strokeColor(Color.BLUE)
                .strokeWidth(5);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(point) // set marker at the center of the circle
                .title("Invasive Species")
                .snippet("Species lv")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));//marker color
        mCircle = mMap.addCircle(circleOptions);
        mMarker = mMap.addMarker(markerOptions);

    }
    /**
    * Sets up the map and connects the GoogleApiClient
    * Method is designed this way to ensure that the map is ready
    * on location update
    */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
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
}

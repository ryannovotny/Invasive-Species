package com.florafinder.invasive_species;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.FragmentManager;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;



import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
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
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                              Private Data
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Polygon mSquare;
    private Marker mMarker;
    private TextView time;
    private long starttime = 0L;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedtime = 0L;
    private int t = 1;
    private int secs = 0;
    private int mins = 0;
    private int hours = 0;
    private int days = 0;
    private int milliseconds = 0;
    private String times;
    private Handler handler = new Handler();




    //For resolving connection issues
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    //Data involving permissions
    private final static String permissionFine = Manifest.permission.ACCESS_FINE_LOCATION;
    private final static String permissionCoarse = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int REQUEST_CODE_PERMISSION = 2;

    //Client-Server data
    private final static String SERVER_IP = "192.168.2.3";
    private final static String SERVER_PORT = ":4096";
    private final static String MAP_DIRECTORY = "/mapdata";
    private final static String USER_DIRECTORY = "/userdata";

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
        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();

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
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mGoogleApiClient, getIndexApiAction());
        Log.d("Lifecycle", "Map activity stopped");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.disconnect();
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
            Intent intentSpecies = new Intent(DrawerActivity.this, RecyclerViewActivity.class);
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
//    @Override
//    public void onMapLongClick(LatLng point) {
//        String s = point.toString();
//        String[] latLng = s.substring(10, s.length() - 1).split(",");
//        String sLat = latLng[0];
//        String sLng = latLng[1];
//        double dLat = Double.parseDouble(sLat);
//        double dLng = Double.parseDouble(sLng);
//        PolygonOptions squareOpt = new PolygonOptions()
//                .add(point, new LatLng(dLat,dLng+.001), new LatLng(dLat+.0005,dLng+.001), new LatLng(dLat+.0005,dLng)) //set size
//                .fillColor(0x40ff0000)  //semi-transparent
//                .strokeColor(Color.BLUE)
//                .strokeWidth(5);
//        MarkerOptions markerOptions = new MarkerOptions()
//                .position(new LatLng(dLat+.00025,dLng+.0005)) // set marker at the center of the circle
//                .title("Invasive Species")
//                .snippet(dLat+"/"+dLng)
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));//marker color
//        mSquare = mMap.addPolygon(squareOpt);
//        mMarker = mMap.addMarker(markerOptions);
//
//    }




    public Runnable updateTimer = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - starttime;

            updatedtime = timeSwapBuff + timeInMilliseconds;

            secs = (int) (updatedtime / 1000);
            mins = secs / 60;
            hours = mins / 60;
            days = hours / 24;
            secs = secs % 60;
            milliseconds = (int) (updatedtime % 1000);
            time.setText("" + days + ":"
                    + "" + hours + ":"
                    + "" + mins + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));
            handler.postDelayed(this, 0);
        }

    };
    /**
    * Sets up the map and connects the GoogleApiClient
    * Method is designed this way to ensure that the map is ready
    * on location update
    */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       // mMap.setOnMapLongClickListener(this);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        //initialize tiles
        mapGET();
        //Initialize onClick listener
        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                DialogActivity dialogActivity = new DialogActivity();
                dialogActivity.show(getSupportFragmentManager(), "tag");
                polygon.setFillColor(0x40ff0000);
                if (days == 10){
                    polygon.setFillColor(0x7fffff00);
                }
                mMap.setOnMapClickListener(new OnMapClickListener(){
                    @Override
                    public void onMapClick(LatLng point) {
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(point) // set marker at the center of the circle
                                .title("Invasive Species")
                                .snippet(times)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        mMarker = mMap.addMarker(markerOptions);
                    }

                });
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker){
                        starttime = 0L;
                        timeInMilliseconds = 0L;
                        timeSwapBuff = 0L;
                        updatedtime = 0L;
                        t = 1;
                        secs = 0;
                        mins = 0;
                        milliseconds = 0;
                        handler.removeCallbacks(updateTimer);
                        time.setText("00:00:00:00:00");
                        times = time.getText().toString();
                        return true;
                    }
                });
            }

        });

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
    public void onConnectionSuspended(int i) {}

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

    @Override
    public void onLocationChanged(Location location) {}

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
                mMap.setMyLocationEnabled(true);

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
     * Called to perform GET request to server
     * Upon request, the map is updated to inlude tiles as they're
     * defined on the server
     */
    private void mapGET(){

        String result;
        RestAsyncTask asyncTask = new RestAsyncTask();
        asyncTask.execute("http://" + SERVER_IP + SERVER_PORT + MAP_DIRECTORY, "GET");

        try {
            result = asyncTask.get();

            //Create JSONArray from result
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = (JSONArray)jsonObject.get("tiles");

            /**terate through JSONArray and add tiles
             * Starts at 1 to ignore initTile
             */
            for(int i = 1; i < jsonArray.length(); ++i){

                JSONObject tile = (JSONObject) jsonArray.get(i);

                Double dLat = (Double) tile.get("lat");
                Double dLng = (Double) tile.get("lang");

                PolygonOptions squareOpt = new PolygonOptions()
                        .add(new LatLng(dLat, dLng),
                                new LatLng(dLat, dLng + .001),
                                new LatLng(dLat + .0005, dLng + .001),
                                new LatLng(dLat + .0005, dLng)) //set size
                        //.fillColor(0x40ff0000)// color red
                        //.fillColor(0x400ff000)// color green
                        .fillColor(0x00000000)// semi-transparent
                        .strokeColor(Color.BLUE)
                        .strokeWidth(1)
                        .clickable(true);
                mMap.addPolygon(squareOpt);
            }
        }
        catch(ExecutionException err) {
            Log.e("MapGET", "Error executing HTTP call");
            err.printStackTrace();
        }
        catch(InterruptedException err){
            Log.e("MapGET", "Error- HTTP execution interrupted");
            err.printStackTrace();
        }
        catch (JSONException err){
            Log.e("/mapdata GET", "Error parsing JSON");
            err.printStackTrace();
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Drawer Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();
        AppIndex.AppIndexApi.start(mGoogleApiClient, getIndexApiAction());
    }
}

package com.florafinder.invasive_species;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                              Private Data
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    //Lists of polygons for later access
    private InvPolygonList invPolygonList = new InvPolygonList();
    private ArrayList<Polygon> polygonList = new ArrayList<>();

    //For resolving connection issues
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    //Data involving permissions
    private final static String permissionFine = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private final static String permissionCoarse = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int REQUEST_CODE_PERMISSION = 2;

    //Client-Server data
    private final static String SERVER_IP = "https://lempo.d.umn.edu";
    private final static String SERVER_PORT = ":4097";
    private final static String MAP_DIRECTORY = "/mapdata";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create GoogleApiClient object
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return rootView;
    }

    /**
     * When the app is resumed, start requesting location updates and re-zoom
     */
    @Override
    public void onResume(){
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
    public void onPause(){
        super.onPause();
        Log.d("Lifecycle","Map activity paused");
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    /**
     * When the app is stopped, stop requesting location updates
     */
    @Override
    public void onStop(){
        super.onStop();
        Log.d("Lifecycle","Map activity stopped");
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

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
                polygon.setFillColor(0x40ff0000);

                Intent intent = new Intent(getActivity(), SpeciesPickerActivity.class);
                LatLng ltlng = polygon.getPoints().get(0);
                intent.putExtra("lat", ltlng.latitude);
                intent.putExtra("lang", ltlng.longitude);
                startActivityForResult(intent, 1);
            }
        });
        Log.d("onMapReady:", "Attempting to connect to GoogleApiClient");
        mGoogleApiClient.connect();
    }

    /**
     * Upon connection to location services
     * Sets up Location Request Object
     * Zoom to the user's current location
     * @param bundle Bundle
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
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
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
     * @param requestCode Request Code
     * @param permissions Needed Permissions
     * @param grantResults Results
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
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

    /**
     * Handles returning data from intent sent to specieslist
     * @param requestCode Request Code
     * @param resultCode Result Code
     * @param data Data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                    Intent data){

        Log.d("INTENT RESULT", "Result being handled");
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK) {
                Log.d("INTENT RESULT", "Result retrieved");

                InvPolygon tile = invPolygonList.getTile(data.getDoubleExtra("lat", 0), data.getDoubleExtra("lang", 0));
                tile.addSpecies(data.getStringExtra("species"));

                postTile(tile);
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
        if(ActivityCompat.checkSelfPermission(getContext(), permissionFine)
                != PackageManager.PERMISSION_GRANTED){

            Log.e("Permissions: ", "Error loading fine location permission");
            ActivityCompat.requestPermissions(getActivity(), new String[] {permissionFine}, REQUEST_CODE_PERMISSION);

        }// end if
        else{

            //Check for Fine Location Permissions
            if(ActivityCompat.checkSelfPermission(getContext(), permissionCoarse)
                    != PackageManager.PERMISSION_GRANTED) {

                Log.e("Permissions: ", "Error loading coarse location permission");
                ActivityCompat.requestPermissions(getActivity(), new String[]{permissionFine}, REQUEST_CODE_PERMISSION);
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
        asyncTask.execute(SERVER_IP + SERVER_PORT + MAP_DIRECTORY, "GET");

        try {
            result = asyncTask.get();

            //Create JSONArray from result
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = (JSONArray)jsonObject.get("tiles");

            //remove all polygons
            clearPolygons();

            /**iterate through JSONArray and add tiles
             * Starts at 1 to ignore initTile
             */
            for(int i = 1; i < jsonArray.length(); ++i){

                JSONObject tile = (JSONObject) jsonArray.get(i);
                JSONArray species = (JSONArray) jsonArray.getJSONObject(i).get("species");

                ArrayList<String> list = new ArrayList<String>();
                for(int e = 0; i < species.length(); ++e) {
                    list.add((String) species.get(i));
                }

                Double dLat = (Double) tile.get("lat");
                Double dLng = (Double) tile.get("lang");

                pushPolygon(dLat, dLng, list);
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
     * Pushes or updates a polygon to map
     * @param dLat Latitude
     * @param dLng Longitude
     */
    private void pushPolygon(double dLat, double dLng, ArrayList<String> species) {

        PolygonOptions squareOpt = new PolygonOptions()
                .add(new LatLng(dLat, dLng),
                        new LatLng(dLat, dLng + .001),
                        new LatLng(dLat + .0005, dLng + .001),
                        new LatLng(dLat + .0005, dLng)) //set size
                .strokeColor(Color.BLUE)
                .strokeWidth(1)
                .clickable(true);

        if(species.size() == 0)
            squareOpt.fillColor(0x00000000);// semi-transparent
        else
            squareOpt.fillColor(0x400ff000);// color green

        //make data to track
        InvPolygon track = new InvPolygon(dLat, dLng);
        for(String string: species) {
            track.addSpecies(string);
        }

        polygonList.add(mMap.addPolygon(squareOpt));
        invPolygonList.add(track);
    }

    /**
     * Removes all polygons from the map
     */
    private void clearPolygons(){
        for(Polygon polygon: polygonList){
            polygon.remove();
        }
        invPolygonList.clear();
    }

    /**
     * Posts a tile to the server to update
     * @param tile Tile
     */
    private void postTile(InvPolygon tile){

        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("lat", tile.getLat());
            jsonObject.put("lang", tile.getLang());
            JSONArray jsonArray = new JSONArray();

            for(String string: tile.getSpeciesList()){
                jsonArray.put(string);
            }
            jsonObject.put("species", jsonArray);

            RestAsyncTask asyncTask = new RestAsyncTask();
            asyncTask.execute(SERVER_IP + SERVER_PORT + MAP_DIRECTORY, "POST", jsonObject.toString());
            Log.d("mapPOST","Tile posted to server");
        }
        catch (JSONException err){
            Log.e("/mapdata POST", "Error parsing JSON");
            err.printStackTrace();
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
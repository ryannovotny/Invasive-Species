package com.florafinder.invasive_species;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * AsyncTask class that handles REST calls to server
 * by invasive species app
 * Created by Edward FitzSimons on 11/10/2016.
 */

public class RestAsyncTask extends AsyncTask<String, Integer, String> {

    ////////////////////////////////////////////////////////////////////////////////////////
    //                           Private Data

    private Object obj;     //Will be recast based on url and command
    private String command;
    private URL url;

    private final static String IP = "131.212.215.62";
    private final static String MAP_DIRECTORY = "http://"+IP+":4321/mapdata";
    private final static String USER_DIRECTORY = "http://"+IP+":4321/userdata";

    public RestAsyncTask(Object obj){
        this.obj = obj;
    }

    @Override
    protected String doInBackground(String... params) {

        HttpURLConnection serverConnection = null;
        InputStream is = null;

        Log.d("Debug:", "Attempting to connect to: " + params[0]);

        try {
            url = new URL( params[0] );
            command = params[1];
            serverConnection = (HttpURLConnection) url.openConnection();
            serverConnection.setRequestMethod(params[1]);
            if (params[1].equals("POST") ||
                    params[1].equals("PUT") ||
                    params[1].equals("DELETE")) {
                Log.d("DEBUG POST/PUT/DELETE:", "In post: params[0]=" + params[0] + ", params[1]=" + params[1] + ", params[2]=" + params[2]);
                serverConnection.setDoOutput(true);
                serverConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

                // params[2] contains the JSON String to send, make sure we send the
                // content length to be the json string length
                serverConnection.setRequestProperty("Content-Length", "" +
                        Integer.toString(params[2].toString().getBytes().length));

                // Send POST data that was provided.
                DataOutputStream out = new DataOutputStream(serverConnection.getOutputStream());
                out.writeBytes(params[2].toString());
                out.flush();
                out.close();
            }

            int responseCode = serverConnection.getResponseCode();
            Log.d("Debug:", "\nSending " + params[1] + " request to URL : " + params[0]);
            Log.d("Debug: ", "Response Code : " + responseCode);

            is = serverConnection.getInputStream();

            if (params[1] == "GET" || params[1] == "POST" || params[1] == "PUT" || params[1] == "DELETE") {
                StringBuilder sb = new StringBuilder();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                try {
                    JSONObject jsonData = new JSONObject(sb.toString());
                    return jsonData.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serverConnection.disconnect();
        }

        return "Should not get to this if the data has been sent/received correctly!";
    }

    /**
     * Handles results based on url and command
     * @param result the result from the query
     */
    protected void onPostExecute(String result) {

        Log.d("postExe", "URL: " + url + "\ncommand: " + command);
        if(url.toString().equals(MAP_DIRECTORY) && command.equals("GET")){
            updateMapGrids(result);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    //                           Private Methods
    //                  Will handle responses to onPostExecute
    ////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Updates the grids on the Google Map
     * @param result JSON returned from server
     */
    private void updateMapGrids(String result){
        try {
            GoogleMap mMap = (GoogleMap) obj;
            Log.d("/mapdata GET", "Succesfully recast object to Google Map");

            //Create JSONArray from result
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = (JSONArray)jsonObject.get("tiles");

            /**terate through JSONArray and add tiles
             * Starts at 1 to ignore initTile
             * WILL BE UPDATE LATER TO INCLUDE UPDATES TO EXISTING TILES
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
                        .fillColor(0x40ff0000)// color red
                        //.fillColor(0x400ff000)// color green
                        //.fillColor(0x00000000)// semi-transparent
                        .strokeColor(Color.BLUE)
                        .strokeWidth(1);
                mMap.addPolygon(squareOpt);
            }

        }
        catch (JSONException err){
            Log.e("/mapdata GET", "Error parsing JSON");
            err.printStackTrace();
        }
    }

    private void updateUserData(){}

    private void getUserData(){}

    private void getSpeciesData(){}
}

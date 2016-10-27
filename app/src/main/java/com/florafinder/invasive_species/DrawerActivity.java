package com.florafinder.invasive_species;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private GoogleMap mMap;

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
                Toast toast = Toast.makeText(DrawerActivity.this, "Add", Toast.LENGTH_LONG);
                toast.show();
            }
        });
        findViewById(R.id.filter).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(DrawerActivity.this, "Filter", Toast.LENGTH_LONG);
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
            Toast toast = Toast.makeText(this, "Open the list view of all species", Toast.LENGTH_SHORT);
            toast.show();
        } else if (id == R.id.nav_toggle) {
            Toast toast = Toast.makeText(this, "Toggle the grid overlay on or off", Toast.LENGTH_SHORT);
            toast.show();
        } else if (id == R.id.nav_settings) {
            Toast toast = Toast.makeText(this, "Open the settings fragment", Toast.LENGTH_SHORT);
            toast.show();
        }

        //Closes the drawer when an option is selected
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Sets a new marker in a pre-specified location and adds it to the map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(46.7867, -92.1005);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Duluth"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.0f));
    }
}

package com.florafinder.invasive_species;

import android.os.Bundle;
import android.support.design.widget.NavigationView;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle, mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //*****************************************************************************************
        //Creates a toolbar to be placed at the top of the activity
        Toolbar appToolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        setSupportActionBar(appToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //*****************************************************************************************

        //Initialization of Navigation Drawer stuff
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //This allows the navigation drawer to be called (opened) by tapping the hamburger menu
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, appToolbar, R.string.drawer_open, R.string.drawer_closed) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
            }
        };
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //*****************************************************************************************
        //This is for what happens when an item in the navigation drawer is clicked
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {

                    case R.id.navigation_item_attachment:
                        Toast.makeText(MapsActivity.this, "Attachment Clicked", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navigation_item_images:
                        Toast.makeText(MapsActivity.this, "Images Clicked", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navigation_item_location:
                        Toast.makeText(MapsActivity.this, "Location Clicked", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navigation_sub_item_01:
                        Toast.makeText(MapsActivity.this, "Navigation Sub Item 01 Clicked", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navigation_sub_item_02:
                        Toast.makeText(MapsActivity.this, "Navigation Sub Item 02 Clicked", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        //*****************************************************************************************

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //*********************************************************************************************
    //Add the items to the action bar (Settings, Search (soon))
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar, menu);
        setTitle("Invasive Species");
        return true;
    }
    //*********************************************************************************************


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng duluth = new LatLng(46.7867, -92.1005);
        mMap.addMarker(new MarkerOptions().position(duluth).title("Marker in Duluth"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(duluth, 15.0f));
    }
}

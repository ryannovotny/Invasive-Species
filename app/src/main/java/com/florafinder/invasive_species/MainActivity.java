package com.florafinder.invasive_species;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

/**
 * Created by ryano on 12/5/2016.
 */

public class MainActivity extends FragmentActivity implements
        MapFragment.OnFragmentInteractionListener, SpeciesFragment.OnFragmentInteractionListener {

    private Fragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.bottom_nav_map:
                        fragment = new MapFragment();
                        break;
                    case R.id.bottom_nav_species:
                        fragment = new SpeciesFragment();
                        break;
                    case R.id.bottom_nav_settings:

                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                //transaction.setTransition()
                return true;
            }
        });

        fragment = new MapFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, fragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

package com.example.childsafety;

import android.content.Context;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Lat = Float.parseFloat(getIntent().getExtras().get("Lat").toString());
        //Lon = Float.parseFloat(getIntent().getExtras().get("Lon").toString());
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


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
        LatLng myLoc = new LatLng(MainActivity.Lat, MainActivity.Lon);
        LatLng oneMore = new LatLng(28.9704,77.6963);
        mMap.addMarker(new MarkerOptions().position(oneMore).title("Extra location"));
        mMap.addMarker(new MarkerOptions().position(myLoc).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc));
        mMap.setTrafficEnabled(true);
        mMap.setMinZoomPreference(14);

    }
}

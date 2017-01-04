package com.eslamhossam23bichoymessiha.projetelim;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Button mapButton = (Button) findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                MainActivity.socket.askServerForData();
                DataOfLastDay dataRecieved = MainActivity.socket.getDataRecieved();
                Log.d("object", dataRecieved.toString());
                mMap.clear();
                for (LocationdBTriple triple : dataRecieved.getMapLocationdB()) {
                    mMap.addMarker(new MarkerOptions().title(triple.getdB()+"")
                                    .position(new LatLng(triple.getLatitude(), triple.getLongitude())));
                }
                //delete previous data
                MainActivity.socket.flushData();
            }
        });
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
        LatLng nice = new LatLng(43.7030414, 7.1828941);//43.6862115,7.2340225
//        mMap.addMarker(new MarkerOptions().position(nice).title("Marker in Nice"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(nice));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
    }


    public void returnToChart(View v) {
        Intent intent = new Intent(MapsActivity.this, MainActivity.class);
        startActivity(intent);
    }


}

package com.eslamhossam23bichoymessiha.projetelim.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.eslamhossam23bichoymessiha.projetelim.R;
import com.eslamhossam23bichoymessiha.projetelim.models.Cluster;
import com.eslamhossam23bichoymessiha.projetelim.models.LocationdBTriple;
import com.eslamhossam23bichoymessiha.projetelim.util.Dao;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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
//                Dao.askFirebaseForData(Dao.MAP_LOCATIONDB);
                final Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        while (Dao.getDataRecieved() == null) {
//                            Log.d("status", "waiting for Recieving data ");
//                            try {
//                                Thread.sleep(10);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        Log.d("Mock: Data of last day", "Value is: " + Dao.getDataRecieved());

                        try {
                            URL url = new URL("http://analyse-du-son-ambiant.appspot.com/locations");
                            InputStream input = url.openStream();
                            Reader reader = new InputStreamReader(input, "UTF-8");
                            final ArrayList<LocationdBTriple> results = new Gson().fromJson(reader, new TypeToken<ArrayList<LocationdBTriple>>() {
                            }.getType());


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mMap.clear();
                                    for (LocationdBTriple triple : results) {
                                        mMap.addMarker(new MarkerOptions().title(triple.getdB() + "")
                                                .position(new LatLng(triple.getLatitude(), triple.getLongitude())));
                                    }
                                    Dao.flushDataOfLastDay();
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //delete previous data
                    }
                });
                thread.start();
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

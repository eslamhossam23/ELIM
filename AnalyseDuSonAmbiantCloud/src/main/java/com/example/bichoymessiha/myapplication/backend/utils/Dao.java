package com.example.bichoymessiha.myapplication.backend.utils;


import com.example.bichoymessiha.myapplication.backend.models.Cluster;
import com.example.bichoymessiha.myapplication.backend.models.DataOfLastDay;
import com.example.bichoymessiha.myapplication.backend.models.LocationdBTriple;
import com.example.bichoymessiha.myapplication.backend.models.TimedBCouple;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.internal.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by bichoymessiha on 25-Jan-17.
 */

public class Dao {

    public static final String CHART_TIMEDB = "/chartTimedB";
    public static final String MAP_LOCATIONDB = "/mapLocationdB";
    public static final String CLUSTERS_TIMEDB = "/clustersTimeDb";
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DataOfLastDay dataOfLastDay = null;


    public static void writeClusters(ArrayList<Cluster> kmeansClusters) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        //DD/MM/YYYY
        String day = c.get(Calendar.DAY_OF_MONTH) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR);
        DatabaseReference myRef = database.getReference(day);
        myRef.child("timeDbClusters").setValue(kmeansClusters);
    }

    public static void askServerForData(final String type) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
//      DD-MM-YYYY
        String day = c.get(Calendar.DAY_OF_MONTH) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR);
        DatabaseReference chartRef = database.getReference(day + type);
        chartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                switch (type) {
                    case CHART_TIMEDB:
                        ArrayList<TimedBCouple> chartTimedB = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            chartTimedB.add(data.getValue(TimedBCouple.class));
                        }
                        dataOfLastDay = new DataOfLastDay(chartTimedB, null);
                        break;
                    case MAP_LOCATIONDB:
                        ArrayList<LocationdBTriple> mapLocationdB = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            mapLocationdB.add(data.getValue(LocationdBTriple.class));
                        }
                        dataOfLastDay = new DataOfLastDay(null, mapLocationdB);
                        break;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failed to read value.", error.toException().getMessage());
            }
        });
    }

    public static ArrayList<Cluster> getClustersList() {
        KMeans kmeans = new KMeans();
        kmeans.init(Dao.getDataRecieved().getChartTimedB());
        return kmeans.calculate();
    }

    public static ArrayList<LocationdBTriple> getMapLocationdBWithNoRedundancy(){
        return LocationOptimizer.removeRedundancy(dataOfLastDay.getMapLocationdB());
    }

    public static DataOfLastDay getDataRecieved() {
        return dataOfLastDay;
    }

    public static void flushDataOfLastDay() {
        dataOfLastDay = null;
    }
}

package com.eslamhossam23bichoymessiha.projetelim.util;

import android.util.Log;

import com.eslamhossam23bichoymessiha.projetelim.models.Cluster;
import com.eslamhossam23bichoymessiha.projetelim.models.DataOfLastDay;
import com.eslamhossam23bichoymessiha.projetelim.models.LocationdBTriple;
import com.eslamhossam23bichoymessiha.projetelim.models.TimedBCouple;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    public static void insertData(long time, float db, double lat, double lng) {
        // Write a message to the database
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(time));
        //DD/MM/YYYY
        String day = c.get(Calendar.DAY_OF_MONTH) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR);
        DatabaseReference myRef = database.getReference(day);
        TimedBCouple couple = new TimedBCouple(time, db);
        myRef.child("chartTimedB").push().setValue(couple);
        LocationdBTriple triple = new LocationdBTriple(lat, lng, db);
        myRef.child("mapLocationdB").push().setValue(triple);
        Log.d("TEST", "insertData: " + myRef.toString() + ".json");
    }

    public static void askFirebaseForData(final String type) {
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
                        dataOfLastDay = new DataOfLastDay(chartTimedB, null, null);
                        break;
                    case MAP_LOCATIONDB:
                        ArrayList<LocationdBTriple> mapLocationdB = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            mapLocationdB.add(data.getValue(LocationdBTriple.class));
                        }
                        dataOfLastDay = new DataOfLastDay(null, mapLocationdB, null);
                        break;
                    case CLUSTERS_TIMEDB:
                        ArrayList<Cluster> kmeansClusters = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            kmeansClusters.add(data.getValue(Cluster.class));
                        }
                        dataOfLastDay = new DataOfLastDay(null, null, kmeansClusters);
                        break;
                }


                Log.d("Mock: Data of last day", "Value is: " + dataOfLastDay);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("--", "Failed to read value.", error.toException());
            }
        });
    }

    public static DataOfLastDay getDataRecieved() {
        return dataOfLastDay;

    }

    public static void flushDataOfLastDay() {
        dataOfLastDay = null;
    }

}

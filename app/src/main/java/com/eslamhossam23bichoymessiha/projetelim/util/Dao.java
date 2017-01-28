package com.eslamhossam23bichoymessiha.projetelim.util;

import android.util.Log;

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
import java.util.HashMap;

/**
 * Created by bichoymessiha on 25-Jan-17.
 */

public class Dao {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DataOfLastDay dataOfLastDay = null;
    DatabaseReference myRef;
    public Dao() {

    }


    public void insertData(long time, float db, double lat, double lng) {
        // Write a message to the database
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(time));
        //DD/MM/YYYY
        String day = c.get(Calendar.DAY_OF_MONTH) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR);
        myRef = database.getReference(day);
        TimedBCouple couple = new TimedBCouple(time, db);
        myRef.child("chartTimedB").push().setValue(couple);
        LocationdBTriple triple = new LocationdBTriple(lat, lng, db);
        myRef.child("mapLocationdB").push().setValue(triple);
        Log.d("TEST", "insertData: " + myRef.toString()+".json");
    }

    public void askServerForData() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
//        DD/MM/YYYY
        String day = c.get(Calendar.DAY_OF_MONTH) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR);

        myRef = database.getReference(day);
        Log.d("TEST", "askServerForData: " + myRef.toString()+".json");


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> value = (HashMap<String, String>) dataSnapshot.getValue();
                Log.d("", "Value is: " + value);
                dataOfLastDay = new DataOfLastDay();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("--", "Failed to read value.", error.toException());
            }
        });
    }

    public DataOfLastDay getDataRecieved() {
        while (dataOfLastDay == null){
            Log.d("status", "waiting for Recieving data ");
        }
        return dataOfLastDay;
    }
    public void flushDataOfLastDay(){
        dataOfLastDay = null;
    }

}

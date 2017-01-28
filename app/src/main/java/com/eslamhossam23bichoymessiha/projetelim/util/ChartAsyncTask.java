package com.eslamhossam23bichoymessiha.projetelim.util;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.eslamhossam23bichoymessiha.projetelim.models.DataOfLastDay;
import com.eslamhossam23bichoymessiha.projetelim.models.TimedBCouple;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by bichoymessiha on 25-Jan-17.
 */

public class ChartAsyncTask extends AsyncTask<Chart,Integer, DataOfLastDay> {
    public Chart chart;
    public DataOfLastDay dataOfLastDay;
    @Override
    protected DataOfLastDay doInBackground(Chart... params) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.goOnline();
        this.chart = params[0];
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
//        DD/MM/YYYY
        String day = c.get(Calendar.DAY_OF_MONTH) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR);
        DatabaseReference myRef = firebaseDatabase.getReference(day+"/chartTimedB");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    HashMap<String, TimedBCouple> map = (HashMap<String, TimedBCouple>) data.getValue();
                    for(TimedBCouple couple : map.values()){
                        Log.e("TEST", "onDataChange: " + couple.getTime());
                        Log.e("TEST", "onDataChange: " + couple.getdB());
                    }

//                    TimedBCouple couple = entry.getValue();
                }

//                HashMap<String, TimedBCouple> chartTimedB = (HashMap<String,TimedBCouple>) dataSnapshot.getChildren().getValue();
//                for(String key : chartTimedB.keySet()){
//                    Log.d("TEST", "onDataChange: " + chartTimedB.get(key).getTime());
//                    Log.d("TEST", "onDataChange: " + chartTimedB.get(key).getdB());
//                }
                dataOfLastDay = new DataOfLastDay();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("--", "Failed to read value.", error.toException());
            }
        });

        return dataOfLastDay;
    }

    @Override
    protected void onPostExecute(DataOfLastDay dataOfLastDay) {
        super.onPostExecute(dataOfLastDay);
        Log.d("Here", String.valueOf(dataOfLastDay));
//        List<Entry> timedBEntries = new ArrayList<>();
//                for (TimedBCouple couple : dataOfLastDay.getChartTimedB()) {
//                    timedBEntries.add(new Entry(couple.getTime(), couple.getdB()));
//                }
//                LineDataSet dataSet = new LineDataSet(timedBEntries, "Data Of Last Day"); // add entries to dataset
//                dataSet.setColor(Color.BLUE);
//                dataSet.setValueTextColor(Color.BLACK);
//                dataSet.setDrawFilled(true);
//                dataSet.setFillColor(Color.CYAN);
//                dataSet.setFillAlpha(150);
//                dataSet.setDrawCircles(false);
//
//                LineData lineData = new LineData(dataSet);
//                chart.setData(lineData);
//                XAxis xAxis = chart.getXAxis();
//                xAxis.setValueFormatter(new TimeFormatter());
//                chart.invalidate();
    }
}

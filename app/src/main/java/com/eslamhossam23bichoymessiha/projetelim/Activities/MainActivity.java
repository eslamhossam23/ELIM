package com.eslamhossam23bichoymessiha.projetelim.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eslamhossam23bichoymessiha.projetelim.R;
import com.eslamhossam23bichoymessiha.projetelim.models.TimedBCouple;
import com.eslamhossam23bichoymessiha.projetelim.services.RecordingService;
import com.eslamhossam23bichoymessiha.projetelim.util.Dao;
import com.eslamhossam23bichoymessiha.projetelim.util.TimeFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public static String level;
    public LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, RecordingService.class);
        startService(intent);

        // button to retrieve data from server
        final Button chartButton = (Button) findViewById(R.id.chartButton);
        chartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Dao.askFirebaseForData(Dao.CHART_TIMEDB);
                chart = (LineChart) findViewById(R.id.chart);
                final Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (Dao.getDataRecieved() == null) {
                            Log.d("status", "waiting for Recieving data ");
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.d("Mock: Data of last day", "Value is: " + Dao.getDataRecieved());

                        final List<Entry> timedBEntries = new ArrayList<Entry>();
                        for (TimedBCouple couple : Dao.getDataRecieved().getChartTimedB()) {
                            timedBEntries.add(new Entry(couple.getTime(), couple.getdB()));
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                LineDataSet dataSet = new LineDataSet(timedBEntries, "Data Of Last Day"); // add entries to dataset
                                dataSet.setColor(Color.BLUE);
                                dataSet.setValueTextColor(Color.BLACK);
                                dataSet.setDrawFilled(true);
                                dataSet.setFillColor(Color.CYAN);
                                dataSet.setFillAlpha(150);
                                dataSet.setDrawCircles(false);
                                LineData lineData = new LineData(dataSet);

                                chart.setData(lineData);
                                XAxis xAxis = chart.getXAxis();
                                xAxis.setValueFormatter(new TimeFormatter());
                                chart.invalidate();
                                Dao.flushDataOfLastDay();
                            }
                        });
                        //delete previous data
                    }
                });
                thread.start();
            }
        });

        final Button screenshotButton = (Button) findViewById(R.id.screenshotButton);
        screenshotButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Calendar c = Calendar.getInstance();
                c.setTime(new Date(System.currentTimeMillis()));
                String dayOfMonth = c.get(Calendar.DAY_OF_MONTH) + "";
                String dayOfWeek = c.get(Calendar.DAY_OF_WEEK) + "";
                String month = c.get(Calendar.MONTH) + "";

                if (chart != null) {
                    chart.saveToGallery("IMG " + dayOfWeek + " " + dayOfMonth + " " + month, 100);
                } else {
                    Toast.makeText(MainActivity.this, " Please Get Data First", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button showMapButton = (Button) findViewById(R.id.showMapButton);
        showMapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                // saveAudio.stopRecording();
                startActivity(intent);
            }
        });

        final Button kmeansButton = (Button) findViewById(R.id.kmeansButton);
        kmeansButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, KmeansActivity.class);
                // saveAudio.stopRecording();
                startActivity(intent);
            }
        });
    }




}

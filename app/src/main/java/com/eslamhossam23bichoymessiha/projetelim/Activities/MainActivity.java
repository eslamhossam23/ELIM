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
    public static SaveAudio saveAudio;
    //Interval of time in milliseconds between two successive recordings
    public static final int PERIOD = 30000;
    //Delay before start of first recording
    public static final int DELAY = 1000;
    //Data separator
//    private static final String SEPARATOR = "-";
    //Unknown GPS
    public static final String UNKNOWN = "UNKNOWN";
    public static LocationManager locationManager;
    public static Location location;
    public LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Timer timer = new Timer();
        final SaveAudio saveAudio = new SaveAudio();
        timer.schedule(saveAudio, DELAY, PERIOD);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                MainActivity.location = location;
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, locationListener);
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/levels.txt");
        try {
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            FileWriter fileWriter = new FileWriter(file, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("Started app at " + currentDateTimeString);
            printWriter.println();
            printWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // button to retrieve data from server
        final Button chartButton = (Button) findViewById(R.id.chartButton);
        chartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Dao.askServerForData(Dao.CHART_TIMEDB);
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


    public static void setLevel(int levelInt) {
        level = String.valueOf(levelInt);
    }

    public class SaveAudio extends TimerTask {
        public MediaRecorder mediaRecorder;
        public static final int SECONDS = PERIOD / 6000;

        @Override
        public void run() {
            startRecording();
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/levels.txt");
            FileWriter fileWriter = null;
            PrintWriter printWriter = null;
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            try {
                fileWriter = new FileWriter(file, true);
                printWriter = new PrintWriter(fileWriter);
//                socket.sendToServer("Began recording at " + currentDateTimeString);

                printWriter.println("Began recording at " + currentDateTimeString);
                printWriter.println();
                printWriter.flush();
            } catch (IOException e) {
//                e.printStackTrace();
                Log.e("Recording", "Failed to start recording at" + currentDateTimeString);
            }
            for (int i = 0; i < SECONDS; i++) {
                int amplitude = mediaRecorder.getMaxAmplitude();
                float p = (float) (amplitude / 51805.5336);
                if (amplitude > 0) {
                    MainActivity.setLevel(amplitude);
                    Log.d("Audio", String.valueOf(amplitude));
                    currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    String[] time = currentDateTimeString.split(" ");
                    try {
                        final float db = (float) (20.0 * Math.log10(p / 0.00002));
                        if (printWriter != null) {
                            printWriter.println(time[3]);
                            if (location != null) {
                                Dao.insertData(System.currentTimeMillis(), db, location.getLatitude(), location.getLongitude());
                                printWriter.println("Lat: " + location.getLatitude() + " Long: " + location.getLongitude());
                            } else {
                                Dao.insertData(System.currentTimeMillis(), db, 0, 0);
                            }
                            printWriter.println("Noise level = " + db);
                            printWriter.println();
                            printWriter.flush();
                        }
                        Log.d("Amp", String.valueOf(amplitude));
                        Log.d("P", String.valueOf(p));
                        Log.d("Write", String.valueOf(db));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            if (printWriter != null) {
//                socket.sendToServer("Stopped recording at " + currentDateTimeString);
                printWriter.println("Stopped recording at " + currentDateTimeString);
                printWriter.println();
                printWriter.flush();
            }
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (IOException e) {
//                e.printStackTrace();
                Log.e("File", "Failed to liberate resources at " + currentDateTimeString);
            }
            stopRecording();
        }

        public void startRecording() {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setMaxDuration(10000);
            mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/audio.3gp");
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
            }
        }

        public void stopRecording() {
            mediaRecorder.reset();
            mediaRecorder.release();
        }


    }


}

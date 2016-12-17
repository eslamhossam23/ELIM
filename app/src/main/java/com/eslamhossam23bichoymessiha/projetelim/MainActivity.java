package com.eslamhossam23bichoymessiha.projetelim;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public static String level;
    public static SaveAudio saveAudio;
    //Interval of time in milliseconds between two successive recordings
    public static final int PERIOD = 10000;
    //Delay before start of first recording
    public static final int DELAY = 1000;
    public static LocationManager locationManager;
    public static Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timer timer = new Timer();
        SaveAudio saveAudio = new SaveAudio();
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
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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
    }

    public void updateFile(View v){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/levels.txt");
        FileWriter fileWriter;
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        try {
            fileWriter = new FileWriter(file, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(currentDateTimeString + " - Noise level = " + level);
            printWriter.close();
            fileWriter.close();
            Log.d("Write", level);
        } catch (Exception e) {
            e.printStackTrace();
        }


//        TextView textView = (TextView)findViewById(R.id.textView);
//        textView.setText(String.valueOf(mediaRecorder.getMaxAmplitude()));


    }

    public static void setLevel(int levelInt){
        level = String.valueOf(levelInt);
    }

    @Override
    protected void onDestroy() {
        saveAudio.stopRecording();
        super.onDestroy();

    }

    public class SaveAudio extends TimerTask {
        public MediaRecorder mediaRecorder;
        public static final int SECONDS = PERIOD/1000;
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
                printWriter.println("Began recording at " + currentDateTimeString);
                printWriter.println();
                printWriter.flush();
            } catch (IOException e) {
//                e.printStackTrace();
                Log.e("Recording", "Failed to start recording at" + currentDateTimeString);
            }
            for(int i = 0; i < SECONDS; i++){
                int amplitude = mediaRecorder.getMaxAmplitude();
                float p = (float) (amplitude/51805.5336);
                if(amplitude > 0){
                    MainActivity.setLevel(amplitude);
                    Log.d("Audio", String.valueOf(amplitude));
                    currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    String[] time = currentDateTimeString.split(" ");
                    try {
                        float db = (float) (20.0 * Math.log10(p/ 0.00002));
                        if (printWriter != null) {
                            printWriter.println(time[3]);
                            if(location != null){
                                printWriter.println("Lat: " + location.getLatitude() + " Long: " + location.getLongitude());
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void stopRecording() {
            mediaRecorder.reset();
            mediaRecorder.release();
        }
    }


}

package com.eslamhossam23bichoymessiha.projetelim.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.eslamhossam23bichoymessiha.projetelim.activities.MainActivity;
import com.eslamhossam23bichoymessiha.projetelim.util.Dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class RecordingService extends Service {
    public static SaveAudio saveAudio;
    //Interval of time in milliseconds between two successive recordings
    public static final int PERIOD = 30000;
    //Delay before start of first recording
    public static final int DELAY = 1000;

    public static LocationManager locationManager;
    public static Location location;


    public RecordingService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Timer timer = new Timer();
        saveAudio = new SaveAudio();
        timer.schedule(saveAudio, DELAY, PERIOD);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                RecordingService.location = location;
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
        return super.onStartCommand(intent, flags, startId);
    }

    public class SaveAudio extends TimerTask {
        public MediaRecorder mediaRecorder;
        public static final int SECONDS = 1;

        @Override
        public void run() {
            startRecording();

            for (int i = 0; i <= SECONDS; i++) {
                int amplitude = mediaRecorder.getMaxAmplitude();
                float p = (float) (amplitude / 51805.5336);
                if (amplitude > 0) {
                    Log.d("Audio", String.valueOf(amplitude));
                    try {
                        final float db = (float) (20.0 * Math.log10(p / 0.00002));
                            if (location != null) {
                                Dao.insertData(System.currentTimeMillis(), db, location.getLatitude(), location.getLongitude());
                            } else {
                                Dao.insertData(System.currentTimeMillis(), db, 0, 0);
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

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

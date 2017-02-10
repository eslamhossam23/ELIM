package com.eslamhossam23bichoymessiha.projetelim.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eslamhossam23bichoymessiha.projetelim.R;
import com.eslamhossam23bichoymessiha.projetelim.models.Cluster;
import com.eslamhossam23bichoymessiha.projetelim.models.DataOfLastDay;
import com.eslamhossam23bichoymessiha.projetelim.models.LocationdBTriple;
import com.eslamhossam23bichoymessiha.projetelim.util.Dao;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class KmeansActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kmeans);


        final Button returnButton = (Button) findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(KmeansActivity.this, MainActivity.class);
                // saveAudio.stopRecording();
                startActivity(intent);
            }
        });

        final Button calculateButton = (Button) findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://analyse-du-son-ambiant.appspot.com/kmeans");
                            InputStream input = url.openStream();
                            Reader reader = new InputStreamReader(input, "UTF-8");
                            final ArrayList<Cluster> results = new Gson().fromJson(reader, new TypeToken<ArrayList<Cluster>>(){}.getType());
                                  runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            TextView textView = (TextView) findViewById(R.id.result);
                                            textView.setText("");
                                            Calendar calendar = Calendar.getInstance();
                                            for (Cluster cluster : results) {
                                                Date date = new Date((long) cluster.getCentroid().getTime());
                                                calendar.setTime(date);
                                                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                                                int minutes = calendar.get(Calendar.MINUTE);
                                                textView.append("Around " + hours + "h" + minutes + "m, You have been exposed to an average of "
                                                        + Math.round(cluster.getCentroid().getdB()) + "db" + "\n");
                                            }
                                            textView.append("\n");
//                                    textView.append("Minimum dB you have been exposed to : " + Dao.getDataRecieved().getMinimumdB() + "\n");
//                                    textView.append("Maximum dB you have been exposed to : " + Dao.getDataRecieved().getMaximumdB() + "\n");
                                            textView.append("\n");
//                                    for (LocationdBTriple triple : Dao.getDataRecieved().getMapLocationdB()) {
//                                        textView.append("Average Noise you were exposed to at location:"
//                                                + triple.getLatitude() + ","
//                                                + triple.getLongitude()
//                                                + "is " + triple.getdB() + " dB" + "\n");
//                                    }

                                        }
                                    });
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });
    }
}

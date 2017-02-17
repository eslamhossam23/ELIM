package com.eslamhossam23bichoymessiha.projetelim.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.eslamhossam23bichoymessiha.projetelim.R;
import com.eslamhossam23bichoymessiha.projetelim.models.Cluster;
import com.eslamhossam23bichoymessiha.projetelim.models.TimedBCouple;
import com.eslamhossam23bichoymessiha.projetelim.services.RecordingService;
import com.eslamhossam23bichoymessiha.projetelim.util.Dao;
import com.eslamhossam23bichoymessiha.projetelim.util.TimeFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class KmeansActivity extends AppCompatActivity {

    public BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kmeans);


        final ImageButton screenshotButton = (ImageButton) findViewById(R.id.screenshotButton);
        screenshotButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Calendar c = Calendar.getInstance();
                c.setTime(new Date(System.currentTimeMillis()));
                String dayOfMonth = c.get(Calendar.DAY_OF_MONTH) + "";
                String dayOfWeek = c.get(Calendar.DAY_OF_WEEK) + "";
                String month = c.get(Calendar.MONTH) + "";

                if (barChart != null) {
                    barChart.saveToGallery("IMG_Report " + dayOfWeek + " " + dayOfMonth + " " + month, 100);
                    Toast.makeText(KmeansActivity.this, "Saved To Gallery.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(KmeansActivity.this, " Please Get Data First", Toast.LENGTH_SHORT).show();
                }
            }
        });
        final Button calculateButton = (Button) findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!RecordingService.NetworkChangeReceiver.connected){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(KmeansActivity.this,"You must connect to the internet.",Toast.LENGTH_SHORT).show();
                        }
                    });

                }else {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL("http://analyse-du-son-ambiant.appspot.com/kmeans");
                                InputStream input = url.openStream();
                                Reader reader = new InputStreamReader(input, "UTF-8");
                                final ArrayList<Cluster> results = new Gson().fromJson(reader, new TypeToken<ArrayList<Cluster>>() {
                                }.getType());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TextView textView = (TextView) findViewById(R.id.result);
                                        textView.setText("");
                                        Calendar calendar = Calendar.getInstance();
                                        barChart = (BarChart) findViewById(R.id.barChart);
                                        double minimumClusterTime = results.get(0).getCentroid().getTime();
                                        double maximumClusterTime = results.get(0).getCentroid().getTime();
                                        final List<BarEntry> clusterEntries = new ArrayList<>();
                                        ArrayList<Integer> colors = new ArrayList<Integer>();
                                        for (Cluster cluster : results) {
                                            clusterEntries.add(new BarEntry((float) cluster.getCentroid().getTime(), (float) cluster.getCentroid().getdB()));
                                            if (minimumClusterTime > cluster.getCentroid().getTime()) {
                                                minimumClusterTime = cluster.getCentroid().getTime();
                                            }
                                            if (maximumClusterTime < cluster.getCentroid().getTime()) {
                                                maximumClusterTime = cluster.getCentroid().getTime();
                                            }
                                            if (cluster.getCentroid().getdB() < 50) {
                                                colors.add(Color.GREEN);
                                            } else if (cluster.getCentroid().getdB() < 70) {
                                                colors.add(Color.rgb(255, 165, 0));
                                            } else {
                                                colors.add(Color.RED);
                                            }
                                            Date date = new Date((long) cluster.getCentroid().getTime());
                                            calendar.setTime(date);
                                            int hours = calendar.get(Calendar.HOUR_OF_DAY);
                                            int minutes = calendar.get(Calendar.MINUTE);
                                            textView.append("Around " + hours + "h" + minutes + "m, You have been exposed to an average of "
                                                    + Math.round(cluster.getCentroid().getdB()) + "db" + "\n");
                                        }
                                        BarDataSet barDataSet = new BarDataSet(clusterEntries, "Last Day Report");
                                        barDataSet.setColors(colors);
                                        BarData barData = new BarData(barDataSet);
                                        float barWidth = 700000f;
                                        barData.setBarWidth(barWidth);
                                        barChart.setData(barData);
                                        barChart.setFitBars(true);
                                        barChart.setDoubleTapToZoomEnabled(false);
                                        barChart.setScaleEnabled(false);
                                        barChart.setHighlightPerTapEnabled(false);
                                        barChart.setHighlightPerDragEnabled(false);
                                        barChart.getLegend().setEnabled(false);
                                        barChart.getDescription().setText("");
                                        XAxis xAxis = barChart.getXAxis();
                                        xAxis.setAxisMinimum((float) minimumClusterTime - barWidth);
                                        xAxis.setAxisMaximum((float) maximumClusterTime + barWidth);
                                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                        xAxis.setValueFormatter(new TimeFormatter());
                                        barChart.invalidate();
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
            }
        });
    }
}

package com.eslamhossam23bichoymessiha.projetelim;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

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
                MainActivity.socket.askServerToCalculateKmeans();
                DataOfLastDay dataRecieved = MainActivity.socket.getDataRecieved();
                Log.d("object", dataRecieved.toString());
                TextView textView = (TextView) findViewById(R.id.result);
                Calendar calendar = Calendar.getInstance();
                for(Cluster cluster : dataRecieved.getKmeansClusters()){
                    Date date = new Date((long) cluster.getCentroid().getTime());
                    calendar.setTime(date);
                    int hours = calendar.get(Calendar.HOUR_OF_DAY);
                    int minutes = calendar.get(Calendar.MINUTE);
                    textView.append("Around " + hours + "h" + minutes + "m, You have been exposed to an average of "
                            + Math.round(cluster.getCentroid().getdB()) + "db" + "\n");
                }
            }
        });

    }
}

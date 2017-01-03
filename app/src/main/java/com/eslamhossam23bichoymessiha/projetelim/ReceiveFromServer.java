package com.eslamhossam23bichoymessiha.projetelim;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * Created by Havoc on 0029-29-12-2016.
 */

// Needs to be initialised in a thread in the MainActivity
public class ReceiveFromServer implements Runnable {

    ObjectInputStream objectInputStream;
    DataOfLastDay dataRecieved = null;

    @Override
    public void run() {
        try {
            objectInputStream = new ObjectInputStream(SendToServer.getSocket().getInputStream());
            while (true) {

                Object readObject = null;

                while ((readObject = objectInputStream.readObject()) != null) {
                    // Needs to be tested
                    if (readObject instanceof DataOfLastDay) {
                        // Logic based on data received from server
//                        Log.d("object", readObject.toString());
                        dataRecieved = (DataOfLastDay) readObject;
                    }
                }


            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public DataOfLastDay getDataRecieved() {
        return dataRecieved;
    }
}

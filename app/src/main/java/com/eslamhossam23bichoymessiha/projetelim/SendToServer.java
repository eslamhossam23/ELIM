package com.eslamhossam23bichoymessiha.projetelim;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Havoc on 0018-18-12-2016.
 */

public class SendToServer implements Runnable {

    public static Socket socket;
    private String data;
    private ObjectInputStream objectInputStream;
    DataOfLastDay dataRecieved = null;

    @Override
    public void run() {
        try {
            socket = new Socket("10.212.109.228", 3000);
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            sendToServer("Started app at " + currentDateTimeString);

            objectInputStream = new ObjectInputStream(socket.getInputStream());
            while (true) {
                try {

                    Object readObject = null;

                    while ((readObject = objectInputStream.readObject()) != null) {
                        // Needs to be tested
                        if (readObject instanceof DataOfLastDay) {
                            // Logic based on data received from server
//                        Log.d("object", readObject.toString());
                            dataRecieved = (DataOfLastDay) readObject;
                        }
                    }


                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
//          e.printStackTrace();
            Log.e("Socket", "Couldn't initialise socket.");
        }
    }

    public void sendToServer(final String data) {
        final String message = data + "\n";
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(message.getBytes());
                } catch (IOException e) {
                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    Log.e("Socket", "Couldn't send data. " + message + currentDateTimeString);
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    public void askServerForData() {
        final String message = "send me data please" + "\n";
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(message.getBytes());
                } catch (IOException e) {
                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    Log.e("Socket", "Couldn't send data. " + message + currentDateTimeString);
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    public DataOfLastDay getDataRecieved() {
        while (dataRecieved == null){
            Log.d("status", "waiting for Recieving data ");
        }
        return dataRecieved;
    }

    public static Socket getSocket() {
        return socket;
    }
}


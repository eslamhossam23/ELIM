package com.eslamhossam23bichoymessiha.projetelim;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Havoc on 0018-18-12-2016.
 */

public class SendToServer implements Runnable {

    private Socket socket;
    private String data;

    @Override
    public void run() {
        try {
            socket = new Socket("192.168.1.52", 3000);
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            sendToServer("Started app at " + currentDateTimeString);
        } catch (IOException e) {
//                    e.printStackTrace();
            Log.e("Socket", "Couldn't initialise socket.");
        }
    }

    public void sendToServer(final String data){
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

    public Socket getSocket() {
        return socket;
    }
}


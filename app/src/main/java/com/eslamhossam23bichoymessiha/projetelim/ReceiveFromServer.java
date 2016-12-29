package com.eslamhossam23bichoymessiha.projetelim;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * Created by Havoc on 0029-29-12-2016.
 */

// Needs to be initialised in a thread in the MainActivity
public class ReceiveFromServer implements Runnable {

    @Override
    public void run() {
        while (true){
            try {
                InputStream inputStream = SendToServer.socket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                Object readObject = null;
                while ((readObject = objectInputStream.readObject()) != null){
                    // Needs to be tested
                    if(readObject.getClass().equals(DataOfLastDay.class)){
                        // Logic based on data received from server
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

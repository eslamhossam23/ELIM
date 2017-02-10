package com.example.bichoymessiha.myapplication.backend;

import com.example.bichoymessiha.myapplication.backend.utils.Dao;
import com.google.appengine.api.ThreadManager;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.internal.Log;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by bichoymessiha on 10-Feb-17.
 */

public class LocationServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, final HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setServiceAccount(getServletContext().getResourceAsStream("/WEB-INF/Analyse du son ambiant-7738ccc36cb1.json"))
                .setDatabaseUrl("https://analyse-du-son-ambiant.firebaseio.com/")
                .build();

        try {
            FirebaseApp.getInstance();
        } catch (Exception error) {
            Log.i("Info", "doesn't exist...");
        }

        try {
            FirebaseApp.initializeApp(options);
        } catch (Exception error) {
            Log.i("Info", "already exists...");
        }
        Thread thread = ThreadManager.createBackgroundThread(new Runnable() {
            @Override
            public void run() {
                Dao.askServerForData(Dao.MAP_LOCATIONDB);
            }
        });
        thread.start();
        while(Dao.dataOfLastDay == null){

        }
        Gson gson = new Gson();
        resp.getWriter().print(gson.toJson(Dao.getMapLocationdBWithNoRedundancy()));
        Dao.flushDataOfLastDay();

    }
}

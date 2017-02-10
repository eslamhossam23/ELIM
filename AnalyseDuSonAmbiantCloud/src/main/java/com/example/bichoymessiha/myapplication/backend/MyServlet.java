/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package com.example.bichoymessiha.myapplication.backend;

import com.example.bichoymessiha.myapplication.backend.utils.Dao;
import com.google.appengine.api.ThreadManager;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.internal.Log;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyServlet extends HttpServlet {
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
                Dao.askServerForData(Dao.CHART_TIMEDB);
            }
        });
        thread.start();
        while(Dao.dataOfLastDay == null){

        }
        while (Dao.getClustersList() == null){

        }
        Gson gson = new Gson();
        resp.getWriter().print(gson.toJson(Dao.getClustersList()));
        Dao.flushDataOfLastDay();

    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String name = req.getParameter("name");
        resp.setContentType("text/plain");
        if (name == null) {
            resp.getWriter().println("Please enter a name");
        }
        resp.getWriter().println("Hello " + name);
    }
}

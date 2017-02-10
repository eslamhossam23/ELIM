package com.example.bichoymessiha.myapplication.backend.utils;

import com.example.bichoymessiha.myapplication.backend.models.LocationdBTriple;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bichoymessiha on 10-Feb-17.
 */

public class LocationOptimizer {
    private static final int RADIUS = 100;

    public static ArrayList<LocationdBTriple> removeRedundancy(ArrayList<LocationdBTriple> list) {
        if (list.size() <= 0) {
            return null;
        }
        HashMap<Integer, Integer> numberOfValues = new HashMap<>();
        ArrayList<LocationdBTriple> result = new ArrayList<>();
        result.add(list.get(0));
        numberOfValues.put(0, 1);

        for (int i = 1; i < list.size(); i++) {
            boolean near = false;
            //compare next value to all the previous
            for (LocationdBTriple triple : result) {
                if (isNear(triple, list.get(i), RADIUS)) {
                    triple.setdB(triple.getdB() + list.get(i).getdB());
                    numberOfValues.put(result.indexOf(triple), numberOfValues.get(result.indexOf(triple)) + 1);
                    near = true;
                    break;
                }
            }
            if (!near) {
                result.add(list.get(i));
                numberOfValues.put(numberOfValues.size(), 1);
            }
        }
        for (int i = 0; i < result.size(); i++) {
            float averagedB = result.get(i).getdB()/numberOfValues.get(i);
            result.get(i).setdB(averagedB);
        }

        return result;
    }

    private static boolean isNear(LocationdBTriple triple, LocationdBTriple newPoint, int radius) {
        double distance = distance(triple.getLatitude(), newPoint.getLatitude(),
                triple.getLongitude(), newPoint.getLongitude(), 0, 0);

        return (distance < radius);

    }

    public static double distance(double lat1, double lat2, double lon1, double lon2, double el1, double el2) {
        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}

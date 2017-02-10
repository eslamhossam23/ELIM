package com.eslamhossam23bichoymessiha.projetelim.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by bichoymessiha on 04-Jan-17.
 */
public class Point implements Serializable {

    private double time = 0;
    private double dB = 0;
    private int cluster_number = 0;

    public Point() {
    }

    public Point(double time, double dB) {
        this.setTime(time);
        this.setdB(dB);
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getTime() {
        return this.time;
    }

    public void setdB(double dB) {
        this.dB = dB;
    }

    public double getdB() {
        return this.dB;
    }

    public void setCluster(int n) {
        this.cluster_number = n;
    }

    public int getCluster() {
        return this.cluster_number;
    }

    //Calculates the distance between two points.
    public static double distance(Point p, Point centroid) {
        return Math.sqrt(Math.pow((centroid.getdB() - p.getdB()), 2) + Math.pow((centroid.getTime() - p.getTime()), 2));
    }

    //Get Points from timedBchart
    public static ArrayList<Point> getPointsFromTimedBChart(ArrayList<TimedBCouple> timedBchart) {
        ArrayList<Point> points = new ArrayList<>();
        for (TimedBCouple couple : timedBchart) {
            points.add(new Point(couple.getTime(), couple.getdB()));
        }
        return points;
    }

    public static Point chooseRandomPoint(ArrayList<Point> points) {
        Random r = new Random();
        int randomNumber = r.nextInt(points.size());
        return points.get(randomNumber);
    }

    //Creates random point
    protected static Point createRandomPoint(int min, int max) {
        Random r = new Random();
        double x = min + (max - min) * r.nextDouble();
        double y = min + (max - min) * r.nextDouble();
        return new Point(x, y);
    }

    protected static List createRandomPoints(int min, int max, int number) {
        List points = new ArrayList(number);
        for (int i = 0; i < number; i++) {
            points.add(createRandomPoint(min, max));
        }
        return points;
    }

    public String toString() {
        return "(" + time + "," + dB + ")";
    }
}
package com.eslamhossam23bichoymessiha.projetelim;

/**
 * Created by bichoymessiha on 04-Jan-17.
 */


import java.io.Serializable;
import java.util.ArrayList;

public class Cluster implements Serializable{

    public ArrayList<Point> points;
    public Point centroid;
    public int id;

    //Creates a new Cluster
    public Cluster(int id) {
        this.id = id;
        this.points = new ArrayList();
        this.centroid = null;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }

    public Point getCentroid() {
        return centroid;
    }

    public void setCentroid(Point centroid) {
        this.centroid = centroid;
    }

    public int getId() {
        return id;
    }

    public void clear() {
        points.clear();
    }

    public void plotCluster() {
        System.out.println("[Cluster: " + id+"]");
        System.out.println("[Centroid: " + centroid + "]");
        System.out.println("[Points: \n");
        for(Point p : points) {
            System.out.println(p);
        }
        System.out.println("]");
    }

    @Override
    public String toString() {
        return "Cluster{" +
                "id=" + id +
                ", centroid=" + centroid +
                ", points=" + points +
                '}';
    }
}


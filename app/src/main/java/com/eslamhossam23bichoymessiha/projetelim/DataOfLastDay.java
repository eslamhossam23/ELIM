package com.eslamhossam23bichoymessiha.projetelim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author bichoymessiha
 */
public class DataOfLastDay implements Serializable {

    private ArrayList<TimedBCouple> chartTimedB = new ArrayList<>();
    private ArrayList<LocationdBTriple> mapLocationdB = new ArrayList<>();
    private ArrayList<Cluster> kmeansClusters;
    private float minimumdB;
    private float maximumdB;

    public DataOfLastDay() {
    }

    public ArrayList<TimedBCouple> getChartTimedB() {
        return chartTimedB;
    }

    public ArrayList<LocationdBTriple> getMapLocationdB() {
        return mapLocationdB;
    }

    public float getMinimumdB() {
        return minimumdB;
    }

    public float getMaximumdB() {
        return maximumdB;
    }

    public void setChartTimedB(ArrayList<TimedBCouple> chartTimedB) {
        this.chartTimedB = chartTimedB;
    }

    public void setMapLocationdB(ArrayList<LocationdBTriple> mapLocationdB) {
        this.mapLocationdB = mapLocationdB;
    }

    public void setMinimumdB(float minimumdB) {
        this.minimumdB = minimumdB;
    }

    public void setMaximumdB(float maximumdB) {
        this.maximumdB = maximumdB;
    }

    public ArrayList<Cluster> getKmeansClusters() {
        return kmeansClusters;
    }

    public void setKmeansClusters(ArrayList<Cluster> kmeansClusters) {
        this.kmeansClusters = kmeansClusters;
    }

    @Override
    public String toString() {
        String str = "chartTimedB=" + chartTimedB
                + ", minimumdB=" + minimumdB
                + ", maximumdB=" + maximumdB
                + ", mapLocationdB={" + mapLocationdB
                + ", clusters={" + kmeansClusters + "}";
        return str;
    }
}

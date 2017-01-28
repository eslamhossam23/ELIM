/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eslamhossam23bichoymessiha.projetelim.models;

import java.io.Serializable;

/**
 *
 * @author bichoymessiha
 */
public class TimedBCouple implements Serializable{
    public long time;
    public float dB;

    public TimedBCouple() {
    }

    public TimedBCouple(long time, float dB) {
        this.time = time;
        this.dB = dB;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getdB() {
        return dB;
    }

    public void setdB(float dB) {
        this.dB = dB;
    }

    @Override
    public String toString() {
        return "time: " + time + " , dB:" + dB + '}';
    }

}

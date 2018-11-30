/*Objeto basado en la tabla de la API HourInterval*/
package com.example.carlo.androidapp.modelos;

import java.io.Serializable;

public class HourInterval implements Serializable {

    private long startTime;
    private long endTime;
    private int frequency;

    public HourInterval(long startTime, long endTime, int frequency) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.frequency = frequency;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}

/*Objeto basado en la tabla de la API DateInterval*/
package com.example.carlo.androidapp.modelos;

import java.io.Serializable;

public class DateInterval implements Serializable {

    private long startDate;
    private long  endDate;
    private boolean service;
    private int id;

    public DateInterval(long startDate, long endDate, boolean service) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.service = service;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public boolean isService() {
        return service;
    }

    public void setService(boolean service) {
        this.service = service;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}

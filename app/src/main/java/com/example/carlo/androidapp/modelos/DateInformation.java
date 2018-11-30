/*Objeto basado en la tabla de la API DateInformation*/
package com.example.carlo.androidapp.modelos;

import java.io.Serializable;

public class DateInformation implements Serializable {

    private int dateId;
    private int hourId;
    private int id;

    public DateInformation(int dateId, int hourId) {
        this.dateId = dateId;
        this.hourId = hourId;
    }

    public int getDateId() {
        return dateId;
    }

    public void setDateId(int dateId) {
        this.dateId = dateId;
    }

    public int getHourId() {
        return hourId;
    }

    public void setHourId(int hourId) {
        this.hourId = hourId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

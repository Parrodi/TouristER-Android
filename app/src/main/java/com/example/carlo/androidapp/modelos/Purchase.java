/*Objeto basado en la tabla de la API Purchase*/
package com.example.carlo.androidapp.modelos;

import java.io.Serializable;
import java.util.HashMap;

public class Purchase implements Serializable {
    private int id;
    private double total;
    private long unixdate;
    private String tourname;
    private HashMap<String,Integer> typecount = new HashMap<>();

    public Purchase(int id, long unixdate) {
        this.id = id;
        this.unixdate = unixdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUnixdate() {
        return unixdate;
    }

    public void setUnixdate(long unixdate) {
        this.unixdate = unixdate;
    }

    public String getTourname() {
        return tourname;
    }

    public void setTourname(String tourname) {
        this.tourname = tourname;
    }

    public HashMap<String, Integer> getTypecount() {
        return typecount;
    }

    public void addCountToMap(String key){
        if(typecount.containsKey(key)){
            int count = typecount.get(key);
            typecount.put(key,count+1);
        }
        else typecount.put(key,1);
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

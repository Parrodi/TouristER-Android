/*Objeto basado en la tabla de la API PlaceType*/
package com.example.carlo.androidapp.modelos;

public class PlaceType {

    private int id;
    private String name;
    private Place[] places;

    public PlaceType(int id, String name, Place[] places) {
        this.id = id;
        this.name = name;
        this.places = places;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Place[] getPlaces() {
        return places;
    }

    public void setPlaces(Place[] places) {
        this.places = places;
    }
}

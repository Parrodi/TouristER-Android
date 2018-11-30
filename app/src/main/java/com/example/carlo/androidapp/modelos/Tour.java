/*Objeto basado en la tabla de la API Tour*/
package com.example.carlo.androidapp.modelos;

import java.io.Serializable;
import java.net.URL;

public class Tour implements Serializable {

    private int id;
    private String name;
    private URL image;
    private String description;
    private Place[] places;
    private DateInformation[] dateInformations;

    public Tour() {

    }

    public Tour(int id, String name, URL image, String description, Place[] places, DateInformation[] dateInformations) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.places = places;
        this.dateInformations = dateInformations;
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

    public URL getImage() {
        return image;
    }

    public void setImage(URL image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Place[] getPlaces() {
        return places;
    }

    public void setPlaces(Place[] places) {
        this.places = places;
    }

    public DateInformation[] getDateInformations() {
        return dateInformations;
    }

    public void setDateInformations(DateInformation[] dateInformations) {
        this.dateInformations = dateInformations;
    }

    public int sizeDates(){
        return dateInformations.length;
    }
}

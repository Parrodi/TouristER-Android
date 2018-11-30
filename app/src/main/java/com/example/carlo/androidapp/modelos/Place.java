/*Objeto basado en la tabla de la API Place*/
package com.example.carlo.androidapp.modelos;

import java.io.Serializable;
import java.net.URL;

public class Place implements Serializable {

    private  int id;
    private String name;
    private String description;
    private int placeTypeId;
    private double latitude;
    private double longitude;
    private String narrativeUrl;
    private String imageUrl;
    private Tour[] tours;

    public Place(String name, String description, int placeTypeId, double latitude, double longitude) {
        this.name = name;
        this.description = description;
        this.placeTypeId = placeTypeId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPlaceTypeId() {
        return placeTypeId;
    }

    public void setPlaceTypeId(int placeTypeId) {
        this.placeTypeId = placeTypeId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNarrativeUrl() {
        return narrativeUrl;
    }

    public void setNarrativeUrl(String narrativeUrl) {
        this.narrativeUrl = narrativeUrl;
    }

    public Tour[] getTours() {
        return tours;
    }

    public void setTours(Tour[] tours) {
        this.tours = tours;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

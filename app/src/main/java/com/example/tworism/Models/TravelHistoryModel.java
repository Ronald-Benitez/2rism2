package com.example.tworism.Models;

import com.example.tworism.Retrofit.RecentsDataModel;

public class TravelHistoryModel {
    private String TravelRegisterId;
    private RecentsDataModel Travel;

    public TravelHistoryModel(String travelRegisterId, RecentsDataModel travel) {
        TravelRegisterId = travelRegisterId;
        Travel = travel;
    }

    public TravelHistoryModel() {
    }

    public String getTravelRegisterId() {
        return TravelRegisterId;
    }

    public void setTravelRegisterId(String travelRegisterId) {
        TravelRegisterId = travelRegisterId;
    }

    public RecentsDataModel getTravel() {
        return Travel;
    }

    public void setTravel(RecentsDataModel travel) {
        Travel = travel;
    }
}

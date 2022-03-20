package com.example.gpstracker;

import android.app.Application;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class MyAplication extends Application {
    private static MyAplication singleton;
    private List<Location> myLocations;
    public MyAplication getInstance(){
        return singleton;
    }
    public void onCreate(){
        super.onCreate();
        singleton = this;
        myLocations = new ArrayList<>();
    }

    public List<Location> getMyLocations() {
        return myLocations;
    }

    public void setMyLocations(List<Location> myLocations) {
        this.myLocations = myLocations;
    }
}

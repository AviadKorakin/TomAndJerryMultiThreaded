package com.tomandjerry.tomandjerryv2.Utilities;

import androidx.annotation.NonNull;


public class MyScore implements Comparable<MyScore> {
    private int score;
    private int meters;
    private double lat;
    private double lng;
    private final String delimiter = "/";

    public MyScore(int score,int meters, double lat, double lng) {
        this.score = score;
        this.meters=meters;
        this.lat = lat;
        this.lng = lng;
    }

    public MyScore(String composedLocation) {
        String[] composed = composedLocation.split(delimiter);
        this.score = Integer.parseInt(composed[0]);
        this.meters = Integer.parseInt(composed[1]);
        this.lat = Double.parseDouble(composed[2]);
        this.lng = Double.parseDouble(composed[3]);

    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getMeters() {
        return meters;
    }

    public void setMeters(int meters) {
        this.meters = meters;
    }

    @NonNull
    @Override
    public String toString() {
        return score + delimiter +meters+delimiter+ lat + delimiter + lng;
    }


    @Override
    public int compareTo(MyScore o) {
         if(o.score - this.score == 0 )return  o.meters-this.meters;
                else return o.score-this.score;
    }
}

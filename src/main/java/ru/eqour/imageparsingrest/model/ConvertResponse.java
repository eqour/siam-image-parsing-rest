package ru.eqour.imageparsingrest.model;

public class ConvertResponse {

    private double[][] points;

    public ConvertResponse() {}

    public ConvertResponse(double[][] points) {
        this.points = points;
    }

    public double[][] getPoints() {
        return points;
    }

    public void setPoints(double[][] points) {
        this.points = points;
    }
}

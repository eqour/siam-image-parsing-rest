package ru.eqour.imageparsingrest.model;

public class SmoothResponse {

    private int iteration;
    private int[][] points;

    public SmoothResponse() {
    }

    public SmoothResponse(int iteration, int[][] points) {
        this.iteration = iteration;
        this.points = points;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public int[][] getPoints() {
        return points;
    }

    public void setPoints(int[][] points) {
        this.points = points;
    }
}

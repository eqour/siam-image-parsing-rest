package ru.eqour.imageparsingrest.model;

public class SmoothRequest {

    private Integer maxIteration;
    private int[][] points;

    public Integer getMaxIteration() {
        return maxIteration;
    }

    public void setMaxIteration(Integer maxIteration) {
        this.maxIteration = maxIteration;
    }

    public int[][] getPoints() {
        return points;
    }

    public void setPoints(int[][] points) {
        this.points = points;
    }
}

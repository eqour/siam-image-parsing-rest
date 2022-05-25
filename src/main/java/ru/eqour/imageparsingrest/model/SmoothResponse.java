package ru.eqour.imageparsingrest.model;

import io.swagger.annotations.ApiModelProperty;

public class SmoothResponse {

    @ApiModelProperty(value = "номер последней итерации алгоритма сглаживания", example = "3", required = true)
    private int iteration;
    @ApiModelProperty(value = "координаты точек", example = "[[3,7],[8,5],[2,0]]", required = true)
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

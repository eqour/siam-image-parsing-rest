package ru.eqour.imageparsingrest.model;

import io.swagger.annotations.ApiModelProperty;

public class ConvertResponse {

    @ApiModelProperty(value = "координаты точек", example = "[[3.0,7.0],[8.4,5.0],[2.7,0.5]]", required = true)
    private double[][] points;

    public ConvertResponse() {
    }

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

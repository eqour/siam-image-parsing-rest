package ru.eqour.imageparsingrest.model;

import io.swagger.annotations.ApiModelProperty;

public class ConvertResponse {

    @ApiModelProperty(value = "координаты точек")
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

package ru.eqour.imageparsingrest.model;

import io.swagger.annotations.ApiModelProperty;

public class ConvertRequest {

    @ApiModelProperty(value = "координаты точек", example = "[[5, 7],[4, 8],[6, 2]]", required = true)
    private double[][] points;

    public ConvertRequest() {
    }

    public ConvertRequest(double[][] points) {
        this.points = points;
    }

    public double[][] getPoints() {
        return points;
    }

    public void setPoints(double[][] points) {
        this.points = points;
    }
}

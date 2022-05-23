package ru.eqour.imageparsingrest.model;

import io.swagger.annotations.ApiModelProperty;

public class SmoothRequest {

    @ApiModelProperty(value = "максимальная итерация", example = "5", required = true)
    private Integer maxIteration;
    @ApiModelProperty(value = "координаты точек", example = "[[5, 7],[4, 8],[6, 2]]", required = true)
    private int[][] points;

    public SmoothRequest() {
    }

    public SmoothRequest(Integer maxIteration, int[][] points) {
        this.maxIteration = maxIteration;
        this.points = points;
    }

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

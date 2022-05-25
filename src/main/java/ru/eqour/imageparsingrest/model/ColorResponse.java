package ru.eqour.imageparsingrest.model;

import io.swagger.annotations.ApiModelProperty;

public class ColorResponse {

    @ApiModelProperty(value = "координаты пикселей", example = "[[3,7],[8,5],[2,0]]", required = true)
    private int[][] pixels;

    public ColorResponse() {
    }

    public ColorResponse(int[][] pixels) {
        this.pixels = pixels;
    }

    public int[][] getPixels() {
        return pixels;
    }

    public void setPixels(int[][] pixels) {
        this.pixels = pixels;
    }
}

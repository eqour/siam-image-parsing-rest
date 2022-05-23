package ru.eqour.imageparsingrest.model;

import io.swagger.annotations.ApiModelProperty;

import java.awt.*;

public class ColorAreaRequest extends ColorEntireRequest {

    @ApiModelProperty(value = "минимальная граница поиска по ширине", example = "5", required = true)
    private Integer minX;
    @ApiModelProperty(value = "минимальная граница поиска по высоте", example = "2", required = true)
    private Integer minY;
    @ApiModelProperty(value = "максимальная граница поиска по ширине", example = "10", required = true)
    private Integer maxX;
    @ApiModelProperty(value = "максимальная граница поиска по высоте", example = "7", required = true)
    private Integer maxY;

    public ColorAreaRequest() {
    }

    public ColorAreaRequest(Long image, Double colorDifference, Color color, Integer minX, Integer minY, Integer maxX, Integer maxY) {
        super(image, colorDifference, color);
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public Integer getMinX() {
        return minX;
    }

    public void setMinX(Integer minX) {
        this.minX = minX;
    }

    public Integer getMinY() {
        return minY;
    }

    public void setMinY(Integer minY) {
        this.minY = minY;
    }

    public Integer getMaxX() {
        return maxX;
    }

    public void setMaxX(Integer maxX) {
        this.maxX = maxX;
    }

    public Integer getMaxY() {
        return maxY;
    }

    public void setMaxY(Integer maxY) {
        this.maxY = maxY;
    }
}

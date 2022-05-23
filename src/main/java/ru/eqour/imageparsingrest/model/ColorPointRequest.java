package ru.eqour.imageparsingrest.model;

import io.swagger.annotations.ApiModelProperty;

public class ColorPointRequest extends ColorRequest {

    @ApiModelProperty(value = "координата пикселя по оси x", example = "104", required = true)
    private Integer x;
    @ApiModelProperty(value = "координата пикселя по оси y", example = "44", required = true)
    private Integer y;
    @ApiModelProperty(value = "радиус поиска соседних пикселей", example = "5", required = true)
    private Integer searchRadius;

    public ColorPointRequest() {
    }

    public ColorPointRequest(Long imageId, Double colorDifference, Integer x, Integer y, Integer searchRadius) {
        super(imageId, colorDifference);
        this.x = x;
        this.y = y;
        this.searchRadius = searchRadius;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getSearchRadius() {
        return searchRadius;
    }

    public void setSearchRadius(Integer searchRadius) {
        this.searchRadius = searchRadius;
    }
}

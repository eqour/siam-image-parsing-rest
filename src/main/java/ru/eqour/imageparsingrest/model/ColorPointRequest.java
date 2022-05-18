package ru.eqour.imageparsingrest.model;

public class ColorPointRequest extends ColorRequest {

    private Integer x;
    private Integer y;
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

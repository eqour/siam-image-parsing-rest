package ru.eqour.imageparsingrest.model;

public class ColorPointRequest extends ColorRequest {

    private Integer x;
    private Integer y;
    private Integer searchRadius;

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

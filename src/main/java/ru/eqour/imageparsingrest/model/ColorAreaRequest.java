package ru.eqour.imageparsingrest.model;

public class ColorAreaRequest extends ColorEntireRequest {

    private Integer minX;
    private Integer minY;
    private Integer maxX;
    private Integer maxY;

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

package ru.eqour.imageparsingrest.model;

public class ColorPointRequest extends ColorRequest {

    private int x;
    private int y;
    private int searchRadius;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSearchRadius() {
        return searchRadius;
    }

    public void setSearchRadius(int searchRadius) {
        this.searchRadius = searchRadius;
    }
}

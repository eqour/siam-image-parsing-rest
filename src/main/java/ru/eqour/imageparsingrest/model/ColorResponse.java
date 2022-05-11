package ru.eqour.imageparsingrest.model;

public class ColorResponse {

    private int[][] pixels;

    public ColorResponse() {}

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

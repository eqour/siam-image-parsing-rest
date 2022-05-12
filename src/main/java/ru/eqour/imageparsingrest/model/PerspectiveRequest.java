package ru.eqour.imageparsingrest.model;

import java.awt.image.BufferedImage;

public class PerspectiveRequest {

    private BufferedImage image;

    private int[][] points;

    private Integer outputWidth;

    private Integer outputHeight;

    public PerspectiveRequest() {
    }

    public int[][] getPoints() {
        return points;
    }

    public void setPoints(int[][] points) {
        this.points = points;
    }

    public Integer getOutputWidth() {
        return outputWidth;
    }

    public void setOutputWidth(Integer outputWidth) {
        this.outputWidth = outputWidth;
    }

    public Integer getOutputHeight() {
        return outputHeight;
    }

    public void setOutputHeight(Integer outputHeight) {
        this.outputHeight = outputHeight;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}

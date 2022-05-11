package ru.eqour.imageparsingrest.model;

import java.awt.image.BufferedImage;

public class ColorRequest {

    private BufferedImage image;
    private double colorDifference;

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public double getColorDifference() {
        return colorDifference;
    }

    public void setColorDifference(double colorDifference) {
        this.colorDifference = colorDifference;
    }
}

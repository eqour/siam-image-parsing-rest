package ru.eqour.imageparsingrest.model;

import java.awt.image.BufferedImage;

public class ColorRequest {

    private BufferedImage image;
    private Double colorDifference;

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public Double getColorDifference() {
        return colorDifference;
    }

    public void setColorDifference(Double colorDifference) {
        this.colorDifference = colorDifference;
    }
}

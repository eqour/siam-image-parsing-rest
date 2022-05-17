package ru.eqour.imageparsingrest.model;

import java.awt.*;

public class ColorEntireRequest extends ColorRequest {

    private Color color;

    public ColorEntireRequest() {
    }

    public ColorEntireRequest(Long image, Double colorDifference, Color color) {
        super(image, colorDifference);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

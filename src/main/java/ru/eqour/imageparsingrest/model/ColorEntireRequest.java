package ru.eqour.imageparsingrest.model;

import java.awt.*;

public class ColorEntireRequest extends ColorRequest {

    private Color color;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
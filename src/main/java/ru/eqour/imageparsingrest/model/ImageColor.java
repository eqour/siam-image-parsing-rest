package ru.eqour.imageparsingrest.model;

import io.swagger.annotations.ApiModelProperty;

import java.awt.*;

public class ImageColor {

    @ApiModelProperty(value = "красный цвет", example = "0", required = true)
    private int red;
    @ApiModelProperty(value = "зелёный цвет", example = "255", required = true)
    private int green;
    @ApiModelProperty(value = "синий цвет", example = "0", required = true)
    private int blue;

    public ImageColor() {
    }

    public ImageColor(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }

    public Color toColor() {
        return new Color(red, green, blue);
    }

    public static ImageColor fromColor(Color color) {
        return new ImageColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }
}

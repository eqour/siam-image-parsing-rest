package ru.eqour.imageparsingrest.model;

public class ColorEntireRequest extends ColorRequest {

    private ImageColor color;

    public ColorEntireRequest() {
    }

    public ColorEntireRequest(Long image, Double colorDifference, ImageColor color) {
        super(image, colorDifference);
        this.color = color;
    }

    public ImageColor getColor() {
        return color;
    }

    public void setColor(ImageColor color) {
        this.color = color;
    }
}

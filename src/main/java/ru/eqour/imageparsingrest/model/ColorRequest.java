package ru.eqour.imageparsingrest.model;

public class ColorRequest {

    private String base64Image;
    private double colorDifference;

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public double getColorDifference() {
        return colorDifference;
    }

    public void setColorDifference(double colorDifference) {
        this.colorDifference = colorDifference;
    }
}

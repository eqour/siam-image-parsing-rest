package ru.eqour.imageparsingrest.model;

public class ColorRequest {

    private Long imageId;
    private Double colorDifference;

    public ColorRequest() {
    }

    public ColorRequest(Long imageId, Double colorDifference) {
        this.imageId = imageId;
        this.colorDifference = colorDifference;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Double getColorDifference() {
        return colorDifference;
    }

    public void setColorDifference(Double colorDifference) {
        this.colorDifference = colorDifference;
    }
}

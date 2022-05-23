package ru.eqour.imageparsingrest.model;

import io.swagger.annotations.ApiModelProperty;

public class ColorRequest {

    @ApiModelProperty(value = "идентификатор изображения", example = "1", required = true)
    private Long imageId;
    @ApiModelProperty(value = "цветовой порог", example = "0", required = true)
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

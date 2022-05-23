package ru.eqour.imageparsingrest.model;

import io.swagger.annotations.ApiModelProperty;

public class ImageResponse {

    @ApiModelProperty(value = "идентификатор изображения")
    private long imageId;

    public ImageResponse() {
    }

    public ImageResponse(long imageId) {
        this.imageId = imageId;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }
}

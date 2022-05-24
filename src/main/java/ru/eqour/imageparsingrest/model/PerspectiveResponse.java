package ru.eqour.imageparsingrest.model;

import io.swagger.annotations.ApiModelProperty;

public class PerspectiveResponse {

    @ApiModelProperty(value = "идентификатор изображения")
    private long imageId;
    @ApiModelProperty(value = "изображение в формате base64")
    private String image;

    public PerspectiveResponse() {
    }

    public PerspectiveResponse(long imageId, String base64Image) {
        this.imageId = imageId;
        this.image = base64Image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }
}

package ru.eqour.imageparsingrest.model;

import io.swagger.annotations.ApiModelProperty;

public class ImageRequest {

    @ApiModelProperty(value = "изображение в формате base64", example = "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAIAAAACDbGyAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsIAAA7CARUoSoAAAAAjSURBVBhXY2T4z/CfkQEJ/IdgEAJiJoggHKDzQfrh4D8jAwDA6wkAOa0ILwAAAABJRU5ErkJggg==", required = true)
    private String image;

    public ImageRequest() {
    }

    public ImageRequest(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

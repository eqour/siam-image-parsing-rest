package ru.eqour.imageparsingrest.model;

public class ImageResponse {

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

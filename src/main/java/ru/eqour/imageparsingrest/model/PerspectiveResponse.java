package ru.eqour.imageparsingrest.model;

import java.awt.image.BufferedImage;

public class PerspectiveResponse {

    private long imageId;
    private BufferedImage image;

    public PerspectiveResponse() {
    }

    public PerspectiveResponse(long imageId, BufferedImage base64Image) {
        this.imageId = imageId;
        this.image = base64Image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }
}

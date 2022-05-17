package ru.eqour.imageparsingrest.model;

import java.awt.image.BufferedImage;

public class ImageRequest {

    private BufferedImage image;

    public ImageRequest() {
    }

    public ImageRequest(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}

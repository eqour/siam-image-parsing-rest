package ru.eqour.imageparsingrest.model;

import java.awt.image.BufferedImage;

public class PerspectiveResponse {

    private BufferedImage image;

    public PerspectiveResponse() {}

    public PerspectiveResponse(BufferedImage base64Image) {
        this.image = base64Image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}

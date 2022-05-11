package ru.eqour.imageparsingrest.model;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;

public class PerspectiveRequest {

    @NotNull
    private BufferedImage image;

    @NotNull
    private int[][] points;

    @NotNull
    @Range(min = 1, max = 65536, message = "value must be between 1 and 65536")
    private int outputWidth;

    @NotNull
    @Range(min = 1, max = 65536, message = "value must be between 1 and 65536")
    private int outputHeight;

    public PerspectiveRequest() {
    }

    public int[][] getPoints() {
        return points;
    }

    public void setPoints(int[][] points) {
        this.points = points;
    }

    public int getOutputWidth() {
        return outputWidth;
    }

    public void setOutputWidth(int outputWidth) {
        this.outputWidth = outputWidth;
    }

    public int getOutputHeight() {
        return outputHeight;
    }

    public void setOutputHeight(int outputHeight) {
        this.outputHeight = outputHeight;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}

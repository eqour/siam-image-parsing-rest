package ru.eqour.imageparsingrest.model;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PerspectiveRequest {

    @NotBlank
    private String base64Image;

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

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
}

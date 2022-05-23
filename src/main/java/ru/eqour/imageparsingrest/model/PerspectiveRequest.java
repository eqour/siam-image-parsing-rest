package ru.eqour.imageparsingrest.model;

import io.swagger.annotations.ApiModelProperty;

import java.awt.image.BufferedImage;

public class PerspectiveRequest {

    private BufferedImage image;
    @ApiModelProperty(value = "координаты точек", example = "[[5, 7],[4, 8],[6, 2]]", required = true)
    private int[][] points;
    @ApiModelProperty(value = "ширина результирующего изображения", example = "1920", required = true)
    private Integer outputWidth;
    @ApiModelProperty(value = "высота результирующего изображения", example = "1080", required = true)
    private Integer outputHeight;

    public PerspectiveRequest() {
    }

    public PerspectiveRequest(BufferedImage image, int[][] points, Integer outputWidth, Integer outputHeight) {
        this.image = image;
        this.points = points;
        this.outputWidth = outputWidth;
        this.outputHeight = outputHeight;
    }

    public int[][] getPoints() {
        return points;
    }

    public void setPoints(int[][] points) {
        this.points = points;
    }

    public Integer getOutputWidth() {
        return outputWidth;
    }

    public void setOutputWidth(Integer outputWidth) {
        this.outputWidth = outputWidth;
    }

    public Integer getOutputHeight() {
        return outputHeight;
    }

    public void setOutputHeight(Integer outputHeight) {
        this.outputHeight = outputHeight;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}

package ru.eqour.imageparsingrest.model;

import io.swagger.annotations.ApiModelProperty;

public class PerspectiveRequest {

    @ApiModelProperty(value = "изображение в формате base64", example = "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAIAAAACDbGyAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsIAAA7CARUoSoAAAAAjSURBVBhXY2T4z/CfkQEJ/IdgEAJiJoggHKDzQfrh4D8jAwDA6wkAOa0ILwAAAABJRU5ErkJggg==", required = true)
    private String image;
    @ApiModelProperty(value = "координаты четырёхугольника, описывающего преобразованное изображение", notes = "координаты перечисляются в порядке с левого верхнего угла по часовой стрелке", example = "[[0, 1],[4, 0],[4, 4],[0, 3]]", required = true)
    private int[][] points;
    @ApiModelProperty(value = "ширина преобразованного изображения", example = "5", required = true)
    private Integer outputWidth;
    @ApiModelProperty(value = "высота преобразованного изображения", example = "5", required = true)
    private Integer outputHeight;

    public PerspectiveRequest() {
    }

    public PerspectiveRequest(String image, int[][] points, Integer outputWidth, Integer outputHeight) {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

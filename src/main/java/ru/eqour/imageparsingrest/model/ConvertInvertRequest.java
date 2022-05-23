package ru.eqour.imageparsingrest.model;

import io.swagger.annotations.ApiModelProperty;

public class ConvertInvertRequest extends ConvertRequest {

    @ApiModelProperty(value = "инвертировать значения точек по оси x", example = "true", required = true)
    private Boolean invertByX;
    @ApiModelProperty(value = "инвертировать значения точек по оси y", example = "false", required = true)
    private Boolean invertByY;

    public ConvertInvertRequest() {
    }

    public ConvertInvertRequest(double[][] points, Boolean invertByX, Boolean invertByY) {
        super(points);
        this.invertByX = invertByX;
        this.invertByY = invertByY;
    }

    public Boolean getInvertByX() {
        return invertByX;
    }

    public void setInvertByX(Boolean invertByX) {
        this.invertByX = invertByX;
    }

    public Boolean getInvertByY() {
        return invertByY;
    }

    public void setInvertByY(Boolean invertByY) {
        this.invertByY = invertByY;
    }
}

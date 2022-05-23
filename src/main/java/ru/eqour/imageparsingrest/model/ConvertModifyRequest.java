package ru.eqour.imageparsingrest.model;

import io.swagger.annotations.ApiModelProperty;

public class ConvertModifyRequest extends ConvertRequest {

    @ApiModelProperty(value = "коэффициент изменения координат по оси x", example = "0.5", required = true)
    private Double dx;
    @ApiModelProperty(value = "коэффициент изменения координат по оси y", example = "-1.5", required = true)
    private Double dy;

    public ConvertModifyRequest() {
    }

    public ConvertModifyRequest(double[][] points, Double dx, Double dy) {
        super(points);
        this.dx = dx;
        this.dy = dy;
    }

    public Double getDx() {
        return dx;
    }

    public void setDx(Double dx) {
        this.dx = dx;
    }

    public Double getDy() {
        return dy;
    }

    public void setDy(Double dy) {
        this.dy = dy;
    }
}

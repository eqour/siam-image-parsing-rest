package ru.eqour.imageparsingrest.model;

import io.swagger.annotations.ApiModelProperty;

public class ConvertAreaRequest extends ConvertRequest {

    @ApiModelProperty(value = "карйняя начальная координата исходной области по оси x", example = "0", required = true, position = 1)
    private Double srcSX;
    @ApiModelProperty(value = "карйняя начальная координата исходной области по оси y", example = "0", required = true, position = 2)
    private Double srcSY;
    @ApiModelProperty(value = "карйняя конечная координата исходной области по оси x", example = "5", required = true, position = 3)
    private Double srcEX;
    @ApiModelProperty(value = "карйняя конечная координата исходной области по оси y", example = "5", required = true, position = 4)
    private Double srcEY;
    @ApiModelProperty(value = "карйняя начальная координата преобразованной области по оси x", example = "10", required = true, position = 5)
    private Double dstSX;
    @ApiModelProperty(value = "карйняя начальная координата преобразованной области по оси y", example = "10", required = true, position = 6)
    private Double dstSY;
    @ApiModelProperty(value = "карйняя конечная координата преобразованной области по оси x", example = "20", required = true, position = 7)
    private Double dstEX;
    @ApiModelProperty(value = "карйняя конечная координата преобразованной области по оси y", example = "20", required = true, position = 8)
    private Double dstEY;

    public ConvertAreaRequest() {
    }

    public ConvertAreaRequest(double[][] points,
                              Double srcSX, Double srcSY, Double srcEX, Double srcEY,
                              Double dstSX, Double dstSY, Double dstEX, Double dstEY) {
        super(points);
        this.srcSX = srcSX;
        this.srcSY = srcSY;
        this.srcEX = srcEX;
        this.srcEY = srcEY;
        this.dstSX = dstSX;
        this.dstSY = dstSY;
        this.dstEX = dstEX;
        this.dstEY = dstEY;
    }

    public Double getSrcSX() {
        return srcSX;
    }

    public void setSrcSX(Double srcSX) {
        this.srcSX = srcSX;
    }

    public Double getSrcSY() {
        return srcSY;
    }

    public void setSrcSY(Double srcSY) {
        this.srcSY = srcSY;
    }

    public Double getSrcEX() {
        return srcEX;
    }

    public void setSrcEX(Double srcEX) {
        this.srcEX = srcEX;
    }

    public Double getSrcEY() {
        return srcEY;
    }

    public void setSrcEY(Double srcEY) {
        this.srcEY = srcEY;
    }

    public Double getDstSX() {
        return dstSX;
    }

    public void setDstSX(Double dstSX) {
        this.dstSX = dstSX;
    }

    public Double getDstSY() {
        return dstSY;
    }

    public void setDstSY(Double dstSY) {
        this.dstSY = dstSY;
    }

    public Double getDstEX() {
        return dstEX;
    }

    public void setDstEX(Double dstEX) {
        this.dstEX = dstEX;
    }

    public Double getDstEY() {
        return dstEY;
    }

    public void setDstEY(Double dstEY) {
        this.dstEY = dstEY;
    }
}

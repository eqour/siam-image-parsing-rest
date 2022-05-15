package ru.eqour.imageparsingrest.model;

public class ConvertAreaRequest extends ConvertRequest {

    private Double srcSX;
    private Double srcSY;
    private Double srcEX;
    private Double srcEY;
    private Double dstSX;
    private Double dstSY;
    private Double dstEX;
    private Double dstEY;

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

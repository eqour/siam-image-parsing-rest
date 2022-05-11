package ru.eqour.imageparsingrest.model;

public class ConvertRequest {

    private double[][] points;
    private double srcSX;
    private double srcSY;
    private double srcEX;
    private double srcEY;
    private double dstSX;
    private double dstSY;
    private double dstEX;
    private double dstEY;

    public double[][] getPoints() {
        return points;
    }

    public void setPoints(double[][] points) {
        this.points = points;
    }

    public double getSrcSX() {
        return srcSX;
    }

    public void setSrcSX(double srcSX) {
        this.srcSX = srcSX;
    }

    public double getSrcSY() {
        return srcSY;
    }

    public void setSrcSY(double srcSY) {
        this.srcSY = srcSY;
    }

    public double getSrcEX() {
        return srcEX;
    }

    public void setSrcEX(double srcEX) {
        this.srcEX = srcEX;
    }

    public double getSrcEY() {
        return srcEY;
    }

    public void setSrcEY(double srcEY) {
        this.srcEY = srcEY;
    }

    public double getDstSX() {
        return dstSX;
    }

    public void setDstSX(double dstSX) {
        this.dstSX = dstSX;
    }

    public double getDstSY() {
        return dstSY;
    }

    public void setDstSY(double dstSY) {
        this.dstSY = dstSY;
    }

    public double getDstEX() {
        return dstEX;
    }

    public void setDstEX(double dstEX) {
        this.dstEX = dstEX;
    }

    public double getDstEY() {
        return dstEY;
    }

    public void setDstEY(double dstEY) {
        this.dstEY = dstEY;
    }
}

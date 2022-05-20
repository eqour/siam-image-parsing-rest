package ru.eqour.imageparsingrest.model;

public class ConvertModifyRequest extends ConvertRequest {

    private Double dx;
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

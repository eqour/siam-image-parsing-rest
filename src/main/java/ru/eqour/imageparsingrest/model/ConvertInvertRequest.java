package ru.eqour.imageparsingrest.model;

public class ConvertInvertRequest extends ConvertRequest {

    private Boolean invertByX;
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

package ru.eqour.imageparsingrest.model;

public class ConvertInvertRequest extends ConvertRequest {

    private Boolean invertByX;
    private Boolean invertByY;

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

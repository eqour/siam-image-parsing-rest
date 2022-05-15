package ru.eqour.imageparsingrest.model;

public class ConvertInvertAxisRequest extends ConvertRequest {

    private Double invertedAxisPosition;

    public Double getInvertedAxisPosition() {
        return invertedAxisPosition;
    }

    public void setInvertedAxisPosition(Double invertedAxisPosition) {
        this.invertedAxisPosition = invertedAxisPosition;
    }
}

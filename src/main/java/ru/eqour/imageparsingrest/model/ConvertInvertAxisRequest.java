package ru.eqour.imageparsingrest.model;

public class ConvertInvertAxisRequest extends ConvertRequest {

    private Double invertedAxisPosition;

    public ConvertInvertAxisRequest() {
    }

    public ConvertInvertAxisRequest(double[][] points, Double invertedAxisPosition) {
        super(points);
        this.invertedAxisPosition = invertedAxisPosition;
    }

    public Double getInvertedAxisPosition() {
        return invertedAxisPosition;
    }

    public void setInvertedAxisPosition(Double invertedAxisPosition) {
        this.invertedAxisPosition = invertedAxisPosition;
    }
}

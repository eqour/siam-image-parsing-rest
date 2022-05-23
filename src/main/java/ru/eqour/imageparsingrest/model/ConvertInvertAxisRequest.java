package ru.eqour.imageparsingrest.model;

import io.swagger.annotations.ApiModelProperty;

public class ConvertInvertAxisRequest extends ConvertRequest {

    @ApiModelProperty(value = "новая позиция оси", example = "5.0", required = true)
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

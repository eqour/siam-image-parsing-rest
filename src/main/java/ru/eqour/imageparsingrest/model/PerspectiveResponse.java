package ru.eqour.imageparsingrest.model;

public class PerspectiveResponse {

    private String base64Image;

    public PerspectiveResponse() {
    }

    public PerspectiveResponse(String base64Image) {
        this.base64Image = base64Image;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
}

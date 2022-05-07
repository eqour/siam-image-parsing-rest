package ru.eqour.imageparsingrest.exception;

import java.util.Map;

public class ErrorsResponse {

    private Map<String, String> errors;

    public ErrorsResponse() {
    }

    public ErrorsResponse(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

}

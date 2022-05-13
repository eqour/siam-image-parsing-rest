package ru.eqour.imageparsingrest.exception;

import java.util.Map;

public class BadRequestResponse extends ErrorResponse {
    private Map<String, String> fields;

    public BadRequestResponse() {}

    public BadRequestResponse(String message, Map<String, String> fields) {
        super(message);
        this.fields = fields;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }
}

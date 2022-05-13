package ru.eqour.imageparsingrest.exception;

public class ConvertImageException extends RuntimeException {
    public ConvertImageException(String message) {
        super(message);
    }

    public ConvertImageException(String message, Throwable cause) {
        super(message, cause);
    }
}

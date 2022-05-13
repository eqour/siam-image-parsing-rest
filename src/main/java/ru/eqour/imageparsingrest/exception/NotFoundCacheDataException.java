package ru.eqour.imageparsingrest.exception;

public class NotFoundCacheDataException extends RuntimeException {
    public NotFoundCacheDataException(String message) {
        super(message);
    }
}

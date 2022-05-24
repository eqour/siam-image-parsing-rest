package ru.eqour.imageparsingrest.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ExtendedErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {

        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
        Throwable error = super.getError(webRequest);

        if (error instanceof NotFoundCacheDataException || error instanceof ConvertImageException)
            errorAttributes.put("message", error.getMessage());

        if (error instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException)error;
            errorAttributes.put("message", "некорректный запрос");
            errorAttributes.put("fields", ex.getBindingResult().getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField, e -> e.getDefaultMessage() == null ? "" : e.getDefaultMessage())));
        }

        return errorAttributes;
    }

}

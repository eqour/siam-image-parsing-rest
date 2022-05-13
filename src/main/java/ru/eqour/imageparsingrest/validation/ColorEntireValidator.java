package ru.eqour.imageparsingrest.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.eqour.imageparsingrest.model.ColorEntireRequest;

@Component
public class ColorEntireValidator extends ColorValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ColorEntireRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        super.validate(target, errors);

        ColorEntireRequest request = (ColorEntireRequest) target;

        if (request.getColor() == null)
            errors.rejectValue("color", "", "значение не должно быть null");
    }
}

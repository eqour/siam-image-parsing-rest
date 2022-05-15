package ru.eqour.imageparsingrest.validation;

import org.springframework.validation.Errors;
import ru.eqour.imageparsingrest.model.ConvertInvertRequest;

public class ConvertInvertValidator extends ConvertValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ConvertInvertRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        super.validate(target, errors);

        ConvertInvertRequest request = (ConvertInvertRequest) target;

        if (request.getInvertByX() == null) {
            errors.rejectValue("invertByX", "", "значение не должно быть null");
        }

        if (request.getInvertByY() == null) {
            errors.rejectValue("invertByY", "", "значение не должно быть null");
        }
    }
}

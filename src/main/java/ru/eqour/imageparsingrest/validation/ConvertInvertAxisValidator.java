package ru.eqour.imageparsingrest.validation;

import org.springframework.validation.Errors;
import ru.eqour.imageparsingrest.model.ConvertInvertAxisRequest;

public class ConvertInvertAxisValidator extends ConvertValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ConvertInvertAxisRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        super.validate(target, errors);

        ConvertInvertAxisRequest request = (ConvertInvertAxisRequest) target;

        if (request.getInvertedAxisPosition() == null) {
            errors.rejectValue("invertedAxisPosition", "", "значение не должно быть null");
        }
    }
}
